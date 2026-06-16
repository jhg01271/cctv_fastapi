package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AnnualOhsInternalAuditPlanVO extends BaseVO {

    @Schema(description = "조직ID", example = "202411260001")
    private String orgnId;

    @Schema(description = "조직명", example = "ESG사업부")
    private String orgnNm;

    @Schema(description = "심사일정 1월", example = "Y")
    private String auditSchedule1;

    @Schema(description = "심사일정 2월", example = "Y")
    private String auditSchedule2;

    @Schema(description = "심사일정 3월", example = "Y")
    private String auditSchedule3;

     @Schema(description = "심사일정 4월", example = "Y")
    private String auditSchedule4;

    @Schema(description = "심사일정 5월", example = "Y")
    private String auditSchedule5;

    @Schema(description = "심사일정 6월", example = "Y")
    private String auditSchedule6;

     @Schema(description = "심사일정 7월", example = "Y")
    private String auditSchedule7;

    @Schema(description = "심사일정 8월", example = "Y")
    private String auditSchedule8;

    @Schema(description = "심사일정 9월", example = "Y")
    private String auditSchedule9;

     @Schema(description = "심사일정 10월", example = "Y")
    private String auditSchedule10;

    @Schema(description = "심사일정 11월", example = "Y")
    private String auditSchedule11;

    @Schema(description = "심사일정 12월", example = "Y")
    private String auditSchedule12;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "비고 리스트", example = "[...]")
    private List<AnnualOhsInternalAuditPlanDetailVO> remarkList;
}
