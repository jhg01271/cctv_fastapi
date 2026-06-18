package kr.co.igns.mobile.committees.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CommitteesDetailVO {
	@Schema(description = "위원회 ID", example = "")
	private String id;
	@Schema(description = "위원회 코드 값", example = "")
	private String category;
	@Schema(description = "회의 장소", example = "")
	private String location;
	@Schema(description = "제목", example = "")
	private String title;
	@Schema(description = "정기 수시 구분 (REGULAR, ADHOC)", example = "")
	private String type;
	@Schema(description = "참석자 수 데이터", example = "")
	private Map<String, Object> participantCount;
	@Schema(description = "회의 안건", example = "")
	private String agenda;
	@Schema(description = "안건수 (의결사항 수)", example = "")
	private int agreementCount;
	@Schema(description = "협의율", example = "")
	private int agreementRate;
	@Schema(description = "의결 사항", example = "")
	private List<Map<String, Object>> agreements;
	@Schema(description = "기타사항 또는 참고사항", example = "")
	private String comment;
	@Schema(description = "현재 조회자의 참석 서명 여부", example = "")
	private Boolean signed;
	@Schema(description = "작성자", example = "")
	private Map<String, Object> writer;
	@Schema(description = "검토자", example = "")
	private Map<String, Object> reviewer;
	@Schema(description = "승인자", example = "")
	private Map<String, Object> approver;
	@Schema(description = "회의 시작일시", example = "")
	private String startedAt;
	@Schema(description = "생성일시", example = "")
	private String createdAt;
	@Schema(description = "회의 종료일시", example = "")
	private String endedAt;
}
