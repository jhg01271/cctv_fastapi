package kr.co.igns.mobile.riskReports.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RiskReportsSaveVO {
	// 근로자 의견
	private String content;
	// 작업장 id
	private String workplaceId;
	// 경혐담 업데이트 리스트
	private List<Experiences> experiences;
	// 경험담 생성 리스트
	private List<String> newExperiences;
	// 경험담 삭제 리스트
	private List<String> deleteExperiences;
	// 검토자 id
	private String reviewerHrId;
	// 근로자 참여 업데이트 유무
	private boolean workerOpinionUpdateFlag;
	// 검토자 업데이트 유무
	private boolean reviewerUpdateFlag;
	// 검토자 서명 flag
	private boolean writerSignFlag;
	
	private String compId;
	
	private String hrId;
	
	private String regDt;
	
	private String writeYear;
	
	private String docType;
	
	private String docNo;
	
    private String createdBy;
    
    private String updatedBy;
    
    private String type;
	
    @JsonCreator
    public RiskReportsSaveVO(
        @JsonProperty("content") String content,
        @JsonProperty("workplaceId") String workplaceId,
        @JsonProperty("experiences") List<Experiences> experiences,
        @JsonProperty("newExperiences") List<String> newExperiences,
        @JsonProperty("deleteExperiences") List<String> deleteExperiences,
        @JsonProperty("reviewerHrId") String reviewerHrId,
        @JsonProperty("workerOpinionUpdateFlag") boolean workerOpinionUpdateFlag,
        @JsonProperty("reviewerUpdateFlag") boolean reviewerUpdateFlag,
        @JsonProperty("writerSignFlag") boolean writerSignFlag,
        @JsonProperty("compId") String compId,
        @JsonProperty("hrId") String hrId,
        @JsonProperty("regDt") String regDt,
        @JsonProperty("createdBy")String createdBy,
        @JsonProperty("updatedBy") String updatedBy
    ) {
        this.content = content;
        this.workplaceId = workplaceId;
        this.experiences = experiences;
        this.newExperiences = newExperiences;
        this.deleteExperiences = deleteExperiences;
        this.reviewerHrId = reviewerHrId;
        this.workerOpinionUpdateFlag = workerOpinionUpdateFlag;
        this.reviewerUpdateFlag = reviewerUpdateFlag;
        this.writerSignFlag = writerSignFlag;
        this.compId = compId;
        this.hrId = SecurityUtil.getCurrentHrId();
        this.regDt = regDt;
        this.createdBy = SecurityUtil.getCurrentMemberId();
        this.updatedBy = SecurityUtil.getCurrentMemberId();
    }
    
    @Data
    public static class Experiences {
        private String id;
        private String content;
   
        @JsonCreator
        public Experiences(
            @JsonProperty("id") String id,
            @JsonProperty("content") String content
        ) {
            this.id = id;
            this.content = content;
        }
    }
}
