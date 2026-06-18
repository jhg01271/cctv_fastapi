package kr.co.igns.business.orgstatus.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeesAndStakeholdersVO extends BaseVO {
	
	private static final long serialVersionUID = -2417117617959565367L;
	
	@Schema(description = "메인저장구분", example = "")
	private String mainCmd;
	
	@Schema(description = "상세저장구분", example = "")
	private String detailCmd;
	
	@Schema(description = "년도", example = "")
	private String year;
	
//	@Schema(description = "순번 (YYYYMMDD+SEQ(4자리))", example = "")
//    private String empId;
	
	@Schema(description = "작성년도", example = "2024")
	private String writeYear;
	
	@Schema(description = "작성일자", example = "202401010001")
	private String writeDt;
	
	@Schema(description = "문서타입", example = "202401010001")
	private String docType;
	
	@Schema(description = "문서번호", example = "202401010001")
	private String docNo;
	
	@Schema(description = "사업장ID", example = "")
	private String compId;
	
	@Schema(description = "조직 ID", example = "")
    private String orgnId;
	
	@Schema(description = "조직명", example = "")
    private String orgnNm;
	
    @Schema(description = "사용유무", example = "")
    private String useYn;
    
    @Schema(description = "메인 사용유무", example = "")
    private String mainYn;
    
    @Schema(description = "건수", example = "")
    private String detailCount;
    
    @Schema(description = "결재상태", example = "")
    private String approvalStatus;
    
    @Schema(description = "생성자 ID", example = "")
    private String createdBy;
    
    @Schema(description = "생성날짜", example = "")
    private String createdAt;
    
    @Schema(description = "수정자 ID", example = "")
    private String updatedBy;
    
    @Schema(description = "수정날짜", example = "")
    private String updatedAt;

    @Schema(description = "작성 서명자 ID", example = "")
    private String writer;

    /********************************************detail********************************************/
    
    @Schema(description = "상세 순번 (YYYYMMDD+SEQ(4자리))", example = "")
    private String detailId;
    
    @Schema(description = "내/외부 구분(I: 내부, O: 외부)", example = "")
    private String gubun;
    
    @Schema(description = "내/외부 구분(I: 내부, O: 외부)", example = "")
    private String gubunNm;

    @Schema(description = "이해관계자", example = "")
    private String stakeholders;
    
    @Schema(description = "이해관계자", example = "")
    private String stakeholdersNm;

    @Schema(description = "요구사항", example = "")
    private String requirements;

    @Schema(description = "준수 의무사항", example = "")
    private String obligation;
    
    @Schema(description = "리스크", example = "")
    private String risk;
    
    @Schema(description = "기회", example = "")
    private String chance;
   

	// 출력물
    @Schema(description = "업무", example= "")
    private String workDesc;
    @Schema(description = "이슈", example= "")
    private String issue;
    
//	@Schema(description = "중간 페이지", example = "Y")
//	private String subPage;
//
//	@Schema(description = "전체 페이지", example = "Y")
//	private Integer page;
//
//	@Schema(description = "내부 페이지", example = "Y")
//	private Integer localPage;
	
	@Schema(description = "전체 출력 유무", example = "Y")
	private boolean printAll = false;
    
}
