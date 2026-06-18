package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthVO extends BaseVO {
    @Schema(description = "메뉴 순번", example = "2")
    private Long menuSeq;
    @Schema(description = "사업장 아이디", example = "IGNS")
    private String compId;
    @Schema(description = "권한 순번", example = "1")
    private String grpCd;
    @Schema(description = "그룹 아이디", example = "1")
    private String grpId;
    @Schema(description = "메뉴 아이디", example = "코드 이름")
    private String menuId;
    @Schema(description = "삭제 권한", example = "Y")
    private String delAh;
    @Schema(description = "엑셀 권한", example = "Y")
    private String exlAh;
    @Schema(description = "저장 권한", example = "Y")
    private String savAh;
    @Schema(description = "조회 권한", example = "Y")
    private String schAh;
    
    @Schema(description = "웹/앱 구분", example = "W")
    private String param;

    // 상위 메뉴 아이디
    private String cmd;
    private String upMenuId;
    private String menuNm;
    private Integer menuOdr;

    // 사용자별 메뉴 저장시
    private String hrId;
    
    private List<AuthVO> children = new ArrayList<>();
}
