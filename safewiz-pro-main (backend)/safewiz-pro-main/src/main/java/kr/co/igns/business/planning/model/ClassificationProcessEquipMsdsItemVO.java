package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ClassificationProcessEquipMsdsItemVO extends BaseVO {
	@Schema(description = "문서 번호", example = "")
	private String itemDocNo;

	@Schema(description = "문서 시퀀스", example = "")
	private String itemDocSeq;
	
	@Schema(description = "문서 아이템 시퀀스", example = "")
	private String docItemSeq;
	
	@Schema(description = "타입", example = "EQUIP 또는 MSDS")
	private String type;
	
	@Schema(description = "아이템 ID", example = "설비 또는 MSDS ID")
	private String itemId;
	
	@Schema(description = "아이템 명", example = "설비명 또는 MSDS명")
	private String itemNm;
}
