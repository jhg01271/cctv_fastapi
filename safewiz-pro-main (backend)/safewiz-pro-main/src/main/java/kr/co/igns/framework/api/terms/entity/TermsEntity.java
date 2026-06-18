//package kr.co.igns.framework.api.terms.entity;
//
//import java.util.UUID;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//import org.hibernate.annotations.GenericGenerator;
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
//@Table(name="tb_terms")
//public class TermsEntity extends BaseEntity {
//	
//	@Id
//	@GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uui2d", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", columnDefinition = "uuid DEFAULT public.uuid_generate_v1()")
//	@Comment("약관 ID")
//	private UUID id;
//	
//	@Comment("약관내용")
//	@Column(name="content", nullable = false , columnDefinition = "text")
//	@NotNull
//	private String content;
//	
//	@Comment("종류 (이용약관 : B , 개인정보 : P ...)")
//	@Column(name="type", nullable = false)
//	@NotNull
//	private String type;
//	
//	@Comment("서비스(웹:W, 모바일:A)")
//	@Column(name="service_type" , columnDefinition = "varchar(1)")
//	private String serviceType;
//	
//	@Comment("게시일(yyyy-MM-dd)")
//	@Column(name="publish_date" , columnDefinition = "varchar(10)")
//	private String publishDate;
//	
//	@Comment("컨텐츠 타입(md, json, html ..)")
//	@Column(name="content_type" , columnDefinition = "varchar(10)")
//	private String contentType;
//		
//	@Builder	
//	public TermsEntity(UUID id, String content, String type, String serviceType, String publishDate, String contentType, String createdBy , String updatedBy) {
//		this.id = id;
//		this.content = content;
//		this.type = type;
//		this.serviceType = serviceType;
//		this.publishDate = publishDate;
//		this.contentType = contentType;
//		setCreatedBy(createdBy);
//		setUpdatedBy(updatedBy);
//	}
//}
