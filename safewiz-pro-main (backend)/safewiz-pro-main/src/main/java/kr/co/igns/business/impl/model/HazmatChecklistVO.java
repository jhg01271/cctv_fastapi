package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class HazmatChecklistVO extends BaseVO {
    @Schema(description = "작업장 id")
    private String workplaceId;
    @Schema(description = "작업장명")
    private String workplaceNm;
    @Schema(description = "점검일자")
    private String checklistDt;
    @Schema(description = "점검 시간")
    private String checklistTime;
    @Schema(description = "점검 시작시간")
    private String checklistStart;
    @Schema(description = "점검 종료시간")
    private String checklistEnd;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;

    @Schema(description = "MSDS 리스트")
    private List<MsdsVO> msdsList;
    @Schema(description = "점검항목 리스트")
    private List<HazmatChecklistDetailVO> detailList;
}
