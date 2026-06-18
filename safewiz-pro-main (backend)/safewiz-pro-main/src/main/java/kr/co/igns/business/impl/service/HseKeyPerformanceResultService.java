package kr.co.igns.business.impl.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;

import kr.co.igns.business.impl.dao.postgres.HseKeyPerformanceResultDAO;
import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service
@RequiredArgsConstructor
public class HseKeyPerformanceResultService {

    private final UtilsService utilsService;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final HseKeyPerformanceResultDAO dao;
    private final ReportService reportService;

    public List<RiskAssessmentVO> getHsePerformance(SpSearchVO vo)
            throws Exception {
        List<RiskAssessmentVO> voList = dao.getHsePerformance(vo);
        return voList;
    }

    public List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectives(vo);
        return voList;
    }

    // 상세 화면 조회
    public List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectivesDetail(vo);
        for (SafetyAndHealthObjectivesVO resultVo : voList) {
            List<HseKeyPerformanceResultVO> res = dao.getDetail(resultVo);
            resultVo.setResult(res);
        }

        return voList;
    }

    public List<HseKeyPerformanceResultVO> save(List<HseKeyPerformanceResultVO> voList) {
        for (HseKeyPerformanceResultVO resultVo : voList) {
        	if("100".equals(resultVo.getPercent())) {
        		String reqInfoKey = "KPR" + "^" + resultVo.getWriteYear() + "^" + resultVo.getDocNo()+ "^" + resultVo.getDocSeq()+ "^" + resultVo.getDocSeqDetail();
        		resultVo.setReqInfoKey(reqInfoKey);
        		resultVo.setTaskDetailDiv("notFinished");
        		List<TaskVO> taskList = dao.getTaskList(resultVo);
                if(taskList.size() > 0) {
                	for(TaskVO tvo : taskList) {
                		dao.updateTask(tvo);                		
                	}
                }
        	}
        	
        	
            if (resultVo.getCmd() != null && "U".equals(resultVo.getCmd())) {
                dao.update(resultVo);
            } else {
                dao.insert(resultVo);
                String reqInfoKey = "KPR" + "^" + resultVo.getWriteYear() + "^" + resultVo.getDocNo()+ "^" + resultVo.getDocSeq();
                resultVo.setReqInfoKey(reqInfoKey);
                resultVo.setTaskDetailDiv("notRegistered");
                List<TaskVO> taskList = dao.getTaskList(resultVo);
                if(taskList.size() > 0) {
                	for(TaskVO tvo : taskList) {
                		dao.updateTask(tvo);                		
                	}
                }
            }

        }
        return voList;
    }

    public List<HseKeyPerformanceResultVO> delete(List<HseKeyPerformanceResultVO> voList) {
        for (HseKeyPerformanceResultVO resultVo : voList) {
            dao.delete(resultVo);
        }
        return voList;
    }

    // 일괄 출력
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        List<SpSearchVO> checkedObjList = dao.getAllReport(spSearchVO);
        if (!checkedObjList.isEmpty()) {
            spSearchVO.setCheckedObjList(checkedObjList);
            spSearchVO.setPrintAll(true);
            reportList = getReportList(request, response, spSearchVO);
        }
        return reportList;
    }

    //카드 출력
    public List<JasperPrint> getReportList(HttpServletRequest request, HttpServletResponse response, SpSearchVO searchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String title = "HSE 중점 추진실적";
        String fileNm = title;

        int page = searchVO.getPage();
        int subPage = searchVO.getSubPage();
        int localPage = searchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", searchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        List<String> writeYearList = new ArrayList<>();

        for (SpSearchVO vo : searchVO.getCheckedObjList()) {
            List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectivesReport(vo);
            List<SafetyAndHealthObjectivesVO> filteredList = voList.stream()
                    .filter(item -> "Y".equals(item.getUseYn()) && "Y".equals(item.getUseYnMain()))
                    .collect(Collectors.toList());
            if (!writeYearList.contains(voList.get(0).getWriteYear())) {
                writeYearList.add(voList.get(0).getWriteYear());
            }
            if (searchVO.getCheckedObjList().size() == 1) {
                fileNm = fileNm + "_" + voList.get(0).getOrgnNm();
            }
            voList.get(0).setPage(page);
            voList.get(0).setSubPage(subPage);
            voList.get(0).setLocalPage(localPage);
            ReportVO res = getReportDetail(request, response, !filteredList.isEmpty() ? filteredList : voList);
            JasperPrint JasperReport = reportService.allReportJasperPrint(res);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }

        String writeYear = String.join(",",  writeYearList);
        fileNm = "(" + writeYear + ")" + fileNm;

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (searchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, searchVO.getType(), fileNm);

        return reportList;
    }

    //상세 출력
    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO searchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String title = "HSE 중점 추진실적";
        String fileNm = title;

        int page = searchVO.getPage();
        int subPage = searchVO.getSubPage();
        int localPage = searchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", searchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        String orgnNm = null;
        for (SpSearchVO vo : searchVO.getCheckedObjList()) {
            List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectivesReport(vo);
            orgnNm = voList.get(0).getOrgnNm();
            List<SafetyAndHealthObjectivesVO> filteredList = voList.stream()
                    .filter(item -> "Y".equals(item.getUseYn()) && "Y".equals(item.getUseYnMain()))
                    .collect(Collectors.toList());

            ReportVO res = getReportDetail(request, response, !filteredList.isEmpty() ? filteredList : voList);
            JasperPrint JasperReport = reportService.allReportJasperPrint(res);
            reportList.add(JasperReport);
        }
//        UtilsVO signParam = new UtilsVO();
//        signParam.setDocType("CTR");
//        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, spSearchVO.getWriteYear()+spSearchVO.getCompId());
//        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));
        fileNm = orgnNm == null ? "(" + searchVO.getWriteYear() + ")" + fileNm : "(" + searchVO.getWriteYear() + ")" + fileNm + "_" + orgnNm;

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (searchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, searchVO.getType(), fileNm);

        return reportList;
    }

    public ReportVO getReportDetail(HttpServletRequest request, HttpServletResponse response, List<SafetyAndHealthObjectivesVO> searchVO) throws Exception {

        ReportVO reportVO = new ReportVO();
        // 출력 생성용 Jasper 파일 위치
        reportVO.setJrxmlPath("report/impl/HseKeyPerformanceResult/HseKeyPerformanceResult.jrxml");

        Map<String, Object> params = new HashMap<String, Object>();
        int page = searchVO.get(0).getPage();
        int subPage = searchVO.get(0).getSubPage();
        int localPage = searchVO.get(0).getLocalPage();
        // 1. 서명 데이터 조회

        UtilsVO signParam = new UtilsVO();
        signParam.setDocType("KPR");
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, searchVO.get(0).getWriteYear() + searchVO.get(0).getDocNo());
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));
        // 2. 메인 데이터 조회
        SafetyAndHealthObjectivesVO mainData = dao.getReportData(searchVO.get(0));

        params.put("orgnNm", mainData.getOrgnNm());
        params.put("revisedDt", mainData.getRevisedDt() != null && !mainData.getRevisedDt().isEmpty() ? DateUtils.formatDate(mainData.getRevisedDt()) : "");
        params.put("enactedDt", DateUtils.formatDate(mainData.getEnactedDt()));

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo);
        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);


        // 3. 상세 데이터 조회
        List<Map<String, Object>> dataset = new ArrayList<>();
        for (SafetyAndHealthObjectivesVO vo : searchVO) {
            mainData = dao.getReportData(vo);
            List<HseKeyPerformanceResultVO> detailData = dao.getDetail(vo);

            if ("Y".equals(mainData.getUseYn()) && detailData.isEmpty()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("safetyHealthGoal", mainData.getSafetyHealthGoal());
                data.put("detailGoalMethod", mainData.getDetailGoalMethod());
                data.put("requiredResource", mainData.getRequiredResource());
                data.put("evaluationMethod", mainData.getEvaluationMethod());
                data.put("remark", mainData.getRemark());
                data.put("no", "1");
                dataset.add(data);
            }
            for (HseKeyPerformanceResultVO detailDatum : detailData) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("percent", detailDatum.getPercent());
                data.put("content", detailDatum.getContent());
                data.put("actionSchedule1", detailDatum.getActionResult1().equals("Y") ? "●" : "");
                data.put("actionSchedule2", detailDatum.getActionResult2().equals("Y") ? "●" : "");
                data.put("actionSchedule3", detailDatum.getActionResult3().equals("Y") ? "●" : "");
                data.put("actionSchedule4", detailDatum.getActionResult4().equals("Y") ? "●" : "");
                data.put("actionSchedule5", detailDatum.getActionResult5().equals("Y") ? "●" : "");
                data.put("actionSchedule6", detailDatum.getActionResult6().equals("Y") ? "●" : "");
                data.put("actionSchedule7", detailDatum.getActionResult7().equals("Y") ? "●" : "");
                data.put("actionSchedule8", detailDatum.getActionResult8().equals("Y") ? "●" : "");
                data.put("actionSchedule9", detailDatum.getActionResult9().equals("Y") ? "●" : "");
                data.put("actionSchedule10", detailDatum.getActionResult10().equals("Y") ? "●" : "");
                data.put("actionSchedule11", detailDatum.getActionResult11().equals("Y") ? "●" : "");
                data.put("actionSchedule12", detailDatum.getActionResult12().equals("Y") ? "●" : "");
                data.put("hrNm", detailDatum.getHrNm());
                data.put("safetyHealthGoal", mainData.getSafetyHealthGoal());
                data.put("detailGoalMethod", mainData.getDetailGoalMethod());
                data.put("requiredResource", mainData.getRequiredResource());
                data.put("evaluationMethod", mainData.getEvaluationMethod());
                data.put("remark", mainData.getRemark());
                data.put("no", String.valueOf(dataset.size() + 1));
                dataset.add(data);
            }
        }
        if (dataset.isEmpty()) {
            Map<String, Object> data = new HashMap<String, Object>();
            dataset.add(data);
        }

        // 입력
        params.put("Dataset", new JRBeanCollectionDataSource(dataset));
        reportVO.setParameter(params);

        return reportVO;
    }
}
