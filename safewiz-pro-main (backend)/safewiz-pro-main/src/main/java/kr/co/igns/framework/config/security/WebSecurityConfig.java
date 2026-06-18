package kr.co.igns.framework.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.utils.filter.LoginAuthenticationFilter;
import kr.co.igns.system.user.utils.filter.TokenAuthInternalFilter;
import kr.co.igns.system.user.utils.filter.TokenAuthSSOFilter;
import kr.co.igns.system.user.utils.handler.LoginFailureHandler;
import kr.co.igns.system.user.utils.handler.LoginSuccessHandler;
import kr.co.igns.system.user.service.LoginService;
import kr.co.igns.system.user.service.TokenService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

/**
 * 
 * @ 프로젝트 : igns @ 패키지 : com.web.framework.igns.config.security @ 파일명 :
 * WebSecurityConfig.java @ 기능명 : 스프링 시큐리티 설정 @ 작성자 : 김성준 @ 최초생성일 : 2020. 9. 10.
 */

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

	@Value("${sso.enabled}")
	private boolean isSsoEnabled;
	@Value("${jwt.refresh.header}")
	private String refreshHeader;
	public static final String ROLE = "ADMIN";
	private final JwtService jwtService;
	private final ObjectMapper objectMapper;
	private final LoginService loginService;
	private final TokenService tokenService;
	private final UserDAO userDao;
	private final MessageService messageService;
	
	@Autowired
	private AuthWhiteList authWhiteList = new AuthWhiteList();;

	/**
	 * 시큐리티 Bean 등록
	 * @param http
	 * @return
	 * @throws Exception
	 */
    @Bean
    protected SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    	http.formLogin().disable();															 // FormLogin 사용 X
		http.httpBasic().disable();															// rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트
        http.csrf().disable();																// rest api 이므로 csrf 보안이 필요없음
        http
        	.headers()
//		        .defaultsDisabled().cacheControl().and()
        	.contentTypeOptions().and()
        	.contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;").and()
        	.xssProtection().and()
        	.frameOptions().sameOrigin();	// 동일도메인에서는 iframe 접근이 가능하도록 설정

        http
        	.exceptionHandling()
        	.accessDeniedHandler(new SecurityAccessDeniedHandler())
        	.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());

        http
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	// JWT TOKEN 으로 인증할것이므로 세션이 필요없음
        http
        	.authorizeHttpRequests()
				.antMatchers(HttpMethod.TRACE, "/**").denyAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").denyAll()
				.antMatchers(HttpMethod.PUT, "/igns/auth/changePassword", "/igns/auth/user/recommendCode").hasAnyAuthority("ROLE_USER" , "ROLE_ADMIN", "ROLE_ASSET")
				.antMatchers(HttpMethod.PATCH, "/igns/auth/user", "/igns/auth/changeEmail", "/igns/auth/changeRole").hasAnyAuthority("ROLE_USER" , "ROLE_ADMIN", "ROLE_ASSET")
				.antMatchers(HttpMethod.DELETE, "/igns/auth/user", "/igns/auth/userDelete").hasAnyAuthority("ROLE_ADMIN", "ROLE_ASSET")
				.antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll() // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
				.antMatchers("/igns/auth/admin").hasAnyAuthority("ROLE_ADMIN", "ROLE_ASSET")
				.antMatchers("/igns/auth/jwt-test","/igns/auth/extractToken").hasAnyAuthority("ROLE_USER" , "ROLE_ADMIN", "ROLE_ASSET")
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/base/signin").permitAll()
				.antMatchers("/igns/auth/sign-up", "/igns/auth/verification/**", "/igns/auth/password").permitAll()
				.antMatchers(HttpMethod.GET, "/exception/**").permitAll() 						// 등록된 GET요청 리소스는 누구나 접근가능
				.antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//				.antMatchers("/safewizpro/getComp").permitAll() // 청주 산단용
//				.antMatchers("/safewizpro/getDashboard/**").permitAll() // 청주 산단용
//				.antMatchers("/safewizpro/dashboard/**").permitAll()
				.antMatchers("/igns/auth/refresh").permitAll() // 리플레시 토큰을 허용하는 설정
				.anyRequest().authenticated();													// 나머지 요청에 대해 인증 후 접근이 가능하도록 함
		http
			.logout()
			.logoutUrl("/igns/auth/logout") //logout url 설정
			.deleteCookies("JSESSIONID" , refreshHeader , "remember-me")
				.addLogoutHandler(new LogoutHandler() { //logout이 성공했을 때 처리할 내용
					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
						String refreshToken = "";
						if (request.getCookies() != null) {
							refreshToken = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(refreshHeader)).findFirst().map(Cookie::getValue).orElse(null);
						}
						tokenService.removeByRefreshToken(refreshToken);
						HttpSession session = request.getSession();
						session.invalidate();
					}
				})
				.logoutSuccessHandler(new LogoutSuccessHandler() { //logout 시 이동할 url 및 더 많은 로직 구현 가능
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
						response.setStatus(HttpServletResponse.SC_OK);
					}
				});
		if (isSsoEnabled) {
        http
        	.addFilterBefore(JwtFilter(), UsernamePasswordAuthenticationFilter.class);	// JwtFilter를 수행
		} else {
			http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
			http.addFilterBefore(jwtAuthenticationProcessingFilter(), LoginAuthenticationFilter.class);
		}

        return http.build();
    }

	/**
	 * 암호화
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}
	@Bean
	@ConditionalOnProperty(name = "sso.enabled", havingValue = "true")
    public TokenAuthSSOFilter JwtFilter() {
		TokenAuthSSOFilter jwtFilter = new TokenAuthSSOFilter();
        return jwtFilter;
    }
	/**
	 * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
	 */
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(jwtService, userDao,messageService,loginService);
	}

	/**
	 * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
	 */
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler(loginService);
	}


	/**
	 * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
	 * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
	 * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
	 * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
	 */
	@Bean
	public LoginAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
		LoginAuthenticationFilter customJsonUsernamePasswordLoginFilter
				= new LoginAuthenticationFilter(userDao, objectMapper);
		customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
		customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
		customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
		return customJsonUsernamePasswordLoginFilter;
	}

	@Bean
	public TokenAuthInternalFilter jwtAuthenticationProcessingFilter() {
		TokenAuthInternalFilter jwtAuthenticationFilter = new TokenAuthInternalFilter(jwtService, tokenService, userDao);
		return jwtAuthenticationFilter;
	}
}
