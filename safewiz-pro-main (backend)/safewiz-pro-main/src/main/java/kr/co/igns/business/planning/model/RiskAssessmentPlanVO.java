package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.Data;

@Data
public class RiskAssessmentPlanVO extends BaseVO {

    @Schema(description = "조직ID")
    private String orgnId;

    @Schema(description = "위험성평가 계획명")
    private String planNm;

    @Schema(description = "평가기간 시작일", example = "YYYY-MM-DD")
    private String startDate;

    @Schema(description = "평가기간 종료일", example = "YYYY-MM-DD")
    private String endDate;

    @Schema(description = "평가일자", example = "YYYY-MM-DD")
    private String assessmentDate;

    @Schema(description = "완료일자", example = "YYYY-MM-DD")
    private String assessmentComplDate;

    @Schema(description = "위험성평가 조직도 차수")
    private String riskAssessmentChartId;

    @Schema(description = "위험성평가 구분", example = "시스템코드(C0026) [ Initial: 최초평가 / regular: 정기평가 / occasional: 수시평가]")
    private String riskAssessmentGubun;

    @Schema(description = "위험성 추정기준", example = "시스템코드(C0027) [ 3b3: 3*3 / 5b4: 5*4 ]")
    private String riskAssessmentStandards;

    @Schema(description = "허용 가능 위험성 기준", example = "시스템코드(C0040) [ 3b3_1: 낮음 / 5b4_1: 낮음 / 3a_l: 하 ]")
    private String riskAllowanceStandards;

    @Schema(description = "설명")
    private String desc;

    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    
    @Schema(description = "감소대책 등록 요인수", example = "")
    private String reductionCount;
    
    @Schema(description = "유해요인건수", example = "N")
    private String implCount;
    
    @Schema(description = "감소대책등록율", example = "N")
    private String implPercentage;
    
    @Schema(description = "개선조치 건수", example = "N")
    private String reductionCompletedCount;
    
    @Schema(description = "감소대책 건수", example = "N")
    private String reductionAllCount;
    
    @Schema(description = "개선이행율", example = "N")
    private String reductionPercentage;
    
    @Schema(description = "평균 위험도 개선전", example = "N")
    private String implAvgRisk;
    
    @Schema(description = "평균 위험도 개선후", example = "N")
    private String reduAvgRisk;

    @Schema(description = "시스템코드 리스트", example = "N")
    List<SystemMinorCodeVO> systemCodeList;

    @Schema(description = "위험성평가 공정/설비/물질 참조 작성년도", example = "")
    private String cpemWriteYear;

    @Schema(description = "위험성평가 공정/설비/물질 참조 문서 번호", example = "")
    private String cpemDocNo;

    @Schema(description = "위험성평가 공정/설비/물질 작성년도", example = "")
    private String cpemConfirmWriteYear;

    @Schema(description = "위험성평가 공정/설비/물질 문서 번호", example = "")
    private String cpemConfirmDocNo;

    @Schema(description = "위험성평가 공정/설비/물질 수정할 작성년도", example = "")
    private String cpemUpdateWriteYear;

    @Schema(description = "위험성평가 공정/설비/물질 수정할 문서 번호", example = "")
    private String cpemUpdateDocNo;

    @Schema(description = "위험성평가 공정/설비/물질 참조 확정 여부", example = "N")
    private YesNo confirmYn;
    /*
     * 데이터 추출용
     */
    // 이행
    @Schema(description = "공정 리스트")
    private List<ClassificationProcessEquipMsdsAnalysisVO> classPrcsList;
    
    // 서명
    @Schema(description = "작성자 서명 여부")
    private String writerSignYn;
    @Schema(description = "결재상태", example = "null: 작성중, writer: 작성완료, reviewer: 검토완료, approver: 승인완료")
    private String signStatus;

    // 조직
    @Schema(description = "조직명")
    private String orgnNm;

    // 위험성평가 조직도
    @Schema(description = "조직도명")
    private String chartNm;
    @Schema(description = "조직도 데이터")
    private String chartData;
}
