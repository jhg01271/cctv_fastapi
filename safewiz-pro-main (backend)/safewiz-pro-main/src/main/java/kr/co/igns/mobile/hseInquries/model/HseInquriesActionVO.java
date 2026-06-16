package kr.co.igns.mobile.hseInquries.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class HseInquriesActionVO {
	private String writeYear;
	private String docType;
	private String docNo;
	private String actionContent;
	private String actionDt;
	private String replyDt;
	private String actionOrgnId;
	private String updatedBy;
	
    @JsonCreator
    public HseInquriesActionVO(
	    @JsonProperty("action")
		String actionContent,
		@JsonProperty("actionedAt")
		String actionDt,
		@JsonProperty("repliedAt")
		String replyDt,
		@JsonProperty("actorId")
		String actionOrgnId,
		@JsonProperty("updatedBy")
		String updatedBy
    ) {
        this.actionContent = actionContent;
        this.actionDt = convertToDateFormat(actionDt);
        this.replyDt = convertToDateFormat(replyDt);
        this.actionOrgnId = actionOrgnId;
        this.updatedBy = SecurityUtil.getCurrentMemberId();
    }

    private String convertToDateFormat(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);
            return dateTime.format(outputFormatter);
        } catch (Exception e) {
        	throw new IllegalArgumentException("Invalid Date: " + e);
        }
    }
}
