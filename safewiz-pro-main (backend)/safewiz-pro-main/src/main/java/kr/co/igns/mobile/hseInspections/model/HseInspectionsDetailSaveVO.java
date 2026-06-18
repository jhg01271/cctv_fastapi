package kr.co.igns.mobile.hseInspections.model;

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
public class HseInspectionsDetailSaveVO {
    private String compId;
    private String stdEqId;
    private String equipmentId;
    private String checkDt;
    private String checkStart;
    private String checkEnd;
    private String setupLocation;
    private String nonComplianceAction;
    private List<CheckList> checkList;
    
    private String createdBy;
 
    private String updatedBy;
    
    private String hrNm;
    
    private String hrId;
    
    private String orgnNm;
    
    private String jbrpNm;
    
    private String writeYear;
    
	private String docType;

    private String docNo;
    
    private String docSeq;
    
    @JsonCreator
    public HseInspectionsDetailSaveVO(
        @JsonProperty("compId") String compId, 
        @JsonProperty("stdEqId") String stdEqId, 
        @JsonProperty("equipmentId") String equipmentId, 
        @JsonProperty("checkDt") String checkDt,
        @JsonProperty("checkStart") String checkStart, 
        @JsonProperty("checkEnd") String checkEnd, 
        @JsonProperty("setupLocation") String setupLocation, 
        @JsonProperty("nonComplianceAction") String nonComplianceAction, 
        @JsonProperty("checkList") List<CheckList> checkList, 
        @JsonProperty("createdBy") String createdBy,
        @JsonProperty("updatedBy") String updatedBy,
        @JsonProperty("hrNm") String hrNm,
        @JsonProperty("hrId") String hrId,
        @JsonProperty("orgnNm") String orgnNm,
        @JsonProperty("jbrpNm") String jbrpNm
    ) {
        this.compId = compId;
        this.stdEqId = stdEqId;
        this.equipmentId = equipmentId;
        this.checkDt = checkDt;
        this.checkStart = checkStart;
        this.checkEnd = checkEnd;
        this.setupLocation = setupLocation;
        this.nonComplianceAction = nonComplianceAction;
        this.checkList = checkList;
        this.createdBy = SecurityUtil.getCurrentMemberId();
        this.updatedBy = SecurityUtil.getCurrentMemberId();
        this.hrNm = hrNm;
        this.hrId = hrId;
        this.orgnNm = orgnNm;
        this.jbrpNm = jbrpNm;
    }
    
    @Data
    public static class CheckList {
        private String category;
        private String check;
        private Boolean acceptable;
        private String docSeq;
        private String acceptableYn;
        private String nonCompliantYn;
        
        @JsonCreator
        public CheckList(
            @JsonProperty("category") String category,
            @JsonProperty("check") String check,
            @JsonProperty("acceptable") Boolean acceptable,
            @JsonProperty("docSeq") String docSeq
        ) {
            this.category = category;
            this.check = check;
            this.acceptable = acceptable;
            this.docSeq = docSeq;
        }
        
        public void setAcceptableYn() {
        	if(acceptable == null) {
                acceptableYn = "N";
                nonCompliantYn = "N";
        	} else if (acceptable) {
                acceptableYn = "Y";
                nonCompliantYn = "N";
            } else {
                acceptableYn = "N";
                nonCompliantYn = "Y";
            }
        }
    }
    
}