package kr.co.igns.system.base.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.model.*;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.entity.SpFileEntity;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.FileTemplateRepository;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.dao.postgres.UserManageDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.setting.model.UserManageVO;
import kr.co.igns.system.setting.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class HrService {
    private final PasswordEncoder passwordEncoder;
    private final HrDAO hrDao;
    private final UserManageDAO userManageDAO;
    private final UserManageService userManageService;
    private final FileService fileService;
    // 파일 양식 다운로드 관련
    private final FileProperty property;
    private final FileTemplateRepository fileTemplateRepository;
    private final SystemCodeDAO systemCodeDao;
    private final OrganizationService organizationService;
    private final PartnerService partnerService;

    public List<HrVO> getHr(SpSearchVO searchVo) throws Exception {
        List<HrVO> voList = hrDao.getHrList(searchVo);
        for (HrVO hr : voList) {
            decryptHr(hr);
        }

        return voList;
    }

    // 인원 정보 디코딩
    private void decryptHr(HrVO hrVo) {
        if (hrVo.getPhone() != null) hrVo.setPhone(Aes256.decryptString(hrVo.getPhone()));
        if (hrVo.getPhone() != null) hrVo.setEmail(Aes256.decryptString(hrVo.getEmail()));
    }

    public HrVO getHrDetail(SpSearchVO searchVo) throws Exception {
        HrVO voList = hrDao.getHrDetail(searchVo);
        decryptHr(voList);
        List<HrVO> compListData = hrDao.getAffMgrCompIdByHr(searchVo.getHrId());

        List<BaseMapVO> objectList = new ArrayList<>(hrDao.getCompIdByHr(voList.getHrId()));

        for (int i = 0; i < compListData.size(); i++) {
            if (compListData.get(i).getOrgnCompYn().equals("Y")) {
                voList.setAffCompId(compListData.get(i).getCompId());
                voList.setAffCompNm(compListData.get(i).getCompNm());
            } else {
                if (voList.getMgrCompId() == null) {
                    voList.setMgrCompId(compListData.get(i).getCompId());
                    voList.setMgrCompNm(compListData.get(i).getCompNm());
                } else {
                    voList.setMgrCompId(voList.getMgrCompId() + ";" + compListData.get(i).getCompId());
                    voList.setMgrCompNm(voList.getMgrCompNm() + ";" + compListData.get(i).getCompNm());
                }
            }
        }
        voList.setCompList(objectList);

        return voList;
    }

    public boolean checkExceedMaxHrCount(HrVO hrVo) throws Exception {
        HrVO result = hrDao.checkExceedMaxHrCount(hrVo);

        int maxHrCount = result.getMaxHrCount();
        int hrCount = result.getHrCount();

        // hrId가 null이 아닌 경우 (기존 인원)
        if (hrVo.getHrId() != null) {
            // use_yn이 N -> Y로 변경되는 경우만 체크
            if (hrVo.getUseYn().equals(YesNo.valueOf("Y")) && result.getUseYn().equals(YesNo.valueOf("N"))) {
                hrCount += 1;
                if (maxHrCount < hrCount) {
                    return true; // 인원 초과
                } else {
                    return false; // 인원 미초과
                }
            }

            // 이미 Y인 경우는 변경사항 없으므로 false 반환
            return false;
        } else {
            if (hrVo.getUseYn().equals(YesNo.valueOf("Y"))) {
                hrCount += 1;
                if (maxHrCount < hrCount) {
                    return true; // 인원 초과
                } else {
                    return false; // 인원 미초과
                }
            }

            // N인 경우는 false 반환
            return false;
        }
    }

    public List<OrgHistoryVO> getOrgHistoryList(String hrId) throws Exception {
        List<OrgHistoryVO> voList = hrDao.getOrgHistoryList(hrId);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<HrVO> insertHrExcel(List<HrVO> reqVo) throws Exception {

        boolean hasDuplicates = reqVo.stream()
                .map(HrVO::getUserId) // use_id 필드 추출
                .filter(Objects::nonNull) // null 값 무시 (선택 사항)
                .collect(Collectors.toSet()) // Set으로 수집하여 중복 제거
                .size() != reqVo.stream()
                .map(HrVO::getUserId) // use_id 필드 추출
                .filter(Objects::nonNull) // null 값 무시
                .count(); // 원래 데이터 개수와 비교

        if (hasDuplicates)
            throw new Exception("중복 아이디가 존재합니다.");

        //인원 아이디 중복 체크
        SpSearchVO sv = new SpSearchVO();
        sv.setCompId(reqVo.get(0).getCompId());
        List<HrVO> hrList = hrDao.getHrList(sv);

        //인원 저장 로직 활용
        for (HrVO vo : reqVo) {
            if (hrList.stream().anyMatch(hr -> vo.getUserId().equals(hr.getUserId()))) {
                throw new Exception("중복 아이디가 존재합니다.");
            } else {
                //TODO: 두번째 row부터 createby가 안들어감
                vo.setCreatedBy(reqVo.get(0).getCreatedBy());
                insertHr(null, vo);
            }
        }
        return reqVo;
    }

    public void downloadHrExcelTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
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
        SystemCodeVO sexVO = new SystemCodeVO();
        sexVO.setMajorCd("C0048");
        List<SystemMinorCodeVO> sexDivList = systemCodeDao.findDetail(sexVO);
        for (int i = 0; i < sexDivList.size(); i++) {
            Row row = sheet.getRow(currentRow); // 엑셀의 행
            if (row == null) row = sheet.createRow(currentRow);
            // -- minorCd
            Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
            minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
            minorCdCell.setCellValue(sexDivList.get(i).getMinorCd());

            // -- minorNm
            Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
            minorNmCell.setCellValue(sexDivList.get(i).getMinorNm());
            currentRow++;
        }

        // === 직위 ===
        currentRow = 2; // ==> 3행
        currentCol = 4; // ==> E열
        // ==> 초기값 E3셀
        List<HrJbrpVO> jbrpList = hrDao.getHrJbrp(SecurityUtil.getCurrentCompId());
        if (!jbrpList.isEmpty()) {
            jbrpList = jbrpList.stream()
                    .filter(jbrp -> YesNo.Y.equals(jbrp.getUseYn()))
                    .collect(Collectors.toList());
            for (int i = 0; i < jbrpList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 직위코드
                Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
                minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정
                minorCdCell.setCellValue(jbrpList.get(i).getJbrpId());

                // -- 직위명
                Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                minorNmCell.setCellValue(jbrpList.get(i).getJbrpNm());
                currentRow++;
            }
        }
        // === 조직 ===
        currentRow = 2; // ==> 3행
        currentCol = 7; // ==> H열
        // ==> 초기값 H3셀
        SpSearchVO orgnVO = new SpSearchVO();
        orgnVO.setCompId(SecurityUtil.getCurrentCompId());
        List<OrganizationVO> orgnList = organizationService.getOrganization(orgnVO);
        if (!orgnList.isEmpty()) {
            orgnList = orgnList.stream()
                    .filter(orgn -> YesNo.Y.equals(orgn.getUseYn()))
                    .collect(Collectors.toList());
            for (int i = 0; i < orgnList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 조직코드
                Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
                minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정
                minorCdCell.setCellValue(orgnList.get(i).getOrgnId());

                // -- 조직명
                Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                minorNmCell.setCellValue(orgnList.get(i).getOrgnNm());
                currentRow++;
            }
        }

        // === 협력사 ===
        currentRow = 2; // ==> 3행
        currentCol = 10; // ==> K열
        // ==> 초기값 K3셀
        SpSearchVO partnerVO = new SpSearchVO();
        partnerVO.setCompId(SecurityUtil.getCurrentCompId());
        List<PartnerVO> partnerList = partnerService.getPartner(partnerVO);
        if (!partnerList.isEmpty()) {
            partnerList = partnerList.stream()
                    .filter(partner -> YesNo.Y.equals(partner.getUseYn()))
                    .collect(Collectors.toList());
            for (int i = 0; i < partnerList.size(); i++) {
                Row row = sheet.getRow(currentRow); // 엑셀의 행
                if (row == null) row = sheet.createRow(currentRow);
                // -- 협력사코드
                Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
                minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정
                minorCdCell.setCellValue(partnerList.get(i).getPartCompId());

                // -- 협력사명
                Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
                minorNmCell.setCellValue(partnerList.get(i).getPartCompNm());
                currentRow++;
            }
        }
        return workbook;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertHr(List<MultipartFile> files, HrVO reqVo) throws Exception {
        
    	//1.인원 정보 추가 (tb_hr_info)
    	if (reqVo.getWorkingAt() == null) {
            // 입사일 null일 경우, 오늘 날짜로 세팅
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = today.format(formatter);
            reqVo.setWorkingAt(formattedDate);
        }
        hrDao.insertHr(reqVo);

        //2.사용자 정보 추가 (tb_user_info)
        UserManageVO userVo = new UserManageVO();
        userVo.setClntId(reqVo.getClntId());
        userVo.setTargetClntId(reqVo.getClntId());
        userVo.setUserId(reqVo.getUserId());
        userVo.setUserNm(reqVo.getHrNm());
        userVo.setUserPs(reqVo.getUserPs());
        userVo.setEmail(reqVo.getEmail());
        userVo.setPhone(reqVo.getPhone());
        userVo.setAuthGroupCd("USER");  //TODO: 임시로 USER 넣어둠 (인원 관리에서 생성되는 사용자는 USER 권한임)
        userVo.setHrId(reqVo.getHrId());
        userVo.setUseYn(reqVo.getUseYn());
        userVo.setOrgnCompYn("Y"); //hrService에서 호출된 것인지 판단 기준 항목
        userManageService.insertUser(userVo);
        
        //3.사업장 매핑 정보 추가 (tb_hr_info_comp_map)        
        //3-1.소속 사업장 데이터
        UserManageVO orgnCompYnY = userVo;

        // 소속 사업장 데이터가 있는 경우만 처리
        if (reqVo.getAffCompId() != null) {
            orgnCompYnY.setCompId(reqVo.getAffCompId());
            orgnCompYnY.setOrgnCompYn("Y");
            userManageDAO.insertHrInfoCompMap(orgnCompYnY);
        }

        //3-2.관리 사업장 데이터
        UserManageVO orgnCompYnN = userVo;
        orgnCompYnN.setOrgnCompYn("N");
        orgnCompYnN.setCompId(reqVo.getMgrCompId());

        if (reqVo.getMgrCompId() != null && !reqVo.getMgrCompId().isEmpty()) {
            if (reqVo.getMgrCompId().contains(";")) {
                String[] splitText = reqVo.getMgrCompId().split(";");
                for (int i = 0; i < splitText.length; i++) {
                    orgnCompYnN.setCompId(splitText[i]);
                    userManageDAO.insertHrInfoCompMap(orgnCompYnN);
                }
            } else {
                userManageDAO.insertHrInfoCompMap(orgnCompYnN);
            }
        }

        //4.협럭사가 아닌 경우에만 조직 이력 저장
        if (reqVo.getPartCompId() == null) {
            //조직 변경 이력 업데이트, 추가
            reqVo.setStDate(reqVo.getWorkingAt());
            hrDao.addOrgnHistoryHr(reqVo);
        }

        //5.파일 저장
//        String fileId = fileService.saveFile(files, "hrImg", reqVo.getHrId(), null, "base");
        String fileId = fileService.saveFile(files, "hrImg", reqVo.getHrId(), SecurityUtil.getCurrentCompId(), "base");
        reqVo.setFileId(fileId);
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateHr(List<MultipartFile> insertFiles, HrVO reqVo) throws Exception {
        
    	HrVO vo = hrDao.getHrById(reqVo.getHrId());        
        if (vo == null)
            return null;

        //조직 이력 관리 flag
        boolean orgnChangeYn = Objects.equals(vo.getOrgnId(), reqVo.getOrgnId());
        reqVo.setOldOrgnId(vo.getOrgnId());
        
        vo = (HrVO) SpUtils.objectMap(reqVo, vo);

        //1.인원 정보 수정 (tb_hr_info)
        hrDao.updateHr(vo);

        //2.사용자 정보 수정  - 성명, 사용여부, 전화번호, 이메일 (tb_user_info)
        vo.setUserNm(reqVo.getHrNm());
        vo.setUseYn(reqVo.getUseYn());
        if (vo.getPhone() != null) vo.setPhone(Aes256.encryptString(reqVo.getPhone()));
        if (vo.getEmail() != null) vo.setEmail(Aes256.encryptString(reqVo.getEmail()));
        hrDao.updateUserHr(vo);

        //3.사업장 매핑 정보 설정 (tb_hr_info_comp_map)
        
        // 현재 인원의 사업장 매핑 정보 조회
        UserManageVO userVo = new UserManageVO();
        userVo.setUserId(reqVo.getUserId());
        String beforeCompId = userManageDAO.getBeforeHrInfoCompMap(userVo);

        userManageDAO.deleteHrInfoCompMap(userVo);
        UserManageVO orgnCompYnY = userVo;
        // 소속 사업장 데이터가 있는 경우만 처리

        orgnCompYnY.setCompId(reqVo.getAffCompId());
        orgnCompYnY.setOrgnCompYn("Y");
        orgnCompYnY.setHrId(reqVo.getHrId());
        userManageDAO.insertHrInfoCompMap(orgnCompYnY);
        if (reqVo.getAffCompId() != null && !reqVo.getAffCompId().equals(beforeCompId)) {
            // 사업장 변경에 따라 파일 경로, 위치도 수정함
            fileService.moveFile("hrImg", reqVo.getHrId(), beforeCompId, reqVo.getAffCompId());
        }

        //관리 사업장 데이터
        UserManageVO orgnCompYnN = userVo;
        orgnCompYnN.setOrgnCompYn("N");
        orgnCompYnN.setCompId(reqVo.getMgrCompId());
        orgnCompYnN.setHrId(reqVo.getHrId());
        if (reqVo.getMgrCompId() != null && !reqVo.getMgrCompId().isEmpty()) {
            if (reqVo.getMgrCompId().contains(";")) {
                String[] splitText = reqVo.getMgrCompId().split(";");
                for (int i = 0; i < splitText.length; i++) {
                    orgnCompYnN.setCompId(splitText[i]);
                    userManageDAO.insertHrInfoCompMap(orgnCompYnN);
                }
            } else {
                userManageDAO.insertHrInfoCompMap(orgnCompYnN);
            }
        }

        //4.조직 변경 이력 업데이트, 추가
        if (!orgnChangeYn) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String today = LocalDate.now().format(formatter);
            reqVo.setStDate(today);
            reqVo.setEdDate(today);
            //edDate가 null인 경우 업데이트하기 때문에 순서를 항상 업데이트하고 insert해야됨
            hrDao.updateOrgnHistoryHr(reqVo);
            hrDao.addOrgnHistoryHr(reqVo);
        }

        //5.파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "hrImg", reqVo.getHrId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "hrImg", reqVo.getHrId(), SecurityUtil.getCurrentCompId(), "base");
        vo.setFileId(fileId);   //수정된 파일의 file id를 front에 적용해줌
        reqVo.setFileId(fileId);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteHr(HrVO reqVo) throws Exception {
        BaseVO vo = hrDao.getHrById(reqVo.getHrId());
        deleteHrProcess(reqVo);

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHrs(List<HrVO> list) throws Exception {
        for (HrVO tmp : list) {
            deleteHrProcess(tmp);
        }
    }

    public void deleteHrProcess(HrVO reqVo) {
        //인원 삭제
        hrDao.deleteHr(reqVo);
        hrDao.deleteHrInfoCompMap(reqVo);
        //사용자 삭제
        hrDao.deleteUserHr(reqVo.getHrId());
    }

    // 인원 매핑의 hrId 기반으로 인원 정보 넣음
    // 아래의 코드를 이용해 조회 후, 해당 함수 사용
    // BaseMapVO baseMapVO = new BaseMapVO("targetType",targetId);
    // List<HrVO> hrList = hrDao.getHrMapDetail(baseMapVO);
    // 사용예시 : HrService.setHrInfoByGubun(hrList, "HEAD", voList::setHrListH);
    static public void setHrInfoByGubun(List<HrVO> hrList, String gubun, Consumer<HrVO> setter) {
        HrVO result = hrList.stream()
                .filter(hr -> gubun.equals(hr.getGubun()))
                .findFirst()
                .orElse(null);

        if (result != null) {
            setter.accept(result);
        }
    }

    // 직위 관리
    // 저장된 직위 데이터 조회
    public List<HrJbrpVO> getHrJbrp() {
        List<HrJbrpVO> resultList = new ArrayList<>();
        String compId = SecurityUtil.getCurrentCompId();
        resultList = hrDao.getHrJbrp(compId);
        return resultList;
    }

    // 인원 관리
    // 소속 사업장 직위 데이터 조회
    public List<HrJbrpVO> getHrJbrpByCompId(String compId) {
        List<HrJbrpVO> resultList = new ArrayList<>();
        resultList = hrDao.getHrJbrp(compId);
        return resultList;
    }

    // 직위 예시 데이터 조회
    public List<HrJbrpVO> getDatasetHrJbrp() {
        List<HrJbrpVO> resultList = new ArrayList<>();
        resultList = hrDao.getDatasetHrJbrp();
        return resultList;
    }

    // 직위 데이터 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHrJbrp(List<HrJbrpVO> voList) {
        for (HrJbrpVO vo : voList) {
            if (vo.getOrdSeq() == null) {
                vo.setOrdSeq(99);
            }

            if ("I".equals(vo.getCmd())) {
                hrDao.insertHrJbrp(vo);
            } else {
                hrDao.updateHrJbrp(vo);
            }
        }
        return voList.get(0);
    }

    // 직위 데이터 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteHrJbrp(List<HrJbrpVO> voList) {
        for (HrJbrpVO vo : voList) {
            hrDao.deleteHrJbrp(vo);
        }
    }

    // 비밀번호 변경
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO resetPassword(UserManageVO vo) {
        UserManageVO userVo = hrDao.getUserInfo(vo);
        if (userVo != null) {
            vo.setUserPs(userVo.getUserId());
            vo.passwordEncode(passwordEncoder);
            vo.setUserId(userVo.getUserId());
            hrDao.resetPassword(vo);
        }
        return vo;
    }
}
