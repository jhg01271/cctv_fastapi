package kr.co.igns.framework.comm.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FcmMessageVO {
	private String topic; // 보내는 topic
	private String fromUserId; // 보내는사람
	private String targetUserId; // 받는사람
	private List<String> targetUserIdList; // 받는 사람 리스트
	private String routerNm; // 알람유형
	private String title; // 제목
	private String content; // 내용
	Map<String, Object> params;// 파라미터

}
