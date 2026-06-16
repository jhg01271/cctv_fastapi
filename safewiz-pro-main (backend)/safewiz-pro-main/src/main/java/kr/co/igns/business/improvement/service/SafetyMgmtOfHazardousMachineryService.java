package kr.co.igns.business.improvement.service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;

import kr.co.igns.business.improvement.dao.postgres.SafetyMgmtOfHazardousMachineryDAO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryInspectionVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryOrgnVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryRequestVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SafetyMgmtOfHazardousMachineryService {
	private final SafetyMgmtOfHazardousMachineryDAO safetyMgmtDao;
	private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasFileService;
	
	public List<SafetyMgmtOfHazardousMachineryRequestVO> getSafetyMgmts(SpSearchVO searchVo) {

		List<SafetyMgmtOfHazardousMachineryRequestVO> voList = safetyMgmtDao.getSafetyMgmts(searchVo);


		for(SafetyMgmtOfHazardousMachineryRequestVO vo : voList){
			for(SafetyMgmtOfHazardousMachineryInspectionVO inspection : vo.getSafetyMgmtInspectionList()){
				inspection.setFiles(fileService.getFileList(inspection.getInspectionWriteYear() + vo.getSafetyMgmt().getDocNo() + inspection.getDocSeq(), "SMOHM"));
			}
		}

		return voList;
	}

	@Transactional
	public void saveSafetyMgmt(Map<String, List<MultipartFile>> files, SafetyMgmtOfHazardousMachineryRequestVO vo) throws Exception {
		SafetyMgmtOfHazardousMachineryVO safetyMgmt = vo.getSafetyMgmt();
		List<SafetyMgmtOfHazardousMachineryOrgnVO> orgnList = vo.getSafetyMgmtOrgnList();
		List<SafetyMgmtOfHazardousMachineryInspectionVO> inspectionList = vo.getSafetyMgmtInspectionList();

		// 작성자 생성
		String createAndUpdateBy = SecurityUtil.getCurrentMemberId();

		orgnList.forEach(item -> {
			item.setCreatedBy(createAndUpdateBy);
			item.setUpdatedBy(createAndUpdateBy);
		});
		inspectionList.forEach(item -> {
			item.setCreatedBy(createAndUpdateBy);
			item.setUpdatedBy(createAndUpdateBy);
		});

		if(safetyMgmt.getCmd().equals("I")) {
			// 1. 안전관리 설비가 Insert일 경우
			// 안전관리 설비
			safetyMgmt.setCreatedBy(createAndUpdateBy);
			safetyMgmtDao.insertSafetyMgmt(safetyMgmt);
			// 사용 조직
			for(SafetyMgmtOfHazardousMachineryOrgnVO item : orgnList) {
				item.setDocNo(safetyMgmt.getDocNo());
				safetyMgmtDao.insertSafetyMgmtOrgn(item);
			}
			// 검사 일정
			for(SafetyMgmtOfHazardousMachineryInspectionVO item : inspectionList) {
				item.setDocNo(safetyMgmt.getDocNo());
				safetyMgmtDao.insertSafetyMgmtInspection(item);
				String docSeqKey = item.getDocSeq();
				String fileKey = item.getWriteYear() + item.getDocNo() + docSeqKey;
				// 파일 저장 처리
				if (files != null && files.containsKey(docSeqKey)) {
					fileService.saveFile(files.get(docSeqKey), "SMOHM", fileKey, SecurityUtil.getCurrentCompId());
				}
			}
		}else {
			// 2. 안전관리 설비가 Update일 경우
			// 안전관리 설비
			safetyMgmt.setUpdatedBy(createAndUpdateBy);
			safetyMgmtDao.updateSafetyMgmt(safetyMgmt);

			// 사용 조직
			// getEquip > orgn 정보를 가져와 저장하기 때문에 해당 DB의 상태를 따라가야 함

			// 생성 or 삭제
			// 저장된 db와 view에서 받아온 list 비교
			// db에 없에 view에 있는 정보 : insert
			// db에 있고 view에 없는 정보 : delete
			// 있는 정보는 굳이 db에 접속하지 않음
			// 삽입 대상: 기존 데이터베이스(tb_safety_mgmt_of_hazardous_machinery_orgn)에 없던 데이터
			if(orgnList.size() > 0) {
				List<SafetyMgmtOfHazardousMachineryOrgnVO> oldOrgnList = safetyMgmtDao.getSafetyMgmtOrgnByDocNo(orgnList.get(0));
				List<SafetyMgmtOfHazardousMachineryOrgnVO> insertOrgnList = orgnList.stream()
						.filter(item -> oldOrgnList.stream()
								.map(SafetyMgmtOfHazardousMachineryOrgnVO::getOrgnId)
								.noneMatch(id -> id.equals(item.getOrgnId())))
						.collect(Collectors.toList());
				for(SafetyMgmtOfHazardousMachineryOrgnVO item : insertOrgnList) {
					safetyMgmtDao.insertSafetyMgmtOrgn(item);
				}
				// 삭제 대상(기존에 있었지만 view에서 받왔을땐 없는 데이터)
				List<SafetyMgmtOfHazardousMachineryOrgnVO> deleteOrgnList = oldOrgnList.stream()
						.filter(item -> orgnList.stream()
								.map(SafetyMgmtOfHazardousMachineryOrgnVO::getOrgnId)
								.noneMatch(id -> id.equals(item.getOrgnId())))
						.collect(Collectors.toList());
				for(SafetyMgmtOfHazardousMachineryOrgnVO item : deleteOrgnList) {
					safetyMgmtDao.deleteSafetyMgmtOrgn(item.getDocSeq());
				}
			}

			// 검사 일정
			for (SafetyMgmtOfHazardousMachineryInspectionVO item : inspectionList) {
				String docSeqKey = item.getDocSeq(); // 신규 추가 시에는 임시 번호 저장 되어있음
				String fileKey = item.getWriteYear() + item.getDocNo() + docSeqKey;

				if ("I".equals(item.getCmd())) {
					safetyMgmtDao.insertSafetyMgmtInspection(item);

					// 신규 추가 시 새로 채번 된 docSeq로 재설정
					fileKey = item.getWriteYear() + item.getDocNo() + item.getDocSeq();
				} else {
					safetyMgmtDao.updateSafetyMgmtInspection(item);

					// 삭제할 파일 처리
					fileService.deleteFile(item.getDeleteFiles(), item.getDocType(), fileKey, SecurityUtil.getCurrentCompId());
				}

				// 파일 저장 처리
				if (files != null && files.containsKey(docSeqKey)) {
					fileService.saveFile(files.get(docSeqKey), "SMOHM", fileKey, SecurityUtil.getCurrentCompId());
				}
			}

		}

	}

	public SafetyMgmtOfHazardousMachineryRequestVO getSafetyMgmtDetail(SafetyMgmtOfHazardousMachineryVO vo) {
		
		SafetyMgmtOfHazardousMachineryRequestVO resVo = new SafetyMgmtOfHazardousMachineryRequestVO();
		
		SafetyMgmtOfHazardousMachineryVO safetyMgmt = safetyMgmtDao.getSafetyMgmt(vo);
		resVo.setSafetyMgmt(safetyMgmt);
		
		if(safetyMgmt != null) {
			List<SafetyMgmtOfHazardousMachineryOrgnVO> safetyMgmtOrgns = safetyMgmtDao.getSafetyMgmtOrgns(safetyMgmt);
			resVo.setSafetyMgmtOrgnList(safetyMgmtOrgns);
			
			List<SafetyMgmtOfHazardousMachineryInspectionVO> safetyMgmtInspections = safetyMgmtDao.getSafetyMgmtInspections(safetyMgmt);

			for(SafetyMgmtOfHazardousMachineryInspectionVO inspection : safetyMgmtInspections){
				inspection.setFiles(fileService.getFileList(inspection.getWriteYear() + inspection.getDocNo() + inspection.getDocSeq(), "SMOHM"));
			}

			resVo.setSafetyMgmtInspectionList(safetyMgmtInspections);
		}
		
		return resVo;
	}
	
	public void deleteSafetyMgmts(List<SafetyMgmtOfHazardousMachineryVO> deleteList) {
		for(SafetyMgmtOfHazardousMachineryVO vo : deleteList) {
			safetyMgmtDao.deleteSafetyMgmt(vo);
		}
	}

	public void deleteSafetyMgmtInspections(List<SafetyMgmtOfHazardousMachineryInspectionVO> deleteList) {
		for(SafetyMgmtOfHazardousMachineryInspectionVO vo : deleteList) {
			safetyMgmtDao.deleteSafetyMgmtInspection(vo);
		}
	}
	
	public void getHazardousMachineryReport(
			HttpServletRequest request, HttpServletResponse response,
            List<SafetyMgmtOfHazardousMachineryRequestVO> voList
				) throws Exception {
		
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setCompId(SecurityUtil.getCurrentCompId());
		searchVo.setWriteYear(voList.get(0).getWriteYear());
		List<SafetyMgmtOfHazardousMachineryRequestVO> voDataList = safetyMgmtDao.getSafetyMgmts(searchVo);
		
		/*
         * 인트로 페이지
         */
		InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
		SpSearchVO searchVO = new SpSearchVO();
		
		ReportVO introReportVO = new ReportVO();
		System.out.println("voList :"+voList);
		String fileNm=String.format("(%s)", voList.get(0).getWriteYear());
		//String fileNm = "위험기계 • 기구 목록표";
		String title="위험기계 • 기구 목록표";
		fileNm+=title;
		
		String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
//		introReportVO.setFileName(fileNm);
//        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
//        introReportVO.setType("pdf");
//
//        Map<String, Object> introParams = new HashMap<>();
//        introParams.put("title", title);
//        introParams.put("subTitle", "사업장명: " + compNm);
//        introParams.put("logo", introClntLogo);
//        introParams.put("page", 0);
//        introParams.put("subPage", searchVO.getSubPage());
//        introParams.put("localPage", searchVO.getLocalPage());
//
//        introReportVO.setParameter(introParams);
//        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);

		//레포트 표지 가로 출력으로 수정
		JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse",searchVO,title);
        jasperPrintList.add(JasperFrontReport);
        
        /*
         * 데이터 페이지
         */
        ReportVO dataReportVO = new ReportVO();
        Map<String, Object> dataParams = new HashMap<>();
        
        dataReportVO.setFileName(fileNm);
        dataReportVO.setJrxmlPath("report/improvement/safetyMgmtOfHazardousMachinery/HazardousMachineryReportData.jrxml");
        dataReportVO.setType("pdf");
        
        dataParams.put("title", title);
        dataParams.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId()));
        dataParams.put("subPage", searchVO.getSubPage());
        dataParams.put("localPage", searchVO.getLocalPage());
        dataParams.put("page", JasperFrontReport.getPages().size());
        
        List<Map<String, Object>> resultList = new ArrayList<>();
		for(SafetyMgmtOfHazardousMachineryRequestVO reqVo : voDataList) {
			SafetyMgmtOfHazardousMachineryVO safetyMgmt = reqVo.getSafetyMgmt();
			List<SafetyMgmtOfHazardousMachineryOrgnVO> orgnList = reqVo.getSafetyMgmtOrgnList();
			List<SafetyMgmtOfHazardousMachineryInspectionVO> inspectionList = reqVo.getSafetyMgmtInspectionList();
			
			if(voDataList.size()==1)
			{
				fileNm+=String.format("_%s", safetyMgmt.getEquipmentNm()); 
			}
			
			String orgnValue = orgnList.stream()
	                .map(SafetyMgmtOfHazardousMachineryOrgnVO::getCardOrgnNm)
	                .collect(Collectors.joining(","));
			for(SafetyMgmtOfHazardousMachineryInspectionVO ins : inspectionList) {

				//날짜 포맷팅
				if(isNullOrEmpty(ins.getInspectionDate())){ins.setInspectionDate(DateUtils.getDateByFormatToString(ins.getInspectionDate(), "yyyymmdd", "yyyy.mm.dd")); }
				if (isNullOrEmpty(ins.getInspectionValidFromDt())) {ins.setInspectionValidFromDt(DateUtils.getDateByFormatToString(ins.getInspectionValidFromDt(), "yyyymmdd", "yyyy.mm.dd"));}
				if (isNullOrEmpty(ins.getInspectionValidToDt())) {ins.setInspectionValidToDt(DateUtils.getDateByFormatToString(ins.getInspectionValidToDt(), "yyyymmdd", "yyyy.mm.dd"));}
				if(isNullOrEmpty(ins.getNextInspectionDate())){ ins.setNextInspectionDate(DateUtils.getDateByFormatToString(ins.getNextInspectionDate(), "yyyymmdd", "yyyy.mm.dd"));}

				Map<String, Object> obj = new HashMap<>();
				obj.put("equipmentMgmtNum", safetyMgmt.getEquipmentMgmtNum());
				obj.put("equipmentNm", safetyMgmt.getEquipmentNm());
				obj.put("equipmentMfSpec", safetyMgmt.getEquipmentMfSpec());
				obj.put("orgnList", orgnValue);
				obj.put("inspectionCycle", safetyMgmt.getInspectionCycle());
				obj.put("lastInspectionDate", ins.getInspectionDate());
				obj.put("inspectionValidDate", (ins.getInspectionValidFromDt() != null ? ins.getInspectionValidFromDt() : "") + " ~ " + (ins.getInspectionValidToDt() != null ? ins.getInspectionValidToDt() : ""));
				obj.put("nextInspectionDate", ins.getNextInspectionDate());
				resultList.add(obj);
			}

		}
		
		dataParams.put("gridData", new JRBeanCollectionDataSource(resultList));
		dataReportVO.setParameter(dataParams);
		JasperPrint JasperDataReport = reportService.allReportJasperPrint(dataReportVO);
		jasperPrintList.add(JasperDataReport);
        reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
	}

	public void getHazardousMachineryReportDetail(
			HttpServletRequest request, HttpServletResponse response,
			List<SafetyMgmtOfHazardousMachineryRequestVO> voList
	) throws Exception {

		List<JasperPrint> jasperPrintList = new ArrayList<>();
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setCompId(SecurityUtil.getCurrentCompId());
		searchVo.setWriteYear(voList.get(0).getWriteYear());

		List<String> docNoList = new ArrayList<>();
		for(SafetyMgmtOfHazardousMachineryRequestVO vo : voList){
			docNoList.add(vo.getDocNo());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("compId", SecurityUtil.getCurrentCompId());
		params.put("docNoList", docNoList);
		List<SafetyMgmtOfHazardousMachineryRequestVO> voDataList = safetyMgmtDao.getSafetyMgmtsByDocNo(params);

		/*
		 * 인트로 페이지
		 */
		SpSearchVO searchVO = new SpSearchVO();
		System.out.println("voList :"+voList);
		String fileNm=String.format("(%s)", voList.get(0).getWriteYear());
		//String fileNm = "위험기계 • 기구 목록표";
		String title="위험기계 • 기구 목록표";
		fileNm+=title;

		//레포트 표지 가로 출력으로 수정
		JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse",searchVO,title);
		jasperPrintList.add(JasperFrontReport);

		/*
		 * 데이터 페이지
		 */
		ReportVO dataReportVO = new ReportVO();
		Map<String, Object> dataParams = new HashMap<>();

		dataReportVO.setFileName(fileNm);
		dataReportVO.setJrxmlPath("report/improvement/safetyMgmtOfHazardousMachinery/HazardousMachineryReportData.jrxml");
		dataReportVO.setType("pdf");

		dataParams.put("title", title);
		dataParams.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId()));
		dataParams.put("subPage", searchVO.getSubPage());
		dataParams.put("localPage", searchVO.getLocalPage());
		dataParams.put("page", JasperFrontReport.getPages().size());

		List<Map<String, Object>> resultList = new ArrayList<>();
		for(SafetyMgmtOfHazardousMachineryRequestVO reqVo : voDataList) {
			SafetyMgmtOfHazardousMachineryVO safetyMgmt = reqVo.getSafetyMgmt();
			List<SafetyMgmtOfHazardousMachineryOrgnVO> orgnList = reqVo.getSafetyMgmtOrgnList();
			List<SafetyMgmtOfHazardousMachineryInspectionVO> inspectionList = reqVo.getSafetyMgmtInspectionList();

			if(voDataList.size()==1)
			{
				fileNm+=String.format("_%s", safetyMgmt.getEquipmentNm());
			}

			String orgnValue = orgnList.stream()
					.map(SafetyMgmtOfHazardousMachineryOrgnVO::getOrgnNm)
					.collect(Collectors.joining(","));
			for(SafetyMgmtOfHazardousMachineryInspectionVO ins : inspectionList) {
				//날짜 포맷팅
				if(isNullOrEmpty(ins.getInspectionDate())){ins.setInspectionDate(DateUtils.getDateByFormatToString(ins.getInspectionDate(), "yyyymmdd", "yyyy.mm.dd")); }
				if (isNullOrEmpty(ins.getInspectionValidFromDt())) {ins.setInspectionValidFromDt(DateUtils.getDateByFormatToString(ins.getInspectionValidFromDt(), "yyyymmdd", "yyyy.mm.dd"));}
				if (isNullOrEmpty(ins.getInspectionValidToDt())) {ins.setInspectionValidToDt(DateUtils.getDateByFormatToString(ins.getInspectionValidToDt(), "yyyymmdd", "yyyy.mm.dd"));}
				if(isNullOrEmpty(ins.getNextInspectionDate())){ ins.setNextInspectionDate(DateUtils.getDateByFormatToString(ins.getNextInspectionDate(), "yyyymmdd", "yyyy.mm.dd"));}

				Map<String, Object> obj = new HashMap<>();
				obj.put("equipmentMgmtNum", safetyMgmt.getEquipmentMgmtNum());
				obj.put("equipmentNm", safetyMgmt.getEquipmentNm());
				obj.put("equipmentMfSpec", safetyMgmt.getEquipmentMfSpec());
				obj.put("orgnList", orgnValue);
				obj.put("inspectionCycle", safetyMgmt.getInspectionCycle());
				obj.put("lastInspectionDate", ins.getInspectionDate());
				obj.put("inspectionValidDate", (ins.getInspectionValidFromDt() != null ? ins.getInspectionValidFromDt() : "") + " ~ " + (ins.getInspectionValidToDt() != null ? ins.getInspectionValidToDt() : ""));
				obj.put("nextInspectionDate", ins.getNextInspectionDate());
				resultList.add(obj);
			}

		}

		dataParams.put("gridData", new JRBeanCollectionDataSource(resultList));
		dataReportVO.setParameter(dataParams);
		JasperPrint JasperDataReport = reportService.allReportJasperPrint(dataReportVO);
		jasperPrintList.add(JasperDataReport);
		reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
	}

	public Boolean isNullOrEmpty(String data){
		if(data != null && !data.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
}
