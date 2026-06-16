package kr.co.igns.mobile.toolBoxMeeting.model;

import kr.co.igns.mobile.common.model.HrVO;
import kr.co.igns.mobile.common.model.RiskFactorVO;
import kr.co.igns.mobile.common.model.WorkVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ToolBoxMeetingVO {
    private String id;
    private String compId;
    private String location;
    private String startedAt;
    private String endedAt;
    private boolean sameWorkingDate;
    private boolean includedRiskAssessment;
    private boolean signed;
    private String status;
    private List<RiskFactorVO> riskFactors;
    private List<RiskFactorVO> safetyCheckItems;
    private WorkVO work;
    private HrVO leader;
    private List<HrVO> attendees;
    private Map<String, Object> participantCount;
    private String checkResult;
    private String endMeeting;
    private String createdAt;
    private String createdBy;
}
