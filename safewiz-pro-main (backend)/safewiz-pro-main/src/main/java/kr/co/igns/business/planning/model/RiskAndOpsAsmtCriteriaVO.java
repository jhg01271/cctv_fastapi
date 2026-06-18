package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class RiskAndOpsAsmtCriteriaVO extends BaseVO {
	//전체main조회
	@Schema(description = "메뉴명", example = "")
    private String title;
	
	@Schema(description = "라우터주소", example = "")
    private String route;

	@Schema(description = "설명", example = "")
    private String description;
	
	@Schema(description = "데이터여부", example = "202408200001")
    private String activeYn;
	
	//main
	@Schema(description = "사업장ID", example = "")
    private String compId;
	
	@Schema(description = "차수", example = "202410230001")
    private String criteriaId;
	
	@Schema(description = "평가구분기준", example = "R")
    private String criteriaType;
	
	@Schema(description = "확정여부", example = "Y")
    private String confirmedYn;
	
	@Schema(description = "확정일자", example = "20240101")
    private String confirmedDt;
	
	@Schema(description = "사용여부", example = "Y")
    private String useYn;
	
	@Schema(description = "등록일자", example = "20240101")
    private String createdAt;
	
	@Schema(description = "등록자", example = "userId")
    private String createdBy;

	@Schema(description = "수정일자", example = "20240101")
    private String updatedAt;

	@Schema(description = "수정자", example = "userId")
	private String updatedBy;
	
	//detail
	@Schema(description = "detail메뉴", example = "P")
    private String detailType;
	
	@Schema(description = "detail시퀀스", example = "00001")
	private String detailSeq;
	
	@Schema(description = "내용", example = "202410230001")
    private String content1;
	
	@Schema(description = "내용", example = "202410230001")
    private String content2;
	
	@Schema(description = "내용", example = "202410230001")
    private String content3;
	
	@Schema(description = "내용", example = "202410230001")
    private String content4;
	
	@Schema(description = "순서", example = "1")
    private String ordSeq;
	
	@Schema(description = "유저id", example = "createdby,updatedby")
    private String userId;
	
	@Schema(description = "조회조건useYn", example = "true")
    private Boolean mainUseYn;
	
	@Schema(description = "디테일그리드 useYn", example = "true")
	private String detailUseYn;
	
}
