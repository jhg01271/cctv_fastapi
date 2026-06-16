package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.planning.dao.postgres.RiskAssessmentPlanDAO;
import kr.co.igns.business.planning.model.*;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.planning.dao.postgres.ImplementationOfRiskAseessmentOrocessDAO;
import kr.co.igns.business.planning.dao.postgres.ImprovementAndImplementationDAO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ImplementationOfRiskAseessmentService {
    private final ImprovementAndImplementationDAO dao;
    private final ImplementationOfRiskAseessmentOrocessDAO riskAssessmentImplementationDAO;
    private final RiskAssessmentPlanDAO riskAssessmentPlanDAO;
    private final FileService fileService;
    private final NasFileService nasFileService;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final CompDAO compDao;
    private final ClientDAO clientDao;
    //private final   service;

    /**
     * 계획 목록 조회
     *
     * @param searchVo
     * @return
     * @throws Exception
     */
    public List<RiskAssessmentPlanVO> getRiskImplList(SpSearchVO searchVo) throws Exception {
        List<RiskAssessmentPlanVO> voList = riskAssessmentImplementationDAO.selectRiskImplList(searchVo);
        return voList;
    }

    /**
     * 이행 상세 조회
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ClassificationOfHazardsVO> getRiskAssessmentDetail(ClassificationOfHazardsVO vo) throws Exception {
        List<ClassificationOfHazardsVO> result = riskAssessmentImplementationDAO.selectHazardsDetail(vo);
        return result;
    }

    public List<ClassificationOfHazardsVO> getRiskAssessmentDetailAll(ClassificationOfHazardsVO vo) throws Exception {
        List<String> test = Arrays.asList(vo.getProcessId().split(";"));
        vo.setProcessIds(test);
        List<ClassificationOfHazardsVO> result = riskAssessmentImplementationDAO.selectHazardsDetailAll(vo);
        return result;
    }

    public List<ClassificationOfHazardsVO> getRiskAssessmentDetailPopupAll(ClassificationOfHazardsVO vo) throws Exception {
        List<ClassificationOfHazardsVO> result = riskAssessmentImplementationDAO.selectHazardsDetailPopupAll(vo);
        return result;
    }

    public List<ImplementationOfRiskAseessmentVO> selectReferenceRiskImplList(SpSearchVO vo) throws Exception {
        List<ImplementationOfRiskAseessmentVO> result = riskAssessmentImplementationDAO.selectReferenceRiskImplList(vo);
        return result;
    }

    public List<ImplementationOfRiskAseessmentVO> checkRemoveRiskImplList(SpSearchVO vo) throws Exception {
        List<ImplementationOfRiskAseessmentVO> result = riskAssessmentImplementationDAO.checkRemoveRiskImplList(vo);
        return result;
    }

    /**
     * 이행, 감소대책, 담당자 저장 및 수정
     *
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public List<ClassificationOfHazardsVO> saveRiskImpl(List<ClassificationOfHazardsVO> reqVo, RiskAssessmentPlanVO planReqVO) throws Exception {

        if(planReqVO.getCpemDocNo() == null){
            planReqVO.setCpemWriteYear(planReqVO.getCpemConfirmWriteYear());
            planReqVO.setCpemDocNo(planReqVO.getCpemConfirmDocNo());
            // plan 테이블의 cpem_write_year, cpem_doc_no update
            riskAssessmentPlanDAO.updateRiskAssessmentPlanCpemData(planReqVO);
        }

        for (ClassificationOfHazardsVO param : reqVo) {
            if (param.getImplRiskAssList().size() > 0) {
                for (int j = 0; j < param.getImplRiskAssList().size(); j++) {
                    // 1-1. hazardsType 데이터 세팅
                    param.getImplRiskAssList().get(j).setHazardsType(param.getHazardsType());
                    // 1-2. 이행 데이터 저장
                    param.getImplRiskAssList().get(j).setCreatedBy(param.getCreatedBy());
                    param.getImplRiskAssList().get(j).setUpdatedBy(param.getUpdatedBy());
                    riskAssessmentImplementationDAO.insertRiskAssImpl(param.getImplRiskAssList().get(j));
                    List<ImplementationOfRiskAseessmentReductionVO> reductionList = param.getImplRiskAssList().get(j).getImplementReduList();
                    // 2-1. 감소대책 데이터가 있다면
                    if (reductionList.size() > 0) {
                        // 2-1. 감소대책 데이터 저장
                        for (ImplementationOfRiskAseessmentReductionVO red : reductionList) {
                            red.setCreatedBy(param.getCreatedBy());
                            red.setUpdatedBy(param.getUpdatedBy());
                            if(red.getExpectedDate() != null){
                                red.setExpectedDate(DateUtils.formatDateForDB(red.getExpectedDate()));
                            }
                            if(red.getCompletedDate() != null){
                                red.setCompletedDate(DateUtils.formatDateForDB(red.getCompletedDate()));
                            }
                            red.setDocSeq(param.getImplRiskAssList().get(j).getDocSeq());
                            riskAssessmentImplementationDAO.insertRiskAssImplReduction(red);
                            // 3-1. 감소대책 담당자 전체 삭제
                            riskAssessmentImplementationDAO.deleteRiskAssImplReductionHr(red);
                            // 3-2. 감소대책 담당자 데이터가 있다면
                            if (red.getHrList().size() > 0) {
                                // 3-2. 감소대책 담당자 저장
                                riskAssessmentImplementationDAO.insertRiskAssImplReductionHr(red);
                            }
                        }
                    }
                }
            }
        }

        // 저장 성공 시 success 반환
        return reqVo;
    }

    /**
     * 위험성평가 계획 이전 정보 선택 팝업 - 이행 저장 및 수정
     *
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public List<ClassificationOfHazardsVO> saveMainRiskImpl(List<ClassificationOfHazardsVO> reqVo) throws Exception {
        for (ClassificationOfHazardsVO param : reqVo) {
            if (param.getImplRiskAssList().size() > 0) {
                for (int j = 0; j < param.getImplRiskAssList().size(); j++) {
                    // 1-1. hazardsType 데이터 세팅
                    param.getImplRiskAssList().get(j).setHazardsType(param.getHazardsType());
                    // 1-2. 이행 데이터 저장
                    param.getImplRiskAssList().get(j).setCreatedBy(param.getCreatedBy());
                    param.getImplRiskAssList().get(j).setUpdatedBy(param.getUpdatedBy());
                    riskAssessmentImplementationDAO.insertRiskAssImpl(param.getImplRiskAssList().get(j));
                }
            }
        }

        // 저장 성공 시 success 반환
        return reqVo;
    }

    /**
     * 개선 이미지 등록 및 수정
     *
     * @param prevVo
     * @param afterVo
     * @param prevFiles
     * @param afterFiles
     * @param deleteList
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public String saveRiskImplImage(List<ImplementationOfRiskAseessmentReductionVO> prevVo,
                                    List<ImplementationOfRiskAseessmentReductionVO> afterVo,
                                    List<MultipartFile> prevFiles, List<MultipartFile> afterFiles, List<String> deleteList) throws Exception {
        // 1. 파일 삭제
        for (String key : deleteList) {
            fileService.deleteFile(deleteList, "RAP", key, SecurityUtil.getCurrentCompId());
        }

        // 2. 개선 전 이미지가 있는 경우
        if (prevVo != null) {
            for (int i = 0; i < prevVo.size(); i++) {
                ImplementationOfRiskAseessmentReductionVO vo = prevVo.get(i);
                List<MultipartFile> prevFile = new ArrayList<MultipartFile>();
                prevFile.add(prevFiles.get(i));
                String targetId = vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq() + vo.getDocDetailSeq() + "A";
                fileService.saveFile(prevFile, "RAP", targetId, SecurityUtil.getCurrentCompId());
            }
        }

        // 3. 개선 후 이미지가 있는 경우
        if (afterVo != null) {
            for (int j = 0; j < afterVo.size(); j++) {
                ImplementationOfRiskAseessmentReductionVO vo = afterVo.get(j);
                List<MultipartFile> afterFile = new ArrayList<MultipartFile>();
                afterFile.add(afterFiles.get(j));
                String targetId = vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq() + vo.getDocDetailSeq() + "B";
                fileService.saveFile(afterFile, "RAP", targetId, SecurityUtil.getCurrentCompId());
            }
        }

        return "success";
    }

    /**
     * 위험성평가 이행 인트로 페이지
     *
     * @param vo
     * @return
     */
    public ReportVO setIntroReport(ClassificationOfHazardsVO vo) {
        CompVO cpVO = new CompVO();
        cpVO.setClntId(vo.getClntId());
        cpVO.setCompId(vo.getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();

        ReportVO introReportVO = new ReportVO();
        introReportVO.setJrxmlPath("report/utils/basicFront_reverse.jrxml");
        introReportVO.setType("pdf");
        Map<String, Object> introParams = new HashMap<>();
        introParams.put("title", "위험성평가 이행");
        introParams.put("subTitle", "사업장명 : " + compNm);
        introParams.put("extra1", vo.getNm());
        InputStream introClntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        introParams.put("logo", introClntLogo);
        SpSearchVO searchVO = new SpSearchVO();
        introParams.put("page", vo.getPage());
        introParams.put("subPage", vo.getSubPage());
        introParams.put("localPage", vo.getLocalPage());
        introReportVO.setParameter(introParams);

        return introReportVO;
    }


    // 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<RiskAssessmentPlanVO> planList = getRiskImplList(spSearchVO);
        List<RiskAssessmentPlanVO> validPlanList = new ArrayList<>();
        for (RiskAssessmentPlanVO plan : planList) {
            spSearchVO.setDocNo(plan.getDocNo());
            spSearchVO.setSearchText(plan.getCpemDocNo());
            validPlanList.addAll(getRiskImplList(spSearchVO)); // 상세 데이터
        }
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<JasperPrint> jaspers = new ArrayList<>();
        for (RiskAssessmentPlanVO plan : validPlanList) {
            List<ClassificationOfHazardsVO> reportParam = new ArrayList<>();
            for (ClassificationProcessEquipMsdsAnalysisVO prcs : plan.getClassPrcsList()) {
                for (ImplementationOfRiskAseessmentProcessVO work : prcs.getProcessList()) {
                    if (Integer.valueOf(work.getRegisterCount()) > 0) {
                        // 등록된 항목만
                        ClassificationOfHazardsVO param = new ClassificationOfHazardsVO();
                        param.setWriteYear(spSearchVO.getWriteYear());
                        param.setCompId(spSearchVO.getCompId());
                        param.setDocNo(plan.getDocNo());
                        param.setPrcsWorkId(Integer.valueOf(work.getPrcsWorkId()));
                        param.setPrcsWorkNm(work.getWorkContent());
                        param.setProcessId(prcs.getProcessId());
                        param.setProcessNm(prcs.getProcessNm());
                        param.setRevNo(prcs.getRevNo());
                        reportParam.add(param);
                    }
                }
            }
            if (!reportParam.isEmpty()) {
                reportParam.get(0).setPrintAll(true);
                reportParam.get(0).setType(spSearchVO.getType());
                reportParam.get(0).setNm(plan.getPlanNm());
                reportParam.get(0).setPage(page);
                reportParam.get(0).setSubPage(subPage);
                reportParam.get(0).setLocalPage(localPage);
                spSearchVO.setPrintAll(true);
                jaspers = getReport(request, response, reportParam);
                reportList.addAll(jaspers);
                for (JasperPrint jasper : jaspers) {
                    page += jasper.getPages().size();
                    localPage += jasper.getPages().size();
                }
            }
        }
        return reportList;
    }

    /**
     * 위험성평가표 (공정별로 작성) 출력
     *
     * @param request
     * @param response
     * @param vo
     * @throws Exception
     */

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, List<ClassificationOfHazardsVO> vo) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        /***********************/
        /****** 공통 정보 조회 ******/
        /***********************/
        // 공통 정보 조회
        vo.get(0).setClntId(SecurityUtil.getCurrentClntId());
        vo.get(0).setCompId(SecurityUtil.getCurrentCompId());

        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(vo.get(0).getClntId());
        String clntNm = cVo.getClntNm();

        CompVO cpVO = new CompVO();
        cpVO.setClntId(vo.get(0).getClntId());
        cpVO.setCompId(vo.get(0).getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();
        // 작성년도
        String writeYear = vo.get(0).getWriteYear();

        // 인트로 페이지
        ReportVO introReportVO = setIntroReport(vo.get(0));

        int page = vo.get(0).getPage();
        int subPage = vo.get(0).getSubPage();
        int localPage = vo.get(0).getLocalPage();

        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);
        page += JasperIntroReport.getPages().size();
        localPage += JasperIntroReport.getPages().size();

        String fileNm = "";
        List<SpSearchVO> listVo = new ArrayList<SpSearchVO>();    // 완료 감소대책 파라미터

        // 중복을 피하기 위해 docNo+docSeq 조합 저장
        Set<String> uniqueDocSeqSet = new HashSet<>();

        /***********************/
        /****** 전체 데이터 조회 ******/
        /***********************/

        // 모든 공정 데이터를 한 번에 조회
        List<Map<String, Object>> allResultData = riskAssessmentImplementationDAO.selectAllHazardsDetailReport(vo.get(0));

        if (allResultData == null || allResultData.isEmpty()) {
            return jasperPrintList;
        }

        // process_id와 prcs_work_id로 그룹핑
        Map<String, List<Map<String, Object>>> groupedByProcess = allResultData.stream().collect(Collectors.groupingBy(data ->
            data.get("process_id") + "_" + data.get("prcs_work_id")
        ));

        /**
         * 위험성평가표 (공정별로 작성) 출력물 관련
         */
        for (ClassificationOfHazardsVO hazards : vo) {
            String processKey = hazards.getProcessId() + "_" + hazards.getPrcsWorkId();
            List<Map<String, Object>> processData = groupedByProcess.get(processKey);

            if (processData == null || processData.isEmpty()) {
                continue;
            }

            // 각 hazards에 대해 필요한 정보 설정
            hazards.setIsAllPrint(true);

            List<Map<String, Object>> gridData = new ArrayList<>();

            // 첫 번째 결과에서 파일명 설정
            if (fileNm.isEmpty()) {
                fileNm = "(" + writeYear + ")" + "위험성평가표_";
                fileNm += processData.get(0).get("plan_nm");
            }

            ReportVO reportVO = new ReportVO();

            for (Map<String, Object> stringObjectMap : processData) {
                if (stringObjectMap.get("resultcnt") != null) {
                    String resultCnt = stringObjectMap.get("resultcnt").toString();
                    stringObjectMap.put("resultCnt", (Object) resultCnt);
                }
                if (stringObjectMap.get("doc_seq") != null) {
                    gridData.add(stringObjectMap);
                    String uniqueKey = stringObjectMap.get("write_year").toString() + stringObjectMap.get("doc_no").toString() + stringObjectMap.get("doc_seq").toString();
                    if (stringObjectMap.get("completed_date") != null && !uniqueDocSeqSet.contains(uniqueKey)) {
                        SpSearchVO search = new SpSearchVO();
                        search.setWriteYear(hazards.getWriteYear());
                        search.setDocType("RAP");
                        search.setDocNo(hazards.getDocNo());
                        search.setDocSeq(stringObjectMap.get("doc_seq").toString());
                        listVo.add(search);
                        uniqueDocSeqSet.add(uniqueKey);
                    }
                }

                // 출력물 양식 지정(상중하 기법인 경우 3a 출력물로 지정)
                if(stringObjectMap.get("risk_assessment_standards") != null){
                    if(stringObjectMap.get("risk_assessment_standards").equals("3a")){
                        reportVO.setJrxmlPath("report/planning/riskAssessment/implementationOfRiskAssessment3a.jrxml");
                    }else{
                        reportVO.setJrxmlPath("report/planning/riskAssessment/implementationOfRiskAssessment.jrxml");
                    }
                }
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("year", writeYear);

            //고객사 명 입력
            params.put("clntNm", clntNm);
            //사업장 명 입력
            params.put("compNm", compNm);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(vo.get(0).getClntId());
            params.put("logo", logo);
            params.put("title", "위험성평가표 (공정별로 작성)");

            // 공정대분류
            params.put("processNm", hazards.getProcessNm());
            // 세부분류
            params.put("prcsWorkNm", hazards.getPrcsWorkNm());

            // 출력 파일 명 설정
            reportVO.setFileName(fileNm);
            // 출력 생성용 Japser 파일 위치
            if(reportVO.getJrxmlPath() == null){
                reportVO.setJrxmlPath("report/planning/riskAssessment/implementationOfRiskAssessment.jrxml");
            }

            // 출력 파일 형식 지정
            reportVO.setType("pdf");
            params.put("gridData", new JRBeanCollectionDataSource(gridData));

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);

            // page
            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
        }

        /**
         * 중요위험성등록부/세부실행계획서(감소대책 수립 및 실행) 출력물 관련
         */
        JasperPrint jasperPrint2 = getReportDetail(request, response, vo.get(0), page, subPage, localPage);
        jasperPrintList.add(jasperPrint2);
        page = page + jasperPrint2.getPages().size();
        localPage = localPage + jasperPrint2.getPages().size();

        /**
         * 개선 실행 및 결과 출력물 관련
         */
        if (listVo.size() > 0) {
            List<JasperPrint> jasperPrint3 = getReportDetail2(request, response, listVo, vo.get(0).getClntId(), page, subPage, localPage);
            for (JasperPrint jas : jasperPrint3) {
                jasperPrintList.add(jas);

                page = page + jas.getPages().size();
                localPage = localPage + jas.getPages().size();
            }
        }

        // 빈 페이지 제거
        jasperPrintList = jasperPrintList.stream()
                .filter(jasperPrint -> jasperPrint != null && jasperPrint.getPages().size() > 0)
                .collect(Collectors.toList());

        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (vo.get(0).isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, vo.get(0).getType(), fileNm);
        }
        return jasperPrintList;
    }

    public List<JasperPrint> getReportAll(HttpServletRequest request, HttpServletResponse response, List<ClassificationOfHazardsVO> voList) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        /***********************/
        /****** 공통 정보 조회 ******/
        /***********************/
        // 공통 정보 조회
        voList.get(0).setClntId(SecurityUtil.getCurrentClntId());
        voList.get(0).setCompId(SecurityUtil.getCurrentCompId());

        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(voList.get(0).getClntId());
        String clntNm = cVo.getClntNm();

        CompVO cpVO = new CompVO();
        cpVO.setClntId(voList.get(0).getClntId());
        cpVO.setCompId(voList.get(0).getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();
        // 작성년도
        String writeYear = voList.get(0).getWriteYear();

        // 인트로 페이지
        ReportVO introReportVO = setIntroReport(voList.get(0));

        int page = voList.get(0).getPage();
        int subPage = voList.get(0).getSubPage();
        int localPage = voList.get(0).getLocalPage();

        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
        jasperPrintList.add(JasperIntroReport);
        page += JasperIntroReport.getPages().size();
        localPage += JasperIntroReport.getPages().size();

        String fileNm = "";
        List<SpSearchVO> listVo = new ArrayList<SpSearchVO>();    // 완료 감소대책 파라미터

        // 중복을 피하기 위해 docNo+docSeq 조합 저장
        Set<String> uniqueDocSeqSet = new HashSet<>();

        /***********************/
        /****** 전체 데이터 조회 ******/
        /***********************/

        // 모든 공정 데이터를 한 번에 조회

        List<ClassificationOfHazardsVO> detailVoList = new ArrayList<>();

        for (ClassificationOfHazardsVO vo : voList) {
            List<Map<String, Object>> allResultData = riskAssessmentImplementationDAO.selectAllHazardsDetailReport(vo);
            if (allResultData == null || allResultData.isEmpty()){
                continue;
            }

            // process_id + prcs_work_id 기준으로 그룹핑
            Map<String, List<Map<String, Object>>> groupedByProcess = allResultData.stream()
                    .collect(Collectors.groupingBy(
                            data -> data.get("process_id") + "_" + data.get("prcs_work_id")
                    ));

            String processKey = vo.getProcessId() + "_" + vo.getPrcsWorkId();
            List<Map<String, Object>> processData = groupedByProcess.get(processKey);
            if (processData == null || processData.isEmpty()) continue;

            // vo 단위로 gridData 생성 (초기화)
            List<Map<String, Object>> gridData = new ArrayList<>();
            vo.setIsAllPrint(true);

            // 파일명 설정 (처음 한 번만)
            if (fileNm.isEmpty() && processData.get(0).get("plan_nm") != null) {
                fileNm = "(" + writeYear + ")위험성평가표_" + processData.get(0).get("plan_nm").toString();
            }

            // doc_seq, completed_date 체크 및 gridData 추가
            for (Map<String, Object> map : processData) {
                if (map.get("resultcnt") != null) map.put("resultCnt", map.get("resultcnt").toString());

                if (map.get("doc_seq") != null) {
                    gridData.add(map);

                    String uniqueKey = map.get("write_year").toString() + map.get("doc_no").toString() + map.get("doc_seq").toString();
                    if (map.get("completed_date") != null && !uniqueDocSeqSet.contains(uniqueKey)) {
                        SpSearchVO search = new SpSearchVO();
                        search.setWriteYear(vo.getWriteYear());
                        search.setDocType("RAP");
                        search.setDocNo(vo.getDocNo());
                        search.setDocSeq(map.get("doc_seq").toString());
                        listVo.add(search);
                        uniqueDocSeqSet.add(uniqueKey);
                    }
                }
            }

            // JasperPrint 생성
            Map<String, Object> params = new HashMap<>();
            params.put("year", writeYear);
            params.put("clntNm", clntNm);
            params.put("compNm", compNm);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            InputStream logo = utilsService.getClntLogo(vo.getClntId());
            params.put("logo", logo);
            params.put("title", "위험성평가표 (공정별로 작성)");
            params.put("processNm", vo.getProcessNm());
            params.put("prcsWorkNm", vo.getPrcsWorkNm());
            params.put("gridData", new JRBeanCollectionDataSource(gridData));

            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setType("pdf");
            reportVO.setJrxmlPath("report/planning/riskAssessment/implementationOfRiskAssessment.jrxml");
            reportVO.setParameter(params);

            JasperPrint jasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(jasperReport);

            page += jasperReport.getPages().size();
            localPage += jasperReport.getPages().size();

            // detailVoList에 vo 복사
            ClassificationOfHazardsVO detailVo = new ClassificationOfHazardsVO();
            detailVo.setWriteYear(vo.getWriteYear());
            detailVo.setDocNo(vo.getDocNo());
            detailVo.setProcessId(vo.getProcessId());
            detailVo.setPrcsWorkId(vo.getPrcsWorkId());
            detailVo.setProcessNm(vo.getProcessNm());
            detailVo.setPrcsWorkNm(vo.getPrcsWorkNm());
            detailVo.setClntId(vo.getClntId());
            detailVo.setIsAllPrint(vo.getIsAllPrint());
            detailVoList.add(detailVo);
        }

// detailVoList 순서대로 JasperPrint 생성
        for (ClassificationOfHazardsVO vo : detailVoList) {
            JasperPrint jasperPrint2 = getReportDetail(request, response, vo, page, subPage, localPage);
            jasperPrintList.add(jasperPrint2);
            page += jasperPrint2.getPages().size();
            localPage += jasperPrint2.getPages().size();
        }

        /**
         * 개선 실행 및 결과 출력물 관련
         */
        if (listVo.size() > 0) {
            List<JasperPrint> jasperPrint3 = getReportDetail2(request, response, listVo, voList.get(0).getClntId(), page, subPage, localPage);
            for (JasperPrint jas : jasperPrint3) {
                jasperPrintList.add(jas);

                page = page + jas.getPages().size();
                localPage = localPage + jas.getPages().size();
            }
        }

        // 빈 페이지 제거
        jasperPrintList = jasperPrintList.stream()
                .filter(jasperPrint -> jasperPrint != null && jasperPrint.getPages().size() > 0)
                .collect(Collectors.toList());

        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (voList.get(0).isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, voList.get(0).getType(), fileNm);
        }
        return jasperPrintList;
    }


    /**
     * 중요위험성등록부/세부실행계획서(감소대책 수립 및 실행) 출력
     *
     * @param request
     * @param response
     * @param gridData
     * @throws Exception
     */
    public JasperPrint getReportDetail(HttpServletRequest request, HttpServletResponse response, ClassificationOfHazardsVO vo, int page, int subPage, int localPage) throws Exception {
        vo.setIsAllPrint(false);
        List<Map<String, Object>> result = riskAssessmentImplementationDAO.selectHazardsDetailReport(vo);
        List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();
        ReportVO reportVO = new ReportVO();

        for (Map<String, Object> stringObjectMap : result) {
            if (stringObjectMap.get("doc_seq") != null) {
                gridData.add(stringObjectMap);
            }
            ;
        }
        gridData = gridData.stream()
                .filter(map -> map.get("reduction_measures") != null) // 감소대책이 있는경우에만
                .collect(Collectors.toList());
        // write_year, doc_no, doc_seq, doc_seq_detail 별로 hrNm ㅇㅇㅇ, ㅇㅇㅇ, ㅇㅇㅇ 으로 나오게 함
        Map<String, String> groupedResult = gridData.stream()
                .filter(data -> data.get("hr_nm") != null) // null 값 필터링
                .collect(Collectors.groupingBy(
                        // 그룹화 기준: write_year, doc_no, doc_seq, doc_seq_detail
                        data -> data.get("write_year") + "-" + data.get("doc_no") + "-" + data.get("doc_seq") + "-" + data.get("doc_seq_detail"),
                        Collectors.mapping(data -> (String) data.get("hr_nm"), // hr_nm 추출
                                Collectors.toSet())) // Set을 사용하여 중복 제거
                )
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.join(", ", entry.getValue()) // Set을 쉼표로 구분하여 문자열로 합침
                ));
        int cnt = 0;
        for (Map<String, Object> data : gridData) {
            cnt++;
            if (data.get("reduction_measures") != null) {
                // 감소대책이 있는 항목만
                if (data.get("resultcnt") != null) {
                    String resultCnt = data.get("resultcnt").toString();
                    data.put("resultCnt", (Object) resultCnt);
                }
                String key = data.get("write_year") + "-" + data.get("doc_no") + "-" + data.get("doc_seq") + "-" + data.get("doc_seq_detail");
                // 그룹화된 hr_nm을 해당 Map의 hr_nm에 넣기
                if (groupedResult.containsKey(key)) {
                    data.put("hrNm", groupedResult.get(key));
                } else {
                    data.put("hrNm", "");
                }
            }

            if(data.get("completed_date") != null){
                data.put("completed_date", DateUtils.formatDate(data.get("completed_date").toString()));
            }
            if(data.get("expected_date") != null){
                data.put("expected_date", DateUtils.formatDate(data.get("expected_date").toString()));
            }

            // 출력물 양식 지정(상중하 기법인 경우 3a 출력물로 지정)
            if(data.get("risk_assessment_standards") != null){
                if(data.get("risk_assessment_standards").equals("3a")){
                    reportVO.setJrxmlPath("report/planning/riskAssessment/riskRegister3a.jrxml");
                }else{
                    reportVO.setJrxmlPath("report/planning/riskAssessment/riskRegister.jrxml");
                }
            }
        }

        if(reportVO.getJrxmlPath() == null){
            reportVO.setJrxmlPath("report/planning/riskAssessment/riskRegister.jrxml");
        }

        // 출력  파일 형식 지정
        reportVO.setType("pdf");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("year", vo.getWriteYear());

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);
//		 Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(vo.getClntId());
        params.put("logo", logo);
        params.put("title", "중요위험성등록부/세부실행계획서(감소대책 수립 및 실행)");
        params.put("reductionGrid", new JRBeanCollectionDataSource(gridData));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        page = page + JasperReport.getPages().size();
        localPage = page + JasperReport.getPages().size();
        return JasperReport;
    }

    /**
     * 개선 실행 및 결과 출력물
     *
     * @param request
     * @param response
     * @param listVo
     * @return
     * @throws Exception
     */
    public List<JasperPrint> getReportDetail2(HttpServletRequest request, HttpServletResponse response,
                                              List<SpSearchVO> listVo, String clntId, int page, int subPage, int localPage) throws Exception {
        SpSearchVO vo = listVo.get(0);
        List<JasperPrint> reportList = new ArrayList<>();
        for (SpSearchVO spSearchVO : listVo) {
            List<ImprovementAndImplementationVO> searchData = dao.searchReport(spSearchVO);
            for (ImprovementAndImplementationVO searchDatum : searchData) {
                InputStream logo = utilsService.getClntLogo(clntId);
                ReportVO reportVO = new ReportVO();
                Map<String, Object> params = new HashMap<String, Object>();

                // 출력물 양식 지정(상중하 기법인 경우 3a 출력물로 지정)
                if(searchDatum.getRiskAssessmentStandards() != null){
                    if(searchDatum.getRiskAssessmentStandards().equals("3a")){
                        reportVO.setJrxmlPath("report/planning/improvementAndImplementation/improvementAndImplementation3a.jrxml");
                    }else{
                        reportVO.setJrxmlPath("report/planning/improvementAndImplementation/improvementAndImplementation.jrxml");
                    }
                }else{
                    reportVO.setJrxmlPath("report/planning/improvementAndImplementation/improvementAndImplementation.jrxml");
                }

                params.put("page", page);
                params.put("subPage", subPage);
                params.put("localPage", localPage);
                params.put("logo", logo);
                params.put("processNm", searchDatum.getProcessNm());
                params.put("hazardsFactor", searchDatum.getHazardsFactor());
                params.put("frequencyScore", searchDatum.getFrequencyScore());
                params.put("impactScore", searchDatum.getImpactScore());
                params.put("riskScore", searchDatum.getRiskScore());
                params.put("afterFrequencyScore", searchDatum.getAfterFrequencyScore());
                params.put("afterImpactScore", searchDatum.getAfterImpactScore());
                params.put("afterRiskScore", searchDatum.getAfterRiskScore());
                params.put("implContent", searchDatum.getImplContent());
                params.put("reductionMeasures", searchDatum.getReductionMeasures());
                params.put("workDetailHrs", searchDatum.getWorkDetailHrs());
                params.put("completedDate", DateUtils.formatDate(searchDatum.getCompletedDate()));
                if (searchDatum.getPrevFileId() != null) {
                    params.put("prevImg", nasFileService.getNasFileInputStream(searchDatum.getPrevFileId()));
                } else {
                    params.put("prevImg", null);
                }
                if (searchDatum.getAfterFileId() != null) {
                    params.put("afterImg", nasFileService.getNasFileInputStream(searchDatum.getAfterFileId()));
                } else {
                    params.put("afterImg", null);
                }
                reportVO.setParameter(params);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                reportList.add(JasperReport);
                page += JasperReport.getPages().size();
                localPage += JasperReport.getPages().size();
            }
        }

        return reportList;
    }

    /**
     * 근로자 확인 팝업 - 근로자 일괄 삭제
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public UtilsVO deleteWorkerApprovalInfoAll(UtilsVO vo) throws Exception {
        riskAssessmentImplementationDAO.deleteWorkerApprovalInfoAll(vo);

        return vo;
    }
}
