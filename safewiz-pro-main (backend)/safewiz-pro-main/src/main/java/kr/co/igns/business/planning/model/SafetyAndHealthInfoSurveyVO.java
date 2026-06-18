package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyAndHealthInfoSurveyVO extends BaseVO {
	@Schema(description = "작성년도", example = "2024")
    private String writeYear;
	
	@Schema(description = "문서타입", example = "ORGST")
    private String docType;
	
	@Schema(description = "문서번호", example = "00001")
    private String docNo;
	
	@Schema(description = "문서번호", example = "00001")
    private List<String> docNoList;
	
	@Schema(description = "타깃Id", example = "202408200001")
    private String targetId;

    @Schema(description = "설비 ID", example = "202408200001")
    private String stdEqId;

	@Schema(description = "공정 ID", example = "202408200001")
    private String processId;
	
	@Schema(description = "공정 이름", example = "공정명")
    private String processNm;
	
	@Schema(description = "공정 차수", example = "202408200001")
    private String revNo;
    
    @Schema(description = "공정 차수명", example = "차수명")
    private String revNm;
	
	@Schema(description = "원재료", example = "원재료")
    private String material;
	
	@Schema(description = "생산품", example = "생산품")
    private String product;
	
	@Schema(description = "근로자수", example = "5")
    private String workerCnt;
	
	@Schema(description = "공정 확정 ID", example = "1")
    private String prcsCnfmId;
	
	@Schema(description = "작업상세ID", example = "00001")
    private String workDetailId;
	
	@Schema(description = "작업장ID", example = "1")
    private String workplaceId;
	
	@Schema(description = "작업장명", example = "작업장")
    private String workplaceNm;
	
	@Schema(description = "작업 타입", example = "msds")
    private String workType;
	
	@Schema(description = "공정 작업 ID", example = "15")
    private String prcsWorkId;
	
	@Schema(description = "작업내용", example = "15")
    private String workContent;
	
	@Schema(description = "작업내용 명", example = "벤젠")
    private String content;
	
	@Schema(description = "내용1", example = "5")
    private String desc1;
	
	@Schema(description = "내용2", example = "6")
    private String desc2;
	
	@Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
	
	@Schema(description = "사용여부", example = "Y")
    private String useYn;

    @Schema(description = "카드 제목", example = "안전보건경영방침")
    private String title;

    @Schema(description = "카드 메뉴 라우터 경로", example = "HsePolicy")
    private String route;

    @Schema(description = "카드 활성화 상태 값", example = "Y")
    private YesNo activeYn;

    @Schema(description = "설명", example = "안전보건경영방침은 ...")
    private String description;
    
    @Schema(description = "작업수", example = "1")
    private String workCnt;
    
    @Schema(description = "장비,설비 수", example = "2")
    private String equipCnt;
    
    @Schema(description = "화학제품 수", example = "3")
    private String msdsCnt;
    
    @Schema(description = "cmd", example = "I")
    private String cmd;
    
    @Schema(description = "설비명", example = "설비")
    private String equipContent;
    
    @Schema(description = "화학명", example = "화학")
    private String msdsContent;
    
    @Schema(description = "비고", example = "비고")
    private String remark;
    
    @Schema(description = "업종명", example = "컴퓨터업종")
    private String bizNm;
    
    @Schema(description = "report용 임시id", example = "1")
    private int newWorkDetailId;
    
    @Schema(description = "cmd", example = "3")
    private String msdsTime;

    @Schema(description = "확정여부", example = "Y")
    private String confirmYn;

    @Schema(description = "확정일시", example = "")
    private String confirmDt;

    @Schema(description = "작성일자", example = "")
    private String writeDt;
}
