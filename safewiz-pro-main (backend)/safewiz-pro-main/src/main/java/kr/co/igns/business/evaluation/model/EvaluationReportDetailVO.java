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
public class EvaluationReportDetailVO extends BaseVO {
	@Schema(description = "평가항목")
    private String evaluationId;
    @Schema(description = "평가항목명")
    private String evaluationNm;
    @Schema(description = "평가사항명")
    private String evaluationItemNm;
    @Schema(description = "평가사항")
    private String evaluationItemId;
    @Schema(description = "평가 결과(적합 : Y, 부적합 : N)")
    private String resultYn;
    @Schema(description = "달성률")
    private int achievementRate;
    @Schema(description = "내용")
    private String contents;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용여부")
    private String useYn;
    @Schema(description = "체크 여부")
    private Boolean checked;

    //성과평가표
    @Schema(description = "평가대상")
    private String evaluationDt;
    @Schema(description = "평가일자")
    private String evaluationTarget;
    @Schema(description = "평가기준")
    private String evaluationMethod;
    private String evaluationCriteria;
    @Schema(description = "성과평가표 - 비고")
    private String performanceRemark;

    //메뉴, HSE 업무(DOC_TYPE) 매핑 관리 테이블 리스트
    List<EvaluationMenuVO> menuList;
}
