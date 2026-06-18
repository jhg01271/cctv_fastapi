package kr.co.igns.business.planning.model;


import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class WorkersOpinionsOnHazardsVO extends BaseVO {
	@Schema(description = "작성년도", example = "2024")
    private String writeYear;
	
	@Schema(description = "문서타입", example = "WOOHA")
    private String docType;
	
	@Schema(description = "문서번호", example = "00001")
    private String docNo;
	
	@Schema(description = "인원id", example = "202408200001")
    private String hrId;
	
	@Schema(description = "인원이름", example = "이름")
    private String hrNm;
	
	@Schema(description = "작업장 ID", example = "202408200001")
    private String workplaceId;
	
	@Schema(description = "작업장 명", example = "작업장")
    private String workplaceNm;
	
	@Schema(description = "사용여부", example = "Y")
    private String useYn;
	
	@Schema(description = "생성일자", example = "2024.11.01")
    private String createdAt;
	
	@Schema(description = "생성자", example = "abcd1234")
    private String createdBy;
	
	@Schema(description = "수정일자", example = "2024.11.01")
    private String updatedAt;
	
	@Schema(description = "수정자", example = "abcd1234")
    private String updatedBy;
	
	@Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
	
	@Schema(description = "등록일자", example = "2024.11.01")
    private String regDt;
	
	@Schema(description = "근로자 의견", example = "근로자의견")
    private String workerOpinion;
	
	@Schema(description = "검토자 의견", example = "검토자의견")
    private String reviewerOpinion;
	
	@Schema(description = "경험담 no", example = "00001")
    private String experienceNo;
	
	@Schema(description = "경험담", example = "경험담")
    private String content;

    @Schema(description = "카드 제목", example = "안전보건경영방침")
    private String title;

    @Schema(description = "카드 메뉴 라우터 경로", example = "HsePolicy")
    private String route;

    @Schema(description = "카드 활성화 상태 값", example = "Y")
    private YesNo activeYn;

    @Schema(description = "설명", example = "안전보건경영방침은 ...")
    private String description;
    
    @Schema(description = "작성건수", example = "3")
    private String cnt;
    
    @Schema(description = "저장상태", example = "I")
    private String cmd;
    
    @Schema(description = "작성완료건수", example = "3")
    private String writerCnt;
    
    @Schema(description = "검토완료건수", example = "3")
    private String reviewerCnt;
    
    @Schema(description = "작성자명", example = "3")
    private String writerNm;

    @Schema(description = "작성자ID", example = "3")
    private String writerId;
    
    @Schema(description = "검토자명", example = "3")
    private String reviewerNm;

    @Schema(description = "검토자Id", example = "3")
    private String reviewerId;
    
    @Schema(description = "결재상태", example = "I")
    private String approvalStatus;
    
    @Schema(description = "경험담 리스트", example = "안전보건경영방침은 ...")
    private List<WorkersOpinionsOnHazardsVO> experienceList;

    @Schema(description = "등록일자(타이틀용)")
    private String titleRegDt;
}
