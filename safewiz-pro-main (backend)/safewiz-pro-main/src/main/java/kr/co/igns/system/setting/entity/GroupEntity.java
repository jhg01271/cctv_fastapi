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
@Table(name = "tb_comp_group" , schema="esg")
public class GroupEntity extends BaseEntity {

	@Id
	@Comment("코드")
	@Column(name = "grp_cd", nullable = false, columnDefinition = "varchar(30)")
	private String grpCd;

	@Comment("코드명")
	@Column(name = "grp_nm", nullable = false, columnDefinition = "varchar(30)")
	private String grpNm;

	@Comment("표시 순서")
	@Column
	private int grpOdr;

}
