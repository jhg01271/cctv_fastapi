package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class HazmatInoutVO extends BaseVO {
    @Schema(description = "작업장 id")
    private String workplaceId;
    @Schema(description = "작업장명")
    private String workplaceNm;
    @Schema(description = "msds id")
    private String hazmatId;
    @Schema(description = "msds 명")
    private String hazmatNm;
    @Schema(description = "관용명(동의어)")
    private String msdsSynonym;
    @Schema(description = "CAS No.")
    private String casNo;
    @Schema(description = "일일취급량(0.0~)")
    private float dailyVolume;
    @Schema(description = "일일취급시간(0.0~24)")
    private float dailyTime;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "점검항목 리스트")
    private List<HazmatInoutDetailVO> detailList;

    //합계
    @Schema(description = "입고 수량 합계")
    private int totalInQty;
    @Schema(description = "출고 수량 합계")
    private int totalOutQty;
    @Schema(description = "재고 수량 합계")
    private int totalStoreQty;

    @Schema(description = "마지막 재고량")
    private int lastStoreQty;
}
