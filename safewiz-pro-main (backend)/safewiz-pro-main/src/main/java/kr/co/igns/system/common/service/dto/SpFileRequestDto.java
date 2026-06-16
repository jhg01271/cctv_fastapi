package kr.co.igns.system.common.service.dto;

import kr.co.igns.system.common.code.YesNo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpFileRequestDto {

	private String fileId;
	private String compId;
	private String targetType;
	private String targetId;
	private String filePath;
	private String fileNm;
	private String saveNm;
	private String fileType;
	private String extension;
	private int fileSize;
	private YesNo useYn;
	private YesNo delYn;


//	public FileEntity toEntity() {
//		FemsFileEntity builder = FemsFileEntity.builder()
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
//		if (StringUtils.isNotEmpty(fileId)) {
//			builder.fileId(fileId);
//		}
//		return builder.build();
//	}

}
