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
public class CommitteesVO {
	@Schema(description = "문서타입", example = "")
    private String docType;
	@Schema(description = "위원회 달력 count", example = "")
    private List<Map<String, Integer>> calendarCount;
	
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
	@Schema(description = "안건수 (의결사항 수)", example = "")
	private int agreementCount;
	@Schema(description = "협의율", example = "")
	private int agreementRate;
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
}
