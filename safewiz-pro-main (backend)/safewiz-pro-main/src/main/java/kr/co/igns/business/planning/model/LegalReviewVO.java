package kr.co.igns.business.planning.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LegalReviewVO extends BaseVO {

    @Schema(description = "작성년도", example = "")
    private String writeYear;

    @Schema(description = "문서 타입", example = "")
    private String docType;

    @Schema(description = "문서 번호", example = "")
    private String docNo;

    @Schema(description = "사업장 ID", example = "")
    private String compId;

    @Schema(description = "법규 검토서 명", example = "")
    private String legalReviewNm;

    @Schema(description = "사용유무", example = "Y")
    private String useYn;
    
    @Schema(description = "작성일자", example = "")
    private String createdAt;

    @Schema(description = "수정일자", example = "")
    private String updateAt;
    ;
    
    @Schema(description = "등록 건수", example = "")
    private String cnt;
    ;
    

    @Schema(description = "cmd", example = "")
    private String cmd;
    
    @Schema(description = "checkYn", example = "")
    private String checkedYn;
    
    // 출력물
//    	@Schema(description = "중간 페이지", example = "Y")
//    	private String subPage;
//
//    	@Schema(description = "전체 페이지", example = "Y")
//    	private Integer page;
//
//    	@Schema(description = "내부 페이지", example = "Y")
//    	private Integer localPage;
    	
    	@Schema(description = "전체 출력 유무", example = "Y")
    	private boolean printAll = false;
    	
    	@Schema(description = "gridData 조회 조건", example = "")
        private List<String> gridValue;
    	
    	@Schema(description = "gridDataAll 조회 조건", example = "")
        private List<LegalReviewVO> gridValueAll;
     	
    	
    	private List<OrganizationStatusVO> children = new ArrayList<>();
    

}
