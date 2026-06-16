package kr.co.igns.business.evaluation.service;

import kr.co.igns.business.evaluation.dao.postgres.MonitoringManageDAO;
import kr.co.igns.business.evaluation.model.*;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.igns.business.participation.service.HsePolicyService;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoringManageService {
    private final MonitoringManageDAO monitoringManageDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final FileService fileService;
    private final HsePolicyService hsePolicyService;
    private String targetType = "ER";

    public List<EvaluationReportVO> getEvaluationReportList(SpSearchVO searchVo) throws Exception {
        List<EvaluationReportVO> voList = monitoringManageDao.getEvaluationReportList(searchVo);
        return voList;
    }

    //#region 평가 결과서
    public EvaluationReportVO getEvaluationReportDetail(SpSearchVO searchVo) throws Exception {
        List<EvaluationReportVO> vo = monitoringManageDao.getEvaluationReportList(searchVo);

        if(vo != null && !vo.isEmpty()) {
            //평가 항목 조회
            List<EvaluationReportDetailVO> detail = monitoringManageDao.getEvaluationReportDetail(searchVo);
            vo.get(0).setDetailList(detail);
            return vo.get(0);
        }
        return null;
    }

    public EvaluationReportVO getEvaluationReportDetailByChecked(SpSearchVO searchVo) throws Exception {
        List<EvaluationReportVO> vo = monitoringManageDao.getEvaluationReportList(searchVo);
        List<EvaluationReportDetailVO> detail = new ArrayList<>();
        for(SpSearchVO spVo : searchVo.getCheckedObjList()){
            detail.add(monitoringManageDao.getEvaluationReportDetailByChecked(spVo));
        }
        vo.get(0).setDetailList(detail);
        return vo.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEvaluationReport(EvaluationReportVO vo) throws Exception {
        if(vo.getCmd().equals("I")){
            monitoringManageDao.insertEvaluationReport(vo);
        }else if(vo.getCmd().equals("U")){
            monitoringManageDao.updateEvaluationReport(vo);
        }
        if(vo.getDetailList() != null && !vo.getDetailList().isEmpty()){
            for(EvaluationReportDetailVO item : vo.getDetailList()){
                if(item.getCmd().equals("D")){
                    monitoringManageDao.deleteEvaluationReportDetail2(item);
                }
            }
        }
        if(vo.getDetailList() != null && !vo.getDetailList().isEmpty()){
            for(EvaluationReportDetailVO item : vo.getDetailList()){
                if(item.getCmd().equals("I")){
                    EvaluationReportDetailVO chkVo = monitoringManageDao.getEvaluationChecklistCriteria(item);
                    item.setWriteYear(vo.getWriteYear());
                    item.setDocNo(vo.getDocNo());
                    item.setDocType(targetType);
                    item.setCreatedBy(vo.getCreatedBy());
                    item.setUseYn(vo.getUseYn());
                    if(chkVo != null){
                        item.setEvaluationCriteria(chkVo.getEvaluationCriteria());
                        item.setEvaluationMethod(chkVo.getEvaluationMethod());
                    }
                    monitoringManageDao.insertEvaluationReportDetail(item);
                }else if(item.getCmd().equals("U")){
                    EvaluationReportDetailVO resultVO2 = monitoringManageDao.getEvaluationReportDetailById(item);

                    if(resultVO2 == null){
                        return null;
                    }
                    item.setContents(item.getContents() == null ? "" : item.getContents());
                    item.setRemark(item.getRemark() == null ? "" : item.getRemark());
                    resultVO2 = (EvaluationReportDetailVO) SpUtils.objectMap(item, resultVO2);
                    monitoringManageDao.updateEvaluationReportDetail(resultVO2);
                }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteEvaluationReportList(List<EvaluationReportVO> list) throws Exception {
        for (EvaluationReportVO tmp : list) {
            if(tmp.getDetailList() == null || tmp.getDetailList().isEmpty()){
                monitoringManageDao.deleteEvaluationReport(tmp);
            }else {
                for(EvaluationReportDetailVO vo : tmp.getDetailList()){
                    monitoringManageDao.deleteEvaluationReportDetail(vo);
                }
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteEvaluationReportListDetail(EvaluationReportVO reqVo) throws Exception {
        if(reqVo.getDetailList() != null && !reqVo.getDetailList().isEmpty()){
            for(EvaluationReportDetailVO vo : reqVo.getDetailList()){
                monitoringManageDao.deleteEvaluationReportDetail(vo);
            }
        }


    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int localPage = spSearchVO.getLocalPage();

        // 안전보건정보 데이터 조회
        List<String> checkedList = monitoringManageDao.getAllReport(spSearchVO);

        // 안전보건정보 리포트
        if(!checkedList.isEmpty()) {
            spSearchVO.setDocType("ER");
            spSearchVO.setPrintAll(true);
            spSearchVO.setCheckedList(checkedList);
            String month = spSearchVO.getSearchText();
            if(Integer.parseInt(spSearchVO.getSearchText())<10) {
                month = String.valueOf(Integer.valueOf(month));
            }
            spSearchVO.setExtra1(month+ "월");

            // -- 모니터링, 성과측정 및 평가 결과서 -------------------------------------------------
            List<JasperPrint> first = getEvaluationReport(request,response,spSearchVO);
            reportList.addAll(first);
            for(JasperPrint jasper : first) {
                page+= jasper.getPages().size();
                localPage+= jasper.getPages().size();
            }
            spSearchVO.setPage(page);
            spSearchVO.setLocalPage(localPage);

            // -- 성과평가표 --------------------------------------------------------------------
            List<JasperPrint> third = getEvaluationPerformanceReport(request,response,spSearchVO);
            reportList.addAll(third);
            for(JasperPrint jasper : third) {
                page+= jasper.getPages().size();
                localPage+= jasper.getPages().size();
            }
            spSearchVO.setPage(page);
            spSearchVO.setLocalPage(localPage);
        }

        return reportList;
    }

    public List<JasperPrint> getEvaluationReport(HttpServletRequest request, HttpServletResponse response,
                                                 SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "모니터링, 성과측정 및 평가 결과서" ;
        String title = "모니터링, 성과측정 및 평가 결과서";

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

            for(String docNo : spSearchVO.getCheckedList()) {
                InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                //데이터 조회
                EvaluationReportVO vo = new EvaluationReportVO();
                spSearchVO.setDocNo(docNo);
                if(spSearchVO.getCheckedObjList() == null || spSearchVO.getCheckedObjList().isEmpty()){//모니터링, 성과측정 및 평가 결과서 화면 또는 상세에서 detail을 선택하지 않고 출력했을 경우, 사용여부가 N인 Detail 데이터는 제외
                    vo = getEvaluationReportDetail(spSearchVO);
                    vo.setDetailList(vo.getDetailList().stream().filter(item -> "Y".equals(item.getUseYn())).collect(Collectors.toList()));
                }else{ //상세 페이지에서 detail을 선택했을 경우
                    vo = getEvaluationReportDetailByChecked(spSearchVO);
                }
                if(spSearchVO.getCheckedList().size() == 1) {
                    fileNm = fileNm + "_" + vo.getEvaluationTarget();
                }

                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/evaluation/monitoringManage/evaluation.jrxml");
                reportVO.setType("pdf");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("logo", logo2);

                // 날짜 형식 변환
                if(vo.getEvaluationDt() != null && !vo.getEvaluationDt().equals("")) {
                    vo.setEvaluationDt(DateUtils.formatDate(vo.getEvaluationDt()));
                }
                params.put("evaluationDt", vo.getEvaluationDt());
                params.put("evaluationTarget", vo.getEvaluationTarget());

                //결재
                vo.setDocType("ER");    //서명 중복때문에 평가결과서에 대한 type을 지정해서 서명정보 조회
                List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
                params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

                params.put("page", page);
                params.put("subPage", subPage);
                params.put("localPage", localPage);

                // 모니터링, 성과측정 및 평가 결과
                List<Map<String, Object>> datasource = new ArrayList<>();

                //모니터링, 성과측정, 평가 결과서가 없을때, 빈값 생성
                if(vo.getDetailList().isEmpty()){
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("evaluationNm", "");
                    datasource.add(data);
                }
                if (vo.getDetailList() != null) {
                    for (EvaluationReportDetailVO compliance : vo.getDetailList()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("compId", vo.getCompId());
                        data.put("evaluationId", compliance.getEvaluationId());
                        data.put("evaluationNm", compliance.getEvaluationNm());
                        data.put("evaluationItemId", compliance.getEvaluationItemId());
                        data.put("evaluationItemNm", compliance.getEvaluationItemNm());
                        data.put("resultY", compliance.getResultYn() == null ? "" : compliance.getResultYn().equals("Y") ? "Y" : "");
                        data.put("resultN", compliance.getResultYn() == null ? "" : compliance.getResultYn().equals("N") ? "N" : "");
                        data.put("achievementRate", compliance.getAchievementRate());
                        data.put("contents", compliance.getContents());
                        data.put("remark", compliance.getRemark());
                        datasource.add(data);
                    }
                }

                params.put("dataList", new JRBeanCollectionDataSource(datasource));
                reportVO.setParameter(params);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                reportList.add(JasperReport);
                page = page + JasperReport.getPages().size();
                localPage = localPage + JasperReport.getPages().size();
            }



        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }

    //#endregion

    //#region 평가항목 관리 팝업

    //예시불러오기
    public List<EvaluationChecklistVO> getBaseEvaluationChecklist(SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getBaseEvaluationChecklist(searchVo);
        for (EvaluationChecklistVO vo : voList ) {
            List<EvaluationChecklistVO> itemList= monitoringManageDao.getBaseEvaluationChecklistDetail(vo);
            vo.setItemList(itemList);
        }
        return voList;
    }

    public List<EvaluationChecklistVO> getEvaluationChecklist(SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getEvaluationChecklist(searchVo);
        for (EvaluationChecklistVO vo : voList ) {
            List<EvaluationChecklistVO> itemList= monitoringManageDao.getEvaluationChecklistDetail(vo);
            for(EvaluationChecklistVO bvo : itemList) {
                bvo.setEvaluationId(vo.getEvaluationId());
                bvo.setEvaluationNm(vo.getEvaluationNm());
            }
            vo.setDetailList(itemList);
        }
        return voList;
    }

    public List<EvaluationChecklistVO> getEvaluationChecklistDataSet(SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getEvaluationChecklistDataSet(searchVo);
        for (EvaluationChecklistVO vo : voList ) {
            List<EvaluationChecklistVO> detailList= monitoringManageDao.getEvaluationChecklistDetailDataSet(vo);
            for(EvaluationChecklistVO bvo : detailList) {
                bvo.setEvaluationId(vo.getEvaluationId());
                bvo.setEvaluationNm(vo.getEvaluationNm());
            }
            vo.setDetailList(detailList);
        }
        return voList;
    }

    public List<EvaluationChecklistVO> getEvaluationChecklistByUseYn(SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getEvaluationChecklistByUseYn(searchVo);
        for (EvaluationChecklistVO vo : voList ) {
            List<EvaluationChecklistVO> itemList= monitoringManageDao.getEvaluationChecklistDetail(vo);
            vo.setDetailList(itemList);
        }
        return voList;
    }

    public List<EvaluationChecklistVO> getEvaluationChecklistDetail(EvaluationChecklistVO vo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getEvaluationChecklistDetail(vo);
        return voList;
    }

    public List<EvaluationChecklistVO> getEvaluationChecklistDetailBySearchText(SpSearchVO vo) throws Exception {
        List<EvaluationChecklistVO> voList = monitoringManageDao.getEvaluationChecklistDetailBySearchText(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEvaluationChecklist(EvaluationChecklistVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 평가항목 추가
            monitoringManageDao.insertEvaluationChecklist(vo);

            // 평가항목 사항 추가
            for(EvaluationChecklistVO detail : vo.getItemList()){
                detail.setEvaluationId(vo.getEvaluationId());
                monitoringManageDao.insertEvaluationChecklistDetail(detail);
            }
        } else {
            // 평가항목 수정
            EvaluationChecklistVO resultVO = monitoringManageDao.getEvaluationChecklistById(vo);
            if (resultVO == null)
                return null;
            resultVO = (EvaluationChecklistVO) SpUtils.objectMap(vo, resultVO);

            monitoringManageDao.updateEvaluationChecklist(resultVO);

            // 평가항목 사항 추가/수정
            for(EvaluationChecklistVO detail : vo.getItemList()){
                if (detail.getCmd().equals("I")) {
                    // 평가사항 추가
                    monitoringManageDao.insertEvaluationChecklistDetail(detail);
                } else {
                    // 평가사항 수정
                    EvaluationChecklistVO resultVO2 = monitoringManageDao.getEvaluationChecklistDetailById(detail);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (EvaluationChecklistVO) SpUtils.objectMap(detail, resultVO2);

                    monitoringManageDao.updateEvaluationChecklistDetail(resultVO2);
                }
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEvaluationChecklistByPopup(List<EvaluationChecklistVO> voList) throws Exception {
        for(EvaluationChecklistVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if (vo.getCmd().equals("I")) {
                // 평가항목 추가
                monitoringManageDao.insertEvaluationChecklist(vo);

                // 평가항목 사항 추가
                for(EvaluationChecklistVO detail : vo.getDetailList()){
                    detail.setCompId(SecurityUtil.getCurrentCompId());
                    detail.setEvaluationId(vo.getEvaluationId());
                    monitoringManageDao.insertEvaluationChecklistDetail(detail);
                }
            } else {
                // 평가항목 수정
                EvaluationChecklistVO resultVO = monitoringManageDao.getEvaluationChecklistById(vo);
                if (resultVO == null)
                    return null;
                resultVO = (EvaluationChecklistVO) SpUtils.objectMap(vo, resultVO);

                monitoringManageDao.updateEvaluationChecklist(resultVO);

                // 평가항목 사항 추가/수정
                for(EvaluationChecklistVO detail : vo.getDetailList()){
                    if (detail.getCmd().equals("I")) {
                        // 평가사항 추가
                        monitoringManageDao.insertEvaluationChecklistDetail(detail);
                    } else {
                        // 평가사항 수정
                        EvaluationChecklistVO resultVO2 = monitoringManageDao.getEvaluationChecklistDetailById(detail);
                        if (resultVO2 == null)
                            return null;
                        resultVO2 = (EvaluationChecklistVO) SpUtils.objectMap(detail, resultVO2);

                        monitoringManageDao.updateEvaluationChecklistDetail(resultVO2);
                    }
                }
            }
        }


        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteEvaluationChecklist(List<EvaluationChecklistVO> voList) throws Exception {
        for(EvaluationChecklistVO vo : voList){
            monitoringManageDao.deleteEvaluationChecklist(vo);
            if(vo.getDetailList() != null && !vo.getDetailList().isEmpty()){
                monitoringManageDao.deleteEvaluationChecklistDetailData(vo);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteEvaluationChecklistDetail(List<EvaluationChecklistVO> voList) throws Exception {
        for(EvaluationChecklistVO vo : voList){
            monitoringManageDao.deleteEvaluationChecklistDetail(vo);
        }
    }
    //#endregion

    //#region 준수평가표
    public List<LegalComplianceEvaluationVO> getLegalComplianceEvaluationList(SpSearchVO searchVo) throws Exception {
        //법규 준수평가표 리스트 조회
        List<LegalComplianceEvaluationVO> voList = monitoringManageDao.getLegalComplianceEvaluationList(searchVo);

        return voList;
    }


    public List<LegalComplianceEvaluationDetailVO> getLegalComplianceEvaluationDetailList(SpSearchVO searchVo) throws Exception {
        //법규 준수평가표 상세 리스트 조회
        List<LegalComplianceEvaluationDetailVO> detailVOList = monitoringManageDao.getLegalComplianceEvaluationDetailList(searchVo);

        // 조직 조회
        for(LegalComplianceEvaluationDetailVO detail : detailVOList){
            List<LegalComplianceEvaluationDetailOrgnVO> orgnList = monitoringManageDao.getLegalComplianceEvaluationDetailOrgnList(detail);
            if(!orgnList.isEmpty()){
                detail.setLegalComplianceDetailOrgnList(orgnList);
            }
        }

        return detailVOList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveLegalComplianceEvaluation(LegalComplianceEvaluationVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 준수평가표 추가
            monitoringManageDao.insertLegalComplianceEvaluation(vo);
        } else {
            // 준수평가표 수정
            monitoringManageDao.updateLegalComplianceEvaluation(vo);
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveLegalComplianceEvaluationDetail(Map<String,List<MultipartFile>> files, List<LegalComplianceEvaluationDetailVO> voList) throws Exception {
        for(LegalComplianceEvaluationDetailVO vo : voList) {
            String docSeqKey = vo.getDocSeq(); // 신규 추가 시에는 임시 번호 저장 되어있음
            String key = vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq();
            if (vo.getCmd().equals("I")) {
                // 준수평가표 상세 추가
                monitoringManageDao.insertLegalComplianceEvaluationDetail(vo);
                key = vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq();
            } else {
                // 준수평가표 상세 수정
                monitoringManageDao.updateLegalComplianceEvaluationDetail(vo);
                fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), key, SecurityUtil.getCurrentCompId());
            }
            //파일 저장
            if (files != null && files.containsKey(docSeqKey)) {
                fileService.saveFile(files.get(docSeqKey), vo.getDocType(), key, SecurityUtil.getCurrentCompId());
            }

            LegalComplianceEvaluationDetailOrgnVO orgnVO = new LegalComplianceEvaluationDetailOrgnVO();
            orgnVO.setWriteYear(vo.getWriteYear());
            orgnVO.setDocType(vo.getDocType());
            orgnVO.setDocNo(vo.getDocNo());
            orgnVO.setDocSeq(vo.getDocSeq());

            // 준수평가표 상세 조직 저장
            saveLegalComplianceEvaluationDetailOrgn(orgnVO, vo.getLegalComplianceDetailOrgnList());
        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveLegalComplianceEvaluationDetailOrgn(LegalComplianceEvaluationDetailOrgnVO vo, List<LegalComplianceEvaluationDetailOrgnVO> voList) throws Exception {

        // 기존 조직 삭제
        monitoringManageDao.deleteLegalComplianceEvaluationDetailOrgn(vo);

        if(voList != null){
            for(LegalComplianceEvaluationDetailOrgnVO orgnVO : voList) {
                orgnVO.setOrgnId(orgnVO.getId());
                orgnVO.setOrgnNm(orgnVO.getNm());
                orgnVO.setWriteYear(vo.getWriteYear());
                orgnVO.setDocType(vo.getDocType());
                orgnVO.setDocNo(vo.getDocNo());
                orgnVO.setDocSeq(vo.getDocSeq());

                // 준수평가표 상세 조직 추가
                monitoringManageDao.insertLegalComplianceEvaluationDetailOrgn(orgnVO);
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalComplianceEvaluation(List<LegalComplianceEvaluationVO> list) throws Exception {
        for (LegalComplianceEvaluationVO tmp : list) {
            monitoringManageDao.deleteLegalComplianceEvaluation(tmp);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteLegalComplianceEvaluationDetail(List<LegalComplianceEvaluationDetailVO> list) throws Exception {
        for (LegalComplianceEvaluationDetailVO tmp : list) {
            monitoringManageDao.deleteLegalComplianceEvaluationDetail(tmp);
        }
    }

    public List<JasperPrint> getAllEvaluationComplianceReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        // 법규준수평가표 데이터 조회
        List<String> evaluationCheckedList = monitoringManageDao.getAllEvaluationComplianceReport(spSearchVO);
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int localPage = spSearchVO.getLocalPage();

        // -- 준수평가표 --------------------------------------------------------------------
        if(!evaluationCheckedList.isEmpty()){
            spSearchVO.setDocType("LCE");
            spSearchVO.setPrintAll(true);  // 통합 출력 모드 유지
            spSearchVO.setCheckedList(evaluationCheckedList);

            List<JasperPrint> second = getEvaluationComplianceReport(request, response, spSearchVO);
            reportList.addAll(second);

            for(JasperPrint jasper : second) {
                page+= jasper.getPages().size();
                localPage+= jasper.getPages().size();
            }
        }

        return reportList;
    }

    public List<JasperPrint> getEvaluationComplianceReport(HttpServletRequest request, HttpServletResponse response,
                                                           SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "법규준수평가표" ;
        String title = "법규준수평가표";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for(String docNo : spSearchVO.getCheckedList()) {
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            //데이터 조회
            SpSearchVO spSearchVOCopy = spSearchVO;
            spSearchVOCopy.setDocNo(docNo);

            List<LegalComplianceEvaluationVO> legalComplianceEvaluation = getLegalComplianceEvaluationList(spSearchVOCopy);

            // 통합 출력 시 checkedObjList가 비어있으므로 생성
            if (spSearchVO.getCheckedObjList() == null || spSearchVO.getCheckedObjList().isEmpty()) {
                List<SpSearchVO> checkedObjList = new ArrayList<>();
                List<String> checkedList = new ArrayList<>();

                for (LegalComplianceEvaluationVO data : legalComplianceEvaluation) {
                    // 프론트와 동일한 구조로 checkedObjList 생성
                    SpSearchVO checkedObj = new SpSearchVO();
                    checkedObj.setWriteYear(data.getWriteYear());
                    checkedObj.setDocNo(data.getDocNo());
                    checkedObj.setDocType(data.getDocType());
                    checkedObj.setDocSeq(data.getDocSeq());
                    checkedObj.setCompId(data.getCompId());

                    checkedObjList.add(checkedObj);
                    checkedList.add(data.getDocNo());
                }

                // 생성된 리스트를 spSearchVO에 설정
                spSearchVO.setCheckedObjList(checkedObjList);
            }

            if(!legalComplianceEvaluation.isEmpty()){
                LegalComplianceEvaluationVO vo = legalComplianceEvaluation.get(0);

                if(spSearchVO.getCheckedList().size() == 1) {
                    fileNm = "(" + spSearchVO.getWriteYear() + ")" + "법규준수평가표_" + vo.getLegalEvaluationNm();
                }

                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/evaluation/monitoringManage/compliance.jrxml");
                reportVO.setType("pdf");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("logo", logo2);
                params.put("title", title);

                params.put("docNo", vo.getWriteYear() + "-" + vo.getDocType() + "-" + docNo);
                String dt = DateUtils.formatDate(vo.getLegalEvaluationDt());
                params.put("legalEvaluationDt", dt);
                params.put("legalEvaluationNm", vo.getLegalEvaluationNm());
                params.put("contentHeader", "법규 준수평가표");
                //결재
                vo.setDocType("LCE");    //서명 중복때문에 준수평가표에 대한 type을 지정해서 서명정보 조회
                List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
                params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

                params.put("page", page);
                params.put("subPage", subPage);
                params.put("localPage", localPage);

                // 디테일 리스트
                List<Map<String, Object>> datasource = new ArrayList<>();

                // 해당 docNo를 가진 List만 추출
                List<SpSearchVO> matchedSearchVOList = spSearchVOCopy.getCheckedObjList().stream().filter(item -> docNo.equals(item.getDocNo())).collect(Collectors.toList());

                int i = 0;
                for (SpSearchVO matchedSearchVO : matchedSearchVOList) {
                    // 추출한 데이터를 통해 디테일 리스트 조회
                    List<LegalComplianceEvaluationDetailVO> detailVOList = monitoringManageDao.getLegalComplianceEvaluationDetailList(matchedSearchVO);

                    for (LegalComplianceEvaluationDetailVO detailVO : detailVOList) {
                        // 해당 디테일 리스트의 조직 데이터를 조회
                        List<LegalComplianceEvaluationDetailOrgnVO> orgnList = monitoringManageDao.getLegalComplianceEvaluationDetailOrgnList(detailVO);

                        // 조직 데이터 설정
                        String orgnNm = orgnList.stream().map(LegalComplianceEvaluationDetailOrgnVO::getOrgnNm).filter(Objects::nonNull).collect(Collectors.joining("\n"));

                        Map<String, Object> data = new HashMap<>();
                        data.put("no", ++i);
                        data.put("legalNm", detailVO.getLegalNm());
                        data.put("legalArticleNm", detailVO.getLegalArticleNm());
                        data.put("orgnNm", orgnNm);
                        data.put("contents", detailVO.getContents());
                        data.put("legalReviewN", detailVO.getLegalReviewYn() == null ? "" :
                                detailVO.getLegalReviewYn().getKey().equals("No") ? "V" : "");
                        data.put("legalReviewY", detailVO.getLegalReviewYn() == null ? "" :
                                detailVO.getLegalReviewYn().getKey().equals("Yes") ? "V" : "");
                        data.put("remark", detailVO.getRemark());
                        datasource.add(data);
                    }
                }

                params.put("dataList", new JRBeanCollectionDataSource(datasource));
                reportVO.setParameter(params);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                reportList.add(JasperReport);
                page = page + JasperReport.getPages().size();
                localPage = localPage + JasperReport.getPages().size();
            }

        }

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }

    //#endregion

    //#region 성과평가표

    public List<EvaluationReportDetailVO> getEvaluationReportPerformance(SpSearchVO searchVo) throws Exception {
        //성과평가표 조회
        return monitoringManageDao.getEvaluationReportDetail(searchVo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEvaluationReportPerformance(List<EvaluationReportDetailVO> voList) throws Exception {
        // 성과평가 수정
        for (EvaluationReportDetailVO detail : voList) {
            monitoringManageDao.updateEvaluationReportPerformance(detail);
        }
        return voList.get(0);
    }

    public List<JasperPrint> getEvaluationPerformanceReport(HttpServletRequest request, HttpServletResponse response,
            SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "성과평가표" ;
        String title = "성과평가표";

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for(String docNo : spSearchVO.getCheckedList()) {
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            //데이터 조회
            EvaluationReportVO vo = new EvaluationReportVO();
            spSearchVO.setDocNo(docNo);
            if(spSearchVO.getCheckedObjList() == null || spSearchVO.getCheckedObjList().isEmpty()){//모니터링, 성과측정 및 평가 결과서 화면 또는 상세에서 detail을 선택하지 않고 출력했을 경우, 사용여부가 N인 Detail 데이터는 제외
                vo = getEvaluationReportDetail(spSearchVO);
                vo.setDetailList(vo.getDetailList().stream().filter(item -> "Y".equals(item.getUseYn())).collect(Collectors.toList()));
            }else{ //상세 페이지에서 detail을 선택했을 경우
                vo = getEvaluationReportDetailByChecked(spSearchVO);
            }
            if(spSearchVO.getCheckedList().size() == 1) {
                fileNm = fileNm + "_" + vo.getEvaluationTarget();
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/evaluation/monitoringManage/performance.jrxml");
            reportVO.setType("pdf");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("logo", logo2);

            // 날짜 형식 변환
            if(vo.getEvaluationDt() != null && !vo.getEvaluationDt().equals("")) {
                vo.setEvaluationDt(DateUtils.formatDate(vo.getEvaluationDt()));
            }
            params.put("evaluationDt", vo.getEvaluationDt());
            params.put("evaluationTarget", vo.getEvaluationTarget());

            //결재
            vo.setDocType("ERP");    //서명 중복때문에 평가결과서에 대한 type을 지정해서 서명정보 조회
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 모니터링, 성과측정 및 평가 결과
            List<Map<String, Object>> datasource = new ArrayList<>();

            //모니터링, 성과측정, 평가 결과서가 없을때, 빈값 생성
            if(vo.getDetailList().isEmpty()){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("evaluationNm", "");
                datasource.add(data);
            }
            if (vo.getDetailList() != null) {
                for (EvaluationReportDetailVO compliance : vo.getDetailList()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("compId", vo.getCompId());
                    data.put("evaluationId", compliance.getEvaluationId());
                    data.put("evaluationNm", compliance.getEvaluationNm());
                    data.put("evaluationItemId", compliance.getEvaluationItemId());
                    data.put("evaluationItemNm", compliance.getEvaluationItemNm());
                    data.put("result", compliance.getResultYn() == null ? "" : compliance.getResultYn().equals("N") ? "부적합" : "적합");
                    data.put("evaluationCriteria", compliance.getEvaluationCriteria());
                    data.put("evaluationMethod", compliance.getEvaluationMethod());
                    data.put("remark", compliance.getRemark());
                    datasource.add(data);
                }
            }

            params.put("dataList", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }



        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }

    public SpSearchVO getMenuKeyInfo(SpSearchVO searchVo) throws Exception {
        SpSearchVO vo = new SpSearchVO();
        if(searchVo.getType().equals("FUNC") && searchVo.getDocType().equals("PLC")){ //안전보건경영 방침 상세
            List<HsePolicyVO> voList = hsePolicyService.getConfirmedHsePolicyList(searchVo);
            if(voList != null && !voList.isEmpty()){
                vo.setWriteYear(voList.get(0).getWriteYear());
                vo.setDocType(voList.get(0).getDocType());
                vo.setDocNo(voList.get(0).getDocNo());
            }else{
               vo = null;
            }
        }
        return vo;

    }

    //#endregion
}
