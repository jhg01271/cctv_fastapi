package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import kr.co.igns.business.planning.dao.postgres.ClassificationOfHazardsDAO;
import kr.co.igns.business.planning.model.ClassificationOfHazardsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.dao.postgres.OrganizationDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ClassificationOfHazardsService {
	private final ClassificationOfHazardsDAO classificationOfHazardsDAO;
	private final UtilsService utilsService;
	private final ReportService reportService;
	private final OrganizationDAO organizationDao;

	/**
	 * 유해위험요인 분류 조회
	 * 
	 * @param ClassificationOfHazardsVO
	 * @return
	 * @throws Exception
	 */
	public List<ClassificationOfHazardsVO> getHazardsList(SpSearchVO searchVo) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getHazardsList(searchVo);
		return voList;
	}

	public List<ClassificationOfHazardsVO> selectHazardsList(ClassificationOfHazardsVO vo) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.selectHazardsList(vo);
		return voList;
	}

	/**
	 * 유해위험요인 분류상세 공정 조회
	 * 
	 * @param ClassificationOfHazardsVO
	 * @return
	 * @throws Exception
	 */
	public List<ClassificationOfHazardsVO> getProcssList(SpSearchVO searchVo) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getProcessList(searchVo);
		for (ClassificationOfHazardsVO vo : voList) {
			// 조직 매핑 조회
			BaseMapVO baseVo = new BaseMapVO("process", vo.getProcessId());
			List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(baseVo);

			// orgnList의 orgnNm들을 엔터로 구분하여 하나의 텍스트로 변환
			StringBuilder orgnNmBuilder = new StringBuilder();
			for (int i = 0; i < orgnlist.size(); i++) {
				orgnNmBuilder.append(orgnlist.get(i).getNm());
				// 마지막 항목이 아니면 엔터(\n) 추가
				if (i < orgnlist.size() - 1) {
					orgnNmBuilder.append("\n");
				}
			}
			vo.setNm(orgnNmBuilder.toString());

			vo.setOrgnList(orgnlist);
		}
		return voList;
	}

	/**
	 * 유해위험요인 상세 조회
	 * 
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public List<ClassificationOfHazardsVO> getHazardsDetail(SpSearchVO searchVo) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.selectHazardDetail(searchVo);
		return voList;
	}

	/**
	 * 유해위험요인 분류상세 공정에 해당하는 설비,위험물질 조회
	 * 
	 * @param ClassificationOfHazardsVO
	 * @return
	 * @throws Exception
	 */
	public List<ClassificationOfHazardsVO> getProcessEquipmentAndMsds(String processId) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getProcessEquipmentAndMsds(processId);
		return voList;
	}

	/**
	 * 유해위험요인 분류상세 수정시 공정에 해당하는 위험요인분류 목록 조회
	 * 
	 * @param ClassificationOfHazardsVO
	 * @return
	 * @throws Exception
	 */
	public List<ClassificationOfHazardsVO> getSavedFactorList(ClassificationOfHazardsVO vo) throws Exception {
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getSavedFactorList(vo);
		return voList;
	}

	/**
	 * 유해위험요인 분류상세 저장
	 * 
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Throwable.class)
	public BaseVO saveHazards(ClassificationOfHazardsVO reqVo) throws Exception {
		ClassificationOfHazardsVO result = classificationOfHazardsDAO.getHazardDupe(reqVo);

		// cmd가 I (신규저장)일 때
		if ("I".equals(reqVo.getCmd())) {

			if (result != null) {
				// 중복확인
				if (!"".equals(result.getDocNo()) && "Y".equals(reqVo.getUseYn())) { // 기존에 등록된 데이터가 있으면서 신규 데이터가 Y인 경우 제외
					return null;
				}
			}

			// 1. master 정보 저장
			classificationOfHazardsDAO.saveHazards(reqVo);
		}
		// cmd가 U(수정)일 때
		else if ("U".equals(reqVo.getCmd())) {

			// 중복 확인
			if (result != null) {
				String dupeId = result.getWriteYear() + result.getDocNo(); // 중복 데이터의 id
				String targetId = reqVo.getWriteYear() + reqVo.getDocNo(); // 수정 데이터의 id
				// result와 reqVo가 id가 다르면서 result의 useYn = 'Y', reqVo의 useYn = 'Y'인 경우 저장 불가
				if (!dupeId.equals(targetId) && "Y".equals(result.getUseYn()) && "Y".equals(reqVo.getUseYn())) {
					return null;
				}
			}

			// 1. master 정보 수정
			classificationOfHazardsDAO.updateHazards(reqVo);
			// 2. 상세 정보 전체 삭제
			classificationOfHazardsDAO.deleteHazardsDetail(reqVo);
		}

		// 유해위험요인 데이터가 있는 경우 insert
		if (reqVo.getFactorList().size() > 0) {
			classificationOfHazardsDAO.saveHazardsList(reqVo);
		}

		return reqVo;
	}

	/**
	 * 유해위험요인 분류 전년도 불러오기 저장
	 *
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void savePrevHazards(List<ClassificationOfHazardsVO> reqVo) throws Exception {
		for(ClassificationOfHazardsVO vo : reqVo){
			SpSearchVO searchVO = new SpSearchVO();
			searchVO.setCompId(vo.getCompId());
			searchVO.setWriteYear(vo.getWriteYear());
			searchVO.setDocNo(vo.getDocNo());

			// 해당 key를 통해 디테일 조회 후 그대로 저장
			List<ClassificationOfHazardsVO> result = classificationOfHazardsDAO.selectHazardDetail(searchVO);

			String writeYear = String.valueOf(Integer.valueOf(vo.getWriteYear()) + 1);
			vo.setWriteYear(writeYear);
			vo.setDocNo(null);

			classificationOfHazardsDAO.saveHazards(vo);

			// 유해위험요인 데이터가 있는 경우 insert
			if (!result.isEmpty()) {
				for(ClassificationOfHazardsVO factor : result.get(0).getFactorList()){
					factor.setDetailDocSeq(null);
				}
				vo.setFactorList(result.get(0).getFactorList());
				classificationOfHazardsDAO.saveHazardsList(vo);
			}
		}
	}

	/**
	 * 유해위험요인분류 삭제
	 * 
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteHazards(List<ClassificationOfHazardsVO> reqVo) throws Exception {
		for (ClassificationOfHazardsVO classificationOfHazardsVO : reqVo) {
			classificationOfHazardsDAO.deleteHazardsMaster(classificationOfHazardsVO); // 위험요인분류 삭제
		}
	}

	// 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(classificationOfHazardsDAO.getAllReport(spSearchVO));
        if(!spSearchVO.getCheckedObjList().isEmpty()) {
            spSearchVO.setPrintAll(true);
            jaspers = getClassificationOfHazardsReports(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

	public List<JasperPrint> getClassificationOfHazardsReports(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid SpSearchVO vo) throws Exception {
		List<JasperPrint> jasperPrintList = new ArrayList<>(); // 출력물 리스트
		Map<String, Object> classificationOfHazardsParams = new HashMap<>();

		/*
		 * 인트로 페이지
		 */
		ReportVO introReportVO = new ReportVO();
		String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());

		String fileNm = '(' + vo.getWriteYear() + ')' + "유해위험요인분류";

		int page = vo.getPage();
        int subPage = vo.getSubPage();
        int localPage = vo.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(vo, "유해 위험요인분류");
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();
		// 선택한 데이터만큼 조회한다.
		for (SpSearchVO obj : vo.getCheckedObjList()) {
			ReportVO reportvo = new ReportVO();
//			reportvo.setFileName(fileNm); // 파일이름 세팅
			reportvo.setJrxmlPath("report/planning/classificationOfHazards/classificationOfHazards.jrxml"); // 제스퍼 레포트
			// 파일경로
			reportvo.setType("pdf"); // 레포트 파일 확장자 세팅
			SpSearchVO searchvo = new SpSearchVO();
			searchvo.setCompId(SecurityUtil.getCurrentCompId()); // 회사ID
			searchvo.setWriteYear(obj.getWriteYear()); // 작성년도
			searchvo.setDocNo(obj.getDocNo()); // 문서번호
			searchvo.setDocType("CHD"); // 문서타입
			List<ClassificationOfHazardsVO> searchResult = classificationOfHazardsDAO.selectHazardDetail(searchvo); // 공정,설비,물질
			if(vo.getCheckedObjList().size() == 1) {
				fileNm += "_" + searchResult.get(0).getProcessNm();
			}
			List<ClassificationOfHazardsVO> factorResult = classificationOfHazardsDAO.getPrintFactorList(searchvo); // 위험요인분류
			List<Map<String, Object>> tableRowData = new ArrayList<>(); // 테이블 로우
			String processNm = "";
			String msds = "";
			String equipment = "";
			List<String> msdsList = new ArrayList<String>();
			List<String> equipmentList = new ArrayList<String>();
			// 공정,설비 넣어주는부분
			for (ClassificationOfHazardsVO item : searchResult) {
				processNm = item.getProcessNm();
				for (int k = 0; k < item.getItemList().size(); k++) {
					String workType = item.getItemList().get(k).getWorkType();
					if (item.getItemList().get(k).getContent() == null) {
						continue;
					}
					if ("msds".equals(workType)) {
						if (!msdsList.contains(item.getItemList().get(k).getContent())) {
							msdsList.add(item.getItemList().get(k).getContent());
						}
					} else if ("equipment".equals(workType)) {
						if (!equipmentList.contains(item.getItemList().get(k).getContent())) {
							equipmentList.add(item.getItemList().get(k).getContent());
						}
					}
				}
			}
			for (String m : msdsList) {
				msds += m + ", ";
			}
			for (String m : equipmentList) {
				equipment += m + ", ";
			}
			classificationOfHazardsParams.put("Process", processNm);
			if (equipment.length() >= 2) {
				classificationOfHazardsParams.put("Equipment", equipment.substring(0, equipment.length() - 2));
			} else {
				classificationOfHazardsParams.put("Equipment", "");
			}
			if (msds.length() >= 2) {
				classificationOfHazardsParams.put("Msds", msds.substring(0, msds.length() - 2));
			} else {
				classificationOfHazardsParams.put("Msds", "");
			}
			// 각 요인별 유해위험분류값들
			String factor1Name = "";
			String factor2Name = "";
			String factor3Name = "";
			String factor4Name = "";
			String factor5Name = "";
			String factor6Name = "";
			for (ClassificationOfHazardsVO factorItem : factorResult) {
				String factorName = factorItem.getFactorId();
				switch (factorName) {
					case "factor_1":
						factor1Name += " " + factorItem.getClassNm();
						break;
					case "factor_2":
						factor2Name += " " + factorItem.getClassNm();
						break;
					case "factor_3":
						factor3Name += " " + factorItem.getClassNm();
						break;
					case "factor_4":
						factor4Name += " " + factorItem.getClassNm();
						break;
					case "factor_5":
						factor5Name += " " + factorItem.getClassNm();
						break;
					case "factor_6":
						factor6Name += " " + factorItem.getClassNm();
						break;
				}
			}
			if (factor1Name.length() > 0) {
				factor1Name = factor1Name.substring(0, factor1Name.length() - 1);
			}
			if (factor2Name.length() > 0) {
				factor2Name = factor2Name.substring(0, factor2Name.length() - 1);
			}
			if (factor3Name.length() > 0) {
				factor3Name = factor3Name.substring(0, factor3Name.length() - 1);
			}
			if (factor4Name.length() > 0) {
				factor4Name = factor4Name.substring(0, factor4Name.length() - 1);
			}
			if (factor5Name.length() > 0) {
				factor5Name = factor5Name.substring(0, factor5Name.length() - 1);
			}
			if (factor6Name.length() > 0) {
				factor6Name = factor6Name.substring(0, factor6Name.length() - 1);
			}
			Map<String, Object> row1 = new HashMap<>();
			row1.put("number", "1.");
			row1.put("factorName", "기계(설비)적 요인");
			row1.put("factorList", factor1Name);
			Map<String, Object> row2 = new HashMap<>();
			row2.put("number", "2.");
			row2.put("factorName", "전기적 요인");
			row2.put("factorList", factor2Name);
			Map<String, Object> row3 = new HashMap<>();
			row3.put("number", "3.");
			row3.put("factorName", "화학(물질)적 요인");
			row3.put("factorList", factor3Name);
			Map<String, Object> row4 = new HashMap<>();
			row4.put("number", "4.");
			row4.put("factorName", "생물학적 요인");
			row4.put("factorList", factor4Name);
			Map<String, Object> row5 = new HashMap<>();
			row5.put("number", "5.");
			row5.put("factorName", "작업특성 요인");
			row5.put("factorList", factor5Name);
			Map<String, Object> row6 = new HashMap<>();
			row6.put("number", "6.");
			row6.put("factorName", "작업환경 요인");
			row6.put("factorList", factor6Name);
			classificationOfHazardsParams.put("Page", page);
			classificationOfHazardsParams.put("subPage", subPage);
			classificationOfHazardsParams.put("localPage", localPage);
			// 고객사 로고
			InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
			classificationOfHazardsParams.put("logo", clntLogo);
			tableRowData.add(row1);
			tableRowData.add(row2);
			tableRowData.add(row3);
			tableRowData.add(row4);
			tableRowData.add(row5);
			tableRowData.add(row6);
			classificationOfHazardsParams.put("tableDataSet", new JRBeanCollectionDataSource(tableRowData));
			reportvo.setParameter(classificationOfHazardsParams);
			JasperPrint JasperReport = reportService.allReportJasperPrint(reportvo);
			jasperPrintList.add(JasperReport);

			page += JasperReport.getPages().size();
        	localPage += JasperReport.getPages().size();
		}
		if (vo.isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, vo.getType(), fileNm);
        }
        return jasperPrintList;

	}
	public List<ClassificationOfHazardsVO> getClassData(String code) throws Exception {
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setCompId(SecurityUtil.getCurrentCompId());
		searchVo.setSearchText(code);
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getClassData(searchVo);
		return voList;
	}
	public List<ClassificationOfHazardsVO> getSampleClassData(String code) throws Exception {
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setSearchText(code);
		List<ClassificationOfHazardsVO> voList = classificationOfHazardsDAO.getSampleClassData(searchVo);
		return voList;
	}
	@Transactional(rollbackFor = Throwable.class)
	public BaseVO deleteClassData(List<ClassificationOfHazardsVO> voList) throws Exception {
		for(ClassificationOfHazardsVO vo : voList) {
			classificationOfHazardsDAO.deleteClassData(vo);
		}
		return voList.get(0);
	}

	@Transactional(rollbackFor = Throwable.class)
	public BaseVO saveClassData(List<ClassificationOfHazardsVO> voList) throws Exception {
		for(ClassificationOfHazardsVO vo : voList) {
			if("I".equals(vo.getCmd())) {
				classificationOfHazardsDAO.insertClassData(vo);
			}else {
				classificationOfHazardsDAO.updateClassData(vo);
			}
		}
		return voList.get(0);
	}


}
