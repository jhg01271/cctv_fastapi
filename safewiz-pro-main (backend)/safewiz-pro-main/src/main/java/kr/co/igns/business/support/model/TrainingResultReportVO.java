package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class TrainingResultReportVO extends BaseVO {
    @Schema(description = "제목")
    private String title;

    @Schema(description = "교육일")
    private String trainingDate;

    @Schema(description = "교육시작시간")
    private String trainingStart;

    @Schema(description = "교육종료시간")
    private String trainingEnd;

    @Schema(description = "교육 구분")
    private String trainingType;

    @Schema(description = "교육 구분 명")
    private String trainingTypeNm;

    @Schema(description = "예산 비용")
    private int budget;

    @Schema(description = "소요 내역")
    private String requiredDetails;

    @Schema(description = "교육 목표")
    private String trainingGoal;

    @Schema(description = "기대 효과")
    private String expectedEffect;

    @Schema(description = "교육자료 - 기타")
    private String materialsEtc;

    @Schema(description = "교안")
    private String materials;

    @Schema(description = "프로젝터")
    private String projector;

    @Schema(description = "VTR")
    private String vtr;

    @Schema(description = "교육 내용")
    private String trainingContent;

    @Schema(description = "교육 기관")
    private String trainingInstitute;

    @Schema(description = "교육 기관명")
    private String trainingInstituteNm;

    @Schema(description = "강사")
    private String trainingInstructor;

    @Schema(description = "강사명")
    private String trainingInstructorNm;

    @Schema(description = "교육 장소")
    private String trainingLocation;

    @Schema(description = "교육 장소명")
    private String trainingLocationNm;

    @Schema(description = "질의응답 여부")
    private YesNo qnaYn;

    @Schema(description = "보고서 여부")
    private YesNo reportYn;

    @Schema(description = "전달 교육 여부")
    private YesNo deliveryTrainingYn;

    @Schema(description = "테스트 여부")
    private YesNo testYn;

    @Schema(description = "교육 훈련 효과성 파악 - 기타")
    private String effectivenessEtc;

    @Schema(description = "양호 여부")
    private String goodCondition;
    
    @Schema(description = "보통 여부")
    private String normalCondition;
    
    @Schema(description = "상관없음 여부")
    private String noConcern;

    @Schema(description = "특기 사항")
    private String remark;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
    
    @Schema(description = "결재상태", example = "I")
	private String approvalStatus;
    
    @Schema(description = "교육시간")
    private String trainingPeriod;

    // 인원 Mapping
    @Schema(description = "교육인원")
    private List<UtilsVO> HrList;
    
    @Schema(description = "교육계획인원")
    private List<HrVO> planHrList;
    
    private List<TrainingResultReportHrVO> chargeList;
    private String planDocType;
    private String planDocNo;
    private String planWriteYear;
    private String planTitle;

    //파일
    @Schema(description = "파일 정보 리스트")
    private List<FileVO> files;  // 파일 정보 리스트 추가
    private String fileId;	//조회, 저장(file upload)
    private String fileId2;
    private List<String> deleteFiles;	//저장(file delete)
    private List<String> deleteFiles2;

}
