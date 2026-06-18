package kr.co.igns.mobile.hsePolicy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HsePolicyPdfVO {
	@Schema(description = "targetId", example = "")
	private String targetId;
	@Schema(description = "문서타입", example = "")
	private String targetType;
	@Schema(description = "데이터 타입 (출력물 0001, 이미지 0002)", example = "")
	private String dataType;
	@Schema(description = "latest", example = "")
	private boolean latest;
	@Schema(description = "제목", example = "")
	private String title;
	@Schema(description = "내용", example = "")
	private String content;
	@Schema(description = "하단", example = "")
	private String footer;
	@Schema(description = "작성일자", example = "")
	private String createdAt;
	@Schema(description = "compId", example = "")
	private String compId;
}
