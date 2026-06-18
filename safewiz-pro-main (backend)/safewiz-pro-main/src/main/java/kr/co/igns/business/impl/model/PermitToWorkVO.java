package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class PermitToWorkVO extends BaseVO {
    @Schema(description = "작업일자")
    private String workDt;
    @Schema(description = "작업시간")
    private String workTime;
    @Schema(description = "작업 시작시간")
    private String workStart;
    @Schema(description = "작업 종료시간")
    private String workEnd;
    @Schema(description = "작업 장소")
    private String workplace;
    @Schema(description = "설비 id")
    private String equipmentId;
    @Schema(description = "설비명")
    private String equipmentNm;
    @Schema(description = "작업 내용")
    private String workContent;
    @Schema(description = "협력사 id")
    private String partCompId;
    @Schema(description = "협력사명")
    private String partCompNm;

    @Schema(description = "점검사항")
    List<SafetyWorkChecklistVO> inspectionList; // getSafetyWorkChecklistById
    @Schema(description = "종합 의견(전문가 기술)")
    private String overallOpinion;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "점검항목 리스트")
    private List<PermitToWorkDetailVO> detailList;
    @Schema(description = "작업종류 리스트")
    private List<PermitToWorkTypeVO> workTypeList;

    //인원 매핑
    @Schema(description = "작업자 리스트")
    private List<HrVO> hrList;
}
