package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyInspectionLogSearchVO extends SpSearchVO {
    private String stdEqId;
    private String equipmentId;
    private String checkDt;
}
