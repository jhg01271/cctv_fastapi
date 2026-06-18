package kr.co.igns.business.evaluation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AuditExecutionPlanDetailVO extends BaseVO {


    @Schema(description = "문서번호", example = "00001")
    private String docSeq;

    @Schema(description = "심사일자", example = "YYYYMMDD")
    private String auditDt;

    @Schema(description = "심사시간", example = "hh:mm ~ hh:mm")
    private String auditTime;

    @Schema(description = "심사시작 시간", example = "hhmm")
    private String auditStTime;

    @Schema(description = "심사 종료 시간", example = "hhmm")
    private String auditEdTime;

    @Schema(description = "대상 조직 ID", example = "")
    private String orgnId;

    @Schema(description = "대상 조직 명", example = "")
    private String orgnNm;

    @Schema(description = "심사내용", example = "")
    private String auditContent;

    @Schema(description = "비고", example = "")
    private String remark;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "인원 리스트", example = "[...]")
    private List<HrVO> auditHrList;

    @Schema(description = "평가 등록 여부", example = "Y")
    private String submitYn;

    @Schema(description = "결재 상태", example = "Y")
    private String approvalStatus;

    @Schema(description = "상세 정보", example = "[...]")
    private List<AuditResultDetaillVO> detail;

    @Schema(description="조직장 ID")
    private String orgnHeadId;

    @Schema(description="조직장 이름")
    private String orgnHeadNm;

    @Schema(description="평가 이름")
    private String auditNm;
    
    @Schema(description = "심사 대상 수", example = "0")
    private int auditDetailCount;
}


