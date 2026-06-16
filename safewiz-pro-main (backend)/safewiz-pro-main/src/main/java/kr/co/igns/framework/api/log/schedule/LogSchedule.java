package kr.co.igns.framework.api.log.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.igns.framework.api.log.service.LogService;
import kr.co.igns.framework.config.log.LogUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogSchedule {

	private final LogService logService;
	
//	@Scheduled(cron = "0 0 0 * * *") // 매일 00시 정각 실행 (초 분 시간 일 월)
//	//@Scheduled(cron = "0 * * * * *") // 1분 주기
//	public void scheduleTaskByMonth() {
//		String datetime = LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		try {
//			int rows = logService.removeLog(datetime);
//			LogUtil.ConsoleLogInfo(HttpStatus.OK, "SCHDULER LOG", "[스케줄러] 로그 " + rows + "건을 삭제하였습니다.", null);
//		}
//		catch (Exception e) {
//			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "SCHDULER Exception ::: scheduleTaskByMonth", e.getMessage(), null);
//		}
//	}
}
