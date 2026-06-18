package kr.co.igns.business.evaluation.service;
import kr.co.igns.business.evaluation.dao.postgres.AuditResultReportDAO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.AuditResultDetaillVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuditResultReportService {
    private final AuditResultReportDAO auditResultReportDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public String formatTime(String hhmm) {
        String hours = hhmm.substring(0, 2);
        String minutes = hhmm.substring(2, 4);
        return hours + ":" + minutes;
    }

    public String formatTimeRange(String startTime, String endTime) {
        if(startTime!=null && endTime!=null){
            return formatTime(startTime) + " ~ " + formatTime(endTime);
        }
        return "";
    }

    public List<AuditExecutionPlanVO> getAuditResult(SpSearchVO searchVo) throws Exception {
        List<AuditExecutionPlanVO> voList = auditResultReportDAO.getAuditResultReport(searchVo);
        for(AuditExecutionPlanVO vo : voList) {
            // 대상 조직 세팅
            List<AuditExecutionPlanDetailVO> auditOrgnList = auditResultReportDAO.getAuditResultReportOrgn(vo);

            for (int i = 0; i < auditOrgnList.size(); i++) {
                AuditExecutionPlanDetailVO orgn = auditOrgnList.get(i);
                orgn.setDocType("ARR");
                AuditExecutionPlanDetailVO detail = auditResultReportDAO.getAuditResultReportSingle(orgn);
                if (detail == null) {
                    orgn.setSubmitYn("N");
                    orgn.setDocType("AXP");
                } else {
                    // 원본 리스트 요소를 새 객체로 교체
                    auditOrgnList.set(i, detail);
                    detail.setSubmitYn("Y");
                    detail.setAuditHrList(auditResultReportDAO.getAuditResultReportHr(detail));
                    detail.setAuditTime(formatTimeRange(detail.getAuditStTime(), detail.getAuditEdTime()));
                    
                    // 심사 대상 수 계산
                    List<AuditResultDetaillVO> detailList = auditResultReportDAO.getAuditResultReportDetail(detail);
                    int detailListUseCount = 0;
                    if (detailList != null) {
                    	detailListUseCount = (int) detailList.stream()
                            .filter(d -> "Y".equalsIgnoreCase(String.valueOf(d.getUseYn())))
                            .count();
                    }
                    detail.setAuditDetailCount(detailListUseCount);
                }
            }

            vo.setAuditOrgnList(auditOrgnList);
        }
        return voList;
    }



    public List<AuditExecutionPlanDetailVO> deleteAuditResult(List<AuditExecutionPlanDetailVO> voList) throws Exception {
        for(AuditExecutionPlanDetailVO vo: voList) {
            // 대상 조직 세팅
            auditResultReportDAO.deleteAuditReport(vo);
        }
        return voList;
    } 

    public AuditExecutionPlanDetailVO getAuditResultDetail(SpSearchVO searchVo) throws Exception {
        searchVo.setDocType("AXP");
        AuditExecutionPlanDetailVO vo = auditResultReportDAO.getAuditExecutionPlan(searchVo);
        vo.setDocType("ARR");
        AuditExecutionPlanDetailVO detail =  auditResultReportDAO.getAuditResultReportSingle(vo);
        if(detail==null){
            vo.setDocType("AXP");
            vo.setSubmitYn("N");
            vo.setCmd("I");
            vo.setAuditHrList(auditResultReportDAO.getAuditExecutionPlanHr(vo));
        }else{
            vo=detail;
            vo.setSubmitYn("Y");
            vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq(), "ARR"));
            vo.setDetail(auditResultReportDAO.getAuditResultReportDetail(vo));
            vo.setAuditHrList(auditResultReportDAO.getAuditResultReportHr(vo));
        }
        vo.setAuditTime(formatTimeRange(vo.getAuditStTime(),vo.getAuditEdTime()));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
	public AuditExecutionPlanDetailVO saveAuditResult(List<MultipartFile> files, AuditExecutionPlanDetailVO vo) throws Throwable {
        if (vo.getAuditTime() != null && !vo.getAuditTime().equals("")) {
            String auditTime = vo.getAuditTime().replace("~", "").replaceAll(" ", "");
            vo.setAuditStTime(auditTime.substring(0, 5).replace(":", ""));
            vo.setAuditEdTime(auditTime.substring(5, 10).replace(":", ""));
        }
        if (vo.getCmd()!=null && vo.getCmd().equals("U")) {
            auditResultReportDAO.updateAuditResult(vo);
            fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq(), SecurityUtil.getCurrentCompId());
        } else {                                                                                                                      
            vo.setDocType("ARR");
            auditResultReportDAO.insertAuditResult(vo);
        }
        List<AuditResultDetaillVO> details = vo.getDetail();
        for(AuditResultDetaillVO detail :details){
            detail.setDocNo(vo.getDocNo());
            detail.setDocSeq(vo.getDocSeq());
            detail.setDocType(vo.getDocType());
            detail.setWriteYear(vo.getWriteYear());
            if(detail.getCmd()!=null && detail.getCmd().equals("U")){
                auditResultReportDAO.updateAuditResultDetail(detail);
            }else{
                auditResultReportDAO.insertAuditResultDetail(detail);
            }
        }
        
		//파일 저장
		fileService.saveFile(files, "ARR", vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq(), SecurityUtil.getCurrentCompId());
		return vo;
	}
    
    // 심사 팀원 저장
    @Transactional(rollbackFor = Throwable.class)
    public void saveAuditHr(AuditExecutionPlanDetailVO vo) throws Throwable {
        List<HrVO> hrList = vo.getAuditHrList();
        vo.setDocType("ARR");
        auditResultReportDAO.deleteAuditHr(vo);
        for(HrVO hr : hrList){
            hr.setDocNo(vo.getDocNo());
            hr.setDocSeq(vo.getDocSeq());
            hr.setDocType(vo.getDocType());
            hr.setWriteYear(vo.getWriteYear());
            auditResultReportDAO.insertAuditHr(hr);
        }
    }

    public List<JasperPrint> getAuditResultReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "안전보건 내부심사 결과 보고서";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        Boolean isSingle = false;
        if(spSearchVO.getCheckedObjList() != null && spSearchVO.getCheckedObjList().size()==1){
            isSingle=true;
        }
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            AuditExecutionPlanDetailVO result = getAuditResultDetail(vo); // 데이터
            if(isSingle==true){
                fileNm +=  "_"+result.getAuditNm()+"_"+result.getOrgnNm();
            }
            System.out.println(fileNm);
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/evaluation/auditResultReport/auditResultReport.jrxml");
            reportVO.setType(spSearchVO.getType());

            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            //결재
            UtilsVO signParam = new UtilsVO();
            signParam.setDocType("ARR");
            String signTargetId = vo.getWriteYear()+ vo.getDocNo()+vo.getDocSeq();
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, signTargetId);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            params.put("orgnNm",result.getOrgnNm());
            params.put("docNo",result.getWriteYear()+result.getDocNo());
            
            // 날짜 형식 변환
            if(result.getAuditDt() != null && !result.getAuditDt().equals("")) {
            	result.setAuditDt(DateUtils.formatDate(result.getAuditDt()));
            }else {
            	result.setAuditDt("");
            }
            params.put("auditDt",result.getAuditDt()+" "+result.getAuditTime());
            String auditHrList = "";
            for(HrVO hr : result.getAuditHrList()){
                auditHrList = auditHrList + hr.getHrNm() + ", ";
            }
            auditHrList = auditHrList.substring(0,auditHrList.length()-2);
            params.put("auditHrList",auditHrList);
            params.put("remark",result.getRemark());
                List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
                for (AuditResultDetaillVO detail : result.getDetail()) {
                    if(detail.getUseYn().equals("Y")) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("targetJob",detail.getTargetJob());
                        data.put("result",detail.getResult());
                        data.put("remark",detail.getRemark());
                        datasource.add(data);
                    }
                }
                if(datasource.size()<1) {
                   Map<String, Object> data = new HashMap<String, Object>();
                   datasource.add(data); 
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
