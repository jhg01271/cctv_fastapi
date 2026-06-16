package kr.co.igns.business.alarm.dao.postgres;

import java.util.List;

import kr.co.igns.business.alarm.model.TrainingPlanImplAlarmVO;
import kr.co.igns.system.base.model.HrVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import kr.co.igns.business.alarm.model.AlarmVO;

@Mapper
@Repository
public interface AlarmDAO {
	//웹,앱 푸시 알림 전송 이력
	List<AlarmVO> getAlarmList(AlarmVO vo);
	void updateReadNoticeAlarm(AlarmVO vo);      // 공지사항 알림 읽음 처리
	void updateReadEmergencyAlarm(AlarmVO vo);   //비상 상황 알림 읽음 처리
	void insertEmergencyAlarmHistory(AlarmVO vo);//비상 상황 알림 내역 저장

	String getSelectedTokenUser(HrVO user);
	List<HrVO> getSelectedTokenUserInfo(HrVO user);
	int insertTrainingPlanImplAlarmHistory(TrainingPlanImplAlarmVO vo);
	void updateReadTrainingPlanImplAlarm(AlarmVO vo);

}
