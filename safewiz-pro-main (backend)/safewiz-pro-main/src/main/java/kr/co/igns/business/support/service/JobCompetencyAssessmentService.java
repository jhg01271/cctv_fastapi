package kr.co.igns.business.support.service;

import kr.co.igns.business.participation.model.ActPlanDetailVO;
import kr.co.igns.business.support.dao.postgres.JobCompetencyAssessmentDAO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.JobMgmtVO;
import kr.co.igns.business.support.model.JobLevelVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobCompetencyAssessmentService {
    private final JobCompetencyAssessmentDAO jobCompetencyAssessmentDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<JobCompetencyAssessmentDetailVO> getCardJobManageList(SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentDetailVO> voList = jobCompetencyAssessmentDAO.getCardJobManageList(searchVo);
        return voList;
    }

    public List<JobMgmtVO> getJobManageList(SpSearchVO searchVo) throws Exception {
        List<JobMgmtVO> voList = jobCompetencyAssessmentDAO.getJobManageList(searchVo);
        return voList;
    }

    public List<JobMgmtVO> getDataSetJobManageList(SpSearchVO searchVo) throws Exception {
        List<JobMgmtVO> onlyInDataSet = new ArrayList<>();
//        2025.06.10 미사용 처리, 추후 다시 사용될 수도 있음. 추후 사용하게 될 경우 dataset.tb_dataset_job_competency_assessment_jobmgmt 사용
//        List<JobMgmtVO> onlyInDataSet = jobCompetencyAssessmentDAO.getEsgSetJobManageList(searchVo).stream()
//                .filter(vo -> jobCompetencyAssessmentDAO.getJobManageList(searchVo).stream()
//                        .map(JobMgmtVO::getOrgnRoleId)
//                        .noneMatch(id -> Objects.equals(id, vo.getOrgnRoleId())))
//                .collect(Collectors.toList());
//
//        if(onlyInDataSet == null || onlyInDataSet.isEmpty()){
//            onlyInDataSet = jobCompetencyAssessmentDAO.getDataSetJobManageList(searchVo).stream()
//                    .filter(vo -> jobCompetencyAssessmentDAO.getJobManageList(searchVo).stream()
//                            .map(JobMgmtVO::getOrgnRoleId)
//                            .noneMatch(id -> Objects.equals(id, vo.getOrgnRoleId())))
//                    .collect(Collectors.toList());
//        }
        return onlyInDataSet;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<JobMgmtVO> saveJobManageList(List<JobMgmtVO> voList) throws Exception {
        for(JobMgmtVO vo : voList){
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getJobId().equals("")){
                jobCompetencyAssessmentDAO.insertJobManageList(vo);
            }else{
                jobCompetencyAssessmentDAO.updateJobManageList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<JobMgmtVO> deleteJobManageList(List<JobMgmtVO> voList) throws Exception {
        for(JobMgmtVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            jobCompetencyAssessmentDAO.deleteJobManageList(vo);
        }
        return voList;
    }

    public List<JobLevelVO> getJobLevelManageList(SpSearchVO searchVo) throws Exception {
        List<JobLevelVO> voList = jobCompetencyAssessmentDAO.getJobLevelManageList(searchVo);
        return voList;
    }

    public List<JobLevelVO> getDataSetJobLevelManageList(SpSearchVO searchVo) throws Exception {
        List<JobLevelVO> voList = jobCompetencyAssessmentDAO.getDataSetJobLevelManageList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<JobLevelVO> saveJobLevelManageList(List<JobLevelVO> voList) throws Exception {
        for(JobLevelVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                jobCompetencyAssessmentDAO.insertJobLevelManageList(vo);
            }else{
                jobCompetencyAssessmentDAO.updateJobLevelManageList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<JobLevelVO> deleteJobLevelManageList(List<JobLevelVO> voList) throws Exception {
        for(JobLevelVO vo : voList){
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            jobCompetencyAssessmentDAO.deleteJobLevelManageList(vo);
        }
        return voList;
    }



    public List<JobCompetencyAssessmentVO> getJobCompAssessList(SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentVO> voList = jobCompetencyAssessmentDAO.getJobCompAssessList(searchVo);
        if (searchVo.getDocNo() != null && !searchVo.getDocNo().equals("")) {
            // 상세보기인 경우
        	if(voList.size() > 0) {
        		voList.get(0).setDetailList(jobCompetencyAssessmentDAO.getJobCompAssessDetail(searchVo));        		
        	}
        } else if (voList.size() > 0) {
            // 카드 정보 표현용
            for (JobCompetencyAssessmentVO vo : voList) {
                searchVo.setDocType(vo.getDocType());
                searchVo.setDocNo(vo.getDocNo());
                List<JobCompetencyAssessmentDetailVO> detail = jobCompetencyAssessmentDAO.getJobCompAssessDetail(searchVo);
                List<String> nmList = detail.stream()
                        .map(JobCompetencyAssessmentDetailVO::getJobNm)
                        .collect(Collectors.toList());
                vo.setCardJobNm(nmList);
            }
        }

        return voList;
    }

    public List<JobCompetencyAssessmentVO> getPreJobCompAssessList(SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentVO> voList = jobCompetencyAssessmentDAO.getPreJobCompAssessList(searchVo);
        for (JobCompetencyAssessmentVO vo : voList) {
            searchVo.setWriteYear(vo.getWriteYear());
            searchVo.setDocType(vo.getDocType());
            searchVo.setDocNo(vo.getDocNo());
            vo.setDetailList(jobCompetencyAssessmentDAO.getJobCompAssessDetail(searchVo));
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveJobCompAssess(JobCompetencyAssessmentVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.insertJobCompAssess(vo);
        } else {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.updateJobCompAssess(vo);
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveJobCompAssessDetail(JobCompetencyAssessmentVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.insertJobCompAssess(vo);
        } else {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.updateJobCompAssess(vo);
        }
        for (JobCompetencyAssessmentDetailVO map : vo.getDetailList()) {
            map.setCompId(vo.getCompId());
            map.setDocNo(vo.getDocNo());
            // 하위 카드 데이터
            if (map.getDocSeq() == null || map.getDocSeq().equals("")) {
                map.setCreatedBy(SecurityUtil.getCurrentMemberId());
                jobCompetencyAssessmentDAO.insertJobCompAssessDetail(map);
            } else {
                vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                jobCompetencyAssessmentDAO.updateJobCompAssessDetail(map);
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO savePreJobCompAssess(List<JobCompetencyAssessmentVO> voList) throws Exception {

        for (JobCompetencyAssessmentVO vo : voList) {
            vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            String writeYear = String.valueOf(Integer.valueOf(vo.getWriteYear()) + 1);
            vo.setWriteYear(writeYear);
            jobCompetencyAssessmentDAO.insertJobCompAssess(vo);
            for (JobCompetencyAssessmentDetailVO map : vo.getDetailList()) {
                map.setCompId(vo.getCompId());
                map.setDocNo(vo.getDocNo());
                map.setWriteYear(vo.getWriteYear());
                // 하위 카드 데이터
                map.setCreatedBy(SecurityUtil.getCurrentMemberId());
                jobCompetencyAssessmentDAO.insertJobCompAssessDetail(map);
            }
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteJobCompAssess(List<JobCompetencyAssessmentVO> voList) throws Exception {
        for (JobCompetencyAssessmentVO vo : voList) {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.deleteJobCompAssess(vo);

        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteJobCompAssessDetail(List<JobCompetencyAssessmentDetailVO> voList) throws Exception {
        for (JobCompetencyAssessmentDetailVO vo : voList) {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            jobCompetencyAssessmentDAO.deleteJobCompAssessDetail(vo);

        }
        return voList.get(0);
    }


//    public List<JobCompetencyAssessmentVO> getJobFieldList(SpSearchVO searchVo) throws Exception {
//        List<JobCompetencyAssessmentVO> voList = jobCompetencyAssessmentDAO.getJobFieldList(searchVo);
//        return voList;
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public BaseVO saveJobField(List<JobCompetencyAssessmentVO> voList) throws Exception {
//        for (JobCompetencyAssessmentVO vo : voList) {
//            if (vo.getJobSeq() == null) {
//                vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
//                jobCompetencyAssessmentDAO.insertJobField(vo);
//            } else {
//                vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
//                jobCompetencyAssessmentDAO.updateJobField(vo);
//            }
//        }
//        return voList.get(0);
//    }

    // 상세 페이지 에서 출력
    public List<JasperPrint> getJobCompAssessReportDetail(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO, List<JobCompetencyAssessmentVO> resultList) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String title = "직무 적격성 평가서";
        ReportVO reportVO = new ReportVO();
        reportVO.setJrxmlPath("report/support/jobCompetencyAssessment.jrxml");
        reportVO.setType("pdf");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("title", title);
        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo2);

        UtilsVO utilsVo = new UtilsVO();
        utilsVo.setTargetType("JCA");
        utilsVo.setTargetId(spSearchVO.getWriteYear() + spSearchVO.getDocNo());
        List<UtilsVO> signatureList = utilsService.getApprovalInfo(utilsVo);
        if (!signatureList.isEmpty()) {
            params.put("signWriterNm", signatureList.get(0).getHrNm());
        }
        JobCompetencyAssessmentVO result = resultList.get(0);

        params.put("writeDt", result.getCreatedAt());
        params.put("hrNm", result.getHrNm());
        params.put("orgnNm", result.getOrgnNm());
        params.put("jbrpNm", result.getJbrpNm());

        params.put("page", 0);
        params.put("subPage", 0);
        params.put("localPage", 0);

        List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
        // 상세테이블 데이터
        if (!result.getDetailList().isEmpty()) {
            for (JobCompetencyAssessmentDetailVO res : result.getDetailList()) {
                boolean isContained = spSearchVO.getCheckedList().contains(res.getDocSeq());
                if (isContained == true) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("jobFieldNm", res.getJobCategory());
                    data.put("jobNm", res.getJobNm());
                    data.put("demandLevelNm", res.getDemandLevelNm());
                    data.put("currentLevelNm", res.getCurrentLevelNm());
                    data.put("trainingContent", res.getTrainingContent());
                    data.put("trainingCourseNm", res.getTrainingCourseNm());
                    data.put("trainingInstituteNm", res.getTrainingInstituteNm());
                    datasource.add(data);
                }
            }
        }
        params.put("assessmentList", new JRBeanCollectionDataSource(datasource));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        return reportList;
    }

    // 일괄출력 파라미터 조회
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<String> checkedList = jobCompetencyAssessmentDAO.getAllReport(spSearchVO);
        if (!checkedList.isEmpty()) {
            List<JasperPrint> jaspers = new ArrayList<>();
            spSearchVO.setCheckedList(checkedList);
            spSearchVO.setPrintAll(true);
            spSearchVO.setPage(page);
            spSearchVO.setSubPage(subPage);
            spSearchVO.setLocalPage(localPage);
            jaspers = getJobCompAssessReport(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    // 목록에서 출력
    public List<JasperPrint> getJobCompAssessReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += "직무 적격성 평가서";
        String title = "직무 적격성 평가서";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        if (spSearchVO.getDocNo() != null) {
            // 상세화면에서 선택된 항목만 출력을 원할 때
            spSearchVO.setDocNo(spSearchVO.getDocNo());
            List<JobCompetencyAssessmentVO> resultList = getJobCompAssessList(spSearchVO); // 데이터
            reportList.addAll(getJobCompAssessReportDetail(request, response, spSearchVO, resultList));
            fileNm += "_" + resultList.get(0).getHrNm();
        } else {
            for (String docNo : spSearchVO.getCheckedList()) {
                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/support/jobCompetencyAssessment.jrxml");
                reportVO.setType("pdf");
                spSearchVO.setDocNo(docNo);
                List<JobCompetencyAssessmentVO> resultList = getJobCompAssessList(spSearchVO); // 데이터
                JobCompetencyAssessmentVO result = new JobCompetencyAssessmentVO();
                if(resultList.size() > 0) {
                	result = resultList.get(0);                	
                
                	if (spSearchVO.getCheckedList().size() == 1) {
                		fileNm += "_" + result.getHrNm();
                	}
                	Map<String, Object> params = new HashMap<String, Object>();
                	params.put("title", title);
                	InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                	params.put("logo", logo2);
                	
                	UtilsVO utilsVo = new UtilsVO();
                	utilsVo.setTargetType("JCA");
                	utilsVo.setTargetId(spSearchVO.getWriteYear() + docNo);
                	utilsVo.setType("sign");
                	List<UtilsVO> signatureList = utilsService.getApprovalInfo(utilsVo);
                	params.put("writeDt", result.getCreatedAt());
                	
                	if (!signatureList.isEmpty()) {
                		for (UtilsVO sign : signatureList) {
                			if(sign.getParam().equals("reviewer")) {
                				params.put("signWriterNm", sign.getHrNm());
                			}
                		}
                	}
                	
                	params.put("hrNm", result.getHrNm());
                	params.put("orgnNm", result.getOrgnNm());
                	params.put("jbrpNm", result.getJbrpNm());
                	
                	params.put("page", page);
                	params.put("subPage", subPage);
                	params.put("localPage", localPage);
                	
                	// 상세테이블 데이터
                	List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
                	if (!result.getDetailList().isEmpty()) {
                		for (JobCompetencyAssessmentDetailVO res : result.getDetailList()) {
                			if (res.getUseYn() == YesNo.Y) {
                				Map<String, Object> data = new HashMap<String, Object>();
                				data.put("jobFieldNm", res.getJobCategory());
                				data.put("jobNm", res.getJobNm());
                				data.put("demandLevelNm", res.getDemandLevelNm());
                				data.put("currentLevelNm", res.getCurrentLevelNm());
                				data.put("trainingContent", res.getTrainingContent());
                				data.put("trainingCourseNm", res.getTrainingCourseNm());
                				data.put("trainingInstituteNm", res.getTrainingInstituteNm());
                				datasource.add(data);
                			}
                		}
                	}
                	params.put("assessmentList", new JRBeanCollectionDataSource(datasource));
                	reportVO.setParameter(params);
                	JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                	reportList.add(JasperReport);
                	page = page + JasperReport.getPages().size();
                	localPage = localPage + JasperReport.getPages().size();
                }

            }
        }


        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }
}
