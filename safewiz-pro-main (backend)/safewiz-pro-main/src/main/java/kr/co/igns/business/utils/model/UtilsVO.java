package kr.co.igns.business.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class UtilsVO extends BaseVO {
    @Schema(description = "user id", example = "iljoogns##")
    private String userId;

    @Schema(description = "서명자 use id", example = "")
    private String hrUserId;

    @Schema(description = "서명자 use id", example = "")
    private String reqMenuId;

    @Schema(description = "hr id", example = "202410160001")
    private String hrId;
    
    @Schema(description = "hr Nm", example = "202410160001")
    private String hrNm;

    @Schema(description = "targetType", example = "")
    private String targetType;
    
    @Schema(description = "targetId", example = "")
    private String targetId;

    @Schema(description = "formattedTargetId", example = "")
    private String formattedTargetId;
    
    @Schema(description = "param", example = "")
    private String param;
    
    @Schema(description = "signature", example = "")
    private String signature;
    
    @Schema(description = "orgnNm", example = "")
    private String orgnNm;

    @Schema(description = "jbrpNm", example = "")
    private String jbrpNm;

    @Schema(description = "logoId", example = "")
    private String logoId;

    @Schema(description = "type", example = "")
    private String type;

    @Schema(description = "type", example = "")
    private String compId;

    @Schema(description = "type", example = "")
    private String compNm;

    @Schema(description = "type", example = "")
    private String clntId;

    @Schema(description = "순번", example = "")
    private int ordSeq;

    @Schema(description = "서명 정보", example = "")
    private Long seq;

    @Schema(description = "화면 이름", example = "")
    private String displayName;

    @Schema(description = "사용여부", example = "")
    private String useYn;

    @Schema(description = "사용여부", example = "")
    private String signTime;

}
