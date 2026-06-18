package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyInspectionLogVO extends BaseVO {
    @Schema(description = "안전점검표 제목")
    private String title;
    @Schema(description = "설비유형 id")
    private String stdEqId;
    @Schema(description = "설비유형명")
    private String stdEqNm;
    @Schema(description = "설비 id")
    private String equipmentId;
    @Schema(description = "설비명")
    private String equipmentNm;
    @Schema(description = "점검일자")
    private String checkDt;
    @Schema(description = "점검시간")
    private String checkTime;
    @Schema(description = "점검 시작시간")
    private String checkStart;
    @Schema(description = "점검 종료시간")
    private String checkEnd;
    @Schema(description = "설치 장소")
    private String setupLocation;
    @Schema(description = "규격 및 용량")
    private String mfSpec;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "점검자id")
    private String hrId;
    @Schema(description = "점검자")
    private String hrNm;
    @Schema(description = "점검자 조직")
    private String hrOrgnNm;
    @Schema(description = "불량 판정 조치 사항")
    private String nonComplianceAction;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    private String signature;
    private Boolean completed; // 점검 상태 - 전체 체크 시 true

    @Schema(description = "점검사항 리스트")
    private List<SafetyInspectionLogDetailVO> detailList;

    //조회 데이터가 없을때 설비 최신 정보 반환
    @Schema(description = "조직 리스트-설비")
    private List<BaseMapVO> orgnList;

    @Schema(description = "점검주기")
    private String inspectionCycle;
    @Schema(description = "점검주기명")
    private String inspectionCycleNm;
    @Schema(description = "점검실행 여부")
    private String inspectionYn;
}
