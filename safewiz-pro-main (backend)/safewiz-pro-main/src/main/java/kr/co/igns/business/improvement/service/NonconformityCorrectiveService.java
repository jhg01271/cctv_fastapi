package kr.co.igns.business.improvement.service;

import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.improvement.model.*;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.improvement.dao.postgres.NonconformityCorrectiveDAO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service
@RequiredArgsConstructor
public class NonconformityCorrectiveService {
    private final NonconformityCorrectiveDAO nonconformityCorrectiveDao;
    private final ReportService reportService;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final NasFileService nasFileService;
    private final UtilsDAO utilsDAO;


    public List<ContinualImprovementVO> getParticipation(@Valid SpSearchVO searchVo) {
        return nonconformityCorrectiveDao.getParticipation(searchVo);
    }

    public List<NearMissReportVO> getNearMissReport(SpSearchVO searchVo) throws Exception {
        return nonconformityCorrectiveDao.getNearMissReport(searchVo);
    }

    public NearMissReportVO getNearMissReportDetail(String docNo) throws Exception {
        NearMissReportVO vo = nonconformityCorrectiveDao.getNearMissReportDetail(docNo);
        vo.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "NMR"));
        return vo;
    }

    public NearMissReportVO getOrgnHead(String actionOrgnId) throws Exception {
        return nonconformityCorrectiveDao.getOrgnHead(actionOrgnId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertNearMissReport(List<MultipartFile> files, NearMissReportVO vo) throws Exception {
        nonconformityCorrectiveDao.insertNearMissReport(vo);
        String targetType = "NMR";
        // 파일 저장
        fileService.saveFile(files, targetType, LocalDate.now().getYear() + vo.getDocNo(),
                SecurityUtil.getCurrentCompId());
        vo.setWriteYear(String.valueOf(LocalDate.now().getYear()));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateNearMissReport(List<MultipartFile> files, NearMissReportVO vo) throws Exception {
        nonconformityCorrectiveDao.updateNearMissReport(vo);
        String targetType = "NMR";

        fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear() + vo.getDocNo(),
                SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(files, targetType, vo.getWriteYear() + vo.getDocNo(),
                SecurityUtil.getCurrentCompId());
        vo.setFileId(fileId); // 수정
        return vo;
    }

    public BaseVO deleteNearMissReport(NearMissReportVO vo) throws Exception {
        nonconformityCorrectiveDao.deleteNearMissReport(vo);
        return vo;
    }

    public void deleteNearMissReports(List<NearMissReportVO> list) throws Exception {
        for (NearMissReportVO tmp : list) {
            nonconformityCorrectiveDao.deleteNearMissReport(tmp);
        }
    }

    public List<JasperPrint> printNearMissReports(HttpServletRequest request, HttpServletResponse response,
                                                  SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "아차사고 보고서";
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/improvement/NearMissReport/NearMissReport.jrxml");
            reportVO.setType("pdf");

            NearMissReportVO result = getNearMissReportDetail(vo.getDocNo());
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getCreatOrgnNm();
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            params.put("creatTeamNm", result.getCreatOrgnNm());
            params.put("creatName", result.getHrNm());
            
            // 날짜 형식 변환
            if(result.getWriteDt() != null && !result.getWriteDt().equals("")) {
            	result.setWriteDt(DateUtils.formatDate(result.getWriteDt()));
            }else {
            	result.setWriteDt("");
            }
            params.put("creatDate", result.getWriteDt());
            
            // 날짜 형식 변환
            if(result.getReceiptDt() != null && !result.getReceiptDt().equals("")) {
            	result.setReceiptDt(DateUtils.formatDate(result.getReceiptDt()));
            }else {
            	result.setReceiptDt("");
            }
            params.put("receiptDt", result.getReceiptDt());
            
            // 날짜 형식 변환
            if(result.getActionDt() != null && !result.getActionDt().equals("")) {
            	result.setActionDt(DateUtils.formatDate(result.getActionDt()));
            }else {
            	result.setActionDt("");
            }
            params.put("actionDt", result.getActionDt());
            
            params.put("creatOwner", ""); // 작성조직 서명 담당
            params.put("creatLeader", ""); // 작성조직 서명 조직장
            params.put("actionTeamNm", result.getActionOrgnNm());
            params.put("actionOwner", ""); // 조치조직 서명 담당
            params.put("actionLeader", ""); // 조치조직 서명 담당
            params.put("accidentContent", result.getAccidentContent());
            params.put("accidentImproved", result.getAccidentImproved());
            params.put("dangerContent", result.getDangerContent());
            params.put("dangerImproved", result.getDangerImproved());
            params.put("actionContent", result.getActionContent());

            SpSearchVO creatSpSearchVO = new SpSearchVO();
            creatSpSearchVO.setWriteYear(result.getWriteYear());
            creatSpSearchVO.setDocNo(result.getDocNo());
            creatSpSearchVO.setDocType("NMR");
            creatSpSearchVO.setType("CREAT_NEAR");
//            List<Object> creatApprovalInfo = utilsService.getApprovalInfoToArray(creatSpSearchVO);
//
//            params.put("creatOwnerSign", creatApprovalInfo.get(0));
//            params.put("creatOwner", creatApprovalInfo.get(1));
//            params.put("creatLeaderSign", creatApprovalInfo.get(2));
//            params.put("creatLeader", creatApprovalInfo.get(3));

            List<Map<String, Object>> creatApprovalInfo = utilsService.getApprovalInfoToArray(creatSpSearchVO);
            params.put("signatureList", new JRBeanCollectionDataSource(creatApprovalInfo));


            SpSearchVO actionSpSearchVO = new SpSearchVO();
            actionSpSearchVO.setWriteYear(result.getWriteYear());
            actionSpSearchVO.setDocNo(result.getDocNo());
            actionSpSearchVO.setDocType("NMR");
            actionSpSearchVO.setType("ACTION_NEAR");
//            List<Object> actionApprovalInfo = utilsService.getApprovalInfoToArray(actionSpSearchVO);
//
//            params.put("actionOwnerSign", actionApprovalInfo.get(4));
//            params.put("actionOwner", actionApprovalInfo.get(5));
//            params.put("actionLeaderSign", actionApprovalInfo.get(6));
//            params.put("actionLeader", actionApprovalInfo.get(7));

            List<Map<String, Object>> actionApprovalInfo = utilsService.getApprovalInfoToArray(actionSpSearchVO);
            params.put("signatureList2", new JRBeanCollectionDataSource(actionApprovalInfo));

            // 파일 목록
            List<Map<String, Object>> files = new ArrayList<>();
            List<FileVO> fileVOList = nonconformityCorrectiveDao.getNearMissReportFileList(result);

            for (FileVO fileVO : fileVOList) {
                HashMap<String, Object> image = new HashMap<>();
                // 이미지 파일만 추출
                if (fileVO.getDocType().equals("IMAGE")) {
                    image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                    files.add(image);
                }
            }
            // 파일이 없을때 null 이라도 넣어서 출력물 디자인 조정
            if (files.isEmpty()) {
                HashMap<String, Object> image = new HashMap<>();
                image.put("img", null);
                files.add(image);
            }

            params.put("files", new JRBeanCollectionDataSource(files));

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
            spSearchVO.setPage(page);
            spSearchVO.setLocalPage(localPage);

        }
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return null;
    }

    public List<CorrectiveActionRequestVO> getCorrectiveActionRequest(CorrectiveActionRequestVO vo) throws Exception {
        List<CorrectiveActionRequestVO> voList = nonconformityCorrectiveDao.getCorrectiveActionRequest(vo);
        return voList;
    }

    public CorrectiveActionRequestVO getCorrectiveActionRequestDetail(SpSearchVO vo) throws Exception {
        CorrectiveActionRequestVO result = nonconformityCorrectiveDao.getCorrectiveActionRequestDetail(vo);
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertCorrectiveActionRequest(CorrectiveActionRequestVO vo) throws Exception {
        vo.setCompId(SecurityUtil.getCurrentCompId());
//		LocalDate now = LocalDate.now();
//		int year = now.getYear();
//		vo.setWriteYear(String.valueOf(year));
        nonconformityCorrectiveDao.insertCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateCorrectiveActionRequest(CorrectiveActionRequestVO vo) throws Exception {
        nonconformityCorrectiveDao.updateCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteCorrectiveActionRequest(CorrectiveActionRequestVO vo) throws Exception {
        nonconformityCorrectiveDao.deleteCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteCorrectiveActionRequests(List<CorrectiveActionRequestVO> list) throws Exception {
        for (CorrectiveActionRequestVO tmp : list) {
            nonconformityCorrectiveDao.deleteCorrectiveActionRequest(tmp);
        }
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(nonconformityCorrectiveDao.getAllReport(spSearchVO));
        if (!spSearchVO.getCheckedObjList().isEmpty()) {
            // 조직별로 타이틀 나누기
//            JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, spSearchVO.getCheckedObjList().get(0).getSearchText2());
//            reportList.add(JasperFrontReport);
//            spSearchVO.setPage(spSearchVO.getPage() + JasperFrontReport.getPages().size());
//            spSearchVO.setLocalPage(spSearchVO.getLocalPage() + JasperFrontReport.getPages().size());

            spSearchVO.setPrintAll(true);
            jaspers = printCorrectiveActionRequests(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }


    public List<JasperPrint> printCorrectiveActionRequests(HttpServletRequest request, HttpServletResponse response,
                                                           SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        String fileNm = "시정조치 요구서";
        //------------------------------ 표지 ------------------------------
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        if (!spSearchVO.isPrintAll()) {
            JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "시정조치 요구서");
            reportList.add(JasperFrontReport);
            page = page + JasperFrontReport.getPages().size();
            localPage = localPage + JasperFrontReport.getPages().size();
        }
        //------------------------------ 본문 ------------------------------
        Set<String> yearSet = new HashSet<>();
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            CorrectiveActionRequestVO result = getCorrectiveActionRequestDetail(vo); // 상세정보 조회
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getTitle();
            }
            if (!yearSet.contains(result.getWriteYear())) {  // 중복이 아닐 경우에만 추가
                yearSet.add(result.getWriteYear());
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath("report/improvement/correctiveActionRequest/CorrectiveActionRequest_Data.jrxml");
            String extra1 = "";
            if (spSearchVO.isPrintAll()) {
                extra1 = "(개선)";
            }
            Map<String, Object> param = new HashMap<>();
            param.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId())); // 로고
            param.put("page", page);
            param.put("subPage", subPage);
            param.put("localPage", localPage);
            param.put("extra1", extra1);
            param.put("title", result.getTitle()); // 제목
            param.put("docNo", result.getWriteYear() + "-" + result.getDocType() + "-" + result.getDocNo()); // 문서번호
            param.put("ctrlOrgnNm", result.getCtrlOrgnNm()); // 주관조직
            
            // 날짜 형식 변환
            if(result.getReturnDt() != null && !result.getReturnDt().equals("")) {
            	result.setReturnDt(DateUtils.formatDate(result.getReturnDt()));
            }else {
            	result.setReturnDt("");
            }
            param.put("returnDt", result.getReturnDt()); // 회신요구일
            param.put("actionOrgnNm", result.getActionOrgnNm()); // 조치조직
            param.put("actionRequestContent", result.getActionRequestContent()); // 부적합 사항 내용
            param.put("analyticPreventContent", result.getAnalyticPreventContent()); // 원인 분석 및 재발 방지대책 내용
            param.put("resultValidationYn", result.getResultValidationYn()); // 적합, 부적합(Y,N)
            param.put("resultValidationContent", result.getResultValidationContent()); // 시정조치 결과 유효성 확인 내용

            // ============================== 서명start
            // =========================================
            // 부적합 사항 - 서명 데이터 조회
            UtilsVO signParam1 = new UtilsVO();
            signParam1.setTargetType(result.getDocType());
            signParam1.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam1.setType("sign");

            List<UtilsVO> signList = utilsService.getApprovalInfo(signParam1); // 서명 데이터 조회
            if (!signList.isEmpty()) {
                for (UtilsVO signInfo : signList) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("writer")) {
                        param.put("reviewer1",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm1", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("reviewer")) {
                        param.put("approverNm1", signInfo.getHrNm());
                        param.put("approver1",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                    }
                }
            } else {
                param.put("reviewer1", "");
                param.put("reviewerNm1", "");
                param.put("approverNm1", "");
                param.put("approver1", "");
            }

            // 원인 분석 및 재발 방지 대책 - 서명 데이터 조회
            UtilsVO signParam2 = new UtilsVO();
            signParam2.setTargetType(result.getDocType());
            signParam2.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam2.setType("sign");

            List<UtilsVO> signList2 = utilsService.getApprovalInfo(signParam2); // 서명 데이터 조회
            if (!signList2.isEmpty()) {
                for (UtilsVO signInfo : signList2) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("approver")) {
                        param.put("reviewer2",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm2", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("verification")) {
                        param.put("approver2",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("approverNm2", signInfo.getHrNm());
                    }
                }
            } else {
                param.put("reviewer2", "");
                param.put("reviewerNm2", "");
                param.put("approver2", "");
                param.put("approverNm2", "");
            }

            // 시정조치 결과 유효성 확인 - 서명 데이터 조회
            UtilsVO signParam3 = new UtilsVO();
            signParam3.setTargetType(result.getDocType());
            signParam3.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam3.setType("sign");

            List<UtilsVO> signList3 = utilsService.getApprovalInfo(signParam3); // 서명 데이터 조회
            if (!signList3.isEmpty()) {
                for (UtilsVO signInfo : signList3) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("approvalReview")) {
                        param.put("reviewer3",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm3", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("finalApproval")) {
                        param.put("approver3",
                                utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("approverNm3", signInfo.getHrNm());
                    }
                }
            } else {
                param.put("reviewer3", "");
                param.put("reviewerNm3", "");
                param.put("approver3", "");
                param.put("approverNm3", "");
            }
            reportVO.setParameter(param);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String year : yearSet) {
            joiner.add(year);
        }
        fileNm = "(" + joiner.toString() + ")" + fileNm;

        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }


    // 인원 정보 디코딩
    private void decrypt(IncidentReportVO Vo) {
        if (Vo.getPhone() != null)
            Vo.setPhone(Aes256.decryptString(Vo.getPhone()));
        if (Vo.getWphone() != null)
            Vo.setWphone(Aes256.decryptString(Vo.getWphone()));
    }

    public List<IncidentReportVO> getIncidentReport(@Valid SpSearchVO searchVo) {
        List<IncidentReportVO> voList = nonconformityCorrectiveDao.getIncidentReport(searchVo);

        return voList;
    }

    public List<IncidentReportVO> getIncidentDetailReport(IncidentReportVO vo) {
        IncidentReportVO voList = nonconformityCorrectiveDao.getIncidentDetailReport(vo);
        if (voList != null) decrypt(voList);
        List<IncidentReportVO> objectList = new ArrayList<>();
        objectList.add(voList);
        return objectList;
    }

    public List<IncidentReportVO> getIncidentDetailManage(IncidentReportVO vo) {
        List<IncidentReportVO> voList = nonconformityCorrectiveDao.getIncidentDetailManage(vo);
        return voList;
    }

    public List<IncidentReportVO> getIncidentDetailExtent(IncidentReportVO vo) {
        List<IncidentReportVO> voList = nonconformityCorrectiveDao.getIncidentDetailExtent(vo);

        for (IncidentReportVO item : voList) {
            String fileKey = item.getWriteYear() + item.getDocNo() + item.getDocSeq(); // 파일 키 생성
            item.setFiles(fileService.getFileList(fileKey, "ICR")); // 파일 데이터 설정

        }

        return voList;
    }

    public List<IncidentReportVO> getIncidentDetailOpinion(IncidentReportVO vo) {
        List<IncidentReportVO> voList = nonconformityCorrectiveDao.getIncidentDetailOpinion(vo);
        return voList;
    }

    public List<IncidentReportVO> getIncidentState(IncidentReportVO vo) {

        IncidentReportVO voList = nonconformityCorrectiveDao.getIncidentState(vo);
        decrypt(voList);
        List<IncidentReportVO> objectList = new ArrayList<>();
        objectList.add(voList);
        return objectList;
    }

    public String getdoc(String writeYear) {
        return nonconformityCorrectiveDao.getdoc(writeYear);
    }

    // 재해발생 보고서 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertIncidentReport(IncidentReportVO vo) {

        nonconformityCorrectiveDao.insertIncidentReport(vo);

        return vo;
    }

    // 재해발생 보고서 상세내역 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertIncidentReportDetail(List<MultipartFile> files, IncidentReportVO vo) throws Exception {
        String docSeq = nonconformityCorrectiveDao.getDetailSeq(vo.getWriteYear(), vo.getDocNo());
        nonconformityCorrectiveDao.insertIncidentReportDetail(vo);

        String targetType = "ICR";

        fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear() + vo.getDocNo() + docSeq,
                SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(files, targetType, vo.getWriteYear() + vo.getDocNo() + docSeq,
                SecurityUtil.getCurrentCompId());
        vo.setFileId(fileId);
        return vo;
    }

    // 재해발생 보고서 사고처리 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertIncidentReportManage(IncidentReportVO vo) {

        nonconformityCorrectiveDao.insertIncidentReportManage(vo);

        return vo;
    }

    // 재해발생 보고서 사고처리 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertIncidentReportOpinion(IncidentReportVO vo) {

        nonconformityCorrectiveDao.insertIncidentReportOpinion(vo);

        return vo;
    }

    // 재해발생 보고서 진술서 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertIncidentReportStatment(IncidentReportVO vo) {

        nonconformityCorrectiveDao.insertIncidentReportStatment(vo);
        String docSeq = nonconformityCorrectiveDao.getStatementdocSeq(vo);
        vo.setDocSeq(docSeq);
        return vo;
    }


    // 재해발생 보고서 수정
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateIncidentReportuseYn(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.updateIncidentReportuseYn(vo);

        return vo;
    }

    // 재해발생 보고서 수정
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateIncidentReport(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.updateIncidentReport(vo);

        return vo;
    }

    // 재해발생 보고서 상세수정
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateIncidentReportDetail(List<MultipartFile> files, IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.updateIncidentReportDetail(vo);

        String targetType = "ICR";

        fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(),
                SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(files, targetType, vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(),
                SecurityUtil.getCurrentCompId());
        vo.setFileId(fileId);
        return vo;
    }

    // 재해발생 보고서 사건처리수정
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO UpdateIncidentReportManage(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.UpdateIncidentReportManage(vo);

        return vo;
    }

    // 재해발생 보고서 대책예방 및 의견 수정
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO UpdateIncidentReportOpinion(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.UpdateIncidentReportOpinion(vo);

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateIncidentReportStatment(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.updateIncidentReportStatment(vo);

        return vo;
    }

    public BaseVO deleteIncidentReport(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.deleteIncidentReport(vo);
        return vo;
    }

    public void deleteIncidentReport(List<IncidentReportVO> list) throws Exception {
        for (IncidentReportVO vo : list)
            nonconformityCorrectiveDao.deleteIncidentReport(vo);
    }

    public void deleteIncidentDetail(List<IncidentReportVO> list) throws Exception {
        for (IncidentReportVO vo : list)
            nonconformityCorrectiveDao.deleteIncidentDetail(vo);
    }

    public void deleteIncidentHospi(List<IncidentReportVO> list) throws Exception {
        for (IncidentReportVO vo : list)
            nonconformityCorrectiveDao.deleteIncidentHospi(vo);
    }

    public void deleteIncidentOpinion(List<IncidentReportVO> list) throws Exception {
        for (IncidentReportVO vo : list)
            nonconformityCorrectiveDao.deleteIncidentOpinion(vo);
    }

    public void DeteleStatement(IncidentReportVO vo) throws Exception {
        nonconformityCorrectiveDao.DeteleStatement(vo);
    }

    // 통합 보고서 출력
    public void IncidentReportCombine(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        String name = "";
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ") ";
        String title = "재해발생보고서";
        if (spSearchVO.getCheckedObjList().size() > 1) {
            fileNm += title;
        } else {
            name = nonconformityCorrectiveDao.getIncidentPersonInfo(spSearchVO.getCheckedObjList().get(0)).getIncidentPersonNm();
            fileNm += title + "_" + name;
        }

        int page = spSearchVO.getPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        jasperPrintList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        for (SpSearchVO obj : spSearchVO.getCheckedObjList()) {
            // 각 데이터 항목에 대한 개별 처리
            SpSearchVO itemSpSearchVO = new SpSearchVO();

            // itemSpSearchVO에 obj의 필드를 복사
            itemSpSearchVO.setWriteYear(obj.getWriteYear());
            itemSpSearchVO.setDocType(obj.getDocType());
            itemSpSearchVO.setDocNo(obj.getDocNo());
            itemSpSearchVO.setCompId(obj.getCompId());

            // itemSpSearchVO의 CheckedObjList 초기화 및 obj 추가
            if (itemSpSearchVO.getCheckedObjList() == null) {
                itemSpSearchVO.setCheckedObjList(new ArrayList<SpSearchVO>());
            }
            itemSpSearchVO.getCheckedObjList().add(obj);  // obj를 CheckedObjList에 추가

            // 보고서 생성
            List<JasperPrint> incidentReportPrintData = IncidentReportPrintData(itemSpSearchVO, page);
            if (incidentReportPrintData != null && !incidentReportPrintData.isEmpty()) {
                jasperPrintList.addAll(incidentReportPrintData);
                page += incidentReportPrintData.stream().mapToInt(print -> print.getPages().size()).sum(); // 페이지 증가
            }

            // 사고자 진술서 생성
            List<JasperPrint> incidentReportStatementPrintData = IncidentReportStatementPrintData(itemSpSearchVO, page);
            if (incidentReportStatementPrintData != null && !incidentReportStatementPrintData.isEmpty()) {
                jasperPrintList.addAll(incidentReportStatementPrintData);
                page += incidentReportStatementPrintData.stream().mapToInt(print -> print.getPages().size()).sum(); // 페이지 증가
            }

            // 목격자 진술서 생성
            List<JasperPrint> witnessReportStatementPrintData = WitnessReportStatementPrintData(itemSpSearchVO, page);
            if (witnessReportStatementPrintData != null && !witnessReportStatementPrintData.isEmpty()) {
                jasperPrintList.addAll(witnessReportStatementPrintData);
                page += witnessReportStatementPrintData.stream().mapToInt(print -> print.getPages().size()).sum(); // 페이지 증가
            }

            itemSpSearchVO = null;
        }
//		// IncidentReportPrintData가 비어 있지 않은 경우에만 추가
//		List<JasperPrint> incidentReportPrintData = IncidentReportPrintData(spSearchVO, page);
//		if (incidentReportPrintData != null && !incidentReportPrintData.isEmpty()) {
//			jasperPrintList.addAll(incidentReportPrintData);
//		}
//
//		// IncidentReportStatementPrintData가 비어 있지 않은 경우에만 추가
//		List<JasperPrint> incidentReportStatementPrintData = IncidentReportStatementPrintData(spSearchVO, page);
//		if (incidentReportStatementPrintData != null && !incidentReportStatementPrintData.isEmpty()) {
//			jasperPrintList.addAll(incidentReportStatementPrintData);
//		}
//
//		// WitnessReportStatementPrintData가 비어 있지 않은 경우에만 추가
//		List<JasperPrint> witnessReportStatementPrintData = WitnessReportStatementPrintData(spSearchVO, page);
//		if (witnessReportStatementPrintData != null && !witnessReportStatementPrintData.isEmpty()) {
//			jasperPrintList.addAll(witnessReportStatementPrintData);
//		}

        reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
    }

    // 재해발생 보고서 출력
    public void IncidentReportPrint(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        String name = nonconformityCorrectiveDao.getIncidentPersonInfo(spSearchVO.getCheckedObjList().get(0)).getIncidentPersonNm();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ") ";
        String title = "재해발생보고서";
        fileNm += title + "_" + name;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        jasperPrintList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        // IncidentReportPrintData가 비어 있지 않은 경우에만 추가
        List<JasperPrint> incidentReportPrintData = IncidentReportPrintData(spSearchVO, page);
        if (incidentReportPrintData != null && !incidentReportPrintData.isEmpty()) {
            jasperPrintList.addAll(incidentReportPrintData);
        }

        reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
    }

    public void IncidentReportPrintchecked(HttpServletRequest request, HttpServletResponse response, List<Object> param) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        // param.get(0)을 SpSearchVO로 변환
        Map<String, Object> headerData = (Map<String, Object>) param.get(0);
        SpSearchVO spSearchVO = new SpSearchVO();
        spSearchVO.setWriteYear((String) headerData.get("writeYear"));
        spSearchVO.setDocNo((String) headerData.get("docNo"));

        // 파일명 생성
        String name = nonconformityCorrectiveDao.getIncidentPersonInfo(spSearchVO).getIncidentPersonNm();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + headerData.get("writeYear") + ") ";
        String title = "재해발생보고서";
        fileNm += title + "_" + name;

        // 페이지 정보 설정
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        // 프론트 리포트 추가
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();

        // incidentReportPrintCheckedData가 비어 있지 않은 경우에만 추가
        List<JasperPrint> incidentReportPrintCheckedData = incidentReportPrintCheckedData(param, page);
        if (incidentReportPrintCheckedData != null && !incidentReportPrintCheckedData.isEmpty()) {
            jasperPrintList.addAll(incidentReportPrintCheckedData);
        }
        // 리포트 출력
        Map<String, Object> mapData = (Map<String, Object>) param.get(0);
        reportService.exportReportAll(request, response, jasperPrintList, (String) mapData.get("type"), fileNm);
    }

    // 사고자 진술서 출력
    public void IncidentReportStatementPrint(HttpServletRequest request, HttpServletResponse response,
                                             SpSearchVO spSearchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();
        String name = nonconformityCorrectiveDao.getIncidentPersonInfo(spSearchVO.getCheckedObjList().get(0)).getIncidentPersonNm();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "재해자 진술서";
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ") ";
        fileNm += title + "_" + name;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        page = page + JasperFrontReport.getPages().size();
        jasperPrintList.add(JasperFrontReport);

        // IncidentReportStatementPrintData가 비어 있지 않은 경우에만 추가
        List<JasperPrint> incidentReportStatementPrintData = IncidentReportStatementPrintData(spSearchVO, page);
        if (incidentReportStatementPrintData != null && !incidentReportStatementPrintData.isEmpty()) {
            jasperPrintList.addAll(incidentReportStatementPrintData);
        }

        reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);
    }

    // 목격자 진술서 출력
    public void WitnessReportStatementPrint(HttpServletRequest request, HttpServletResponse response,
                                            SpSearchVO spSearchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();
        String name = nonconformityCorrectiveDao.getIncidentPersonInfo(spSearchVO.getCheckedObjList().get(0)).getWitnessPersonNm();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "목격자 진술서";
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ") ";
        fileNm += title + "_" + name;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        page = page + JasperFrontReport.getPages().size();
        jasperPrintList.add(JasperFrontReport);

        // WitnessReportStatementPrintData가 비어 있지 않은 경우에만 추가
        List<JasperPrint> witnessReportStatementPrintData = WitnessReportStatementPrintData(spSearchVO, page);
        if (witnessReportStatementPrintData != null && !witnessReportStatementPrintData.isEmpty()) {
            jasperPrintList.addAll(witnessReportStatementPrintData);
        }

        reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);
    }

    // 재해발생 보고서 호출
    public List<JasperPrint> IncidentReportPrintData(SpSearchVO spSearchVO, int page) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();

        //String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";

        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        // page = page + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            // String fileNm = String.format("(%s) 재해발생보고서_%s",
            // tmp2.getWriteYear(),tmp2.getIncidentPersonNm());
            //reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/improvement/incidentReport/Incident_report.jrxml");
            reportVO.setType(vo.getType());
            IncidentReportVO voList = nonconformityCorrectiveDao.IncidentReportPrint(vo);
            decrypt(voList);
            Map<String, Object> introParams = new HashMap<>();
            introParams.put("orgn_Nm", voList.getOrgnNm()); // 사고자 조직명
            introParams.put("Name", voList.getIncidentPersonNm()); // 사고자명
            introParams.put("working_At", voList.getWorkingAt() != null && !voList.getWorkingAt().isEmpty() ? DateUtils.formatDate(voList.getWorkingAt()) : ""); // 사고자입사일
            introParams.put("birth_Dt", voList.getBirthDt() != null && !voList.getBirthDt().isEmpty() ? DateUtils.formatDate(voList.getBirthDt()) : ""); // 사고자 생년월일
            introParams.put("e_No", voList.getSabun()); // 사고자 사번
            introParams.put("Tel", voList.getPhone()); // 사고자 연락처
            introParams.put("Addrs", voList.getAddrs()); // 사고자 연락처

            introParams.put("jbrp_nm", voList.getJbrpNm());// 사고자 직위
            introParams.put("worgn_Nm", voList.getWorgnNm()); // 목격자 조직명
            introParams.put("wName", voList.getWitnessPersonNm()); // 목격자명
            introParams.put("we_No", voList.getWsabun()); // 목격자 사번
            introParams.put("wTel", voList.getWphone()); // 목격자 연락처

            introParams.put("incident_Dt", voList.getIncidentDt()); // 사고일자
            introParams.put("incident_Loc", voList.getIncidentLocation()); // 사고장소

            introParams.put("incident_Extent", voList.getIncidentExtent()); // 사고정도
            introParams.put("comp_opinion", voList.getCompOpinion()); // 사업장의견

            introParams.put("incident_Type", voList.getIncidentTypenm()); // 처리구분
            introParams.put("incident_His", voList.getIncidentHis()); // 사고경력
            introParams.put("incident_pm_so", voList.getIncidentPmSo()); // 사고경력

            introParams.put("page", page);
            introParams.put("subPage", subPage);
            introParams.put("localPage", localPage);

            List<Map<String, Object>> datasource = new ArrayList<>(); // 사고경위
            List<IncidentReportVO> temp1 = nonconformityCorrectiveDao.getIncidentDetailExtent(voList);
            if (temp1 == null || temp1.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();
                Emptydata.put("incident_Detail", "");

                List<Map<String, Object>> files = new ArrayList<>();
                HashMap<String, Object> image = new HashMap<>();
                Emptydata.put("img", null);
                files.add(image);
                Emptydata.put("Files", new JRBeanCollectionDataSource(files));
                datasource.add(Emptydata);
                ;
            } else {
                for (IncidentReportVO tmp3 : temp1) {
                    if (tmp3.getUseYn().equals("Y")) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        // System.out.println(tmp3);
                        data.put("incident_Detail", tmp3.getIncidentDetail());

                        // 파일 목록
                        List<Map<String, Object>> files = new ArrayList<>();
                        List<FileVO> fileVOList = nonconformityCorrectiveDao.getIncidentReportFileList(tmp3);

                        for (FileVO fileVO : fileVOList) {
                            HashMap<String, Object> image = new HashMap<>();
                            // 이미지 파일만 추출
                            if (fileVO.getDocType().equals("IMAGE")) {
                                // image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                                image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                                files.add(image);
                            }
                        }
                        // 파일이 없을때 null 이라도 넣어서 출력물 디자인 조정
                        if (files.isEmpty()) {
                            HashMap<String, Object> image = new HashMap<>();
                            // image.put("img", null);
                            image.put("img", null);
                            // files.add(image);
                            files.add(image);
                        }

                        data.put("Files", new JRBeanCollectionDataSource(files));
                        datasource.add(data);
                    }
                }
            }

            if (datasource.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();
                Emptydata.put("incident_Detail", "");

                List<Map<String, Object>> files = new ArrayList<>();
                HashMap<String, Object> image = new HashMap<>();
                Emptydata.put("img", null);
                files.add(image);
                Emptydata.put("Files", new JRBeanCollectionDataSource(files));
                datasource.add(Emptydata);
            }

            List<Map<String, Object>> datasource2 = new ArrayList<>(); // 사고처리(병원)
            List<IncidentReportVO> temp2 = nonconformityCorrectiveDao.getIncidentDetailManage(voList);

            if (temp2 == null || temp2.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();

                Emptydata.put("hospital_nm", "");
                Emptydata.put("hospital_tel", "");
                Emptydata.put("hospital_pl", "");
                Emptydata.put("hosp_in_fr_dt", "");
                Emptydata.put("hosp_in_to_dt", "");
                Emptydata.put("hosp_out_fr_dt", "");
                Emptydata.put("hosp_out_to_dt", "");
                Emptydata.put("hospin", "");
                Emptydata.put("hospout", "");

                datasource2.add(Emptydata);
            } else {
                for (IncidentReportVO tmp3 : temp2) {
                    if (tmp3.getUseYn().equals("Y")) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        // System.out.println(tmp3);
                        data.put("hospital_nm", tmp3.getHospitalNm());
                        data.put("hospital_tel", tmp3.getHospitalTel());
                        data.put("hospital_pl", tmp3.getHospitalPl());
                        if(tmp3.getHospInfrDt() != null) {
                        	data.put("hosp_in_fr_dt", DateUtils.formatDate(tmp3.getHospInfrDt()));
                        }else {
                        	data.put("hosp_in_fr_dt", "");
                        }
                        
                        if(tmp3.getHospIntoDt() != null) {
                        	data.put("hosp_in_to_dt", DateUtils.formatDate(tmp3.getHospIntoDt()));
                        }else {
                        	data.put("hosp_in_to_dt", "");
                        }
                        
                        if(tmp3.getHospOutfrDt() != null) {
                        	data.put("hosp_out_fr_dt", DateUtils.formatDate(tmp3.getHospOutfrDt()));
                        }else {
                        	data.put("hosp_out_fr_dt", "");
                        }

                        if(tmp3.getHospOuttoDt() != null) {
                        	data.put("hosp_out_to_dt", DateUtils.formatDate(tmp3.getHospOuttoDt()));
                        }else {
                        	data.put("hosp_out_to_dt", "");
                        }
                        data.put("hospin", tmp3.getHospIn());
                        data.put("hospout", tmp3.getHospOut());
                        datasource2.add(data);
                    }
                }
            }

            if (datasource2.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();

                Emptydata.put("hospital_nm", "");
                Emptydata.put("hospital_tel", "");
                Emptydata.put("hospital_pl", "");
                Emptydata.put("hosp_in_fr_dt", "");
                Emptydata.put("hosp_in_to_dt", "");
                Emptydata.put("hosp_out_fr_dt", "");
                Emptydata.put("hosp_out_to_dt", "");
                Emptydata.put("hospin", "");
                Emptydata.put("hospout", "");

                datasource2.add(Emptydata);
            }

            List<Map<String, Object>> datasource3 = new ArrayList<>(); // 대책예방 및 관리자의견
            List<IncidentReportVO> temp3 = nonconformityCorrectiveDao.getIncidentDetailOpinion(voList);

            if (temp3 == null || temp3.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();
                Emptydata.put("preventive_measures", "");
                Emptydata.put("supervisors_opinion", "");
                datasource3.add(Emptydata);
            } else {
                for (IncidentReportVO tmp3 : temp3) {
                    if (tmp3.getUseYn().equals("Y")) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        // System.out.println(tmp3);
                        data.put("preventive_measures", tmp3.getPreventiveMeasures());
                        data.put("supervisors_opinion", tmp3.getSupervisorsOpinion());
                        datasource3.add(data);
                    }
                }
            }

            if (datasource3.isEmpty()) {
                Map<String, Object> Emptydata = new HashMap<String, Object>();
                Emptydata.put("preventive_measures", "");
                Emptydata.put("supervisors_opinion", "");
                datasource3.add(Emptydata);
            }

            // introParams.put("Files", new JRBeanCollectionDataSource(files));
            introParams.put("testList", new JRBeanCollectionDataSource(datasource));
            introParams.put("manageList", new JRBeanCollectionDataSource(datasource2));
            introParams.put("opiniList", new JRBeanCollectionDataSource(datasource3));
            reportVO.setParameter(introParams);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);
        }
        return jasperPrintList;
    }

    // 재해발생 보고서 호출(체크된것들만 호출하는 함수)
    public List<JasperPrint> incidentReportPrintCheckedData(List<Object> param, int page) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();

        Map<String, Object> headerData = (Map<String, Object>) param.get(0);
        SpSearchVO vo = new SpSearchVO();
        vo.setWriteYear((String) headerData.get("writeYear"));
        vo.setDocNo((String) headerData.get("docNo"));
        int subPage = vo.getSubPage();
        int localPage = vo.getLocalPage();

        ReportVO reportVO = new ReportVO();
        reportVO.setJrxmlPath("report/improvement/incidentReport/Incident_report.jrxml");
        reportVO.setType("pdf");
        IncidentReportVO voList = nonconformityCorrectiveDao.IncidentReportPrint(vo);
        decrypt(voList);
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("orgn_Nm", voList.getOrgnNm()); //사고자 조직명
        introParams.put("Name", voList.getIncidentPersonNm()); //사고자명
        introParams.put("working_At", voList.getWorkingAt()); //사고자입사일
        introParams.put("birth_Dt", voList.getBirthDt());    //사고자 생년월일
        introParams.put("e_No", voList.getSabun()); //사고자 사번
        introParams.put("Tel", voList.getPhone()); //사고자 연락처
        introParams.put("Addrs", voList.getAddrs()); //사고자 연락처

        introParams.put("jbrp_nm", voList.getJbrpNm());//사고자 직위
        introParams.put("worgn_Nm", voList.getWorgnNm()); //목격자 조직명
        introParams.put("wName", voList.getWitnessPersonNm()); //목격자명
        introParams.put("we_No", voList.getWsabun()); //목격자 사번
        introParams.put("wTel", voList.getWphone()); //목격자 연락처

        introParams.put("incident_Dt", voList.getIncidentDt()); //사고일자
        introParams.put("incident_Loc", voList.getIncidentLocation()); //사고장소

        introParams.put("incident_Extent", voList.getIncidentExtent()); //사고정도
        introParams.put("comp_opinion", voList.getCompOpinion()); //사업장의견

        introParams.put("incident_Type", voList.getIncidentTypenm()); //처리구분
        introParams.put("incident_His", voList.getIncidentHis()); //사고경력
        introParams.put("incident_pm_so", voList.getIncidentPmSo()); //예방대책 및 관리자의견

        introParams.put("page", page);
        introParams.put("subPage", subPage);
        introParams.put("localPage", localPage);

        List<Map<String, Object>> datasource = new ArrayList<>(); // 사고경위

        List<Map<String, Object>> docSeqList1 = (List<Map<String, Object>>) param.get(1);

        if (docSeqList1 == null || docSeqList1.isEmpty()) {
            Map<String, Object> Emptydata = new HashMap<String, Object>();
            Emptydata.put("incident_Detail", "");

            List<Map<String, Object>> files = new ArrayList<>();
            HashMap<String, Object> image = new HashMap<>();
            Emptydata.put("img", null);
            files.add(image);
            Emptydata.put("Files", new JRBeanCollectionDataSource(files));
            datasource.add(Emptydata);
        } else {

            for (Map<String, Object> docSeqData : docSeqList1) {
                // 기존 voList를 활용하여 docSeq만 업데이트
                voList.setDocSeq((String) docSeqData.get("docSeq")); // docSeq 설정
                List<IncidentReportVO> temp1 = nonconformityCorrectiveDao.getIncidentDetailExtentchk(voList);

                if (temp1 == null || temp1.isEmpty()) {
                    Map<String, Object> Emptydata = new HashMap<String, Object>();
                    Emptydata.put("incident_Detail", "");

                    List<Map<String, Object>> files = new ArrayList<>();
                    HashMap<String, Object> image = new HashMap<>();
                    Emptydata.put("img", null);
                    files.add(image);
                    Emptydata.put("Files", new JRBeanCollectionDataSource(files));
                    datasource.add(Emptydata);
                } else {
                    for (IncidentReportVO tmp3 : temp1) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        //System.out.println(tmp3);
                        data.put("incident_Detail", tmp3.getIncidentDetail());

                        // 파일 목록
                        List<Map<String, Object>> files = new ArrayList<>();
                        List<FileVO> fileVOList = nonconformityCorrectiveDao.getIncidentReportFileList(tmp3);

                        for (FileVO fileVO : fileVOList) {
                            HashMap<String, Object> image = new HashMap<>();
                            // 이미지 파일만 추출
                            if (fileVO.getDocType().equals("IMAGE")) {
                                //image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                                image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                                files.add(image);
                            }
                        }
                        // 파일이 없을때 null 이라도 넣어서 출력물 디자인 조정
                        if (files.isEmpty()) {
                            HashMap<String, Object> image = new HashMap<>();
                            //image.put("img", null);
                            image.put("img", null);
                            //files.add(image);
                            files.add(image);
                        }

                        data.put("Files", new JRBeanCollectionDataSource(files));
                        datasource.add(data);
                    }
                }
            }
        }

        List<Map<String, Object>> datasource2 = new ArrayList<>(); // 사고처리(병원)

        List<Map<String, Object>> docSeqList2 = (List<Map<String, Object>>) param.get(2);

        if (docSeqList2 == null || docSeqList2.isEmpty()) {
            Map<String, Object> Emptydata = new HashMap<String, Object>();

            Emptydata.put("hospital_nm", "");
            Emptydata.put("hospital_tel", "");
            Emptydata.put("hospital_pl", "");
            Emptydata.put("hosp_in_fr_dt", "");
            Emptydata.put("hosp_in_to_dt", "");
            Emptydata.put("hosp_out_fr_dt", "");
            Emptydata.put("hosp_out_to_dt", "");
            Emptydata.put("hospin", "");
            Emptydata.put("hospout", "");

            datasource2.add(Emptydata);
        } else {
            for (Map<String, Object> docSeqData : docSeqList2) {
                // 기존 voList를 활용하여 docSeq만 업데이트
                voList.setDocSeq((String) docSeqData.get("docSeq")); // docSeq 설정
                List<IncidentReportVO> temp2 = nonconformityCorrectiveDao.getIncidentDetailManagechk(voList);
                if (temp2 == null || temp2.isEmpty()) {
                    Map<String, Object> Emptydata = new HashMap<String, Object>();

                    Emptydata.put("hospital_nm", "");
                    Emptydata.put("hospital_tel", "");
                    Emptydata.put("hospital_pl", "");
                    Emptydata.put("hosp_in_fr_dt", "");
                    Emptydata.put("hosp_in_to_dt", "");
                    Emptydata.put("hosp_out_fr_dt", "");
                    Emptydata.put("hosp_out_to_dt", "");
                    Emptydata.put("hospin", "");
                    Emptydata.put("hospout", "");

                    datasource2.add(Emptydata);
                } else {
                    for (IncidentReportVO tmp3 : temp2) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        //System.out.println(tmp3);
                        data.put("hospital_nm", tmp3.getHospitalNm());
                        data.put("hospital_tel", tmp3.getHospitalTel());
                        data.put("hospital_pl", tmp3.getHospitalPl());
                        if(tmp3.getHospInfrDt() != null) {
                        	data.put("hosp_in_fr_dt", DateUtils.formatDate(tmp3.getHospInfrDt()));
                        }else {
                        	data.put("hosp_in_fr_dt", "");
                        }
                        
                        if(tmp3.getHospIntoDt() != null) {
                        	data.put("hosp_in_to_dt", DateUtils.formatDate(tmp3.getHospIntoDt()));
                        }else {
                        	data.put("hosp_in_to_dt", "");
                        }
                        
                        if(tmp3.getHospOutfrDt() != null) {
                        	data.put("hosp_out_fr_dt", DateUtils.formatDate(tmp3.getHospOutfrDt()));
                        }else {
                        	data.put("hosp_out_fr_dt", "");
                        }

                        if(tmp3.getHospOuttoDt() != null) {
                        	data.put("hosp_out_to_dt", DateUtils.formatDate(tmp3.getHospOuttoDt()));
                        }else {
                        	data.put("hosp_out_to_dt", "");
                        }
                        data.put("hospin", tmp3.getHospIn());
                        data.put("hospout", tmp3.getHospOut());
                        datasource2.add(data);
                    }
                }
            }
        }

        List<Map<String, Object>> datasource3 = new ArrayList<>(); //대책예방 및 관리자의견

        List<Map<String, Object>> docSeqList3 = (List<Map<String, Object>>) param.get(3);

        if (docSeqList3 == null || docSeqList3.isEmpty()) {
            Map<String, Object> Emptydata = new HashMap<String, Object>();
            Emptydata.put("preventive_measures", "");
            Emptydata.put("supervisors_opinion", "");
            datasource3.add(Emptydata);
        } else {
            for (Map<String, Object> docSeqData : docSeqList3) {
                // 기존 voList를 활용하여 docSeq만 업데이트
                voList.setDocSeq((String) docSeqData.get("docSeq")); // docSeq 설정
                List<IncidentReportVO> temp3 = nonconformityCorrectiveDao.getIncidentDetailOpinionchk(voList);

                if (temp3 == null || temp3.isEmpty()) {

                    Map<String, Object> Emptydata = new HashMap<String, Object>();
                    Emptydata.put("preventive_measures", "");
                    Emptydata.put("supervisors_opinion", "");
                    datasource3.add(Emptydata);
                } else {
                    for (IncidentReportVO tmp3 : temp3) {

                        Map<String, Object> data = new HashMap<String, Object>();
                        //System.out.println(tmp3);
                        data.put("preventive_measures", tmp3.getPreventiveMeasures());
                        data.put("supervisors_opinion", tmp3.getSupervisorsOpinion());
                        datasource3.add(data);
                    }
                }
            }
        }
//		            	
//		                //introParams.put("Files", new JRBeanCollectionDataSource(files));
        introParams.put("testList", new JRBeanCollectionDataSource(datasource));
        introParams.put("manageList", new JRBeanCollectionDataSource(datasource2));
        introParams.put("opiniList", new JRBeanCollectionDataSource(datasource3));
        reportVO.setParameter(introParams);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        jasperPrintList.add(JasperReport);
        System.out.println(jasperPrintList);
//		    		}
        return jasperPrintList;
    }

    // 사고자 보고서 호출
    public List<JasperPrint> IncidentReportStatementPrintData(SpSearchVO spSearchVO, int page) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();

        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";

        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/improvement/incidentReport/incident_state.jrxml");
            reportVO.setType("pdf");
            IncidentReportVO result = nonconformityCorrectiveDao.IncidentReportStatementPrint(vo);
            if (result != null) {
                Map<String, Object> introParams = new HashMap<>();
                introParams.put("Report_Title", "재해자 진술서"); // 사고자 조직명
                introParams.put("orgn_Nm", result.getOrgnNm()); // 사고자 조직명
                introParams.put("Name", result.getIncidentPersonNm()); // 사고자명
                introParams.put("working_At", result.getWorkingAt() != null && !result.getWorkingAt().isEmpty() ? DateUtils.formatDate(result.getWorkingAt()) : ""); // 사고자입사일
                introParams.put("Addrs", result.getAddrs()); // 사고자 주소지

                introParams.put("jbrp_nm", result.getJbrpNm());// 사고자 직위
                introParams.put("statement_content", result.getStatementContent()); // 진술서내용
                introParams.put("write_dt", result.getWriteDt()); // 작성일자
                introParams.put("StaticComment", result.getStaticComment()); // 고정문구 1

                introParams.put("page", page);
                introParams.put("subPage", subPage);
                introParams.put("localPage", localPage);

                // 서명정보
                UtilsVO signParam = new UtilsVO();
                signParam.setTargetType("ICR");
                signParam.setType("incidentSign");
                signParam.setTargetId(result.getWriteYear() + result.getDocNo());
                List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회
                if (!signList.isEmpty()) {
                        introParams.put("Incidentsign",
                                utilsService.getSignatureFromBase64String(signList.get(0).getSignature()));
                } else {
                    introParams.put("Incidentsign", null);
                }
                reportVO.setParameter(introParams);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO); // 여기서 오류 발생
                jasperPrintList.add(JasperReport);

            }

        }
        return jasperPrintList;
    }

    // 목격자 보고서 호출
    public List<JasperPrint> WitnessReportStatementPrintData(SpSearchVO spSearchVO, int page) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";

        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath("report/improvement/incidentReport/witness_state.jrxml");
            reportVO.setType("pdf");
            IncidentReportVO result = nonconformityCorrectiveDao.WitnessReportStatementPrint(vo);
            if (result != null) {
                Map<String, Object> introParams = new HashMap<>();
                introParams.put("Report_Title", "목격자 진술서"); // 진술서 Title
                introParams.put("orgn_Nm", result.getOrgnNm()); // 목격자 조직명
                introParams.put("Name", result.getWitnessPersonNm()); // 목격자명
                introParams.put("working_At", result.getWorkingAt() != null && !result.getWorkingAt().isEmpty() ? DateUtils.formatDate(result.getWorkingAt()) : ""); // 목격자입사일
                introParams.put("Addrs", result.getAddrs()); // 목격자 주소지

                introParams.put("jbrp_nm", result.getJbrpNm());// 목격자 직위
                introParams.put("Incident_dt",result.getIncidentDt() != null && !result.getIncidentDt().isEmpty() ? DateUtils.formatDate(result.getIncidentDt()) + ' ' + result.getIncidentTm()  : "");// 사고 목격일자
                introParams.put("statement_content", result.getStatementContent()); // 진술서내용
                introParams.put("write_dt", result.getWriteDt()); // 작성일자
                introParams.put("StaticComment", result.getStaticComment()); // 고정문구 1
                introParams.put("wStaticComment", result.getWStaticComment()); // 고정문구 2

                introParams.put("page", page);
                introParams.put("subPage", subPage);
                introParams.put("localPage", localPage);

                // 서명정보
                UtilsVO signParam = new UtilsVO();
                signParam.setTargetType("ICR");
                signParam.setType("witnessSign");
                signParam.setTargetId(result.getWriteYear() + result.getDocNo());
                List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회
                if (!signList.isEmpty()) {
                        introParams.put("witnesssign",
                                utilsService.getSignatureFromBase64String(signList.get(0).getSignature()));
                } else {
                    introParams.put("witnesssign", null);
                }

                reportVO.setParameter(introParams);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO); // 여기서 오류 발생
                jasperPrintList.add(JasperReport);
            }

        }
        return jasperPrintList;
    }
}
