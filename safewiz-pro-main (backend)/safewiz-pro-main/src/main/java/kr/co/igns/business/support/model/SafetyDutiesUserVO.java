package kr.co.igns.business.support.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyDutiesUserVO extends BaseVO{
    private String hrId;
    private String hrNm;
    private String orgnNm;
    private String jbrpNm;
    private String logoId;
    private String useYn;
    private String appointmentDt;
    private String dismissalDt;
}
