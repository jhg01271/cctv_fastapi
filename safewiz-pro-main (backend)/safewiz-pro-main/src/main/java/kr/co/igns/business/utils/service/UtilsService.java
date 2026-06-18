package kr.co.igns.business.utils.service;

import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.task.service.TaskService;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.RequiredArgsConstructor;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilsService {
    private final UtilsDAO utilsDAO;
    private final FileService fileService;
    private final NasFileService nasFileService;
    private final ReportService reportService;
    private final TaskService taskService;
    private final SystemCodeDAO systemCodeDao;
    private final FileProperty property;

    // 고객사 로고를 불러옵니다.
    public InputStream getClntLogo(String clntId) {
        UtilsVO vo = new UtilsVO();
        vo.setClntId(clntId);
        String fileId = utilsDAO.getLogoFileId(vo);

        InputStream logo = null;
        if (fileId != null) {
            // 고객 로고 호출
            if ("local".equals(property.getTarget())) {
                logo = fileService.getFileInputStream(fileId);
            } else if ("nas".equals(property.getTarget())) {
                logo = nasFileService.getNasFileInputStream(fileId);
            }
        }
        return logo;
    }

    // 기준정보 담당자 정/부 를 불러옵니다.
    public List<HrVO> getHrIncharge(String targetType, String targetId, String gubun) {
        List<HrVO> hrList = utilsDAO.getHrIncharge(targetType, targetId, gubun);
        return hrList;
    }

    // 매핑정보 조회
    // 작업장 매핑정보 조회
    public List<BaseMapVO> getWpMapByTargetIds(Map<String, Object> param) {
        List<BaseMapVO> resultList = utilsDAO.getWpMapByTargetIds(param);
        return resultList;
    }

    // 조직 매핑정보 조회
    public List<BaseMapVO> getOrgnMapByTargetIds(Map<String, Object> param) {
        List<BaseMapVO> resultList = utilsDAO.getOrgnMapByTargetIds(param);
        return resultList;
    }

    // 작업내용 매핑정보 조회
    public List<PrcsWorkVO> getPrcsWorkMapByTargetIds(Map<String, Object> param) {
        List<PrcsWorkVO> resultList = utilsDAO.getPrcsWorkMapByTargetIds(param);
        return resultList;
    }

    // 담당자 매핑정보 조회
    public List<HrVO> getHrInchargeByTargetIds(Map<String, Object> param) {
        List<HrVO> hrList = utilsDAO.getHrInchargeByTargetIds(param);
        return hrList;
    }

    public List<UtilsVO> searchCompList(String clntId) throws Exception {
        List<UtilsVO> list = utilsDAO.searchCompList(clntId);
        return list;
    }

    // 고객사 이름을 불러옵니다..
    public String getClntNm(String clntId) {
        UtilsVO vo = new UtilsVO();
        vo.setClntId(clntId);
        String clntNm = utilsDAO.getClntNm(vo);
        return clntNm;
    }

    // 사업장 이름을 불러옵니다..
    public String getCompNm(String clntId, String compId) {
        UtilsVO vo = new UtilsVO();
        vo.setClntId(clntId);
        vo.setCompId(compId);
        String compNm = utilsDAO.getCompNm(vo);
        return compNm;
    }

    // 서명 데이터를 불러옵니다.
    public InputStream getUserSignatureByUserId(String clntId, String userId) {
        UtilsVO vo = new UtilsVO();
        vo.setClntId(clntId);
        vo.setUserId(userId);
        String base64 = utilsDAO.getUserSignature(vo);
        InputStream signature = null;
        if (base64 != null && !base64.equals("")) {
            byte[] decodedSignature = Base64.getDecoder().decode(base64.split(",")[1]);
            signature = new ByteArrayInputStream(decodedSignature);
        }
        return signature;
    }

    public InputStream getUserSignatureByHrId(String clntId, String hrId) {
        UtilsVO vo = new UtilsVO();
        vo.setClntId(clntId);
        vo.setHrId(hrId);
        String base64 = utilsDAO.getUserSignature(vo);

        return convertToInputStream(base64);
    }

    public InputStream convertToInputStream(String base64) {
        InputStream signature = null;
        if (base64 != null && !base64.equals("")) {
            byte[] decodedSignature = Base64.getDecoder().decode(base64.split(",")[1]);
            signature = new ByteArrayInputStream(decodedSignature);
        }
        return signature;
    }

    public InputStream getSignatureFromBase64String(String base64) {
        InputStream signature = null;
        if (base64 != null && !base64.equals("")) {
            byte[] decodedSignature = Base64.getDecoder().decode(base64.split(",")[1]);
            signature = new ByteArrayInputStream(decodedSignature);
        }
        return signature;
    }

    public List<UtilsVO> getApprovalInfo(UtilsVO vo) throws Exception {

        List<UtilsVO> list = utilsDAO.getApprovalInfo(vo);

        return list;
    }

    public List<UtilsVO> getApprovalInfoTask(UtilsVO vo) throws Exception {

        List<UtilsVO> list = utilsDAO.getApprovalInfoTask(vo);

        return list;
    }

    public List<UtilsVO> getApprovalInfoSimple(UtilsVO vo) throws Exception {
        List<UtilsVO> list = utilsDAO.getApprovalInfoSimple(vo);
        return list;
    }

//    public boolean isDuplicateApprovalInfo(UtilsVO saveVo) {
//        int result = utilsDAO.checkDuplicateApprovalInfo(saveVo);
//        return result > 0; // 중복이 있으면 true, 없으면 false 반환
//    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertApprovalInfo(UtilsVO vo) throws Exception {

        vo.setUserId(SecurityUtil.getCurrentMemberId());

        //서명 조회
        List<UtilsVO> list = getApprovalInfoTask(vo);
        SystemCodeVO systemCodeReqVo = new SystemCodeVO();
        //시스템코드 조회(Task 분류 : C0044, 서명 : sign001~ )
        systemCodeReqVo.setMajorCd("C0044");    //task major code
        List<SystemMinorCodeVO> taskSystemCodeList = systemCodeDao.findDetailTaskSign(systemCodeReqVo);

        //FIX: 임시 IF : 서명관련 개발 중
//        if(vo.getFormattedTargetId() != null && !Objects.equals(vo.getFormattedTargetId(), "")) {
//            //task 취소
//            String hrId = list.stream() // 기존 동일한 대상 서명자 task 취소
//                    .filter(item -> vo.getParam().equals(item.getParam()))
//                    .map(item -> Optional.ofNullable(item.getHrId()).orElse(""))
//                    .findFirst()
//                    .orElse(null);
//            vo.setId(hrId);
//            cancelTask(vo);
//        }
//        utilsDAO.deleteApprovalInfo(vo); // 기존 동일한 대상 서명자 정보 삭제
        if ("U".equals(vo.getCmd())) {
            utilsDAO.updateApprovalInfo(vo);
        } else {
            utilsDAO.insertApprovalInfo(vo); // 서명자 정보 저장
        }

        if (vo.getHrId() == null || vo.getHrId().isEmpty()) {
            return vo;
        }
        //FIX: 임시 IF : 서명관련 개발 중
        if (vo.getFormattedTargetId() != null && !Objects.equals(vo.getFormattedTargetId(), "")) {
            //task 추가
            TaskVO taskVo = new TaskVO();
            boolean saveTaskYn = false; //Task 저장할지 판단 (앞의 서명이 완료된 경우)

            //시스템코드 조회(서명 분류 : C0045)
            systemCodeReqVo.setMajorCd("C0045");    //서명 major code
            List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);

            //이전 서명이 완료되었을 경우, task 생성
            for (int i = 0; i < systemCodeList.size(); i++) {
                SystemMinorCodeVO sc = systemCodeList.get(i);
                SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                        .filter(item -> item.getExtra2().equals(sc.getMinorCd()))
                        .findFirst()
                        .orElse(null);
                if (sc.getOrdSeq() < 1) {    //시스템코드의 순번이 1부터 사용되는 서명코드로 판단 : 서명 없음 등 필요한 코드는 0(<1)으로 사용
                    continue;
                } else if (sc.getOrdSeq() == 1 && vo.getParam().equals(sc.getMinorCd())) {
                    //서명 분류 [0]는 항상 task 생성
                    saveTaskYn = true;
                    if (vo.getDisplayName() == null) {
                        taskVo.setTaskDetailContent("서명");
                    } else {
                        taskVo.setTaskDetailContent("서명" + "(" + vo.getDisplayName() + ")");
                    }
//                    if ("sign".equals(vo.getType())) {
//                        taskVo.setTaskDetailContent(taskDiv.getMinorNm());
//                    } else {
//                        taskVo.setTaskDetailContent("서명");
//                    }
                    taskVo.setTaskDetailDiv(taskDiv.getMinorCd());
                } else {
                    if (vo.getParam().equals(sc.getMinorCd())) {
                        //이전 서명 사인 조회
                        String prevMinorCd = systemCodeList.get(i - 1).getMinorCd();
                        List<UtilsVO> filteredList = list.stream()
                                .filter(item -> prevMinorCd.equals(item.getParam()) && item.getHrId() != null)
                                .collect(Collectors.toList());
                        if (filteredList.isEmpty()) continue;
                        boolean hasInvalidSignature = filteredList.stream()
                                .map(item -> item.getSignature())
                                .anyMatch(signature -> signature == null || signature.isEmpty());
                        if (!hasInvalidSignature) {
                            saveTaskYn = true;
                            if (vo.getDisplayName() == null) {
                                taskVo.setTaskDetailContent("서명");
                            } else {
                                taskVo.setTaskDetailContent("서명" + "(" + vo.getDisplayName() + ")");
                            }
//                            if ("sign".equals(vo.getType())) {
//                                taskVo.setTaskDetailContent(taskDiv.getMinorNm());
//                            } else {
//                                taskVo.setTaskDetailContent("서명");
//                            }
                            taskVo.setTaskDetailDiv(taskDiv.getMinorCd());
                        }
                    }
                }
            }

            if (saveTaskYn) {
                taskVo.setClntId(vo.getClntId());
                taskVo.setTaskContent("서명");
                taskVo.setTaskDetailHrId(vo.getHrId());
                taskVo.setTaskDetailUserId(vo.getHrUserId());
                taskVo.setUserId(vo.getUserId());
                taskVo.setReqUserId(vo.getUserId());
                taskVo.setReqInfoKey(vo.getFormattedTargetId());
                taskVo.setReqMenuId(vo.getReqMenuId());
                taskVo.setUseYn(vo.getUseYn());
                taskService.saveTask(taskVo);
            }
        }

        return vo;
    }


    @Transactional(rollbackFor = Throwable.class)
    public List<UtilsVO> insertApprovalInfoList(List<UtilsVO> voList) throws Exception {
        for (UtilsVO saveVo : voList) {
            // 사람이 지정되어 있지 않을 경우 continue
//            if (saveVo.getHrId() == null || saveVo.getHrId().isEmpty()) {
//                continue;
//            }
//            if (!this.isDuplicateApprovalInfo(saveVo)) {
            insertApprovalInfo(saveVo);
//            }
//            else{
//                LogUtil.ConsoleLogInfo(HttpStatus.OK, "동일한 서명정보가 이미 존재하여 저장할 수 없습니다","마운트 두번 ", null);
//            }
        }

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateTaskUseYn(UtilsVO vo) throws Exception {
        taskService.updateTaskUseYn(vo);
        return vo;
    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public List<UtilsVO> insertApprovalInfoListMulti(List<UtilsVO> voList) throws Exception {
//        utilsDAO.deleteApprovalInfo(voList.get(0));
//        for (UtilsVO saveVo : voList) {
//            // 사람이 지정되어 있지 않을 경우 continue
//            if (saveVo.getHrId() == null || saveVo.getHrId().isEmpty()) {
//                continue;
//            }
//            insertApprovalInfoMulti(saveVo);
//        }
//
//        return voList;
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public BaseVO insertApprovalInfoMulti(UtilsVO vo) throws Exception {
//
//        vo.setUserId(SecurityUtil.getCurrentMemberId());
//        insertApprovalInfo(vo); // 서명자 정보 저장
//
//        return vo;
//    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateApprovalInfo(UtilsVO vo) throws Exception {

        vo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDAO.updateApprovalInfo(vo);

        return vo;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateApprovalInfoSign(UtilsVO vo) throws Exception {

        vo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDAO.updateApprovalInfoSign(vo);

        //FIX: 임시 IF : 서명관련 개발 중
        if (vo.getFormattedTargetId() != null && !Objects.equals(vo.getFormattedTargetId(), "")) {
            TaskVO taskVo = new TaskVO();

            //시스템코드 조회(서명 분류 : C0045)
            SystemCodeVO systemCodeReqVo = new SystemCodeVO();
            systemCodeReqVo.setMajorCd("C0045");    //서명 major code
            List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);

            //시스템코드 조회(Task 분류 : C0044, 서명 : sign001~ )
            systemCodeReqVo.setMajorCd("C0044");    //task major code
            List<SystemMinorCodeVO> taskSystemCodeList = systemCodeDao.findDetailTaskSign(systemCodeReqVo);

            //task 완료
            for (SystemMinorCodeVO sc : systemCodeList) {
                if (sc.getMinorCd().equals(vo.getParam())) {
                    SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                            .filter(item -> item.getExtra2().equals(sc.getMinorCd()))
                            .findFirst()
                            .orElse(null);
                    if (vo.getDisplayName() == null) {
                        taskVo.setTaskDetailContent(taskDiv.getMinorNm());
                    }
                    taskVo.setTaskDetailDiv(taskDiv.getMinorCd());
                }
            }

            String currentSingParam = vo.getParam();
            taskVo.setExtra1(currentSingParam);
            taskVo.setReqInfoKey(vo.getFormattedTargetId());
            taskVo.setUserId(vo.getHrId());
            taskService.updateTaskComplete(taskVo);

            //task 추가
            for (int i = 0; i < systemCodeList.size(); i++) {
                SystemMinorCodeVO sc = systemCodeList.get(i);
                if (sc.getOrdSeq() < 1) {    //시스템코드의 순번이 1부터 사용되는 서명코드로 판단 : 서명 없음 등 필요한 코드는 0(<1)으로 사용
                    continue;
                } else if (i == systemCodeList.size() - 1 && vo.getParam().equals(sc.getMinorCd())) {
                    //마지막 서명일 경우, 항상 task 미생성
                } else {
                    List<UtilsVO> list = getApprovalInfoTask(vo);
                    if (vo.getParam().equals(sc.getMinorCd())) {

                        //같은 서명 순서의 모든 서명자가 서명을 했는지 확인
                        boolean hasInvalidSignature = list.stream()
                                .filter(item -> currentSingParam.equals(item.getParam()))
                                .map(item -> item.getSignature())
                                .anyMatch(signature -> signature == null || signature.isEmpty());

                        if (!hasInvalidSignature) {
                            //다음 서명자 조회
                            SystemMinorCodeVO nextMinor = systemCodeList.get(i + 1);

                            List<UtilsVO> filteredList = list.stream()
                                    .filter(item -> nextMinor.getMinorCd().equals(item.getParam()) && item.getHrId() != null)
                                    .collect(Collectors.toList());

                            SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                                    .filter(item -> item.getExtra2().equals(nextMinor.getMinorCd()))
                                    .findFirst()
                                    .orElse(null);

                            //다음 서명자 task 생성
                            for (UtilsVO nextUser : filteredList) {
                                TaskVO taskSaveVo = new TaskVO();
                                taskSaveVo.setTaskDetailHrId(nextUser.getHrId());
                                taskSaveVo.setTaskDetailUserId(nextUser.getHrUserId());
                                taskSaveVo.setUserId(vo.getUserId());
                                taskSaveVo.setTaskContent("서명");
                                taskSaveVo.setTaskDetailContent(taskDiv.getMinorNm());
                                taskSaveVo.setClntId(vo.getClntId());
                                taskSaveVo.setReqUserId(vo.getUserId());
                                taskSaveVo.setReqInfoKey(vo.getFormattedTargetId());
                                taskSaveVo.setReqMenuId(vo.getReqMenuId());
                                taskSaveVo.setTaskDetailDiv(taskDiv.getMinorCd());
                                if (nextUser.getSignature() != null && !Objects.equals(nextUser.getSignature(), "")) {
                                    //이미 다음 서명자가 서명을 한 경우
                                    taskSaveVo.setTaskUserChk(YesNo.Y);
                                    taskSaveVo.setCompleteDt("complete");
                                }
                                taskService.saveTask(taskSaveVo);
                            }
                        }
                    }
                }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateApprovalInfoSignCancel(UtilsVO vo) throws Exception {

        vo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDAO.updateApprovalInfoSignCancel(vo);

        //FIX: 임시 IF : 서명관련 개발 중
        if (vo.getFormattedTargetId() != null && !Objects.equals(vo.getFormattedTargetId(), "")) {
            TaskVO taskVo = new TaskVO();

            //시스템코드 조회(서명 분류 : C0045)
            SystemCodeVO systemCodeReqVo = new SystemCodeVO();
            systemCodeReqVo.setMajorCd("C0045");    //서명 major code
            List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);

            //시스템코드 조회(Task 분류 : C0044, 서명 : sign001~ )
            systemCodeReqVo.setMajorCd("C0044");    //task major code
            List<SystemMinorCodeVO> taskSystemCodeList = systemCodeDao.findDetailTaskSign(systemCodeReqVo);

            //다음 서명자 정보
            SystemMinorCodeVO nextSc = null;

            //task 완료 취소
            for (int i = 0; i < systemCodeList.size() - 1; i++) {
                SystemMinorCodeVO sc = systemCodeList.get(i);
                if (sc.getOrdSeq() < 1) {    //시스템코드의 순번이 1부터 사용되는 서명코드로 판단 : 서명 없음 등 필요한 코드는 0(<1)으로 사용
                    continue;
                } else if (sc.getMinorCd().equals(vo.getParam())) {
                    SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                            .filter(item -> item.getExtra2().equals(sc.getMinorCd()))
                            .findFirst()
                            .orElse(null);
                    taskVo.setTaskDetailContent(taskDiv.getMinorNm());
                    taskVo.setTaskDetailDiv(taskDiv.getMinorCd());
                    nextSc = systemCodeList.get(i + 1);
                }
            }

            taskVo.setReqInfoKey(vo.getFormattedTargetId());
            taskVo.setUserId(vo.getHrId());
            taskVo.setCompleteDt("cancel");
            taskService.updateTaskComplete(taskVo);


            //다음 서명자 task 있으면 취소
            if (nextSc != null) {
                SystemMinorCodeVO finalNextSc = nextSc;
                SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                        .filter(item -> item.getExtra2().equals(finalNextSc.getMinorCd()))
                        .findFirst()
                        .orElse(null);
                TaskVO taskDelVo = new TaskVO();
                taskDelVo.setTaskDetailDiv(taskDiv.getMinorCd());
                taskDelVo.setTaskDetailContent("서명취소");
                taskDelVo.setReqInfoKey(vo.getFormattedTargetId());
                taskDelVo.setExtra1(nextSc.getMinorCd());
                taskService.updateTaskCancelMulti(taskDelVo);
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteApprovalInfo(UtilsVO vo) throws Exception {
        //Task 취소
        vo.setId(vo.getHrId());
        cancelTask(vo);

        utilsDAO.deleteApprovalInfo(vo);

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO nullUpdateApprovalInfo(UtilsVO vo) throws Exception {
        //Task 취소
        vo.setId(vo.getHrId());
        cancelTask(vo);

        vo.setUserId(SecurityUtil.getCurrentMemberId());
        vo.setHrId(null);
        utilsDAO.updateApprovalInfo(vo);
//        utilsDAO.deleteApprovalInfo(vo);

        return vo;
    }

    // Task 취소
    // 다음 서명자의 task가 생성되어있을 경우, 다음 서명자의 task 제거
    // UtilsVO의 id를 취소할 인원 id(hr_id)로 세팅하고 호출하여야함
    public void cancelTask(UtilsVO vo) throws Exception {
        //FIX: 임시 IF : 서명관련 개발 중
        if (vo.getFormattedTargetId() != null && !Objects.equals(vo.getFormattedTargetId(), "")) {
            //시스템코드 조회(서명 분류 : C0045)
            SystemCodeVO systemCodeReqVo = new SystemCodeVO();
            systemCodeReqVo.setMajorCd("C0045");    //서명 major code
            List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);

            //시스템코드 조회(Task 분류 : C0044, 서명 : sign001~ )
            systemCodeReqVo = new SystemCodeVO();
            systemCodeReqVo.setMajorCd("C0044");    //task major code
            List<SystemMinorCodeVO> taskSystemCodeList = systemCodeDao.findDetailTaskSign(systemCodeReqVo);

            SystemMinorCodeVO taskDiv = taskSystemCodeList.stream()
                    .filter(item -> item.getExtra2().equals(vo.getParam()))
                    .findFirst()
                    .orElse(null);

            //task 취소
            TaskVO taskVo = new TaskVO();
            taskVo.setTaskDetailContent("서명취소");
            taskVo.setTaskDetailDiv(taskDiv.getMinorCd());
            taskVo.setReqInfoKey(vo.getFormattedTargetId());
            taskVo.setUserId(vo.getId());
            taskService.updateTaskCancel(taskVo);

            //다음 서명자 정보
            SystemMinorCodeVO nextSc = null;
            for (int i = 0; i < systemCodeList.size() - 1; i++) {
                SystemMinorCodeVO sc = systemCodeList.get(i);
                if (sc.getOrdSeq() < 1) {    //시스템코드의 순번이 1부터 사용되는 서명코드로 판단 : 서명 없음 등 필요한 코드는 0(<1)으로 사용
                    continue;
                } else if (sc.getMinorCd().equals(vo.getParam())) {
                    nextSc = systemCodeList.get(i + 1);
                }
            }

            //다음 서명자 task 있으면 취소
            if (nextSc != null) {
                SystemMinorCodeVO finalNextSc = nextSc;
                SystemMinorCodeVO taskDiv_1 = taskSystemCodeList.stream()
                        .filter(item -> item.getExtra2().equals(finalNextSc.getMinorCd()))
                        .findFirst()
                        .orElse(null);
                TaskVO taskDelVo = new TaskVO();
                taskDelVo.setTaskDetailDiv("서명취소");
                taskDelVo.setReqInfoKey(vo.getFormattedTargetId());
                taskVo.setTaskDetailDiv(taskDiv_1.getMinorCd());
                taskService.updateTaskCancelMulti(taskDelVo);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateUserSignature(UtilsVO vo) throws Exception {

        vo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDAO.updateUserSignature(vo);

        return vo;
    }

    //보고서 결재 조회(없는 경우, 각각 null로 반환)
    //list[0] : writer InputStream
    //list[1] : writer HrNm
    //list[2] : reviewer InputStream
    //list[3] : reviewer HrNm
    //list[4] : approver InputStream
    //list[5] : approver HrNm
//    public List<Object> getApprovalInfoToArray(BaseVO vo) throws Exception {
//        // targetId가 없으면 자동 생성
//        String targetId = vo.getWriteYear() + vo.getDocNo();
//        return this.getApprovalInfoToArray(vo, targetId);
//    }
//
//    // target_id 커스텀
//    public List<Object> getApprovalInfoToArray(BaseVO vo, String targetId) throws Exception {
//        List<Object> resultList = new ArrayList<Object>();
//
//        UtilsVO utilVo = new UtilsVO();
//        utilVo.setTargetId(targetId);
//        utilVo.setTargetType(vo.getDocType());
//        utilVo.setType(vo.getType());
//        List<UtilsVO> signature = utilsDAO.getApprovalInfo(utilVo);
//        SystemCodeVO systemCodeReqVo = new SystemCodeVO();
//
//        systemCodeReqVo.setMajorCd("C0045");
//        List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);
//        for (SystemMinorCodeVO item : systemCodeList) {
//            addSignatureToResultList(resultList, signature, item.getMinorCd());
//        }
//        return resultList;
//    }
//
//    private void addSignatureToResultList(List<Object> resultList, List<UtilsVO> signature, String param) {
//        Optional<UtilsVO> item = signature.stream()
//                .filter(el -> el.getParam().equals(param))
//                .findFirst();
//
//        if (item.isPresent()) {
//            InputStream signStream = convertToInputStream(item.get().getSignature());
//            resultList.add(signStream);
//            resultList.add(item.get().getHrNm());
//        } else {
//            // 데이터가 없을 때 null 추가
//            resultList.add(null);
//            resultList.add(null);
//        }
//    }

    // 동적 서명 작업 중 - 03/14 LJH start
    public List<Map<String, Object>> getApprovalInfoToArray(BaseVO vo) throws Exception {
        // targetId가 없으면 자동 생성
        String targetId = vo.getWriteYear() + vo.getDocNo();
        return this.getApprovalInfoToArray(vo, targetId);
    }

    public List<Map<String, Object>> getApprovalInfoToArray(BaseVO vo, String targetId) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();

        UtilsVO utilVo = new UtilsVO();
        utilVo.setTargetId(targetId);
        utilVo.setTargetType(vo.getDocType());
        if (vo.getType() != null && !"".equals(vo.getType())) {
            utilVo.setType(vo.getType());
        } else {
            utilVo.setType("sign");
        }
        List<UtilsVO> signature = utilsDAO.getApprovalInfo(utilVo);
        SystemCodeVO systemCodeReqVo = new SystemCodeVO();

        systemCodeReqVo.setMajorCd("C0045");
        List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);
        for (SystemMinorCodeVO item : systemCodeList) {
            addSignatureToResultList(resultList, signature, item.getOrdSeq(), item.getMinorCd(), item.getMinorNm());
        }
        return resultList;
    }

    private void addSignatureToResultList(List<Map<String, Object>> resultList, List<UtilsVO> signature, int ordSeq, String minorCd, String minorNm) {
        Optional<UtilsVO> item = signature.stream()
                .filter(el -> el.getParam().equals(minorCd))
                .findFirst();
        if (item.isPresent()) {
            Map<String, Object> data = new HashMap<>();
            data.put("type", "결재");
            data.put("ordSeq", ordSeq);
            data.put("title", item.get().getDisplayName()); // 서명 타이틀 (ex.작성, 검토, 승인)
            data.put("signature", convertToInputStream(item.get().getSignature())); // 서명자 서명 (inputStream)
            data.put("name", item.get().getHrNm() == null ? "" : item.get().getHrNm()); // 서명자 이름
            data.put("jbrpNm", item.get().getJbrpNm() == null ? "" : item.get().getJbrpNm()); //서명자 직급
            resultList.add(data);
        }

    }
// 동적 서명 작업 중 - 03/14 LJH end

    // 레포트 표지 (type = basicFront/basicFront_reverse )
    public JasperPrint getFrontReport(String type, SpSearchVO spSearchVO, String title) throws Exception {
        String compNm = getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        InputStream logo = getClntLogo(SecurityUtil.getCurrentClntId());
        // 표지 리포트
        ReportVO frontReportVO = new ReportVO();
        frontReportVO.setJrxmlPath("report/utils/" + type + ".jrxml");
        Map<String, Object> frontParam = new HashMap<String, Object>();
        frontParam.put("logo", logo);
        frontParam.put("title", title);
        frontParam.put("subTitle", "사업장명:" + compNm);
        frontParam.put("page", spSearchVO.getPage()); // 하단 페이지
        frontParam.put("subPage", spSearchVO.getSubPage());// 우측 상단 페이지 ( 1 - 2 의 1을 의미함)
        frontParam.put("extra1", spSearchVO.getExtra1());// 추가 제목
        if (spSearchVO.getLocalPage() > 0) {
            frontParam.put("localPage", spSearchVO.getLocalPage());// 우측 상단 페이지 ( 1 - 2 의 2를 의미함)
        } else {
            frontParam.put("localPage", 1);// 우측 상단 페이지 ( 1 - 2 의 2를 의미함)
        }
        frontReportVO.setParameter(frontParam);
        JasperPrint JasperFrontReport = reportService.allReportJasperPrint(frontReportVO);
        return JasperFrontReport;
    }

    // 기본값(basicFront)을 사용하는 메소드 오버로딩
    public JasperPrint getFrontReport(SpSearchVO spSearchVO, String title) throws Exception {
        return getFrontReport("basicFront", spSearchVO, title);
    }

    public static List<FileVO> filterFilesByExtensions(List<FileVO> fileList) {
        // 필터링 수행
        String[] imgExtension = {"jpg", "png", "jpeg"};
        return fileList.stream()
                .filter(file -> Arrays.asList(imgExtension).contains(file.getExtension()))
                .collect(Collectors.toList()); // 필터링 결과를 List로 반환
    }

    // 첨부파일 (이미지) 레포트 출력
    public JasperPrint getFilesReport(SpSearchVO spSearchVO, List<FileVO> fileList) throws Exception {
        InputStream logo = getClntLogo(SecurityUtil.getCurrentClntId());
        // 표지 리포트
        ReportVO fileReportVO = new ReportVO();
        fileReportVO.setJrxmlPath("report/utils/basicFiles.jrxml");
        Map<String, Object> filesParam = new HashMap<String, Object>();
        filesParam.put("logo", logo); // 좌측 상단 로고
        filesParam.put("page", spSearchVO.getPage()); // 하단 페이지
        filesParam.put("subPage", spSearchVO.getSubPage()); // 우측 상단 페이지 ( 1 - 2 의 1을 의미함)
        filesParam.put("localPage", spSearchVO.getLocalPage()); // 우측 상단 페이지 ( 1 - 2 의 2를 의미함)
        List<Map<String, Object>> files = new ArrayList<>();
        List<FileVO> filteredFileList = filterFilesByExtensions(fileList);
        for (int i = 0; i < filteredFileList.size(); i++) {
            // 허용되는 파일 확장자 일 경우에만 수행
            Map<String, Object> data = new HashMap<String, Object>();
            if ("local".equals(property.getTarget())) {
                data.put("img", fileService.getFileInputStream(filteredFileList.get(i).getFileId()));
            } else if ("nas".equals(property.getTarget())) {
                data.put("img", nasFileService.getNasFileInputStream(filteredFileList.get(i).getFileId()));
            }
            data.put("index", i + 1);
            files.add(data);
        }
        filesParam.put("files", new JRBeanCollectionDataSource(files)); // 파일 목록
        fileReportVO.setParameter(filesParam);
        JasperPrint JasperFileReport = reportService.allReportJasperPrint(fileReportVO);
        return JasperFileReport;
    }
}
