package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import kr.co.igns.business.planning.model.LegalManageVO;

@Setter
@Data
public class LegalManageDetailVO extends LegalManageVO{
    @Schema(description = "작성년도", example = "")
    private String writeYear;

    @Schema(description = "문서 타입", example = "")
    private String docType;

    @Schema(description = "문서 번호", example = "")
    private String docNo;

    @Schema(description = "문서 시퀀스", example = "")
    private String docSeq;

    @Schema(description = "조장", example = "")
    private String articleTitle;

    @Schema(description = "법규 규제치 및 요구사항", example = "")
    private String articleContent;
    
    @Schema(description = "법규명", example = "")
    private String legalNm;

    @Schema(description = "비고", example = "")
    private String remarkDc;

    @Schema(description = "제,개정일자", example = " 의견")
    private String revisionAt;

    @Schema(description = "사용여부", example = "")
    private String useYn;
    
    @Schema(description = "cmd", example = "")
    private String cmd;
    
    @Schema(description = "작성일자", example = "")
    private String createdAt;
    
    @Schema(description = "작성자", example = "")
    private String createdBy;

    @Schema(description = "수정일자", example = "")
    private String updatedAt;
    
    @Schema(description = "수정자", example = "")
    private String updatedBy;
    
    @Schema(description = "체크여부", example = "")
    private String checkYn;

    @Schema(description = "API 참조 ID", example = "")
    private String refId;
 	
    
}
