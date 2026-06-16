package kr.co.igns.system.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Data
public class FavoritesVO extends BaseVO {

    @Schema(description = "메뉴 ID")
    private String menuId;
    @Schema(description = "메뉴 아이콘")
    private String menuIcon;
    @Schema(description = "메뉴명")
    private String menuNm;
    @Schema(description = "메뉴 영문명")
    private String menuNmEng;
    @Schema(description = "표시 순서")
    private Integer menuOdr;
    @Schema(description = "파라미터")
    private String param;
    @Schema(description = "라우터명")
    private String routerNm;
    @Schema(description = "라우터 경로")
    private String routerPath;
    @Schema(description = "상위 아이디")
    private String upMenuId;
    @Schema(description = "뷰 파일 경로")
    private String viewPath;
    @Schema(description = "사용여부")
    private String useYn;
    @Schema(description = "사업장번호")
    private String compId;
    @Schema(description = "인원 ID")
    private String hrId;
    @Schema(description = "로그인 ID")
    private String loginId;
    @Schema(description = "신규여부")
    private String newYn;
    @Schema(description = "즐겨찾기 ID")
    private String faveId;

    @JsonProperty("_children")
    private List<FavoritesVO> children = new ArrayList<>();
}
