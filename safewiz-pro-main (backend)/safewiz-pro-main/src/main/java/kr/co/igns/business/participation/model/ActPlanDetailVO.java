package kr.co.igns.business.participation.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ActPlanDetailVO extends BaseVO {
    private String docSeqO;
    private String writeYearO;
    private String docTypeO;
    private String docNoO;

    @Schema(description = "전사 목표명")
    private String companyObjective;
    @Schema(description = "전사 목표")
    private String compObjectiveId;
    @Schema(description = "중점 추진 목표")
    private String actionObjective;
    @Schema(description = "세부 항목 번호")
    private String detailItemId;
    @Schema(description = "세부 항목")
    private String detailItem;
    @Schema(description = "세부 계획")
    private String detailPlan;
    @Schema(description = "성과 구분(목표)")
    private String performanceTypeO;
    @Schema(description = "성과 반복 타입(목표)")
    private String performanceRepeatO;
    @Schema(description = "성과 건수(목표)")
    private int performanceCntO;
    @Schema(description = "성과 사용자 입력")
    private String performanceCustomO;
    @Schema(description = "성과 구분")
    private String performanceType;
    @Schema(description = "성과 구분명")
    private String performanceTypeNm;
    @Schema(description = "성과 반복 타입")
    private String performanceRepeat;
    @Schema(description = "성과 반복 타입명")
    private String performanceRepeatNm;
    @Schema(description = "성과 건수")
    private int performanceCnt;
    @Schema(description = "성과 사용자 입력")
    private String performanceCustom;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "순번")
    private int ordSeq;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;

    @Schema(description = "추진 일정 (1월)")
    private YesNo actionSchedule1;
    @Schema(description = "추진 일정 (2월)")
    private YesNo actionSchedule2;
    @Schema(description = "추진 일정 (3월)")
    private YesNo actionSchedule3;
    @Schema(description = "추진 일정 (4월)")
    private YesNo actionSchedule4;
    @Schema(description = "추진 일정 (5월)")
    private YesNo actionSchedule5;
    @Schema(description = "추진 일정 (6월)")
    private YesNo actionSchedule6;
    @Schema(description = "추진 일정 (7월)")
    private YesNo actionSchedule7;
    @Schema(description = "추진 일정 (8월)")
    private YesNo actionSchedule8;
    @Schema(description = "추진 일정 (9월)")
    private YesNo actionSchedule9;
    @Schema(description = "추진 일정 (10월)")
    private YesNo actionSchedule10;
    @Schema(description = "추진 일정 (11월)")
    private YesNo actionSchedule11;
    @Schema(description = "추진 일정 (12월)")
    private YesNo actionSchedule12;

    @Schema(description = "추진 일정 (1분기)")
    private YesNo actionSchedule1O;
    @Schema(description = "추진 일정 (2분기)")
    private YesNo actionSchedule2O;
    @Schema(description = "추진 일정 (3분기)")
    private YesNo actionSchedule3O;
    @Schema(description = "추진 일정 (4분기)")
    private YesNo actionSchedule4O;
    
    @Schema(description = "세부계획 건수")
    private int detailItemCnt;
    
    @Schema(description = "예산")
    private int budget;
    @Schema(description = "실적금액(합계)")
    private int performanceAmount;

    //조직 매핑
    @Schema(description = "조직 리스트(조회용)")
    private List<BaseMapVO> orgnList;

}
