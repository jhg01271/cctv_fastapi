package kr.co.igns.business.support.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.support.model.TrainingPlanImplVO;
import kr.co.igns.system.common.model.FileVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.business.support.dao.postgres.TrainingResultReportDAO;
import kr.co.igns.business.support.model.TrainingResultReportHrVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TrainingResultReportService {
    private final TrainingResultReportDAO trainingResultReportDao;
    private final HrDAO hrDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final String targetType = "TRR";
    private final FileService fileService;
    private final NasFileService nasFileService;
    public List<TrainingResultReportVO> getTrainingResult(SpSearchVO searchVo) throws Exception {
        List<TrainingResultReportVO> voList = trainingResultReportDao.getTrainingResult(searchVo);
        return voList;
    }

    public TrainingResultReportVO getTrainingResultDetail(SpSearchVO searchVo) throws Exception {
        TrainingResultReportVO vo = trainingResultReportDao.getTrainingResultDetail(searchVo);
        SpSearchVO planVO = new SpSearchVO();
        planVO.setWriteYear(vo.getPlanWriteYear());
        planVO.setDocType(vo.getPlanDocType());
        planVO.setDocNo(vo.getPlanDocNo());
        vo.setPlanHrList(trainingResultReportDao.getTrainingResultDetailPlanHrList(planVO));
        vo.setFiles(trainingResultReportDao.getTrainingResultDetailFileList(vo));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveTrainingResultReport(List<MultipartFile> files, List<TrainingResultReportVO> voList) throws Exception {
        for (TrainingResultReportVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                vo.setDocType(targetType);
                trainingResultReportDao.insertTrainingResultReport(vo);
                List<TrainingResultReportHrVO> chargeList = vo.getChargeList();
                for(TrainingResultReportHrVO cvo : chargeList) {
                	cvo.setDocType(targetType);
                	cvo.setDocNo(vo.getDocNo());
                	cvo.setWriteYear(vo.getWriteYear());
                	trainingResultReportDao.insertTrainingResultReportHr(cvo);
                }
                fileService.saveFile(files, targetType, vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
            } else {
                //HSE 업무분장 수정
                TrainingResultReportVO resultVO = trainingResultReportDao.getTrainingResultReportById(vo);
                if (resultVO == null)
                    return null;
                resultVO = (TrainingResultReportVO) SpUtils.objectMap(vo, resultVO);

                trainingResultReportDao.updateTrainingResultReport(resultVO);
                
                trainingResultReportDao.deleteTrainingResultReportHr(resultVO);
                List<TrainingResultReportHrVO> chargeList = vo.getChargeList();
                for(TrainingResultReportHrVO cvo : chargeList) {
                	cvo.setDocType(targetType);
                	cvo.setDocNo(vo.getDocNo());
                	cvo.setWriteYear(vo.getWriteYear());
                	trainingResultReportDao.insertTrainingResultReportHr(cvo);
                }
                fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
                fileService.saveFile(files, targetType, vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteTrainingResultReport(TrainingResultReportVO reqVo) throws Exception {
        BaseVO vo = trainingResultReportDao.getTrainingResultReportById(reqVo);
        //trainingResultReportDao.deleteTrainingResultReportHr(reqVo); //삭제 시 담당자는 삭제되지 않도록 처리 2025.07.31 류원진
        trainingResultReportDao.deleteTrainingResultReport(reqVo);
        fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
        return vo;
    }

    public void deleteTrainingResultReports(List<TrainingResultReportVO> list) throws Exception {
        for (TrainingResultReportVO tmp : list) {
        	//trainingResultReportDao.deleteTrainingResultReportHr(tmp); //삭제 시 담당자는 삭제되지 않도록 처리 2025.07.31 류원진
            trainingResultReportDao.deleteTrainingResultReport(tmp);
        }
    }

    // 일괄출력 파라미터 조회
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<String> checkedList = trainingResultReportDao.getAllReport(spSearchVO);
        if (!checkedList.isEmpty()) {
            List<JasperPrint> jaspers = new ArrayList<>();
            spSearchVO.setCheckedList(checkedList);
            spSearchVO.setPrintAll(true);
            String month = spSearchVO.getSearchText();
            if(Integer.parseInt(spSearchVO.getSearchText())<10) {
                month = String.valueOf(Integer.valueOf(month));
            }
            spSearchVO.setExtra1(month+ "월");
            spSearchVO.setPage(page);
            spSearchVO.setSubPage(subPage);
            spSearchVO.setLocalPage(localPage);
            jaspers = getTrainingResultReport(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    public List<JasperPrint> getTrainingResultReport(HttpServletRequest request, HttpServletResponse response,
                                                       SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전보건교육 결과보고서";
        String title = "안전보건교육 결과보고서";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (String docNo : spSearchVO.getCheckedList()) {
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/support/trainingResult/trainingResult.jrxml");
            reportVO.setType(spSearchVO.getType());
            spSearchVO.setDocNo(docNo);
            TrainingResultReportVO detailList = trainingResultReportDao.getTrainingResultDetail(spSearchVO);
            if(spSearchVO.getCheckedList().size() == 1){
                fileNm += "_" + detailList.getTitle();
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("logo", logo2);
            params.put("page", spSearchVO.getPage());
            params.put("subPage", spSearchVO.getSubPage());
            params.put("localPage", spSearchVO.getLocalPage());
            params.put("title", title);

            // 파일 목록
            List<Map<String, Object>> imageList = new ArrayList<>();
            List<FileVO> fileVOList = trainingResultReportDao.getTrainingResultDetailFileList(detailList);
            for (FileVO fileVO : fileVOList) {
                HashMap<String, Object> image = new HashMap<>();
                // 이미지 파일만 추출
                if (fileVO.getDocType().equals("IMAGE")) {
                    image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                    imageList.add(image);
                }
            }

            // 파일이 없을때 null 이라도 넣어서 출력물 디자인 조정
            if( imageList.isEmpty() ) {
                HashMap<String, Object> image = new HashMap<>();
                image.put("img", null);
                imageList.add(image);
            }

            params.put("imageList", new JRBeanCollectionDataSource(imageList));

            //결재
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(detailList);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            // 기본 컬럼
            params.put("titleSub", detailList.getTitle());
            String time = "";
            if(detailList.getTrainingPeriod() != null){
                time = " " + detailList.getTrainingPeriod();
            }
            params.put("trainingDate", SpUtils.formatDate1(detailList.getTrainingDate()) + time);
            params.put("trainingTypeNm", detailList.getTrainingTypeNm());
            params.put("budget", SpUtils.formatCurrency1(detailList.getBudget()));
            params.put("requiredDetails", detailList.getRequiredDetails());
            params.put("trainingGoal", detailList.getTrainingGoal());
            params.put("expectedEffect", detailList.getExpectedEffect());
            params.put("materialsEtc", detailList.getMaterialsEtc() == null ? "" : detailList.getMaterialsEtc());
            params.put("materialsEtcYn", (detailList.getMaterialsEtc()== null || detailList.getMaterialsEtc().equals("")) ? "N" : "Y");
            params.put("materials", detailList.getMaterials());
            params.put("projector", detailList.getProjector());
            params.put("vtr", detailList.getVtr());
            params.put("trainingContent", detailList.getTrainingContent());
            params.put("trainingInstituteNm", detailList.getTrainingInstituteNm());
            params.put("trainingInstructorNm", detailList.getTrainingInstructorNm());
            params.put("trainingLocationNm", detailList.getTrainingLocationNm());
            params.put("qnaYn", detailList.getQnaYn().toString());
            params.put("reportYn", detailList.getReportYn().toString());
            params.put("deliveryTrainingYn", detailList.getDeliveryTrainingYn().toString());
            params.put("testYn", detailList.getTestYn().toString());
            params.put("effectivenessEtc", detailList.getEffectivenessEtc() == null ? "" : detailList.getEffectivenessEtc());
            params.put("effectivenessEtcYn", (detailList.getEffectivenessEtc()== null || detailList.getEffectivenessEtc().equals(""))? "N" : "Y");
            params.put("goodCondition", detailList.getGoodCondition());
            params.put("normalCondition", detailList.getNormalCondition());
            params.put("noConcern", detailList.getNoConcern());
            params.put("remark", detailList.getRemark());

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 인원
            BaseMapVO searchVo2 = new BaseMapVO(detailList.getDocType(),
                    detailList.getWriteYear() + detailList.getDocNo());
            searchVo2.setCompId(detailList.getCompId());   
            List<HrVO> hrlist = hrDao.getHrMapDetail(searchVo2);

            //인원 결재 매핑 조회(TPI)
            UtilsVO searchUtilVo = new UtilsVO();
            searchUtilVo.setTargetType(targetType);
            searchUtilVo.setTargetId(detailList.getWriteYear() + detailList.getDocNo() + "1");
            searchUtilVo.setType("attendee");
            List<UtilsVO> attendeeList = utilsService.getApprovalInfo(searchUtilVo).stream()
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> datasource = new ArrayList<>(); // 교육인원 결재
            if (!attendeeList.isEmpty()) {
                for (UtilsVO hr : attendeeList) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("hrNm", hr.getHrNm());
                    data.put("orgnNm", hr.getOrgnNm());
                    data.put("jbrpNm", hr.getJbrpNm());
                    data.put("signHr", utilsService.getSignatureFromBase64String(hr.getSignature()));
                    datasource.add(data);
                }
            }
            params.put("hrList", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();

        }

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public List<TrainingResultReportVO> getCurrentAndPreviousYear(SpSearchVO searchVo) throws Exception {
        List<TrainingResultReportVO> voList = trainingResultReportDao.getCurrentAndPreviousYear(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveCurrentAndPreviousYear(List<TrainingResultReportVO> voList) throws Exception {
        if(voList == null)
            return null;
        String newWriteYear = SpUtils.addNumToString(voList.get(0).getWriteYear(),1);
        voList.forEach(vo -> {
            vo.setWriteYear(newWriteYear);
            vo.setCmd("I");
            vo.setTrainingDate(null);
            vo.setTrainingStart(null);
            vo.setTrainingEnd(null);
            vo.setUpdatedBy(null);
            vo.setUpdatedAt(null);
            vo.setUseYn(YesNo.valueOf("Y"));
        });
        List<MultipartFile> newList = new ArrayList<>();
        return saveTrainingResultReport(newList, voList);
    }

}
