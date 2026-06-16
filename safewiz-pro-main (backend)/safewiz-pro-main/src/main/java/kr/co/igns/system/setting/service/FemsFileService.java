package kr.co.igns.system.setting.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.system.common.dao.postgres.FileDAO;
import kr.co.igns.system.common.model.FileVO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.system.setting.dao.FemsFileRepository;
import kr.co.igns.system.common.service.dto.SpFileRequestDto;
import kr.co.igns.system.common.service.dto.SpFileResponseDto;
import kr.co.igns.system.common.entity.SpFileEntity;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.util.FileUtil;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.utils.file.FileDelete;
import kr.co.igns.framework.utils.file.UploadParameters;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FemsFileService {

//    private final FemsFileRepository femsFileRepository;
//    private final FileProperty property;
//    private final FileUtil util;
//    private final FileDAO fileDAO;
//
//    public List<SpFileResponseDto> search(UploadParameters parameter) throws Exception {
//        // target, 사업장 정보로 삭제되지 않은 file들 조회
//        List<FileVO> fileList = fileDAO.getFileListById(parameter.getFileId());
//        String targetType = fileList.get(0).getTargetType();
//        String targetId = fileList.get(0).getTargetId();
//
//        List<SpFileEntity> entities = femsFileRepository.findByTargetTypeAndTargetIdAndDelYn(targetType, targetId, "N");
//        // 계층간 데이터 이동을 위해 dto로 변환
//        List<SpFileResponseDto> files = new ArrayList<>();
//        for (SpFileEntity entity : entities) {
//
//            SpFileResponseDto file = (SpFileResponseDto) SpUtils.objectMap(entity, SpFileResponseDto.class);
////			FemsFileResponseDto file = new FemsFileResponseDto(entity);
//            files.add(file);
//        }
//        return files;
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void deleteByList(List<SpFileRequestDto> dtoList) {
//        for (SpFileRequestDto dto : dtoList) {
//            if (StringUtils.isNoneEmpty(dto.getFileId())) {
//                deleteById(dto.getFileId());
//            } else {
//                deleteByTarget(dto);
//            }
//        }
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void deleteById(String fileId) {
//        // fileId로 해당 파일 정보 조회
//        SpFileEntity file = femsFileRepository.findByFileId(fileId);
//        delete(file);
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void deleteByTarget(SpFileRequestDto dto) {
//        System.out.println("dto >> " + dto);
//        // 타켓 정보로 해당 파일 정보 조회
//        List<SpFileEntity> files = femsFileRepository.findByTargetIdAndTargetTypeAndCompId(dto.getTargetId(), dto.getTargetType(), dto.getCompId());
//        System.out.println("files >> " + files);
//        for (SpFileEntity file : files) {
//            delete(file);
//        }
//    }
//
//    public void delete(SpFileEntity file) {
//        // 서버 & 테이블 제거 여부
//        if (property.isAllowDelete()) {
//            if (file != null) {
//                String filePullPath = file.getFilePath() + File.separator + file.getSaveNm() + "." + file.getExtension();
//                FileDelete fileDeleteThread = new FileDelete(filePullPath);
//                fileDeleteThread.run();
//                femsFileRepository.delete(file);
//            }
//        } else {
//            SpFileEntity entity = SpFileEntity.builder()
//                    .fileId(file.getFileId())//
//                    .compId(file.getCompId())//
//                    .fileType(file.getFileType())//
//                    .filePath(file.getFilePath())//
//                    .fileNm(file.getFileNm())//
//                    .fileSize(file.getFileSize())//
//                    .extension(file.getExtension())//
//                    .saveNm(file.getSaveNm())//
//                    .targetId(file.getTargetId())//
//                    .targetType(file.getTargetType())//
//                    .useYn(file.getUseYn())//
//                    .delYn("Y").build();
//
//            femsFileRepository.save(entity);
//        }
//    }
//
//
//    public void download(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
//        // fileId로 파일데이터 불러오기
//        SpFileEntity fileEntity = femsFileRepository.findByFileId(fileId);
//
//        String filePath = fileEntity.getFilePath() + File.separator + fileEntity.getSaveNm() + "." + fileEntity.getExtension();
//        if (fileEntity.getFileSize() > 10 * 1024 * 1024) {
//            // 10MB가 넘는 zip 파일일 경우
//            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".zip";
//        }
//        File file = new File(filePath);
//        // 실제 경로에 파일 존재할 시
//        if (file.exists()) {
//            OutputStream os = null;
//            FileInputStream is = null;
//            try {
////				String fileName = util.encodingByBrowser(request, response, fileEntity.getFileNm());
////				String fileName = URLEncoder.encode(fileEntity.getFileNm(), "UTF-8").replace("+", "%20").replace("\n", "").replace("\r", "") ;
//                // octet-stream 8비트 단위의 바이너리 데이터를 의미함. 알 수없는 파일 형식을 사용할때 사용함
//
//                String fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + "." + fileEntity.getExtension();
//
//                if (fileEntity.getFileSize() > 10 * 1024 * 1024) {
//                    // 10MB가 넘는 zip 파일일 경우
//                    fileName = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm() + ".zip";
//                }
//                 String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
//                response.setContentType("application/octet-stream");
//                response.setContentLength((int) file.length());
//                response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);
//
//                os = response.getOutputStream();
//                is = null;
//                is = new FileInputStream(file);
//                // output stream > input stream 복사
//                FileCopyUtils.copy(is, os);
//                os.flush();
//            } catch (FileNotFoundException var25) {
//                LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "FileNotFoundException ::: downloadFile", var25.getMessage(), null);
//            } catch (IOException var26) {
//                LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException ::: downloadFile", var26.getMessage(), null);
//            } catch (Exception e) {
//                LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: downloadFile", e.getMessage(), null);
//            } finally {
//                if (os != null) {
//                    try {
//                        os.close();
//                    } catch (IOException e) {
//                        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException ::: downloadFile", e.getMessage(), null);
//                    }
//                }
//                if (is != null) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException ::: downloadFile", e.getMessage(), null);
//                    }
//                }
//            }
//        }
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void upload(UploadParameters parameters) throws Exception {
//        System.out.println("@@@@");
//        MultipartFile file = parameters.getMultipartFile();
//        String fileName = file.getOriginalFilename();
//        String fileNameRemoveExtension = FilenameUtils.removeExtension(fileName);
//        String[] fileNameSplit = fileName.split("\\.");
//        String extension = fileNameSplit[fileNameSplit.length - 1];
//        String path = Paths.get(property.getUploadDir(), parameters.getTargetType(), parameters.getTargetId()).toString();
//        String type = util.getFileType(extension); // 파일 타입
//        Long size = file.getSize();
//        // 파일사이즈 변환 byte에서 KB,MB,GB..타입으로 변환
////		String realSize = util.getReadableSize(size);
//
//        // 파일명 가져오기
//        HashMap<String, String> saveFile;
//        saveFile = util.getFileName(fileName, parameters); // 중복 파일명이 있을경우 처리를 위함
//        String saveName = saveFile.get("filename");
//        String savePath = path + File.separator + saveName + "." + extension;
//
//        // 경로 생성 관련
//        File dir = null;
//        dir = new File(path);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        try {
//            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(savePath));
//        } catch (Exception e) {
//            System.out.println(e);
//            throw new CUserDefinedException("파일복사에 실패했습니다.");
//        }
//
//
//        SpFileEntity entity = SpFileEntity.builder()
//                .fileId(SpUtils.getUuid())
//                .fileNm(fileNameRemoveExtension)
//                .saveNm(saveName)
//                .extension(extension)
//                .fileType(type)
//                .fileSize(size)
//                .filePath(path)
//                .useYn("Y")
//                .delYn("N")
//                .compId(parameters.getCompId())
//                .targetId(parameters.getTargetId())
//                .targetType(parameters.getTargetType())
//                .build();
//        femsFileRepository.save(entity);
//    }
}
