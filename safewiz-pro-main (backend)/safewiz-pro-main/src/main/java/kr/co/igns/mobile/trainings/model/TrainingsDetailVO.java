package kr.co.igns.mobile.trainings.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TrainingsDetailVO extends BaseVO {
	@Schema(description = "교육 ID", example = "")
	private String id;
	@Schema(description = "교육 코드 값", example = "")
	private String category;
	@Schema(description = "교육 내용", example = "")
	private String content;
	@Schema(description = "교육 장소", example = "")
	private String location;
	@Schema(description = "참석자 수 및 서명", example = "")
	private Map<String, Object> participantCount;
	@Schema(description = "서명 여부", example = "")
	private Boolean signed;
	@Schema(description = "교육 시작일시", example = "")
	private String startedAt;
	@Schema(description = "교육 종료일시", example = "")
	private String endedAt;
	@Schema(description = "생성일시", example = "")
	private String createdAt;
	@Schema(description = "교육제목", example = "")
	private String title;
	@Schema(description = "특기/기타/참고 사항", example = "")
	private String remark;
	@Schema(description = "강사정보", example = "")
	private Map<String, String> trainer;
	@Schema(description = "교육 목표", example = "")
	private String goal;
	@Schema(description = "기대 효과", example = "")
	private String expectedEffect;
	@Schema(description = "material", example = "")
	private Map<String, String> material;
	@Schema(description = "작성자", example = "")
	private Map<String, Object> writer;
	@Schema(description = "검토자", example = "")
	private Map<String, Object> reviewer;
	@Schema(description = "승인자", example = "")
	private Map<String, Object> approver;
}
