package kr.co.igns.mobile.toolBoxMeeting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToolBoxMeetingSearchVO {
    private String hrId;
    private String compId;
    private String startDate;
    private String endDate;
    private boolean onlyDone;
    private String id;
    private String createdBy;
}
