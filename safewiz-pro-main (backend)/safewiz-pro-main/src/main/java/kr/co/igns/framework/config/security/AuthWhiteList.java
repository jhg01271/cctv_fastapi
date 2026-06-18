package kr.co.igns.framework.config.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AuthWhiteList {

	/**
	 * Rest API 호출 시 인증 없이 접근 허용할 URL 정리
	 */
	String[] AUTH_WHITE = new String[] { "/", "/resources/**", "/webjars/**", "/favicon.ico", "/igns/common", "/igns/common/terms/**",
			"/igns/common/file", "/signup", "/signup/**", "/v2/api-docs", "/swagger-resources/**", "/swagger/**",
			"/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/ws/**",
			"/igns/common/blockChain/**", "/igns/common/menuByUser/**",	"/igns/common/menuByUserList/**"
//			, "/safewizpro/dashboard/**"
			};
	String[] AUTH_ADMIN = new String[] { "/igns/admin/v1/**" };

	/**
	 * String[] → List<String> 변환
	 * 
	 * @return List<String>
	 */
	public List<String> getAuthWhiteList() {
		List<String> AUTH_WHITELIST_LIST = new ArrayList<String>();
		AUTH_WHITELIST_LIST = Arrays.asList(AUTH_WHITE);
		return AUTH_WHITELIST_LIST;
	}

	public List<String> getAdminList() {
		List<String> ADMIN_LIST = new ArrayList<String>();
		ADMIN_LIST = Arrays.asList(AUTH_ADMIN);
		return ADMIN_LIST;
	}
}
