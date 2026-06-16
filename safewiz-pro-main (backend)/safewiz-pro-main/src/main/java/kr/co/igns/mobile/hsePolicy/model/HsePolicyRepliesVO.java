package kr.co.igns.mobile.hsePolicy.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HsePolicyRepliesVO {
	@Schema(description = "댓글 ID", example = "")
	private String id;
	@Schema(description = "의견 내용", example = "")
	private String content;
	@Schema(description = "createdAt", example = "")
	private String createdAt;
	@Schema(description = "writer", example = "")
	private Map<String, Object> writer;
}
