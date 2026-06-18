package kr.co.igns.business.task.dao.postgres;

import kr.co.igns.business.alarm.model.AlarmVO;
import kr.co.igns.business.dashboard.model.DashboardPerVO;
import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.impl.model.SafetyChecklistHrVO;
import kr.co.igns.business.impl.model.SafetyChecklistVO;
import kr.co.igns.business.impl.model.SafetyInspectionLogVO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsSearchVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskDAO {

	List<TaskVO> getTask(SpSearchVO searchVo);
	TaskVO getTaskMasterYn(TaskVO reqVo);
	List<TaskVO> getTaskAll(TaskVO reqVo);	//임시
	TaskVO getCurrentDocMasterTask(TaskVO reqVo); // 마스터 테이블에 이미 데이터가 존재하는지 확인
	TaskVO getCurrentDocDetailTask(TaskVO reqVo); // 디테일 테이블에 이미 데이터가 존재하는지 확인
	int insertTask(TaskVO reqVo);
	int insertHseJob(TaskVO reqVo);
	int updateTaskComplete(TaskVO reqVo);
	int updateTaskChkByComplete(TaskVO reqVo);
	int updateTaskCancel(TaskVO reqVo);
	int updateTaskCancelMulti(TaskVO reqVo);
	int updateTaskChk(TaskVO reqVo);

	List<DashboardPerVO> getTaskDashboard(SpSearchVO searchVo);

	//Task 디테일
	int insertTaskDetail(TaskVO reqVo);

    // 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출)
    int updateTaskUseYn(UtilsVO reqVo);
	//스케줄러
	TaskVO getSchChecklist();
	
	// 스케줄러 관련 
	List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesDetailList(SafetyAndHealthObjectivesVO vo);
	
	List<HseKeyPerformanceResultVO> getDetailList(SafetyAndHealthObjectivesVO vo);
	List<HseKeyPerformanceResultVO> getHseKeyDetailList(SafetyAndHealthObjectivesVO vo);
	
	List<SafetyChecklistVO> getSafetyCheckList(SpSearchVO searchVo); // 점검주기가 일별인 안전점검표 목록 조회
	List<SafetyChecklistHrVO> getSafetyCheckInspector(SpSearchVO searchVo); // 해당 설비유형에 배정된 담당자 목록 조회
    SafetyInspectionLogVO getSafetyInspectionLogsToday(SafetyChecklistHrVO searchVo); // 오늘 날짜에 안전점검표가 있는지 확인
    SafetyInspectionLogVO originSafetyCheckListTask(SafetyInspectionLogVO vo); // 안전점검일지 등록 시 TASK에 저장된 정보 호출용도 
    int updateSafetyCheckListTask(TaskVO vo); // 점검사항이 점검 완료되었을 때
    int cancelSafetyCheckListTask(TaskVO vo); // 안전점검 task cancelDt update
    int deleteApprovalInfo(TaskVO vo); // 안전점검 삭제 시 저장된 서명정보 제거
    Boolean getExistsLogsToday(SafetyChecklistHrVO searchVo); // 안점점검 실시했는지 확인

	void deleteNoticeAlarm(AlarmVO vo);          //읽은 메세지 중, 일정 기간이 지난 공지사항 알림 삭제
	void deleteEmergencyAlarm(AlarmVO vo);       //읽은 메세지 중, 일정 기간이 지난 비상 상황 알림 삭제
	void deleteTrainingPlanImplAlarm(AlarmVO vo);//읽은 메세지 중, 일정 기간이 지난 안전보건 교육 실시 계획서 알림 삭제

	List<HseInspectionsVO> getSafetyInspections(SpSearchVO searchVo);
	
	List<UtilsVO> getApprovalInfo(UtilsVO utilsVO);
	List<TrainingResultReportVO> getTrainingResult(SpSearchVO searchVo);
	
	List<HseInspectionsVO> getTrainingPlanImpl(SpSearchVO searchVo);



}
