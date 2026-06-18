package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AuditExecutionPlanVO extends BaseVO {

    @Schema(description = "작성일자", example = "YYYYMMDD")
    private String writeDt;

    @Schema(description = "심사명", example = "")
    private String auditNm;

    @Schema(description = "심사구분(C0042 minorCd)", example = "")
    private String auditDiv;

    @Schema(description = "심사구분명(C0042 minorNm)", example = "")
    private String auditDivNm;

    @Schema(description = "심사 목적 및 범위", example = "")
    private String auditPurposeScope;

    @Schema(description = "심사 준비 요청 사항", example = "")
    private String auditRequest;

    @Schema(description = "배포처", example = "")
    private String distribute;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "결재 상태", example = "Y")
    private String approvalStatus;

    @Schema(description = "조직 리스트", example = "[...]")
    private List<AuditExecutionPlanDetailVO> auditOrgnList;
}
