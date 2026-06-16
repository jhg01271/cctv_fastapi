package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SafetyMgmtOfHazardousMachineryInspectionVO extends BaseVO {
	
	@Schema(description = "작성년도", example = "")
	private String writeYear;
	
	@Schema(description = "문서타입", example = "")
    private String docType;

    @Schema(description = "문서번호", example = "")
    private String docNo;
	
	@Schema(description = "문서 시퀀스", example = "")
	private String docSeq;

	@Schema(description = "디테일 년도", example = "")
	private String inspectionWriteYear;

	@Schema(description = "검사일", example = "")
	private String inspectionDate;

	@Schema(description = "검사 유효기간 시작일", example = "")
	private String inspectionValidFromDt;

	@Schema(description = "검사 유효기간 종료일", example = "")
	private String inspectionValidToDt;
	
	@Schema(description = "차기 검사일", example = "")
	private String nextInspectionDate;
	
	@Schema(description = "사용여부", example = "")
	private String useYn;
	
	@Schema(description = "생성날짜", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정날짜", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;

	private String stdEqId;
}
