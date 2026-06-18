//package kr.co.igns.framework.api.menu.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.Table;
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
//@IdClass(AuthPk.class)
//@Table(name = "tb_auth_group_map_m")
//public class AuthGroupMapEntity extends BaseEntity {
//
//	@Id
//	@Comment("권한그룹코드")
//	@Column(name = "grp_auth_cd", columnDefinition = "varchar(100)")
//	private String grpAuthCd;
//
//	@Id
//	@Comment("메뉴ID")
//	@Column(name = "menu_id", columnDefinition = "int4")
//	private int menuId;
//
//	@Comment("조회권한여부")
//	@Column(name = "sch_ah", columnDefinition = "varchar(1)")
//	private String schAh;
//
//	@Comment("저장권한여부")
//	@Column(name = "sav_ah", columnDefinition = "varchar(1)")
//	private String savAh;
//
//	@Comment("엑셀권한여부")
//	@Column(name = "exl_ah", columnDefinition = "varchar(1)")
//	private String exlAh;
//
//	@Comment("삭제권한여부")
//	@Column(name = "del_ah", columnDefinition = "varchar(1)")
//	private String delAh;
//
//	@Comment("기타권한1(임시)")
//	@Column(name = "fn1_ah", columnDefinition = "varchar(1)")
//	private String fn1Ah;
//
//	@Comment("기타권한2(임시)")
//	@Column(name = "fn2_ah", columnDefinition = "varchar(1)")
//	private String fn2Ah;
//
//	@Comment("기타권한3(임시)")
//	@Column(name = "fn3_ah", columnDefinition = "varchar(1)")
//	private String fn3Ah;
//
//	@Comment("기타권한4(임시)")
//	@Column(name = "fn4_ah", columnDefinition = "varchar(1)")
//	private String fn4Ah;
//
//	@Comment("기타권한5(임시)")
//	@Column(name = "fn5_ah", columnDefinition = "varchar(1)")
//	private String fn5Ah;
//
//	@Comment("즐겨찾기 여부")
//	@Column(name = "favorite_yn", columnDefinition = "varchar(1)")
//	private String favoriteYn;
//
//	@Builder
//	public AuthGroupMapEntity(String grpAuthCd, int menuId, String schAh, String savAh, String exlAh, String delAh,
//			String fn1Ah, String fn2Ah, String fn3Ah, String fn4Ah, String fn5Ah, String favoriteYn, String createdBy,
//			String updatedBy) {
//		this.grpAuthCd = grpAuthCd;
//		this.menuId = menuId;
//		this.schAh = schAh;
//		this.savAh = savAh;
//		this.exlAh = exlAh;
//		this.delAh = delAh;
//		this.fn1Ah = fn1Ah;
//		this.fn2Ah = fn2Ah;
//		this.fn3Ah = fn3Ah;
//		this.fn4Ah = fn4Ah;
//		this.fn5Ah = fn5Ah;
//		this.favoriteYn = favoriteYn;
//		setCreatedBy(createdBy);
//		setUpdatedBy(updatedBy);
//	}
//
//}
