package kr.co.igns.mobile.signatures.model;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CommitteeSignaturesVO {
	@Schema(description = "company", example = "")
    private List<Map<String, Object>> company;
	@Schema(description = "partner", example = "")
    private List<Map<String, Object>> partner;
	@Schema(description = "employee", example = "")
    private List<Map<String, Object>> employee;
	@Schema(description = "writer", example = "")
    private Map<String, Object> writer;
	@Schema(description = "reviewer", example = "")
    private Map<String, Object> reviewer;
	@Schema(description = "approver", example = "")
    private Map<String, Object> approver;
}
