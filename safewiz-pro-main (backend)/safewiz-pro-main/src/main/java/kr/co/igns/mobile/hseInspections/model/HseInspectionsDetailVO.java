package kr.co.igns.mobile.hseInspections.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HseInspectionsDetailVO {
	@Schema(description = "안전점검 점검표 템플릿 ID", example = "")
	private String id;
	@Schema(description = "안전점검 템플릿 제목", example = "")
	private String title;
	@Schema(description = "점검완료 여부", example = "")
	private boolean done;
	@Schema(description = "점검 위치", example = "")
	private String location;
	@Schema(description = "체크리스트의 불량 판정에 대한 추가 조치사항", example = "")
	private String action;
	@Schema(description = "점검 시작시간", example = "")
	private String startedAt;
	@Schema(description = "점검 종료시간", example = "")
	private String endedAt;
	@Schema(description = "작성일시", example = "")
	private String createdAt;
	@Schema(description = "점검 대상", example = "")
	private Map<String, Object> checkTarget;
	@Schema(description = "점검 수행자", example = "")
	private Map<String, Object> checker;
	@Schema(description = "점검자 정보", example = "")
	private List<Map<String, Object>> checkList;
}
