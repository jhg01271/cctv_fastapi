package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyAndHealthInfoRegisterVO extends BaseVO {

    @Schema(description = "인원 ID", example = "202410230001")
    private String hrId;

    @Schema(description = "인원 이름", example = "홍길동")
    private String hrNm;

    @Schema(description = "조직 ID", example = "202410230001")
    private String orgnId;

    @Schema(description = "조직 이름", example = "활빈당")
    private String orgnNm;

    @Schema(description = "직위 ID", example = "202410230001")
    private String jbrpId;

    @Schema(description = "직위 이름", example = "도적")
    private String jbrpNm;

    @Schema(description = "처리 조직 ID", example = "202410230001")
    private String actionOrgnId;

    @Schema(description = "처리 이름", example = "활빈당")
    private String actionOrgnNm;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "주요 내용", example = "...")
    private String keyContent;

    @Schema(description = "조치 내용", example = "...")
    private String actionContent;

    @Schema(description = "접수일자", example = "20241028")
    private String receiptDt;

    @Schema(description = "조치일자", example = "20241028")
    private String actionDt;

    @Schema(description = "회신일자", example = "20241028")
    private String replyDt;

    @Schema(description = "비고", example = "20241028")
    private String remark;
}
