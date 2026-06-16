package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ClassificationProcessEquipMsdsVO extends BaseVO {
	@Schema(description = "조직ID", example = "")
	private String orgnId;
	
	@Schema(description = "조직이름", example = "AAA01")
	private String orgnNm;
	
	@Schema(description = "조직장 ID", example = "")
	private String headId;
	
	@Schema(description = "조직장 명", example = "")
	private String headNm;
	
	@Schema(description = "평가시작일", example = "")
	private String startAt;
	
	@Schema(description = "평가종료일", example = "")
	private String endAt;
	
	@Schema(description = "공정개요", example = "")
	private String processOverview;

	@Schema(description = "근로자 수", example = "Y")
    private String memberCount;
	
	@Schema(description = "비고", example = "")
	private String remark;
	
	@Schema(description = "사용유무", example = "Y")
	private YesNo useYn;
    
    @Schema(description = "참여자 정보", example = "")
    private List<HrVO> hrList;
    
    @Schema(description = "공정 정보", example = "")
    private List<ClassificationProcessEquipMsdsAnalysisVO> processList;
    
    @Schema(description = "공정(차수)", example = "")
	private List<String> processIdList;

	@Schema(description = "확정여부", example = "Y")
	private String confirmYn;

	@Schema(description = "확정일시", example = "Y")
	private String confirmDt;

	@Schema(description = "작성일자", example = "Y")
	private String writeDt;
    
    // 출력물
// 	@Schema(description = "중간 페이지", example = "Y")
// 	private String subPage;
//
// 	@Schema(description = "전체 페이지", example = "Y")
// 	private Integer page;
//
// 	@Schema(description = "내부 페이지", example = "Y")
// 	private Integer localPage;
 	
 	
}
