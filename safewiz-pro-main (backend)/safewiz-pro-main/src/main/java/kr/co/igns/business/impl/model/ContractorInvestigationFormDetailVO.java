package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ContractorInvestigationFormDetailVO extends BaseVO {

    @Schema(description = "점검 항목 ID", example = "0001")
    private String inspectionId;

    @Schema(description = "점검 항목명", example = "")
    private String inspectionNm;

    @Schema(description = "점검사항 ID", example = "")
    private String inspectionItemId;

    @Schema(description = "점검사항 내용", example = "")
    private String inspectionItemNm;

    @Schema(description = "상 점수", example = "")
    private int upperScore;

    @Schema(description = "중 점수", example = "")
    private int middleScore;

    @Schema(description = "하 점수", example = "")
    private int lowerScore;

    @Schema(description = "의견", example = "")
    private String comment;

    @Schema(description = "득점", example = "")
    private int score;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "순번", example = "99")
    private int ordSeq;

    @Schema(description = "점검항목 상세 리스트", example = "")
    private List<ContractorInvestigationFormDetailVO> detailList;


}
