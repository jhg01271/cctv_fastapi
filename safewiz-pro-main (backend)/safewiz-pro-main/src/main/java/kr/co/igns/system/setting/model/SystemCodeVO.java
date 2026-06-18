package kr.co.igns.system.setting.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SystemCodeVO extends BaseVO {
	private String compId;
    private String majorCd;
	private String majorDiv;
	private String majorType;
	private String majorNm;
	private String remark;
    private String useYn;
	private String name;
	private boolean __created__;
}
