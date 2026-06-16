package kr.co.igns.mobile.signatures.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApprovalSignaturesVO {
	@Schema(description = "writer", example = "")
    private Map<String, Object> writer;
	@Schema(description = "reviewer", example = "")
    private Map<String, Object> reviewer;
	@Schema(description = "approver", example = "")
    private Map<String, Object> approver;
}