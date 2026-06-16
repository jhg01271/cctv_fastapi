package kr.co.igns.system.base.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.dao.postgres.OrganizationDAO;
import kr.co.igns.system.base.dao.postgres.PrcsDAO;
import kr.co.igns.system.base.dao.postgres.WpDAO;
import kr.co.igns.system.base.model.*;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.FileTemplateRepository;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
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
public class PrcsService {
    private final PrcsDAO prcsDao;
    private final WpDAO wpDao;
    private final HrDAO hrDao;
    private final OrganizationDAO organizationDao;
    private final FileService fileService;
    private final UtilsService utilsService;
    // 파일 양식 다운로드 관련
    private final FileProperty property;
    private final FileTemplateRepository fileTemplateRepository;
    private final SystemCodeDAO systemCodeDao;

    public List<PrcsVO> getPrcs(SpSearchVO searchVo) throws Exception {
        List<PrcsVO> voList = prcsDao.getPrcs(searchVo);
        if (!voList.isEmpty()) {
            List<String> prcsIds = voList.stream()
                    .map(PrcsVO::getProcessId)
                    .collect(Collectors.toList());
            Map<String, Object> param = new HashMap<>();
            param.put("targetType", "process");
            param.put("targetIds", prcsIds);

            // 작업장 매핑
            List<BaseMapVO> allWpMaps = utilsService.getWpMapByTargetIds(param);
            Map<String, List<BaseMapVO>> wpMapGrouped = allWpMaps.stream()
                    .collect(Collectors.groupingBy(BaseMapVO::getTargetId));
            // 조직 매핑
            List<BaseMapVO> allOrgnMaps = utilsService.getOrgnMapByTargetIds(param);
            Map<String, List<BaseMapVO>> orgnMapGrouped = allOrgnMaps.stream()
                    .collect(Collectors.groupingBy(BaseMapVO::getTargetId));
            // 담당자 매핑
            param.put("gubun", "HEAD");
            List<HrVO> allHeadMaps = utilsService.getHrInchargeByTargetIds(param);
            Map<String, List<HrVO>> headMapGrouped = allHeadMaps.stream()
                    .collect(Collectors.groupingBy(HrVO::getTargetId));
            // 담당자 매핑
            param.put("gubun", "2ND");
            List<HrVO> allSecondMaps = utilsService.getHrInchargeByTargetIds(param);
            Map<String, List<HrVO>> secondMapGrouped = allSecondMaps.stream()
                    .collect(Collectors.groupingBy(HrVO::getTargetId));

            for (PrcsVO vo : voList) {
                String prcsId = vo.getProcessId();
                vo.setWorkplaceList(wpMapGrouped.getOrDefault(prcsId, Collections.emptyList()));
                vo.setOrgnList(orgnMapGrouped.getOrDefault(prcsId, Collections.emptyList()));
                vo.setHeadHrList(headMapGrouped.getOrDefault(prcsId, Collections.emptyList()));
                vo.setSecondHrList(secondMapGrouped.getOrDefault(prcsId, Collections.emptyList()));
            }
        }
        return voList;
    }

    public PrcsVO getPrcsDetail(SpSearchVO Vo) throws Exception {
        PrcsVO voList = prcsDao.getPrcsDetail(Vo);

        //작업장 매핑 조회
        BaseMapVO searchVo = new BaseMapVO("process", voList.getProcessId());
        List<BaseMapVO> wplist = wpDao.getWpMapByUseYn(searchVo);
        voList.setWorkplaceList(wplist);

        //조직 매핑 조회
        BaseMapVO searchVo1 = new BaseMapVO("process", voList.getProcessId());
        List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(searchVo1);
        voList.setOrgnList(orgnlist);

        // 담당자 세팅
        voList.setHeadHrList(utilsService.getHrIncharge("process", voList.getProcessId(), "HEAD"));
        voList.setSecondHrList(utilsService.getHrIncharge("process", voList.getProcessId(), "2ND"));

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertPrcs(List<MultipartFile> files, PrcsVO reqVo) throws Exception {
        //공정 생성
        //ID 자동 부여 (년월일순번)
        prcsDao.insertPrcs(reqVo);

        //인원 매핑 - 담당자(정/부) 생성(target_id = process_id)
        String targetType = "process";
        BaseMapVO headHrInfo = new BaseMapVO(targetType, reqVo.getProcessId());
        BaseMapVO secondHrInfo = new BaseMapVO(targetType, reqVo.getProcessId());
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

        //작업장 매핑 생성
        for (String workplaceId : reqVo.getWorkplaceIdList()) {
            BaseMapVO wpMapInfo = new BaseMapVO(targetType, reqVo.getProcessId(), workplaceId, reqVo.getCreatedBy());
            wpDao.addWpMap(wpMapInfo);
        }

        //조직 매핑 생성
        for (String orgnId : reqVo.getOrgnIdList()) {
            BaseMapVO orgnMapInfo = new BaseMapVO(targetType, reqVo.getProcessId(), orgnId, reqVo.getCreatedBy());
            organizationDao.addOrgnMap(orgnMapInfo);
        }

        //파일 저장
        if (files != null) {
            String fileId = fileService.saveFile(files, "process", reqVo.getProcessId(), SecurityUtil.getCurrentCompId(), "base");
            reqVo.setFileId(fileId);
        }

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updatePrcs(List<MultipartFile> insertFiles, PrcsVO reqVo) throws Exception {
        //공정 수정
        PrcsVO vo = prcsDao.getPrcsById(reqVo.getProcessId());
        if (vo == null) return null;
        vo = (PrcsVO) SpUtils.objectMap(reqVo, vo);

        prcsDao.updatePrcs(vo);

        String targetType = "process";

        //인원 매핑 - 담당자(정/부) 수정(target_id = process_id, hr_id = id)
        BaseMapVO baseMapVO = new BaseMapVO("process", reqVo.getProcessId());
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

        //작업장 매핑 수정
        BaseMapVO searchVo2 = new BaseMapVO(targetType, reqVo.getProcessId());
        List<BaseMapVO> wpList = wpDao.getWpMap(searchVo2);

        // 1. 설비에 대한 작업장 전체 use_yn = 'N'
        BaseMapVO param = new BaseMapVO(targetType, reqVo.getProcessId(), null, reqVo.getUpdatedBy());
        wpDao.deleteWpMap(param);

        for (String workplaceId : reqVo.getWorkplaceIdList()) {
            Optional<BaseMapVO> matchingMap = wpList.stream().filter(data -> data.getId().equals(workplaceId)).findFirst();

            if (matchingMap.isPresent()) {
                // 2. 설비에 해당 작업장이 원래 있는 경우
                param = matchingMap.get();
                param.setUseYnByString("Y");
                param.setUpdatedBy(reqVo.getUpdatedBy());
                wpDao.updateUseYnWpMap(param);
            } else {
                // 3. 설비에 해당 작업장이 없는 경우
                param.setId(workplaceId);
                param.setCreatedBy(reqVo.getUpdatedBy());
                wpDao.addWpMap(param);
            }
        }

        //조직 매핑 수정
        BaseMapVO searchVo3 = new BaseMapVO("process", reqVo.getProcessId());
        List<BaseMapVO> orgnList = organizationDao.getOrgnMap(searchVo3);

        // 1. 공정에 대한 조직 전체 use_yn = 'N'
        BaseMapVO param2 = new BaseMapVO("process", reqVo.getProcessId(), null, reqVo.getUpdatedBy());
        organizationDao.deleteOrgnMap(param2);

        for (String orgnId : reqVo.getOrgnIdList()) {
            Optional<BaseMapVO> matchingMap = orgnList.stream().filter(data -> data.getId().equals(orgnId)).findFirst();

            if (matchingMap.isPresent()) {
                // 2. 공정에 해당 조직이 원래 있는 경우
                param2 = matchingMap.get();
                param2.setUseYnByString("Y");
                param2.setUpdatedBy(reqVo.getUpdatedBy());
                organizationDao.updateUseYnOrgnMap(param2);
            } else {
                // 3. 공정에 해당 조직이 없는 경우
                param2.setId(orgnId);
                param2.setCreatedBy(reqVo.getUpdatedBy());
                organizationDao.addOrgnMap(param2);
            }
        }

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "process", reqVo.getProcessId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "process", reqVo.getProcessId(), SecurityUtil.getCurrentCompId(), "base");
        vo.setFileId(fileId);   //수정

        return vo;
    }

    public BaseVO deletePrcs(PrcsVO reqVo) throws Exception {
        BaseVO vo = prcsDao.getPrcsById(reqVo.getProcessId());
        prcsDao.deletePrcs(reqVo);
        return vo;
    }

    public void deletePrcss(List<PrcsVO> list) throws Exception {
        for (PrcsVO tmp : list) {
            prcsDao.deletePrcs(tmp);
        }
    }

    //region 공정별 작업 내용관련

    public List<PrcsCnfmVO> getPrcsRevList(String processId) throws Exception {
        List<PrcsCnfmVO> voList = prcsDao.getPrcsRevList(processId);
        return voList;
    }

    public List<PrcsWorkVO> getPrcsCnfmWorkList(String processId) throws Exception {
        List<PrcsWorkVO> voList = prcsDao.getPrcsCnfmWorkList(processId);
        return voList;
    }

    public List<PrcsWorkVO> getPrcsWorkList(PrcsCnfmVO prcsCnfmVO) throws Exception {
        prcsCnfmVO.setCompId(SecurityUtil.getCurrentCompId());
        List<PrcsWorkVO> voList = prcsDao.getPrcsWorkList(prcsCnfmVO);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertPrcsRev(PrcsCnfmVO reqVo) throws Exception {
        //공정 작업내용 차수 생성
        prcsDao.insertPrcsRev(reqVo);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updatePrcsRev(PrcsCnfmVO reqVo) throws Exception {
        //공정 작업내용 차수 사용여부 업데이트
        prcsDao.updatePrcsRev(reqVo);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updatePrcsRevCnfm(PrcsCnfmVO reqVo) throws Exception {
        //공정 작업내용 차수 확정/확정취소
        prcsDao.updatePrcsRevCnfm(reqVo);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<PrcsWorkVO> updatePrcsWorks(List<PrcsWorkVO> reqVo) throws Exception {
        prcsDao.updatePrcsWorkCnfm(reqVo.get(0)); // 차수명/사용여부 변경

        // 공정 작업내용 차수 사용여부 업데이트
        for (PrcsWorkVO work : reqVo) {
            // 신규 작업내용인 경우
            if ("I".equals(work.getCmd())) {
                prcsDao.insertPrcsWork(work);
            } else if ("U".equals(work.getCmd())) {  // 작업내용 수정인 경우
                prcsDao.updatePrcsWork(work);
            }
        }
        return reqVo;
    }

    public void downloadPrcsExcelTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
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
    public void insertPrcsExcel(List<PrcsVO> voList) throws Exception {
        for (PrcsVO vo : voList) {
            insertPrcs(null, vo);
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

        try (InputStream in = new FileInputStream(file); Workbook workbook = setExcelSheet(in); OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
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

            com.hierynomus.smbj.share.File smbFile = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);

            in = new BufferedInputStream(smbFile.getInputStream());

            // === Apache POI를 사용하여 엑셀 파일 파싱 ===
            workbook = setExcelSheet(in);

            // === 사용자에게 전송 ===
            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20"); // 공백 + 방지

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
        // === 사용처 ===
        int currentRow = 2; // ==> 3행
        int currentCol = 1; // ==> B열
        // ==> 초기값 B3셀
        SystemCodeVO usageTypeVO = new SystemCodeVO();
        usageTypeVO.setMajorCd("C0010");
        List<SystemMinorCodeVO> usageTypeList = systemCodeDao.findDetail(usageTypeVO);
        for (int i = 0; i < usageTypeList.size(); i++) {
            Row row = sheet.getRow(currentRow); // 엑셀의 행
            if (row == null) row = sheet.createRow(currentRow);
            // -- minorCd
            Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
            minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
            minorCdCell.setCellValue(usageTypeList.get(i).getMinorCd());

            // -- minorNm
            Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
            minorNmCell.setCellValue(usageTypeList.get(i).getMinorNm());
            currentRow++;
        }

        // === 인원 ===
        currentRow = 2; // ==> 3행
        currentCol = 4; // ==> E열
        SpSearchVO searchVO = new SpSearchVO();
        searchVO.setCompId(SecurityUtil.getCurrentCompId());
        List<HrVO> hrList = hrDao.getHrList(searchVO);
        if (!hrList.isEmpty()) {
            hrList = hrList.stream().filter(hr -> YesNo.Y.equals(hr.getUseYn())).collect(Collectors.toList());
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

        // === 조직 ===
        currentRow = 2; // ==> 3행
        currentCol = 9; // ==> J열

        List<OrganizationVO> orgnList = organizationDao.getOrganizationList(searchVO);
        if (!orgnList.isEmpty()) {
            orgnList = orgnList.stream().filter(orgn -> YesNo.Y.equals(orgn.getUseYn())).collect(Collectors.toList());
            for (int i = 0; i < orgnList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 코드 (orgnId)
                Cell orgnIdCell = row.createCell(currentCol); // 엑셀의 열
                orgnIdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
                orgnIdCell.setCellValue(orgnList.get(i).getOrgnId());

                // -- 조직명 (orgnNm)
                Cell orgnNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                orgnNmCell.setCellValue(orgnList.get(i).getOrgnNm());

                currentRow++;
            }
        }
        // === 작업장 ===
        currentRow = 2; // ==> 3행
        currentCol = 12; // ==> J열
        List<WpVO> wpList = wpDao.getWp(searchVO);
        if (!wpList.isEmpty()) {
            wpList = wpList.stream().filter(wp -> YesNo.Y.equals(wp.getUseYn())).collect(Collectors.toList());
            for (int i = 0; i < wpList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 코드 (workplaceId)
                Cell workplaceIdCell = row.createCell(currentCol); // 엑셀의 열
                workplaceIdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
                workplaceIdCell.setCellValue(wpList.get(i).getWorkplaceId());

                // -- 작업장명 (workplaceNm)
                Cell workplaceNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                workplaceNmCell.setCellValue(wpList.get(i).getWorkplaceNm());

                currentRow++;
            }
        }

        return workbook;
    }
    //endregion
}
