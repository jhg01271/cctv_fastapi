package kr.co.igns.system.base.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class OrgChartVO extends BaseVO{
    private String chartId;
    private String chartNm;
    private String confirmYn;
    private String confirmDt;
    private String chartData;
    private String orgCount;
    private String useYn;
    private String chartBlobData;
}
