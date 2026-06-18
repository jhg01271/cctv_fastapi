package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ImplementationOfRiskAseessmentReductionVO extends BaseVO {
	@Schema(description = "감소대책 문서타입", example = "")
	private String reductionDocType;

	@Schema(description = "감소대책 문서번호", example = "00001")
	private String docSeqDetail;
	
	@Schema(description = "감소대책", example = "")
	private String reductionMeasures;
	
	@Schema(description = "개선예정일", example = "20241112")
	private String expectedDate;
	
	@Schema(description = "개선 완료일", example = "20241112")
	private String completedDate;
	
	@Schema(description = "실행내용", example = "")
	private String implContent;
	
	@Schema(description = "사용여부", example = "Y")
	private String useYn;
	
	@Schema(description = "개선 전 이미지 파일 ID", example = "")
	private String prevFileId;
	
	@Schema(description = "개선 후 이미지 파일 ID", example = "")
	private String afterFileId;

	@Schema(description = "감소대책 생성일", example = "")
	private String reductionCreatedAt;
	
	@Schema(description = "담당자 리스트", example = "")
	private List<ImplementationOfRiskAseessmentReductionHrVO> hrList;
}
