package kr.co.igns.system.user.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.model
 * ※ 파일명 : UserVO.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 12.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

//@SuperBuilder
@Data
public class UserVO extends BaseVO {
	
	private static final long serialVersionUID = -2417117617959565367L;
	@Schema(description = "사용자 사진 ID", example = "사용자 사진 ID")
	private String logoId;

	@Schema(description = "사용자 아이디", example = "사용자 아이디")
	private String uid;

	@Schema(description = "고객 아이디", example = "사용자 아이디")
	private String hrId;

	@Schema(description = "고객 아이디", example = "고객 아이디")
	private String clntId;

	@Schema(description = "사용자 아이디", example = "사용자 아이디")
	private String userId;
	
	@Schema(description = "사용자 이름", example = "사용자 이름")
	private String userNm;

	@Schema(description = "사용자 이메일", example = "사용자 이메일")
	private String email;

	@Schema(description = "사용자 휴대전화번호", example = "사용자 번호")
	private String phone;
	
	@Schema(description = "성별", example = "성별")
	private String sexDiv;
	
	@Schema(description = "생년월일", example = "생년월일")
	private String birthDt;
	
	@Schema(description = "우편번호", example = "우편번호")
	private String zipCd;
	
	@Schema(description = "주소", example = "주소")
	private String addrs1;
	
	@Schema(description = "상세주소", example = "상세주소")
	private String addrs2;
	
	@Schema(description = "사용자 패스워드", example = "사용자 패스워드")
	private String userPs;
	
	@Schema(description = "패스워스 마지막 변경일", example = "패스워드 마지막 변경일")
	private String lastPsDate;
	
	@Schema(description = "사용자 권한", example = "사용자 권한")
	private String role;

	@Schema(description = "삭제여부", example = "삭제여부")
	private String delYn;
	
	@Schema(description = "로그인 실패 횟수", example = "로그인 실패 횟수")
	private Integer loginFailCnt;

	@Schema(description = "초대키", example = "BASE64...")
	private String inviteKey;

	private String signature;
	private String orgnId;		//사용자 조직 ID
	private String orgnNm;		//사용자 조직 명
	
	private String orgnHeadId;	//사용자 조직 HEAD 아이디
	private String orgnHeadNm;	//사용자 조직 HEAD 명

	private String jbrpId;		//사용자 직위 ID
	private String jbrpNm;		//사용자 직위 명

	private String fcmToken;	//사용자 FCM 토큰
	private String loginDevice;	//로그인 기종
	private String safewizProYn;//세이프위즈 알림 허용 여부

	@Schema(description = "사용자가 로그인 할 때 입력하는 ID", example = "사용자 아이디")
	private String loginId;

	@Schema(description = "사용자의 그룹 권한", example = "안전관리자")
	private String grpNm;

	@Schema(description = "사용자의 권한 코드", example = "MANAGER")
	private String 	authGroupCd;
	
	@Schema(description = "사용자의 권한", example = "관리자")
	private String 	authGroupNm;

    @Schema(description = "ip", example = "121.xxx.xxx.xxx")
    private String 	ip;

	// Password Encode
	public void passwordEncode(PasswordEncoder passwordEncoder) {
		this.userPs = passwordEncoder.encode(this.userPs);
	}

	public String getRoleName(){
		return this.role;
	}
}
