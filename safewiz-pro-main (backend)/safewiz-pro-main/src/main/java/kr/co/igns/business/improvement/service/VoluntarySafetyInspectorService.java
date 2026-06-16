package kr.co.igns.business.improvement.service;

import kr.co.igns.business.improvement.dao.postgres.VoluntarySafetyInspectorDAO;
import kr.co.igns.business.improvement.model.VoluntarySafetyInspectorVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoluntarySafetyInspectorService {

    private final VoluntarySafetyInspectorDAO voluntarySafetyInspectorDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<VoluntarySafetyInspectorVO> getVoluntarySafetyInspectorList(SpSearchVO vo) throws Exception {
        return voluntarySafetyInspectorDAO.getVoluntarySafetyInspectorList(vo);
    }

    public void saveVoluntarySafetyInspector(List<VoluntarySafetyInspectorVO> voList) throws Exception {
        for (VoluntarySafetyInspectorVO vo : voList) {
            vo.setDocType("VSI");

            // DocNo가 'TEMP'인 것은 새로 추가된 ROW
            if (vo.getDocNo().equals("TEMP")) {
                vo.setCompId(SecurityUtil.getCurrentCompId());
                voluntarySafetyInspectorDAO.insertVoluntarySafetyInspector(vo);
            }else {
                voluntarySafetyInspectorDAO.updateVoluntarySafetyInspector(vo);
            }
        }
    }

    public void deleteVoluntarySafetyInspector(List<VoluntarySafetyInspectorVO> voList) throws Exception {
        for (VoluntarySafetyInspectorVO vo : voList) {
            voluntarySafetyInspectorDAO.deleteVoluntarySafetyInspector(vo);
        }
    }

    public void getVoluntarySafetyInspectorReport(
            HttpServletRequest request, HttpServletResponse response, SpSearchVO searchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        /*
         * 인트로 페이지
         */
        ReportVO introReportVO = new ReportVO();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "자체검사원 등록부";
        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", fileNm);
        introParams.put("subTitle", "사업장명: " + compNm);
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
        introParams.put("page", 1);
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());

        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);

        // 데이터 셋 페이지
        ReportVO dataReportVO = new ReportVO();
        dataReportVO.setFileName(fileNm);
        dataReportVO.setJrxmlPath("report/improvement/VoluntarySafetyInspector/voluntarySafetyInspector.jrxml");
        dataReportVO.setType("pdf");

        // 파라미터 세팅
        Map<String, Object> params = new HashMap<>();
        params.put("title", "자체검사원 등록부");
        params.put("subTitle", "사업장명: " + compNm);

        // 고객사 로고
        InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", clntLogo);

        // 데이터
        List<Map<String, Object>> dataList = new ArrayList<>();
        int index = 1;
        for (VoluntarySafetyInspectorVO vo : voluntarySafetyInspectorDAO.getVoluntarySafetyInspectorList(searchVO)) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("no", index);
            data.put("orgnNm", vo.getOrgnNm());
            data.put("jbrp", vo.getJbrp());
            data.put("hrNm", vo.getHrNm());
            data.put("workingAt", vo.getWorkingAt() != null && !vo.getWorkingAt().isEmpty() ? DateUtils.formatDate(vo.getWorkingAt()) : "");
            if (vo.getHasCertifiCate() == YesNo.Y) {
                data.put("hasCertificate", "보유");
            }else {
                data.put("hasCertificate", "미보유");
            }
            dataList.add(data);
            index++;
        }
        params.put("dataList", new JRBeanCollectionDataSource(dataList));
        params.put("page", JasperIntroReport.getPages().size() + 1);
        params.put("subPage", searchVO.getSubPage());
        params.put("localPage", searchVO.getLocalPage());

        dataReportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(dataReportVO);
        jasperPrintList.add(JasperReport);

        reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);
    }
    
    
    public void getVoluntarySafetyInspectorReportApichk(
            HttpServletRequest request, HttpServletResponse response, SpSearchVO searchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        /*
         * 인트로 페이지
         */
        ReportVO introReportVO = new ReportVO();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "자체검사원 등록부";
        introReportVO.setFileName(fileNm);
        introReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        introReportVO.setType("pdf");
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", fileNm);
        introParams.put("subTitle", "사업장명: " + compNm);
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
        introParams.put("page", 1);
        introParams.put("subPage", searchVO.getSubPage());
        introParams.put("localPage", searchVO.getLocalPage());

        introReportVO.setParameter(introParams);
        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);

        // 데이터 셋 페이지
        ReportVO dataReportVO = new ReportVO();
        dataReportVO.setFileName(fileNm);
        dataReportVO.setJrxmlPath("report/improvement/VoluntarySafetyInspector/voluntarySafetyInspector.jrxml");
        dataReportVO.setType("pdf");

        // 파라미터 세팅
        Map<String, Object> params = new HashMap<>();
        params.put("title", "자체검사원 등록부");
        params.put("subTitle", "사업장명: " + compNm);

        // 고객사 로고
        InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", clntLogo);

        // 데이터
        List<Map<String, Object>> dataList = new ArrayList<>();
        int index = 1;
        for(SpSearchVO voList : searchVO.getCheckedObjList())
        {
        	for (VoluntarySafetyInspectorVO vo : voluntarySafetyInspectorDAO.getVoluntarySafetyInspectorListchk(voList)) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("no", index);
                data.put("orgnNm", vo.getOrgnNm());
                data.put("jbrp", vo.getJbrp());
                data.put("hrNm", vo.getHrNm());
                data.put("workingAt", vo.getWorkingAt() != null && !vo.getWorkingAt().isEmpty() ? DateUtils.formatDate(vo.getWorkingAt()) : "");
                if (vo.getHasCertifiCate() == YesNo.Y) {
                    data.put("hasCertificate", "보유");
                }else {
                    data.put("hasCertificate", "미보유");
                }
                dataList.add(data);
                index++;
            }
        }
        
        params.put("dataList", new JRBeanCollectionDataSource(dataList));
        params.put("page", JasperIntroReport.getPages().size() + 1);
        params.put("subPage", searchVO.getSubPage());
        params.put("localPage", searchVO.getLocalPage());

        dataReportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(dataReportVO);
        jasperPrintList.add(JasperReport);

        reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);
    }
}
