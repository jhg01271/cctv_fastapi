package kr.co.igns.system.user.utils.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 스프링 시큐리티의 폼 기반의 UsernamePasswordAuthenticationFilter를 참고하여 만든 커스텀 필터 거의 구조가
 * 같고, Type이 Json인 Login만 처리하도록 설정한 부분만 다르다. (커스텀 API용 필터 구현) Username : 회원 아이디
 * -> email로 설정 "/login" 요청 왔을 때 JSON 값을 매핑 처리하는 필터
 */
/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.utils.filter
 * ※ 파일명 : CustomJsonUsernamePasswordAuthenticationFilter.java
 * ※ 기능명 : 로그인 처리 필터
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 16.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_LOGIN_REQUEST_URL = "/base/signin"; // "/igns/auth/login"으로 오는 요청을 처리
	private static final String HTTP_METHOD = "POST"; 							// 로그인 HTTP 메소드는 POST
	private static final String CONTENT_TYPE = "application/json"; 				// JSON 타입의 데이터로 오는 로그인 요청만 처리
	private static final String USERNAME_KEY = "userId"; 							// 회원 로그인 시 Id 요청 JSON Key : "id"
	private static final String PASSWORD_KEY = "userPs"; 						// 회원 로그인 시 비밀번호 요청 JSon Key : "password"
	private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
			DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); 							// "/igns/auth/login" + POST로 온 요청에 매칭된다.

	private final UserDAO userDao;
	private final ObjectMapper objectMapper;

	public LoginAuthenticationFilter(UserDAO userDao, ObjectMapper objectMapper) {
		super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); 								// 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
		this.userDao = userDao;
		this.objectMapper = objectMapper;
	}

	/**
	 * 인증 처리 메소드
	 *
	 * UsernamePasswordAuthenticationFilter와 동일하게
	 * UsernamePasswordAuthenticationToken 사용 StreamUtils를 통해 request에서
	 * messageBody(JSON) 반환 요청 JSON Example { "userId" : "admin" "userPs" :
	 * "test123" } 꺼낸 messageBody를 objectMapper.readValue()로 Map으로 변환 (Key : JSON의 키
	 * -> id, password) Map의 Key(id, password)로 해당 아이디, 패스워드 추출 후
	 * UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
	 *
	 * AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로
	 * AuthenticationManager 객체를 반환 받은 후 authenticate()의 파라미터로
	 * UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리 (여기서 AuthenticationManager
	 * 객체는 ProviderManager -> SecurityConfig에서 설정)
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException(
					"Authentication Content-Type not supported: " + request.getContentType());
		}

		String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);


		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
		Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, typeRef);

		String id = usernamePasswordMap.get(USERNAME_KEY);

		// TODO 비밀번호 복호화
		String password = new String(Base64.getDecoder().decode(usernamePasswordMap.get(PASSWORD_KEY)));
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(id, password);// principal
		return this.getAuthenticationManager().authenticate(authRequest);
	}


}
