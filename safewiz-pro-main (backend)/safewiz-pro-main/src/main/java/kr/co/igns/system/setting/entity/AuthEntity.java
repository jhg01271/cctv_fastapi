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
@Table(name = "tb_comp_auth", schema="esg")
public class AuthEntity extends BaseEntity {
	@Comment("아이디")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long menuSeq;

	@Comment("권한 순번")
	@Column(name = "grp_cd", nullable = false)
	private Long grpCd;

	@Comment("코드")
	@Column(name = "menu_id", nullable = false, columnDefinition = "varchar(20)")
	private String menuId;

	@Comment("저장")
	@Column(name = "sav_ah", nullable = false, columnDefinition = "varchar(20)")
	private String savAh;

	@Comment("삭제")
	@Column(name = "del_ah", nullable = false, columnDefinition = "varchar(20)")
	private String delAh;

	@Comment("엑셀")
	@Column(name = "exl_ah", nullable = false, columnDefinition = "varchar(20)")
	private String exlAh;
}
