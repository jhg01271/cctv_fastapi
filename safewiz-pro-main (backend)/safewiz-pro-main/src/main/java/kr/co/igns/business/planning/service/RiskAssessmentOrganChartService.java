package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.RiskAssessmentOrganChartDAO;
import kr.co.igns.business.planning.model.RiskAssessmentOrganChartVO;
import kr.co.igns.business.planning.model.RiskOrgnRoleVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class RiskAssessmentOrganChartService {
    private final RiskAssessmentOrganChartDAO riskAssessmentOrganChartDAO;
    private final SystemCodeDAO systemCodeDao;
    private final UtilsService utilsService;
    private final ReportService reportService;

    /**
     * 위험성평가 조직 역할 구성 데이터 조회
     * @param searchVo
     * @return
     */
    public List<RiskOrgnRoleVO> getRiskOrgList(SpSearchVO searchVo) throws Exception {
        List<RiskOrgnRoleVO> voList = riskAssessmentOrganChartDAO.getRiskOrgList(searchVo);
        return voList;
    }

    /**
     * 위험성평가 조직 역할 예시 데이터 조회
     * @param searchVo
     * @return
     */
    public List<RiskOrgnRoleVO> getDataSetRiskOrgList(SpSearchVO searchVo) throws Exception {
        List<RiskOrgnRoleVO> voList = riskAssessmentOrganChartDAO.getDataSetRiskOrgList(searchVo);
        return voList;
    }

    /**
     * 전체인원 검색
     * @param searchVo
     * @return
     */
    public List<HrVO> getMembers(SpSearchVO searchVo) throws Exception {
        List<HrVO> voList = riskAssessmentOrganChartDAO.getMembers(searchVo);
        return voList;
    }

    public List<RiskOrgnRoleVO> saveRiskOrgList(List<RiskOrgnRoleVO> reqVo) throws Exception {
        for(RiskOrgnRoleVO vo : reqVo){
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                riskAssessmentOrganChartDAO.insertRiskOrgList(vo);
            }else{
                riskAssessmentOrganChartDAO.updateRiskOrgList(vo);
            }
        }
        return reqVo;
    }



 
    /**
     * 조직도 목록 조회
     * @param searchVo
     * @return
     * @throws Exception
     */
    public List<RiskAssessmentOrganChartVO> getRiskAssessmentOrganChartList(SpSearchVO searchVo) throws Exception {
        List<RiskAssessmentOrganChartVO> voList = riskAssessmentOrganChartDAO.getRiskAssessmentOrganChartList(searchVo);
        return voList;
    }
    
    /**
     * 조직도 상세 조회
     * @param chartId
     * @return
     * @throws Exception
     */
    public RiskAssessmentOrganChartVO getRiskAssessmentOrganChartDetail(String chartId) throws Exception {
    	RiskAssessmentOrganChartVO vo = riskAssessmentOrganChartDAO.getRiskAssessmentOrganChartDetail(chartId);
        return vo;
    }
    
    /**
     * 조직도 입력 및 수정
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveRiskAssessmentOrganChart(RiskAssessmentOrganChartVO reqVo) throws Exception {
    	
    	// 신규 생성인 경우
    	if ("I".equals(reqVo.getCmd())) {
            // 2. 데이터 insert
            riskAssessmentOrganChartDAO.insertRiskAssessmentOrganChart(reqVo);
    	}
    	// 수정일 경우
    	else if ("U".equals(reqVo.getCmd())) {
    		// 1. 조직 수정
    		RiskAssessmentOrganChartVO vo = riskAssessmentOrganChartDAO.getChartDetail(reqVo.getChartId());
            if (vo == null)
                return null;
            vo = (RiskAssessmentOrganChartVO) SpUtils.objectMap(reqVo, vo);
            // 2. 데이터 update
            riskAssessmentOrganChartDAO.updateRiskAssessmentOrganChart(vo);
    	}
        return reqVo;
    }
    
    /**
     * 조직도 확정여부 수정
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO confirmRiskAssessmentOrganChart(RiskAssessmentOrganChartVO reqVo) throws Exception {
    	riskAssessmentOrganChartDAO.confirmRiskAssessmentOrganChart(reqVo);
        return reqVo;
    }
    
    /**
     * 조직도 삭제
     * @param list
     * @throws Exception
     */
    public void deleteRiskAssessmentOrganChart(List<RiskAssessmentOrganChartVO> list) throws Exception {
        for (RiskAssessmentOrganChartVO tmp : list) {
        	riskAssessmentOrganChartDAO.deleteRiskAssessmnetOrganChart(tmp);
        }
    }

    public List<JasperPrint> getRiskAssessmentOrganChartReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String fileNm = "위험성평가 조직도";
        String title = "위험성평가 조직도";

        // 레포트 출력 타입이 새로형인지 가로형인지
        // 표지 리포트
        String typeMinorCd = "HOR";
        SystemCodeVO systemCodeReqVo = new SystemCodeVO();
        systemCodeReqVo.setMajorCd("OT01");
        List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);
        SystemMinorCodeVO selectedType = systemCodeList.stream()
                .filter(el -> "Y".equals(el.getExtra1()))
                .findFirst()
                .orElse(null); // 혹은 기본값을 설정 가능
        if (selectedType != null) {
            typeMinorCd = selectedType.getMinorCd();
        }
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport(selectedType.getExtra2(), spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            RiskAssessmentOrganChartVO chartData = getRiskAssessmentOrganChartDetail(vo.getSearchText());
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm = "위험성평가 조직도" + "_" + chartData.getChartNm();
            }
            String jrxmlPath = "report/planning/riskAssessmentOrganChart/";
            if ("HOR".equals(typeMinorCd)) {
                // 가로형
                jrxmlPath = jrxmlPath + "chart_hor.jrxml";
            } else if ("VER".equals(typeMinorCd)) {
                // 세로형
                jrxmlPath = jrxmlPath + "chart_VER.jrxml";
            }

            //파라미터 설정
            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath(jrxmlPath); // 레포트 파일 경로
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("page", page); // 페이지
            params.put("subPage", subPage); // 서브페이지
            params.put("localPage", localPage); // 로컬페이지

            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);

            // 데이터 바인딩
            params.put("chartId", chartData.getChartId());
            params.put("chartNm", chartData.getChartNm());
            if ("Y".equals(chartData.getConfirmYn())) {
                String confirmAt = DateUtils.formatDate(chartData.getConfirmDt());
                params.put("confirmAt", confirmAt);
            }
            String createdAt = DateUtils.formatDate(chartData.getCreatedAt());
            params.put("createdAt", createdAt);
            params.put("orgnNm", chartData.getOrgnNm());

            params.put("chartBlobData", utilsService.convertToInputStream(chartData.getChartBlobData()));
            reportVO.setParameter(params);

            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();


        }
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }
}
