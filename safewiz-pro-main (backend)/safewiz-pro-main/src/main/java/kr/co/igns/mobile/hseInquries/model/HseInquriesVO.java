package kr.co.igns.mobile.hseInquries.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HseInquriesVO {
	@Schema(description = "상담 ID", example = "")
    private String id;
	@Schema(description = "문의내용", example = "")
    private String content;
	@Schema(description = "작성자", example = "")
    private Map<String, Object> writer;
	@Schema(description = "접수일시", example = "")
    private String receiptedAt;
	@Schema(description = "회신일시", example = "")
    private String repliedAt;
	@Schema(description = "조치일시", example = "")
    private String actionedAt;
	@Schema(description = "조치 내용", example = "")
    private String action;
	@Schema(description = "조치 비고", example = "")
    private String remark;
	@Schema(description = "조치자", example = "")
    private Map<String, Object> actor;
	@Schema(description = "생성일시", example = "")
    private String createdAt;
}
