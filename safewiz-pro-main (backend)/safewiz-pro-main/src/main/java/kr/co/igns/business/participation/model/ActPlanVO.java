package kr.co.igns.business.participation.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ActPlanVO extends BaseVO {
    @Schema(description = "전사 목표 번호")
    private String compObjectiveId;
    @Schema(description = "전사 목표")
    private String companyObjective;
    @Schema(description = "중점 추진 목표")
    private String actionObjective;
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
    @Schema(description = "추진 일정 (1분기)")
    private YesNo actionSchedule1;
    @Schema(description = "추진 일정 (2분기)")
    private YesNo actionSchedule2;
    @Schema(description = "추진 일정 (3분기)")
    private YesNo actionSchedule3;
    @Schema(description = "추진 일정 (4분기)")
    private YesNo actionSchedule4;
    @Schema(description = "성과 타입")
    private String performanceDiv;

    //예산
    @Schema(description = "예산")
    private int budget;

    //예산
    @Schema(description = "전사 목표 - 있는 데이터 사용, 신규, 수정 구분")
    private String newYn;

    //조직 매핑
    @Schema(description = "조직 ID 리스트")
    private List<String> orgnIdList;
    @Schema(description = "조직 리스트(조회용)")
    private List<BaseMapVO> orgnList;
    @Schema(description = "조직 ID")
    private String orgnId;
    @Schema(description = "조직 명")
    private String orgnNm;
}
