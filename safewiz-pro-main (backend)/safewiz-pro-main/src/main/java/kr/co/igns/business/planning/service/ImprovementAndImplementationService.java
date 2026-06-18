package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import kr.co.igns.business.planning.dao.postgres.ImprovementAndImplementationDAO;
import kr.co.igns.business.planning.model.ImprovementAndImplementationVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import kr.co.igns.system.common.service.NasFileService;
import net.sf.jasperreports.engine.JasperPrint;

@Service
@RequiredArgsConstructor
public class ImprovementAndImplementationService {
	private final ImprovementAndImplementationDAO dao;
	private final ClientDAO clientDao;
	private final CompDAO compDao;
	private final UtilsService utilsService;
	private final ReportService reportService;
	private final UtilsDAO utilsDao;
	private final NasFileService nasFileService;
	
	public List<ImprovementAndImplementationVO> searchData(ImprovementAndImplementationVO vo) {
		List<ImprovementAndImplementationVO> result = dao.searchData(vo);
		return result;
	}
	
	public List<ImprovementAndImplementationVO> searchDataDetail(ImprovementAndImplementationVO vo) {
		List<ImprovementAndImplementationVO> result = dao.searchDataDetail(vo);
		return result;
	}
	
	public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO listVo) throws Exception {
		SpSearchVO vo = listVo.getCheckedObjList().get(0);
		List<JasperPrint> reportList = new ArrayList<>();
		String title = "개선 실행 및 결과";
		
		int page = vo.getPage();
        int subPage = vo.getSubPage();
        int localPage = vo.getLocalPage();
        
        JasperPrint JasperFrontReport = utilsService.getFrontReport(vo, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        
    	vo.setClntId(SecurityUtil.getCurrentClntId());
    	
		ClientVO cVo = clientDao.getClientById(vo.getClntId());
		String clntNm = cVo.getClntNm();
		
		vo.setCompId(SecurityUtil.getCurrentCompId());
    	
		CompVO cpVO = new CompVO();
		cpVO.setClntId(vo.getClntId());
		cpVO.setCompId(vo.getCompId());
		CompVO cPo = compDao.getCompById(cpVO);
		String compNm = cPo.getCompNm();

		String fileNmEnd = "";
		
		for(int i = 0; i < listVo.getCheckedObjList().size(); i++) {
			List<ImprovementAndImplementationVO> searchData = dao.searchReport(listVo.getCheckedObjList().get(i));
			
			if(listVo.getCheckedObjList().size() == 1 && i == 0) {
				fileNmEnd = String.format("_%s", searchData.get(0).getHazardsFactor());
			}
			for (ImprovementAndImplementationVO searchDatum : searchData) {
				InputStream logo = utilsService.getClntLogo(vo.getClntId());
				ReportVO reportVO = new ReportVO();
				Map<String, Object> params = new HashMap<String, Object>();
				if(searchDatum.getRiskAssessmentStandards().equals("3a")){
					reportVO.setJrxmlPath("report/planning/improvementAndImplementation/improvementAndImplementation3a.jrxml");
				}else{
					reportVO.setJrxmlPath("report/planning/improvementAndImplementation/improvementAndImplementation.jrxml");
				}

				params.put("page", page);
				params.put("subPage", subPage);
				params.put("localPage", localPage);
				params.put("logo", logo);
				params.put("processNm", searchDatum.getProcessNm());
				params.put("hazardsFactor", searchDatum.getHazardsFactor());
				params.put("frequencyScore", searchDatum.getFrequencyScore());
				params.put("impactScore", searchDatum.getImpactScore());
				params.put("riskScore", searchDatum.getRiskScore());
				params.put("afterFrequencyScore", searchDatum.getAfterFrequencyScore());
				params.put("afterImpactScore", searchDatum.getAfterImpactScore());
				params.put("afterRiskScore", searchDatum.getAfterRiskScore());
				params.put("implContent", searchDatum.getImplContent());
				params.put("reductionMeasures", searchDatum.getReductionMeasures());
				params.put("workDetailHrs", searchDatum.getWorkDetailHrs());
				params.put("completedDate", DateUtils.formatDate(searchDatum.getCompletedDate()));
				if (searchDatum.getPrevFileId() != null) {
					params.put("prevImg", nasFileService.getNasFileInputStream(searchDatum.getPrevFileId()));
				} else {
					params.put("prevImg", null);
				}
				if (searchDatum.getAfterFileId() != null) {
					params.put("afterImg", nasFileService.getNasFileInputStream(searchDatum.getAfterFileId()));
				} else {
					params.put("afterImg", null);
				}
				reportVO.setParameter(params);
				JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
				reportList.add(JasperReport);
				page += JasperReport.getPages().size();
			}
		}
		
		String fileNm = String.format("(%s)개선 실행 및 결과%s", vo.getWriteYear(), fileNmEnd);
		
		if (vo.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, listVo.getType(), fileNm);
		
		return reportList;
	}
}
