package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class LoginHistoryVO extends BaseVO {
    @Schema(description = "번호", example = "")
    private String rowNum;
    @Schema(description = "로그인 날짜", example = "")
    private String loginDt;
    @Schema(description = "사용자코드", example = "")
    private String userId;
    @Schema(description = "사용자명", example = "")
    private String userNm;
    @Schema(description = "로그인 일시", example = "")
    private String loginDate;
    @Schema(description = "ip", example = "")
    private String loginIp;
    @Schema(description = "로그인 OS", example = "")
    private String loginDevice;
    @Schema(description = "로그인 성공여부", example = "")
    private String loginSuccessYn;
    @Schema(description = "로그인 실패 횟수", example = "")
    private int loginFailCount;
}
