package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.model.EmergencyResponseTrainingResultVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import kr.co.igns.business.impl.dao.postgres.EmergencyResponseTrainingResultDAO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyResponseTrainingResultService {
    private final EmergencyResponseTrainingScenarioService emergencyResponseTrainingScenarioService;
    private final EmergencyResponseTrainingResultDAO emergencyResponseTrainingResultDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<EmergencyResponseTrainingVO> getResultList(SpSearchVO searchVo) throws Exception {
        List<EmergencyResponseTrainingVO> voList = emergencyResponseTrainingScenarioService.getTrainingList(searchVo);
        List<EmergencyResponseTrainingVO> returnValue = new ArrayList<>();
        for (EmergencyResponseTrainingVO vo : voList) {
            if (vo.getUseYn().equals(YesNo.valueOf("Y"))) {
                // 시나리오 사용여부가 Y인 항목만 조회
                List<EmergencyResponseTrainingResultVO> result = emergencyResponseTrainingResultDAO.getResultList(vo);
                if (result.size() > 0) {
                    vo.setResultList(emergencyResponseTrainingResultDAO.getResultList(vo));
                    returnValue.add(vo);
                }
            }
        }
        return returnValue;
    }

    public List<EmergencyResponseTrainingResultVO> getResultMaster(SpSearchVO searchVo) throws Exception {
        EmergencyResponseTrainingVO vo = new EmergencyResponseTrainingVO();
        vo.setWriteYear(searchVo.getWriteYear());
        vo.setDocType(searchVo.getDocType());
        vo.setDocNo(searchVo.getDocNo());
        vo.setCompId(searchVo.getCompId());
        return emergencyResponseTrainingResultDAO.getResultList(vo);
    }

    public List<EmergencyResponseTrainingResultVO> getResultDetail(EmergencyResponseTrainingVO vo) throws Exception {
        List<EmergencyResponseTrainingResultVO> voList = emergencyResponseTrainingResultDAO.getResultDetail(vo);
        for (EmergencyResponseTrainingResultVO result : voList) {
            result.setFiles(fileService.getFileList(result.getWriteYear() + result.getDocNo() + result.getDocSeq(), "ERT"));
        }
        return voList;
    }

    public List<EmergencyResponseTrainingVO> getValidSenarioList(EmergencyResponseTrainingVO vo) throws Exception {
        List<EmergencyResponseTrainingVO> voList = emergencyResponseTrainingResultDAO.getValidSenarioList(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public EmergencyResponseTrainingResultVO saveResult(List<MultipartFile> files, EmergencyResponseTrainingResultVO vo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(userId);
            emergencyResponseTrainingResultDAO.insertResult(vo);
            fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(), SecurityUtil.getCurrentCompId());
        } else if (vo.getCmd().equals("U")) {
            vo.setUpdatedBy(userId);
            fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(), SecurityUtil.getCurrentCompId());
            emergencyResponseTrainingResultDAO.updateResult(vo);
            fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(), SecurityUtil.getCurrentCompId());
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteResult(List<EmergencyResponseTrainingResultVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (EmergencyResponseTrainingResultVO vo : voList) {
            vo.setUpdatedBy(userId);
            emergencyResponseTrainingResultDAO.deleteResult(vo);
        }
        return voList.get(0);
    }

    // 일괄출력
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        String scenarioDocNo = "";
        List<SpSearchVO> resultList = emergencyResponseTrainingResultDAO.getAllReport(spSearchVO);
        // 시나리오
        if (!scenarioDocNo.equals(resultList.get(0).getDocNo())) {
            // 시나리오는 한번만 출력
            scenarioDocNo = resultList.get(0).getDocNo();
            List<JasperPrint> scenarioJaspers = new ArrayList<>();
            SpSearchVO scenarioVO = new SpSearchVO();
            scenarioVO.setWriteYear(spSearchVO.getWriteYear());
            scenarioVO.setCompId(spSearchVO.getCompId());
            scenarioVO.setDocType(resultList.get(0).getDocType());
            scenarioVO.setDocNo(scenarioDocNo);
            scenarioVO.setPrintAll(true);
            scenarioVO.setPage(page);
            scenarioVO.setSubPage(subPage);
            scenarioVO.setLocalPage(localPage);
            scenarioJaspers = emergencyResponseTrainingScenarioService.getScenarioReport(request, response, scenarioVO);

            for (JasperPrint report : scenarioJaspers) {
                page += report.getPages().size();
                localPage += report.getPages().size();
            }
            reportList.addAll(scenarioJaspers);
        }

        // 결과보고서
        SpSearchVO resultVO = new SpSearchVO();
        resultVO.setWriteYear(spSearchVO.getWriteYear());
        resultVO.setCompId(spSearchVO.getCompId());
        resultVO.setPrintAll(true);
        resultVO.setPage(page);
        resultVO.setSubPage(subPage);
        resultVO.setLocalPage(localPage);
        resultVO.setCheckedObjList(resultList);

        List<JasperPrint> resultJaspers = new ArrayList<>();
        resultJaspers = getResultDetailReport(request, response, resultVO);
        reportList.addAll(resultJaspers);
        spSearchVO.setSubPage(spSearchVO.getSubPage());
        for (JasperPrint report : resultJaspers) {
            page += report.getPages().size();
            localPage += report.getPages().size();
        }
        return reportList;
    }

    public List<JasperPrint> getResultReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "비상대응 훈련 실시 보고서";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (SpSearchVO spVo : spSearchVO.getCheckedObjList()) {

            EmergencyResponseTrainingVO vo = new EmergencyResponseTrainingVO();
            vo.setWriteYear(spVo.getWriteYear());
            vo.setDocType(spVo.getDocType());
            vo.setDocNo(spVo.getDocNo());
            vo.setCompId(spVo.getCompId());
            List<EmergencyResponseTrainingResultVO> resultList = getResultDetail(vo); // 데이터

            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + resultList.get(0).getTrainingNm();
            }
            for (EmergencyResponseTrainingResultVO result : resultList) {
                if (result.getUseYn().equals(YesNo.valueOf("Y"))) {
                    // 목록에서 출력할 때
                    ReportVO reportVO = new ReportVO();
                    reportVO.setFileName(fileNm);
                    reportVO.setJrxmlPath("report/impl/emergencyResponseTraining/emergencyResponseTrainingResult.jrxml");
                    reportVO.setType(spSearchVO.getType());
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("title", title);
                    params.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId()));

                    params.put("page", page);
                    params.put("subPage", subPage);
                    params.put("localPage", localPage);

                    String targetId = result.getWriteYear() + result.getDocNo() + result.getDocSeq();
                    List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo, targetId);
                    params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

                    params.put("trainingNm", result.getTrainingNm());
                    params.put("orgnNm", result.getOrgnNm());
                    params.put("actionNm", result.getActionNm());
                    params.put("actionDt", DateUtils.formatDate(result.getActionDt()));
                    params.put("actionLocation", result.getActionLocation());
                    params.put("actionContent", result.getActionContent());
                    params.put("actionResult", result.getActionResult());
                    params.put("targetTime", result.getTargetTime());
                    params.put("measurementTime", result.getMeasurementTime());
                    params.put("observationActualBehavior", result.getObservationActualBehavior());
                    params.put("remark", result.getRemark());
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
            }
        }
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }

    public List<JasperPrint> getResultDetailReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "비상대응 훈련 실시 보고서";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;


        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        EmergencyResponseTrainingVO vo = new EmergencyResponseTrainingVO();
        vo.setWriteYear(spSearchVO.getCheckedObjList().get(0).getWriteYear());
        vo.setDocType(spSearchVO.getCheckedObjList().get(0).getDocType());
        vo.setDocNo(spSearchVO.getCheckedObjList().get(0).getDocNo());
        vo.setCompId(spSearchVO.getCheckedObjList().get(0).getCompId());
        List<EmergencyResponseTrainingResultVO> resultList = getResultDetail(vo);
        fileNm += "_" + resultList.get(0).getTrainingNm();
        for (EmergencyResponseTrainingResultVO result : resultList) {
            List<SpSearchVO> isContained = spSearchVO.getCheckedObjList().stream()
                    .filter(el -> el.getWriteYear().equals(result.getWriteYear()) && el.getDocNo().equals(result.getDocNo()) && el.getDocSeq().equals(result.getDocSeq()))
                    .collect(Collectors.toList());
            if (isContained.size() > 0) {
                // 목록에서 출력할 때
                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/impl/emergencyResponseTraining/emergencyResponseTrainingResult.jrxml");
                reportVO.setType(spSearchVO.getType());
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("title", title);
                params.put("logo", utilsService.getClntLogo(SecurityUtil.getCurrentClntId()));

                params.put("page", page);
                params.put("subPage", subPage);
                params.put("localPage", localPage);

                String targetId = result.getWriteYear() + result.getDocNo() + result.getDocSeq();
                List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo, targetId);
                params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

                params.put("trainingNm", result.getTrainingNm());
                params.put("orgnNm", result.getOrgnNm());
                params.put("actionNm", result.getActionNm());
                params.put("actionDt", DateUtils.formatDate(result.getActionDt()));
                params.put("actionLocation", result.getActionLocation());
                params.put("actionContent", result.getActionContent());
                params.put("actionResult", result.getActionResult());
                params.put("targetTime", result.getTargetTime());
                params.put("measurementTime", result.getMeasurementTime());
                params.put("observationActualBehavior", result.getObservationActualBehavior());
                params.put("remark", result.getRemark());
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
        }
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }

}
