package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class FacilityEquipManageDetailVO extends BaseVO {

	@Schema(description = "작성년도", example = "2024")
    private String writeYear;
	
	@Schema(description = "문서타입", example = "WOOHA")
    private String docType;
	
	@Schema(description = "문서번호", example = "00001")
    private String docNo;
	
    @Schema(description = "시설/설비 id", example = "202411010001")
    private String equipmentId;

    @Schema(description = "고객사id", example = "202408200001")
    private String compId;

    @Schema(description = "시설/설비 명", example = "시설")
    private String equipmentNm;
    
    @Schema(description = "사용여부", example = "Y")
    private String useYn;
    
    @Schema(description = "썸네일 id", example = "202401010000")
    private String fileId;
    
    @Schema(description = "생성일자", example = "2024.11.01")
    private String createdAt;
    
    @Schema(description = "생성자id", example = "abcd1234")
    private String createdBy;
    
    @Schema(description = "수정일자", example = "2024.11.01")
    private String updatedAt;
    
    @Schema(description = "수정자id", example = "abcd1234")
    private String updatedBy;
}
