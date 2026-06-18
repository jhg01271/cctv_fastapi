package kr.co.igns.system.common.model;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;

@Data
public class FileVO extends BaseVO {

	@Schema(description = "파일 ID", example = "202408200001")
    private String fileId;    // 파일 ID
    private String fileName;  // 파일 이름
    private String fileExt;    // 파일 확장자
    private String fileType;    // 파일 타입
    private String filePath;    // 파일 경로
    private Long fileSize;    // 파일 크기
    private String extension;

    private String targetId;
    private String targetType;

}