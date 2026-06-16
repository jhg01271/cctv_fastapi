package kr.co.igns.business.evaluation.service;

import kr.co.igns.business.evaluation.dao.postgres.AuditExecutionPlanDAO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.business.evaluation.dao.postgres.InternalAuditDAO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditExecutionPlanService {
    private final AuditExecutionPlanDAO auditExecutionPlanDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<AuditExecutionPlanVO> getAuditExecutionPlan(SpSearchVO searchVo) throws Exception {
        List<AuditExecutionPlanVO> voList = auditExecutionPlanDAO.getAuditExecutionPlan(searchVo);
        for (AuditExecutionPlanVO vo : voList) {
            // 대상 조직 세팅
            List<AuditExecutionPlanDetailVO> auditOrgnList = auditExecutionPlanDAO.getAuditExecutionPlanOrgn(vo);
            if (!auditOrgnList.isEmpty() && searchVo.getDocNo() != null && !searchVo.getDocNo().isEmpty()) {
                for (AuditExecutionPlanDetailVO orgn : auditOrgnList) {
                    // 심사 팀원 세팅
                    orgn.setAuditHrList(auditExecutionPlanDAO.getAuditExecutionPlanOrgnHr(orgn));
                }
            }
            vo.setAuditOrgnList(auditOrgnList);
        }
        return voList;
    }

    public List<HrVO> getJudgeList(SpSearchVO searchVo) throws Exception {
        // 내부 심사원 자격인증 페이지에서 통과인 항목만
        return auditExecutionPlanDAO.getJudgeList(searchVo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveAuditExecutionPlan(AuditExecutionPlanVO vo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(userId);
            auditExecutionPlanDAO.insertAuditExecutionPlan(vo);
        } else {
            vo.setUpdatedBy(userId);
            auditExecutionPlanDAO.updateAuditExecutionPlan(vo);
        }
        if (!vo.getAuditOrgnList().isEmpty()) {
            for (AuditExecutionPlanDetailVO orgn : vo.getAuditOrgnList()) {
                if (orgn.getAuditTime() != null && !orgn.getAuditTime().equals("")) {
                    String auditTime = orgn.getAuditTime().replace("~", "").replaceAll(" ", "");
                    orgn.setAuditStTime(auditTime.substring(0, 5));
                    orgn.setAuditEdTime(auditTime.substring(5, 10));
                }
                if (orgn.getCmd().equals("U")) {
                    orgn.setUpdatedBy(userId);
                    auditExecutionPlanDAO.updateAuditExecutionPlanOrgn(orgn);
                } else {
                    orgn.setWriteYear(vo.getWriteYear());
                    orgn.setDocType(vo.getDocType());
                    orgn.setDocNo(vo.getDocNo());
                    orgn.setCreatedBy(userId);
                    auditExecutionPlanDAO.insertAuditExecutionPlanOrgn(orgn);
                }
                if (!orgn.getAuditHrList().isEmpty()) {
                    auditExecutionPlanDAO.deleteAuditExecutionPlanOrgnHr(orgn);

                    for (HrVO hr : orgn.getAuditHrList()) {
                        hr.setWriteYear(orgn.getWriteYear());
                        hr.setDocType(orgn.getDocType());
                        hr.setDocNo(orgn.getDocNo());
                        hr.setDocSeq(orgn.getDocSeq());
                        hr.setCreatedBy(userId);
                        auditExecutionPlanDAO.insertAuditExecutionPlanOrgnHr(hr);
                    }
                }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteAuditExecutionPlan(List<AuditExecutionPlanVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (AuditExecutionPlanVO vo : voList) {
            vo.setUpdatedBy(userId);
            auditExecutionPlanDAO.deleteAuditExecutionPlan(vo);
        }
        return voList.get(0);
    }

    public List<Map<String, Object>> getCheckedReport(AuditExecutionPlanVO result, SpSearchVO spSearchVO) throws ParseException {
        List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
        for (AuditExecutionPlanDetailVO orgn : result.getAuditOrgnList()) {

            boolean isContained = spSearchVO.getCheckedList().contains(orgn.getDocSeq());
            if (isContained == true) {
                // 사용중인 항목만 레포트에 출력
                Map<String, Object> data = new HashMap<String, Object>();
                String auditDt = orgn.getAuditDt() != null ? DateUtils.formatDate(orgn.getAuditDt()) : "";
                data.put("auditDt", auditDt);
                if (orgn.getAuditStTime() != null && !orgn.getAuditStTime().equals("")) {
                    String stTime = orgn.getAuditStTime().substring(0, 2) + ":" + orgn.getAuditStTime().substring(2, 4);
                    String edTime = orgn.getAuditEdTime().substring(0, 2) + ":" + orgn.getAuditEdTime().substring(2, 4);
                    data.put("auditTime", stTime + "~" + edTime);
                }
                data.put("orgnNm", orgn.getOrgnNm());
                if (!orgn.getAuditHrList().isEmpty()) {
                    String auditHrList = orgn.getAuditHrList().stream()
                            .map(HrVO::getHrNm)
                            .collect(Collectors.joining("\n"));
                    data.put("auditHrList", auditHrList);
                }
                data.put("auditContent", orgn.getAuditContent());
                data.put("remark", orgn.getRemark());
                datasource.add(data);
            }
        }
        return datasource;
    }

    public List<JasperPrint> getAuditExecutionPlanReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "안전보건 내부심사 수행 계획서";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/evaluation/ohsInternalAuditExecutionPlan/ohsInternalAuditExecutionPlan.jrxml");
            reportVO.setType(spSearchVO.getType());

            List<AuditExecutionPlanVO> resultList = getAuditExecutionPlan(vo); // 데이터
            AuditExecutionPlanVO result = resultList.get(0);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getAuditNm();
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            //결재
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));


            params.put("auditNm", result.getAuditNm());
            
            // 날짜 형태 변환
            if(result.getWriteDt() != null && !result.getWriteDt().equals("")) {
            	result.setWriteDt(DateUtils.formatDate(result.getWriteDt()));
            }else{
            	result.setWriteDt("");
            }
            params.put("writeDt", result.getWriteDt());
            
            params.put("auditDiv", result.getAuditDiv());
            params.put("auditPurposeScope", result.getAuditPurposeScope());
            params.put("auditRequest", result.getAuditRequest());
            params.put("distribute", result.getDistribute());

            if (!result.getAuditOrgnList().isEmpty()) {
                List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
                if (spSearchVO.getCheckedList().size() > 0) {
                    // 선택한 항목들만 출력을 원할 때
                    datasource.addAll(getCheckedReport(result, spSearchVO));
                } else {

                    for (AuditExecutionPlanDetailVO orgn : result.getAuditOrgnList()) {
                        if (orgn.getUseYn().equals(YesNo.Y)) {
                            // 사용중인 항목만 레포트에 출력
                            Map<String, Object> data = new HashMap<String, Object>();
                            
                            // 날짜 형태 변환
                            if(orgn.getAuditDt() != null && !orgn.getAuditDt().equals("")) {
                            	orgn.setAuditDt(DateUtils.formatDate(orgn.getAuditDt()));
                            }else{
                            	orgn.setAuditDt("");
                            }
                            data.put("auditDt", orgn.getAuditDt());
                            
                            if (orgn.getAuditStTime() != null && !orgn.getAuditStTime().equals("")) {
                                String stTime = orgn.getAuditStTime().substring(0, 2) + ":" + orgn.getAuditStTime().substring(2, 4);
                                String edTime = orgn.getAuditEdTime().substring(0, 2) + ":" + orgn.getAuditEdTime().substring(2, 4);
                                data.put("auditTime", stTime + "~" + edTime);
                            }
                            data.put("orgnNm", orgn.getOrgnNm());
                            if (!orgn.getAuditHrList().isEmpty()) {
                                String auditHrList = orgn.getAuditHrList().stream()
                                        .map(HrVO::getHrNm)
                                        .collect(Collectors.joining("\n"));
                                data.put("auditHrList", auditHrList);
                            }
                            data.put("auditContent", orgn.getAuditContent());
                            data.put("remark", orgn.getRemark());
                            datasource.add(data);
                        }
                    }
                }
                params.put("dataList", new JRBeanCollectionDataSource(datasource));
            }
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
            spSearchVO.setPage(page);
            spSearchVO.setLocalPage(localPage);
            // 첨부파일
            if (result.getFiles() != null && !result.getFiles().isEmpty()) {
                JasperPrint JasperFileReport = utilsService.getFilesReport(spSearchVO, result.getFiles());
                reportList.add(JasperFileReport);
                page = page + JasperFileReport.getPages().size();
                localPage = localPage + JasperFileReport.getPages().size();
            }
        }
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }
}
