package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.JobCompetencyAssessmentDAO;
import kr.co.igns.business.support.dao.postgres.SafetyAndHealthCommPlanDAO;
import kr.co.igns.business.support.model.*;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;
import java.util.TreeSet;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class SafetyAndHealthCommPlanService {
    private final SafetyAndHealthCommPlanDAO safetyAndHealthCommPlanDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;


    public List<SafetyAndHealthCommPlanVO> getShCommPlanList(SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthCommPlanVO> voList = safetyAndHealthCommPlanDAO.getShCommPlanList(searchVo);
        if (voList.size() > 0) {
            for (SafetyAndHealthCommPlanVO vo : voList) {
                vo.setContentType("0001");
                vo.setInnerList(safetyAndHealthCommPlanDAO.getShCommPlanDetail(vo));
                vo.setContentType("0002");
                vo.setExternalList(safetyAndHealthCommPlanDAO.getShCommPlanDetail(vo));
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveShCommPlan(SafetyAndHealthCommPlanVO vo) throws Exception {
        // 마스터 테이블 저장
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            safetyAndHealthCommPlanDAO.insertShCommPlan(vo);
        } else {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            safetyAndHealthCommPlanDAO.updateShCommPlan(vo);
        }
        // 디테일 테이블 저장
        if (vo.getInnerList().size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO inner : vo.getInnerList()) {
                if (inner.getCmd().equals("I")) {
                    inner.setCreatedBy(SecurityUtil.getCurrentMemberId());
                    inner.setWriteYear(vo.getWriteYear());
                    inner.setDocNo(vo.getDocNo());
                    safetyAndHealthCommPlanDAO.insertShCommPlanDetail(inner);
                } else {
                    inner.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                    inner.setWriteYear(vo.getWriteYear());
                    inner.setDocNo(vo.getDocNo());
                    safetyAndHealthCommPlanDAO.updateShCommPlanDetail(inner);
                }
            }
        }
        if (vo.getExternalList().size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO external : vo.getExternalList()) {
                if (external.getCmd().equals("I")) {
                    external.setCreatedBy(SecurityUtil.getCurrentMemberId());
                    external.setWriteYear(vo.getWriteYear());
                    external.setDocNo(vo.getDocNo());
                    safetyAndHealthCommPlanDAO.insertShCommPlanDetail(external);
                } else {
                    external.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                    external.setWriteYear(vo.getWriteYear());
                    external.setDocNo(vo.getDocNo());
                    safetyAndHealthCommPlanDAO.updateShCommPlanDetail(external);
                }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteShCommPlan(List<SafetyAndHealthCommPlanVO> voList) throws Exception {
        for (SafetyAndHealthCommPlanVO vo : voList) {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            safetyAndHealthCommPlanDAO.deleteShCommPlan(vo);
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteShCommPlanDetail(SafetyAndHealthCommPlanVO vo) throws Exception {
        if (vo.getInnerList().size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO inner : vo.getInnerList()) {
                inner.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                safetyAndHealthCommPlanDAO.deleteShCommPlanDetail(inner);
            }
        }

        if (vo.getExternalList().size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO external : vo.getExternalList()) {
                external.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                safetyAndHealthCommPlanDAO.deleteShCommPlanDetail(external);
            }
        }
        return vo;
    }

    public List<JasperPrint> getShCommPlanReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "";
        fileNm += "안전보건 의사소통 계획";
        String title = "안전보건 의사소통 계획";
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        ReportVO frontReportVO = new ReportVO();
        frontReportVO.setFileName(fileNm);
        frontReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        frontReportVO.setType(spSearchVO.getType());
        Map<String, Object> frontParam = new HashMap<String, Object>();
        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        frontParam.put("logo", logo);
        frontParam.put("title", title);
        frontParam.put("subTitle", "사업장명:" + compNm);
        frontParam.put("page", spSearchVO.getPage());
        frontParam.put("subPage", spSearchVO.getSubPage());
        frontParam.put("localPage", spSearchVO.getLocalPage());
        frontReportVO.setParameter(frontParam);
        JasperPrint JasperFrontReport = reportService.allReportJasperPrint(frontReportVO);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
//
        List<SafetyAndHealthCommPlanVO> resultList = getShCommPlanList(spSearchVO); // 데이터

        Set<String> yearSet = new TreeSet<>(); // 파일명 저장을 위한 년도 변수
        if (spSearchVO.getCheckedObjList().size() == 1) {
            fileNm += "_" + resultList.get(0).getTitle();
        }
        for (SafetyAndHealthCommPlanVO result : resultList) {
//            boolean isContained = spSearchVO.getCheckedList().contains(result.getDocNo());
            List<SpSearchVO> isContained = spSearchVO.getCheckedObjList().stream()
                    .filter(el -> el.getWriteYear().equals(result.getWriteYear()) && el.getDocNo().equals(result.getDocNo()))
                    .collect(Collectors.toList());
            if (isContained.size() > 0) {
                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/support/safetyAndHealthCommPlan/safetyAndHealthCommPlan.jrxml");
                reportVO.setType(spSearchVO.getType());
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("title", title);
                params.put("docTitle", result.getTitle());
                params.put("docNo", result.getWriteYear() + "-" + result.getDocType() + "-" + result.getDocNo());
                InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                params.put("logo", logo2);

                params.put("page", page);
                params.put("subPage", subPage);
                params.put("localPage", localPage);

                if (!yearSet.contains(result.getWriteYear())) {  // 중복이 아닐 경우에만 추가
                    yearSet.add(result.getWriteYear());
                }

                List<Map<String, Object>> innerList = new ArrayList<>(); // 내부 의사소통 데이터
                List<Map<String, Object>> externalList = new ArrayList<>(); // 외부 의사소통 데이터
                if (result.getInnerList().size() > 0) {
                    long count = result.getInnerList().stream()
                            .filter(vo -> YesNo.Y.equals(vo.getUseYn())) // useYn 값이 "Y"인 항목 필터링
                            .count(); // 필터링된 항목 개수 세기
                    if (count == 0) {
                        // Y인 항목이 아예없을 때
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("content", "");
                        innerList.add(data);
                    } else {
                        for (SafetyAndHealthCommPlanDetailVO inner : result.getInnerList()) {
                            if (inner.getUseYn() == YesNo.Y) {
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("content", inner.getContent());
                                data.put("period", inner.getPeriod());
                                data.put("subject", inner.getSubject());
                                data.put("method", inner.getMethod());
                                innerList.add(data);
                            }
                        }
                    }
                } else {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", "");
                    innerList.add(data);
                }
                if (result.getExternalList().size() > 0) {
                    long count = result.getExternalList().stream()
                            .filter(vo -> YesNo.Y.equals(vo.getUseYn())) // useYn 값이 "Y"인 항목 필터링
                            .count(); // 필터링된 항목 개수 세기
                    if (count == 0) {
                        // Y인 항목이 아예없을 때
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("content", "");
                        externalList.add(data);
                    } else {
                        for (SafetyAndHealthCommPlanDetailVO external : result.getExternalList()) {
                            if (external.getUseYn() == YesNo.Y) {
                                Map<String, Object> data = new HashMap<String, Object>();
                                data.put("content", external.getContent());
                                data.put("period", external.getPeriod());
                                data.put("subject", external.getSubject());
                                data.put("method", external.getMethod());
                                externalList.add(data);
                            }
                        }
                    }
                } else {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", "");
                    externalList.add(data);
                }

                params.put("innerList", new JRBeanCollectionDataSource(innerList));
                params.put("externalList", new JRBeanCollectionDataSource(externalList));
                reportVO.setParameter(params);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                reportList.add(JasperReport);
                page = page + JasperReport.getPages().size();
                localPage = localPage + JasperReport.getPages().size();
            }
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String year : yearSet) {
            joiner.add(year);
        }
        fileNm = "(" + joiner.toString() + ")" + fileNm;
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }


    public List<JasperPrint> getShCommPlanDetailReport(HttpServletRequest request, HttpServletResponse response, SafetyAndHealthCommPlanVO vo) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + vo.getWriteYear() + ")";
        fileNm += "안전보건 의사소통 계획_";
        String title = "안전보건 의사소통 계획";

        // 데이터 조회
        SpSearchVO spSearchVO = new SpSearchVO();
        spSearchVO.setWriteYear(vo.getWriteYear());
        spSearchVO.setDocType(vo.getDocType());
        spSearchVO.setDocNo(vo.getDocNo());
        spSearchVO.setCompId(vo.getCompId());
        List<SafetyAndHealthCommPlanVO> resultList = getShCommPlanList(spSearchVO); // 데이터
        SafetyAndHealthCommPlanVO result = resultList.get(0);
        fileNm += result.getTitle();

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        // 표지 리포트
        ReportVO frontReportVO = new ReportVO();
        frontReportVO.setFileName(fileNm);
        frontReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        frontReportVO.setType(vo.getType());
        Map<String, Object> frontParam = new HashMap<String, Object>();
        frontParam.put("logo", logo);
        frontParam.put("title", title);
        frontParam.put("subTitle", "사업장명:" + compNm);
        frontParam.put("page", page);
        frontParam.put("subPage", subPage);
        frontParam.put("localPage", localPage);
        frontReportVO.setParameter(frontParam);
        JasperPrint JasperFrontReport = reportService.allReportJasperPrint(frontReportVO);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        ReportVO reportVO = new ReportVO();
        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/support/safetyAndHealthCommPlan/safetyAndHealthCommPlan.jrxml");
        reportVO.setType(spSearchVO.getType());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("title", title);
        params.put("docTitle", result.getTitle());
        params.put("docNo", result.getWriteYear() + "-" + result.getDocType() + "-" + result.getDocNo());
        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo2);

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);


        // 선택한 내부 의사소통 docSeq값 추출
        List<String> innerCheckedList = vo.getInnerList().stream()
                .map(SafetyAndHealthCommPlanDetailVO::getDocSeq) // docSeq 필드 추출
                .collect(Collectors.toList());
        // 선택한 외부 의사소통 docSeq값 추출
        List<String> externalCheckedList = vo.getExternalList().stream()
                .map(SafetyAndHealthCommPlanDetailVO::getDocSeq) // docSeq 필드 추출
                .collect(Collectors.toList());

        // 내부 의사소통
        List<Map<String, Object>> innerList = new ArrayList<>();
        if (innerCheckedList.size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO inner : result.getInnerList()) {
                boolean isContained = innerCheckedList.contains(inner.getDocSeq());
                if (isContained == true) {
                    // 선택한 요소라면
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", inner.getContent());
                    data.put("period", inner.getPeriod());
                    data.put("subject", inner.getSubject());
                    data.put("method", inner.getMethod());
                    innerList.add(data);
                }
            }
        } else {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("content", "");
            innerList.add(data);
        }
        params.put("innerList", new JRBeanCollectionDataSource(innerList));


        // 외부 의사소통
        List<Map<String, Object>> externalList = new ArrayList<>(); // 평가데이터
        if (externalCheckedList.size() > 0) {
            for (SafetyAndHealthCommPlanDetailVO external : result.getExternalList()) {
                boolean isContained = externalCheckedList.contains(external.getDocSeq());
                if (isContained == true) {
                    // 선택한 요소라면
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("content", external.getContent());
                    data.put("period", external.getPeriod());
                    data.put("subject", external.getSubject());
                    data.put("method", external.getMethod());
                    externalList.add(data);
                }
            }
        } else {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("content", "");
            externalList.add(data);
        }
        params.put("externalList", new JRBeanCollectionDataSource(externalList));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);

        // 통합 출력인 경우 reportList만 반환하고 종료
        reportService.exportReportAll(request, response, reportList, vo.getType(), fileNm);

        return reportList;
    }


}
