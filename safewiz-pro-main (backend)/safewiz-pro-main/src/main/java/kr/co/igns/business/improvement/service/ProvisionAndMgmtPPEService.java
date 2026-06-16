package kr.co.igns.business.improvement.service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.improvement.dao.postgres.ProvisionAndMgmtPPEDAO;
import kr.co.igns.business.improvement.model.ProvisionAndMgmtPPEVO;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ProvisionAndMgmtPPEService {
    private final ProvisionAndMgmtPPEDAO provisionAndMgmtPPEDao;
	private final UtilsDAO utilsDAO;
	private final CompDAO compDao;
	private final ClientDAO clientDao;
	private final UtilsService utilsService;
	private final ReportService reportService;

	public List<ProvisionAndMgmtPPEVO> getProvisionAndMgmtPPEList(@Valid SpSearchVO searchVo) {
		List<ProvisionAndMgmtPPEVO> voList = provisionAndMgmtPPEDao.getProvisionAndMgmtPPEList(searchVo);
		return voList;
	}
	
	public List<ProvisionAndMgmtPPEVO> getProvisionAndMgmtPPEDetailList(@Valid ProvisionAndMgmtPPEVO searchVo) {
		List<ProvisionAndMgmtPPEVO> voList = provisionAndMgmtPPEDao.getProvisionAndMgmtPPEDetailList(searchVo);
		return voList;
	}

	
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo) throws Exception {
    	vo.setCreatedBy(SecurityUtil.getCurrentMemberId());

    	provisionAndMgmtPPEDao.insertProvisionAndMgmtPPE(vo);
        return vo;
    }

	public BaseVO deleteProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo) throws Exception {
		vo.setCreatedBy(SecurityUtil.getCurrentMemberId());

		provisionAndMgmtPPEDao.deleteProvisionAndMgmtPPE(vo);
		UtilsVO uvo = new UtilsVO();
		uvo.setTargetType("PPE");
		uvo.setTargetId(vo.getWriteYear() + vo.getPpeId() + vo.getDocNo() );
        utilsDAO.deleteApprovalInfoAll(uvo);
		
       return vo;
	}

	// 데이터 쪼개기 함수
    public List<List<ProvisionAndMgmtPPEVO>> splitData(List<ProvisionAndMgmtPPEVO> data, int splitSize) {
        // 데이터 그룹화
        Map<String, List<ProvisionAndMgmtPPEVO>> groupedData = data.stream()
                .collect(Collectors.groupingBy(ProvisionAndMgmtPPEVO::getPpeNm));

        // 각 그룹 내 0번지의 ppeOrd 기준으로 오름차순 정렬
        groupedData = groupedData.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().get(0).getPpeOrd()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        List<String> uniqueValues = new ArrayList<>(groupedData.keySet());

        List<List<ProvisionAndMgmtPPEVO>> result = new ArrayList<>();

        // 데이터를 splitSize 마다 분리
        for (int i = 0; i < uniqueValues.size(); i += splitSize) {
            List<String> splitKeys = uniqueValues.subList(i, Math.min(i + splitSize, uniqueValues.size()));
            List<ProvisionAndMgmtPPEVO> splitData = new ArrayList<>();

            // 청크에 데이터를 추가
//            System.out.println("@@@@@@@ chunkKeys " + splitKeys);
            for (String key : splitKeys) {
                splitData.addAll(groupedData.get(key));
            }
            int requireEmptyCnt = (splitKeys.size() % splitSize == 0)? 0: splitSize - (splitKeys.size() % splitSize); // 추가가 필요한 빈 값의 갯수

//            System.out.println("# 1 chunk.size() = " + splitData.size());
//            System.out.println("# 2 chunkPpeData.size() = " + splitKeys.size());
//            System.out.println("# 3 chunkPpeData.size()%"+splitSize+" = " + splitKeys.size() % splitSize);
//            System.out.println("# 4 requireEmptyCnt = " + requireEmptyCnt);
//            System.out.println("# 5 chunk = " + splitData);

            // 청크의 크기가 splitSize 보다 작으면 빈 객체를 추가하여 크기 맞추기
            int lastIndex = splitData.size() - 1;
            if (requireEmptyCnt>0) {
                //입고,지급,현재고
                for (int index = 0; index < requireEmptyCnt; index++) {
                    splitData.add(getNewDataset(splitData.get(lastIndex - 2), String.valueOf(index)));
                    splitData.add(getNewDataset(splitData.get(lastIndex - 1), String.valueOf(index)));
                    splitData.add(getNewDataset(splitData.get(lastIndex), String.valueOf(index)));
                }
            }
            // 결과 리스트에 청크 추가
            result.add(splitData);
        }

        return result;
    }
	public ProvisionAndMgmtPPEVO getNewDataset(ProvisionAndMgmtPPEVO data, String ppeNm) {
        ProvisionAndMgmtPPEVO emptyData = new ProvisionAndMgmtPPEVO();
        emptyData.setPpeNm("emptyData" + ppeNm);
        emptyData.setReceiptDt(data.getReceiptDt());
        emptyData.setOrgnNm(data.getOrgnNm());
        emptyData.setManagerNm(data.getManagerNm());
        emptyData.setPpeOrd(99999999);
        emptyData.setOrdSeq(data.getOrdSeq());
        emptyData.setDocNo(data.getDocNo());
        emptyData.setGubun(data.getGubun());
        return emptyData;
    }

	//**********************레포트 관련******************************
    public ReportVO getReportProvisionAndMgmtPPE(HttpServletRequest request, HttpServletResponse response, List<ProvisionAndMgmtPPEVO> voList)
			throws Exception {

    	/***********************/
		/****** 공통 정보 조회 ******/
		/***********************/
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
 		
        //String fileNm = "안전보호구 지급 및 관리";
 		String fileNm = String.format("(%s)", voList.get(0).getWriteYear());
        String title = "안전보호구 품목 관리";
        fileNm+=title;
        if(voList.size()==1)
		{
			fileNm+=String.format("_%s",voList.get(0).getPpeNm() );
		}
        introReportVO.setFileName(title);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", "안전보호구 지급 및 관리");
        introParams.put("subTitle", "사업장명: " + cPo.getCompNm());
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
         
        introParams.put("page", searchVO.getPage());
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());
		
        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);
        
		/**********************************/
		/****** 출력물 공통 Parameter 입력 *****/
		/**********************************/
		
		ProvisionAndMgmtPPEVO commonVo = voList.get(0);
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("page", 0);
		params.put("subPage", 0);
		params.put("localPage", 0);

		// Logo 파일 조회
		InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
		params.put("logo", logo);
		
		
		/*************************************/
		/****** 출력물 Custom Parameter 입력 *****/
		/*************************************/
		
		params.put("year",commonVo.getReceiptDt().substring(0, 4));
		
		//조직명 입력
		params.put("title", "");
				
		//gridData 조회
		

		List<ProvisionAndMgmtPPEVO> unionData = new ArrayList<ProvisionAndMgmtPPEVO>();
        for (ProvisionAndMgmtPPEVO vo : voList) {
            List<ProvisionAndMgmtPPEVO> data = provisionAndMgmtPPEDao.getReportDataProvisionAndMgmt(vo);
            // 데이터를 10개 단위로 분할
            unionData.addAll(data);
        }
		List<Map<String, JRBeanCollectionDataSource>> datasource = new ArrayList<>();
        List<List<ProvisionAndMgmtPPEVO>> chunks = splitData(unionData, 10);
            // 결과 출력
	    for (List<ProvisionAndMgmtPPEVO> chunk : chunks) {
		    Map<String, JRBeanCollectionDataSource> dataSourceMap = new HashMap<>();
		    dataSourceMap.put("gridData", new JRBeanCollectionDataSource(chunk));
		    datasource.add(dataSourceMap);
	    }
        //gridData 입력
        params.put("datasource", new JRBeanCollectionDataSource(datasource));

//		for(ProvisionAndMgmtPPEVO vo : voList) {
//			List<ProvisionAndMgmtPPEVO> data = provisionAndMgmtPPEDao.getReportDataProvisionAndMgmt(vo);
//			unionData.addAll(data);
//		}
//		//gridData 입력
//		params.put("gridData", new JRBeanCollectionDataSource(unionData));
				

		/*************************************/
		/****** Jasper Export File Setup *****/
		/*************************************/
				
		ReportVO reportVO = new ReportVO();
				
    	// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
//		fileNm = cPo.getCompNm() + "_안전보호구 지급 및 관리_";
//		fileNm += "(" + DateUtils.getNowDate("YYYY.MM.dd") + ")";
		
		// 출력 파일 명 설정
		reportVO.setFileName(fileNm);
		
		// 출력 생성용 Jasper 파일 위치
		reportVO.setJrxmlPath("report/improvement/provisionAndMgmtPPE/provisionAndMgmtPPE_new.jrxml");

		// 출력  파일 형식 지정
		reportVO.setType("pdf");
		
		/**********************/
		/****** 출력물 출력 *******/
		/**********************/
		
		reportVO.setParameter(params);
		JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        jasperPrintList.add(JasperReport);
        
		// 통합 출력인 경우 reportVO만 반환하고 종료
		if(commonVo.isPrintAll()) return reportVO;
		
		reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
		
		return reportVO;
	}
    
  //**********************레포트 관련******************************
    public ReportVO getReportPPEManagement(HttpServletRequest request, HttpServletResponse response, SpSearchVO vo)
			throws Exception {

    	/***********************/
		/****** 공통 정보 조회 ******/
		/***********************/
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
 		
 		//String fileNm = String.format("(%s)", vo.getCheckedObjList().get(0).getWriteYear());
        String fileNm = "안전보호구 품목 관리";
		
        //fileNm+=title;
//        if(vo.getCheckedObjList().size()==1)
//		{
//			fileNm+=String.format(("_%s",vo.getCheckedObjList().get(0).get )
//		}
        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", fileNm);
        introParams.put("subTitle", "사업장명: " + cPo.getCompNm());
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
         
        introParams.put("page", searchVO.getPage());
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());
		
        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);
        
		/**********************************/
		/****** 출력물 공통 Parameter 입력 *****/
		/**********************************/		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("page", 0);
		params.put("subPage", 0);
		params.put("localPage", 0);

		// Logo 파일 조회
		InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
		params.put("logo", logo);
		
		
		/*************************************/
		/****** 출력물 Custom Parameter 입력 *****/
		/*************************************/						
		//gridData 조회
		List<ProvisionAndMgmtPPEVO> unionData = provisionAndMgmtPPEDao.getDatasetPPEListReport(vo);

		//gridData 입력
		params.put("gridData", new JRBeanCollectionDataSource(unionData));
				

		/*************************************/
		/****** Jasper Export File Setup *****/
		/*************************************/
				
		ReportVO reportVO = new ReportVO();
				
    	// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
		//fileNm = cPo.getCompNm() + "_안전보호구 품목 관리_";
		//fileNm += "(" + DateUtils.getNowDate("YYYY.MM.dd") + ")";
		
		// 출력 파일 명 설정
		reportVO.setFileName(fileNm);
		
		// 출력 생성용 Jasper 파일 위치
		reportVO.setJrxmlPath("report/improvement/provisionAndMgmtPPE/PPEManagement.jrxml");

		// 출력  파일 형식 지정
		reportVO.setType("pdf");
		
		/**********************/
		/****** 출력물 출력 *******/
		/**********************/
		
		reportVO.setParameter(params);
		JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        jasperPrintList.add(JasperReport);
        
		reportService.exportReportAll(request, response, jasperPrintList, vo.getType(), fileNm);
		
		return reportVO;
	}
	
    public ReportVO getReportPPEManagementchk(HttpServletRequest request, HttpServletResponse response, List<ProvisionAndMgmtPPEVO> voList)
			throws Exception {

    	/***********************/
		/****** 공통 정보 조회 ******/
		/***********************/
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
 		

        String fileNm = "안전보호구 품목 관리";
		
        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", fileNm);
        introParams.put("subTitle", "사업장명: " + cPo.getCompNm());
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
         
        introParams.put("page", searchVO.getPage());
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());
		
        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);
        
		/**********************************/
		/****** 출력물 공통 Parameter 입력 *****/
		/**********************************/		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("page", 0);
		params.put("subPage", 0);
		params.put("localPage", 0);

		// Logo 파일 조회
		InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
		params.put("logo", logo);
		
		
		/*************************************/
		/****** 출력물 Custom Parameter 입력 *****/
		/*************************************/						
		//gridData 조회
				
	    // voList에서 compId 추출
	    String compId = voList.get(0).getCompId();

	    // voList에서 ppeId만 추출하여 리스트로 만듦
	    List<String> ppeIdList = voList.stream()
	                                    .map(ProvisionAndMgmtPPEVO::getPpeId)
	                                    .collect(Collectors.toList());

	    // Map으로 파라미터를 넘겨줌
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("compId", compId);
	    paramMap.put("ppeIdList", ppeIdList);
	    
	    // DAO 호출
	    List<ProvisionAndMgmtPPEVO> unionData = provisionAndMgmtPPEDao.getReportPPEManagementchk(paramMap);
	    		
		//gridData 입력
		params.put("gridData", new JRBeanCollectionDataSource(unionData));
				

		/*************************************/
		/****** Jasper Export File Setup *****/
		/*************************************/
			
        ReportVO reportVO = new ReportVO();
				
    	// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
		//fileNm = cPo.getCompNm() + "_안전보호구 품목 관리_";
		//fileNm += "(" + DateUtils.getNowDate("YYYY.MM.dd") + ")";
		
		// 출력 파일 명 설정
		reportVO.setFileName(fileNm);
		
		// 출력 생성용 Jasper 파일 위치
		reportVO.setJrxmlPath("report/improvement/provisionAndMgmtPPE/PPEManagement.jrxml");

		// 출력  파일 형식 지정
		reportVO.setType("pdf");
		
		/**********************/
		/****** 출력물 출력 *******/
		/**********************/
		
		reportVO.setParameter(params);
		JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        jasperPrintList.add(JasperReport);
        
		reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);

		return reportVO;
	}
    
    
	
	public List<ProvisionAndMgmtPPEVO> getDatasetPPEList(@Valid SpSearchVO searchVo) {
		List<ProvisionAndMgmtPPEVO> voList = provisionAndMgmtPPEDao.getDatasetPPEList(searchVo);
		return voList;
	}
	
	@Transactional(rollbackFor = Throwable.class)
    public BaseVO saveDatasetPPE(List<ProvisionAndMgmtPPEVO> voList) throws Exception {
		for(ProvisionAndMgmtPPEVO vo : voList) {
			vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
			
			if(vo.getPpeId() == null || "".equals(vo.getPpeId())) {
				provisionAndMgmtPPEDao.insertDatasetPPE(vo);
			} else {
				provisionAndMgmtPPEDao.updateDatasetPPE(vo);
			}
		}

        return voList.get(0);
    }

	@Transactional(rollbackFor = Throwable.class)
	public BaseVO deleteDatasetPPE(List<ProvisionAndMgmtPPEVO> voList) {
		for(ProvisionAndMgmtPPEVO vo : voList) {
			vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
			provisionAndMgmtPPEDao.deleteDatasetPPE(vo);
		}
       return voList.get(0);
	}
}
