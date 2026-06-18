package kr.co.igns.system.user.utils.filter;
import com.google.gson.JsonObject;
import kr.co.igns.framework.config.security.AuthWhiteList;
import kr.co.igns.framework.config.security.TokenResponseHandler;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.utils.filter
 * ※ 파일명 : JwtFilter.java
 * ※ 기능명 : SSO 토큰 검증 필터
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 16.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@RequiredArgsConstructor
public class TokenAuthSSOFilter extends OncePerRequestFilter {

	// 로그
	private static final Logger log = LogManager.getLogger(TokenAuthSSOFilter.class);

	@Value("${sso.auth}")
	private String authServer;
	
	/**
	 * URI 체크
	 * 
	 * @param requestUri
	 * @return
	 */
	private boolean isWhitelisted(String requestUri) {
		AuthWhiteList authWhiteList = new AuthWhiteList();
		return authWhiteList.getAuthWhiteList().stream().anyMatch(urlPattern -> pathMatches(urlPattern, requestUri));
	}

	private boolean isAdmin(String requestUri) {
		AuthWhiteList adminList = new AuthWhiteList();
		return adminList.getAdminList().stream().anyMatch(urlPattern -> pathMatches(urlPattern, requestUri));
	}

	/**
	 * URI 패턴 매칭
	 * 
	 * @param pattern
	 * @param path
	 * @return
	 */
	private boolean pathMatches(String pattern, String path) {
		if (pattern.equals(path)) {
			return true;
		}
		if (pattern.endsWith("/**")) {
			String patternPrefix = pattern.substring(0, pattern.length() - 3);
			return path.startsWith(patternPrefix);
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.setHeader("X-Content-Type-Options", "nosniff");
		String requestUri = request.getRequestURI();

		// restTemplate을 사용하여 API 호출
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setErrorHandler(new TokenResponseHandler());
		String reqUrl = "";
		String userId = "";

		if (isWhitelisted(requestUri) && request.getHeader("Authorization") == null) {
			log.info("[doFilterInternal] AUTH_WHITE : " + requestUri);
		} else {
			// TODO api 경로별 토큰 url 맵핑하기 // AdminServer가 분리 되어 있다고 한다면...
			if (isAdmin(requestUri)) {
				reqUrl = authServer + "/igns/auth/admin";
			} else {
				reqUrl = authServer + "/igns/auth/jwt-test";
			}

//			URI uri = URI.create(reqUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", request.getHeader("Authorization"));
			// create param
			JsonObject jsonObject = new JsonObject();
			HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
			ResponseEntity<String> apiResponse = restTemplate.exchange(reqUrl, HttpMethod.GET, entity, String.class);

//			log.info("[doFilterInternal] TOKEN_VAILDATION : " + requestUri);

			if (apiResponse.getStatusCodeValue() == HttpStatus.SC_UNAUTHORIZED) {
				response.setStatus(HttpStatus.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				response.getWriter().write("{\"code\":\"INVALID_TOKEN\"}");
				return;
			}

			if (apiResponse.getStatusCodeValue() != HttpStatus.SC_OK) {
				response.setStatus(HttpStatus.SC_FORBIDDEN);
				response.setContentType("application/json");
				response.getWriter().write("{\"code\":\"NOAUTH_TOKEN\"}");
				return;
			} else {
				userId = apiResponse.getBody();
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println(userId );
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
//				request.setAttribute("userId", userId);  // 사용하지 않는다. limhs11

			}

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, null);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}

}
