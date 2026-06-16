package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.WorkersOpinionsOnHazardsDAO;
import kr.co.igns.business.planning.model.WorkersOpinionsOnHazardsVO;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
@RequiredArgsConstructor
public class WorkersOpinionsOnHazardsService { 
    private final WorkersOpinionsOnHazardsDAO workerOpinionsOnHazardsDAO;
	private final ClientDAO clientDao;
	private final CompDAO compDao;
	private final ReportService reportService;
	private final UtilsService utilsService;
	
	public List<WorkersOpinionsOnHazardsVO> getWorkersOpinionsOnHazards(@Valid SpSearchVO searchVo) {
		List<WorkersOpinionsOnHazardsVO> voList = workerOpinionsOnHazardsDAO.getWorkersOpinionsOnHazards(searchVo);
        
        return voList;
	}
	//#region [단일 키값 검색 - 문서 찾기] Sim 25. 3. 10.
	public String getHrId(@Valid SpSearchVO searchVo) {
		String result = workerOpinionsOnHazardsDAO.getHrId(searchVo);
		return result;
	}
	//#endregion
	public List<WorkersOpinionsOnHazardsVO> getWorkersOpinionsOnHazardsDetail(@Valid SpSearchVO searchVo) {
		List<WorkersOpinionsOnHazardsVO> voList = workerOpinionsOnHazardsDAO.getWorkersOpinionsOnHazardsDetail(searchVo);
        
        return voList;
	}
	
	public List<WorkersOpinionsOnHazardsVO> getWorkerHr(@Valid WorkersOpinionsOnHazardsVO searchVo) {
		List<WorkersOpinionsOnHazardsVO> voList = workerOpinionsOnHazardsDAO.getWorkerHr(searchVo);
        return voList;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public List<WorkersOpinionsOnHazardsVO> createWorkersOpinionsOnHazardsDetail(List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		
		for(WorkersOpinionsOnHazardsVO saveVo : saveVoList) {
//			List<WorkersOpinionsOnHazardsVO> dupeVo = riskAssessmentDao.getWorkeryDupe(saveVo);
//			if(dupeVo.size() == 0) {
//				riskAssessmentDao.insertWorkersOpinionsOnHazards(saveVo);
//				
//			} else {
//				saveVo.setWriteYear(dupeVo.get(0).getWriteYear());
//				riskAssessmentDao.changeWorkersOpinionsOnHazards(saveVo);
//			}
//			2025.01.02 년도는 파라미터에서 받아오도록 수정
//			saveVo.setWriteYear(String.valueOf(LocalDate.now().getYear()));
			saveVo.setDocType("WOOHA");
			if("I".equals(saveVo.getCmd())) {
				workerOpinionsOnHazardsDAO.insertWorkersOpinionsOnHazardsDetail(saveVo);
			} else {
				workerOpinionsOnHazardsDAO.changeWorkersOpinionsOnHazardsDetail(saveVo);
			}
			
			for(WorkersOpinionsOnHazardsVO vo : saveVo.getExperienceList()) {
				vo.setWriteYear(saveVo.getWriteYear());
				vo.setDocType(saveVo.getDocType());
				vo.setDocNo(saveVo.getDocNo());
				vo.setRegDt(saveVo.getRegDt());
				vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
				vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
				if ("D".equals(vo.getCmd())) {
					if("".equals(vo.getExperienceNo()) || vo.getExperienceNo() == null) continue;
					workerOpinionsOnHazardsDAO.deleteWorkerOpinonsOnHarzardsExp(vo);
				} else if("".equals(vo.getExperienceNo()) || vo.getExperienceNo() == null) {
					workerOpinionsOnHazardsDAO.insertWorkersOpinionsOnHazardsExp(vo);
				} else {
					workerOpinionsOnHazardsDAO.changeWorkersOpinionsOnHazardsExp(vo);
				}
			}
		}
		return saveVoList;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public List<WorkersOpinionsOnHazardsVO> createWorkersOpinionsOnHazardsExp(List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		for(WorkersOpinionsOnHazardsVO saveVo : saveVoList) {
			workerOpinionsOnHazardsDAO.insertWorkersOpinionsOnHazardsExp(saveVo);
		}
		return saveVoList;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public List<WorkersOpinionsOnHazardsVO> changeWorkersOpinionsOnHazardsUseN(List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		for(WorkersOpinionsOnHazardsVO saveVo : saveVoList) {
			workerOpinionsOnHazardsDAO.changeWorkersOpinionsOnHazardsUseN(saveVo);
		}
		return saveVoList;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<WorkersOpinionsOnHazardsVO> deleteWorkerOpinonsOnHarzardsDetail(List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		for(WorkersOpinionsOnHazardsVO saveVo : saveVoList) {
			workerOpinionsOnHazardsDAO.deleteWorkerOpinonsOnHarzardsDetail(saveVo);
		}
		return saveVoList;
	}    

	// 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
		List<WorkersOpinionsOnHazardsVO> voList = workerOpinionsOnHazardsDAO.getWorkersOpinionsOnHazards(spSearchVO);
        if(!voList.isEmpty()) {
            voList.get(0).setPrintAll(true);
            voList.get(0).setPage(spSearchVO.getPage());
            voList.get(0).setSubPage(spSearchVO.getSubPage());
            voList.get(0).setLocalPage(spSearchVO.getLocalPage());
            jaspers = getWorkerReportList(request, response, voList);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    // 유해위험 근로자 리포트 마스터에서 인원 선택
    public List<JasperPrint>  getWorkerReportList(HttpServletRequest request, HttpServletResponse response, List<WorkersOpinionsOnHazardsVO> voList)
			throws Exception {

    	List<JasperPrint> jasperPrintList = new ArrayList<>();
    	// 임시 
    	List<WorkersOpinionsOnHazardsVO> searchVo = new ArrayList<WorkersOpinionsOnHazardsVO>();
    	
    	// 사람을 선택해서 들어오기 때문에 해당하는 사람의 모든 키값을 먼저 받아와야 한다.
    	for(WorkersOpinionsOnHazardsVO vo : voList) {
    		// 그리고 받아온 키값을 임시 searchVo에 넣어준다.
    		for(WorkersOpinionsOnHazardsVO tempVo : workerOpinionsOnHazardsDAO.getWorkerReportHrList(vo)) {
    			searchVo.add(tempVo);
    		}
    	}
        /*
         * 인트로 페이지
         */
        ReportVO introReportVO = new ReportVO();
        CompVO cpVO = new CompVO();
        SpSearchVO searchVO = new SpSearchVO();
        // 고객사 ID 조회
        searchVO.setClntId(SecurityUtil.getCurrentClntId());
    	
    	// 고객사 명칭 조회
		ClientVO cVo = clientDao.getClientById(searchVO.getClntId());
		String clntNm = cVo.getClntNm();
		
		// 사업장 ID 조회
		searchVO.setCompId(SecurityUtil.getCurrentCompId());
		
		cpVO.setClntId(searchVO.getClntId());
		cpVO.setCompId(searchVO.getCompId());
		CompVO cPo = compDao.getCompById(cpVO);
		String fileNm = "("+voList.get(0).getWriteYear()+")"+"유해위험요인 근로자 의견";
		if (voList.size() == 1) {
			fileNm = "("+voList.get(0).getWriteYear()+")"+"유해위험요인 근로자 의견_"+voList.get(0).getHrNm();
		}


		int page = voList.get(0).getPage();
        int subPage = voList.get(0).getSubPage();
        int localPage = voList.get(0).getLocalPage();
		SpSearchVO frontVO = new SpSearchVO();
		frontVO.setPage(page);
		frontVO.setSubPage(subPage);
		frontVO.setLocalPage(localPage);
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(frontVO, "유해위험요인 근로자 의견");
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();

        /*
         * 데이터 페이지
         */

        // 인원별로 조회한 키값을 모두 돌려버리기~
        for (WorkersOpinionsOnHazardsVO vo : searchVo) {
        	//출력물 용 데이터 조회
    		ReportVO reportVO = new ReportVO();
    		
    		WorkersOpinionsOnHazardsVO reportData = workerOpinionsOnHazardsDAO.getWorkerReportDetail(vo);
    		reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/riskAssessment/workersOpinionsOnHazards/WorkersOpinionsOnHazards.jrxml");
            reportVO.setType("pdf");
            
    		// 파라미터 세팅
            Map<String, Object> params = new HashMap<>();
            
    		//조직명 입력
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
    		params.put("workplaceNm", reportData.getWorkplaceNm());
    		params.put("regDt", DateUtils.formatDate(reportData.getRegDt()));
    		params.put("writerNm", reportData.getWriterNm());
			if(reportData.getWriterId() != null) {
    			params.put("writerSign", utilsService.getUserSignatureByHrId(SecurityUtil.getCurrentClntId(), reportData.getWriterId()));
			}
    		params.put("reviewerNm", reportData.getReviewerNm());
			if(reportData.getReviewerId() != null) {
    			params.put("reviewerSign", utilsService.getUserSignatureByHrId(SecurityUtil.getCurrentClntId(), reportData.getReviewerId()));
			}
    		params.put("workerOpinion", reportData.getWorkerOpinion());
    		params.put("reviewerOpinion", reportData.getReviewerOpinion());
    		
    		// 고객사 로고
    		InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);
    		//gridData 조회
    		List<WorkersOpinionsOnHazardsVO> resultList = workerOpinionsOnHazardsDAO.getWorkerReportDetailExp(vo);
    		//gridData 입력
    		params.put("gridData", new JRBeanCollectionDataSource(resultList));

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);

            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
        }
		if (voList.get(0).isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
        }
		return jasperPrintList;
	}
    
    // 유해위험 근로자 리포트 상세
    public void getWorkerReport(HttpServletRequest request, HttpServletResponse response, List<WorkersOpinionsOnHazardsVO> voList)
			throws Exception {

    	List<JasperPrint> jasperPrintList = new ArrayList<>();

        /*
         * 인트로 페이지
         */
        ReportVO introReportVO = new ReportVO();
        CompVO cpVO = new CompVO();
        SpSearchVO searchVO = new SpSearchVO();
        // 고객사 ID 조회
        searchVO.setClntId(SecurityUtil.getCurrentClntId());
    	
    	// 고객사 명칭 조회
		ClientVO cVo = clientDao.getClientById(searchVO.getClntId());
		String clntNm = cVo.getClntNm();
		
		// 사업장 ID 조회
		searchVO.setCompId(SecurityUtil.getCurrentCompId());
		
		cpVO.setClntId(searchVO.getClntId());
		cpVO.setCompId(searchVO.getCompId());
		CompVO cPo = compDao.getCompById(cpVO);

		LocalDate now = LocalDate.now();
		int year = now.getYear();

		String hrNm = "";
		String fileNm = "("+year+")"+"유해위험요인 근로자 의견";
		// hrId로 인물 이름 가져오기
		if(!voList.isEmpty()) {
			hrNm = workerOpinionsOnHazardsDAO.getWorkerHrName(voList.get(0).getHrId());
			fileNm = "("+voList.get(0).getWriteYear()+")"+"유해위험요인 근로자 의견_"+hrNm;
		}

        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", "유해 위험요인 근로자 의견");
        introParams.put("subTitle", "사업장명: " + cPo.getCompNm());
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
        
        introParams.put("page", searchVO.getPage());
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());

        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);

        /*
         * 데이터 페이지
         */

        int page = 1;

		// 선택한 데이터 전부 출력
		for (WorkersOpinionsOnHazardsVO vo : voList) {
			//출력물 용 데이터 조회
			ReportVO reportVO = new ReportVO();

			WorkersOpinionsOnHazardsVO reportData = workerOpinionsOnHazardsDAO.getWorkerReportDetail(vo);
			reportVO.setFileName(fileNm);
			reportVO.setJrxmlPath("report/riskAssessment/workersOpinionsOnHazards/WorkersOpinionsOnHazards.jrxml");
			reportVO.setType("pdf");

			// 파라미터 세팅
			Map<String, Object> params = new HashMap<>();

			//조직명 입력
			params.put("page", page);
			params.put("subPage", 0);
			params.put("localPage", 0);
			params.put("workplaceNm", reportData.getWorkplaceNm());
			params.put("regDt", DateUtils.formatDate(reportData.getRegDt()));
			params.put("writerNm", reportData.getWriterNm());
			params.put("reviewerNm", reportData.getReviewerNm());
			params.put("workerOpinion", reportData.getWorkerOpinion());
			params.put("reviewerOpinion", reportData.getReviewerOpinion());

			// 고객사 로고
			InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
			params.put("logo", clntLogo);
			//gridData 조회
			List<WorkersOpinionsOnHazardsVO> resultList = workerOpinionsOnHazardsDAO.getWorkerReportDetailExp(vo);
			//gridData 입력
			params.put("gridData", new JRBeanCollectionDataSource(resultList));

			reportVO.setParameter(params);
			JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
			jasperPrintList.add(JasperReport);

			page =+ JasperReport.getPages().size() + page;
		}

        reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
	}
}
