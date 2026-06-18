package kr.co.igns.business.evaluation.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;


@Data
public class OhsCorrectiveRequestVO extends BaseVO {
    // ========= 결과보고서 정보 (response only) =======
    
    @Schema(description = "심사명", example = "")
    private String auditNm;

    @Schema(description = "대상 조직 ID", example = "")
    private String orgnId;
    
    @Schema(description = "대상 조직 명", example = "")
    private String orgnNm;
    
    @Schema(description = "인원 리스트", example = "[...]")
    private List<HrVO> auditHrList;
    
    // ================================================

    @Schema(description = "작성일자", example = "YYYYMMDD")
    private String writeDt;

    @Schema(description = "회신요구일", example = "YYYYMMDD")
    private String reqDt;

    @Schema(description = "부적합 사항")
    private String nonconformities;

    @Schema(description = "원인 분석 및 재발 방지대책")
    private String measures;

    @Schema(description = "유효성 확인 결과")
    private String validate;
    
    @Schema(description = "유효성 점검 필요 여부")
    private String required;

    @Schema(description = "최종 점검일자")
    private String finalCheckDt;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "서명여부")
    private String approvalStatus;
}
