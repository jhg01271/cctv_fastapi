package kr.co.igns.system.common.entity;
import kr.co.igns.framework.utils.etc.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.common.entitity
 * ※ 파일명 : FileEntity.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 7. 2.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@DynamicUpdate // update 시 null 필드 제외
@Table(name = "tb_sys_file", schema = "esg")
public class SpFileEntity extends BaseEntity {

	@Comment("파일 아이디")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false, length = 20)
	private String fileId;
	
	@Comment("사업장 아이디")
	@Column
	private String compId;

	@Comment("기능명")
	@Column
	private String targetType;
	
	@Comment("연결 키")
	@Column
	private String targetId;
	
	@Comment("저장 파일명")
	@Column
	private String saveNm;//파일명
	
	@Comment("파일 종류")
	@Column
	private String fileType;//IMAGE
	
	@Comment("파일크기")
	@Column
	private Long fileSize;
	
	@Comment("첨부 순서")
	@Column
	private int fileSeq;
	
	@Comment("파일 경로")
	@Column
	private String filePath;// 절대 경로만 
	
	@Comment("파일명")
	@Column
	private String fileNm;
	
	@Comment("파일 설명")
	@Column
	private String fileDesc;
	
	@Comment("확장자")
	@Column
	private String extension;
	
	@Comment("삭제 여부")
	@Column(columnDefinition = "varchar(3) default 'N'")
	private String delYn;
	
	@Comment("사용 여부")
	@Column(columnDefinition = "varchar(3) default 'N'")
	private String useYn;

    @Comment("생성자")
    @Column
    private String createdBy;
	

}
