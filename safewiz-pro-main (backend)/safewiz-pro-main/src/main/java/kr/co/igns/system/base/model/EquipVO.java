package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EquipVO extends BaseVO {
    @Schema(description = "설비 ID")
    private String equipmentId;
    @Schema(description = "설비 유형 ID", example = "STEQ00001")
    private String stdEqId;
    @Schema(description = "설비 유형 이름", example = "설비유형#1")
    private String stdEqNm;
    @Schema(description = "설비명", example = "설비#1")
    private String equipmentNm;
    @Schema(description = "사용처(본사/도급/사급)")
    private String usageType;
    @Schema(description = "설치장소")
    private String setupLocation;
    @Schema(description = "관리번호 (QR코드 생성 시 사용할 번호)")
    private String mgmtNum;
    @Schema(description = "제조사")
    private String mfComp;
    @Schema(description = "규격 및 용량")
    private String mfSpec;
    @Schema(description = "제조일자(yyyyMMdd)")
    private String mfDate;
    @Schema(description = "설치일자(yyyyMMdd)")
    private String setupAt;
    @Schema(description = "폐기일자(yyyyMMdd)")
    private String discardAt;
    @Schema(description = "정렬순서", example = "99")
    private int ordSeq=99;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

    //인원 Mapping
    @Schema(description = "담당자(정)")
    private List<HrVO> headHrList;
    @Schema(description = "담당자(부)")
    private List<HrVO> secondHrList;

    //Mapping
    @Schema(description = "작업장 ID 리스트")
    private List<String> workplaceIdList;
    @Schema(description = "작업장 이름 리스트")
    private List<BaseMapVO> workplaceList;
    @Schema(description = "작업장 ID")
    private String workplaceId;

    @Schema(description = "조직 ID 리스트")
    private List<String> orgnIdList;
    @Schema(description = "조직 리스트(조회용)")
    private List<BaseMapVO> orgnList;
    @Schema(description = "조직 ID")
    private String orgnId;

    @Schema(description = "작업내용 리스트")
    private List<PrcsWorkVO> workList;
    @Schema(description = "공정 작업 ID")
    private String prcsWorkId;
    @Schema(description = "점검주기명")
    private String inspectionCycleNm;
}
