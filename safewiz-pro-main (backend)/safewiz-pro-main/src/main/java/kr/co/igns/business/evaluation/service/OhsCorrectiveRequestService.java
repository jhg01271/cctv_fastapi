package kr.co.igns.business.evaluation.service;
import kr.co.igns.business.evaluation.dao.postgres.AuditResultReportDAO;
import kr.co.igns.business.evaluation.dao.postgres.OhsCorrectiveRequestDAO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.AuditResultDetaillVO;
import kr.co.igns.business.evaluation.model.OhsCorrectiveRequestVO;
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
public class OhsCorrectiveRequestService {
    private final OhsCorrectiveRequestDAO ohsCorrectiveRequestDAO;
    private final AuditResultReportDAO auditResultReportDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<OhsCorrectiveRequestVO> getCorrectiveRequest(SpSearchVO searchVo) throws Exception {
        List<OhsCorrectiveRequestVO> voList = ohsCorrectiveRequestDAO.getCorrectiveRequest(searchVo);
        return voList;
    } 

    public OhsCorrectiveRequestVO getCorrectiveRequestDetail(SpSearchVO searchVo) throws Exception {
        OhsCorrectiveRequestVO vo = ohsCorrectiveRequestDAO.getCorrectiveRequestDetail(searchVo);

        vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq()+vo.getDocDetailSeq(), "OCAR"));
        AuditExecutionPlanDetailVO resultReport = new AuditExecutionPlanDetailVO();
            resultReport.setWriteYear(searchVo.getWriteYear());
            resultReport.setDocNo(searchVo.getDocNo());
            resultReport.setDocSeq(searchVo.getDocSeq());
            resultReport.setDocType("ARR");
        vo.setAuditHrList(auditResultReportDAO.getAuditResultReportHr(resultReport));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
	public OhsCorrectiveRequestVO saveCorrectiveRequest(List<MultipartFile> files, OhsCorrectiveRequestVO vo) throws Throwable {
        if (vo.getCmd()!=null && vo.getCmd().equals("U")) {
            ohsCorrectiveRequestDAO.updateCorrectiveRequest(vo);
            fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq()+vo.getDocDetailSeq(), SecurityUtil.getCurrentCompId());
        } else {                                                                                                                      
            vo.setDocType("OCAR");
            ohsCorrectiveRequestDAO.insertCorrectiveRequest(vo);
        }


		//파일 저장
		fileService.saveFile(files, "OCAR", vo.getWriteYear()+vo.getDocNo()+vo.getDocSeq()+vo.getDocDetailSeq(), SecurityUtil.getCurrentCompId());
		return vo;
	}

    @Transactional(rollbackFor = Throwable.class)
    public SpSearchVO deleteCorrectiveRequest(List<SpSearchVO> voList) throws Exception{
        for(SpSearchVO vo:voList){
            ohsCorrectiveRequestDAO.deleteCorrectiveRequest(vo);
        }
        return voList.get(0);
    }

    public List<JasperPrint> getCorrectiveRequestReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "안전보건 시정조치 요구서";
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
            OhsCorrectiveRequestVO result = getCorrectiveRequestDetail(vo); // 데이터
            if(isSingle==true){
                fileNm +=  "_"+result.getAuditNm()+"_"+result.getOrgnNm();
            }
            System.out.println(fileNm);
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/evaluation/ohsCorrectiveActionRequest/ohsCorrectiveActionRequest.jrxml");
            reportVO.setType(spSearchVO.getType());

            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            Map<String, String> signTypes = Map.of("orgnHead", "signOrgnHead","teamLeader", "signTeamLeader");
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType("OCAR");
            signParam.setTargetId(result.getWriteYear() + result.getDocNo() + result.getDocSeq() + result.getDocDetailSeq());
            //결재
            for (var signType : signTypes.entrySet()) {
            	signParam.setType(signType.getKey());    

                List<UtilsVO> signList = utilsService.getApprovalInfo(signParam);
                
	            for (UtilsVO signInfo : signList) {
	            	String type = signType.getKey();
	            	
	                // 서명 데이터 입력
	                if(type.equals("orgnHead")) {
	                    params.put(signType.getValue(), utilsService.getSignatureFromBase64String(signInfo.getSignature()));
	                    params.put(signType.getValue() + "Nm", signInfo.getHrNm());
	                }
	                if(type.equals("teamLeader")) {
	                    params.put(signType.getValue(), utilsService.getSignatureFromBase64String(signInfo.getSignature()));
	                    params.put(signType.getValue() + "Nm", signInfo.getHrNm());
	                }
	            }
            }
            params.put("orgnNm",result.getOrgnNm());
            params.put("docNo",result.getWriteYear()+result.getDocNo()+result.getDocSeq()+result.getDocDetailSeq());
            
            // 날짜 형식 변환
            if(result.getWriteDt() != null && !result.getWriteDt().equals("")) {
            	result.setWriteDt(DateUtils.formatDate(result.getWriteDt()));
            }else {
            	result.setWriteDt("");
            }
            params.put("auditDt",result.getWriteDt());
            String auditHrList = "";
            for(HrVO hr : result.getAuditHrList()){
                auditHrList = auditHrList + hr.getHrNm() + ", ";
            }

            if(!auditHrList.isEmpty()){
                auditHrList = auditHrList.substring(0,auditHrList.length()-2);
            }

            params.put("auditHrList",auditHrList);
            params.put("nonconformities",result.getNonconformities());
            params.put("measures",result.getMeasures());
            String remark;
        	remark = "유효성 확인 결과\n";
            if(result.getValidate()!=null){
                if(result.getValidate().equals("Y")){
                    remark += "■ 적합 □ 부적합";
                }else if(result.getValidate().equals("N")){
                    remark += "□ 적합 ■ 부적합";
                }else{
                    remark += "□ 적합 □ 부적합";
                }
            }else{
                remark += "□ 적합 □부적합";
            }
            if(result.getRequired() == "Y" || result.getRequired().equals("Y")){
                remark = remark + "\n\n유효성 점검 필요 여부\n■ 필요 □ 불필요";
                if(result.getFinalCheckDt() != null){
                	remark = remark + "\n최종 점검일자 : " + result.getFinalCheckDt();
                }
            }else {
            	remark = remark + "\n\n유효성 점검 필요 여부\n□ 필요 ■ 불필요";
            }
            
            params.put("remark",remark);
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
