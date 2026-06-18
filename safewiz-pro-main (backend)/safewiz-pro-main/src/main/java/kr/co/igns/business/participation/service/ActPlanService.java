package kr.co.igns.business.participation.service;

import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.participation.dao.postgres.ActPlanDAO;
import kr.co.igns.business.participation.model.*;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.dao.postgres.OrganizationDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
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
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActPlanService {
    private final ActPlanDAO actPlanDao;
    private final OrganizationDAO organizationDao;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final FileService fileService;
    private final UtilsDAO utilsDao;

    private final String targetType = "OBJ";

    public List<ActPlanVO> getActPlanMaster(SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> voList = actPlanDao.getActPlanMaster(searchVo);
        return voList;
    }

    //region 전사 목표/추진 목표 및 분기별 계획
    public List<ActPlanVO> getActPlan(SpSearchVO searchVo) throws Exception {
        searchVo.setWriteYear(searchVo.getWriteYear());
        List<ActPlanVO> masterData = actPlanDao.getActPlanMaster(searchVo);
        if (!masterData.isEmpty()) {
            searchVo.setDocType(masterData.get(0).getDocType());
            searchVo.setDocNo(masterData.get(0).getDocNo());
        }
        List<ActPlanVO> voList = actPlanDao.getActPlan(searchVo);

        for (ActPlanVO vo : voList) {
            //조직 매핑 조회(OBJ)
            BaseMapVO searchVo2 = new BaseMapVO(vo.getDocType(), vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq());
            List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(searchVo2);
            vo.setOrgnList(orgnlist);
            String performanceDiv;

            if (vo.getPerformanceRepeatNm() != null && !vo.getPerformanceRepeatNm().equals("")) {
                performanceDiv = String.valueOf(vo.getPerformanceCnt()) + "건 / " + vo.getPerformanceRepeatNm() + " / " + vo.getPerformanceTypeNm();
            } else if (vo.getPerformanceCnt() != 0) {
                performanceDiv = String.valueOf(vo.getPerformanceCnt()) + "건" + " / " + vo.getPerformanceTypeNm();
            } else if (!"0000".equals(vo.getPerformanceType()) && vo.getPerformanceCnt() == 0) {
                performanceDiv = vo.getPerformanceTypeNm();
            } else {
                performanceDiv = vo.getPerformanceCustom();
            }

            // performanceDiv 값을 ActPlanVO에 설정 (어떤 필드에 저장할지 확인)
            vo.setPerformanceDiv(performanceDiv);
        }

        return voList;
    }

    public ActPlanVO getPartnerDetail(SpSearchVO searchVo) throws Exception {
        ActPlanVO voList = actPlanDao.getActPlanDetail(searchVo);

        //조직 매핑 조회
        BaseMapVO searchVo1 = new BaseMapVO(voList.getDocType(), voList.getWriteYear() + voList.getDocNo() + voList.getDocSeq());
        List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(searchVo1);
        voList.setOrgnList(orgnlist);

        return voList;
    }

    public List<ActPlanVO> getObjective(SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> voList = actPlanDao.getObjective(searchVo);

        return voList;
    }

    public List<ActPlanVO> getActionObjective(SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> voList = actPlanDao.getActionObjective(searchVo);

        return voList;
    }

    public List<ActPlanPerformanceVO> getActionObjectiveOrgnList(SpSearchVO searchVo) throws Exception {
        List<ActPlanPerformanceVO> voList = actPlanDao.getActionObjectiveOrgnList(searchVo);

        return voList;
    }

    public List<SafetyAndHealthObjectivesVO> getSafetyHealthObjOrgnList(SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = actPlanDao.getSafetyHealthObjOrgnList(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveActPlan(List<ActPlanVO> voList) throws Exception {
        boolean isDocNoUpdated = false; // 플래그 선언
        //master data가 없을 때 만들어줌
        if (voList.get(0).getDocNo() == null || voList.get(0).getDocNo().isEmpty()) {
            voList.get(0).setDocType(targetType);
            actPlanDao.insertActPlanMaster(voList.get(0));
            isDocNoUpdated = true; // 플래그 설정 - 최초 작성자를 위한 플래그
        }
        for (ActPlanVO vo : voList) {
            //데이터 세팅(Master 데이터가 새로 생성된 경우,)
            if (vo.getDocNo() == null || vo.getDocNo().isEmpty()) {
                vo.setDocNo(voList.get(0).getDocNo());
            }
            vo.setDocType(targetType);

            // 플래그를 기반으로 최초 작성자 데이터 삽입
            // ---- 2025.08.27 LJH ----
            // 최초 문서 생성 시 작성자 default로 넣지 않고, 사용자가 직접 설정하여 i-signature 컴포넌트 내부에서 자체적으로 실행되도록 함
            // ------------------------
//            if (isDocNoUpdated) {
//                UtilsVO signParam = new UtilsVO();
//                signParam.setTargetType(targetType);
//                signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
//                signParam.setHrId(SecurityUtil.getCurrentHrId());
//                signParam.setParam("writer");
//                signParam.setType("sign");
//                signParam.setUserId(vo.getCreatedBy());
//
//                // 최초 작성자 서명 데이터 입력
//                utilsDao.insertApprovalInfo(signParam);
//            }

            if (vo.getCmd().equals("I")) {
                //전사 목표 및 중점 추진사항 생성
                if (Objects.equals(vo.getNewYn(), "Y")) {
                    //전사 목표 ID 자동 부여 (년월일순번)
                    actPlanDao.insertObjective(vo);
                }

                //중점 추진사항 추가
                vo.setDocType(targetType);
                actPlanDao.insertActPlan(vo);


                //조직 매핑 생성
                for (String orgnId : vo.getOrgnIdList()) {
                    BaseMapVO orgnMapInfo = new BaseMapVO(targetType, vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq(), orgnId, vo.getCreatedBy());
                    organizationDao.addOrgnMap(orgnMapInfo);
                }


            } else {
                if (Objects.equals(vo.getNewYn(), "Y")) {
                    //전사 목표 수정
                    actPlanDao.updateObjective(vo);
                }

                //전사 목표 및 중점 추진사항 수정
                ActPlanVO actPlanVO = actPlanDao.getActPlanById(vo);
                if (actPlanVO == null)
                    return null;
                actPlanVO = (ActPlanVO) SpUtils.objectMap(vo, actPlanVO);

                actPlanDao.updateActPlan(actPlanVO);

                //조직 매핑 수정
                String id = vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq();
                BaseMapVO searchVo3 = new BaseMapVO(targetType, id);
                List<BaseMapVO> orgnList = organizationDao.getOrgnMap(searchVo3);

                // 1. 공정에 대한 조직 전체 use_yn = 'N'
                BaseMapVO param = new BaseMapVO(targetType, id, null, vo.getUpdatedBy());
                organizationDao.deleteOrgnMap(param);

                for (String orgnId : vo.getOrgnIdList()) {
                    Optional<BaseMapVO> matchingMap = orgnList.stream()
                            .filter(data -> data.getId().equals(orgnId))
                            .findFirst();

                    if (matchingMap.isPresent()) {
                        // 2. 공정에 해당 조직이 원래 있는 경우
                        param = matchingMap.get();
                        param.setUseYnByString("Y");
                        param.setUpdatedBy(vo.getUpdatedBy());
                        organizationDao.updateUseYnOrgnMap(param);
                    } else {
                        // 3. 공정에 해당 조직이 없는 경우
                        param.setId(orgnId);
                        param.setCreatedBy(vo.getUpdatedBy());
                        organizationDao.addOrgnMap(param);
                    }
                }

                SpSearchVO searchVO = new SpSearchVO();
                searchVO.setCompId(vo.getCompId());
                searchVO.setWriteYear(vo.getWriteYear());
                searchVO.setDocType(vo.getDocType());
                searchVO.setDocNo(vo.getDocNo());
                searchVO.setDocSeq(vo.getDocSeq());

                // 예산에 등록된 조직 리스트
                List<ActPlanPerformanceVO> actPlanPerformanceOrgnList = actPlanDao.getActionObjectiveOrgnList(searchVO);

                // 예산에 등록된 조직 리스트와 vo.getOrgnIdList() 비교해서 제거된 조직 리스트
                List<ActPlanPerformanceVO> deleteActPlanOrgnList = actPlanPerformanceOrgnList.stream()
                        .filter(actPlan -> !vo.getOrgnIdList().contains(actPlan.getOrgnId()))
                        .collect(Collectors.toList());

                // 담당조직이 제거 되는 경우 예산 제거
                for (ActPlanPerformanceVO actPlanPerformanceVO : deleteActPlanOrgnList) {
                    actPlanDao.deleteActionPerformanceByOrgnId(actPlanPerformanceVO);
                    actPlanDao.deleteActionPerformanceDetail(actPlanPerformanceVO);
                }
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteActPlan(ActPlanVO reqVo) throws Exception {
        BaseVO vo = actPlanDao.getActPlanById(reqVo);
        actPlanDao.deleteActPlan(reqVo);
        return vo;
    }

    public void deleteActPlans(List<ActPlanVO> list) throws Exception {
        for (ActPlanVO tmp : list) {
            actPlanDao.deleteActPlan(tmp);
        }
    }
    //endregion

    //region 추진 계획 상세 - 월별
    public List<ActPlanDetailVO> getActionMonthly(SpSearchVO searchVo) throws Exception {

        List<ActPlanVO> masterData = actPlanDao.getActPlanMaster(searchVo);
        if (masterData != null && !masterData.isEmpty()) {
            searchVo.setWriteYear(masterData.get(0).getWriteYear());
            searchVo.setDocType(masterData.get(0).getDocType());
            searchVo.setDocNo(masterData.get(0).getDocNo());

            List<ActPlanDetailVO> voList = actPlanDao.getActionMonthly(searchVo);
            // `detail_item_id` 기준으로 갯수를 계산하기 위한 Map
            Map<String, Long> detailItemCountMap = voList.stream()
                    .filter(vo -> vo.getDetailItemId() != null && YesNo.Y.equals(vo.getUseYn()))  // null 값 필터링
                    .collect(Collectors.groupingBy(ActPlanDetailVO::getDetailItemId, Collectors.counting()));

            for (ActPlanDetailVO vo : voList) {
                //조직 매핑 조회
                BaseMapVO searchVo2 = new BaseMapVO(vo.getDocTypeO(), vo.getWriteYearO() + vo.getDocNoO() + vo.getDocSeqO());
                List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(searchVo2);
                // `detailItemCnt` 값 할당
                vo.setDetailItemCnt(detailItemCountMap.getOrDefault(vo.getDetailItemId(), 0L).intValue());
                vo.setOrgnList(orgnlist);
            }
            return voList;
        }

        // masterData가 비어 있으면 빈 리스트 반환
        return Collections.emptyList();
    }

    public List<ActPlanDetailVO> getActionMonthlyDetail(SpSearchVO searchVo) throws Exception {
        List<ActPlanDetailVO> voList = actPlanDao.getActionMonthlyDetail(searchVo);

        //조직 매핑 조회
        for (ActPlanDetailVO vo : voList) {
            //조직 매핑 조회
            BaseMapVO searchVo2 = new BaseMapVO(vo.getDocType(), vo.getWriteYear() + vo.getDocNo() + vo.getDocSeq());
            List<BaseMapVO> orgnlist = organizationDao.getOrgnMapByUseYn(searchVo2);
            vo.setOrgnList(orgnlist);
        }

        return voList;
    }

    public List<ActPlanDetailVO> getDetailItem(SpSearchVO searchVo) throws Exception {
        List<ActPlanDetailVO> voList = actPlanDao.getDetailItem(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveActionDetailItem(ActPlanDetailVO vo) throws Exception {
        actPlanDao.updateActionDetailItem(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveActionMonthly(List<ActPlanDetailVO> voList) throws Exception {
        String detailItemId = null;
        //세부 항목이 신규일 경우, 세부 항목 추가
        if (voList.get(0).getDetailItemId() == null || voList.get(0).getDetailItemId().isEmpty()) {
            //세부 항목 ID 자동 부여 (년월일순번)
            voList.get(0).setCompId(SecurityUtil.getCurrentCompId());
            actPlanDao.insertDetailItem(voList.get(0));
        }
        detailItemId = voList.get(0).getDetailItemId();

        for (ActPlanDetailVO vo : voList) {
            vo.setDetailItemId(detailItemId);
            if (vo.getCmd().equals("I")) {
                //추진 계획(월별) 생성
                //추진 계획(월별) insert
                actPlanDao.insertActionMonthly(vo);
                
                // ---- 2025.08.29 LJH ----
                // 최초 문서 생성 시 작성자 default로 넣지 않고, 사용자가 직접 설정하여 i-signature 컴포넌트 내부에서 자체적으로 실행되도록 함
                // 아래 서명 처리 주석 
//                //서명
//                UtilsVO signParam = new UtilsVO();
//                signParam.setTargetType("OBJP");
//                signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
//                signParam.setHrId(SecurityUtil.getCurrentHrId());
//                signParam.setParam("writer");
//                signParam.setUserId(vo.getCreatedBy());
//                signParam.setCompId(SecurityUtil.getCurrentCompId());
//                signParam.setType("sign");
//
//                // 조회
//                List<UtilsVO> approvalInfoList = utilsDao.getApprovalInfo(signParam);
//                if (approvalInfoList == null || approvalInfoList.isEmpty()) {
//                    // 서명 조회 후 없으면 insert
//                    utilsDao.insertApprovalInfo(signParam);// 최초 작성자 서명 데이터 입력
//                }
            } else {
                actPlanDao.updateActionDetailItem(vo);
                //추진 계획(월별) 및 실적 수정
                ActPlanDetailVO actPlanDetailVO = actPlanDao.getActionMonthlyById(vo);
                if (actPlanDetailVO == null)
                    return null;
                actPlanDetailVO = (ActPlanDetailVO) SpUtils.objectMap(vo, actPlanDetailVO);
                actPlanDao.updateActionMonthly(actPlanDetailVO);
            }
        }
        return voList.get(0);
    }

    public void deleteActionMonthlyByDetailItem(List<ActPlanDetailVO> list) throws Exception {
        for (ActPlanDetailVO tmp : list) {
            actPlanDao.deleteActionMonthlyByDetailItem(tmp);
        }
    }

    public void deleteActionMonthlys(List<ActPlanDetailVO> list) throws Exception {
        for (ActPlanDetailVO tmp : list) {
            actPlanDao.deleteActionMonthly(tmp);
        }
    }

    //endregion

    //region 추진 실적, 예산 (증빙자료)

    public List<ActPlanPerformanceVO> getActionPerformance(SpSearchVO searchVo) throws Exception {
        List<ActPlanPerformanceVO> voList = actPlanDao.getActionPerformance(searchVo);
        for (ActPlanPerformanceVO vo : voList) {
            // 성과타입
            String performanceDiv;
            if (vo.getPerformanceRepeatNm() != null && !vo.getPerformanceRepeatNm().equals("")) {
                performanceDiv = String.valueOf(vo.getPerformanceCnt()) + "건 / " + vo.getPerformanceRepeatNm() + " / " + vo.getPerformanceTypeNm();
            } else if (vo.getPerformanceCnt() != 0) {
                performanceDiv = String.valueOf(vo.getPerformanceCnt()) + "건" + " / " + vo.getPerformanceTypeNm();
            } else if (!"0000".equals(vo.getPerformanceType()) && vo.getPerformanceCnt() == 0) {
                performanceDiv = vo.getPerformanceTypeNm();
            } else {
                performanceDiv = vo.getPerformanceCustom();
            }

            // performanceDiv 값을 ActPlanVO에 설정 (어떤 필드에 저장할지 확인)
            vo.setPerformanceDiv(performanceDiv);
        }
        return voList;
    }

    public ActPlanPerformanceVO getActionPerformanceDetail(SpSearchVO searchVo) throws Exception {
        ActPlanPerformanceVO voList = actPlanDao.getActionPerformanceDetail(searchVo);

        if (voList != null) {
            searchVo.setSearchText(voList.getDocPerformanceSeq());

            //실적증빙자료 조회
            List<ActPlanPerformanceDetailVO> performanceList = actPlanDao.getPerformanceList(searchVo);
            voList.setPerformanceList(performanceList);

            // 실적 담당자 매핑정보 조회
            List<HrVO> hrList = actPlanDao.getPerformanceHrList(voList);
            voList.setHrList(hrList);
        }

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveActionPerformance(ActPlanPerformanceVO vo, List<MultipartFile> insertFiles) throws Exception {
        if (vo.getDocPerformanceSeq() == null) {
            //추진 실적, 예산 생성
            vo.setDocType(targetType);
            actPlanDao.insertActionPerformance(vo);

            for (int i = 0; i < vo.getPerformanceList().size(); i++) {
                ActPlanPerformanceDetailVO voDetail = vo.getPerformanceList().get(i);
                //증빙 자료 생성
                voDetail.setDocPerformanceSeq(vo.getDocPerformanceSeq());
                actPlanDao.insertActionPerformanceDetail(voDetail);

                // 파일 저장
                if (insertFiles != null && i < insertFiles.size() && !insertFiles.get(i).getContentType().equals("application/octet-stream")) {
                    // 파일 저장을 위한 key 생성
                    String id = voDetail.getWriteYear() + voDetail.getDocNo() + voDetail.getDocSeq() + voDetail.getDocDetailSeq() + voDetail.getDocPerformanceSeq() + voDetail.getDocPerformanceDetailSeq();
                    //파일 저장
                    List<MultipartFile> singleFileList = insertFiles.subList(i, i + 1);
                    String fileId = fileService.saveFile(singleFileList, targetType, id, SecurityUtil.getCurrentCompId());
                    voDetail.setFileId(fileId);
                }
            }
            if (vo.getHrList() != null) {
                for (HrVO hr : vo.getHrList()) {
                    vo.setId(hr.getHrId());
                    actPlanDao.insertActionPerformanceHrList(vo);
                }
            }
        } else {
            //추진 실적, 예산 수정
            ActPlanPerformanceVO resultVo = actPlanDao.getActionPerformanceById(vo);
            if (resultVo == null)
                return null;
            resultVo = (ActPlanPerformanceVO) SpUtils.objectMap(vo, resultVo);
            actPlanDao.updateActionPerformance(resultVo);

            //실적 증빙 자료 생성/수정
            for (int i = 0; i < vo.getPerformanceList().size(); i++) {
                ActPlanPerformanceDetailVO voDetail = vo.getPerformanceList().get(i);
                if (voDetail.getCmd().equals("I")) {
                    //증빙 자료 생성
                    actPlanDao.insertActionPerformanceDetail(voDetail);
                    if (insertFiles != null && i < insertFiles.size() && !insertFiles.get(i).getContentType().equals("application/octet-stream")) {
                        //파일저장을 위한 key(targetId) 생성
                        String id = voDetail.getWriteYear() + voDetail.getDocNo() + voDetail.getDocSeq() + voDetail.getDocDetailSeq() + voDetail.getDocPerformanceSeq() + voDetail.getDocPerformanceDetailSeq();
                        // 파일 저장
                        List<MultipartFile> items = new ArrayList<MultipartFile>();
                        items.add(Objects.requireNonNull(insertFiles).get(i));
                        fileService.saveFile(items, targetType, id, SecurityUtil.getCurrentCompId());
                    }
                } else {
                    //증빙 자료 수정
                    ActPlanPerformanceDetailVO resultVo1 = actPlanDao.getActionPerformanceDetailById(voDetail);
                    if (resultVo1 == null)
                        return null;
                    resultVo1 = (ActPlanPerformanceDetailVO) SpUtils.objectMap(voDetail, resultVo1);

                    actPlanDao.updateActionPerformanceDetail(resultVo1);

                    //파일저장을 위한 key(targetId) 생성
                    String id = voDetail.getWriteYear() + voDetail.getDocNo() + voDetail.getDocSeq() + voDetail.getDocDetailSeq() + voDetail.getDocPerformanceSeq() + voDetail.getDocPerformanceDetailSeq();

                    //파일 추가/변경
                    String fileId = "";
                    fileService.deleteFile(voDetail.getDeleteFiles(), targetType, id, SecurityUtil.getCurrentCompId());
                    if (insertFiles != null && !insertFiles.get(i).getContentType().equals("application/octet-stream")) {
                        List<MultipartFile> singleFileList = insertFiles.subList(i, i + 1);
                        fileId = fileService.saveFile(singleFileList, targetType, id, SecurityUtil.getCurrentCompId());
                    }
                    voDetail.setFileId(fileId);   //수정된 파일의 file id를 front에 적용해줌
                }
            }
            actPlanDao.deleteActionPerformanceHrList(vo);
            if (vo.getHrList() != null) {
                for (HrVO hr : vo.getHrList()) {
                    vo.setId(hr.getHrId());
                    actPlanDao.insertActionPerformanceHrList(vo);
                }
            }
        }

        return vo;
    }

    public void deleteActionPerformance(List<ActPlanPerformanceVO> list) throws Exception {
        //조직 실적 삭제
        for (ActPlanPerformanceVO tmp : list) {
            //실적삭제
            actPlanDao.deleteActionPerformance(tmp);
            //증빙 자료 삭제
            actPlanDao.deleteActionPerformanceDetail(tmp);
        }
    }

    public void deleteActionPerformanceDetail(ActPlanPerformanceVO vo) throws Exception {
        //조직별 실적증빙데이터 삭제(use_yn 변경)
        for (ActPlanPerformanceDetailVO tmp : vo.getPerformanceList()) {
            actPlanDao.updateActionPerformanceDetailUseYn(tmp);
        }
    }

    //endregion
    public List<JasperPrint> getAllReportOBJ(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        spSearchVO.setCheckedPrint(false);
        reportList = getActionObjectiveReport(request, response, spSearchVO);
        return reportList;
    }

    public List<JasperPrint> getActionObjectiveReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        ReportVO reportVO = new ReportVO();

        List<ActPlanVO> resultList = getActPlan(spSearchVO);
        if (!spSearchVO.isCheckedPrint()) {
            resultList = resultList.stream()
                    .filter(plan -> plan.getUseYn().equals(YesNo.valueOf("Y")))
                    .collect(Collectors.toList());
        } else {
            resultList = resultList.stream()
                    .filter(plan -> spSearchVO.getCheckedList().contains(plan.getDocSeq()))
                    .collect(Collectors.toList());
        }
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "목표 및 중점추진사항";
        String title = "목표 및 중점추진사항";

        reportVO.setFileName(fileNm); // 다운로드되는 파일 명
        reportVO.setJrxmlPath("report/participation/objectivesAndActionPlan/objectivesAndActionPlan.jrxml"); // 레포트 파일 경로
        reportVO.setType(spSearchVO.getType()); // 출력물 타입

        // 표지 리포트
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        //파라미터 설정
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page); // 페이지
        params.put("subPage", subPage); // 서브페이지
        params.put("localPage", localPage); // 로컬페이지

        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo);
        params.put("title", title);

        List<Map<String, Object>> datasource = new ArrayList<>();

        if (resultList.isEmpty()) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("compId", "");
            datasource.add(data);
        }

        //sort 변수
        String curCompObjectiveId = null;
        String curDocNo = null;
        String curDocSeq = null;
        long seqCnt = 0;
        long seqCntSecond = 0;

        for (ActPlanVO result : resultList) {
            //결재
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(resultList.get(0));
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            Map<String, Object> data = new HashMap<String, Object>();
            if (curCompObjectiveId == null || !curCompObjectiveId.equals(result.getCompObjectiveId())) {
                //순서 맞춰주기 위해 순서값 생성 - 전사목표
                curCompObjectiveId = result.getCompObjectiveId();
                seqCnt++;
            }

            if (curDocNo == null || curDocSeq == null || !curDocNo.equals(result.getDocNo()) || !curDocSeq.equals(result.getDocSeq())) {
                //순서 맞춰주기 위해 순서값 생성 - 상세목표
                curDocNo = result.getDocNo();
                curDocSeq = result.getDocSeq();
                seqCntSecond++;
            }

            data.put("seq", seqCnt);
            data.put("seq2", seqCntSecond);
            data.put("compId", SecurityUtil.getCurrentCompId());
            data.put("companyObjective", result.getCompanyObjective());
            data.put("actionObjective", result.getActionObjective());
            data.put("actionSchedule1", String.valueOf(result.getActionSchedule1()));
            data.put("actionSchedule2", String.valueOf(result.getActionSchedule2()));
            data.put("actionSchedule3", String.valueOf(result.getActionSchedule3()));
            data.put("actionSchedule4", String.valueOf(result.getActionSchedule4()));
            String performanceDiv = "";
            if (result.getPerformanceRepeatNm() != null && !result.getPerformanceRepeatNm().equals("")) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건 / " + result.getPerformanceRepeatNm() + " / " + result.getPerformanceTypeNm();
            } else if (result.getPerformanceCnt() != 0) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건" + " / " + result.getPerformanceTypeNm();
            } else if (!"0000".equals(result.getPerformanceType()) && result.getPerformanceCnt() == 0) {
                performanceDiv = result.getPerformanceTypeNm();
            } else {
                performanceDiv = result.getPerformanceCustom();
            }
            data.put("performanceDiv", performanceDiv);
            data.put("budget", String.valueOf(NumberFormat.getInstance().format(result.getBudget() / 10000)));
            String orgnNm = result.getOrgnList().stream()
                    .map(BaseMapVO::getNm)  // 각 ActPlanVO에서 nm 필드를 가져옴
                    .collect(Collectors.joining(", "));  // 콤마로 구분하여 연결
            data.put("orgnNm", orgnNm);
            datasource.add(data);
        }

        params.put("datasource", new JRBeanCollectionDataSource(datasource));
        reportVO.setParameter(params);

        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page = page + JasperReport.getPages().size();
        localPage = localPage + JasperReport.getPages().size();

        // 통합 출력인 경우 reportList만 반환하고 종료
        System.out.println("################ + " + spSearchVO.getType());
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public List<JasperPrint> getAllReportOBJM(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        spSearchVO.setCheckedPrint(false);
        reportList = getDetailedActionPlanReport(request, response, spSearchVO);
        return reportList;
    }

    public List<JasperPrint> getDetailedActionPlanReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        ReportVO reportVO = new ReportVO();
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "중점추진사항별 세부계획";
        String title = "중점추진사항별 세부계획";

        reportVO.setFileName(fileNm); // 다운로드되는 파일 명
        reportVO.setJrxmlPath("report/participation/objectivesAndActionPlan/detailedActionPlan.jrxml"); // 레포트 파일 경로
        reportVO.setType("pdf"); // 출력물 타입

        // 표지 리포트
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        List<ActPlanDetailVO> resultList = getActionMonthly(spSearchVO);
        if (!spSearchVO.isCheckedPrint()) {
            resultList = resultList.stream()
                    .filter(plan -> plan.getUseYn().equals(YesNo.valueOf("Y"))) // plan.getDetailItem()과 비교
                    .collect(Collectors.toList());
        } else {
            resultList = resultList.stream()
                    .filter(plan -> plan.getDocDetailSeq() != null && spSearchVO.getCheckedList().contains(plan.getWriteYear() + plan.getDocNo() + plan.getDocSeq() + plan.getDocDetailSeq()))
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> datasource = new ArrayList<>();
        Map<String, Object> params = new HashMap<String, Object>();
        // 해당하는 페이지는 전체출력에 사용될 예정이므로 필수가 아닙니다.
        params.put("page", page); // 페이지
        params.put("subPage", subPage); // 서브페이지
        params.put("localPage", localPage); // 로컬페이지
        params.put("title", title);

        if (resultList.isEmpty()) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("compId", "");
            datasource.add(data);
        }

        //sort 변수
        String curDetailItemId = null;
        String curDocNo = null;
        String curDocSeq = null;
        String curDocDetailSeq = null;
        long seqCnt = 0;
        long seqCntSecond = 0;
        long seqCntThird = 0;
        long seqCnt4 = 0;

        for (ActPlanDetailVO result : resultList) {
            //목표및중점추진사항과 TYPE이 겹쳐서 세부계획 사인은 TYPE - 'OBJP'로 설정
            resultList.get(0).setDocType("OBJP");
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(resultList.get(0));
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            //계획
            Map<String, Object> data = new HashMap<String, Object>();

            if (curDocNo == null || curDocSeq == null || !curDocNo.equals(result.getDocNo()) || !curDocSeq.equals(result.getDocSeq())) {
                //순서 맞춰주기 위해 순서값 생성 - 상세목표
                curDocNo = result.getDocNo();
                curDocSeq = result.getDocSeq();
                seqCnt++;
            }

            if (curDetailItemId == null || !curDetailItemId.equals(result.getDetailItemId())) {
                //순서 맞춰주기 위해 순서값 생성 - 세부항목
                curDetailItemId = result.getDetailItemId();
                seqCntSecond++;
            }

            if (curDocNo == null || curDocSeq == null || curDocDetailSeq == null || !curDocNo.equals(result.getDocNo()) || !curDocSeq.equals(result.getDocSeq()) || !curDocDetailSeq.equals(result.getDocDetailSeq())) {
                //순서 맞춰주기 위해 순서값 생성 - 추진계획
                curDocNo = result.getDocNo();
                curDocSeq = result.getDocSeq();
                curDocDetailSeq = result.getDocDetailSeq();
                seqCntThird++;
            }

            data.put("compId", SecurityUtil.getCurrentCompId());
            data.put("seq", seqCnt);
            data.put("seq2", seqCntSecond);
            data.put("seq3", seqCntThird);
            data.put("seq4", seqCnt4++);
            data.put("actionObjective", result.getActionObjective());
            data.put("detailItem", result.getDetailItem());
            data.put("detailPlan", result.getDetailPlan());
            data.put("planDiv", "계획");

            data.put("actionSchedule1", String.valueOf(result.getActionSchedule1()));
            data.put("actionSchedule2", String.valueOf(result.getActionSchedule2()));
            data.put("actionSchedule3", String.valueOf(result.getActionSchedule3()));
            data.put("actionSchedule4", String.valueOf(result.getActionSchedule4()));
            data.put("actionSchedule5", String.valueOf(result.getActionSchedule5()));
            data.put("actionSchedule6", String.valueOf(result.getActionSchedule6()));
            data.put("actionSchedule7", String.valueOf(result.getActionSchedule7()));
            data.put("actionSchedule8", String.valueOf(result.getActionSchedule8()));
            data.put("actionSchedule9", String.valueOf(result.getActionSchedule9()));
            data.put("actionSchedule10", String.valueOf(result.getActionSchedule10()));
            data.put("actionSchedule11", String.valueOf(result.getActionSchedule11()));
            data.put("actionSchedule12", String.valueOf(result.getActionSchedule12()));
            String performanceDiv = "";
            if (result.getPerformanceRepeatNm() != null && !result.getPerformanceRepeatNm().equals("")) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건 / " + result.getPerformanceRepeatNm() + " / " + result.getPerformanceTypeNm();
            } else if (result.getPerformanceCnt() != 0) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건" + " / " + result.getPerformanceTypeNm();
            } else if (!"0000".equals(result.getPerformanceType()) && result.getPerformanceCnt() == 0) {
                performanceDiv = result.getPerformanceTypeNm();
            } else {
                performanceDiv = result.getPerformanceCustom();
            }
            data.put("performanceDiv", performanceDiv);
            data.put("budget", String.valueOf(NumberFormat.getInstance().format(result.getBudget() / 10000)));
            String orgnNm = result.getOrgnList().stream()
                    .map(BaseMapVO::getNm)  // 각 ActPlanVO에서 nm 필드를 가져옴
                    .collect(Collectors.joining(", "));  // 콤마로 구분하여 연결
            data.put("orgnNm", orgnNm);
            datasource.add(data);

            //실적
            Map<String, Object> data1 = new HashMap<String, Object>();

            data1.put("compId", SecurityUtil.getCurrentCompId());
            data1.put("seq", seqCnt);
            data1.put("seq2", seqCntSecond);
            data1.put("seq3", seqCntThird);
            data1.put("seq4", seqCnt4++);
            data1.put("actionObjective", result.getActionObjective());
            data1.put("detailItem", result.getDetailItem());
            data1.put("detailPlan", result.getDetailPlan());
            data1.put("planDiv", "실적");

            System.out.println("# docNo = " + result.getDocNo());
            System.out.println("# dosSeq = " + result.getDocSeq());
            System.out.println("# dosDetailSeq = " + result.getDocDetailSeq());
            System.out.println("# docNo0 = " + result.getDocNoO());
            System.out.println("# docSeq0 = " + result.getDocSeqO());

            HseKeyPerformanceResultVO performanceData = actPlanDao.getOnlyActionPerformance(result);
            if (performanceData != null) {
                data1.put("actionSchedule1", performanceData.getActionResult1());
                data1.put("actionSchedule2", performanceData.getActionResult2());
                data1.put("actionSchedule3", performanceData.getActionResult3());
                data1.put("actionSchedule4", performanceData.getActionResult4());
                data1.put("actionSchedule5", performanceData.getActionResult5());
                data1.put("actionSchedule6", performanceData.getActionResult6());
                data1.put("actionSchedule7", performanceData.getActionResult7());
                data1.put("actionSchedule8", performanceData.getActionResult8());
                data1.put("actionSchedule9", performanceData.getActionResult9());
                data1.put("actionSchedule10", performanceData.getActionResult10());
                data1.put("actionSchedule11", performanceData.getActionResult11());
                data1.put("actionSchedule12", performanceData.getActionResult12());
            }
//            data1.put("actionSchedule1", "null");
//            data1.put("actionSchedule2", "null");
//            data1.put("actionSchedule3", "null");
//            data1.put("actionSchedule4", "null");
//            data1.put("actionSchedule5", "null");
//            data1.put("actionSchedule6", "null");
//            data1.put("actionSchedule7", "null");
//            data1.put("actionSchedule8", "null");
//            data1.put("actionSchedule9", "null");
//            data1.put("actionSchedule10", "null");
//            data1.put("actionSchedule11", "null");
//            data1.put("actionSchedule12", "null");
            data1.put("performanceDiv", performanceDiv);
            data1.put("budget", "");
            data1.put("orgnNm", orgnNm);
            datasource.add(data1);
        }

        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo);
        params.put("datasource", new JRBeanCollectionDataSource(datasource));

        reportVO.setParameter(params);

        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page = page + JasperReport.getPages().size();
        localPage = localPage + JasperReport.getPages().size();

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public List<JasperPrint> getAllReportOBJB(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        spSearchVO.setCheckedPrint(false);
        reportList = getHseBudgetReport(request, response, spSearchVO);
        return reportList;
    }

    public List<JasperPrint> getHseBudgetReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        ReportVO reportVO = new ReportVO();

        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전보건환경 예산";
        String title = "안전보건환경 예산";

        // 표지 리포트
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        reportVO.setFileName(fileNm); // 다운로드되는 파일 명
        reportVO.setJrxmlPath("report/participation/actionPerformance/actionPerformance.jrxml"); // 레포트 파일 경로
        reportVO.setType(spSearchVO.getType()); // 출력물 타입

        List<ActPlanPerformanceVO> resultList = getActionPerformance(spSearchVO);
        if (!spSearchVO.isCheckedPrint()) {
            resultList = resultList.stream()
                    .filter(plan -> plan.getUseYn().equals(YesNo.valueOf("Y")))
                    .collect(Collectors.toList());
        } else {
            resultList = resultList.stream()
                    .filter(plan -> {
                        String comparisonKey = plan.getDocPerformanceSeq() != null
                                ? plan.getWriteYearO() + plan.getDocNoO() + plan.getDocSeqO() + plan.getDocDetailSeqO() + plan.getDocPerformanceSeq()
                                : plan.getWriteYearO() + plan.getDocNoO() + plan.getDocSeqO() + plan.getDocDetailSeqO() + plan.getOrgnIdO();
                        return spSearchVO.getCheckedList().contains(comparisonKey);
                    })
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> datasource = new ArrayList<>();
        Map<String, Object> params = new HashMap<String, Object>();
        // 해당하는 페이지는 전체출력에 사용될 예정이므로 필수가 아닙니다.
        params.put("page", page); // 페이지
        params.put("subPage", subPage); // 서브페이지
        params.put("localPage", localPage); // 로컬페이지
        params.put("title", title);


        if (resultList.isEmpty()) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("compId", "");
            datasource.add(data);
        }

        //sort 변수
        String curCompObjectiveId = null;
        String curDetailItemId = null;
        String curDocNo = null;
        String curDocSeq = null;
        String curDocDetailSeq = null;
        long seqCnt = 0;
        long seqCntSecond = 0;
        long seqCntThird = 0;
        long seqCntFourth = 0;
        int totalBudget = 0;
        int totalPerformanceAmount = 0;
        long Fifth = 0;
        for (ActPlanPerformanceVO result : resultList) {
            Map<String, Object> data = new HashMap<String, Object>();

            if (curCompObjectiveId == null || !curCompObjectiveId.equals(result.getCompObjectiveId())) {
                //순서 맞춰주기 위해 순서값 생성 - 전사목표
                curCompObjectiveId = result.getCompObjectiveId();
                seqCnt++;
            }
//            if (curDocNo == null || curDocSeq == null || !curDocNo.equals(result.getDocNo()) || !curDocSeq.equals(result.getDocSeq())) {
//                //순서 맞춰주기 위해 순서값 생성 - 상세목표
//                if (result.getDocNo() != null) curDocNo = result.getDocNo();
//                if (result.getDocSeq() != null) curDocSeq = result.getDocSeq();
//                seqCntSecond++;
//            }
//
//            if (curDetailItemId == null || !curDetailItemId.equals(result.getDetailItemId())) {
//                //순서 맞춰주기 위해 순서값 생성 - 세부항목
//                curDetailItemId = result.getDetailItemId();
//                seqCntThird++;
//            }
//
//            if (curDocNo == null || curDocSeq == null || curDocDetailSeq == null || !curDocNo.equals(result.getDocNo()) || !curDocSeq.equals(result.getDocSeq()) || !curDocDetailSeq.equals(result.getDocDetailSeq())) {
//                //순서 맞춰주기 위해 순서값 생성 - 추진계획
//                if (result.getDocNo() != null) curDocNo = result.getDocNo();
//                if (result.getDocSeq() != null) curDocSeq = result.getDocSeq();
//                if (result.getDocDetailSeq() != null) curDocDetailSeq = result.getDocDetailSeq();
//                seqCntFourth++;
//            }
            data.put("compId", SecurityUtil.getCurrentCompId());
            data.put("seq", seqCnt);
//            data.put("seq2", seqCntSecond);
//            data.put("seq3", seqCntThird);
//            data.put("seq4", seqCntFourth);
            data.put("seq5", Fifth++);
            data.put("companyObjective", result.getCompanyObjective());
            data.put("actionObjective", result.getActionObjective());
            data.put("detailItem", result.getDetailItem());
            data.put("detailPlan", result.getDetailPlan());
            data.put("plandiv1", "계획");
            data.put("plandiv2", "실적");

            data.put("actionSchedule1", String.valueOf(result.getActionSchedule1()));
            data.put("actionSchedule2", String.valueOf(result.getActionSchedule2()));
            data.put("actionSchedule3", String.valueOf(result.getActionSchedule3()));
            data.put("actionSchedule4", String.valueOf(result.getActionSchedule4()));
            data.put("actionSchedule5", String.valueOf(result.getActionSchedule5()));
            data.put("actionSchedule6", String.valueOf(result.getActionSchedule6()));
            data.put("actionSchedule7", String.valueOf(result.getActionSchedule7()));
            data.put("actionSchedule8", String.valueOf(result.getActionSchedule8()));
            data.put("actionSchedule9", String.valueOf(result.getActionSchedule9()));
            data.put("actionSchedule10", String.valueOf(result.getActionSchedule10()));
            data.put("actionSchedule11", String.valueOf(result.getActionSchedule11()));
            data.put("actionSchedule12", String.valueOf(result.getActionSchedule12()));
            data.put("actionPerformance1", String.valueOf(result.getActionPerformance1()));
            data.put("actionPerformance2", String.valueOf(result.getActionPerformance2()));
            data.put("actionPerformance3", String.valueOf(result.getActionPerformance3()));
            data.put("actionPerformance4", String.valueOf(result.getActionPerformance4()));
            data.put("actionPerformance5", String.valueOf(result.getActionPerformance5()));
            data.put("actionPerformance6", String.valueOf(result.getActionPerformance6()));
            data.put("actionPerformance7", String.valueOf(result.getActionPerformance7()));
            data.put("actionPerformance8", String.valueOf(result.getActionPerformance8()));
            data.put("actionPerformance9", String.valueOf(result.getActionPerformance9()));
            data.put("actionPerformance10", String.valueOf(result.getActionPerformance10()));
            data.put("actionPerformance11", String.valueOf(result.getActionPerformance11()));
            data.put("actionPerformance12", String.valueOf(result.getActionPerformance12()));
            String performanceDiv = "";
            if (result.getPerformanceRepeatNm() != null && !result.getPerformanceRepeatNm().equals("")) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건 / " + result.getPerformanceRepeatNm() + " / " + result.getPerformanceTypeNm();
            } else if (result.getPerformanceCnt() != 0) {
                performanceDiv = String.valueOf(result.getPerformanceCnt()) + "건" + " / " + result.getPerformanceTypeNm();
            } else if (!"0000".equals(result.getPerformanceType()) && result.getPerformanceCnt() == 0) {
                performanceDiv = result.getPerformanceTypeNm();
            } else {
                performanceDiv = result.getPerformanceCustom();
            }
            data.put("performanceDiv", performanceDiv);

            // 2025.11.14 BHJ 만원단위 반올림 계산 적용            
            data.put("budget", NumberFormat.getInstance().format(Math.round(result.getBudget() / 10000.0)));
            data.put("performanceAmount", NumberFormat.getInstance().format(Math.round(result.getPerformanceAmount() / 10000.0)));
            totalBudget += Math.round(result.getBudget() / 10000.0);
            totalPerformanceAmount += Math.round(result.getPerformanceAmount() / 10000.0);
            data.put("orgnNm", result.getOrgnNm());
            
            List<HrVO> hrList = actPlanDao.getPerformanceHrList(result);
            String names = hrList.stream()
                    .map(HrVO::getHrNm)  // hrNm만 추출
                    .collect(Collectors.joining("\n"));
            data.put("createdBy", names);
            datasource.add(data);
        }

        params.put("totalBudget", String.valueOf(NumberFormat.getInstance().format(totalBudget)));
        params.put("totalPerformanceAmount", String.valueOf(NumberFormat.getInstance().format(totalPerformanceAmount)));

        // 서명 조회
        SpSearchVO signParam = new SpSearchVO();
        signParam.setDocType("BDG");
        signParam.setWriteYear(spSearchVO.getWriteYear());        
        // // 사인은 해당년도의 최초값 단 하나기때문에 '00001' 고정해서 사용
        // // signParam.setDocNo("00001");        
        // 왜 고정했는지 히스토리가 파악안됨, 다른 사업장이 개설하면 00001이 아니라 max값으로 채번됨. spSearchVO.getDocNo()으로 수정
        signParam.setDocNo(spSearchVO.getDocNo());
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo);
        params.put("datasource", new JRBeanCollectionDataSource(datasource));

        reportVO.setParameter(params);

        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page = page + JasperReport.getPages().size();
        localPage = localPage + JasperReport.getPages().size();

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }
}
