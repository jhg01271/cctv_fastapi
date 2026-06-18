package kr.co.igns.system.user.utils.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtWhiteList {


    String[] White = new String[]{
            "/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**",
            "/base/signin",
            "/igns/auth/oauth2/**", "/igns/auth/sign-up", "/igns/auth/login/**", "/igns/auth/login/kakao",
            "/igns/auth/otp/**", "/igns/auth/resetPassword", "/igns/auth/user/checkId",
            "/igns/auth/verification/**", "/igns/auth/password", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
//            "/safewizpro/getComp", "/safewizpro/getDashboard/**"
//		 "/safewizpro/dashboard/**"
    };

    /**
     * String[] → List<String> 변환
     *
     * @return List<String>
     */
    public List<String> getAuthWhiteList() {
        List<String> AUTH_WHITELIST_LIST = new ArrayList<String>();
        AUTH_WHITELIST_LIST = Arrays.asList(White);
        return AUTH_WHITELIST_LIST;
    }
}
