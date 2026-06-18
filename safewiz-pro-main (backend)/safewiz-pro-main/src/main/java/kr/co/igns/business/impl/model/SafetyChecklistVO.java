package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class SafetyChecklistVO extends BaseVO {
    @Schema(description = "설비유형 id")
    private String stdEqId;
    @Schema(description = "설비유형명")
    private String stdEqNm;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "점검주기")
    private String inspectionCycle;
    @Schema(description = "사용 여부", example = "Y")
    private String useYn;

    @Schema(description = "정렬", example = "99")
    private int ordSeq;
    //항목, 사항 갯수
    private int itemCnt;
    private int listCnt;

    @Schema(description = "점검항목 리스트")
    private List<SafetyChecklistItemVO> itemList;
    @Schema(description = "점검사항 리스트")
    private List<SafetyChecklistDetailVO> DetailList;
    @Schema(description = "주기변경 이력")
    private List<SafetyChecklistHistoryVO> historyList;
    
    private String writeYear;
    private String docType;
    private String docNo;
    private String onlyWeekday;
    private String compId;

    @Schema(description = "설비이력 시퀀스")
    private int histSeq;


}
