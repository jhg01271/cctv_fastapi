//package kr.co.igns.framework.api.menu.dto;
//
//import java.util.ArrayList;
//
//import kr.co.igns.framework.api.menu.entity.MenuEntity;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//public class MenuRequestDto {
//
//	@Setter
//	private int menuId; // 메뉴ID
//	private String menuNm; // 메뉴명
//	private String menuNmCd; // 메뉴명코드
//	@Setter
//	private int parentId; // 부모메뉴ID
//	private int lvl; // 레벨
//	private int sort; // 순서
//	private String menuIcon; // 메뉴아이콘
//	private String useYn; // 사용여부
//	private String svcGubn; // W:WEB, M:MOBILE
//	private String svcType; // 서비스 타입 N : 일반, P : 준비중, I : 점검중
//
//	@Setter
//	private String createdBy; // 생성자ID
//	@Setter
//	private String updatedBy; // 수정자ID
//
//	private String menuEngNm; // 메뉴 영문이름
//	private boolean __created__; // 프론트단의 새로만들어진 열에대한 여부
//
//	private String routerPath; // 메뉴 라우터경로
//	private String routerName; // 메뉴 라우터명
//
//	private ArrayList<MenuRequestDto> items;  // 하위메뉴들
//
//	public MenuEntity toEntity() {
//		return MenuEntity.builder().menuId(menuId).menuNm(menuNm).menuNmCd(menuNmCd).parentId(parentId).lvl(lvl).sort(sort)
//				.menuIcon(menuIcon).useYn(useYn).svcGubn(svcGubn).svcType(svcType).createdBy(createdBy)
//				.updatedBy(updatedBy).menuEngNm(menuEngNm).routerName(routerName).routerPath(routerPath).build();
//	}
//
//}
