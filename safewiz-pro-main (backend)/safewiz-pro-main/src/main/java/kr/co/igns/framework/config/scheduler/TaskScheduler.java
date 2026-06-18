package kr.co.igns.framework.config.scheduler;

import kr.co.igns.business.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.scheduler
 * @ 파일명		: TaskScheduler.java
 * @ 기능명 		: 스케줄러 생성, cron 언어 사용
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Component
public class TaskScheduler {

	//동작 확인 완료 : 2025.02.17
	@Autowired
	private TaskService taskService;

	@Scheduled(cron = "0 0 10 * * *") // 매일 오전 10시에 실행
//	@Scheduled(cron = "0/10 * * * * *")
	public void schInsertChecklist() {
//        taskService.insertSaftyHealthObjectiveTask();
//        taskService.insertHseKeyPerformanceTask();
//		taskService.insertDailySafetyCheckListTask();
//		taskService.insertSafetyCheckListNotFinishedTask();
		taskService.insertTrainingPlanResultReportTask();
	}

	@Scheduled(cron = "0 0 10 * * *") // 매일 오전 10시에 실행
//	@Scheduled(cron = "0/10 * * * * *") // 테스트용 10초마다 호출
	public void schSafetyChecklist() {
		// 안전점검표 스케쥴러
		taskService.getDailySafetyCheckListTask(); // TASK 추가할 항목을 추가함
		taskService.getMonthlySafetyCheckListTask(); // TASK 추가할 항목을 추가함
	}

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
//	@Scheduled(cron = "0/10 * * * * *") // 테스트용 10초마다 호출
	public void schDeleteAlarmHis() {
		//읽은 알림 삭제 스케쥴러
		taskService.deleteAlarmHis();
	}

}
 