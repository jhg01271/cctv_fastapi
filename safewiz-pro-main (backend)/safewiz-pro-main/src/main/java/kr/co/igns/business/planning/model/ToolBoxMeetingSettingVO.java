package kr.co.igns.business.planning.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToolBoxMeetingSettingVO extends BaseVO {
    private String settingKey;
    private String settingName;
    private String settingValue;
}