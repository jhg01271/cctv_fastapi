//package kr.co.igns.framework.api.menu.dto;
//
//import java.time.LocalDateTime;
//
//import kr.co.igns.framework.api.menu.entity.MenuEntity;
//import lombok.Getter;
//
//@Getter
//public class MenuResponseDto {
//
//	private int menuId;
//	private String menuNm;
//	private String menuNmCd;
//	private int parentId;
//	private int lvl;
//	private int sort;
//	private String menuIcon;
//	private String useYn;
//	private String svcGubn;
//	private String svcType;
//	private String menuEngNm;
//	private LocalDateTime createdAt;
//	private String createdBy;
//	private LocalDateTime updatedAt;
//	private String updatedBy;
//
//	public MenuResponseDto(MenuEntity entity) {
//		this.menuId = entity.getMenuId();
//		this.menuNm = entity.getMenuNm();
//		this.menuNmCd = entity.getMenuNmCd();
//		this.parentId = entity.getParentId();
//		this.lvl = entity.getLvl();
//		this.sort = entity.getSort();
//		this.menuIcon = entity.getMenuIcon();
//		this.useYn = entity.getUseYn();
//		this.svcGubn = entity.getSvcGubn();
//		this.svcType = entity.getSvcType();
//		this.menuEngNm = entity.getMenuEngNm();
//		this.createdAt = entity.getCreatedAt();
//		this.createdBy = entity.getCreatedBy();
//		this.updatedAt = entity.getCreatedAt();
//		this.updatedBy = entity.getCreatedBy();
//	}
//}
