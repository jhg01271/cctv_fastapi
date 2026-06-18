package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SafetyAndHealthObjectivesVO extends BaseVO {
	@Schema(description = "회사코드", example = "202408200001")
	private String compId;

	@Schema(description = "작성년도", example = "2024")
	private String writeYear;

	@Schema(description = "문서타입", example = "SHO")
	private String docType;

	@Schema(description = "문서번호", example = "00001")
	private String docNo;

	@Schema(description = "조직코드", example = "202410020004")
	private String orgnId;

	@Schema(description = "조직명", example = "ESG")
	private String orgnNm;

	@Schema(description = "결재상태", example = "W")
	private String approvalStatus;

	@Schema(description = "제정일자", example = "YYYY.MM.DD")
	private String revisedDt;

	@Schema(description = "개정일자", example = "YYYY.MM.DD")
	private String enactedDt;

	@Schema(description = "문서(상세) 시퀀스", example = "1,2,3...")
	private String docSeq;

	@Schema(description = "문서 개수", example = "0,1,2...")
	private String docCount;

	@Schema(description = "안전보건목표", example = "안전보건경영방침은 ...")
	private String safetyHealthGoal;

	@Schema(description = "안전보건 세부목표 및 달성방법", example = "안전보건 세부목표 및 달성방법은 ...")
	private String detailGoalMethod;

	@Schema(description = "추진일자", example = "1,2,3,4...")
	private String promotionSchedule;

	@Schema(description = "담당자 ID", example = "202410080006")
	private String hrId;

	@Schema(description = "담당자명", example = "담당자명")
	private String hrNm;

	@Schema(description = "필요자원", example = "필요자원은 ...")
	private String requiredResource;

	@Schema(description = "평가방법", example = "평가방법은 ...")
	private String evaluationMethod;

	@Schema(description = "비고", example = "비고는 ...")
	private String remark;

	@Schema(description = "상세사용유무", example = "Y/N")
	private String useYn;

	@Schema(description = "메인사용유무", example = "Y/N")
	private String useYnMain;

	@Schema(description = "유저ID", example = "202410080006")
	private String userId;

	@Schema(description = "실적 건수", example = "202410080006")
	private String resCount;

    @Schema(description = "추진율 합계", example = "210")
    private int progressSum;

	private List<HseKeyPerformanceResultVO> result;

	private boolean isPrintAll;
	
	private String fileNm; //메인화면에서 출력시 파일이름

	// 리포트 번호
	private String no;
	// 추진계획
	private String actionDocType;
	private String actionDocNo;
	private String actionDocSeq;
	private String actionDocDetailSeq;
	private String actionObjective;
	private String detailItem;
	private String actionSchedule1;
	private String actionSchedule2;
	private String actionSchedule3;
	private String actionSchedule4;
	private String actionSchedule5;
	private String actionSchedule6;
	private String actionSchedule7;
	private String actionSchedule8;
	private String actionSchedule9;
	private String actionSchedule10;
	private String actionSchedule11;
	private String actionSchedule12;
	private String checkableActionSchedule1;
	private String checkableActionSchedule2;
	private String checkableActionSchedule3;
	private String checkableActionSchedule4;
	private String checkableActionSchedule5;
	private String checkableActionSchedule6;
	private String checkableActionSchedule7;
	private String checkableActionSchedule8;
	private String checkableActionSchedule9;
	private String checkableActionSchedule10;
	private String checkableActionSchedule11;
	private String checkableActionSchedule12;
	private String companyObjective;
	private String detailPlan;
	private String detailItemId;
	private String reqInfoKey;
	private String clntId;

	@Schema(description = "추진 계획 해당 조직 등록 여부", example = "Y")
	private String actionOrgnYn;
}
