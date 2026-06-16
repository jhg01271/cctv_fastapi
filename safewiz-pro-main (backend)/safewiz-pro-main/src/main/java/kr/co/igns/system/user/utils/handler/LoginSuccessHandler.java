package kr.co.igns.system.user.utils.handler;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.system.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final UserDAO userDao;
	private final MessageService messageService;
	private final LoginService loginService;
	
	@Value("${jwt.access.expiration}")
	private String accessTokenExpiration;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpiration;

	@Value("${user.remember-user-id}")
	private Long rememberUserIdExpiration;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		String userId = extractUsername(authentication); // 인증 정보에서 Username(id) 추출
		JSONObject jsonResponse = new JSONObject(); // 실어서 응답
		ResponseEntity<SingleResponseDto<HashMap<String, Object>>>  rtn = null;

		HashMap<String, Object> result = new HashMap<String, Object>();
		UserVO user = userDao.findOne(userId);
		if (userId != null) {
            String loginIp = loginService.getClientIp(request);
            user.setIp(loginIp);
			String accessToken = jwtService.createAccessToken(user); // JwtService의 createAccessToken을 사용하여 AccessToken발급
			String refreshToken = jwtService.createRefreshToken(user); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
			jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken,RefreshToken

			JSONObject data = new JSONObject();
			data.put("accessToken", accessToken);
			data.put("grpAuthCd", Aes256.encryptString(SecurityUtil.getCurrentRole()));

			result.put("result", data);
			jsonResponse.put("success", true);
			jsonResponse.put("data", result);
			rtn = ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
			LogUtil.ConsoleLogInfo(HttpStatus.OK, "[TOKEN_DEBUG]", "[저장 UID : " + user.getUid() + "]:::[저장 ID : " + user.getUserId() + "]:::[REFRESH_TOKEN : " + refreshToken + "]", null);

			// 쿠키 가져오기
			Cookie[] cookies = request.getCookies();
			String autoId = "";
			String rememberUserId = "";
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					System.out.println("쿠키 이름: " + cookie.getName() + ", 값: " + cookie.getValue());

					// 특정 쿠키 값 가져오기 (예: "userId" 쿠키 찾기)
					if ("AUTOID".equals(cookie.getName())) {
						autoId = cookie.getValue();
						ResponseCookie rCookie = ResponseCookie.from("AUTOID", autoId).maxAge(rememberUserIdExpiration)
								.path("/").secure(false).httpOnly(false).build();
						response.addHeader("Set-Cookie", rCookie.toString());
					}
					else if ("USERID".equals(cookie.getName())) {
						rememberUserId = cookie.getValue();
						ResponseCookie rCookie = ResponseCookie.from("USERID", rememberUserId).maxAge(rememberUserIdExpiration)
								.path("/").secure(false).httpOnly(false).build();
						response.addHeader("Set-Cookie", rCookie.toString());
					}
				}
			} else {
				System.out.println("쿠키가 없습니다.");
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		out.print(jsonResponse.toString());
		out.flush();
		LogUtil.ConsoleLogInfo(HttpStatus.OK, "LOGIN SUCCESS", userId, request);

		//#region 로그인 이력 처리
        try {
			loginService.saveLoginHistory(request,"Y");
        } catch (Exception e) {
			LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "SAVE LOGIN HiSTORY FAIL", e.getMessage(), request);
        }
		//#endregion
    }

	private String extractUsername(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUsername();
	}
}
