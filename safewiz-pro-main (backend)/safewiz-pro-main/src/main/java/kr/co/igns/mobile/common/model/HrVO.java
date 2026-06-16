package kr.co.igns.mobile.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import lombok.Data;

@Data
public class HrVO {
	@Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "인원 ID")
    private String hrId;
    @Schema(description = "성명")
    private String hrNm;
    @Schema(description = "사원번호")
    private String sabun;
    @Schema(description = "직책/직위")
    private String jbrp;
    @Schema(description = "직책/직위")
    private String jbrpNm;
    @Schema(description = "위험성평가 담당")
    private String riskAssRole;
    @Schema(description = "위험성평가 담당명")
    private String riskAssRoleNm;
    @Schema(description = "조직번호")
    private String orgnId;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "이메일주소")
    private String email;
    @Schema(description = "폰번호")
    private String phone;
    @Schema(description = "서명")
    private String signature;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "정렬순서")
    private int ordSeq;
    @Schema(description = "우편번호")
    private String zipCd;
    @Schema(description = "주소1")
    private String addrs1;
    @Schema(description = "주소2")
    private String addrs2;
    @Schema(description = "권한")
    private String role;
    @Schema(description = "card의 logo")
    private String logoId;
    
    private boolean signed;
}
