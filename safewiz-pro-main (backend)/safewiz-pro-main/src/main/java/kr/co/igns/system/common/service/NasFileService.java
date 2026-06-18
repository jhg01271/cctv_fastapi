package kr.co.igns.system.common.service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.file.CommonFileUtils;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.service.dto.SpFileRequestDto;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.utils.SmbFiles;

import kr.co.igns.system.setting.dao.FemsFileRepository;
import kr.co.igns.system.setting.dao.FileTemplateRepository;
import kr.co.igns.system.common.dao.postgres.FileDAO;
import kr.co.igns.system.common.entity.SpFileEntity;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.util.FileUtil;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.utils.file.FileDelete;
import kr.co.igns.framework.utils.file.UploadParameters;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@Service

@RequiredArgsConstructor
public class NasFileService {
    @Value("${file.nas.fo-path}")
    private String foPath;

    private final FemsFileRepository femsFileRepository;
    private final FileTemplateRepository fileTemplateRepository;
    private final FileProperty property;
    private final FileUtil util;
    private final FileDAO fileDAO;


    private static final Logger log = LogManager.getLogger("com.project");
    // 2026.01.12 BHJ Property 설정 확인 용    
    @PostConstruct
    public void init() {
    	log.info("");
    	log.info("file.encoding=" + System.getProperty("file.encoding"));
    	log.info("sun.jnu.encoding=" + System.getProperty("sun.jnu.encoding"));
    	log.info("defaultCharset=" + java.nio.charset.Charset.defaultCharset());
        
    	log.info("target=" + property.getTarget());
    	log.info("uploadDir=" + property.getUploadDir());
    	log.info("nasFoPath=" + property.getNas().getFoPath());
    	log.info("nasShareFolder=" + property.getNas().getShareFolder());
    	
    	Path sourceDir = Paths.get("\\\\" + property.getNas().getIp() + "\\", property.getNas().getShareFolder(), foPath); // 원본 디렉토리
    	log.info(sourceDir.toAbsolutePath());
    	log.info(java.nio.file.Files.exists(sourceDir));
    }

    public void download(HttpServletRequest request, HttpServletResponse response, String fileId) throws UnsupportedEncodingException {
        // fileId로 파일데이터 불러오기
        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);

        String filePath = fileEntity.getFilePath() + File.separator + fileEntity.getSaveNm() + "." + fileEntity.getExtension();
        boolean isAlreadyZip = "zip".equalsIgnoreCase(fileEntity.getExtension());
        boolean shouldUseZip = fileEntity.getFileSize() > 10 * 1024 * 1024 && !isAlreadyZip;
        if (shouldUseZip) {
            // 10MB가 넘는 zip 파일일 경우
            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".zip";
        }
        System.out.println("######### property "+ foPath );
        System.out.println("######### property "+ property.getNas().getFoPath() );
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();
        InputStream in = null;
        OutputStream out = null;

        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);
            // 해당 파일 경로를 통해 파일이 존재하지 않은 경우 예외처리
            if (share.fileExists(filePath) == false)
                throw new CUserDefinedException("File doesn't exist");

            com.hierynomus.smbj.share.File smbFile = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null,
                    SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);

            // DB에 저장된 파일명으로 다운로드 되도록 수정
            String fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + "." + fileEntity.getExtension();
            ;
            if (shouldUseZip) {
                // 10MB가 넘는 zip 파일일 경우
                fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + ".zip";
            }
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setHeader("X-Content-Download", String.valueOf(false));
//            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString());
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            in = new BufferedInputStream(smbFile.getInputStream());
            out = new BufferedOutputStream(response.getOutputStream());

            FileCopyUtils.copy(in, out);
        } catch (Exception e) {
            response.setHeader("CustomHeader", e.getMessage());
        } finally {
            if (in != null)
                try {
                    out.flush();
                    in.close();
                    client.close();
                } catch (Exception ignored) {

                }
        }
    }

    //파일 양식 다운로드
    public void downloadFileTemplate(HttpServletRequest request, HttpServletResponse response, String id) {
        // fileId로 파일데이터 불러오기
        FileTemplateEntity fileEntity = fileTemplateRepository.findById(id);

        String filePath = fileEntity.getFilePath() + "." + fileEntity.getExtension();

        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();
        InputStream in = null;
        OutputStream out = null;

        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);
            // 해당 파일 경로를 통해 파일이 존재하지 않은 경우 예외처리
            if (share.fileExists(filePath) == false)
                throw new CUserDefinedException("File doesn't exist");

            com.hierynomus.smbj.share.File smbFile = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null,
                    SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            //response.setContentLength(fileEntity.getFileSize());

            in = new BufferedInputStream(smbFile.getInputStream());
            out = new BufferedOutputStream(response.getOutputStream());

            FileCopyUtils.copy(in, out);
        } catch (Exception e) {
            response.setHeader("CustomHeader", e.getMessage());
        } finally {
            if (in != null)
                try {
                    out.flush();
                    in.close();
                    client.close();
                } catch (Exception ignored) {

                }
        }
    }

    public InputStream getNasFileInputStream(String fileId) {
        // fileId로 파일데이터 불러오기
        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);

        String filePath = fileEntity.getFilePath() + File.separator + fileEntity.getSaveNm() + "." + fileEntity.getExtension();

        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();
        InputStream in = null;
        OutputStream out = null;

        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);
            // 해당 파일 경로를 통해 파일이 존재하지 않은 경우 예외처리
            if (share.fileExists(filePath) == false)
                throw new CUserDefinedException("File doesn't exist");

            com.hierynomus.smbj.share.File smbFile = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null,
                    SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString());

            File file = CommonFileUtils.convertInputStreamToFile(smbFile.getInputStream());
            in = new FileInputStream(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 나스 접속 닫기
            client.close();
        }
        return in;
    }

    public void downloadImg(HttpServletRequest request, HttpServletResponse response, String fileId) {
        if (fileId == null || fileId.isEmpty())
            return;

        // fileId로 파일데이터 불러오기
        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);

        if (fileEntity == null) return;
        String filePath = fileEntity.getFilePath() + File.separator + fileEntity.getSaveNm() + "." + fileEntity.getExtension();
        boolean isAlreadyZip = "zip".equalsIgnoreCase(fileEntity.getExtension());
        boolean shouldUseZip = fileEntity.getFileSize() > 10 * 1024 * 1024 && !isAlreadyZip;
        // 10MB가 넘는 zip 파일일 경우
        if (shouldUseZip) {
            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".zip";
        }
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();
        InputStream in = null;
        OutputStream out = null;

        SMBClient client = new SMBClient();


        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);
            String fileNameWithoutExt = filePath.substring(0, filePath.lastIndexOf('.'));
            // 해당 파일 경로를 통해 파일이 존재하지 않은 경우 예외처리
            if (!share.fileExists(filePath)) {
                throw new CUserDefinedException("파일이 존재하지 않습니다.");
            }

            com.hierynomus.smbj.share.File smbFile = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null,
                    SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
            String fileName = fileEntity.getSaveNm() + "." + fileEntity.getExtension();
//
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");//
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setHeader("name", encodedFileName);
//            response.setHeader("File-Size", String.valueOf(fileSize));
            response.setContentType("application/octet-stream");
            response.setHeader("image", "Y");
            String extension = (fileEntity.getExtension() == null || fileEntity.getExtension().isEmpty())
                    ? null
                    : fileEntity.getExtension();

            response.setHeader("extension", extension);

            in = new BufferedInputStream(smbFile.getInputStream());
            out = new BufferedOutputStream(response.getOutputStream());

            FileCopyUtils.copy(in, out);
        } catch (CUserDefinedException cue) {
            throw cue; // 원래 메시지를 유지
        } catch (Exception e) {
//            response.setHeader("CustomHeader", e.getMessage());
            throw new CUserDefinedException("파일서버와의 연결이 불안정합니다.");
        } finally {
            if (in != null)
                try {
                    out.flush();
                    in.close();
                    client.close();
                } catch (Exception ignored) {

                }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public String upload(UploadParameters parameters) throws Exception {   	

        String basicPath = "";
        if ("local".equals(property.getTarget())) {
            basicPath = property.getUploadDir();
        } else if ("nas".equals(property.getTarget())) {
            basicPath = property.getNas().getFoPath();
        }

        MultipartFile file = parameters.getMultipartFile();
        String fileName = file.getOriginalFilename();
        String fileNameRemoveExtension = FilenameUtils.removeExtension(fileName);
        String[] fileNameSplit = fileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1].toLowerCase();
        String path = "";
        if ("system".equals(parameters.getFileType())) {
            path = Paths.get(basicPath, "system", parameters.getTargetType(), parameters.getTargetId()).toString();
        } else if ("base".equals(parameters.getFileType())) {
            path = Paths.get(basicPath, parameters.getCompId(), parameters.getTargetType(), parameters.getTargetId()).toString();
        } else {
            path = Paths.get(basicPath, parameters.getCompId(), parameters.getTargetId().substring(0,4),parameters.getTargetType(), parameters.getTargetId()).toString();
        }
        String type = util.getFileType(extension); // 파일 타입
        Long size = file.getSize();

        // 파일명 생성
        HashMap<String, String> saveFile = util.getFileName(fileName, parameters);
        String saveName = saveFile.get("filename") + "-" + UUID.randomUUID().toString();

        File tempFolder = CreateTempFolder(); // 임시폴더 생성

        // 압축 여부 판단
        boolean isAlreadyZip = "zip".equals(extension);
        boolean shouldZip = size >= 10_000_000 && !isAlreadyZip;

        // 저장 파일명
        String fullFileName = tempFolder.getPath() + File.separator + saveName + "." + (shouldZip ? "zip" : extension);

        // 파일 크기가 10MB 이상인 경우 .zip 형태로 저장
        if (shouldZip) {
            String saveFileName = saveName + "." + extension;
            ConvertInputStreamToZipFile(file.getInputStream(), file, fullFileName, saveFileName);
        } else {
            ConvertInputStreamToFile(file.getInputStream(), fullFileName);
        }
        try {
            copyFolderToSmb(tempFolder, path);
        } catch (Exception e) {
            throw new CUserDefinedException("파일복사에 실패했습니다.");
        }

        SpFileEntity entity = SpFileEntity.builder()
                .fileNm(fileNameRemoveExtension)
                .saveNm(saveName)
                .extension(extension)
                .fileType(type)
                .fileSize(size)
                .filePath(path)
                .useYn("Y")
                .delYn("N")
                .compId(parameters.getCompId())
                .targetId(parameters.getTargetId())
                .targetType(parameters.getTargetType())
                .createdBy(SecurityUtil.getCurrentMemberId())
                .build();
        SpFileEntity savedEntity = femsFileRepository.save(entity);

        return savedEntity.getFileId();
    }

    // 파일 위치 이동
    @Transactional(rollbackFor = Throwable.class)
    public void moveFileDir(String targetType, String targetId, String originCompId, String targetCompId)
            throws Exception {
        String basicPath = "";
        if ("local".equals(property.getTarget())) {
            basicPath = property.getUploadDir();

        } else if ("nas".equals(property.getTarget())) {
            basicPath = property.getNas().getFoPath();
        }

        Path sourceDir = Paths.get("\\\\" + property.getNas().getIp() + "\\", "FileServer", basicPath, originCompId, targetType, targetId); // 원본 디렉토리
        Path targetDir = Paths.get("\\\\" + property.getNas().getIp() + "\\", "FileServer", basicPath, targetCompId, targetType, targetId); // 이동할 디렉토리
        if (originCompId.equals(targetCompId)) return;
        // 타겟 디렉토리 없으면 생성
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        // 파일 이동
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Path targetFile = targetDir.resolve(file.getFileName());
                    Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    List<FileVO> originFile = fileDAO.getFileList(targetId, targetType);
                    if (!originFile.isEmpty()) {
                        // 테이블 업데이트
                        String filePath = Paths.get(basicPath, targetCompId, targetType, targetId).toString();
                        fileDAO.updateMoveFile(targetCompId, originFile.get(0).getFileId(), filePath);
                    }
                }
            }
        }

        // 기존 경로 제거
        if (Files.exists(sourceDir) && Files.isDirectory(sourceDir)) {
            // 디렉토리 내부 파일 먼저 삭제
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                }
            }
            // 디렉토리 삭제
            Files.deleteIfExists(sourceDir);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByList(List<SpFileRequestDto> dtoList) throws Exception {
        for (SpFileRequestDto dto : dtoList) {
            if (StringUtils.isNotEmpty(dto.getFileId())) {
                deleteById(dto.getFileId());
            } else {
                deleteByTarget(dto);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(String fileId) throws Exception {
        // fileId로 해당 파일 정보 조회
        SpFileEntity file = femsFileRepository.findByFileId(fileId);
        delete(file);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByTarget(SpFileRequestDto dto) throws Exception {
        // 타켓 정보로 해당 파일 정보 조회
        List<SpFileEntity> files = femsFileRepository.findByTargetIdAndTargetTypeAndCompId(dto.getTargetId(), dto.getTargetType(), dto.getCompId());
        for (SpFileEntity file : files) {
            delete(file);
        }
    }

    public void delete(SpFileEntity file) throws Exception {
        // 서버 & 테이블 제거 여부
        if (property.isAllowDelete()) {
            if (file != null) {
                String filePullPath = file.getFilePath() + File.separator + file.getSaveNm() + "." + file.getExtension();
                if (file.getFileSize() > 10 * 1024 * 1024) {
                    // 10MB가 넘는 zip 파일일 경우
                    filePullPath = filePullPath.substring(0, filePullPath.lastIndexOf('.')) + ".zip";
                }
                File fileInfo = new File(filePullPath);
                FileDelete fileDeleteThread = new FileDelete(filePullPath);
                fileDeleteThread.run();
                femsFileRepository.delete(file);
                //File fileInfo = new File(Paths.get(property.getNas().getFoPath(), filePullPath).toString());

                deleteFileFromSmb(filePullPath);
            }
        } else {
            SpFileEntity entity = SpFileEntity.builder()
                    .fileId(file.getFileId())//
                    .compId(file.getCompId())//
                    .fileType(file.getFileType())//
                    .filePath(file.getFilePath())//
                    .fileNm(file.getFileNm())//
                    .fileSize(file.getFileSize())//
                    .extension(file.getExtension())//
                    .saveNm(file.getSaveNm())//
                    .targetId(file.getTargetId())//
                    .targetType(file.getTargetType())//
                    .useYn(file.getUseYn())//
                    .delYn("Y").build();

            femsFileRepository.save(entity);
        }
    }

    private File CreateTempFolder() throws IOException {
        File file = File.createTempFile("~gns", "dir");
        file.delete();
        file.mkdir();

        return file;
    }

    /**
     * .zip 이외 파일 저장
     *
     * @param InputStream, String
     * @return File
     * @throws FileNotFoundException, IOException
     */
    private File ConvertInputStreamToFile(InputStream inputStream, String fileName)
            throws FileNotFoundException, IOException {
        File file = new File(fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        return file;
    }

    /**
     * .zip 파일 압축후 저장
     *
     * @param InputStream, MultipartFile, String, String
     * @return File
     * @throws FileNotFoundException, Exception
     */
    private File ConvertInputStreamToZipFile(InputStream inputStream, MultipartFile file, String zipFileName,
                                             String saveFileName) throws FileNotFoundException, IOException {
        File zipFile = new File(zipFileName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
             ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes())) {
            zos.putNextEntry(new ZipEntry(saveFileName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bais.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    private void copyFolderToSmb(File tempFolder, String outFolder) throws Exception {
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();

        SMBClient client = new SMBClient();

        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare(sharedFolder)) {
                createFolderRecursive(share, outFolder);
                copyFolderToSmbBase(share, tempFolder, outFolder);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            client.close();
        }
    }

    private void createFolderRecursive(DiskShare share, String outFolder) {
        File file = new File(outFolder);

        if (file.getParentFile() != null)
            createFolderRecursive(share, file.getParent());

        if (!share.folderExists(file.getPath()))
            share.mkdir(file.getPath());
    }

    private void copyFolderToSmbBase(DiskShare share, File tempFolder, String outFolder) throws IOException {
        File files[] = tempFolder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (!share.folderExists(outFolder + File.separator + file.getName()))
                    share.mkdir(outFolder + File.separator + file.getName());
                copyFolderToSmbBase(share, new File(file.getPath()),
                        outFolder + File.separator + file.getName());
            }
            SmbFiles.copy(file, share, outFolder + File.separator + file.getName(), true);
        }
    }

    private void deleteFileFromSmb(String filePath) throws Exception {
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();

        SMBClient client = new SMBClient();

        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare(sharedFolder)) {
                // 파일 삭제
                if (share.fileExists(filePath)) {
                    // 파일을 삭제합니다.
                    share.rm(filePath); // rm 메소드를 사용하여 파일 삭제
                    System.out.println("파일이 성공적으로 삭제되었습니다: " + filePath);
                } else {
                    System.out.println("파일이 존재하지 않습니다: " + filePath);
                }
            }
        } catch (Exception e) {
            throw new CUserDefinedException("파일 삭제에 실패했습니다.", e);
        } finally {
            client.close();
        }
    }
}
