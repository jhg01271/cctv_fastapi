package kr.co.igns.framework.api.file.service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import kr.co.igns.framework.api.file.dto.FileRequestDto;
//import kr.co.igns.framework.api.file.dto.FileResponseDto;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.util.FileUtil;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.file.CommonFileUtils;
import kr.co.igns.framework.utils.file.UploadParameters;
import kr.co.igns.system.common.dao.postgres.FileDAO;
import kr.co.igns.system.common.entity.SpFileEntity;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.service.NasFileService;

import kr.co.igns.system.common.service.dto.SpFileRequestDto;
import kr.co.igns.system.common.service.dto.SpFileResponseDto;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.FemsFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

//import kr.co.igns.framework.api.file.entity.FileEntity;
//import kr.co.igns.framework.api.file.entity.FileRepository;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.utils.file.FileDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    //    private final FileRepository fileRepository;
    private final FemsFileRepository femsFileRepository;
    private final FileProperty property;
    private final FileUtil util;
    private final FileDAO fileDAO;

    private final NasFileService nasFileService;

    // 저장된 파일 불러오기
    public List<SpFileResponseDto> search(UploadParameters parameter) throws Exception {
        // target, 사업장 정보로 삭제되지 않은 file들 조회
        List<FileVO> fileList = fileDAO.getFileListById(parameter.getFileId());
        String targetType = fileList.get(0).getTargetType();
        String targetId = fileList.get(0).getTargetId();

        List<SpFileEntity> entities = femsFileRepository.findByTargetTypeAndTargetIdAndDelYn(targetType, targetId, "N");
        // 계층간 데이터 이동을 위해 dto로 변환
        List<SpFileResponseDto> files = new ArrayList<>();
        for (SpFileEntity entity : entities) {

            SpFileResponseDto file = (SpFileResponseDto) SpUtils.objectMap(entity, SpFileResponseDto.class);
//			FemsFileResponseDto file = new FemsFileResponseDto(entity);
            files.add(file);
        }
        return files;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByList(List<SpFileRequestDto> dtoList) {
        for (SpFileRequestDto dto : dtoList) {
            if (StringUtils.isNoneEmpty(dto.getFileId())) {
                deleteById(dto.getFileId());
            } else {
                deleteByTarget(dto);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByTarget(SpFileRequestDto dto) {
        // 타켓 정보로 해당 파일 정보 조회
        List<SpFileEntity> files = femsFileRepository.findByTargetIdAndTargetTypeAndCompId(dto.getTargetId(), dto.getTargetType(), dto.getCompId());
        for (SpFileEntity file : files) {
            delete(file);
        }
    }

    public List<FileVO> getFileList(String targetId, String targetType) {
        List<FileVO> files = fileDAO.getFileList(targetId, targetType);
        return files;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(String fileId) {
        // fileId로 해당 파일 정보 조회
        SpFileEntity file = femsFileRepository.findByFileId(fileId);
        delete(file);
    }

    public void delete(SpFileEntity file) {
        // 서버 & 테이블 제거 여부
        if (property.isAllowDelete()) {
            if (file != null) {
                String filePullPath = file.getFilePath() + File.separator + file.getSaveNm() + "." + file.getExtension();
                if (file.getFileSize() > 10 * 1024 * 1024) {
                    // 10MB가 넘는 zip 파일일 경우
                    filePullPath = filePullPath.substring(0, filePullPath.lastIndexOf('.')) + ".zip";
                }
                File targetFile = new File(filePullPath);
                if (targetFile.exists()) {
                    boolean deleted = targetFile.delete();
                    if (!deleted) {
                        // 삭제 실패 로그 출력 등
                        System.out.println("파일 삭제 실패: " + filePullPath);
                    }
                }
                femsFileRepository.delete(file);
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

    // fileId로 파일데이터 불러오기 (다운로드용도)
    public void download(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);

        String filePath = fileEntity.getFilePath() + File.separator + fileEntity.getSaveNm() + "." + fileEntity.getExtension();
        boolean isAlreadyZip = "zip".equalsIgnoreCase(fileEntity.getExtension());
        boolean shouldUseZip = fileEntity.getFileSize() > 10 * 1024 * 1024 && !isAlreadyZip;
        if (shouldUseZip) {
            // 10MB가 넘는 zip 파일일 경우
            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".zip";
        }
        File file = new File(filePath);
        // 실제 경로에 파일 존재할 시
        if (file.exists()) {
            OutputStream os = null;
            FileInputStream is = null;
            try {
                String fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + "." + fileEntity.getExtension();

                if (shouldUseZip) {
                    // 10MB가 넘는 zip 파일일 경우
                    fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + ".zip";
                }
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
                response.setContentType("application/octet-stream");
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);
                response.setHeader("X-Content-Download", String.valueOf(false));

                os = new BufferedOutputStream(response.getOutputStream());
                is = null;
                is = new FileInputStream(file);
                // output stream > input stream 복사
                FileCopyUtils.copy(is, os);
            } catch (Exception e) {
                LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: downloadFile", e.getMessage(), null);
            } finally {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException ::: downloadFile", e.getMessage(), null);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException ::: downloadFile", e.getMessage(), null);
                    }
                }
            }
        }
    }

    public InputStream getFileInputStream(String fileId) {
        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);
        String filePath = fileEntity.getFilePath() + File.separator
                + fileEntity.getSaveNm() + "." + fileEntity.getExtension();

        File file = new File(filePath);
        if (file.exists()) {
            try {
                return new FileInputStream(file); // 그냥 리턴
            } catch (FileNotFoundException e) {
                LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "FileNotFoundException ::: downloadFile", e.getMessage(), null);
            }
        }
        return null;
    }

    // 이미지 업로드
    @Transactional(rollbackFor = Throwable.class)
    public String upload(UploadParameters parameters) {
        MultipartFile file = parameters.getMultipartFile();
        String fileName = file.getOriginalFilename();
        String fileNameRemoveExtension = FilenameUtils.removeExtension(fileName);
        String[] fileNameSplit = fileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];

        String path = "";
        if ("system".equals(parameters.getFileType())) {
            path = Paths.get(property.getUploadDir(), "system", parameters.getTargetType(), parameters.getTargetId()).toString();
        } else if ("base".equals(parameters.getFileType())) {
            path = Paths.get(property.getUploadDir(), parameters.getCompId(), parameters.getTargetType(), parameters.getTargetId()).toString();
        } else {
            path = Paths.get(property.getUploadDir(), parameters.getCompId(), parameters.getTargetId().substring(0,4),parameters.getTargetType(), parameters.getTargetId()).toString();
        }
        System.out.println("######## path " + path);
        String type = util.getFileType(extension); // 파일 타입
        Long size = file.getSize();
        // 파일사이즈 변환 byte에서 KB,MB,GB..타입으로 변환
        String realSize = util.getReadableSize(size);

        // 파일명 가져오기
        HashMap<String, String> saveFile;
        saveFile = util.getFileName(fileName, parameters); // 중복 파일명이 있을경우 처리를 위함
        String saveName = saveFile.get("filename");
        String savePath = path + File.separator + saveName + "." + extension;

        // 경로 생성 관련
        File dir = null;
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(savePath));
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

    public String saveFile(List<MultipartFile> files, String writeYear, String targetType, String targetId, String compId, String type)
            throws Exception {
        // files가 null이거나 비어있는 경우를 처리
        if (files == null || files.isEmpty()) {
            System.out.println("No files to upload");
            return null;
        }
        System.out.println("############### files " + files);
        System.out.println("############### property.getTarget() " + property.getTarget());

        for (MultipartFile file : Objects.requireNonNull(files)) {
            UploadParameters parameters = new UploadParameters();
            parameters.setMultipartFile(file);
            parameters.setWriteYear(writeYear); // 2025.04.22 BHJ writeYear 추가
            parameters.setTargetType(targetType);
            parameters.setTargetId(targetId);
            parameters.setCompId(compId);
            parameters.setFileType(type);
            if ("local".equals(property.getTarget())) {
                if (files.size() == 1) {
                    return upload(parameters);
                }
                upload(parameters);
            } else if ("nas".equals(property.getTarget())) {
                if (files.size() == 1) {
                    return nasFileService.upload(parameters);
                }
                nasFileService.upload(parameters);
            }
        }
        return null;
    }


    // 사업장 변경에따른 파일 위치 변경
    public void moveFile(String targetType, String targetId, String originCompId, String targetCompId) throws Exception {
//        if ("local".equals(property.getTarget())) {
//            moveFileDir(targetType, targetId, originCompId, targetCompId);
//        } else if ("nas".equals(property.getTarget())) {
//            nasFileService.moveFileDir(targetType, targetId, originCompId, targetCompId);
//        }
        nasFileService.moveFileDir(targetType, targetId, originCompId, targetCompId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void moveFileDir(String targetType, String targetId, String originCompId, String targetCompId)
            throws Exception {
        Path sourceDir = Paths.get(property.getUploadDir(), originCompId, targetType, targetId); // 원본 디렉토리
        Path targetDir = Paths.get(property.getUploadDir(), targetCompId, targetType, targetId); // 이동할 디렉토리
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
                        String filePath = Paths.get(property.getUploadDir(), targetCompId, targetType, targetId).toString();
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

    public String saveFile(List<MultipartFile> files, String targetType, String targetId, String compId, String type)
            throws Exception {
        System.out.println("################## type ==> " + type);
        return saveFile(files, null, targetType, targetId, compId, type); // 2025.04.22 BHJ writeYear 폴더가 없는 정보 저장용
    }

    public String saveFile(List<MultipartFile> files, String targetType, String targetId, String compId)
            throws Exception {
        System.out.println("################## type ==> " + "normal");
        return saveFile(files, null, targetType, targetId, compId, "normal"); // 2025.04.22 BHJ writeYear 폴더가 없는 정보 저장용
    }

    public void deleteFile(List<String> files, String targetType, String targetId, String compId) throws Exception {
        // files가 null이거나 비어있는 경우를 처리
        if (files == null || files.isEmpty()) {
            System.out.println("No files to delete");
            return;
        }

        for (String fileId : Objects.requireNonNull(files)) {
            if (fileId != null) {
                if ("local".equals(property.getTarget())) {
                    deleteById(fileId);
                } else if ("nas".equals(property.getTarget())) {
                    nasFileService.deleteById(fileId);
                }
            }
        }
    }
}
