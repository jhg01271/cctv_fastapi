package kr.co.igns.business.comp.master.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompCodeVO extends BaseVO {
    @Schema(description = "코드 순번", example = "2")
    private Long codeSeq;
    @Schema(description = "코드", example = "C0001")
    private String code;
    @Schema(description = "코드명", example = "코드 이름")
    private String codeNm;
    @Schema(description = "순번", example = "1")
    private int codeOdr;
    @Schema(description = "비고", example = "비고 작성란")
    private String codeRmk;
    @Schema(description = "삭제여부", example = "Y")
    private YesNo delYn;
    @Schema(description = "상위  코드 순번", example = "1")
    private Long upCodeSeq;
    List<SystemCodeVO> children = new ArrayList<>();

    @Schema(description = "사업장 아이디", example = "IGNS")
    private String compId;
}
