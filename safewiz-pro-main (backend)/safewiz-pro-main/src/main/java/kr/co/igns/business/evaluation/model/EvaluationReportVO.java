package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.impl.model.PermitToWorkDetailVO;
import kr.co.igns.business.impl.model.PermitToWorkTypeVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EvaluationReportVO extends BaseVO {
    @Schema(description = "평가일자")
    private String evaluationDt;
    @Schema(description = "평가대상")
    private String evaluationTarget;
    @Schema(description = "사용 여부", example = "Y")
    private String useYn;

    private int detailCnt;
    private int complianceCnt;
    private int performanceCnt;

    @Schema(description = "평가항목 리스트")
    private List<EvaluationReportDetailVO> detailList;

    @Schema(description = "준수평가표 데이터 리스트")
    private List<EvaluationReportComplianceVO> complianceList;

    //결재 상태(결과,준수,성과)
    @Schema(description = "결재 상태-결과")
    private String approvalStatusR;
    @Schema(description = "결재 상태-준수")
    private String approvalStatusC;
    @Schema(description = "결재 상태-성과")
    private String approvalStatusP;



}
