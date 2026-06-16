package kr.co.igns.mobile.hseInquries.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class HseInquriesSaveVO {
    @JsonProperty("content")
	private String keyContent;
    @JsonProperty("compId")
	private String compId;
    @JsonProperty("hrId")
	private String hrId;
    
    private String writeYear;
    
    private String docType;
    
    private String docNo;
    
    private String receiptDt;
    
    private String createdBy;
    
    private String updatedBy;
    
    @JsonCreator
    public HseInquriesSaveVO(
        @JsonProperty("content") String keyContent,
        @JsonProperty("compId") String compId,
        @JsonProperty("hrId") String hrId,
        @JsonProperty("createdBy") String createdBy,
        @JsonProperty("updatedBy") String updatedBy
    ) {
        this.keyContent = keyContent;
        this.compId = compId;
        this.hrId = hrId;
        this.createdBy = SecurityUtil.getCurrentMemberId();
        this.updatedBy = SecurityUtil.getCurrentMemberId();
    }
}
