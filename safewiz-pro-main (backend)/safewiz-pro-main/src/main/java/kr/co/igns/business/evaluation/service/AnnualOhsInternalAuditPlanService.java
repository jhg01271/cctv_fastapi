package kr.co.igns.business.evaluation.service;

import kr.co.igns.business.evaluation.dao.postgres.AnnualOhsInternalAuditPlanDAO;
import kr.co.igns.business.evaluation.dao.postgres.AuditExecutionPlanDAO;
import kr.co.igns.business.evaluation.model.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnualOhsInternalAuditPlanService {
    private final AnnualOhsInternalAuditPlanDAO annualOhsInternalAuditPlanDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<AnnualOhsInternalAuditPlanVO> getAnnualOhsInternalAuditPlan(SpSearchVO searchVo) throws Exception {
        List<AnnualOhsInternalAuditPlanVO> voList = annualOhsInternalAuditPlanDAO.getAnnualOhsInternalAuditPlan(searchVo);
        for (AnnualOhsInternalAuditPlanVO vo : voList) {
            vo.setRemarkList(annualOhsInternalAuditPlanDAO.getAnnualOhsInternalAuditDetailPlan(vo));
            // 대상 조직 세팅
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveAnnualOhsInternalAuditPlan(List<AnnualOhsInternalAuditPlanVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (AnnualOhsInternalAuditPlanVO vo : voList) {
            if (vo.getCmd().equals("U")) {
                vo.setCreatedBy(userId);
                annualOhsInternalAuditPlanDAO.updateAnnualOhsInternalAuditPlan(vo);
            } else {
                vo.setUpdatedBy(userId);
                annualOhsInternalAuditPlanDAO.insertAnnualOhsInternalAuditPlan(vo);
            }
            if (!vo.getRemarkList().isEmpty()) {
                annualOhsInternalAuditPlanDAO.deleteAnnualOhsInternalAuditPlanDetail(voList.get(0));
                for (AnnualOhsInternalAuditPlanDetailVO remark : vo.getRemarkList()) {
                    remark.setWriteYear(vo.getWriteYear());
                    remark.setDocType(vo.getDocType());
                    remark.setDocNo(vo.getDocNo());
                    remark.setCreatedBy(userId);
                    annualOhsInternalAuditPlanDAO.insertAnnualOhsInternalAuditPlanDetail(remark);
                }
            }
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteAnnualOhsInternalAuditPlan(List<AnnualOhsInternalAuditPlanVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (AnnualOhsInternalAuditPlanVO vo : voList) {
            vo.setUpdatedBy(userId);
            annualOhsInternalAuditPlanDAO.deleteAnnualOhsInternalAuditPlan(vo);
        }
        return voList.get(0);
    }

    public List<JasperPrint> getAnnualOhsInternalAuditPlanReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "연간 안전보건 내부심사 실시계획서";
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
            reportVO.setJrxmlPath("report/evaluation/annualOhsInternalAuditPlan/annualOhsInternalAuditPlan.jrxml");
            reportVO.setType(spSearchVO.getType());

            List<AnnualOhsInternalAuditPlanVO> resultList = getAnnualOhsInternalAuditPlan(vo); // 데이터
            AnnualOhsInternalAuditPlanVO result = resultList.stream()
                    .filter(item -> item.getDocNo().equals(vo.getDocNo()))
                    .findFirst()
                    .orElse(null); // 조건에 맞는 값이 없을 경우 null 반환
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getOrgnNm();
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            String signTargetId = vo.getWriteYear()+vo.getCompId();
            //결재
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo, signTargetId);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            List<Map<String, Object>> datasource = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                Map<String, Object> data = new HashMap<String, Object>();
                String key = "auditSchedule" + i;
                Field field = result.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object value = field.get(result);
                data.put("schedule", value);
                data.put("month", i);
                data.put("orgnNm", result.getOrgnNm());
                data.put("remark", "");
                datasource.add(data);
            }
            for (AnnualOhsInternalAuditPlanDetailVO remark : result.getRemarkList()) {
                datasource.get(remark.getMonth() - 1).put("remark", remark.getRemark());
            }

            params.put("dataList", new JRBeanCollectionDataSource(datasource));
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
