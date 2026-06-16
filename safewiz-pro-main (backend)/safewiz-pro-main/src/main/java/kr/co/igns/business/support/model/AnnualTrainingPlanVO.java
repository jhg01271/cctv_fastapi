package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class AnnualTrainingPlanVO extends BaseVO {
    @Schema(description = "과목/과정", example = "Safety Management")
    private String trainingCourse;

    @Schema(description = "과목/과정명", example = "Safety Management")
    private String trainingTypeNm;
    
    @Schema(description = "교육 시간", example = "30")
    private int trainingDuration;

    @Schema(description = "교육 기관", example = "ABC Institute")
    private String trainingInstitute;
    
    @Schema(description = "교육 기관명", example = "ABC Institute")
    private String trainingInstituteNm;

    @Schema(description = "강사", example = "John Doe")
    private String trainingInstructor;
    
    @Schema(description = "강사명", example = "John Doe")
    private String trainingInstructorNm;

    @Schema(description = "1월 교육 진행 계획", example = "N")
    private String trainingMonth1;

    @Schema(description = "2월 교육 진행 계획", example = "N")
    private String trainingMonth2;

    @Schema(description = "3월 교육 진행 계획", example = "N")
    private String trainingMonth3;

    @Schema(description = "4월 교육 진행 계획", example = "N")
    private String trainingMonth4;

    @Schema(description = "5월 교육 진행 계획", example = "N")
    private String trainingMonth5;

    @Schema(description = "6월 교육 진행 계획", example = "N")
    private String trainingMonth6;

    @Schema(description = "7월 교육 진행 계획", example = "N")
    private String trainingMonth7;

    @Schema(description = "8월 교육 진행 계획", example = "N")
    private String trainingMonth8;

    @Schema(description = "9월 교육 진행 계획", example = "N")
    private String trainingMonth9;

    @Schema(description = "10월 교육 진행 계획", example = "N")
    private String trainingMonth10;

    @Schema(description = "11월 교육 진행 계획", example = "N")
    private String trainingMonth11;

    @Schema(description = "12월 교육 진행 계획", example = "N")
    private String trainingMonth12;

    @Schema(description = "정렬 순서", example = "99")
    private int ordSeq;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;


    //인원 Mapping
    @Schema(description = "대상인원")
    private List<HrVO> HrList;

    @Schema(description = "순번", example = "N")
    private String remark;

}
