package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.business.planning.dao.postgres.LegalManageDAO;
import kr.co.igns.business.planning.dao.postgres.LegalReviewDAO;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.business.planning.model.LegalReviewDetailVO;
import kr.co.igns.business.planning.model.LegalReviewVO;
import kr.co.igns.business.planning.model.LegalReviewDetailOrgnVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.system.common.model.SpSearchVO;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class LegalReviewService {
    private final LegalReviewDAO legalReviewDao;
    private final FileService fileService;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final ReportService reportService;
    private final UtilsService utilsService;


    public List<LegalReviewVO> getLegalReviewList(SpSearchVO vo) throws Exception {
        List<LegalReviewVO> voList = legalReviewDao.getLegalReviewList(vo);
        return voList;
    }

    public List<LegalReviewVO> getLegalReviewDetailMasterList(SpSearchVO vo) throws Exception {
        List<LegalReviewVO> voList = legalReviewDao.getLegalReviewDetailMasterList(vo);
        return voList;
    }

    public List<LegalReviewDetailVO> getLegalReviewDetailList(SpSearchVO searchVo) throws Exception {
        List<LegalReviewDetailVO> vo = legalReviewDao.getLegalReviewDetailList(searchVo);

        for (LegalReviewDetailVO lrdvo : vo) {
            lrdvo.setLegalReviewOrgnList(legalReviewDao.getLegalReviewDetailOrgnList(lrdvo));
        }
        return vo;
    }

    public List<LegalReviewDetailOrgnVO> getLegalReviewDetailOrgnList(LegalReviewDetailVO vo) throws Exception {
        List<LegalReviewDetailOrgnVO> voList = legalReviewDao.getLegalReviewDetailOrgnList(vo);
        return voList;
    }


    public String getDocNo(LegalReviewVO searchVo) throws Exception {
        String vo = legalReviewDao.getDocNo(searchVo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public LegalReviewVO saveLegalReview(LegalReviewVO legalVo) throws Exception {

        if (legalVo.getCmd().equals("I")) {
            legalReviewDao.insertLegalReview(legalVo);
        } else {
            legalReviewDao.updateLegalReview(legalVo);
        }
        return legalVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<Map<String, Object>> saveLegalReviewDetail(List<Map<String, Object>> legalVo, LegalReviewVO vo) throws Exception {

        for (Map<String, Object> item : legalVo) {
            // 조직
            List<Map<String, Object>> legalReviewOrgnList = Optional.ofNullable((List<Map<String, Object>>) item.get("legalReviewOrgnList")).orElse(Collections.emptyList());

            if (item.get("cmd").equals("I")) {
                item.put("docNo", vo.getDocNo());
                item.put("writeYear", vo.getWriteYear());
                item.put("createdBy", vo.getCreatedBy());
                item.put("updatedBy", vo.getUpdatedBy());
                legalReviewDao.insertLegalReviewDetail(item);

                vo.setDocSeq((String) item.get("docSeq"));
                saveLegalReviewDetailOrgn(legalReviewOrgnList, vo);

            } else {
                item.put("updatedBy", vo.getUpdatedBy());
                legalReviewDao.updateLegalReviewDetail(item);

                vo.setDocSeq((String) item.get("docSeq"));
                saveLegalReviewDetailOrgn(legalReviewOrgnList, vo);
            }
        }

        return legalVo;
    }

    // 법규 검토서 조직 저장
    @Transactional(rollbackFor = Throwable.class)
    public void saveLegalReviewDetailOrgn(List<Map<String, Object>> legalReviewOrgnList, LegalReviewVO vo) throws Exception {

        LegalReviewDetailOrgnVO orgnVO = new LegalReviewDetailOrgnVO();
        orgnVO.setDocType(vo.getDocType());
        orgnVO.setDocNo(vo.getDocNo());
        orgnVO.setDocSeq(vo.getDocSeq());
        orgnVO.setWriteYear(vo.getWriteYear());
        orgnVO.setCreatedBy(vo.getCreatedBy());

        if(vo.getCmd().equals("U")){
            orgnVO.setUpdatedBy(vo.getUpdatedBy());
            legalReviewDao.deleteLegalReviewDetailOrgn(orgnVO);
        }

        // 조직 데이터가 추가 된 경우
        if (!legalReviewOrgnList.isEmpty()) {
            for (Map<String, Object> legalReviewOrgnMap : legalReviewOrgnList) {
                orgnVO.setOrgnId((String) legalReviewOrgnMap.get("id"));
                orgnVO.setOrgnNm((String) legalReviewOrgnMap.get("nm"));
                orgnVO.setDocSeq(vo.getDocSeq());
                legalReviewDao.insertLegalReviewDetailOrgn(orgnVO);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalReview(List<LegalReviewVO> list) throws Exception {
        for (LegalReviewVO tmp : list) {
            tmp.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            legalReviewDao.deleteLegalReview(tmp);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalReviewDetail(List<LegalReviewDetailVO> list) throws Exception {
        for (LegalReviewDetailVO tmp : list) {
            tmp.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            legalReviewDao.deleteLegalReviewDetail(tmp);
        }
    }

    final JRPropertiesUtil jrProps = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());

    public void getLegalReviewReport(HttpServletRequest request, HttpServletResponse response, List<LegalReviewDetailVO> dataVo) throws Exception {
        jrProps.setProperty(JRFont.DEFAULT_PDF_FONT_NAME, "맑은 고딕");
        jrProps.setProperty(JRFont.DEFAULT_PDF_ENCODING, "Identity-H");
        jrProps.setProperty(JRFont.DEFAULT_PDF_EMBEDDED, "TRUE");
        jrProps.setProperty(JRFont.DEFAULT_FONT_NAME, "맑은 고딕");

        // master Data
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        SpSearchVO searchVO = new SpSearchVO();

        LegalReviewVO legalVo = dataVo.get(0);
        List<String> valueList = new ArrayList<String>();
        for (LegalReviewDetailVO item : dataVo) {
            valueList.add(item.getDocSeq());
        }
        legalVo.setGridValue(valueList);
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

        params.put("page", legalVo.getPage());
        params.put("subPage", legalVo.getSubPage());
        params.put("localPage", legalVo.getLocalPage());

        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(legalVo.getClntId());
        params.put("logo", logo);

        /*************************************/
        /****** Jasper Export File Setup *****/
        /*************************************/
        ReportVO introReportVO = new ReportVO();

        //출력물 용 데이터 조회
        LegalReviewVO reportData = legalReviewDao.getReportData(legalVo);

        String fileNm = "";
        fileNm = "(" + legalVo.getWriteYear() + ")법규 검토서" + "_";
        fileNm += legalVo.getLegalReviewNm();

        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront_reverse.jrxml");
        introReportVO.setType("pdf");

        Map<String, Object> paramsTitle = new HashMap<>();
        paramsTitle.put("clntNm", clntNm);
        paramsTitle.put("compNm", compNm);
        paramsTitle.put("title", "법규 검토서");
        paramsTitle.put("subTitle", "사업장명: " + compNm);
        paramsTitle.put("page", searchVO.getPage());
        paramsTitle.put("subPage", searchVO.getSubPage());
        paramsTitle.put("localPage", searchVO.getLocalPage());
        InputStream logoTitle = utilsService.getClntLogo(legalVo.getClntId());
        paramsTitle.put("logo", logoTitle);

        introReportVO.setParameter(paramsTitle);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);

        /*************************************/
        /****** 출력물 Custom Parameter 입력 *****/
        /*************************************/
        ReportVO reportVO = new ReportVO();
        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/planning/legalManage/LegalReview_Data.jrxml");
        reportVO.setType(dataVo.get(0).getType());

        params.put("year", legalVo.getWriteYear());

        params.put("contentHeader", "법규 검토서");

        //문서번호 입력
        params.put("docNo", legalVo.getWriteYear() + "-" + legalVo.getDocType() + "-" + legalVo.getDocNo());

        UtilsVO signParam = new UtilsVO();
        signParam.setDocType("LGR");
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, legalVo.getWriteYear() + legalVo.getDocNo());
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        //제·개정일시 입력
        params.put("createdAt", reportData.getCreatedAt());

        //gridData 조회
        List<LegalReviewDetailVO> resultList = legalReviewDao.getReportGridData(legalVo);

        if (resultList.isEmpty()) {
            resultList = new ArrayList<>(); // 빈 리스트 할당
            LegalReviewDetailVO emptyVo = new LegalReviewDetailVO();
            resultList.add(emptyVo);
        }else{
            for(LegalReviewDetailVO result : resultList){
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

        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        jasperPrintList.add(JasperReport);

        reportService.exportReportAll(request, response, jasperPrintList, dataVo.get(0).getType(), fileNm);
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(legalReviewDao.getAllReport(spSearchVO));
        spSearchVO.setPrintAll(true);
        spSearchVO.setPage(page);
        spSearchVO.setSubPage(subPage);
        spSearchVO.setLocalPage(localPage);
        jaspers = getLegalReviewReportAll(request, response, spSearchVO);
        reportList.addAll(jaspers);
        spSearchVO.setSubPage(spSearchVO.getSubPage());
        for (JasperPrint report : jaspers) {
            page += report.getPages().size();
            localPage += report.getPages().size();
        }
        return reportList;
    }

    public List<JasperPrint> getLegalReviewReportAll(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {

        jrProps.setProperty(JRFont.DEFAULT_PDF_FONT_NAME, "맑은 고딕");
        jrProps.setProperty(JRFont.DEFAULT_PDF_ENCODING, "Identity-H");
        jrProps.setProperty(JRFont.DEFAULT_PDF_EMBEDDED, "TRUE");
        jrProps.setProperty(JRFont.DEFAULT_FONT_NAME, "맑은 고딕");

        List<JasperPrint> jasperPrintList = new ArrayList<>();


        /**********************************/
        /****** 출력물 공통 Parameter 입력 *****/
        /**********************************/

        Set<String> yearSet = new HashSet<>();
        String fileNm = "";
        fileNm = "법규 검토서";


        /*************************************/
        /****** Jasper Export File Setup *****/
        /*************************************/

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, "법규 검토서");
        jasperPrintList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();


        /*************************************/
        /****** 출력물 Custom Parameter 입력 *****/
        /*************************************/
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            Map<String, Object> params = new HashMap<String, Object>();
            InputStream logoTitle = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logoTitle);
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/planning/legalManage/LegalReview_Data.jrxml");
            reportVO.setType("pdf");

            params.put("year", vo.getWriteYear());

            params.put("contentHeader", "법규 검토서");

            //문서번호 입력
            params.put("docNo", vo.getWriteYear() + "-" + vo.getDocType() + "-" + vo.getDocNo());
            params.put("contentTitle", "법규 검토서");

            UtilsVO signParam = new UtilsVO();
            signParam.setDocType("LGR");
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, vo.getWriteYear() + vo.getDocNo());
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            //제·개정일시 입력
            List<LegalReviewDetailVO> resultList = legalReviewDao.getReportGridDataAll(vo);
            if (!yearSet.contains(resultList.get(0).getWriteYear())) {  // 중복이 아닐 경우에만 추가
                yearSet.add(resultList.get(0).getWriteYear());
            }
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + resultList.get(0).getLegalReviewNm();
            }

            if (resultList.size() == 0) {
                resultList = new ArrayList<>();
                LegalReviewDetailVO emptyDetail = new LegalReviewDetailVO();
                resultList.add(emptyDetail);
            }else{
                for(LegalReviewDetailVO result : resultList){
                    result.setRevisionAt(DateUtils.formatDate(result.getRevisionAt()));
                }
            }
            params.put("gridData", new JRBeanCollectionDataSource(resultList));
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);

            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();

        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String year : yearSet) {
            joiner.add(year);
        }
        fileNm = "(" + joiner.toString() + ")" + fileNm;
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return jasperPrintList;
        reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return jasperPrintList;
    }

}
