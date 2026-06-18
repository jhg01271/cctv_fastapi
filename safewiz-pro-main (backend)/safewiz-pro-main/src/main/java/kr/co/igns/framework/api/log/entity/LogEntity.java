package kr.co.igns.framework.api.log.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert // insert 시 null 필드 제외
@DynamicUpdate // update 시 null 필드 제외
@Table(name="tb_application_log", schema="esg")
public class LogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("로그 ID")
	@Column(name = "log_idx")
	private Long logIdx;
	
	@Comment("로그 레벨")
	@Column(name = "log_level", columnDefinition = "varchar(10)")	
	private String logLevel;
	
	@Comment("로그 위치")
	@Column(columnDefinition = "varchar(100)")
	private String location;
	
	@Comment("메세지")
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@Comment("로그 날짜")
	@Column(name = "log_date", columnDefinition = "varchar(25)")
	private String logDate;
	
	@Comment("오류 내용")
	@Column(columnDefinition = "varchar(1000)")
	private String throwable;
	
	@Builder
	public LogEntity(String logLevel, String location, String message, String logDate, String throwable) {
		this.logLevel = logLevel;
		this.location = location;
		this.message = message;
		this.logDate = logDate;
		this.throwable = throwable;
	}
}
