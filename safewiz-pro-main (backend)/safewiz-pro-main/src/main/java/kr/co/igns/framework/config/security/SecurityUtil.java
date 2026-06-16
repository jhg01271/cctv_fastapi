package kr.co.igns.framework.config.security;

import kr.co.igns.framework.utils.etc.Aes256;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.igns.framework.utils.file.CommonFileUtils;
import kr.co.igns.system.user.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.security
 * @ 파일명		: SecurityUtil.java
 * @ 기능명 		:
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 * <p>
 * getCurrentMemberId 반환형 Long에서 String으로 변경 - 임현섭
 */

public class SecurityUtil {

    private SecurityUtil() {
    }

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    private static final Logger log = LogManager.getLogger(SecurityUtil.class);

    public static String getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        log.info("getCurrentMemberId : " + authentication.getName());
        return authentication.getName();
    }

    public static List<String> getCurrentMemberGroup() {
        log.info("getCurrentMemberGroup ");
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return authentication.getAuthorities().stream()
                .map(el -> Role.fromKey(el.getAuthority()).name())
                .collect(Collectors.toList());

    }

    public static String getCurrentClntId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clntId = "";
        if (authentication instanceof CustomAuthenticationToken) {
            CustomAuthenticationToken customAuth = (CustomAuthenticationToken) authentication;

            clntId = (String) customAuth.getAdditionalInfoBykey("clntId");
        }
        return clntId;
    }

    public static String getCurrentHrId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String hrId = "";
        if (authentication instanceof CustomAuthenticationToken) {
            CustomAuthenticationToken customAuth = (CustomAuthenticationToken) authentication;
            hrId = (String) customAuth.getAdditionalInfoBykey("hrId");
        }
        return hrId;

    }

    public static String getCurrentRole() {
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority(); // "ROLE_USER" 같은 값

        return role.replaceFirst("ROLE_", "");

    }

    // RequestContextHolder를 사용하여 HttpServletRequest에서 쿠키 값을 가져오기
    public static String getCurrentCompId() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            throw new RuntimeException("Request 객체가 없습니다.");
        }
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + request.getHeader(headerName) +"\n");
//        }
        String compId = request.getHeader("COMPID");
        if ("".equals(compId)) {
            return null;
        }
        return Aes256.decryptString(compId);
        // 쿠키 배열에서 comp_id 찾기
//        Cookie[] cookies = request.getCookies();
//        request.getCookies();
//        String header = request.getHeader("comp-id");
//        if(header != null && !header.equals("")){
//            return Aes256.decryptString(header);
//        }
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("COMP_ID".equals(cookie.getName())) {
//                    return Aes256.decryptString(cookie.getValue());  // comp_id 쿠키 값 반환
//                }
//            }
//        }
//        return null;  // comp_id 쿠키가 없는 경우 null 반환
    }

    // RequestContextHolder에서 현재 요청을 가져오는 헬퍼 메서드
    private static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
