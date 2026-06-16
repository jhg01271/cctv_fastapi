//package kr.co.igns.framework.api.file.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
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
//@Table(name="tb_file_info")
//public class FileEntity extends BaseEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Comment("파일 ID")
//	@Column(name = "file_id")
//	private Long fileId;
//
//	@Comment("사업장 아이디")
//	@Column(name = "comp_id", columnDefinition = "varchar(30)")
//	private String compId;
//	
//	@Comment("타겟 파일 타입")
//	@Column(name = "target_type", columnDefinition = "varchar(50)")	
//	private String targetType;
//	
//	@Comment("타겟 파일 ID")
//	@Column(name = "target_id", columnDefinition = "varchar(100)")
//	@NotNull
//	private String targetId;
//	
//	@Comment("파일 Path")
//	@Column(name = "file_path", columnDefinition = "varchar(200)")
//	@NotNull
//	private String filePath;
//	
//	@Comment("파일 Origin 이름")
//	@Column(name = "file_nm", columnDefinition = "varchar(200)")
//	@NotNull
//	private String fileNm;
//	
//	@Comment("파일 서버 저장용 이름")
//	@Column(name = "save_nm", columnDefinition = "varchar(350)")
//	@NotNull
//	private String saveNm;
//	
//	@Comment("파일 타입")
//	@Column(name = "file_type", columnDefinition = "varchar(50)")
//	@NotNull
//	private String fileType;
//	
//	@Comment("파일 확장자")
//	@Column(name = "extension", columnDefinition = "varchar(10)")
//	@NotNull
//	private String extension;
//	
//	@Comment("파일 사이즈(MB)")
//	@Column(name = "file_size", columnDefinition = "varchar(30)")
//	@NotNull
//	private String fileSize;
//	
//	@Comment("파일 삭제 여부")
//	@Column(name = "del_yn", columnDefinition = "varchar(1)")
//	@NotNull
//	private String delYn;
//	
//	@Comment("파일 비고")
//	@Column(name = "file_desc", columnDefinition = "varchar(200)")
//	private String fileDesc;
//	
//	@Comment("파일 순서")
//	@Column(name = "file_seq")
//	@ColumnDefault("0")
//	private int fileSeq;
//	
//	@Comment("파일 사용 여부")
//	@Column(name = "use_yn", columnDefinition = "varchar(1) default 'Y'")
//	@NotNull
//	private String useYn;
//	
//	@Builder
//	public FileEntity(Long fileId, String targetType, String targetId, String filePath, String fileNm, String saveNm,
//			String fileType, String extension, String fileSize, String delYn, String fileDesc, int fileSeq, String useYn, 
//			String createdBy, String updatedBy, String compId) {
//		this.fileId = fileId;
//		this.targetType = targetType;
//		this.targetId = targetId;
//		this.filePath = filePath;
//		this.fileNm = fileNm;
//		this.saveNm = saveNm;
//		this.fileType = fileType;
//		this.extension = extension;
//		this.fileSize = fileSize;
//		this.delYn = delYn;
//		this.fileDesc = fileDesc;
//		this.fileSeq = fileSeq;
//		this.useYn = useYn;
//		this.compId = compId;
//		setCreatedBy(createdBy);
//		setUpdatedBy(updatedBy);
//	}
//}
