//package kr.co.igns.business.comp.company.entity;
//
//import kr.co.igns.framework.utils.etc.BaseEntity;
//import lombok.*;
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import javax.persistence.*;
//
//
///**
// * <pre>
// * ※ 프로젝트 : sp_backend
// * ※ 패키지 : kr.co.igns.comp.company.entity
// * ※ 파일명 : CompEntity.java
// * ※ 기능명 :
// * ※ 작성자 : 조동하
// * ※ 최초생성일 : 2024. 7. 18.
// * ※ 프로그램 설명 : 
// * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
// *
// * </pre>
// */
//
////[2024-07-23] 패키지 변경 - 신택훈
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//@DynamicInsert // insert 시 null 필드 제외
//@DynamicUpdate // update 시 null 필드 제외
//@Table(name = "tb_comp_user")
//public class CompanyUserEntity extends BaseEntity {
//
//	@Comment("멤버 순번")
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(updatable = false, nullable = false)
//	private Long memSeq;
//	
//	@Comment("사업장 아이디")
//	@Column(length = 20)
//	private String compId;
//
//	@Comment("권한 순번")
//	@Column
//	private Long grpCd;
//
//	@Comment("사용자 아이디")
//	@Column(length = 20)
//	private String userId;
//}
