package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class FacilityEquipManageVO extends BaseVO {

    @Schema(description = "시설/설비 id", example = "202411010001")
    private String facilitisId;

    @Schema(description = "고객사id", example = "202408200001")
    private String compId;

    @Schema(description = "시설/설비 명", example = "시설")
    private String facilitisNm;

    @Schema(description = "정렬순서", example = "1")
    private String ordSeq;
    
    @Schema(description = "사용여부", example = "Y")
    private String useYn;
    
    @Schema(description = "시설설명", example = "설명")
    private String facilitisDesc;
    
    @Schema(description = "파일 확장자", example = "jpg")
    private String extension;
    
    @Schema(description = "썸네일 id", example = "202401010000")
    private String thumbnailId;
    
    @Schema(description = "상태값", example = "I")
    private String cmd;
    
    @Schema(description = "생성일자", example = "2024.11.01")
    private String createdAt;
    
    @Schema(description = "생성자id", example = "abcd1234")
    private String createdBy;
    
    @Schema(description = "수정일자", example = "2024.11.01")
    private String updatedAt;
    
    @Schema(description = "수정자id", example = "abcd1234")
    private String updatedBy;
}
