package kr.co.igns.mobile.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnsignedSignaturesVO {
	@Schema(description = "미서명 문서 ID", example = "")
	private String id;
	@Schema(description = "항목 카테고리", example = "")
	private String category;
	@Schema(description = "카드 뷰에 표시될 내용", example = "")
	private String content;
	@Schema(description = "문서내 특정 행위 시작일시", example = "")
	private String startedAt;
	@Schema(description = "문서 생성일시", example = "")
	private String createdAt;
}
