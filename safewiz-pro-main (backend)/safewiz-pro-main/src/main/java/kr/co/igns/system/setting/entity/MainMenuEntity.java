package kr.co.igns.system.setting.entity;

import kr.co.igns.framework.utils.etc.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert // insert 시 null 필드 제외
@DynamicUpdate // update 시 null 필드 제외
@Table(name = "tb_sys_menu", schema="esg")
public class MainMenuEntity extends BaseEntity {
	@Comment("아이디")
	@Id
	@Column(updatable = false, nullable = false, columnDefinition = "varchar(20)")
	private String menuId;

	@Comment("메뉴명")
	@Column(name = "menu_nm", nullable = true, columnDefinition = "varchar")
	private String menuNm;

	@Comment("메뉴영문명")
	@Column(name = "menu_nm_eng", nullable = true, columnDefinition = "varchar")
	private String menuNmEng;

	@Comment("메뉴 아이콘")
	@Column(name = "menu_icon", nullable = true, columnDefinition = "varchar")
	private String menuIcon;

	@Comment("라우터 이름")
	@Column(name = "router_nm", nullable = true, columnDefinition = "varchar")
	private String routerNm;

	@Comment("라우터 경로")
	@Column(name = "router_path", nullable = true, columnDefinition = "varchar")
	private String routerPath;

	@Comment("뷰 파일 경로")
	@Column(name = "view_path", nullable = true, columnDefinition = "varchar")
	private String viewPath;

	@Comment("파라미터")
	@Column(name = "param", nullable = true, columnDefinition = "varchar")
	private String param;

	@Comment("표시 순서")
	@Column(name = "menu_odr", nullable = true, columnDefinition = "integer")
	private Integer menuOdr;

	@Comment("상위 아이디")
	@Column(name = "up_menu_id", nullable = true, columnDefinition = "varchar(20)")
	private String upMenuId;

}
