package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class RiskOrgnRoleVO extends BaseVO {
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;

	@Schema(description = "위험성평가 조직 역할 ID", example = "00001")
	private String orgnRoleId;

	@Schema(description = "위험성평가 조직 역할명")
	private String orgnRoleNm;

	@Schema(description = "데이터 타입")
	private String type;

	@Schema(description = "위험성평가 조직 역할 순번")
	private Integer ordSeq;
    
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

	@Schema(description = "비고")
	private String remark;

	@Schema(description = "등록일자", example = "20240101")
	private String createdAt;

	@Schema(description = "등록자", example = "userId")
	private String createdBy;

	@Schema(description = "수정일자", example = "20240101")
	private String updatedAt;

	@Schema(description = "수정자", example = "userId")
	private String updatedBy;
}
