package kr.co.igns.business.improvement.model;

import java.util.List;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyMgmtOfHazardousMachineryRequestVO extends BaseVO {
	
	private SafetyMgmtOfHazardousMachineryVO safetyMgmt;
	private List<SafetyMgmtOfHazardousMachineryOrgnVO> safetyMgmtOrgnList;
	private List<SafetyMgmtOfHazardousMachineryInspectionVO> safetyMgmtInspectionList;
}
