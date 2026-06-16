package kr.co.igns.business.planning.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.evaluation.model.OhsCorrectiveRequestVO;
import kr.co.igns.business.planning.dao.postgres.MsdsDAO;
import kr.co.igns.business.planning.dao.postgres.MsdsUnitDAO;
import kr.co.igns.business.planning.model.ChemDetailResponse;
import kr.co.igns.business.planning.model.ChemDetailVO;
import kr.co.igns.business.planning.model.ChemListResponse;
import kr.co.igns.business.planning.model.ChemSearchVO;
import kr.co.igns.business.planning.model.MsdsHrVO;
import kr.co.igns.business.planning.model.MsdsLawVO;
import kr.co.igns.business.planning.model.MsdsUnitVO;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.dao.postgres.PrcsDAO;
import kr.co.igns.system.base.dao.postgres.WpDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.PrcsWorkMapVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class MsdsService {
    private final MsdsDAO msdsDao;
    private final WpDAO wpDao;
    private final PrcsDAO prcsDao;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasfileService;
    private final MsdsUnitDAO msdsUnitDao;

    @Value("${msds.url}")
    private String MSDS_URL;

    @Value("${msds.key.encoding}")
    private String MSDS_KEY_ENCODING;

    private StringBuilder urlStart() throws Exception {
        return new StringBuilder(MSDS_URL + "/chemlist")
                .append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
                .append("=").append(MSDS_KEY_ENCODING);
    }

    public List<MsdsVO> getMsds(SpSearchVO searchVo) throws Exception {
        List<MsdsVO> voList = msdsDao.getMsds(searchVo);
        for(MsdsVO vo : voList){
        	searchVo.setSearchText(vo.getMsdsId());
            //작업장 매핑 조회
            BaseMapVO searchVo1 = new BaseMapVO("msds", vo.getMsdsId());
            List<BaseMapVO> wplist = wpDao.getWpMapByUseYn(searchVo1);
            vo.setWorkplaceList(wplist);
            //공정 매핑 조회
            List<PrcsWorkVO> prcslist = prcsDao.getPrcsWorkMapByUseYn(searchVo1);
            vo.setWorkList(prcslist);
            
            searchVo.setSearchCd("002");
            List<MsdsLawVO> safetyAndHealthAct = msdsDao.getAct(searchVo);
            searchVo.setSearchCd("004");
            List<MsdsLawVO> chemicalAct = msdsDao.getAct(searchVo);
            searchVo.setSearchCd("006");
            List<MsdsLawVO> dangerousAct = msdsDao.getAct(searchVo);
            
            vo.setSafetyAndHealthAct(safetyAndHealthAct);
            vo.setChemicalAct(chemicalAct);
            vo.setDangerousAct(dangerousAct);
            
            //담당자 조회
            List<MsdsHrVO> hrList = msdsDao.getHrList(searchVo);
            vo.setHrList(hrList);

            MsdsUnitVO msdsUnitVO = new MsdsUnitVO();
            msdsUnitVO.setUnitId(vo.getDailyVolumeUnit());
            msdsUnitVO.setCompId(vo.getCompId());

            MsdsUnitVO muvo = msdsUnitDao.getMsdsUnitById(msdsUnitVO);
            List<MsdsUnitVO> muvoList = new ArrayList<MsdsUnitVO>();
            muvoList.add(muvo);
            vo.setUnitVo(muvoList);     
            
        }
        return voList;
    }

    public MsdsVO getMsdsDetail(SpSearchVO Vo) throws Exception {
        //MSDS 조회(SearchText = MsdsId)
        MsdsVO voList = msdsDao.getMsdsDetail(Vo.getSearchText());

        //작업장 매핑 조회
        BaseMapVO searchVo = new BaseMapVO("msds", voList.getMsdsId());
        List<BaseMapVO> wplist = wpDao.getWpMapByUseYn(searchVo);
        voList.setWorkplaceList(wplist);
        //공정 매핑 조회
        List<PrcsWorkVO> prcslist = prcsDao.getPrcsWorkMapByUseYn(searchVo);
        
        voList.setWorkList(prcslist);
        // 관련 법 조회
        Vo.setSearchCd("002");
        List<MsdsLawVO> safetyAndHealthAct = msdsDao.getAct(Vo);
        Vo.setSearchCd("004");
        List<MsdsLawVO> chemicalAct = msdsDao.getAct(Vo);
        Vo.setSearchCd("006");
        List<MsdsLawVO> dangerousAct = msdsDao.getAct(Vo);
        
        voList.setSafetyAndHealthAct(safetyAndHealthAct);
        voList.setChemicalAct(chemicalAct);
        voList.setDangerousAct(dangerousAct);
        
        //담당자 조회
        List<MsdsHrVO> hrList = msdsDao.getHrList(Vo);
        voList.setHrList(hrList);

        MsdsUnitVO msdsUnitVO = new MsdsUnitVO();
        msdsUnitVO.setUnitId(voList.getDailyVolumeUnit());
        msdsUnitVO.setCompId(voList.getCompId());

        MsdsUnitVO muvo = msdsUnitDao.getMsdsUnitById(msdsUnitVO);
        List<MsdsUnitVO> muvoList = new ArrayList<MsdsUnitVO>();
        muvoList.add(muvo);  
        voList.setUnitVo(muvoList);

        voList.setFiles(fileService.getFileList(voList.getMsdsId(), "msdsFile"));

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertMsds(List<MultipartFile> imageFiles, List<MultipartFile> files, MsdsVO reqVo) throws Exception {

        reqVo.setCompId(SecurityUtil.getCurrentCompId());

        msdsDao.insertMsds(reqVo);

        String targetType = "msds";

        //작업장 매핑 생성
        for (String workplaceId : reqVo.getWorkplaceIdList()) {
            BaseMapVO wpMapInfo = new BaseMapVO(targetType,reqVo.getMsdsId(),workplaceId,reqVo.getCreatedBy());
            wpDao.addWpMap(wpMapInfo);
        }

        //공정 매핑 생성
        for (PrcsWorkVO vo : reqVo.getWorkList()) {
            PrcsWorkMapVO orgnMapInfo = new PrcsWorkMapVO(targetType,reqVo.getMsdsId(),vo.getPrcsWorkId(),reqVo.getCreatedBy(),vo.getProcessId());
            prcsDao.addPrcsWorkMap(orgnMapInfo);
        }

        //파일 저장
        fileService.saveFile(imageFiles, targetType, reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());
        fileService.saveFile(files, "msdsFile", reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());

        for(MsdsLawVO vo : reqVo.getSafetyAndHealthAct()) {
        	vo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(vo);
        }
        for(MsdsLawVO vo : reqVo.getChemicalAct()) {
        	vo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(vo);
        }
        for(MsdsLawVO vo : reqVo.getDangerousAct()) {
        	vo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(vo);
        }
        for(MsdsHrVO vo : reqVo.getHrList()) {
        	vo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsHr(vo);
        }

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateMsds(List<MultipartFile> insertFiles, List<MultipartFile> files,  MsdsVO reqVo) throws Exception {
        //MSDS 수정
        MsdsVO vo = msdsDao.getMsdsById(reqVo.getMsdsId());
        if (vo == null)
            return null;
        vo = (MsdsVO) SpUtils.objectMap(reqVo, vo);

        msdsDao.updateMsds(vo);

        String targetType = "msds";

        //작업장 매핑 수정
        BaseMapVO searchVo2 = new BaseMapVO(targetType,reqVo.getMsdsId());
        List<BaseMapVO> wpList = wpDao.getWpMap(searchVo2);

        // 1. MSDS에 대한 작업장 전체 use_yn = 'N'
        BaseMapVO param = new BaseMapVO(targetType,reqVo.getMsdsId(),null,reqVo.getUpdatedBy());
        wpDao.deleteWpMap(param);

        for (String workplaceId : reqVo.getWorkplaceIdList()) {
            Optional<BaseMapVO> matchingMap = wpList.stream()
                    .filter(data -> data.getId().equals(workplaceId))
                    .findFirst();

            if (matchingMap.isPresent()) {
                // 2. MSDS에 해당 작업장이 원래 있는 경우
                param = matchingMap.get();
                param.setUseYnByString("Y");
                param.setUpdatedBy(reqVo.getUpdatedBy());
                wpDao.updateUseYnWpMap(param);
            }
            else{
                // 3. MSDS에 해당 작업장이 없는 경우
                param.setId(workplaceId);
                param.setCreatedBy(reqVo.getUpdatedBy());
                wpDao.addWpMap(param);
            }
        }

        //공정 매핑 수정
        BaseMapVO searchVo3 = new BaseMapVO(targetType,reqVo.getMsdsId());
        List<PrcsWorkMapVO> prcsList = prcsDao.getPrcsWorkMap(searchVo3);

        // 1. 공정에 대한 조직 전체 use_yn = 'N'
        PrcsWorkMapVO param2 = new PrcsWorkMapVO(targetType,reqVo.getMsdsId(),null,reqVo.getUpdatedBy(),null);
        prcsDao.deletePrcsWorkMap(param2);

        for (PrcsWorkVO wvo : reqVo.getWorkList()) {
            Optional<PrcsWorkMapVO> matchingMap = prcsList.stream()
                    .filter(data -> data.getId().equals(wvo.getPrcsWorkId()))
                    .findFirst();

            if (matchingMap.isPresent()) {
                // 2. 공정에 해당 조직이 원래 있는 경우
                param2 = matchingMap.get();
                param2.setUseYnByString("Y");
                param2.setUpdatedBy(reqVo.getUpdatedBy());
                prcsDao.updateUseYnPrcsWorkMap(param2);
            }
            else{
                // 3. 공정에 해당 조직이 없는 경우
                param2.setId(wvo.getPrcsWorkId());
                param2.setProcessId(wvo.getProcessId());
                param2.setCreatedBy(reqVo.getUpdatedBy());
                prcsDao.addPrcsWorkMap(param2);
            }
        }

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "msds", reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "msds", reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());
        vo.setFileId(fileId);   //수정

        if (!reqVo.getDeleteFiles2().isEmpty()) {
            fileService.deleteFile(reqVo.getDeleteFiles2(), "msdsFile", reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());
        }
        fileService.saveFile(files, "msdsFile", reqVo.getMsdsId(), SecurityUtil.getCurrentCompId());

        msdsDao.deleteLaw(reqVo.getMsdsId());
        msdsDao.deleteHr(reqVo.getMsdsId());
        
        for(MsdsLawVO mlvo : reqVo.getSafetyAndHealthAct()) {
        	mlvo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(mlvo);
        }
        for(MsdsLawVO mlvo : reqVo.getChemicalAct()) {
        	mlvo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(mlvo);
        }
        for(MsdsLawVO mlvo : reqVo.getDangerousAct()) {
        	mlvo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsLaw(mlvo);
        }
        for(MsdsHrVO mhvo : reqVo.getHrList()) {
        	mhvo.setMsdsId(reqVo.getMsdsId());
        	msdsDao.insertMsdsHr(mhvo);
        }

        return vo;
    }

    public BaseVO deleteMsds(MsdsVO reqVo) throws Exception {
        BaseVO vo = msdsDao.getMsdsById(reqVo.getMsdsId());
        msdsDao.deleteMsds(reqVo);
        
        msdsDao.deleteLaw(reqVo.getMsdsId());
        msdsDao.deleteHr(reqVo.getMsdsId());
        
        return vo;
    }

    public void deleteMsdss(List<MsdsVO> list) throws Exception {
        for (MsdsVO tmp : list) {
            msdsDao.deleteMsds(tmp);
            msdsDao.deleteLaw(tmp.getMsdsId());
            msdsDao.deleteHr(tmp.getMsdsId());
        }
    }

    public String getChemData(SpSearchVO searchVo) throws Exception {
        StringBuilder urlBuilder = urlStart()
                // 검색어 ( 필수 )
                .append("&").append(URLEncoder.encode("searchWrd", StandardCharsets.UTF_8))
                .append("=").append(URLEncoder.encode(searchVo.getSearchText(), StandardCharsets.UTF_8))
                // 검색 구분 ( 필수 )
                .append("&").append(URLEncoder.encode("searchCnd", StandardCharsets.UTF_8))
                .append("=").append(URLEncoder.encode(searchVo.getSearchCd(), StandardCharsets.UTF_8))
                // 한 페이지 결과 수 ( 옵션 )
                .append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
                .append("=").append(URLEncoder.encode(searchVo.getSearchText2(), StandardCharsets.UTF_8))
                // 페이지 번호 ( 옵션 )
                .append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
                .append("=").append(URLEncoder.encode(searchVo.getSearchText3(), StandardCharsets.UTF_8));

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();


        try {
            JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
            return xmlJSONObj.toString();
        }catch (Exception e){
            return "error";
        }
    }
    
    public ChemDetailVO getChemDetail(SpSearchVO dto, String operation) throws Exception {
		StringBuilder urlBuilder = urlStart(operation)
				.append("&" + URLEncoder.encode("chemId", "UTF-8") + "=" + URLEncoder.encode(dto.getSearchCd5(), "UTF-8"));
		ChemDetailResponse res =  (ChemDetailResponse) JAXBContext.newInstance(ChemDetailResponse.class)
				.createUnmarshaller().unmarshal(new URL(urlBuilder.toString()));

		System.out.println("res:" + res);
		
		return res.getBody();
	}
    
    public ChemSearchVO getChemList(SpSearchVO dto) throws Exception {
		StringBuilder urlBuilder = urlStart("/chemlist")
				.append("&" + URLEncoder.encode("searchWrd", "UTF-8") + "=" + URLEncoder.encode(dto.getSearchText(), "UTF-8"))
				.append("&" + URLEncoder.encode("searchCnd", "UTF-8") + "=" + URLEncoder.encode(dto.getSearchCd(), "UTF-8"))
				.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(dto.getSearchCd2(), "UTF-8"))
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(dto.getSearchCd3(), "UTF-8"));
		ChemListResponse res = (ChemListResponse) JAXBContext.newInstance(ChemListResponse.class)
				.createUnmarshaller().unmarshal(new URL(urlBuilder.toString()));

		return res.getBody();
	}
    
    public List<JasperPrint> getMsdsReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "MSDS 관리대장";
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
        	MsdsVO result =  getMsdsDetail(vo); // 데이
            if(isSingle==true){
                fileNm +=  "_"+result.getMsdsNm()+"_"+result.getCasNo();
            }
            System.out.println(fileNm);
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/planning/msds/msdsReport.jrxml");
            reportVO.setType(spSearchVO.getType());

            
            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
//            params.put("localPage", localPage);
            
            params.put("casNo", result.getCasNo());
            params.put("msdsNm", result.getMsdsNm());
            String unitStr = "";
            if(result.getUnitVo() !=null && result.getUnitVo().size() > 0 && result.getUnitVo().get(0) != null) {
            	unitStr = result.getUnitVo().get(0).getUnitNm();
            }
            params.put("dailyVolume", result.getDailyVolume() + unitStr);
            params.put("dailyTime", result.getDailyTime() + "시간");
            params.put("msdsSynonym", result.getMsdsSynonym());
            params.put("desc", result.getDesc());
            
            String hrList = result.getHrList().stream().map(MsdsHrVO::getHrNm).collect(Collectors.joining(", "));
            String workPlaceList = result.getWorkplaceList().stream().map(BaseMapVO::getNm).collect(Collectors.joining(", "));
            String workList = result.getWorkList().stream().map(PrcsWorkVO::getProcessNm).collect(Collectors.joining(", "));
            
            params.put("hrList", hrList);
            params.put("workPlaceList", workPlaceList);
            params.put("workList", workList);
            
            int remSafetyAct = result.getSafetyAndHealthAct().size() % 3; 
            int remChemicalAct = result.getChemicalAct().size() % 3; 
            int remDangerousAct = result.getDangerousAct().size() % 3;
            
            List<Map<String,String>> safetyAct1 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> safetyAct2 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> safetyAct3 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> chemicalAct1 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> chemicalAct2 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> chemicalAct3 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> dangerousAct1 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> dangerousAct2 = new ArrayList<Map<String,String>>();
            List<Map<String,String>> dangerousAct3 = new ArrayList<Map<String,String>>();
            
            setAct(params, result.getSafetyAndHealthAct(), remSafetyAct, safetyAct1,safetyAct2,safetyAct3);
            setAct(params, result.getChemicalAct(), remChemicalAct, chemicalAct1,chemicalAct2,chemicalAct3);
            setAct(params, result.getDangerousAct(), remDangerousAct, dangerousAct1,dangerousAct2,dangerousAct3);
            
            params.put("msdsSynonym", result.getMsdsSynonym());

            params.put("safetyAct1", new JRBeanCollectionDataSource(safetyAct1));
            params.put("safetyAct2", new JRBeanCollectionDataSource(safetyAct2));
            params.put("safetyAct3", new JRBeanCollectionDataSource(safetyAct3));
            params.put("chemicalAct1", new JRBeanCollectionDataSource(chemicalAct1));
            params.put("chemicalAct2", new JRBeanCollectionDataSource(chemicalAct2));
            params.put("chemicalAct3", new JRBeanCollectionDataSource(chemicalAct3));
            params.put("dangerousAct1", new JRBeanCollectionDataSource(dangerousAct1));
            params.put("dangerousAct2", new JRBeanCollectionDataSource(dangerousAct2));
            params.put("dangerousAct3", new JRBeanCollectionDataSource(dangerousAct3));

            if(result.getFileId() == null) {
            	params.put("msdsImg", null);
            } else {
            	InputStream msdsImg = nasfileService.getNasFileInputStream(result.getFileId());
            	params.put("msdsImg", msdsImg);
            	
            }

            
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
    
	private StringBuilder urlStart(String operation) throws Exception {
		return new StringBuilder(MSDS_URL + operation)
				.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + MSDS_KEY_ENCODING);
	}
	
	private void setAct(Map<String, Object> params, List<MsdsLawVO> result, int remAct, List<Map<String,String>> act1, List<Map<String,String>> act2, List<Map<String,String>> act3) {
		int idx = 1;
		Map<String,String> emptyData = new HashMap<String,String>();
		emptyData.put("data", "");
		if(result.size() == 0) {
			act1.add(emptyData);
			act2.add(emptyData);
			act3.add(emptyData);
		} else {			
			switch(remAct) {
			case 0: 
				for(MsdsLawVO mlvo :result) {
					Map<String,String> data = new HashMap<String,String>();
					data.put("data", mlvo.getItemDetail());
					switch(idx % 3) {
					case 1: 
						act1.add(data);
						break;
					case 2:
						act2.add(data);
						break;
					case 0:
						act3.add(data);
						break;
					}
					idx++;
				}
				idx = 1;
				break;
			case 1: 
				for(MsdsLawVO mlvo :result) {
					Map<String,String> data = new HashMap<String,String>();
					data.put("data", mlvo.getItemDetail());
					switch(idx % 3) {
					case 1: 
						act1.add(data);
						break;
					case 2:
						act2.add(data);
						break;
					case 0:
						act3.add(data);
						break;
					}
					idx++;
				}
				act2.add(emptyData);
				act3.add(emptyData);
				idx = 1;
				break;
			case 2: 
				for(MsdsLawVO mlvo :result) {
					Map<String,String> data = new HashMap<String,String>();
					data.put("data", mlvo.getItemDetail());
					switch(idx % 3) {
					case 1: 
						act1.add(data);
						break;
					case 2:
						act2.add(data);
						break;
					case 0:
						act3.add(data);
						break;
					}
					idx++;
				}
				act3.add(emptyData);
				idx = 1;
				break;
			}
		}
	}
	
	


}
