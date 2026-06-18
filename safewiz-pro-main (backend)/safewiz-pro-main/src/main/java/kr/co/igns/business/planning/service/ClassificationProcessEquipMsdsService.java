package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.ClassificationProcessEquipMsdsDAO;
import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsAnalysisVO;
import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsItemVO;
import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ClassificationProcessEquipMsdsService {
    private final ClassificationProcessEquipMsdsDAO classificationProcessEquipMsdsDAO;
	private final ReportService reportService;
	private final UtilsService utilsService;
	private final CompDAO compDao;
	private final ClientDAO clientDao;
	
    /**
     * 조직 별 공정 리스트 조회
     * @param searchVo
     * @return
     * @throws Exception
     */
    public List<SafetyAndHealthInfoSurveyVO> getClassProcList(ClassificationProcessEquipMsdsVO searchVo) throws Exception {
        List<SafetyAndHealthInfoSurveyVO> voList = classificationProcessEquipMsdsDAO.selectClassProcList(searchVo);
        return voList;
    }
    
    /**
     * 공정 별 아이템(equip, msds) 조회
     * @param writeYear
     * @param revNoList
     * @param processIdList
     * @return
     * @throws Exception
     */
    public List<SafetyAndHealthInfoSurveyVO> getPrcsItemList(String writeYear, List<String> revNoList, List<String> processIdList) throws Exception {
    	Map<String, Object> params = new HashMap<>();
    	params.put("writeYear", writeYear);
    	params.put("revNoList", revNoList);
    	params.put("processIdList", processIdList);
    	
    	List<SafetyAndHealthInfoSurveyVO> voList = classificationProcessEquipMsdsDAO.selectPrcsItemList(params);
        return voList;
    }
    
    /**
     * 공정/설비/물질 구분 목록 조회
     * @param searchVo
     * @return
     * @throws Exception
     */
    public List<ClassificationProcessEquipMsdsVO> getClassProcEquipMsdsList(SpSearchVO searchVo) throws Exception {
        List<ClassificationProcessEquipMsdsVO> voList = classificationProcessEquipMsdsDAO.selectClassProcEquipMsdsList(searchVo);
        return voList;
    }
    
    /**
     * 공정/설비/물질 구분 상세 조회
     * @param searchVo
     * @return
     * @throws Exception
     */
    public ClassificationProcessEquipMsdsVO getClassProcEquipMsdsDetail(ClassificationProcessEquipMsdsVO searchVo) throws Exception {
    	ClassificationProcessEquipMsdsVO vo = classificationProcessEquipMsdsDAO.selectClassProcEquipMsdsDetail(searchVo);
        return vo;
    }
    	
    /**
     * 공정/설비/물질 구분 입력 및 수정
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveClassProcEquipMsds(ClassificationProcessEquipMsdsVO reqVo) throws Exception {
    	
    	// 신규 생성인 경우
    	if ("I".equals(reqVo.getCmd())) { 
            // 1. 마스터 데이터 insert
    		classificationProcessEquipMsdsDAO.insertClassificationProcessEquipMsds(reqVo);
    	}
    	// 수정인 경우
    	else if ("U".equals(reqVo.getCmd())) {
    		// 1. 마스터 데이터 수정
    		classificationProcessEquipMsdsDAO.updateClassProcEquipMsds(reqVo);
    		// 1-1. 참여자 전체 삭제
    		classificationProcessEquipMsdsDAO.deleteClassProcParticipant(reqVo);
    		// 1-2. 공정, 공정관련 item(equip, msds) 전체 삭제
    		classificationProcessEquipMsdsDAO.deleteClassProcAnalysis(reqVo);
    		classificationProcessEquipMsdsDAO.deleteClassProcEquipMsds(reqVo);
    	}
    	
    	// 2. 참여자 정보가 있으면 insert
		if (reqVo.getHrList().size() > 0) {
			classificationProcessEquipMsdsDAO.insertClassificationProcessEquipMsdsParticipant(reqVo);    			
		}
		// 3. 공정분석 정보가 있으면
		if (reqVo.getProcessList().size() > 0) {
			// 3-1. 공정 insert
			for (int i = 0; i < reqVo.getProcessList().size(); i++) {
				ClassificationProcessEquipMsdsAnalysisVO data = reqVo.getProcessList().get(i);
				// 3-1-1. 공정 정보 insert 시 필요 데이터 세팅
				data.setWriteYear(reqVo.getWriteYear());
				data.setDocNo(reqVo.getDocNo());
				data.setDocType(reqVo.getDocType());
				classificationProcessEquipMsdsDAO.insertClassProcAnalysis(data);
				
				// 3-2. 공정에 따른 아이템(equip, msds) insert
				if (data.getItemList().size() > 0) {
					classificationProcessEquipMsdsDAO.insertClassProcEquipMsds(data);
				}
			}
		}
        return reqVo;
    }

	public BaseVO confirmClassProc(ClassificationProcessEquipMsdsVO vo) throws Exception {
		classificationProcessEquipMsdsDAO.confirmClassProc(vo);
		return vo;
	}
    
    /**
     * 공정/설비/물질 구분 삭제
     * @param list
     * @throws Exception
     */
    public void deleteClassProcEquipMsds(List<ClassificationProcessEquipMsdsVO> list) throws Exception {
        for (ClassificationProcessEquipMsdsVO tmp : list) {
        	classificationProcessEquipMsdsDAO.deleteClassProc(tmp);
        }
    }

	// 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedList(classificationProcessEquipMsdsDAO.getAllReport(spSearchVO));
		if(!spSearchVO.getCheckedList().isEmpty()) {
			spSearchVO.setPrintAll(true);
			spSearchVO.setDocType("CPE");
			jaspers = getReport(request, response, spSearchVO);
			reportList.addAll(jaspers);
        }
        return reportList;
    }

    /**
     * 공정/설비/물질 구분 출력
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO vo) throws Exception {
    	List<JasperPrint> jasperPrintList = new ArrayList<>();
    	
    	/***********************/
		/****** 공통 정보 조회 ******/
		/***********************/
    	
    	// 고객사 ID 조회
    	vo.setClntId(SecurityUtil.getCurrentClntId());
    	
    	// 고객사 명칭 조회
		ClientVO cVo = clientDao.getClientById(vo.getClntId());
		String clntNm = cVo.getClntNm();
		
		// 사업장 ID 조회
		vo.setCompId(SecurityUtil.getCurrentCompId());
    	
		// 사업장 명칭 조회
		CompVO cpVO = new CompVO();
		cpVO.setClntId(vo.getClntId());
		cpVO.setCompId(vo.getCompId());
		CompVO cPo = compDao.getCompById(cpVO);
		String compNm = cPo.getCompNm();
		
		String writeYear = vo.getWriteYear();
		

		// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
    	String fileNm = "";
		//fileNm = clntNm + "_" + compNm + "_";
    	fileNm = "(" + writeYear + ")";
    	
    	if(vo.getCheckedList().size() > 1) {
    		fileNm += "공정설비물질 구분";
    	}else {
    		ClassificationProcessEquipMsdsVO param = new ClassificationProcessEquipMsdsVO();
	    	param.setWriteYear(vo.getWriteYear());
	    	param.setCompId(vo.getCompId());
	    	param.setDocNo(vo.getCheckedList().get(0));
	    	param.setDocType(vo.getDocType());
	    	
			ClassificationProcessEquipMsdsVO reportData = classificationProcessEquipMsdsDAO.selectClassProcEquipMsdsDetail(param);
    		fileNm += "공정설비물질 구분_" + reportData.getOrgnNm();
    	}
    	
		
		//fileNm += "(" + DateUtils.getNowDate("YYYY.MM.dd") + ")";

		
		/*************************************/
		/****** Jasper Export File Setup *****/
		/*************************************/
		
		/*
         * 인트로 페이지
         */
		int page = vo.getPage();
        int subPage = vo.getSubPage();
        int localPage = vo.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(vo, "공정/설비/물질 구분");
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();
		
		/*************************************/
		/****** 출력물 Custom Parameter 입력 *****/
		/*************************************/
		for (String docNo : vo.getCheckedList()) {
			/**********************************/
			/****** 출력물 공통 Parameter 입력 *****/
			/**********************************/
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("year", vo.getWriteYear());

			//고객사 명 입력
			params.put("clntNm", clntNm); 
			//사업장 명 입력
			params.put("compNm", compNm);
			// Logo 파일 조회
			InputStream logo = utilsService.getClntLogo(vo.getClntId());
			params.put("logo", logo);
			ReportVO reportVO = new ReportVO();

	    	// 출력 파일 명 설정
	    	reportVO.setFileName(fileNm);
	    	// 출력 생성용 Japser 파일 위치
	    	reportVO.setJrxmlPath("report/planning/classificationProcessEquipMsds/classificationProcessEquipMsds.jrxml");
	    	// 출력  파일 형식 지정
	    	reportVO.setType("pdf");
	    	
	    	ClassificationProcessEquipMsdsVO param = new ClassificationProcessEquipMsdsVO();
	    	param.setWriteYear(vo.getWriteYear());
	    	param.setCompId(vo.getCompId());
	    	param.setDocNo(docNo);
	    	param.setDocType(vo.getDocType());
	    	
	    	params.put("page", page);
			params.put("subPage", subPage);
			params.put("localPage", localPage);
	    	
			// 출력물 용 데이터 조회
			ClassificationProcessEquipMsdsVO reportData = classificationProcessEquipMsdsDAO.selectClassProcEquipMsdsDetail(param);
			params.put("title", reportData.getOrgnNm());
			params.put("orgnNm", reportData.getOrgnNm());							// 조직명
			params.put("headNm", reportData.getHeadNm());							// 조직장
			params.put("memberCount", reportData.getMemberCount());					// 근로자수
			params.put("processOverview", reportData.getProcessOverview());			// 공정개요 (주요업무내용)
			params.put("startAt", DateUtils.formatDate(reportData.getStartAt()));	// 평가 시작일
			params.put("endAt", DateUtils.formatDate(reportData.getEndAt()));		// 평가 종료일
			params.put("remark", reportData.getRemark());							// 비고
			
			// 담당자 담은 리스트
			StringBuilder hrList = new StringBuilder();
			if (reportData.getHrList().size() > 0) {
				for (HrVO hr : reportData.getHrList()) {
					if (hrList.length() > 0) hrList.append(", ");
					// 직급이 있는 경우 표시
					if (hr.getJbrpNm() != null) {					
						hrList.append(hr.getHrNm()).append("(").append(hr.getJbrpNm()).append(")");
					} else {
						hrList.append(hr.getHrNm());
					}
				}
			}
			params.put("participant", hrList.toString());	// 참여자
			
			// processList 결과를 담을 리스트
	        List<Map<String, String>> result = new ArrayList<>();
			for (int i = 0; i < reportData.getProcessList().size(); i++) {
				ClassificationProcessEquipMsdsAnalysisVO process = reportData.getProcessList().get(i);
				
				Map<String, String> map = new HashMap<>();
				StringBuilder equipNm = new StringBuilder();
				StringBuilder msdsNm = new StringBuilder();
				
				for (ClassificationProcessEquipMsdsItemVO item : process.getItemList()) {
					if ("EQUIP".equals(item.getType())) {
						if (equipNm.length() > 0) equipNm.append(", ");
						equipNm.append(item.getItemNm());
					}
					else if ("MSDS".equals(item.getType())) {
						if (msdsNm.length() > 0) msdsNm.append(", ");
						msdsNm.append(item.getItemNm());
					}
				}

	            map.put("index", String.valueOf(i+1));
				map.put("equipNm", equipNm.toString());
	            map.put("msdsNm", msdsNm.toString());
	            map.put("processNm", process.getProcessNm());
	            map.put("processDesc", process.getProcessDesc());
	            result.add(map);
			}
			
			params.put("gridData", new JRBeanCollectionDataSource(result));	// 공정분석 
			
			reportVO.setParameter(params);
			JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);
            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
		}
		
    	// 통합 출력인 경우 reportVO만 반환하고 종료
		if (vo.isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, vo.getType(), fileNm);
        }
        return jasperPrintList;

    }
}
