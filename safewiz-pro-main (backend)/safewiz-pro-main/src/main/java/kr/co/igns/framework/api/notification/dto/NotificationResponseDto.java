//package kr.co.igns.framework.api.notification.dto;
//
//import java.time.format.DateTimeFormatter;
//
//import kr.co.igns.framework.api.notification.entity.NotificationEntity;
//import lombok.Getter;
//
//@Getter
//public class NotificationResponseDto {
//	private String method;
//	private Boolean agreed;
//	private String createdAt;
//	private String updatedAt;
//
//	public NotificationResponseDto(NotificationEntity entity) {
//		this.method = entity.getMethod();
//		this.agreed = entity.getAgreed();
//		if (entity.getCreatedAt() != null) {
//			this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
//		}
//		if (entity.getUpdatedAt() != null) {
//			this.updatedAt = entity.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
//		}
//	}
//
//}
