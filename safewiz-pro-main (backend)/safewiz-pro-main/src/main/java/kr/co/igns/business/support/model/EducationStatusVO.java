package kr.co.igns.business.support.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class EducationStatusVO extends BaseVO {
    @Schema(description = "인원 ID", example = "202408200001")
    private String hrId;

    @Schema(description = "인원 이름", example = "홍길동")
    private String hrNm;

    @Schema(description = "직위 명", example = "대표")
    private String jbrpNm;
    
    @Schema(description = "조직 ID", example = "202410020001")
    private String orgnId;

    @Schema(description = "조직명", example = "활빈당")
    private String orgnNm;

    @Schema(description = "안전보건역할 타입", example = "risk/safety")
    private String orgnRoleType;

    @Schema(description = "안전보건역할 상세 ID", example = "202408200001")
    private String orgnRoleId;

    @Schema(description = "안전보건역할 상세 명", example = "대장")
    private String orgnRoleNm;
    
    @Schema(description = "교육결과보고서 명", example = "화재예방 교육 결과")
    private String trainingNm;
    
    @Schema(description = "교육이수 시간(분)", example = "360")
    private int trainingMinutes;

    @Schema(description = "교육 이수 현황", example = "Y/N")
    private String educationStatus; // Y일 경우 이수한 것으로 판단(서명이 있을 경우)

    @Schema(description = "조직 순번", example = "1")
    private int orgnOrdSeq;

    @Schema(description = "안전보건역할 순번", example = "1")
    private int orgnRoleOrdSeq;

}
