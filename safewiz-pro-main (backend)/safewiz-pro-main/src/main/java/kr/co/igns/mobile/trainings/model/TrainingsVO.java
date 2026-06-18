package kr.co.igns.mobile.trainings.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TrainingsVO {
	@Schema(description = "교육관리 달력 count", example = "")
    private List<Map<String, Integer>> calendarCount;
	
	@Schema(description = "교육 ID", example = "")
	private String id;
	@Schema(description = "교육 코드 값", example = "")
	private String category;
	@Schema(description = "교육 장소", example = "")
	private String location;
	@Schema(description = "제목", example = "")
	private String title;
	@Schema(description = "교육 내용", example = "")
	private String content;
	@Schema(description = "관여자 및 서명 수", example = "")
	private Map<String, Object> participantCount;
	@Schema(description = "조회자의 참석자 포함 여부", example = "")
	private boolean myParticipation;
	@Schema(description = "서명 여부", example = "")
	private Boolean signed;
	@Schema(description = "교육 시작일시", example = "")
	private String startedAt;
	@Schema(description = "교육 종료일시", example = "")
	private String endedAt;
	@Schema(description = "생성일시", example = "")
	private String createdAt;
}
