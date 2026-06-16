package kr.co.igns.mobile.hsePolicy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HsePolicyVO {
	@Schema(description = "안전보건 경영방침 ID", example = "")
	private String id;
	@Schema(description = "안전보건 경영방침 제목", example = "")
	private String title;
	@Schema(description = "총 근로자 의견수", example = "")
	private int totalReplyCount;
	@Schema(description = "최종 안전보건 경영방침 유무", example = "")
	private Boolean latest;
	@Schema(description = "createdAt", example = "")
	private String createdAt;
}
