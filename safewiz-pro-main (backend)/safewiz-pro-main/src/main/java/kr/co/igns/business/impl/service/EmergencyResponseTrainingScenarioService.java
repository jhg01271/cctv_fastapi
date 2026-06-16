package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.EmergencyResponseDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyResponseTrainingScenarioDAO;
import kr.co.igns.business.impl.model.EmergencyControlTaskAsgmtVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingScenarioVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmergencyResponseTrainingScenarioService {
    private final EmergencyResponseTrainingScenarioDAO emergencyResponseTrainingScenarioDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<EmergencyResponseTrainingVO> getTrainingList(SpSearchVO searchVo) throws Exception {
        List<EmergencyResponseTrainingVO> voList = emergencyResponseTrainingScenarioDAO.getTrainingList(searchVo);
        return voList;
    }

    public EmergencyResponseTrainingVO getTrainingDetail(SpSearchVO searchVo) throws Exception {
        EmergencyResponseTrainingVO vo = emergencyResponseTrainingScenarioDAO.getTrainingDetail(searchVo);
        vo.setScenarioList(emergencyResponseTrainingScenarioDAO.getScenarioList(vo));
        vo.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "ERT"));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveTraining(List<MultipartFile> files, EmergencyResponseTrainingVO vo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
	    switch (vo.getCmd()) {
		    case "I":
			    emergencyResponseTrainingScenarioDAO.insertTraining(vo);
			    fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
			    break;
		    case "U":
			    emergencyResponseTrainingScenarioDAO.updateTraining(vo);
			    fileService.deleteFile(vo.getDeleteFiles(), "ERT", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
			    fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
			    break;
		    case "D":
			    emergencyResponseTrainingScenarioDAO.deleteTraining(vo);
			    fileService.deleteFile(vo.getDeleteFiles(), "ERT", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
			    fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
			    break;
	    }
        for (EmergencyResponseTrainingScenarioVO scenario : vo.getScenarioList()) {
            scenario.setWriteYear(vo.getWriteYear());
            scenario.setDocType(vo.getDocType());
            scenario.setDocNo(vo.getDocNo());
            scenario.setCompId(vo.getCompId());
            if (scenario.getCmd().equals("I")) {
                scenario.setCreatedBy(userId);
                emergencyResponseTrainingScenarioDAO.insertScenario(scenario);
            } else if (scenario.getCmd().equals("U")) {
                scenario.setUpdatedBy(userId);
                emergencyResponseTrainingScenarioDAO.updateScenario(scenario);
            } else {
                emergencyResponseTrainingScenarioDAO.deleteScenario(scenario);
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteTraining(List<EmergencyResponseTrainingVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (EmergencyResponseTrainingVO vo : voList) {
            vo.setUpdatedBy(userId);
            emergencyResponseTrainingScenarioDAO.deleteTraining(vo);
        }
        return voList.get(0);
    }
    public List<JasperPrint> getScenarioReport (HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        spSearchVO.setCheckedObjList(emergencyResponseTrainingScenarioDAO.getScenarioReport(spSearchVO));
        reportList = getTrainingReport(request, response, spSearchVO);
        return reportList;
    }
    public List<JasperPrint> getTrainingReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "비상대응 훈련 시나리오";
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

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/impl/emergencyResponseTraining/emergencyResponseTrainingScenario.jrxml");
            reportVO.setType(spSearchVO.getType());

            EmergencyResponseTrainingVO result = getTrainingDetail(vo); // 데이터
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getTrainingNm();
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            //결재
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));


            params.put("orgnNm", result.getOrgnNm());
            params.put("trainingNm", result.getTrainingNm());
            params.put("trainingLocation", result.getTrainingLocation());
            params.put("trainingContent", result.getTrainingContent());
            params.put("anticipatedDamage", result.getAnticipatedDamage());
            params.put("scenarioList", new JRBeanCollectionDataSource(result.getScenarioList()));
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
}
