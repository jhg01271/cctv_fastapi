package kr.co.igns.business.evaluation.service;

import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.evaluation.dao.postgres.EvaluationCorrectiveActionRequestDAO;
import kr.co.igns.business.evaluation.model.EvaluationCorrectiveActionRequestVO;
import kr.co.igns.business.improvement.dao.postgres.NonconformityCorrectiveDAO;
import kr.co.igns.business.improvement.model.CorrectiveActionRequestVO;
import kr.co.igns.business.improvement.model.IncidentReportVO;
import kr.co.igns.business.improvement.model.NearMissReportVO;
import kr.co.igns.business.improvement.model.NonconformityCorrectiveVO;
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
public class EvaluationCorrectiveActionRequestService {
    private final EvaluationCorrectiveActionRequestDAO EvaluationCorrectiveActionRequestDao;
    private final ReportService reportService;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final NasFileService nasFileService;
    private final UtilsDAO utilsDAO;

    public List<EvaluationCorrectiveActionRequestVO> getCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo) throws Exception {
        List<EvaluationCorrectiveActionRequestVO> voList = EvaluationCorrectiveActionRequestDao.getCorrectiveActionRequest(vo);
        return voList;
    }

    public EvaluationCorrectiveActionRequestVO getCorrectiveActionRequestDetail(SpSearchVO vo) throws Exception {
        EvaluationCorrectiveActionRequestVO result = EvaluationCorrectiveActionRequestDao.getCorrectiveActionRequestDetail(vo);
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo) throws Exception {
        vo.setCompId(SecurityUtil.getCurrentCompId());
//		LocalDate now = LocalDate.now();
//		int year = now.getYear();
//		vo.setWriteYear(String.valueOf(year));
        EvaluationCorrectiveActionRequestDao.insertCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo) throws Exception {
        EvaluationCorrectiveActionRequestDao.updateCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo) throws Exception {
        EvaluationCorrectiveActionRequestDao.deleteCorrectiveActionRequest(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteCorrectiveActionRequests(List<EvaluationCorrectiveActionRequestVO> list) throws Exception {
        for (EvaluationCorrectiveActionRequestVO tmp : list) {
            EvaluationCorrectiveActionRequestDao.deleteCorrectiveActionRequest(tmp);
        }
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(EvaluationCorrectiveActionRequestDao.getAllReport(spSearchVO));
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
            EvaluationCorrectiveActionRequestVO result = getCorrectiveActionRequestDetail(vo); // 상세정보 조회
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getTitle();
            }
            if (!yearSet.contains(result.getWriteYear())) {  // 중복이 아닐 경우에만 추가
                yearSet.add(result.getWriteYear());
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/improvement/correctiveActionRequest/CorrectiveActionRequest_Data.jrxml");
            Map<String, Object> param = new HashMap<>();
            String extra1 = "";
            if (spSearchVO.isPrintAll()) {
                extra1 = "(성과평가)";
            }
            param.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId())); // 로고
            param.put("page", page);
            param.put("subPage", subPage);
            param.put("localPage", localPage);
            param.put("extra1", extra1);
            param.put("title", result.getTitle());                                            // 제목
            param.put("docNo", result.getWriteYear() + "-" + result.getDocType() + "-" + result.getDocNo()); // 문서번호
            param.put("ctrlOrgnNm", result.getCtrlOrgnNm());                                // 주관조직
            
            // 날짜 형식 변환
            if(result.getReturnDt() != null && !result.getReturnDt().equals("")) {
            	result.setReturnDt(DateUtils.formatDate(result.getReturnDt()));
            }
            param.put("returnDt", result.getReturnDt());                                    // 회신요구일
            param.put("actionOrgnNm", result.getActionOrgnNm());                            // 조치조직
            param.put("actionRequestContent", result.getActionRequestContent());            // 부적합 사항 내용
            param.put("analyticPreventContent", result.getAnalyticPreventContent());        // 원인 분석 및 재발 방지대책 내용
            param.put("resultValidationYn", result.getResultValidationYn());                // 적합, 부적합(Y,N)
            param.put("resultValidationContent", result.getResultValidationContent());        // 시정조치 결과 유효성 확인 내용

            // ============================== 서명start =========================================
            // 부적합 사항(시정조치 요구사항) - 서명 데이터 조회
            UtilsVO signParam2 = new UtilsVO();
            signParam2.setTargetType(result.getDocType());
            signParam2.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam2.setType("rootCauseAnalysis");

            List<UtilsVO> signList2 = utilsService.getApprovalInfo(signParam2); // 서명 데이터 조회
            if (!signList2.isEmpty()) {
                for (UtilsVO signInfo : signList2) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("approver")) {
                        param.put("reviewer2", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm2", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("verification")) {
                        param.put("approver2", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("approverNm2", signInfo.getHrNm());
                    }
                }
            } else {
                param.put("reviewer2", "");
                param.put("reviewerNm2", "");
                param.put("approver2", "");
                param.put("approverNm2", "");
            } 
            
            // 원인 분석 및 재발 방지 대책 - 서명 데이터 조회
            UtilsVO signParam1 = new UtilsVO();
            signParam1.setTargetType(result.getDocType());
            signParam1.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam1.setType("nonconformity");

            List<UtilsVO> signList = utilsService.getApprovalInfo(signParam1); // 서명 데이터 조회
            if (!signList.isEmpty()) {
                for (UtilsVO signInfo : signList) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("reviewer")) {
                        param.put("reviewer1", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm1", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("writer")) {
                        param.put("approverNm1", signInfo.getHrNm());
                        param.put("approver1", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                    }
                }
            } else {
                param.put("reviewer1", "");
                param.put("reviewerNm1", "");
                param.put("approverNm1", "");
                param.put("approver1", "");
            }

            // 시정조치 결과 유효성 확인 - 서명 데이터 조회
            UtilsVO signParam3 = new UtilsVO();
            signParam3.setTargetType(result.getDocType());
            signParam3.setTargetId(result.getWriteYear() + result.getDocNo());
            signParam3.setType("correctiveAction");
            
            List<UtilsVO> signList3 = utilsService.getApprovalInfo(signParam3); // 서명 데이터 조회
            if (!signList3.isEmpty()) {
                for (UtilsVO signInfo : signList3) {
                    // 서명 데이터 입력
                    if (signInfo.getParam().equals("approvalReview")) {
                        param.put("reviewer3", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("reviewerNm3", signInfo.getHrNm());
                    }
                    if (signInfo.getParam().equals("finalApproval")) {
                        param.put("approver3", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        param.put("approverNm3", signInfo.getHrNm());
                    }
                }
            } else {
                param.put("reviewer3", "");
                param.put("reviewerNm3", "");
                param.put("approver3", "");
                param.put("approverNm3", "");
            }

            // ============================== 서명 end =========================================
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
}
