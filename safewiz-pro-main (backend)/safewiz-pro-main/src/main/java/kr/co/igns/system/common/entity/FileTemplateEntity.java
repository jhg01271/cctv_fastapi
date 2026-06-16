package kr.co.igns.system.common.entity;
import kr.co.igns.framework.utils.etc.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Comment;
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
@Table(name = "tb_dataset_file_template", schema = "dataset")
public class FileTemplateEntity {

	@Comment("아이디")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false, length = 100)
	private String id;

	@Comment("파일 양식명")
	@Column
	private String idNm;

	@Comment("파일 타입")
	@Column
	private String fileType;
	
	@Comment("파일 경로")
	@Column
	private String filePath;
	
	@Comment("확장자")
	@Column
	private String extension;
	
	@Comment("사용 여부")
	@Column(columnDefinition = "varchar(1) default 'Y'")
	private String useYn;

	@Comment("비고")
	@Column
	private String remark;
	
	

}
