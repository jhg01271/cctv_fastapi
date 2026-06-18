package kr.co.igns.business.support.service;

import java.util.List;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.participation.model.HseObjectivesVO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.participation.model.ParticipationVo;
import kr.co.igns.business.support.dao.postgres.QualificationCertificationAssessmentDAO;
import kr.co.igns.business.support.model.QualificationCertificationAssessmentVO;
import kr.co.igns.business.support.model.QualificationCertificationDocVo;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class QualificationCertificationAssessmentService {
    private final QualificationCertificationAssessmentDAO dao;
    private final UtilsDAO utilsDao;
    private final ReportService reportService;
    private final UtilsService utilsService;

    public List<EducationTrainingVO> getQualificationManagement(SpSearchVO searchVo) throws Exception {
        List<EducationTrainingVO> voList = dao.getQualificationManagement(searchVo);
        return voList;
    }

    public BaseVO saveDocDetail(List<QualificationCertificationDocVo> reqVo) throws Exception {
        QualificationCertificationDocVo firstVo = reqVo.get(0);
        for (QualificationCertificationDocVo vo : reqVo) {
            if ("U".equals(vo.getCmd())) {
                dao.updateDocDetail(vo);
            } else {
                dao.insertDocDetail(vo);
            }
        }

        return firstVo;
    }

    public BaseVO delete(List<QualificationCertificationAssessmentVO> reqVo) throws Exception {
        for (QualificationCertificationAssessmentVO vo : reqVo) {
            dao.delete(vo);
        }

        return reqVo.get(0);
    }

    //내부심사원 자격인증 평가표 관리 - 평가항목
    public List<QualificationCertificationDocVo> getEvaluationList(SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationDocVo> voList = dao.getEvaluationList(searchVo);
        return voList;
    }

    //내부심사원 자격인증 평가표 관리 - 평가기준
    public List<QualificationCertificationDocVo> getEvaluationDetailList(SpSearchVO vo) throws Exception {
        List<QualificationCertificationDocVo> voList = dao.getEvaluationDetailList(vo);
        return voList;
    }

    //내부심사원 자격인증 평가표 관리 - 평가기준 전체조회
    public List<QualificationCertificationDocVo> getAllEvaluationDetailList(SpSearchVO vo) throws Exception {
        List<QualificationCertificationDocVo> voList = dao.getAllEvaluationDetailList(vo);
        return voList;
    }

    //내부심사원 자격인증 평가표 관리 - 평가항목 예시 데이터
    public List<QualificationCertificationDocVo> getEvaluationDataSetList(SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationDocVo> voList = dao.getEvaluationDataSetList(searchVo);
        return voList;
    }

    //내부심사원 자격인증 평가표 관리 - 평가기준 저장
    public List<QualificationCertificationDocVo> saveEvaluationList(List<QualificationCertificationDocVo> voList) throws Exception {
        for (QualificationCertificationDocVo vo : voList) {
            if (vo.getCmd().equals("I")) {
                dao.insertEvaluationList(vo);
            } else {
                dao.updateEvaluationList(vo);
            }

        }
        return voList;
    }


    @Transactional
    public BaseVO create(QualificationCertificationAssessmentVO reqVo) throws Exception {
        QualificationCertificationAssessmentVO maxData = null;
        String dt = reqVo.getWriteDate();
        if (reqVo.getHrType().equals("0001")) {
            reqVo.setColumnEtc2("");
        } else {
            reqVo.setColumnEtc1("");
        }
        reqVo.setWriteDate(DateUtils.formatDateForDB(dt));
        if (reqVo.getCmd() != null && reqVo.getCmd().equals("U")) {
            dao.update(reqVo);
        } else {
            reqVo.setDocType("QCA");
            dao.insert(reqVo);
        }

        ParticipationVo result = new ParticipationVo();
        result.setDocNo(reqVo.getDocNo());
        result.setDocType(reqVo.getDocType());
        result.setWriteYear(reqVo.getWriteYear());
        return result;
    }

    public List<QualificationCertificationAssessmentVO> searchList(SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationAssessmentVO> voList = dao.searchList(searchVo);
        return voList;
    }

    public QualificationCertificationAssessmentVO search(SpSearchVO searchVo) throws Exception {
        QualificationCertificationAssessmentVO vo = dao.search(searchVo);
        return vo;
    }

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "내부심사원 자격인증 평가표";
        String fileNm = "(" + spSearchVO.getWriteYear() + ") ";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        Boolean isSingle = false;
        if (spSearchVO.getCheckedObjList() != null && spSearchVO.getCheckedObjList().size() == 1) {
            isSingle = true;
        }
        for (SpSearchVO checkedObj : spSearchVO.getCheckedObjList()) {
            QualificationCertificationAssessmentVO vo = search(checkedObj);

            if (isSingle == true) {
                fileNm += vo.getHrNm();
            }
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/support/qualificationCert.jrxml");
            reportVO.setType(spSearchVO.getType());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", "내부심사원 자격인증 평가표");
            params.put("subTitle", "사업장명:" + compNm);

            InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            params.put("writerNm", vo.getWriterNm());
            params.put("writerOrgn", vo.getWriterOrgn());
            params.put("writeDate", DateUtils.formatDate(vo.getWriteDate()));
            params.put("hrNm", vo.getHrNm());
            params.put("jbrpNm", vo.getJbrpNm());
            params.put("deptNm", vo.getOrgnNm());
            params.put("remark", vo.getRemark());
            String workingAt = "";
            if (vo.getWorkingAt() != null && !vo.getWorkingAt().equals("")) {
                workingAt = DateUtils.formatDate(vo.getWorkingAt());
            }
            params.put("workingAt", workingAt);
            params.put("column1", String.valueOf(vo.getColumn1()));
            params.put("column2", String.valueOf(vo.getColumn2()));
            params.put("column3", String.valueOf(vo.getColumn3()));
            params.put("column4", String.valueOf(vo.getColumn4()));
            params.put("column5", String.valueOf(vo.getColumn5()));
            params.put("total", String.valueOf(vo.getColumn1() + vo.getColumn2() + vo.getColumn3() + vo.getColumn4() + vo.getColumn5()));

            SpSearchVO searchVO = new SpSearchVO();
            searchVO.setCompId(SecurityUtil.getCurrentCompId());
            List<QualificationCertificationDocVo> docVoList = dao.getAllEvaluationDetailList(searchVO);
            List<Map<String, Object>> table1 = new ArrayList<>();
            List<Map<String, Object>> table2 = new ArrayList<>();
            List<Map<String, Object>> table3 = new ArrayList<>();
            List<Map<String, Object>> table4 = new ArrayList<>();
            List<Map<String, Object>> table5 = new ArrayList<>();
            for (QualificationCertificationDocVo docVo : docVoList) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("content", docVo.getCriteriaNm());
                data.put("point", String.valueOf(docVo.getPoint()));
                switch (docVo.getItemId()) {
                    case "202501010001":
                        table1.add(data);
                        break;
                    case "202501010002":
                        table2.add(data);
                        break;
                    case "202501010003":
                        table3.add(data);
                        break;
                    case "202501010004":
                        table4.add(data);
                        break;
                    case "202501010005":
                        table5.add(data);
                        break;
                }
            }
            List<Map<String, Object>> tableEtc1 = new ArrayList<>();
            List<Map<String, Object>> tableEtc2 = new ArrayList<>();
            String[] arr1 = vo.getColumnEtc1().split(",");
            String[] arr2 = vo.getColumnEtc2().split(",");
            for (QualificationCertificationDocVo docVo : docVoList) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("content", docVo.getCriteriaNm());
                if (docVo.getItemId().equals("202501010006") && vo.getHrType().equals("0001")) {
                    boolean contains = Arrays.stream(arr1).anyMatch(x -> x.equals(docVo.getCriteriaId()));
                    if (contains) {
                        data.put("checked", "Y");
                    } else {
                        data.put("checked", "N");
                    }
                    tableEtc1.add(data);
                } else if (docVo.getItemId().equals("202501010007") && vo.getHrType().equals("0002")) {
                    boolean contains = Arrays.stream(arr2).anyMatch(x -> x.equals(docVo.getCriteriaId()));
                    if (contains) {
                        data.put("checked", "Y");
                    } else {
                        data.put("checked", "N");
                    }
                    tableEtc2.add(data);
                }
            }
            params.put("table1", new JRBeanCollectionDataSource(table1));
            params.put("table2", new JRBeanCollectionDataSource(table2));
            params.put("table3", new JRBeanCollectionDataSource(table3));
            params.put("table4", new JRBeanCollectionDataSource(table4));
            params.put("table5", new JRBeanCollectionDataSource(table5));
            params.put("tableEtc1", new JRBeanCollectionDataSource(tableEtc1));
            params.put("tableEtc2", new JRBeanCollectionDataSource(tableEtc2));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
            spSearchVO.setPage(page);
            spSearchVO.setLocalPage(localPage);
        }
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        return reportList;
    }
}
