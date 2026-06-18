package kr.co.igns.system.base.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.dao.postgres.WpDAO;
import kr.co.igns.system.base.model.*;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.FileTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WpService {
    private final WpDAO wpDao;
    private final HrDAO hrDao;
    private final FileService fileService;
    private final UtilsService utilsService;
    // 파일 양식 다운로드 관련
    private final FileProperty property;
    private final FileTemplateRepository fileTemplateRepository;

    public List<WpVO> getWp(SpSearchVO searchVo) throws Exception {
        List<WpVO> voList = wpDao.getWp(searchVo);
        if (!voList.isEmpty()) {
            List<String> wpIds = voList.stream()
                    .map(WpVO::getWorkplaceId)
                    .collect(Collectors.toList());

            Map<String, Object> param = new HashMap<>();
            param.put("targetType", "workplace");
            param.put("targetIds", wpIds);
            param.put("gubun", "HEAD");
            List<HrVO> allHeadMaps = utilsService.getHrInchargeByTargetIds(param);
            Map<String, List<HrVO>> headMapGrouped = allHeadMaps.stream()
                    .collect(Collectors.groupingBy(HrVO::getTargetId));
            // 담당자 매핑
            param.put("gubun", "2ND");
            List<HrVO> allSecondMaps = utilsService.getHrInchargeByTargetIds(param);
            Map<String, List<HrVO>> secondMapGrouped = allSecondMaps.stream()
                    .collect(Collectors.groupingBy(HrVO::getTargetId));

            for (WpVO vo : voList) {
                // 담당자 인원 매핑
                String wpId = vo.getWorkplaceId();
                vo.setHeadHrList(headMapGrouped.getOrDefault(wpId, Collections.emptyList()));
                vo.setSecondHrList(secondMapGrouped.getOrDefault(wpId, Collections.emptyList()));
            }
        }
        return voList;
    }

    public WpVO getWpDetail(SpSearchVO searchVo) throws Exception {
        WpVO resultData = wpDao.getWpDetail(searchVo);
        resultData.setHeadHrList(utilsService.getHrIncharge("workplace", resultData.getWorkplaceId(), "HEAD"));
        resultData.setSecondHrList(utilsService.getHrIncharge("workplace", resultData.getWorkplaceId(), "2ND"));
        return resultData;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertWp(List<MultipartFile> files, WpVO reqVo) throws Exception {
        //작업장 생성
        //ID 자동 부여 (년월일순번)
        wpDao.insertWp(reqVo);

        //인원 매핑 - 담당자(정/부) 생성(target_id = workplace_id)
        String targetType = "workplace";
        BaseMapVO headHrInfo = new BaseMapVO(targetType, reqVo.getWorkplaceId());
        BaseMapVO secondHrInfo = new BaseMapVO(targetType, reqVo.getWorkplaceId());
        for (HrVO hr : reqVo.getHeadHrList()) {
            // 담당자(정) 업데이트
            headHrInfo.setId(hr.getHrId());
            headHrInfo.setUseYn(YesNo.Y);
            headHrInfo.setGubun("HEAD");
            headHrInfo.setCreatedBy(reqVo.getCreatedBy());
            hrDao.addHrMap(headHrInfo);
        }
        for (HrVO hr : reqVo.getSecondHrList()) {
            // 담당자(정) 업데이트
            secondHrInfo.setId(hr.getHrId());
            secondHrInfo.setUseYn(YesNo.Y);
            secondHrInfo.setGubun("2ND");
            secondHrInfo.setCreatedBy(reqVo.getCreatedBy());
            hrDao.addHrMap(secondHrInfo);
        }

        //파일 저장
        if (files != null) {
            String fileId = fileService.saveFile(files, "workplaceThumbnail", reqVo.getWorkplaceId(), SecurityUtil.getCurrentCompId(), "base");
            reqVo.setFileId(fileId);
        }

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateWp(List<MultipartFile> insertFiles, WpVO reqVo) throws Exception {
        //작업장 수정
        WpVO vo = wpDao.getWpById(reqVo.getWorkplaceId());
        if (vo == null)
            return null;
        vo = (WpVO) SpUtils.objectMap(reqVo, vo);

        wpDao.updateWp(vo);

        //인원 매핑 - 담당자(정/부) 수정(target_id = workplace_id, hr_id = id)
        BaseMapVO baseMapVO = new BaseMapVO("workplace", reqVo.getWorkplaceId());
        hrDao.hardDeleteHrMap(baseMapVO);
        baseMapVO.setCreatedBy(reqVo.getCreatedBy());
        for (HrVO hr : reqVo.getHeadHrList()) {
            // 담당자(정) 업데이트
            baseMapVO.setId(hr.getHrId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("HEAD");
            hrDao.addHrMap(baseMapVO);
        }
        for (HrVO hr : reqVo.getSecondHrList()) {
            // 담당자(정) 업데이트
            baseMapVO.setId(hr.getHrId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("2ND");
            hrDao.addHrMap(baseMapVO);
        }

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "workplaceThumbnail", reqVo.getWorkplaceId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "workplaceThumbnail", reqVo.getWorkplaceId(), SecurityUtil.getCurrentCompId(), "base");
        vo.setFileId(fileId);   //수정

        return vo;
    }

    public BaseVO deleteWp(WpVO reqVo) throws Exception {
        BaseVO vo = wpDao.getWpById(reqVo.getWorkplaceId());
        wpDao.deleteWp(reqVo);
        return vo;
    }

    public void deleteWps(List<WpVO> list) throws Exception {
        for (WpVO tmp : list) {
            wpDao.deleteWp(tmp);
        }
    }

    public void downloadWpExcelTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        // flow
        // NAS에서 파일 읽기 (InputStream)
        //엑셀 파싱 > 데이터 추가
        //수정된 엑셀을 HttpServletResponse에 써서 클라이언트로 전송
        if ("local".equals(property.getTarget())) {
            getLocalTemplate(request, response, id);
        } else if ("nas".equals(property.getTarget())) {
            getNasTemplate(request, response, id);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertWpExcel(List<WpVO> voList) throws Exception {
        for (WpVO vo : voList) {
            insertWp(null, vo);
        }
    }

    // 로컬
    public void getLocalTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        FileTemplateEntity fileEntity = fileTemplateRepository.findById(id);
        String filePath = property.getUploadDir() + File.separator + fileEntity.getFilePath() + "." + fileEntity.getExtension();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다: " + filePath);
        }

        try (
                InputStream in = new FileInputStream(file);
                Workbook workbook = setExcelSheet(in);
                OutputStream out = new BufferedOutputStream(response.getOutputStream())
        ) {
            String[] parts = fileEntity.getFilePath().split("/");
            String lastPart = parts[parts.length - 1];
            String fileName = lastPart + "." + fileEntity.getExtension();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            response.setHeader("Content-Transfer-Encoding", "binary");

            workbook.write(out);
            out.flush();

        } catch (Exception e) {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: downloadFile", e.getMessage(), null);
            throw e;
        }
    }

    // NAS
    public void getNasTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        FileTemplateEntity fileEntity = fileTemplateRepository.findById(id);

        String filePath = property.getNas().getFoPath() + File.separator + fileEntity.getFilePath() + "." + fileEntity.getExtension();
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();

        SMBClient client = new SMBClient();
        InputStream in = null;
        Workbook workbook = null;
        OutputStream out = null;

        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);

            if (!share.fileExists(filePath)) {
                throw new CUserDefinedException("File doesn't exist: " + filePath);
            }

            com.hierynomus.smbj.share.File smbFile = share.openFile(
                    filePath,
                    EnumSet.of(AccessMask.GENERIC_READ),
                    null,
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    null
            );

            in = new BufferedInputStream(smbFile.getInputStream());

            // === Apache POI를 사용하여 엑셀 파일 파싱 ===
            workbook = setExcelSheet(in);

            // === 사용자에게 전송 ===
            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20"); // 공백 + 방지

            // 파일 유형 설정
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            out = response.getOutputStream();
            workbook.write(out);

        } catch (Exception e) {
            response.setHeader("CustomHeader", e.getMessage());
            throw e;
        } finally {
            if (workbook != null) workbook.close();
            if (in != null) in.close();
            if (out != null) out.flush();
            client.close();
        }
    }

    // 엑셀 시트 데이터 바인딩
    public Workbook setExcelSheet(InputStream in) throws Exception {
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(1); // 두 번째 시트(데이터셋 시트)
        CellStyle textStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@")); // "@"는 텍스트 형식 의미

        // === 엑셀에 데이터 추가 ===
        // === 성별 ===
        int currentRow = 2; // ==> 3행
        int currentCol = 1; // ==> B열
        // ==> 초기값 B3셀
        SpSearchVO searchVO = new SpSearchVO();
        searchVO.setCompId(SecurityUtil.getCurrentCompId());
        List<HrVO> hrList = hrDao.getHrList(searchVO);
        if (!hrList.isEmpty()) {
            hrList = hrList.stream()
                    .filter(hr -> YesNo.Y.equals(hr.getUseYn()))
                    .collect(Collectors.toList());
            for (int i = 0; i < hrList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 코드 (hrId)
                Cell hrIdCell = row.createCell(currentCol); // 엑셀의 열
                hrIdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
                hrIdCell.setCellValue(hrList.get(i).getHrId());

                // -- 사용자명 (hrNm)
                Cell hrNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                hrNmCell.setCellValue(hrList.get(i).getHrNm());

                // -- 직위 (jbrpNm)
                Cell jbrpNmCell = row.createCell(currentCol + 2); // 엑셀의 열
                jbrpNmCell.setCellValue(hrList.get(i).getJbrpNm());

                // -- 조직 (orgnNm)
                Cell orgnNmCell = row.createCell(currentCol + 3); // 엑셀의 열
                orgnNmCell.setCellValue(hrList.get(i).getOrgnNm());
                currentRow++;
            }
        }
        return workbook;
    }
}
