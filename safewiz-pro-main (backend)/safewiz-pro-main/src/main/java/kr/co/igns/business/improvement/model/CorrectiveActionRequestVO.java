package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class CorrectiveActionRequestVO extends BaseVO {
	
	@Schema(description = "년도(Key)", example = "2024")
    private String writeYear;

	@Schema(description = "문서구분(Key)", example = "")
    private String docType;
	
	@Schema(description = "문서번호(Key)", example = "00001")
    private String docNo;
	
	@Schema(description = "사업장 ID", example = "")
    private String compId;
	
	@Schema(description = "제목", example = "")
    private String title;
	
	@Schema(description = "주관 조직 ID", example = "")
    private String ctrlOrgnId;
	
	@Schema(description = "주관 조직 명", example = "")
    private String ctrlOrgnNm;
	
	@Schema(description = "조치 조직 ID", example = "")
    private String actionOrgnId;
	
	@Schema(description = "조치 조직 명", example = "")
    private String actionOrgnNm;
	
	@Schema(description = "작성일", example = "")
	private String writeDt;
	
	@Schema(description = "회신요구일", example = "")
	private String returnDt;
	
	@Schema(description = "시정조치 요구사항", example = "")
    private String actionRequestContent;
	
	@Schema(description = "원인분석 및 재발방지대책", example = "")
    private String analyticPreventContent;
	
	@Schema(description = "시정조치 결과 유효성 확인", example = "")
    private String resultValidationContent;
	
	@Schema(description = "적합/부적합", example = "")
    private String resultValidationYn;
	
	@Schema(description = "확인일", example = "")
	private String checkDt;
	
	@Schema(description = "사용여부", example = "")
	private String useYn;
	
	@Schema(description = "생성일자", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정일자", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;

	

}
