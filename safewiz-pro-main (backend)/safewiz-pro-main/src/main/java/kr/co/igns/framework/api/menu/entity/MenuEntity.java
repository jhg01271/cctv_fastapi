//package kr.co.igns.framework.api.menu.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import kr.co.igns.framework.utils.etc.BaseEntity;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//@DynamicInsert // insert 시 null 필드 제외
//@DynamicUpdate // update 시 null 필드 제외
//@Table(name="tb_menu_info")
//public class MenuEntity extends BaseEntity {
//
//	@Id
//	@Comment("메뉴 ID")
//	@Column(name = "menu_id", nullable = false)
//	@NotNull
//	private int menuId;
//
//	@Comment("메뉴명")
//	@Column(name = "menu_nm", nullable = false, columnDefinition = "varchar(100)")
//	private String menuNm;
//
//	@Comment("메뉴명 코드")
//	@Column(name = "menu_nm_cd", nullable = false, columnDefinition = "varchar(100)")
//	private String menuNmCd;
//
//	@Comment("부모메뉴 ID")
//	@Column(name = "parent_id", nullable = false)
//	private int parentId;
//
//	@Comment("레벨")
//	@Column(name = "lvl", nullable = false)
//	private int lvl;
//
//	@Comment("순서")
//	@Column(name = "sort", nullable = false)
//	private int sort;
//
//	@Comment("메뉴아이콘")
//	@Column(name = "menu_icon", nullable = false, columnDefinition = "varchar(50)")
//	private String menuIcon;
//
//	@Comment("사용여부")
//	@Column(name = "use_yn", nullable = false, columnDefinition = "varchar(1) default 'Y'")
//	private String useYn;
//
//	@Comment("서비스 구분")
//	@Column(name = "svc_gubn", nullable = false, columnDefinition = "varchar(1) default 'W'")
//	private String svcGubn;
//
//	@Comment("서비스 타입")
//	@Column(name = "svc_type", nullable = false, columnDefinition = "varchar(1)")
//	private String svcType;
//
//	@Comment("메뉴 영문이름")
//	@Column(name = "menu_eng_nm", nullable = false, columnDefinition = "varchar(100)")
//	private String menuEngNm;
//
//	@Comment("라우터 경로")
//	@Column(name = "router_path" , columnDefinition = "varchar(100)")
//	private String routerPath;
//
//	@Comment("라우터 이름")
//	@Column(name = "router_name" , columnDefinition ="varchar(100)")
//	private String routerName;
//
//	@Builder
//	public MenuEntity(int menuId, String menuNm, String menuNmCd, int parentId, int lvl, int sort ,String menuIcon, String useYn, String svcGubn, String svcType, String menuEngNm, String routerPath, String routerName, String createdBy, String updatedBy) {
//		this.menuId = menuId;
//		this.menuNm = menuNm;
//		this.menuNmCd = menuNmCd;
//		this.parentId = parentId;
//		this.lvl = lvl;
//		this.sort = sort;
//		this.menuIcon = menuIcon;
//		this.useYn = useYn;
//		this.svcGubn = svcGubn;
//		this.svcType = svcType;
//		this.menuEngNm = menuEngNm;
//		this.routerName = routerName;
//		this.routerPath = routerPath;
//		setCreatedBy(createdBy);
//		setUpdatedBy(updatedBy);
//	}
//}
