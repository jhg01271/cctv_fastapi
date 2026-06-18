package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.io.InputStream;

@Data
public class ProvisionAndMgmtPPEVO extends BaseVO {

    @Schema(description = "작성년도", example = "")
    private String writeYear;
    
    @Schema(description = "문서구분", example = "")
    private String docType;

    @Schema(description = "문서번호", example = "")
    private String docNo;

    @Schema(description = "사업장 ID", example = "")
    private String compId;

    @Schema(description = "구분", example = "")
    private String gubun;

    @Schema(description = "수불조직 ID", example = "")
    private String orgnId;

    @Schema(description = "수불조직 명", example = "")
    private String orgnNm;

    @Schema(description = "수불 월", example = "")
    private String receiptMonth;
    
    @Schema(description = "수불 일자", example = "")
    private String receiptDt;

    @Schema(description = "최근 수불 일자", example = "")
    private String lastReceiptDt;
    
    @Schema(description = "수불 건수", example = "")
    private String receiptCnt;

    @Schema(description = "품목 ID", example = "")
    private String ppeId;

    @Schema(description = "품목 순번", example = "")
    private int ppeOrd;
    
    @Schema(description = "품목 명", example = "")
    private String ppeNm;

    @Schema(description = "이전 재고 수량", example = "")
    private int lastStoreQty;
    
    @Schema(description = "입고 수량", example = "")
    private int inQty;
    
    @Schema(description = "지급 수량", example = "")
    private int outQty;
    
    @Schema(description = "재고 수량", example = "")
    private int storeQty;

    @Schema(description = "수량", example = "")
    private String qty;
	
    @Schema(description = "지급 기준", example = "")
    private String provisionStandard;
    
    @Schema(description = "지급 대상", example = "")
    private String provisionTarget;
    
    @Schema(description = "순번", example = "")
    private int ordSeq;
    
    @Schema(description = "사용 유무", example = "")
    private String useYn;
    
    @Schema(description = "비고", example = "")
    private String remark;
    
    @Schema(description = "담당자", example = "")
    private String manager;
    
    @Schema(description = "담당자 명", example = "")
    private String managerNm;

    @Schema(description = "최신 수불정보 유무", example = "")
    private String isLast;
    
	@Schema(description = "생성일자", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정일자", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;

    @Schema(description = "서명", example = "")
    private String signature;
	
	@Schema(description = "수정자 ID", example = "")
    private int page;
	
	@Schema(description = "수정자 ID", example = "")
    private int subPage;
	@Schema(description = "수정자 ID"
			, example = "")
    private int localPage;
}
