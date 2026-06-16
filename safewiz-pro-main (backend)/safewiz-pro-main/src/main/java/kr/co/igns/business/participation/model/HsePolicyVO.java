package kr.co.igns.business.participation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class HsePolicyVO extends BaseVO {

    @Schema(description = "사업장 이름", example = "일주지앤에스")
    private String compNm;

    @Schema(description = "안전보건경영방침 구분", example = "0001")
    private String dataType;

    @Schema(description = "안전보건경영방침 구분명", example = "출력물 양식")
    private String dataTypeNm;

    @Schema(description = "안전보건경영방침 제목", example = "제목")
    private String contentHeader;

    @Schema(description = "안전보건경영방침 내용", example = "내용")
    private String contentBody;

    @Schema(description = "안전보건경영방침 하단", example = "하단")
    private String contentFooter;

    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

    @Schema(description = "이미지 ID", example = "...")
    private String fileId;

    @Schema(description = "근로자 의견 수", example = "...")
    private String workerSuggestionCnt;

    @Schema(description = "확정여부", example = "Y")
    private String confirmYn;

    @Schema(description = "확정일시", example = "Y")
    private String confirmDt;

}
