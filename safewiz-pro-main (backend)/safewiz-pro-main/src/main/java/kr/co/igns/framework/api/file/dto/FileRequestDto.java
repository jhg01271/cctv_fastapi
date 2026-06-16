//package kr.co.igns.framework.api.file.dto;
//
//import kr.co.igns.framework.api.file.entity.FileEntity;
//import lombok.Data;
//
//@Data
//public class FileRequestDto {
//
//	private long fileId;
//	private String compId;
//	private String targetType;
//	private String targetId;
//	private String filePath;
//	private String fileNm;
//	private String saveNm;
//	private String fileType;
//	private String extension;
//	private String fileSize;
//	private String useYn;
//	private String delYn;
//
//
//	public FileEntity toEntity() {
//		FileEntity.FileEntityBuilder builder = FileEntity.builder()
//				.compId(compId)
//				.targetType(targetType)
//				.targetId(targetId)
//				.filePath(filePath)
//				.fileNm(fileNm)
//				.saveNm(saveNm)
//				.fileType(fileType)
//				.extension(extension)
//				.fileSize(fileSize)
//				.useYn(useYn)
//				.delYn(delYn);
//
//		if (fileId != 0) {
//			builder.fileId(fileId);
//		}
//		return builder.build();
//	}
//
//}
