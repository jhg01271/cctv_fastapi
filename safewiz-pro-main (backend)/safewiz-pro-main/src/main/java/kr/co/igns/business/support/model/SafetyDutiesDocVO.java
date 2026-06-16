package kr.co.igns.business.support.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import java.util.List;

@Data

public class SafetyDutiesDocVO extends BaseVO{
    private String writeDate;
    private String title;
    private String subtitle;
    private String content;
    private String useYn;
    private String docType;
    private String label;
    private String orgnRoleType;
    private String orgnRoleId;
    private String desc; //안전업무 지정서 상세 - 안전업무 구분의 desc
    private List<SafetyDutiesUserVO> memberList;
    private List<SafetyDutiesUserVO> cardMemberList;

    private String checked;
    private String appointmentDt;
    private String dismissalDt;

    private String dismissalTitle;
    private String dismissalSubtitle;
}
