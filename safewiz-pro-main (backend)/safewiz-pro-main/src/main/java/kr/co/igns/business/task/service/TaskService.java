package kr.co.igns.business.task.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import kr.co.igns.business.alarm.model.AlarmVO;
import kr.co.igns.business.impl.model.SafetyInspectionLogVO;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.business.dashboard.model.DashboardPerVO;
import kr.co.igns.business.impl.dao.postgres.SafetyChecklistDAO;
import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.impl.model.SafetyChecklistHrVO;
import kr.co.igns.business.impl.model.SafetyChecklistVO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.business.task.dao.postgres.TaskDAO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDAO taskDao;
    private final SafetyChecklistDAO checkListDao;
    private final SystemCodeDAO systemCodeDao;

    //#region TASK - 서명
    // TASK - 서명 조회
    public List<TaskVO> getTask(SpSearchVO searchVo) {
        if (searchVo.getSearchText() == null || Objects.equals(searchVo.getSearchText(), "")) {
            //대시보드 조회용
            searchVo.setSearchText(searchVo.getCreatedBy());
        }
        List<TaskVO> vo = taskDao.getTask(searchVo);
        return vo;
    }

    // TASK - 서명 조회 (대시보드)
    public List<DashboardPerVO> getTaskDashboard(SpSearchVO searchVo) {
        List<DashboardPerVO> vo = taskDao.getTaskDashboard(searchVo);
        return vo;
    }

    // TASK - 서명 데이터 생성
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveTask(TaskVO reqVo) {
        //해당 인원 업무분장 없으면 생성
        taskDao.insertHseJob(reqVo);
        String tempReqInfoKey = reqVo.getReqInfoKey(); // 마스터 용 데이터 가공
        String[] parts = tempReqInfoKey.split("\\^&");
        if (parts.length < 3) reqVo.setReqInfoKey(reqVo.getReqInfoKey()); // 3개 미만이면 그대로 반환
        else reqVo.setReqInfoKey(parts[0] + "^&" + parts[1] + "^&" + parts[2]);

        //해당 화면의 모든 서명 Task 조회
        List<TaskVO> tempTasks = taskDao.getTaskAll(reqVo);
        //같은 서명 분류의 같은 사람이 있을 경우, insert 취소
        if (reqVo.getTaskDetailUserId() != null) {
            boolean exists = tempTasks.stream()
                    .anyMatch(item -> reqVo.getTaskDetailDiv().equals(item.getTaskDetailDiv())
                            && reqVo.getTaskDetailUserId().equals(item.getTaskDetailUserId())
                            && tempReqInfoKey.equals(item.getReqInfoKey())
                    );
            if (exists) return reqVo;
        }

        //서명 없으면 오늘 날짜로 Task 마스터 생성
        if (tempTasks == null || tempTasks.isEmpty()) {
            //오늘 날짜 세팅
            LocalDate today = LocalDate.now();
            reqVo.setTaskYear(String.valueOf(today.getYear()));
            reqVo.setTaskMonth(String.format("%02d", today.getMonthValue()));
            reqVo.setTaskDay(String.format("%02d", today.getDayOfMonth()));
            //Task 마스터 조회

            TaskVO masterYn = taskDao.getTaskMasterYn(reqVo);
            if (masterYn == null) {
                //Task 마스터 생성
                taskDao.insertTask(reqVo);
            } else {
                //Task 마스터 정보 세팅
                reqVo.setTaskYear(masterYn.getTaskYear());
                reqVo.setTaskMonth(masterYn.getTaskMonth());
                reqVo.setTaskDay(masterYn.getTaskDay());
                reqVo.setTaskSeq(masterYn.getTaskSeq());
            }
        } else {
            // 기존 날짜 세팅
            reqVo.setTaskYear(tempTasks.get(0).getTaskYear());
            reqVo.setTaskMonth(tempTasks.get(0).getTaskMonth());
            reqVo.setTaskDay(tempTasks.get(0).getTaskDay());
            reqVo.setTaskSeq(tempTasks.get(0).getTaskSeq());
        }

        //Task 디테일 생성
        reqVo.setReqInfoKey(tempReqInfoKey); // doc_seq, doc_detail_seq가 있는 원본 키를 디테일에 삽입
        taskDao.insertTaskDetail(reqVo);

        return reqVo;
    }

    // TASK - 서명 완료/완료 취소
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateTaskComplete(TaskVO reqVo) {
        if (reqVo.getCompleteDt() == null) {
            //담당자 확인 안되어있는 데이터 update
            taskDao.updateTaskChkByComplete(reqVo);
        }
        //서명 완료 시간 업데이트
        taskDao.updateTaskComplete(reqVo);
        return reqVo;
    }

    // TASK - 서명 취소
    public BaseVO updateTaskCancel(TaskVO reqVo) {
        taskDao.updateTaskCancel(reqVo);
        return reqVo;
    }

    // TASK - 서명 취소 (다중 서명자 처리)
    public BaseVO updateTaskCancelMulti(TaskVO reqVo) {
        taskDao.updateTaskCancelMulti(reqVo);
        return reqVo;
    }

    // TASK - 확인
    public BaseVO updateTaskChk(TaskVO reqVo) {
        taskDao.updateTaskChk(reqVo);
        return reqVo;
    }

    // TASK - 스케줄러(설비안전점검일지)
    @Transactional(rollbackFor = Throwable.class)
    public void insertChecklist() {
        //TODO: 설비안전점검일지 조회 (scheduler) - 새로 쿼리 짜야됨
//        TaskVO reqVo = taskDao.getSchChecklist();
//        taskDao.insertTask(reqVo);
    }


    // TASK - 스케줄러(설비안전점검일지)
    @Transactional(rollbackFor = Throwable.class)
    public void insertSaftyHealthObjectiveTask() {
        //안전 보건 목표 상세 테이블의 추진일정 월 구하기
        int currentMonth = LocalDate.now().getMonthValue();

        SafetyAndHealthObjectivesVO shoVo = new SafetyAndHealthObjectivesVO();

        setSchedule(shoVo, currentMonth);

        List<SafetyAndHealthObjectivesVO> shoVoList = taskDao.getSafetyAndHealthObjectivesDetailList(shoVo);

        for (SafetyAndHealthObjectivesVO vo : shoVoList) {
            List<HseKeyPerformanceResultVO> hkprVoList = taskDao.getDetailList(vo);
            if (hkprVoList.size() > 0) {

            } else {
                TaskVO taskVo = new TaskVO();

                LocalDate today = LocalDate.now();
                taskVo.setTaskYear(String.valueOf(today.getYear()));
                taskVo.setTaskMonth(String.format("%02d", today.getMonthValue()));
                taskVo.setTaskDay(String.format("%02d", today.getDayOfMonth()));

                String reqInfoKey = "KPR" + "^" + vo.getWriteYear() + "^" + vo.getDocNo() + "^" + vo.getDocSeq();

                taskVo.setCompId(vo.getCompId());
                taskVo.setUserId(vo.getUserId());
                taskVo.setClntId(vo.getClntId());
                taskVo.setTaskContent("HSE 중점 추진 실적 미등록");
                taskVo.setReqUserId(vo.getUserId());
                taskVo.setReqInfoKey(reqInfoKey);
                taskVo.setReqMenuId("2202");

                taskDao.insertTask(taskVo);

                taskVo.setTaskDetailDiv("notRegistered");
                taskVo.setTaskDetailContent("HSE 중점 추진 실적 미등록");
                taskVo.setTaskDetailHrId(vo.getHrId());

                taskDao.insertTaskDetail(taskVo);
            }

        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertHseKeyPerformanceTask() {
        //안전 보건 목표 상세 테이블의 추진일정 월 구하기
        int currentMonth = LocalDate.now().getMonthValue();

        SafetyAndHealthObjectivesVO shoVo = new SafetyAndHealthObjectivesVO();

        setSchedule(shoVo, currentMonth);

        List<HseKeyPerformanceResultVO> hkprVoList = taskDao.getHseKeyDetailList(shoVo);


        if (hkprVoList.size() > 0) {
            for (HseKeyPerformanceResultVO vo : hkprVoList) {
                // task 인풋
                TaskVO taskVo = new TaskVO();

                LocalDate today = LocalDate.now();
                taskVo.setTaskYear(String.valueOf(today.getYear()));
                taskVo.setTaskMonth(String.format("%02d", today.getMonthValue()));
                taskVo.setTaskDay(String.format("%02d", today.getDayOfMonth()));


                taskVo.setCompId(vo.getCompId());
                taskVo.setUserId(vo.getUserId());
                taskVo.setClntId(vo.getClntId());//tb_comp_client_map에서 compId로 조회해서 clntId 들고와서 넣기
                taskVo.setTaskContent("HSE 중점 추진 실적 미완료");
                taskVo.setReqUserId(vo.getUserId());
                String reqInfoKey = "KPR" + "^" + vo.getWriteYear() + "^" + vo.getDocNo() + "^" + vo.getDocSeq();
                taskVo.setReqInfoKey(reqInfoKey);
                taskVo.setReqMenuId("2202");

                taskDao.insertTask(taskVo);


                reqInfoKey = "KPR" + "^" + vo.getWriteYear() + "^" + vo.getDocNo() + "^" + vo.getDocSeq() + "^" + vo.getDocSeqDetail();
                taskVo.setReqInfoKey(reqInfoKey);
                taskVo.setTaskDetailDiv("notFinished");
                taskVo.setTaskDetailContent("HSE 중점 추진 실적 미완료");
                taskVo.setTaskDetailHrId(vo.getHrId());

                taskDao.insertTaskDetail(taskVo);

            }
        }

    }

    // *******************************
    // 안전점검표 스케쥴러
    // *******************************
    @Transactional(rollbackFor = Throwable.class)
    public void insertDailySafetyCheckListTask(List<SafetyChecklistVO> safetyCheckList) {
        LocalDate today = LocalDate.now();
        // 주말은 스케쥴러 제외함
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return;
        }

        if (!safetyCheckList.isEmpty()) {
            for (SafetyChecklistVO vo : safetyCheckList) {
                SpSearchVO searchVo = new SpSearchVO();
                searchVo.setSearchText(vo.getStdEqId());
                List<SafetyChecklistHrVO> hrList = taskDao.getSafetyCheckInspector(searchVo); // 해당 설비유형에 배정된 담당자 목록 조회
                if (!hrList.isEmpty()) {
                    for (SafetyChecklistHrVO hrVo : hrList) {
                        String clntId = hrVo.getClntId();
                        String userId = hrVo.getUserId();
                        String hrId = hrVo.getId();
                        String title = "";

                        // --Task vo 세팅 -------------------------------------------
                        TaskVO taskVo = new TaskVO();
                        taskVo.setTaskYear(String.valueOf(today.getYear()));
                        taskVo.setTaskMonth(String.format("%02d", today.getMonthValue()));
                        taskVo.setTaskDay(String.format("%02d", today.getDayOfMonth()));

                        taskVo.setCompId(vo.getCompId());
                        taskVo.setUserId(userId);
                        taskVo.setClntId(clntId);
                        taskVo.setTaskContent(vo.getTitle() + " 확인");
                        taskVo.setReqUserId(userId);
                        String reqInfoKey = vo.getDocType() + "^&" + vo.getWriteYear() + "^&" + vo.getDocNo();
                        taskVo.setReqInfoKey(reqInfoKey);
                        taskVo.setReqMenuId("2215"); // 받아야 함

                        // 1. 오늘 날짜에 안전점검표가 있는지 확인
                        SafetyInspectionLogVO logResult = taskDao.getSafetyInspectionLogsToday(hrVo);
                        if (logResult != null) {
                            // 오늘 날짜에 안전점검표가 있음
                            System.out.println("----------------------------------------------");
                            System.out.println("## 1. 오늘 날짜에 안전점검표가 있음 => " + userId);
                            System.out.println("----------------------------------------------");
                            // 2. 안점점검 실시했는지 확인
                            hrVo.setWriteYear(logResult.getWriteYear());
                            hrVo.setDocNo(logResult.getDocNo());
                            Boolean existsLog = taskDao.getExistsLogsToday(hrVo);
                            if (existsLog) {
                                // 안전점검을 실시함
                                System.out.println("----------------------------------------------");
                                System.out.println("## 2. 안전점검을 실시함 => " + userId);
                                System.out.println("----------------------------------------------");
                            } else {
                                // 안점점검을 실시하지 않음
                                System.out.println("----------------------------------------------");
                                System.out.println("## 2. 안전점검을 실시하지 않음 => " + userId);
                                System.out.println("----------------------------------------------");
                                title = "안전점검 미완료";

                                // -- 마스터 테이블 삽입 -----------------------------------------------------

                                String masterReqInfoKey = vo.getDocType() + "^&" + vo.getWriteYear() + "^&" + vo.getDocNo();
                                taskVo.setReqInfoKey(masterReqInfoKey);
                                TaskVO currentDoc = taskDao.getCurrentDocMasterTask(taskVo); // 현재 문서에 해당하는 Task 정보 조회
                                if (currentDoc == null) {
                                    taskDao.insertTask(taskVo);
                                } else {
                                    taskVo.setTaskSeq(currentDoc.getTaskSeq());
                                }

                                // -- 마스터 디테일 삽입 -----------------------------------------------------
                                reqInfoKey = logResult.getDocType() + "^&" + logResult.getWriteYear() + "^&" + logResult.getDocNo() + "^&" + logResult.getStdEqId() + "^&" + logResult.getEquipmentId();
                                taskVo.setReqInfoKey(reqInfoKey);
                                taskVo.setTaskDetailDiv("notFinished"); // 받아야 함
                                taskVo.setTaskDetailContent(vo.getTitle() + " " + title);
                                taskVo.setTaskDetailHrId(hrId);
                                TaskVO currentDocDetail = taskDao.getCurrentDocDetailTask(taskVo); // 현재 문서에 해당하는 Task 디테일 정보 조회
                                if (currentDocDetail == null) {
                                    taskDao.insertTaskDetail(taskVo);
                                }

                            }

                        } else {
                            // 오늘 날짜에 안전점검표가 없음
                            System.out.println("----------------------------------------------");
                            System.out.println("## 1. 오늘 날짜에 안전점검일지가 없음 => " + userId);
                            System.out.println("----------------------------------------------");
                            title = "안전점검일지 미등록";

                            System.out.println("######### task VO => " + taskVo);
                            TaskVO currentDoc = taskDao.getCurrentDocMasterTask(taskVo); // 현재 문서에 해당하는 Task 정보 조회
                            if (currentDoc == null) {
                                taskDao.insertTask(taskVo);
                            } else {
                                taskVo.setTaskSeq(currentDoc.getTaskSeq());
                            }

                            reqInfoKey = vo.getDocType() + "^&" + vo.getWriteYear() + "^&" + vo.getDocNo() + "^&" + hrVo.getStdEqId() + "^&" + hrVo.getEquipmentId();
                            taskVo.setReqInfoKey(reqInfoKey);
                            taskVo.setTaskDetailDiv("notRegistered"); // 받아야 함
                            taskVo.setTaskDetailContent(vo.getTitle() + " " + title);
                            taskVo.setTaskDetailHrId(hrId);
                            System.out.println("####### detail task " + taskVo);
                            TaskVO currentDocDetail = taskDao.getCurrentDocDetailTask(taskVo); // 현재 문서에 해당하는 Task 디테일 정보 조회
                            if (currentDocDetail == null) {
                                taskDao.insertTaskDetail(taskVo);
                            }
                        }
                    }
                }
            }
        }
    }

    public void getDailySafetyCheckListTask() {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchCd("0001");
        List<SafetyChecklistVO> taskList = taskDao.getSafetyCheckList(searchVo); // 점검주기가 일별인 안전점검표 목록 조회
        insertDailySafetyCheckListTask(taskList);
    }

    public void getMonthlySafetyCheckListTask() {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchCd("0002");
        List<SafetyChecklistVO> taskList = taskDao.getSafetyCheckList(searchVo); // 점검주기가 월별인 안전점검표 목록 조회
        insertDailySafetyCheckListTask(taskList);
    }

    // 안전점검항목 점검 완료 시 테이블 complete_dt 업데이트
    @Transactional(rollbackFor = Throwable.class)
    public void updateSafetyCheckListTask(SafetyInspectionLogVO vo) {
        TaskVO reqVo = new TaskVO();
        reqVo.setCompId(vo.getCompId());
        reqVo.setTaskYear(vo.getCheckDt().substring(0, 4));
        reqVo.setTaskMonth(vo.getCheckDt().substring(4, 6));
        reqVo.setTaskDay(vo.getCheckDt().substring(6, 8));
        if (vo.getCompleted()) {
            System.out.println("# 안전점검항목 점검 완료 " + vo);
            reqVo.setTaskDetailDiv("finished");
            reqVo.setChkTaskDetailDiv("notFinished");
            String reqInfoKey = vo.getDocType() + "^&" + vo.getWriteYear() + "^&" + vo.getDocNo() + "^&" + vo.getStdEqId() + "^&" + vo.getEquipmentId();
            reqVo.setReqInfoKey(reqInfoKey);
        } else {
            System.out.println("# 안전점검항목 등록 완료 " + vo);
            SafetyInspectionLogVO originPK = taskDao.originSafetyCheckListTask(vo);
            if(originPK == null){
               return;
            }
            String reqInfoKey = originPK.getDocType() + "^&" + originPK.getWriteYear() + "^&" + originPK.getDocNo() + "^&" + vo.getStdEqId() + "^&" + vo.getEquipmentId();
            reqVo.setReqInfoKey(reqInfoKey);
            reqVo.setTaskDetailDiv("registered");
            reqVo.setChkTaskDetailDiv("notRegistered");
        }
        taskDao.updateSafetyCheckListTask(reqVo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteApprovalInfo(String targetType, String targetId) {
        TaskVO deleteVO = new TaskVO();
        deleteVO.setTargetType(targetType);
        deleteVO.setTargetId(targetId);
        taskDao.deleteApprovalInfo(deleteVO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertMonthlySafetyCheckListTask() {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchCd("0002");
        List<SafetyChecklistVO> safetyCheckList = taskDao.getSafetyCheckList(searchVo);


        if (safetyCheckList.size() > 0) {

            for (SafetyChecklistVO vo : safetyCheckList) {

                searchVo.setSearchText(vo.getStdEqId());
                List<SafetyChecklistHrVO> hrList = checkListDao.getSafetyCheckInspector(searchVo);

                if (hrList.size() > 0 && hrList.get(0).getHrList().size() > 0 && hrList.get(0).getHrList().get(0).getId() != null) {

                    //일단은 첫번째 순서의 id를 넣음
                    String hrId = hrList.get(0).getHrList().get(0).getId();
                    String userId = hrList.get(0).getHrList().get(0).getUserId();
                    String clntId = hrList.get(0).getHrList().get(0).getClntId();

                    TaskVO taskVo = new TaskVO();

                    LocalDate today = LocalDate.now();
                    taskVo.setTaskYear(String.valueOf(today.getYear()));
                    taskVo.setTaskMonth(String.format("%02d", today.getMonthValue()));
                    taskVo.setTaskDay(String.format("%02d", today.getDayOfMonth()));

                    DayOfWeek dayOfWeek = today.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        if ("Y".equals(vo.getOnlyWeekday())) {
                            return;
                        }
                    }


                    taskVo.setCompId(vo.getCompId());
                    taskVo.setUserId(userId);
                    taskVo.setClntId(clntId);//tb_comp_client_map에서 compId로 조회해서 clntId 들고와서 넣기
                    taskVo.setTaskContent(vo.getTitle() + " 확인");
                    taskVo.setReqUserId(userId);
                    String reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo() + "^" + vo.getDocSeq();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setReqMenuId("2215"); // 받아야 함

                    taskDao.insertTask(taskVo);


                    reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo() + "^" + vo.getDocSeq();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setTaskDetailDiv("notRegistered"); // 받아야 함
                    taskVo.setTaskDetailContent(vo.getTitle() + " 확인");
                    taskVo.setTaskDetailHrId(hrId);

                    taskDao.insertTaskDetail(taskVo);

                }

            }
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertSafetyCheckListNotFinishedTask() {
        SpSearchVO searchVo = new SpSearchVO();
        LocalDate today = LocalDate.now();
        String startDt = String.valueOf(today.getYear()) + String.format("%02d", today.getMonthValue()) + String.format("%02d", today.getDayOfMonth());
        searchVo.setStartDate(startDt);
        searchVo.setEndDate(startDt);
        List<HseInspectionsVO> inspectVoList = taskDao.getSafetyInspections(searchVo);
        if (inspectVoList.size() > 0) {
            for (HseInspectionsVO vo : inspectVoList) {
                if (!vo.isDone()) {
                    TaskVO taskVo = new TaskVO();
                    taskVo.setCompId(vo.getCompId());
                    taskVo.setUserId(vo.getUserId());
                    taskVo.setClntId(vo.getClntId());//tb_comp_client_map에서 compId로 조회해서 clntId 들고와서 넣기
                    taskVo.setTaskContent(vo.getTitle() + " 미완료");
                    taskVo.setReqUserId(vo.getUserId());
                    String reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setReqMenuId("2215"); // 받아야 함

                    taskDao.insertTask(taskVo);


                    reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setTaskDetailDiv("notFinished"); // 받아야 함
                    taskVo.setTaskDetailContent(vo.getTitle() + " 미완료");
                    taskVo.setTaskDetailHrId(vo.getHrId());

                    taskDao.insertTaskDetail(taskVo);
                }
            }
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertTrainingPlanResultReportTask() {
        SpSearchVO searchVo = new SpSearchVO();
        LocalDate today = LocalDate.now();
        String startDt = String.valueOf(today.getYear()) + String.format("%02d", today.getMonthValue()) + String.format("%02d", today.getDayOfMonth());
        searchVo.setStartDate(startDt);
        List<HseInspectionsVO> inspectVoList = taskDao.getTrainingPlanImpl(searchVo);
        if (inspectVoList.size() > 0) {
            for (HseInspectionsVO vo : inspectVoList) {
                if (vo.getHrId() != null) {
                    TaskVO taskVo = new TaskVO();
                    taskVo.setCompId(vo.getCompId());
                    taskVo.setUserId(vo.getUserId());
                    taskVo.setClntId(vo.getClntId());//tb_comp_client_map에서 compId로 조회해서 clntId 들고와서 넣기
                    taskVo.setTaskContent(vo.getTitle() + " - 결과 보고서 미작성");
                    taskVo.setReqUserId(vo.getUserId());
                    String reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setReqMenuId("2189"); // 받아야 함

                    taskDao.insertTask(taskVo);


                    reqInfoKey = vo.getDocType() + "^" + vo.getWriteYear() + "^" + vo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setTaskDetailDiv("notRegistered"); // 받아야 함
                    taskVo.setTaskDetailContent(vo.getTitle() + " - 결과 보고서 미작성");
                    taskVo.setTaskDetailHrId(vo.getHrId());

                    taskDao.insertTaskDetail(taskVo);
                }
            }
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertApprovalNotFinishedTask() {
        SpSearchVO searchVo = new SpSearchVO();

        List<TrainingResultReportVO> trainingList = taskDao.getTrainingResult(searchVo);

        for (TrainingResultReportVO trainingVo : trainingList) {
            UtilsVO uvo = new UtilsVO();
            uvo.setTargetId(trainingVo.getDocType());
            uvo.setTargetType(trainingVo.getWriteYear() + trainingVo.getDocNo() + "1");
            uvo.setType("attendee");
            List<UtilsVO> approvalList = taskDao.getApprovalInfo(uvo);
            for (UtilsVO utilVo : approvalList) {
                if (utilVo.getSignature() == null) {
                    TaskVO taskVo = new TaskVO();
                    taskVo.setCompId(utilVo.getCompId());
                    taskVo.setUserId(utilVo.getUserId());
                    taskVo.setClntId(utilVo.getClntId());//tb_comp_client_map에서 compId로 조회해서 clntId 들고와서 넣기
                    taskVo.setTaskContent(trainingVo.getTitle() + " 미완료");
                    taskVo.setReqUserId(utilVo.getUserId());
                    String reqInfoKey = utilVo.getDocType() + "^" + utilVo.getWriteYear() + "^" + utilVo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setReqMenuId("2189"); // 받아야 함

                    taskDao.insertTask(taskVo);


                    reqInfoKey = utilVo.getDocType() + "^" + utilVo.getWriteYear() + "^" + utilVo.getDocNo();
                    taskVo.setReqInfoKey(reqInfoKey);
                    taskVo.setTaskDetailDiv("notFinished"); // 받아야 함
                    taskVo.setTaskDetailContent(trainingVo.getTitle() + " 미완료");
                    taskVo.setTaskDetailHrId(utilVo.getHrId());

                    taskDao.insertTaskDetail(taskVo);
                }
            }

        }
    }

    /* 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출) */
    @Transactional(rollbackFor = Throwable.class)
    public void updateTaskUseYn(UtilsVO reqVo) {
        taskDao.updateTaskUseYn(reqVo);
    }
    public void deleteAlarmHis() {
        AlarmVO vo = new AlarmVO();
        SystemCodeVO majorCd = new SystemCodeVO();
        majorCd.setMajorCd("C0047"); //알림 삭제 주기 코드
        List<SystemMinorCodeVO> sysVO = systemCodeDao.findDetail(majorCd);
        vo.setDeleteCycle(Integer.parseInt(sysVO.get(0).getExtra1()));
        taskDao.deleteNoticeAlarm(vo);           //공지사항 삭제
        taskDao.deleteEmergencyAlarm(vo);        //비상상황 삭제
        taskDao.deleteTrainingPlanImplAlarm(vo); //안전보건 삭제
    }

    private static void setSchedule(SafetyAndHealthObjectivesVO scheduleVO, int currentMonth) {
        for (int i = 1; i <= 12; i++) {
            if (i < currentMonth) {
                setScheduleValue(scheduleVO, i, "Y");
            } else {

            }
        }
    }

    private static void setScheduleValue(SafetyAndHealthObjectivesVO scheduleVO, int month, String value) {
        switch (month) {
            case 1:
                scheduleVO.setActionSchedule1(value);
                break;
            case 2:
                scheduleVO.setActionSchedule2(value);
                break;
            case 3:
                scheduleVO.setActionSchedule3(value);
                break;
            case 4:
                scheduleVO.setActionSchedule4(value);
                break;
            case 5:
                scheduleVO.setActionSchedule5(value);
                break;
            case 6:
                scheduleVO.setActionSchedule6(value);
                break;
            case 7:
                scheduleVO.setActionSchedule7(value);
                break;
            case 8:
                scheduleVO.setActionSchedule8(value);
                break;
            case 9:
                scheduleVO.setActionSchedule9(value);
                break;
            case 10:
                scheduleVO.setActionSchedule10(value);
                break;
            case 11:
                scheduleVO.setActionSchedule11(value);
                break;
            case 12:
                scheduleVO.setActionSchedule12(value);
                break;
        }
    }

//#endregion
}