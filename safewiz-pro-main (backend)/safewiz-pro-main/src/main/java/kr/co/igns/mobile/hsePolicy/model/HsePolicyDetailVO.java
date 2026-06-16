package kr.co.igns.mobile.hsePolicy.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HsePolicyDetailVO {
	@Schema(description = "안전보건 경영방침 ID", example = "")
	private String id;
	@Schema(description = "안전보건 경영방침 제목", example = "")
	private String title;
	@Schema(description = "최종 안전보건 경영방침 유무 ", example = "")
	private Boolean latest;
	@Schema(description = "현재 사용자 동의 여부", example = "")
	private Boolean agreement;
	@Schema(description = "근로자 의견 개수", example = "")
	private int totalReplyCount;
	@Schema(description = "근로자 의견 리스트", example = "")
	private List<Map<String, Object>> recentlyReplies;
	@Schema(description = "createdAt", example = "")
	private String createdAt;
}
