package kr.co.igns.system.setting.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;


@Data
public class SystemMinorCodeVO extends BaseVO {
	private String compId;
    private String majorCd;
    private String majorNm;
	private String minorNm;
	private String minorCd;
	private String majorDesc;
	private int ordSeq;
	private String extra1;
	private String extra2;
	private String extra3;
	private String extra4;
	private String extra5;
	private String remark;
    private String useYn;
	private String name;
	private boolean __created__;
}
