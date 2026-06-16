package kr.co.igns.business.support.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TrainingResultReportHrVO extends BaseVO {
	private String writeYear;
	private String docType;
	private String docNo;
	private String docSeq;
    @Schema(description = "담당자 id")
    private String hrId;
    @Schema(description = "담당자")
    private String hrNm;
    @Schema(description = "담당자 id")
    private String id;
    @Schema(description = "담당자")
    private String nm;
}
