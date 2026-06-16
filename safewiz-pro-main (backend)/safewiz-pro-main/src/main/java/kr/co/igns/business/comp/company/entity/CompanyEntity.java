//package kr.co.igns.business.comp.company.entity;
//import kr.co.igns.framework.utils.etc.BaseEntity;
//import kr.co.igns.system.common.code.YesNo;
//import lombok.*;
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//
///**
// * <pre>
// * ※ 프로젝트 : sp_backend
// * ※ 패키지 : kr.co.igns.fems.company.entity
// * ※ 파일명 : CompanyEntity.java
// * ※ 기능명 :
// * ※ 작성자 : 이하운
// * ※ 최초생성일 : 2024. 5. 17.
// * ※ 프로그램 설명 : 
// * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
// *
// * </pre>
// */
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//@DynamicInsert // insert 시 null 필드 제외
//@DynamicUpdate // update 시 null 필드 제외
//@Table(name = "tb_comp_info")
//public class CompanyEntity extends BaseEntity {
//
//	@Comment("사업장 아이디")
//	@Id
//	@Column(updatable = false, nullable = false, length = 20)
//	private String compId;
//	
//	@Comment("사업장 이름")
//	@Column
//	private String compNm;
//
//	@Comment("사업장 소개")
//	@Column
//	private String compRmk;
//
//	@Comment("사업자 등록 번호")
//	@Column(length = 30)
//	private String rgstNo;
//
//	@Comment("법인 등록 번호")
//	@Column(length = 20)
//	private String corpNo;
//
//	@Comment("대표자")
//	@Column
//	private String rpstNm;
//	
//	@Comment("우편번호")
//	@Column
//	private String zipCd;
//	
//	@Comment("주소")
//	@Column
//	private String addrs1;
//	
//	@Comment("상세주소")
//	@Column
//	private String addrs2;
//	
//	@Comment("담당자")
//	@Column
//	private String chrgPrsn;
//	
//	@Comment("연락처")
//	@Column
//	private String telNo;
//	
//	@Comment("업종")
//	@Column
//	private String bizNm;
//	
//	@Comment("업종 코드")
//	@Column
//	private String bizCd;
//	
//	@Comment("주요 생산품")
//	@Column
//	private String mainPrdt;
//	
//	@Comment("연간 생산량")
//	@Column
//	private Integer prdAmt;
//	
//	@Comment("사업장 사진")
//	@Column
//	private Long photoFileId;
//	
//	// String으로 변경 
//	@Comment("준공년월")
//	@Column
//	private LocalDateTime cmpltDe;
//	
//	@Comment("연면적")
//	@Column
//	private String flArea;
//	
//	@Comment("매출 금액")
//	@Column(precision = 20, scale = 6)
//	private BigDecimal salesAmt;
//	
//	@Comment("상시  종업원 수")
//	@Column
//	private Integer fullEmplCnt;
//	
//	@Comment("일평균 근무 시간")
//	@Column
//	private Integer avrgWorkHr;
//	
//	// [2024-07-11] 사용여부 컬럼 추가 - 신택훈
//	@Comment("삭제여부")
//	@Column(columnDefinition = "varchar(3) default 'N'")
//	@NotNull
//	private YesNo delYn;
//	
//	// [2024-07-17] 첨부파일 아이디 컬럼 추가 - 신택훈
//	@Comment("첨부파일")
//	@Column
//	private String attachId;
//
//	@Comment("초대키")
//	@Column(length = 500)
//	private String inviteKey;
//
//}
