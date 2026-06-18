package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MainMenuVO extends SpSearchVO {
    @Schema(description = "메뉴 아이디", example = "코드 이름")
    private String menuId;
    @Schema(description = "메뉴명", example = "테스트")
    private String menuNm;
    @Schema(description = "메뉴 영문명", example = "test")
    private String menuNmEng;
    @Schema(description = "메뉴 아이콘", example = "test.png")
    private String menuIcon;
    @Schema(description = "라우터명", example = "MainMenu")
    private String routerNm;
    @Schema(description = "라우터 경로", example = "/base/system")
    private String routerPath;
    @Schema(description = "뷰 파일 경로", example = "/base/system")
    private String viewPath;
    @Schema(description = "메뉴 표시 순서", example = "1")
    private Integer menuOdr;
    @Schema(description = "상위 메뉴 아이디", example = "default")
    private String upMenuId;
    @Schema(description = "파라미터", example = "S")
    private String param;
    @Schema(description = "조회 권한", example = "Y")
    private String schAh;
    @Schema(description = "삭제 권한", example = "Y")
    private String delAh;
    @Schema(description = "엑셀 권한", example = "Y")
    private String exlAh;
    @Schema(description = "저장 권한", example = "Y")
    private String savAh;

    private String webYn;
    private String appYn;

    @Schema(description = "하위 메뉴 존재 여부", example = "true")
    private Boolean hasChildren;
    @Schema(description = "전체 경로", example = "시스템관리 > 메뉴설정 > 권한관리")
    private String fullPath;

    private List<MainMenuVO> children = new ArrayList<>();
}
