package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class HazmatInoutDetailVO extends BaseVO {
    @Schema(description = "입출고일자")
    private String inoutDt;
    @Schema(description = "입고 수량")
    private Integer inQty;
    @Schema(description = "출고 수량")
    private Integer outQty;
    @Schema(description = "재고 수량")
    private Integer storeQty;
    @Schema(description = "입출고 건수")
    private Integer totalQty;
    @Schema(description = "비고")
    private String remark;
}
