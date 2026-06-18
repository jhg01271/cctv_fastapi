package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyMgmtOfHazardousMachineryVO extends BaseVO {
	@Schema(description = "작성년도", example = "")
	private String writeYear;
	
	@Schema(description = "문서타입", example = "")
    private String docType;

    @Schema(description = "문서번호", example = "")
    private String docNo;
	
	@Schema(description = "사업자 ID", example = "")
	private String compId;
	
	@Schema(description = "설비 ID", example = "")
	private String equipmentId;
	
	@Schema(description = "설비 관리번호", example = "")
	private String equipmentMgmtNum;
	
	@Schema(description = "설비 규격 및 용량", example = "")
	private String equipmentMfSpec;
	
	@Schema(description = "점검주기", example = "")
	private String inspectionCycle;
	
	@Schema(description = "검사 유효기간", example = "")
	private String inspectionExpiryDate;
	
	@Schema(description = "검사일", example = "")
	private String lastInspectionDate;
	
	@Schema(description = "차기 검사일", example = "")
	private String lastNextInspectionDate;
	
	@Schema(description = "사용여부", example = "")
	private String useYn;
	
	@Schema(description = "생성날짜", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정날짜", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;
	
	
	private String equipmentNm;
	private String stdEqId;
	private String stdEqNm;
}

