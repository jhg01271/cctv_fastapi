package kr.co.igns.business.participation.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyOrgChartVO extends BaseVO {
    private String chartId;
    private String chartNm;
    private String confirmYn;
    private String confirmDt;
    private String chartData;
    private String orgCount;
    private String useYn;
    private String chartBlobData;
}
