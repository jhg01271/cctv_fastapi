package kr.co.igns.business.impl.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class EmergencyControlTaskAsgmtVO extends BaseVO {

    @Schema(description = "차수", example = "202410310001")
    private String chartId;

    @Schema(description = "차수명", example = "202410310001")
    private String chartNm;

    @Schema(description = "문서 제목", example = "화재폭발 대비 업무분장")
    private String title;

    @Schema(description = "작성일자", example = "20241107")
    private String writeDt;

    @Schema(description = "사용여부", example = "Y")
    private String useYn;

    @Schema(description = "조직도 데이터", example = "JSON{...}")
    private String chartData;

    // 상세
    @Schema(description = "비상통제 역할 ID", example = "0001")
    private String roleId;

    @Schema(description = "비상통제 역할명", example = "대장")
    private String roleNm;
    
    @Schema(description = "담당업무", example = "인원대피지휘")
    private String task;

    @Schema(description = "비상통제 역할 분장", example = "202410100002")
    private String hrId;

    @Schema(description = "비상통제 역할 분장", example = "홍길동")
    private String hrNm;

    @Schema(description = "담당업무 등록 수", example = "3")
    private int registCnt;

    @Schema(description = "역할분장 목록", example = "[...]")
    private List<EmergencyControlTaskAsgmtVO> roleList;

    @Schema(description = "역할분장 인원 매핑 목록", example = "[...]")
    private List<EmergencyControlTaskAsgmtVO> hrList;

    @Schema(description = "역할분장 허용가능 조직목록", example = "[...]")
    private List<String> orgnIdList;

}
