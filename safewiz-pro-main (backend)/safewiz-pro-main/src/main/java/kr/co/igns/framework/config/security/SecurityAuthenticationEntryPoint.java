package kr.co.igns.framework.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.security
 * @ 파일명		: SecurityAuthenticationEntryPoint.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Component
@Log4j2
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 SC_UNAUTHORIZED (401)  
		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		log.info(" =============== AuthenticationEntryPoint =============== ");
        response.sendRedirect("/exception/entrypoint");
    }
}
