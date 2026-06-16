package kr.co.igns.framework.api.notification.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class NotificationRequestDto {
	
	@JsonIgnore
	private String userId;
	private Map<String , Boolean> notification;
	
}
