package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class WHMgmtGuideVO extends BaseVO {
    @Schema(description = "근로자")
    private String hrId;
    @Schema(description = "근로자명")
    private String hrNm;
    @Schema(description = "조직Id")
    private String orgnId;
    @Schema(description = "조직")
    private String orgnNm;
    @Schema(description = "성별")
    private String sex;
    @Schema(description = "나이")
    private int age;
    @Schema(description = "근속연수")
    private int serviceYears;
    @Schema(description = "상담일자")
    private String counselDt;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    //상담일지 갯수
    private int counselCnt;
    private int requestCnt;

    // 인원 Mapping
    @Schema(description = "인원(상담자, 내담자)")
    private List<HrVO> hrListH;
    private List<HrVO> hrListR;
    private List<HrVO> hrListC;

    //detail 조회용
    @Schema(description = "검진 소견")
    private String healthOpinion;
    @Schema(description = "사후관리 소견")
    private String aftercareOpinion;
    @Schema(description = "업무 수행 적합 여부")
    private YesNo workAptitudeYn;
    @Schema(description = "조치 사항")
    private String correctiveAction;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo counselUseYn;
    //하나의 vo에서 저장, 삭제 관리하기 위함
    private YesNo counselChecked;
    private String counselCmd = "U";
    
    @Schema(description = "상담시간")
    private String counselTime;
    @Schema(description = "상담 시작 시간")
    private String counselStart;
    @Schema(description = "상담 종료 시간")
    private String counselEnd;
    @Schema(description = "상담 장소")
    private String counselLocation;
    @Schema(description = "상담 경위")
    private String counselReason;
    @Schema(description = "상담 내용")
    private String counselContent;
    @Schema(description = "상담 결과")
    private String counselResult;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo requestUseYn;
    //하나의 vo에서 저장, 삭제 관리하기 위함
    private YesNo requestChecked;
    private String requestCmd = "U";
}
