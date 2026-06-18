package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyMgmtOfHazardousMachineryOrgnVO extends BaseVO {
	
	@Schema(description = "작성년도", example = "")
	private String writeYear;
	
	@Schema(description = "문서타입", example = "")
    private String docType;

    @Schema(description = "문서번호", example = "")
    private String docNo;
	
	@Schema(description = "조직 ID", example = "")
	private String orgnId;
	
	@Schema(description = "조직명", example = "")
	private String orgnNm;
	
	@Schema(description = "카드 조직명", example = "")
	private String cardOrgnNm;
	
	@Schema(description = "생성날짜", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정날짜", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;
}
