package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserManageVO extends BaseVO {
    @Schema(description = "고객 ID", example = "")
    private String clntId;
    @Schema(description = "변경된 고객 ID", example = "")
    private String targetClntId;
    @Schema(description = "기존 고객 ID", example = "")
    private String currentClntId;
    @Schema(description = "고객명", example = "")
    private String clntNm;
    @Schema(description = "seq", example = "")
    private String uuid;
    @Schema(description = "사용자코드", example = "고객번호+아이디")
    private String userId;
    @Schema(description = "사용자명", example = "")
    private String userNm;
    @Schema(description = "패스워드", example = "")
    private String userPs;
    @Schema(description = "이메일주소", example = "")
    private String email;
    @Schema(description = "폰번호", example = "")
    private String phone;
    @Schema(description = "서명", example = "")
    private String signature;
    @Schema(description = "fcm token id", example = "")
    private String fcmToken;
    @Schema(description = "권한그룹 코드", example = "master/manager/user")
    private String authGroupCd;
    @Schema(description = "권한그룹 코드", example = "master/manager/user")
    private String authGroupNm;
    @Schema(description = "비밀번호수정일", example = "")
    private String lastPsDate;
    @Schema(description = "마지막접속일자", example = "")
    private String lastLoginDate;
    @Schema(description = "마지막 접속 OS", example = "WEB/ANDROID/IOS")
    private String lastLoginOs;
    @Schema(description = "로그인 실패 카운트", example = "")
    private String loginFailCnt;
    @Schema(description = "ip", example = "")
    private String userIp;
    @Schema(description = "위치", example = "")
    private String userLocale;
    @Schema(description = "비고", example = "")
    private String remark;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "사업장ID", example = "202502210001")
    private String compId;
    @Schema(description = "작성자")
    private String createdBy;
    @Schema(description = "인원의 소속 사업장 'Y', 관리 사업장 'N'")
    private String orgnCompYn;
    @Schema(description = "인원ID")
    private String hrId;



    // Password Encode
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.userPs = passwordEncoder.encode(this.userPs);
    }
}
