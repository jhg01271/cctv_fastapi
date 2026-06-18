package kr.co.igns.business.planning.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.framework.config.exception.CUserDefinedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.netty.handler.codec.http.HttpRequest;
import kr.co.igns.business.planning.dao.postgres.LegalManageDAO;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.business.planning.model.LegalSearchVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import kr.co.igns.system.setting.model.CompVO;

import kr.co.igns.framework.report.model.ReportVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:global.properties")
public class LegalManageService {
    private final FileService fileService;
    private final LegalManageDAO legalManageDao;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final ReportService reportService;
    private final UtilsService utilsService;

    @Value("${legal.apiUrl}")
    private String LEGAL_URL;

    @Value("${legal.apiKey.encoding}")
    private String LEGAL_KEY_ENCODING;

    private StringBuilder urlStart() throws Exception {
        return new StringBuilder(LEGAL_URL + "/smartSearch")
                .append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8))
                .append("=").append(LEGAL_KEY_ENCODING);
    }
    
    public List<LegalManageVO> getLegalManageList(LegalManageVO vo) throws Exception {
        List<LegalManageVO> voList = legalManageDao.getLegalManageList(vo);
        return voList;
    }

    public List<LegalManageVO> getLegalManageTypeList(LegalManageVO vo) throws Exception {
        List<LegalManageVO> voList = legalManageDao.getLegalManageTypeList(vo);
        return voList;
    }

    // 법규 관련 예시 데이터 조회
    public List<LegalManageVO> getDatasetLegalManageType(SpSearchVO vo) {
        List<LegalManageVO> voList = new ArrayList<>();
        voList = legalManageDao.getDatasetLegalManageType(vo);
        return voList;
    }

    // 공통 연결 및 응답 데이터 반환
    private String getLegalListData(SpSearchVO vo, String numOfRows) throws Exception {
        try {
            StringBuilder urlBuilder = urlStart()
                    .append("&" + URLEncoder.encode("searchValue", "UTF-8") + "=" + URLEncoder.encode(vo.getSearchText(), "UTF-8"))
                    .append("&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(vo.getSearchCd(), "UTF-8"))
                    .append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"))
                    .append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(vo.getSearchCd3(), "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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

            return sb.toString();

        } catch (Exception e) {
            throw new CUserDefinedException("공공데이터포털 서버와 일시적인 연결 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요.", e);
        }
    }

    // 해당 카테고리의 totalCount 계산
    public Integer getLegalListTotalCount(SpSearchVO vo) throws Exception {
        String responseData = getLegalListData(vo, vo.getSearchCd2());
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseData);
            return rootNode.path("response").path("body").path("totalCount").asInt(0);

        } catch (Exception e) {
            throw new CUserDefinedException("공공데이터포털 서버로부터 받은 데이터에 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요.", e);
        }
    }
    
    // totalCount를 통해 전체 데이터 출력
    public LegalSearchVO getLegalList(SpSearchVO vo) throws Exception {
        Integer totalCountInt = getLegalListTotalCount(vo);

        // totalCount가 0이면 빈 결과 반환
        if (totalCountInt == 0) {
            LegalSearchVO emptyResult = new LegalSearchVO();
            LegalSearchVO.ItemsWrapper wrapper = new LegalSearchVO.ItemsWrapper();
            wrapper.setItem(new ArrayList<>());
            emptyResult.setItems(wrapper);
            emptyResult.setTotalCount(0);
            emptyResult.setNumOfRows(0);
            emptyResult.setPageNo(1);
            return emptyResult;
        }

        String totalCount = totalCountInt.toString();

        // 응답 받음
        String responseData = getLegalListData(vo, totalCount);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JsonNode rootNode = mapper.readTree(responseData);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<LegalSearchVO.Item> filteredItems = new ArrayList<>();

            // 날짜 정규화
            Pattern pattern = Pattern.compile("(\\d{4})[.](\\d{1,2})[.](\\d{1,2})\\s*>");
            LocalDate latestDt = null;

            if (itemsNode.isArray()) {
                for (JsonNode node : itemsNode) {

                    // title에 별표, 서식 이라는 텍스트가 있는 경우 넘김
                    String title = node.path("title").asText("");
                    if (title.contains("별표") || title.contains("서식")) continue;

                    String content = node.path("content").asText("");
                    if (content.contains(">")) {
                        Matcher matcher = pattern.matcher(content);
                        while (matcher.find()) {
                            int year = Integer.parseInt(matcher.group(1));
                            int month = Integer.parseInt(matcher.group(2));
                            int day = Integer.parseInt(matcher.group(3));
                            LocalDate date = LocalDate.of(year, month, day);

                            if (latestDt == null || date.isAfter(latestDt)) {
                                latestDt = date;
                            }
                        }
                    }

                    LegalSearchVO.Item item = mapper.treeToValue(node, LegalSearchVO.Item.class);
                    filteredItems.add(item);
                }
            }

            LegalSearchVO voList = new LegalSearchVO();
            LegalSearchVO.ItemsWrapper wrapper = new LegalSearchVO.ItemsWrapper();

            // 필터링한 데이터 추가
            wrapper.setItem(filteredItems);
            voList.setItems(wrapper);

            // 기타
            voList.setNumOfRows(rootNode.path("response").path("body").path("numOfRows").asInt(0));
            voList.setPageNo(rootNode.path("response").path("body").path("pageNo").asInt(0));
            voList.setTotalCount(rootNode.path("response").path("body").path("totalCount").asInt(0));

            // 날짜
            if (latestDt != null) {
                voList.setLatestDt(DateUtils.formatDate(latestDt.toString()));
            }

            return voList;

        } catch (Exception e) {
            throw new CUserDefinedException("공공데이터포털 서버로 부터 받은 데이터에 오류가 발생하였습니다.\n잠시 후 다시 시도해주세요", e);
        }
    }

    public List<LegalManageVO> getLegalManageListPopup(LegalManageVO vo) throws Exception {
        List<LegalManageVO> voList = legalManageDao.getLegalManageListPopup(vo);
        return voList;
    }
    
    public List<LegalManageVO> getLegalManageDetailMasterList(LegalManageVO vo) throws Exception {
        List<LegalManageVO> voList = legalManageDao.getLegalManageDetailMasterList(vo);
        return voList;
    }

    public List<LegalManageDetailVO> getLegalManageDetail(SpSearchVO searchVo) throws Exception {
        List<LegalManageDetailVO> vo = legalManageDao.getLegalManageDetail(searchVo);
        return vo;
    }

    public String getDocNo(LegalManageVO searchVo) throws Exception {
        String vo = legalManageDao.getDocNo(searchVo);
        return vo;
    }

    public Integer getLegalNm(LegalManageVO searchVo) throws Exception {
        Integer vo = legalManageDao.getLegalNm(searchVo);
        return vo;
    }
    
    public List<LegalManageVO> getValidLegalDivFg(LegalManageVO searchVo) throws Exception {
        List<LegalManageVO> voList = new ArrayList<>();
        voList = legalManageDao.getValidLegalDivFg(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public LegalManageVO saveLegalManage(LegalManageVO legalVo) throws Exception {

        if (legalVo.getCmd().equals("I")) {
            legalManageDao.insertLegalManage(legalVo);
        } else {
            legalManageDao.updateLegalManage(legalVo);
        }

        return legalVo;
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO confirmLegalManage(LegalManageVO reqVo) throws Exception {
    	legalManageDao.confirmLegalManage(reqVo);
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<Map<String, Object>> saveLegalManageDetail(List<Map<String, Object>> legalVo, LegalManageVO vo) throws Exception { //, List<LegalManageDetailVO> legalDetailVo

        for (Map<String, Object> item : legalVo) {
            item.put("docNo", vo.getDocNo());
            item.put("writeYear", vo.getWriteYear());
            if (item.get("cmd").equals("I")) {
                item.put("createdBy", vo.getCreatedBy());
                legalManageDao.insertLegalManageDetail(item);
            } else {
                item.put("updatedBy", vo.getUpdatedBy());
                legalManageDao.updateLegalManageDetail(item);
            }
        }

        return legalVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveLegalManageType(List<LegalManageVO> voList) {
        for(LegalManageVO vo : voList) {
        	if(vo.getOrdSeq() == null) {
        		vo.setOrdSeq(99);
        	}
        	
            if(vo.getCmd().equals("U")) {
            	legalManageDao.updateLegalManageType(vo);
            } else {
            	legalManageDao.insertLegalManageType(vo);
            }
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalManage(List<LegalManageVO> list) throws Exception {
        for (LegalManageVO tmp : list) {
            tmp.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            legalManageDao.deleteLegalManage(tmp);
        }
    }

    // 법규 항목 관리 데이터 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalManageType(List<LegalManageVO> voList) {
        for(LegalManageVO vo : voList) {
        	legalManageDao.deleteLegalManageType(vo);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalManageDetail(List<LegalManageDetailVO> list) throws Exception {
        for (LegalManageDetailVO tmp : list) {
            tmp.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            legalManageDao.deleteLegalManageDetail(tmp);
        }
    }


    final JRPropertiesUtil jrProps = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());

    public void getLegalManageReport(HttpServletRequest request, HttpServletResponse response, List<Object> dataVo) throws Exception {

        jrProps.setProperty(JRFont.DEFAULT_PDF_FONT_NAME, "맑은 고딕");
        jrProps.setProperty(JRFont.DEFAULT_PDF_ENCODING, "Identity-H");
        jrProps.setProperty(JRFont.DEFAULT_PDF_EMBEDDED, "TRUE");
        jrProps.setProperty(JRFont.DEFAULT_FONT_NAME, "맑은 고딕");


        // master Data
        Map<String, Object> masterDataVo = new HashMap<>();
        LegalManageVO masterLegalVo = new LegalManageVO();

        masterDataVo = (Map<String, Object>) dataVo.get(0);
        masterLegalVo.setWriteYear((String) masterDataVo.get("writeYear"));
        masterLegalVo.setDocType((String) masterDataVo.get("docType"));
        masterLegalVo.setDocNo((String) masterDataVo.get("docNo"));
        masterLegalVo.setCompId((String) masterDataVo.get("compId"));
        masterLegalVo.setLegalNm((String) masterDataVo.get("legalNm"));
        masterLegalVo.setUseYn((String) masterDataVo.get("useYn"));
        masterLegalVo.setDivFg((String) masterDataVo.get("divFg"));
        masterLegalVo.setRevisionAt((String) masterDataVo.get("revisionAt"));
        masterLegalVo.setCreatedAt((String) masterDataVo.get("createdAt"));
        masterLegalVo.setUpdatedAt((String) masterDataVo.get("updateAt"));

        masterLegalVo.setCreatedBy((String) masterDataVo.get("createdBy"));
        masterLegalVo.setUpdatedBy((String) masterDataVo.get("updatedBy"));

        masterLegalVo.setCmd((String) masterDataVo.get("cmd"));

        List<Map<String, Object>> detailDataListVo = new ArrayList<>();
        detailDataListVo = (List<Map<String, Object>>) dataVo.get(1);

        System.out.print("333333333");
        System.out.print(detailDataListVo.get(0).get("checkYn"));

        String gridValue = "";
        for (Map<String, Object> item : detailDataListVo) {
            gridValue = gridValue + item.get("docSeq") + ",";
        }

        gridValue = gridValue.substring(0, gridValue.length() - 1);

        String[] values = gridValue.split(",");
        List<String> valueList = Arrays.asList(values);

        masterLegalVo.setGridValue((List<String>) valueList);
        masterLegalVo.setCheckYn((String) detailDataListVo.get(0).get("checkYn"));

        LegalManageVO legalVo = (LegalManageVO) masterLegalVo;

        /***********************/
        /****** 공통 정보 조회 ******/
        /***********************/

        // 고객사 ID 조회
        legalVo.setClntId(SecurityUtil.getCurrentClntId());

        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(legalVo.getClntId());
        String clntNm = cVo.getClntNm();

        // 사업장 ID 조회
        legalVo.setCompId(SecurityUtil.getCurrentCompId());

        // 사업장 명칭 조회
        CompVO cpVO = new CompVO();
        cpVO.setClntId(legalVo.getClntId());
        cpVO.setCompId(legalVo.getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();

        /**********************************/
        /****** 출력물 공통 Parameter 입력 *****/
        /**********************************/

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("contentHeader", "법규등록부 / 준수평가표");
        params.put("page", legalVo.getPage());
        params.put("subPage", legalVo.getSubPage());
        params.put("localPage", legalVo.getLocalPage());

        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(legalVo.getClntId());
        params.put("logo", logo);

        /*************************************/
        /****** 출력물 Custom Parameter 입력 *****/
        /*************************************/

        params.put("year", legalVo.getWriteYear());

        //출력물 용 데이터 조회
        LegalManageVO reportData = legalManageDao.getReportData(legalVo);


        //문서번호 입력
        params.put("docNo", reportData.getWriteYear() + '-' + reportData.getDocType() + '-' + reportData.getDocNo());

        //제·개정일시 입력
        params.put("revisionAt", DateUtils.formatDate(reportData.getRevisionAt()));


        //gridData 조회
        List<LegalManageDetailVO> resultList = legalManageDao.getReportGridData(legalVo);

        if (resultList.size() == 0) {
            resultList.add(new LegalManageDetailVO());
        }else{
            for(LegalManageDetailVO result : resultList){
                result.setRevisionAt(DateUtils.formatDate(result.getRevisionAt()));
            }
        }

        //gridData 입력
        params.put("gridData", new JRBeanCollectionDataSource(resultList));

        int pageCnt = 0;

        if (resultList.size() % 20 == 0) {
            pageCnt = resultList.size() / 20;
        } else {
            pageCnt = (resultList.size() / 20) + 1;
        }

        params.put("pageCnt", pageCnt);

        // 서명 데이터 조회
        UtilsVO signParam = new UtilsVO();
        signParam.setDocType(legalVo.getDocType());
        signParam.setWriteYear(legalVo.getWriteYear());
        signParam.setDocNo(legalVo.getDocNo());

        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        /*************************************/
        /****** Jasper Export File Setup *****/
        /*************************************/


        Map<String, Object> paramsTitle = new HashMap<String, Object>();
        paramsTitle.put("clntNm", clntNm);
        paramsTitle.put("compNm", compNm);
        paramsTitle.put("subTitle", "사업장명: " + compNm);
        paramsTitle.put("contentHeader", "법규등록부 / 준수평가표");
        paramsTitle.put("page", legalVo.getPage());
        paramsTitle.put("subPage", legalVo.getSubPage());
        paramsTitle.put("localPage", legalVo.getLocalPage());
        InputStream logoTitle = utilsService.getClntLogo(legalVo.getClntId());
        paramsTitle.put("logo", logoTitle);

        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        JRDataSource dataSourceTitle = new JREmptyDataSource();

        JRDataSource dataSource = new JREmptyDataSource();

        String fileNm = "";
        fileNm = "(" + legalVo.getWriteYear() + ")법규등록부_준수평가표_";
        if ("pdf".equals(masterDataVo.get("type"))) {
            fileNm += reportData.getLegalNm() + ".pdf";
        } else if ("xls".equals(masterDataVo.get("type"))) {
            fileNm += reportData.getLegalNm() + ".xls";
        }

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream titleReportStream = classLoader.getResourceAsStream("report/planning/legalManage/LegalManage_Report_Title.jrxml");
        InputStream dataReportStream = classLoader.getResourceAsStream("report/planning/legalManage/LegalManage_Report_Data.jrxml");

        JasperReport jasperTitleReport = JasperCompileManager.compileReport(titleReportStream);
        JasperReport jasperDataReport = JasperCompileManager.compileReport(dataReportStream);

        JasperPrint jasperPrintTitle = JasperFillManager.fillReport(jasperTitleReport, paramsTitle, dataSourceTitle);
        JasperPrint jasperPrintData = JasperFillManager.fillReport(jasperDataReport, params, dataSource);

        reportList.add(jasperPrintTitle);
        reportList.add(jasperPrintData);
//	    for (JRPrintPage page : jasperPrintData.getPages()) {
//	    	jasperPrintTitle.addPage(page);
//	    }

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("report", "success");
        response.setHeader("fileName", Base64.getEncoder().encodeToString(fileNm.getBytes()));

        if ("pdf".equals(masterDataVo.get("type"))) {
            @SuppressWarnings("rawtypes")
            JRPdfExporter exporter = new JRPdfExporter();
            //exporter.setExporterInput(new SimpleExporterInput(jasperPrintTitle));
            exporter.setExporterInput(SimpleExporterInput.getInstance(reportList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimplePdfExporterConfiguration configurationPdf = new SimplePdfExporterConfiguration();
            configurationPdf.setCreatingBatchModeBookmarks(true);
            exporter.setConfiguration(configurationPdf);

            exporter.exportReport();
        } else {
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(reportList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            exporter.exportReport();
        }
    }


    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(legalManageDao.getAllReport(spSearchVO));
        spSearchVO.setPrintAll(true);
        spSearchVO.setPage(page);
        spSearchVO.setSubPage(subPage);
        spSearchVO.setLocalPage(localPage);
        jaspers = getLegalManageReportCard(request, response, spSearchVO);
        reportList.addAll(jaspers);
        spSearchVO.setSubPage(spSearchVO.getSubPage());
        for (JasperPrint report : jaspers) {
            page += report.getPages().size();
            localPage += report.getPages().size();
        }
        return reportList;
    }

    public List<JasperPrint> getLegalManageReportCard(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        Set<String> yearSet = new HashSet<>();
        String fileNm = "";
        fileNm = "법규등록부_준수평가표";
//	if(Vo.size() == 1) {
//		fileNm += "_" + Vo.get(0).getLegalNm();
//	}
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, "법규등록부 / 준수평가표");
        jasperPrintList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {

            Map<String, Object> params = new HashMap<String, Object>();
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            params.put("contentHeader", "법규등록부 / 준수평가표");

            UtilsVO signParam = new UtilsVO();
            signParam.setDocType(vo.getDocType());
            signParam.setWriteYear(vo.getWriteYear());
            signParam.setDocNo(vo.getDocNo());

            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            //출력물 용 데이터 조회
            LegalManageVO reportData = legalManageDao.getReportData1(vo);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + reportData.getLegalNm();
            }
            if (!yearSet.contains(reportData.getWriteYear())) {  // 중복이 아닐 경우에만 추가
                yearSet.add(reportData.getWriteYear());
            }

            //문서번호 입력
            params.put("docNo", reportData.getWriteYear() + '-' + reportData.getDocType() + '-' + reportData.getDocNo());

            //제·개정일시 입력
            params.put("revisionAt", DateUtils.formatDate(reportData.getRevisionAt()));

            //gridData 조회
            List<LegalManageDetailVO> resultList = legalManageDao.getReportGridDataAll(vo);

            for(LegalManageDetailVO result : resultList){
                result.setRevisionAt(DateUtils.formatDate(result.getRevisionAt()));
            }

            //gridData 입력
            params.put("gridData", new JRBeanCollectionDataSource(resultList));

            ReportVO legalReportVO = new ReportVO();
            legalReportVO.setFileName(fileNm);
            legalReportVO.setJrxmlPath("report/planning/legalManage/LegalManage_Report_Data.jrxml");
            legalReportVO.setType("pdf");
            legalReportVO.setParameter(params);

            JasperPrint JasperReport = reportService.allReportJasperPrint(legalReportVO);
            jasperPrintList.add(JasperReport);

            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();

        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String year : yearSet) {
            joiner.add(year);
        }
        if(spSearchVO.getCheckedObjList().size() == 1){
            fileNm = "(" + joiner.toString() + ")" + fileNm;
        }

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return jasperPrintList;
        reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return jasperPrintList;

    }

//	public ReportVO getLegalManageReport(HttpRequest request, HttpServletResponse response, SpSearchVO vo)
//			throws Exception {
//
//    	/***********************/
//		/****** 공통 정보 조회 ******/
//		/***********************/
//    	
//		System.out.println("llllllllll");
//		System.out.println(vo);
//		System.out.println(SecurityUtil.getCurrentClntId());
//    	// 고객사 ID 조회
//    	vo.setClntId(SecurityUtil.getCurrentClntId());
//    	System.out.println("2222222");
//    	// 고객사 명칭 조회
//		ClientVO cVo = clientDao.getClientById(vo.getClntId());
//		String clntNm = cVo.getClntNm();
//		System.out.println("33333333");
//		// 사업장 ID 조회
//		vo.setCompId(SecurityUtil.getCurrentCompId());
//		System.out.println("444444444");
//    	
//		// 사업장 명칭 조회
//		CompVO cpVO = new CompVO();
//		cpVO.setClntId(vo.getClntId());
//		cpVO.setCompId(vo.getCompId());
//		CompVO cPo = compDao.getCompById(cpVO);
//		String compNm = cPo.getCompNm();
//		
//		System.out.println("5555555555555");
//		/**********************************/
//		/****** 출력물 공통 Parameter 입력 *****/
//		/**********************************/
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//
////		//고객사 명 입력
////		params.put("clntNm", clntNm); 
////		//사업장 명 입력
////		params.put("compNm", compNm);
//		
//		params.put("page", vo.getPage());
//		params.put("subPage", vo.getSubPage());
//		params.put("localPage", vo.getLocalPage());
//
//		// Logo 파일 조회
//		InputStream logo = utilsService.getClntLogo(vo.getClntId());
//		params.put("logo", logo);
//		
//		
//		/*************************************/
//		/****** 출력물 Custom Parameter 입력 *****/
//		/*************************************/
//		
//		params.put("year",vo.getWriteYear());
//		
//		//출력물 용 데이터 조회
//		LegalManageVO reportData = legalManageDao.getReportData(vo);
//		
//		
//		//문서번호 입력
//		params.put("docNo", reportData.getDocNo());
//		
//		//제·개정일시 입력
//		params.put("revisionAt", reportData.getRevisionAt());
//				
//		//gridData 조회
//		List<LegalManageDetailVO> resultList = legalManageDao.getReportGridData(vo);
//		//gridData 입력
//		params.put("gridData", new JRBeanCollectionDataSource(resultList));
//		
//		// 서명 데이터 조회
//		UtilsVO signParam = new UtilsVO();
//		signParam.setTargetType(vo.getDocType());
//		signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
//		
//		List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회
//		for (UtilsVO signInfo : signList) {
//			// 서명 데이터 입력
//			if(signInfo.getParam().equals("writer")) {
//				params.put("signWriter", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//				params.put("signWriterNm", signInfo.getHrNm());
//			}
//			if(signInfo.getParam().equals("reviewer")) {
//				params.put("signReviewer", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//				params.put("signReviewerNm", signInfo.getHrNm());
//			}
//			if(signInfo.getParam().equals("approver")) {
//				params.put("signApprover", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//				params.put("signApproverNm", signInfo.getHrNm());
//			}
//		}
//		
//
//		/*************************************/
//		/****** Jasper Export File Setup *****/
//		/*************************************/
//				
//		ReportVO reportVO = new ReportVO();
//				
//    	// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
//    	String fileNm = "";
//		fileNm = compNm + "_" + reportData.getLegalNm() + "_";
//		fileNm += "법규등록부 및 준수평가표";
//		fileNm += "(" + DateUtils.getNowDate("YYYY.MM.dd") + ")";
//		
//		// 출력 파일 명 설정
//		reportVO.setFileName(fileNm);
//		
//		// 출력 생성용 Jasper 파일 위치
//		reportVO.setJrxmlPath("report/planning/LegalManage_Report.jrxml");
//
//		// 출력  파일 형식 지정
//		reportVO.setType("pdf");
//		
//		/**********************/
//		/****** 출력물 출력 *******/
//		/**********************/
//		
//		reportVO.setParameter(params);
//		
//		// 통합 출력인 경우 reportVO만 반환하고 종료
//		if(vo.isPrintAll()) return reportVO;
//		
//		reportService.exportReport(request, response, reportVO);
//		
//		return reportVO;
//	}

//    public ReportVO getLegalManageReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
//    	ReportVO reportVO = new ReportVO();
//		reportVO.setFileName("법규관리");
//		reportVO.setJrxmlPath("report/planning/LegalManage/LegalManage.jrxml");
//		reportVO.setType("pdf");
//		spSearchVO.setSearchText2(SecurityUtil.getCurrentCompId());
//		LegalManageVO result = legalManageDao.getLegalManageDetail(spSearchVO);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("contentHeader", result.getContentHeader());
//		params.put("contentBody", result.getContentBody());
//		params.put("contentFooter", result.getContentFooter());
//		// 고객사 로고
//		InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
//		params.put("logo", clntLogo);
//		// 서명
//		String ceoHrId = legalManageDao.getCeoHrId();
//		if(ceoHrId != null) {
//			InputStream signature = utilsService.getUserSignatureByHrId(SecurityUtil.getCurrentClntId(), ceoHrId);
//			params.put("signature", signature);
//		}
//
//		reportVO.setParameter(params);
//		if (!spSearchVO.isPrintAll()) {
//			reportService.exportReport(request, response, reportVO);
//		}
//		return reportVO;
//	}

    // 환경변수 변경 함수
    public String changeVariables(String content, String compNm) {

        Map<String, String> variables = new HashMap<>();

        LocalDate now = LocalDate.now();
        String year = Integer.toString(now.getYear());
        String month = Integer.toString(now.getMonthValue());
        String day = Integer.toString(now.getDayOfMonth());

        variables.put("year", year);
        variables.put("month", month);
        variables.put("day", day);
        variables.put("compNm", compNm);

        Pattern pattern = Pattern.compile("\\{\\{([^}]+)}}"); // 정규 표현식을 이용하여 {{}} 패턴을 찾아 변수로 바꿈
        Matcher matcher = pattern.matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement = variables.getOrDefault(variableName, ""); // 맵에서 변수 값을 가져옴
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
