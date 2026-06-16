package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class JobCompetencyAssessmentDetailVO extends BaseVO {


    @Schema(description = "직무 코드", example = "0001")
    private String jobId;

    @Schema(description = "직무 이름", example = "안전보건관리책임자")
    private String jobNm;

    @Schema(description = "요구수준 코드", example = "0001")
    private String demandLevelId;

    @Schema(description = "요구수준 이름", example = "상")
    private String demandLevelNm;

    @Schema(description = "현재수준 코드", example = "0001")
    private String currentLevelId;

    @Schema(description = "현재수준 이름", example = "상")
    private String currentLevelNm;

    @Schema(description = "과목/과정", example = "Safety Management")
    private String trainingCourse;

    @Schema(description = "과목/과정", example = "Safety Management")
    private String trainingCourseNm;

    @Schema(description = "교육기관", example = "Safety Management")
    private String trainingInstitute;

    @Schema(description = "교육기관", example = "Safety Management")
    private String trainingInstituteNm;

    @Schema(description = "필요 교육 내용", example = "교육 내용")
    private String trainingContent;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "직무분야", example = "")
    private String jobCategory;

    @Schema(description = "안전업무 구분 ID", example = "")
    private String orgnRoleId;

    @Schema(description = "안전업무 구분 타입", example = "")
    private String orgnRoleType;

    @Schema(description = "순번", example = "")
    private Integer ordSeq;

    @Schema(description = "비고", example = "")
    private String remark;

    @Schema(description = "안전업무 구분 타입", example = "")
    private String jobLevelId;

    @Schema(description = "안전업무 구분 타입", example = "")
    private String jobLevelNm;


}
