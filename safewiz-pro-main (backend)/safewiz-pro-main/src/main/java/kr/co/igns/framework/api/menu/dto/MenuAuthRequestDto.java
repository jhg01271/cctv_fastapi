//package kr.co.igns.framework.api.menu.dto;
//
//import kr.co.igns.framework.api.menu.entity.AuthGroupMapEntity;
//import lombok.Data;
//
//@Data
//public class MenuAuthRequestDto {
//
//	private int menuId;
//	private String grpAuthCd;
//	private String svcGubn;
//	private Boolean auth;
//	private Boolean isAllMenu;
//	private int[] menuIdList; 
//	private int saveType;
//	private String createdBy;
//	private String updatedBy;
//	
//	public AuthGroupMapEntity toEntity() {
//		return AuthGroupMapEntity.builder().grpAuthCd(grpAuthCd).menuId(menuId).schAh("Y").savAh("Y").exlAh("Y")
//				.delAh("Y").fn1Ah("Y").fn2Ah("Y").fn3Ah("Y").fn4Ah("Y").fn5Ah("Y").favoriteYn("Y")
//				.createdBy(createdBy).updatedBy(updatedBy).build();
//
//	}
//
//
//}
