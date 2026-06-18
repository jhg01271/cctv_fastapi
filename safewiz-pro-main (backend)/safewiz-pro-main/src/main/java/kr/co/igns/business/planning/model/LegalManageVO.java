package kr.co.igns.business.planning.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter
@Getter
@Data
public class LegalManageVO extends BaseVO {
//	extends BaseVO
    @Schema(description = "작성년도", example = "")
    private String writeYear;

    @Schema(description = "문서 타입", example = "")
    private String docType;

    @Schema(description = "문서 번호", example = "")
    private String docNo;

    @Schema(description = "사업장 ID", example = "")
    private String compId;
    
    @Schema(description = "데이터 타입", example = "")
    private String dataType;

    @Schema(description = "법규명", example = "")
    private String legalNm;

    @Schema(description = "사용유무", example = "Y")
    private String useYn;

    @Schema(description = "구분", example = "")
    private String divFg;

    @Schema(description = "제,개정일자", example = "")
    private String revisionAt;
    
    @Schema(description = "제,개정자", example = "")
    private String revisionBy;
    
    @Schema(description = "작성일자", example = "")
    private String createdAt;
    
    @Schema(description = "작성자", example = "")
    private String createdBy;

    @Schema(description = "수정일자", example = "")
    private String updatedAt;
    
    @Schema(description = "수정자", example = "")
    private String updatedBy;
    
    @Schema(description = "결재상태", example = "")
    private String approvalStatus;
    
    @Schema(description = "cmd", example = "")
    private String cmd;
    
    @Schema(description = "확정여부", example = "N")
    private String confirmedYn;
    
    @Schema(description = "확정일자", example = "20250416")
    private String confirmedDt;
    
    @Schema(description = "법규 ID", example = "00001")
    private String legalId;
    
    @Schema(description = "법규 코드", example = "1")
    private String legalCd;
    
    @Schema(description = "정렬 순서", example = "1")
    private Integer ordSeq;

    @Schema(description = "비고")
    private String desc;
 	
    @Schema(description = "법규 관리 체크여부", example = "true")
    private boolean checked;
    
 // 출력물
// 	@Schema(description = "중간 페이지", example = "Y")
// 	private String subPage;
//
// 	@Schema(description = "전체 페이지", example = "Y")
// 	private Integer page;
//
// 	@Schema(description = "내부 페이지", example = "Y")
// 	private Integer localPage;
 	
 	@Schema(description = "전체 출력 유무", example = "Y")
 	private boolean printAll = false;
 	
 	@Schema(description = "gridData 조회 조건", example = "")
    private List<String> gridValue;
 	
    @Schema(description = "체크여부", example = "")
    private String checkYn;
 	

 	
 	private List<OrganizationStatusVO> children = new ArrayList<>();

}
