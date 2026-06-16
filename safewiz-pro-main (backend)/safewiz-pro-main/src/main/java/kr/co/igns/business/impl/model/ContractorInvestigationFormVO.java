package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ContractorInvestigationFormVO extends BaseVO {

    @Schema(description = "대상업체ID", example = "202410100002")
    private String partCompId;

    @Schema(description = "대상업체명", example = "(주)현진")
    private String partCompNm;

    @Schema(description = "주요사업", example = "배기장치 제조")
    private String desc;

    @Schema(description = "공사와의 거래 품목 ID", example = "0002")
    private String partCompItem;

    @Schema(description = "공사와의 거래 품목명", example = "단열재")
    private String partCompItemNm;

    @Schema(description = "우편번호", example = "29173")
    private String zipCd;

    @Schema(description = "주소", example = "대구 수성구 달구벌대로480길 9")
    private String addrs1;

    @Schema(description = "상세 주소", example = "906호")
    private String addrs2;

    @Schema(description = "심사일자", example = "20241119")
    private String examDt;

    @Schema(description = "사용여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "조사자1 이름", example = "김철수")
    private String investigator1Nm;

    @Schema(description = "조사자2 이름", example = "김영희")
    private String investigator2Nm;

    @Schema(description = "조사자1 서명", example = "")
    private String investigator1Sign;

    @Schema(description = "조사자2 서명", example = "")
    private String investigator2Sign;

    @Schema(description = "결재 상태", example = "Y")
    private String approvalStatus;

    @Schema(description = "점검항목 상세 리스트", example = "")
    private List<ContractorInvestigationFormDetailVO> detailList;

    @Schema(description = "년도별 협력사 합격 점수", example = "")
    private String score;

    @Schema(description = "등록일자", example = "")
    private String regDt;
}
