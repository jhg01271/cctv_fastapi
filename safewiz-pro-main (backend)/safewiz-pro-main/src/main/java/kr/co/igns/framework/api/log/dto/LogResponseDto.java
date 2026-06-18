package kr.co.igns.framework.api.log.dto;

import kr.co.igns.framework.api.log.entity.LogEntity;
import lombok.Getter;

@Getter
public class LogResponseDto {

	private Long logIdx;
	private String logLevel;
	private String location;
	private String message;
	private String logDate;
	private String throwable;
	
	public LogResponseDto(LogEntity entity) {
		this.logIdx = entity.getLogIdx();
		this.logLevel = entity.getLogLevel();
		this.location = entity.getLocation();
		this.message = entity.getMessage();
		this.logDate = entity.getLogDate();
		this.throwable = entity.getThrowable();
	}
}
