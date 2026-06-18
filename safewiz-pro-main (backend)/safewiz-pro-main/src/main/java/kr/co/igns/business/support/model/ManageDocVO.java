package kr.co.igns.business.support.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ManageDocVO extends BaseVO {
    @Schema(description = "작성일자")
    private String writeDt;
    @Schema(description = "작성자 id")
    private String writerId;
    @Schema(description = "작성자명")
    private String writerNm;
    @Schema(description = "문서명")
    private String manageDocNm;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    //등록건수
    private int fileCount;

    @Schema(description = "문서관리 항목")
    private String manageDocType;
    @Schema(description = "문서관리 항목명")
    private String manageDocTypeNm;
}
