package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class JobCompetencyAssessmentVO extends BaseVO {

    @Schema(description = "사용자 사진 ID", example = "사용자 사진 ID")
	private String logoId;

    @Schema(description = "인원 ID", example = "202410230001")
    private String hrId;

    @Schema(description = "인원 이름", example = "홍길동")
    private String hrNm;

    @Schema(description = "조직 ID", example = "202410230001")
    private String orgnId;

    @Schema(description = "조직 이름", example = "활빈당")
    private String orgnNm;

    @Schema(description = "직위 ID", example = "202410230001")
    private String jbrpId;

    @Schema(description = "직위 이름", example = "도적")
    private String jbrpNm;

    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    // 직무 관리
    @Schema(description = "직무 시퀀스", example = "00001")
    private String jobSeq;

    @Schema(description = "직무 분야", example = "00001")
    private String majorCd;
    private String majorNm;

    @Schema(description = "직무", example = "00001")
    private String minorCd;
    private String minorNm;

    @Schema(description = "순번", example = "0")
    private int ordSeq;

    @Schema(description = "결재 상태", example = "false")
    private String approvalStatus;

    @Schema(description = "카드 직무 출력용", example = "00001")
    private List<String> cardJobNm;

    @Schema(description = "상세 카드 데이터", example = "Y")
    private List<JobCompetencyAssessmentDetailVO> detailList;

}
