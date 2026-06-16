package kr.co.igns.business.impl.service;

import kr.co.igns.business.alarm.model.AlarmVO;
import kr.co.igns.business.impl.dao.postgres.ContractorAssmtReportDAO;
import kr.co.igns.business.impl.model.ContractorAssmtReportHrVO;
import kr.co.igns.business.impl.model.ContractorAssmtReportVO;
import kr.co.igns.business.impl.model.ContractorChecklistDetailVO;
import kr.co.igns.business.impl.model.ContractorChecklistVO;
import kr.co.igns.business.impl.model.ContractorInvestigationFormDetailVO;
import kr.co.igns.business.impl.model.ContractorInvestigationFormVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
public class ContractorAssmtReportService {
    private final ContractorAssmtReportDAO dao;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasFileService;

    public List<ContractorChecklistVO> getEvaluationType(SpSearchVO searchVo) throws Exception {
        List<ContractorChecklistVO> voList = dao.getEvaluationType(searchVo);
        for(ContractorChecklistVO vo : voList) {
            searchVo.setSearchText(vo.getEvalTypeId());
            vo.setDetailDataList(this.getEvaluationTypeDetail(searchVo));
        }
        return voList;
    }

    public List<ContractorChecklistDetailVO> getEvaluationTypeDetail(SpSearchVO searchVo) throws Exception {
        List<ContractorChecklistDetailVO> voList = dao.getEvaluationTypeDetail(searchVo);
        return voList;
    }

    public List<ContractorChecklistVO> getEvaluationTypeDataset(SpSearchVO searchVo) throws Exception {
        List<ContractorChecklistVO> voList = dao.getEvaluationTypeDataset(searchVo);
        for (ContractorChecklistVO vo : voList) {
            vo.setDetailDataList(this.getEvaluationTypeDetailDataset(vo));
        }
        return voList;
    }

    public List<ContractorChecklistDetailVO> getEvaluationTypeDetailDataset(ContractorChecklistVO vo) throws Exception {
        List<ContractorChecklistDetailVO> voList = dao.getEvaluationDetailDataset(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEvaluationType(List<ContractorChecklistVO> voList) throws Exception {
        for (ContractorChecklistVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                dao.insertEvaluationType(vo);
            } else {
                dao.updateEvaluationType(vo);
            }
            for (ContractorChecklistDetailVO detail : vo.getDetailDataList()) {
                if (detail.getCmd().equals("I")) {
                    detail.setEvalTypeId(vo.getEvalTypeId());
                    dao.insertEvaluationTypeDetail(detail);
                } else {
                    dao.updateEvaluationTypeDetail(detail);
                }
            }
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteEvaluationType(List<ContractorChecklistVO> voList) throws Exception {
        for (ContractorChecklistVO vo : voList) {
            if ("D".equals(vo.getCmd())) {
                // 하위데이터까지 전체 삭제
                dao.deleteEvaluationType(vo);
                dao.deleteEvaluationTypeDetailAll(vo);
            } else {
                // 하위데이터만 삭제
                for (ContractorChecklistDetailVO detail : vo.getDetailDataList()) {
                    dao.deleteEvaluationTypeDetail(detail);
                }
            }
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteAssmtReport(List<ContractorAssmtReportVO> reqVo) throws Exception {
        for (ContractorAssmtReportVO vo : reqVo) {
            dao.deleteReport(vo);
        }
        return reqVo.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<ContractorAssmtReportVO> saveAssmtReport(List<ContractorAssmtReportVO> reqVo) throws Exception {

        String userId = SecurityUtil.getCurrentMemberId();
        for (ContractorAssmtReportVO vo : reqVo) {
            if (vo.getCmd() == null || vo.getCmd().equals("I")) {
                SpSearchVO searchVo = new SpSearchVO();
                searchVo.setWriteYear(vo.getWriteYear());
                searchVo.setDocType(vo.getDocType());
                searchVo.setPartCompId(vo.getPartCompId());
                dao.insertReport(vo);
            } else {
                dao.updateReport(vo);
            }
            List<ContractorAssmtReportHrVO> hrVoList = vo.getAssmtHrList();
            dao.deleteAssmtHr(vo);
            for (ContractorAssmtReportHrVO hrVo : hrVoList) {
                hrVo.setDocType(vo.getDocType());
                hrVo.setWriteYear(vo.getWriteYear());
                hrVo.setDocNo(vo.getDocNo());
                dao.insertAssmtHr(hrVo);
            }
            List<ContractorChecklistVO> detailVoList = vo.getDetailDataList();
            if (detailVoList.size() > 0 && detailVoList.get(0).getCmd().equals("I")) {
                dao.deleteEvaluation(vo);
            }
            for (ContractorChecklistVO detailVo : detailVoList) {
                detailVo.setWriteYear(vo.getWriteYear());
                detailVo.setDocNo(vo.getDocNo());
                detailVo.setDocType(vo.getDocType());
                if (detailVo.getCmd().equals("I")) {
                    detailVo.setCreatedBy(userId);
                    dao.insertEvaluation(detailVo);
                } else {
                    detailVo.setUpdatedBy(userId);
                    dao.updateEvaluation(detailVo);
                }
                List<ContractorChecklistDetailVO> detalisList = detailVo.getDetailDataList();
                if (detalisList.size() > 0 && detalisList.get(0).getCmd().equals("I")) {
                    dao.deleteEvaluationDetail(detailVo);
                }
                for (ContractorChecklistDetailVO details : detalisList) {
                    details.setWriteYear(vo.getWriteYear());
                    details.setDocNo(vo.getDocNo());
                    details.setDocType(vo.getDocType());
                    if (details.getCmd().equals("I")) {
                        details.setCreatedBy(userId);
                        dao.insertEvaluationDetail(details);
                    } else {
                        details.setUpdatedBy(userId);
                        dao.updateEvaluationDetail(details);
                    }
                }
            }
        }
        return reqVo;
    }

    public List<ContractorAssmtReportVO> getAssmtReport(SpSearchVO searchVo) throws Exception {
        searchVo.setCompId(SecurityUtil.getCurrentCompId());//
        List<ContractorAssmtReportVO> voList = dao.getAssmtReport(searchVo);
        return voList;
    }

    public List<ContractorAssmtReportVO> getAssmtReportDetail(SpSearchVO searchVo) throws Exception {
        List<ContractorAssmtReportVO> vo = dao.getAssmtReportDetail(searchVo);
        for (ContractorAssmtReportVO item : vo) {
            List<ContractorAssmtReportHrVO> hrList = dao.getAssmtHr(item);
            item.setAssmtHrList(hrList);
            List<ContractorChecklistVO> detailList = dao.getEvaluation(item);
            item.setDetailDataList(detailList);
            for (ContractorChecklistVO detail : item.getDetailDataList()) {
                detail.setDetailDataList(dao.getEvaluationDetail(detail));
            }
        }

        return vo;
    }

    public ContractorAssmtReportVO getAssmtReportDetailToDocNo(SpSearchVO searchVo) throws Exception {
        ContractorAssmtReportVO vo = dao.getAssmetReportDetailToDocNo(searchVo);
        List<ContractorAssmtReportHrVO> hrList = dao.getAssmtHr(vo);
        vo.setAssmtHrList(hrList);
        List<ContractorChecklistVO> detailList = dao.getEvaluation(vo);
        vo.setDetailDataList(detailList);
        for (ContractorChecklistVO detail : vo.getDetailDataList()) {
            detail.setDetailDataList(dao.getEvaluationDetail(detail));
        }
        return vo;
    }

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "협력업체 안전보건 평가 보고서";
        String fileNm = "";

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        List<String> yearSet = new ArrayList<>();
        String partCompNm = "";
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {//선택된 카드들 for문

            if (vo.isPrintAll() == true) { //모든 데이터 출력
                List<ContractorAssmtReportVO> detailDatas = getAssmtReportDetail(vo);
                for (ContractorAssmtReportVO item : detailDatas) {
                    ReportVO reportVO = new ReportVO();
                    reportVO.setJrxmlPath("report/impl/contractorAssmtReport/contractorAssmtReport.jrxml");
                    reportVO.setType(spSearchVO.getType());
                    yearSet.add(vo.getWriteYear());
                    Map<String, Object> params = new HashMap<String, Object>();
                    if (item.getUseYn().equals("Y")) {
                        partCompNm = item.getPartCompNm();
                        params.put("title", title);
                        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                        params.put("logo", logo2);
                        params.put("page", page);
                        params.put("subPage", subPage);
                        params.put("localPage", localPage);
                        params.put("partCompNm", item.getPartCompNm());
                        params.put("assmtDt", DateUtils.formatDate(item.getAssmtDt()));

                        //*사인 데이터*//
                        UtilsVO signParam = new UtilsVO();
                        signParam.setDocType("CSHAR");

                        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, item.getWriteYear()+item.getDocNo()+item.getPartCompId());params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));
                        List<ContractorAssmtReportHrVO> hrList = item.getAssmtHrList();
                        String assmtHr = "";
                        if (hrList != null) {
                            for (ContractorAssmtReportHrVO hr : hrList) {
                                if ("".equals(assmtHr)) {
                                    assmtHr = hr.getHrNm();
                                } else {
                                    assmtHr += ", " + hr.getHrNm();
                                }
                            }
                        } else {
                            assmtHr = "";
                        }
                        params.put("assmtHrNm", assmtHr);
                        params.put("assmtManagerHrNm", item.getAssmtManagerHrNm());
                        params.put("assmtContent", item.getAssmtContent());
                        if (item.getPoint() == 0) {
                            item.setPoint(0);
                        }
                        params.put("point", item.getPoint());
                        params.put("remark", item.getRemark());
                        params.put("insufficientContent", item.getInsufficientContent());
                        params.put("superiorContent", item.getSuperiorContent());

                        List<Map<String, Object>> datasource = new ArrayList<>();
                        int idCount = 0;
                        int totalPoint = 0;
                        for (int i = 0; i < item.getDetailDataList().size(); i++) {
                            ContractorChecklistVO checkList = item.getDetailDataList().get(i);
                            for (int j = 0; j < checkList.getDetailDataList().size(); j++) {
                                Map<String, Object> dataMap = new HashMap<String, Object>();
                                ContractorChecklistDetailVO data = checkList.getDetailDataList().get(j);
                                dataMap.put("ordSeq", checkList.getOrdSeq());
                                dataMap.put("compId", SecurityUtil.getCurrentCompId());
                                dataMap.put("evalTypeNm", checkList.getEvalTypeNm());
                                dataMap.put("evalDetailNm", data.getEvalDetailNm());
                                dataMap.put("point", data.getPoint());
                                dataMap.put("weight", data.getWeight());
                                dataMap.put("divA", data.getDivA());
                                dataMap.put("divB", data.getDivB());
                                dataMap.put("divC", data.getDivC());
                                dataMap.put("divD", data.getDivD());
                                dataMap.put("divE", data.getDivE());
                                dataMap.put("contentId", idCount);
                                datasource.add(dataMap);
                                totalPoint += data.getPoint();
                                idCount++;
                            }
                        }
                        params.put("detailList", new JRBeanCollectionDataSource(datasource));
                        params.put("totalPoint", totalPoint);
                        reportVO.setParameter(params);
                        System.out.println(" report vo = > " + reportVO);
                        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                        reportList.add(JasperReport);
                        page = page + JasperReport.getPages().size();
                        localPage = localPage + JasperReport.getPages().size();
                    }
                }
            } else { //선택된 데이터만 출력
                ContractorAssmtReportVO item = getAssmtReportDetailToDocNo(vo);
                ReportVO reportVO = new ReportVO();
                reportVO.setJrxmlPath("report/impl/contractorAssmtReport/contractorAssmtReport.jrxml");
                reportVO.setType(spSearchVO.getType());
                yearSet.add(vo.getWriteYear());
                Map<String, Object> params = new HashMap<String, Object>();
                if (item.getUseYn().equals("Y")) {
                    partCompNm = item.getPartCompNm();
                    params.put("title", title);
                    InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                    params.put("logo", logo2);
                    params.put("page", page);
                    params.put("subPage", subPage);
                    params.put("localPage", localPage);
                    params.put("partCompNm", item.getPartCompNm());
                    params.put("assmtDt", DateUtils.formatDate(item.getAssmtDt()));

                    //*사인 데이터*//
                    UtilsVO signParam = new UtilsVO();
                    signParam.setDocType("CSHAR");

                    List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, item.getWriteYear()+item.getDocNo()+item.getPartCompId());
                    params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));
                    List<ContractorAssmtReportHrVO> hrList = item.getAssmtHrList();
                    String assmtHr = "";
                    if (hrList != null) {
                        for (ContractorAssmtReportHrVO hr : hrList) {
                            if ("".equals(assmtHr)) {
                                assmtHr = hr.getHrNm();
                            } else {
                                assmtHr += ", " + hr.getHrNm();
                            }
                        }
                    } else {
                        assmtHr = "";
                    }
                    params.put("assmtHrNm", assmtHr);
                    params.put("assmtManagerHrNm", item.getAssmtManagerHrNm());
                    params.put("assmtContent", item.getAssmtContent());
                    if (item.getPoint() == 0) {
                        item.setPoint(0);
                    }
                    params.put("point", item.getPoint());
                    params.put("remark", item.getRemark());
                    params.put("insufficientContent", item.getInsufficientContent());
                    params.put("superiorContent", item.getSuperiorContent());

                    List<Map<String, Object>> datasource = new ArrayList<>();
                    int idCount = 0;
                    int totalPoint = 0;
                    for (int i = 0; i < item.getDetailDataList().size(); i++) {
                        ContractorChecklistVO checkList = item.getDetailDataList().get(i);
                        for (int j = 0; j < checkList.getDetailDataList().size(); j++) {
                            Map<String, Object> dataMap = new HashMap<String, Object>();
                            ContractorChecklistDetailVO data = checkList.getDetailDataList().get(j);
                            dataMap.put("ordSeq", Integer.valueOf(checkList.getOrdSeq()));
                            dataMap.put("compId", SecurityUtil.getCurrentCompId());
                            dataMap.put("evalTypeNm", checkList.getEvalTypeNm());
                            dataMap.put("evalDetailNm", data.getEvalDetailNm());
                            dataMap.put("point", data.getPoint());
                            dataMap.put("weight", data.getWeight());
                            dataMap.put("divA", data.getDivA());
                            dataMap.put("divB", data.getDivB());
                            dataMap.put("divC", data.getDivC());
                            dataMap.put("divD", data.getDivD());
                            dataMap.put("divE", data.getDivE());
                            dataMap.put("contentId", idCount);
                            datasource.add(dataMap);
                            totalPoint += data.getPoint();
                            idCount++;
                        }
                    }
                    params.put("detailList", new JRBeanCollectionDataSource(datasource));
                    params.put("totalPoint", totalPoint);
                    reportVO.setParameter(params);
                    JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                    reportList.add(JasperReport);
                    page = page + JasperReport.getPages().size();
                    localPage = localPage + JasperReport.getPages().size();
                }
            }
        }
        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String years = yearSet.stream().distinct().collect(Collectors.joining(","));
        fileNm = "(" + years + ")";
        fileNm += title;
        if (spSearchVO.getCheckedObjList().size() == 1) {
            fileNm += "_" + partCompNm;
        }

        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);
        return reportList;
    }
}
