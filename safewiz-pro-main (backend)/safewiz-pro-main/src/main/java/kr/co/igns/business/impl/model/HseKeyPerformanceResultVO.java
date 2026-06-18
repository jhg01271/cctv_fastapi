package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class HseKeyPerformanceResultVO extends BaseVO{


    @Schema(description = "사용유무", example = "Y/N")
	private String useYn;
	
	@Schema(description = "문서(상세) 시퀀스", example = "1,2,3...")
	private String docSeq;

    @Schema(description = "문서(상세) 시퀀스", example = "1,2,3...")
	private String docSeqDetail;

    private String percent;

    private String content;

    private String checkDt;

	private String hrId;

	private String hrNm;

	private String orgnNm;
    //실적
    private String actionResult1;
	private String actionResult2;
	private String actionResult3;
	private String actionResult4;
	private String actionResult5;
	private String actionResult6;
	private String actionResult7;
	private String actionResult8;
	private String actionResult9;
	private String actionResult10;
	private String actionResult11;
	private String actionResult12;
	
	private String userId;
	private String cnltId;
	private String reqInfoKey;
	private String taskDetailDiv;
}
