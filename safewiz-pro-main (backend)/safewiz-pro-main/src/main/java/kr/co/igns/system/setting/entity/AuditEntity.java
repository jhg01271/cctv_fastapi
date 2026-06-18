package kr.co.igns.system.setting.entity;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import kr.co.igns.framework.utils.etc.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert // insert 시 null 필드 제외
@DynamicUpdate // update 시 null 필드 제외
@Table(name = "tb_sys_audit", schema="esg")
public class AuditEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2642990386270264197L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long seq;

	
	@Column(length = 500)
	private String				referer;

	@Column(length = 500)
	private String				url;

	@Column(length = 500)
	private String				param;

	@Column(length = 200)
	private String				userLocale;

	@Column(length = 200)
	private String				localName;

	@Column(length = 200)
	private String				os;

	@Column(length = 200)
	private String				ipAddress;

	@Column(length = 200)
	private String				browser;

	@Column(length = 300)
	private String				userAgent;


	@Column(length = 200)
	private String				sessionId;

	@Column(length = 100)
	private String				userId;

}