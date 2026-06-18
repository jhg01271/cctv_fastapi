package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.TrainingPlanImplDAO;
import kr.co.igns.business.support.model.*;
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
import kr.co.igns.business.support.model.TrainingLocationVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class TrainingPlanImplService {
    private final TrainingPlanImplDAO trainingPlanImplDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final HrDAO hrDao;
    private final String targetType = "TPI";


    public List<TrainingLocationVO> getLocMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingLocationVO> voList = trainingPlanImplDao.getLocMngList(searchVo);
        return voList;
    }

    public List<TrainingLocationVO> getDataSetLocMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingLocationVO> voList = trainingPlanImplDao.getDataSetLocMngList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingLocationVO> saveLocMngList(List<TrainingLocationVO> voList) throws Exception {
        for(TrainingLocationVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                trainingPlanImplDao.insertLocMngList(vo);
            }else{
                trainingPlanImplDao.updateLocMngList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingLocationVO> deleteLocMngList(List<TrainingLocationVO> voList) throws Exception {
        for(TrainingLocationVO vo : voList){
            trainingPlanImplDao.deleteLocMngList(vo);
        }
        return voList;
    }



    public List<TrainingPlanImplVO> getTrainingPlanImpl(SpSearchVO searchVo) throws Exception {
        List<TrainingPlanImplVO> voList = trainingPlanImplDao.getTrainingPlanImpl(searchVo);

        return voList;
    }

     public List<TrainingPlanImplVO> getCurrentAndPreviousYear(SpSearchVO searchVo) throws Exception {
        if(searchVo.getWriteYear() != null){
            searchVo.setWriteYear(String.valueOf(Integer.parseInt(searchVo.getWriteYear()) - 1));
        }
        List<TrainingPlanImplVO> voList = trainingPlanImplDao.getCurrentAndPreviousYear(searchVo);
        return voList;
    }

    public TrainingPlanImplVO getTrainingPlanImplDetail(SpSearchVO searchVo) throws Exception {
        TrainingPlanImplVO voList = trainingPlanImplDao.getTrainingPlanImplDetail(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveTrainingPlanImpl(List<TrainingPlanImplVO> voList) throws Exception {
        for (TrainingPlanImplVO vo : voList) {
            String key = vo.getWriteYear() + vo.getDocNo();
            if (vo.getCmd().equals("I")) {
                vo.setDocType(targetType);
                trainingPlanImplDao.insertTrainingPlanImpl(vo);

                key = vo.getWriteYear() + vo.getDocNo();
                // 인원 매핑 생성
                if (vo.getHrList() != null) {
//                    for (HrVO hrinfo : vo.getHrList()) {
//                        BaseMapVO hrMapInfo = new BaseMapVO(targetType, key, hrinfo.getHrId(), hrinfo.getHrId(),
//                                hrinfo.getCreatedBy());
//                        hrDao.addHrMap(hrMapInfo);
//                    }
                    trainingPlanImplDao.deleteTrainingPlanImplementationLearner(vo);
                    for (HrVO hrinfo : vo.getHrList()) {
                        hrinfo.setWriteYear(vo.getWriteYear());
                        hrinfo.setDocType(vo.getDocType());
                        hrinfo.setDocNo(vo.getDocNo());

                        trainingPlanImplDao.insertTrainingPlanImplementationLearner(hrinfo);
                    }
                }
                
                List<TrainingPlanImplHrVO> chargeList = vo.getChargeList();
                for(TrainingPlanImplHrVO cvo : chargeList) {
                	cvo.setDocType(targetType);
                	cvo.setDocNo(vo.getDocNo());
                	cvo.setWriteYear(vo.getWriteYear());
                	trainingPlanImplDao.insertTrainingPlanImplHr(cvo);
                }
                
            } else {
                // HSE 업무분장 수정
                TrainingPlanImplVO resultVO = trainingPlanImplDao.getTrainingPlanImplById(vo);
                if (resultVO == null)
                    return null;
                resultVO = (TrainingPlanImplVO) SpUtils.objectMap(vo, resultVO);

                trainingPlanImplDao.updateTrainingPlanImpl(resultVO);

                // 인원 매핑 수정
                BaseMapVO searchVo2 = new BaseMapVO(targetType, key);
                List<BaseMapVO> hrList = hrDao.getHrMap(searchVo2);

                // 1. 안전보건 교육 계획에 대한 인원 전체 use_yn = 'N'
                BaseMapVO param = new BaseMapVO(targetType, key, null, vo.getUpdatedBy());
                hrDao.deleteHrMap(param);

                trainingPlanImplDao.deleteTrainingPlanImplementationLearner(vo);
                for (HrVO hrinfo : vo.getHrList()) {
                    hrinfo.setWriteYear(vo.getWriteYear());
                    hrinfo.setDocType(vo.getDocType());
                    hrinfo.setDocNo(vo.getDocNo());

                    trainingPlanImplDao.insertTrainingPlanImplementationLearner(hrinfo);
                }

                TrainingPlanImplVO reportVo = new TrainingPlanImplVO();
                reportVo.setWriteYear(resultVO.getWriteYear());
                reportVo.setDocNo(resultVO.getDocNo());
                reportVo.setDocType(resultVO.getDocType());

                trainingPlanImplDao.deleteTrainingPlanImplHr(reportVo);
                List<TrainingPlanImplHrVO> chargeList = vo.getChargeList();
                for(TrainingPlanImplHrVO cvo : chargeList) {
                	cvo.setDocType(targetType);
                	cvo.setDocNo(vo.getDocNo());
                	cvo.setWriteYear(vo.getWriteYear());
                    trainingPlanImplDao.insertTrainingPlanImplHr(cvo);
                }
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteTrainingPlanImpl(TrainingPlanImplVO reqVo) throws Exception {
        BaseVO vo = trainingPlanImplDao.getTrainingPlanImplById(reqVo);
        trainingPlanImplDao.deleteTrainingPlanImpl(reqVo);
        return vo;
    }

    public void deleteTrainingPlanImpls(List<TrainingPlanImplVO> list) throws Exception {
        for (TrainingPlanImplVO tmp : list) {
            trainingPlanImplDao.deleteTrainingPlanImpl(tmp);
        }
    }

    // 일괄출력 파라미터 조회
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<String> checkedList = trainingPlanImplDao.getAllReport(spSearchVO);
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
            jaspers = getTrainingPlanImplReport(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    public List<JasperPrint> getTrainingPlanImplReport(HttpServletRequest request, HttpServletResponse response,
            SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        StringBuilder fileNm = new StringBuilder("(" + spSearchVO.getWriteYear() + ")" + "안전보건교육 실시계획서");
        String title = "안전보건교육 실시계획서";

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
            reportVO.setJrxmlPath("report/support/trainingPlanImpl/trainingPlanImpl.jrxml");
            reportVO.setType(spSearchVO.getType());
            spSearchVO.setDocNo(docNo);
            TrainingPlanImplVO detailList = trainingPlanImplDao.getTrainingPlanImplDetail(spSearchVO);

            if(spSearchVO.getCheckedList().size() == 1){
                fileNm.append("_").append(detailList.getTitle());
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("logo", logo2);
            params.put("page", spSearchVO.getPage());
            params.put("subPage", spSearchVO.getSubPage());
            params.put("localPage", spSearchVO.getLocalPage());
            params.put("title", title);

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
            params.put("effectivenessEtcYn", (detailList.getEffectivenessEtc()== null || detailList.getEffectivenessEtc().equals("")) ? "N" : "Y");
            params.put("remark", detailList.getRemark());

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 인원
            BaseMapVO searchVo2 = new BaseMapVO(detailList.getDocType(),
                    detailList.getWriteYear() + detailList.getDocNo());
            searchVo2.setCompId(detailList.getCompId());   
            List<HrVO> hrlist = trainingPlanImplDao.getTrainingPlanImplementationLearner(detailList);
            List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
            if (!hrlist.isEmpty()) {
                for (HrVO hr : hrlist) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("hrNm", hr.getHrNm());
                    data.put("orgnNm", hr.getOrgnNm());
                    data.put("jbrpNm", hr.getJbrpNm());
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
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm.toString());

        return reportList;
    }



    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveCurrentAndPreviousYear(List<TrainingPlanImplVO> voList) throws Exception {
        if(voList == null)
            return null;
        String newWriteYear = SpUtils.addNumToString(voList.get(0).getWriteYear(),1);
        voList.forEach(vo -> {
            vo.setWriteYear(newWriteYear);
            vo.setCmd("I");
            vo.setTrainingDate(null);
            vo.setUpdatedBy(null);
            vo.setUpdatedAt(null);
            vo.setUseYn(YesNo.valueOf("Y"));
        });
        return saveTrainingPlanImpl(voList);
    }

}
