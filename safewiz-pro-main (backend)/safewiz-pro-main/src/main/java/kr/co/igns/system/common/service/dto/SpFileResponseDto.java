package kr.co.igns.system.common.service.dto;

import kr.co.igns.system.common.entity.SpFileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpFileResponseDto {

	private String fileId;
	private String compId;
	private String targetType;
	private String targetId;
	private String filePath;
	private String fileNm;
	private String saveNm;
	private String fileType;
	private String extension;
	private Long fileSize;
	private String delYn;
	private String fileDesc;
	private int fileSeq;
	private String useYn;
	private String createdBy;
	private String updatedBy;
	
	public SpFileResponseDto(SpFileEntity entity) {
		this.fileId = entity.getFileId();
		this.targetType = entity.getTargetType();
		this.targetId = entity.getTargetId();
		this.filePath = entity.getFilePath();
		this.fileNm = entity.getFileNm();
		this.saveNm = entity.getSaveNm();
		this.fileType = entity.getFileType();
		this.extension = entity.getExtension();
		this.fileSize = entity.getFileSize();
		this.delYn = entity.getDelYn();
		this.fileDesc = entity.getFileDesc();
		this.fileSeq = entity.getFileSeq();
		this.useYn = entity.getUseYn();
		this.createdBy = entity.getCreatedBy();
		this.updatedBy = entity.getCreatedBy();
	}
}
