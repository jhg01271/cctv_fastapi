package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class MsdsVO extends BaseVO {
    @Schema(description = "MSDS ID", example="작성 연/월/일 + 순서 | YYYYMMDD0001")
    private String msdsId;
    @Schema(description = "사업장 ID", example = "202408200001")
    private String compId;
    @Schema(description = "MSDS명", example = "MSDS#1")
    private String msdsNm;
    @Schema(description = "관용명(동의어)")
    private String msdsSynonym;
    @Schema(description = "CAS No.")
    private String casNo;
    @Schema(description = "일일취급량(0.0~)")
    private String dailyVolume;
    @Schema(description = "일일취급량 단위")
    private String dailyVolumeUnit;
    @Schema(description = "일일취급시간(0.0~24)")
    private String dailyTime;
    @Schema(description = "설명")
    private String desc;
    @Schema(description = "정렬순서", example = "99")
    private int ordSeq;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

    //Mapping
    @Schema(description = "작업장 ID 리스트")
    private List<String> workplaceIdList;
    @Schema(description = "작업장 이름 리스트")
    private List<BaseMapVO> workplaceList;
    @Schema(description = "작업장 ID")
    private String workplaceId;
    @Schema(description = "작업장 명")
    private String workplaceNm;

    @Schema(description = "공정 ID 리스트")
    private List<String> workIdList;
    @Schema(description = "공정 리스트(조회용)")
    private List<PrcsWorkVO> workList;
    @Schema(description = "공정 ID")
    private String workId;
    
    @Schema(description = "산업안전보건법")
    private List<MsdsLawVO> safetyAndHealthAct;
    @Schema(description = "화학물질관리법")
    private List<MsdsLawVO> chemicalAct;
    @Schema(description = "위험물안전관리법")
    private List<MsdsLawVO> dangerousAct;
    
    @Schema(description = "담당자")
    private List<MsdsHrVO> hrList;
    
    @Schema(description = "단위")
    private List<MsdsUnitVO> unitVo;
}
