package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyAndHealthCommPlanVO extends BaseVO {

    @Schema(description = "인원 ID", example = "202410230001")
    private String hrId;

    @Schema(description = "인원 이름", example = "홍길동")
    private String hrNm;

    @Schema(description = "제목", example = "제목")
    private String title;
    
    @Schema(description = "작성일자", example = "20241029")
    private String writeDt;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "의사소통 구분", example = "0001")
    private String contentType;

    @Schema(description = "상세 데이터(내부)", example = "...")
    private List<SafetyAndHealthCommPlanDetailVO> innerList;

    @Schema(description = "상세 데이터(외부)", example = "...")
    private List<SafetyAndHealthCommPlanDetailVO> externalList;

}
