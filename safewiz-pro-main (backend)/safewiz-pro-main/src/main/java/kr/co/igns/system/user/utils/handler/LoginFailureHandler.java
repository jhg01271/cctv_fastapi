package kr.co.igns.system.user.utils.handler;

import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import kr.co.igns.system.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final LoginService loginService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    	JSONObject jsonResponse = new JSONObject();		
    	jsonResponse.put("success", false);
		jsonResponse.put("code", 400);
        jsonResponse.put("msg", "로그인할 수 없습니다.\n아이디 또는 비밀번호를 확인해주세요.");
    	
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
        LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "LOGIN FAIL", exception.getMessage(), request);

        //#region 로그인 이력 처리
        try {
            loginService.saveLoginHistory(request,"N");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //#endregion
    }


}
