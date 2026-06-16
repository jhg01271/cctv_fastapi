package kr.co.igns.mobile.signatures.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignaturesVO {
	@Schema(description = "참석자 HR ID", example = "")
    private String hrId;
	@Schema(description = "참석자  명", example = "")
    private String name;
	@Schema(description = "참석자 직책", example = "")
    private String jobTitle;
	@Schema(description = "참석자 조직명", example = "")
    private String orgName;
	@Schema(description = "나스에 저장된 프로필 이미지 ID", example = "")
    private String profileImageId;
	@Schema(description = "서명 여부", example = "")
    private Boolean signed;
	@Schema(description = "서명 일자", example = "")
    private String signedAt;
}
