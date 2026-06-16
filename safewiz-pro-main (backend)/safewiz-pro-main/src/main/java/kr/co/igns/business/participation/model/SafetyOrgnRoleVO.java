package kr.co.igns.business.participation.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyOrgnRoleVO extends BaseVO {
    private String compId;
    private String orgnRoleId;
    private String orgnRoleNm;
    private Integer ordSeq;
    private String useYn;
    private String type;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
