package kr.co.igns.system.user.utils.filter;

import kr.co.igns.framework.config.exception.CAuthClaimNotFoundException;
import kr.co.igns.framework.config.exception.CUserNotFoundException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.CustomAuthenticationToken;
import kr.co.igns.framework.config.security.jwt.dto.TokenResponseDto;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.framework.config.security.jwt.util.PasswordUtil;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Jwt 인증 필터 "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 * <p>
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청 AccessToken 만료 시에만 RefreshToken을 요청 헤더에
 * AccessToken과 함께 요청
 * <p>
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는
 * 않는다. 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급,
 * RefreshToken 재발급(RTR 방식) 인증 성공 처리는 하지 않고 실패 처리
 *
 */

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.framework.config.security.jwt.filter
 * ※ 파일명 : JwtAuthenticationProcessingFilter.java
 * ※ 기능명 : 기본 토큰 검증 필터
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 16.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@RequiredArgsConstructor
public class TokenAuthInternalFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserDAO userDao;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @Value("${jwt.api.header}")
    private String API_HEADER;

    /**
     * URI 체크
     *
     * @param requestUri
     * @return
     */
    private boolean isWhitelisted(String requestUri) {
        JwtWhiteList jwtWhiteList = new JwtWhiteList();
        return jwtWhiteList.getAuthWhiteList().stream().anyMatch(urlPattern -> pathMatches(urlPattern, requestUri));
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        if (isWhitelisted(request.getRequestURI())) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }
        // 쿠키에서 RefreshToken 추출
        String refreshToken = "";
        if (request.getCookies() != null) {
            refreshToken = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(refreshHeader)).findFirst().map(Cookie::getValue).orElse(null);
        }

        if (refreshToken != null && request.getRequestURI().equals("/igns/auth/refresh")) {
            // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행
            checkRefreshTokenAndReIssueAccessToken(request, response, refreshToken);
            return;
        }

        // 외부에서 API 요청 할 때 (청주산단)
        String apiToken = request.getHeader(API_HEADER);
        if (apiToken != null) {
            // API 토큰으로 요청이 들어올 시 인증 처리
            checkApiTokenAndAuthentication(request, response, filterChain);
            return;
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        checkAccessTokenAndAuthentication(request, response, filterChain);
        return;

    }

    /**
     * [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드] 파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로
     * DB에서 유저를 찾고, 해당 유저가 있다면 JwtService.createAccessToken()으로 AccessToken 생성,
     * reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출 그 후
     * JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        JSONObject jsonResponse = new JSONObject();
        jwtService.extractUid(refreshToken).ifPresentOrElse(uid -> {
            String enable = jwtService.isEnabledRefreshToken(uid, refreshToken);
            if ("ok".equals(enable)) {
                LogUtil.ConsoleLogInfo(HttpStatus.OK, "[TOKEN_DEBUG]", "[요청 REFRESH : " + refreshToken + "]:::[REDIS UID : " + uid + "]", request);
                UserVO user = userDao.findByUid(uid);
                if (user != null) {
                    LogUtil.ConsoleLogInfo(HttpStatus.OK, "[TOKEN_DEBUG]", "[조회 ID : " + user.getUserId() + "]", request);

                    String reIssuedRefreshToken = jwtService.reIssueRefreshToken(user, refreshToken);
                    String newAccessToken = jwtService.createAccessToken(user);
                    jwtService.sendAccessAndRefreshToken(response, newAccessToken, reIssuedRefreshToken);

                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    try {
                        jsonResponse.put("success", true);
                        jsonResponse.put("code", 200);
                        jsonResponse.put("msg", "SUCCESS REISSUE ACCESS TOKEN");
                        jsonResponse.put("accessToken", newAccessToken);
                        PrintWriter out = response.getWriter();
                        out.print(jsonResponse.toString());
                        out.flush();
                        LogUtil.ConsoleLogInfo(HttpStatus.OK, "SUCCESS REISSUE ACCESS TOKEN", user.getUserId(), request);
                    } catch (IOException e) {
                        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "REISSUE ACCESS TOKEN", e.getMessage(), request);
                    }
                } else {
                    LogUtil.ConsoleLogError(HttpStatus.FORBIDDEN, "REISSUE ACCESS TOKEN", "USER NOT FOUND", request);
                }
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    LogUtil.ConsoleLogError(HttpStatus.FORBIDDEN, "REISSUE ACCESS TOKEN", "TOKEN DISABLE", request);
                    jsonResponse.put("success", false);
                    jsonResponse.put("code", 403);
                    jsonResponse.put("msg", "TOKEN DISABLE");
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                } catch (IOException e) {
                    LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "REISSUE ACCESS TOKEN", e.getMessage(), request);
                }
            }
        }, () -> {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "FAILED REISSUE ACCESS TOKEN", "REFRESH TOKEN EXPIRED", request);
                jsonResponse.put("success", false);
                jsonResponse.put("code", 400);
                jsonResponse.put("msg", "FAILED REISSUE ACCESS TOKEN");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                ResponseCookie cookie = ResponseCookie.from(refreshHeader, refreshToken).maxAge(1).path("/").secure(false).httpOnly(true).build();
                response.setHeader("Set-Cookie", cookie.toString());
                out.flush();
            } catch (IOException e) {
                LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "REISSUE ACCESS TOKEN", e.getMessage(), request);
            }
        });

    }

    /**
     * [API 토큰 체크 & 인증 처리 메소드] request에서 extractApiToken()으로 액세스 토큰 추출 후,
     * isApiTokenValid()로 유효한 토큰인지 검증 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후
     * findByEmail()로 해당 이메일을 사용하는 유저 객체 반환 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * checkWhiteList()로 화이트리스트에 포함된 토큰인지 확인
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기 그 후 다음 인증 필터로 진행
     */
    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    public void checkApiTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            token = jwtService.extractApiToken(request);
        } catch (ServletException e) {
            // 헤더가 없을 때
            sendResponse(request, response, HttpStatus.UNAUTHORIZED, false, 401, e.getMessage());
            return;
        }
        // #1 토큰 유무 확인
        if (token.isEmpty()) {
            sendResponse(request, response, HttpStatus.BAD_REQUEST, false, 400, "NOT_EXTRACT_ACCESS_TOKEN");
            return;
        }
        // #2 토큰 유효 여부 확인
        String check = jwtService.isApiTokenValid(token);
        if (!"ok".equals(check)) {
            sendResponse(request, response, HttpStatus.UNAUTHORIZED, false, 401, "INVALID_TOKEN");
            return;
        }
        // #3 토큰 화이트리스트 포함 여부 확인
        if (tokenService.checkWhiteList(token) == 0) {
            sendResponse(request, response, HttpStatus.BAD_REQUEST, false, 403, "Forbidden");
            return;
        }

        TokenResponseDto responseDto = jwtService.extractApiClaim(token);
        if (responseDto == null) {
            throw new CUserNotFoundException();
        }

        UserVO user = new UserVO();
        user.setUserId(responseDto.getUserId());
        user.setRole(responseDto.getRole());
        saveAuthentication(user);

        filterChain.doFilter(request, response);
    }


    /**
     * [액세스 토큰 체크 & 인증 처리 메소드] request에서 extractAccessToken()으로 액세스 토큰 추출 후,
     * isTokenValid()로 유효한 토큰인지 검증 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후
     * findByEmail()로 해당 이메일을 사용하는 유저 객체 반환 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기 그 후 다음 인증 필터로 진행
     */
    /**
     * issue FIXME /limhs11
     * 1. token clime 정보가 암호화 되어 있지 않았다.
     * 2. 매번 DB 정보를 호출 한다.
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String etxAT = jwtService.extractAccessToken(request);;
        try {
            etxAT = jwtService.extractAccessToken(request);
        } catch (ServletException e) {
            // 헤더가 없을 때
            sendResponse(request, response, HttpStatus.UNAUTHORIZED, false, 401, e.getMessage());
            return;
        }
        if (!etxAT.isEmpty()) {
            String token = etxAT;
            String check = jwtService.isTokenValid(token);
            if (check.equals("ok")) {
                if (jwtService.extractUid(token).isPresent()) {
                    String uid = jwtService.extractUid(token).get();
                    UserVO user = userDao.findByUid(uid);
                    if (user != null) {
//						request.setAttribute("userId", user.getUserId());// 사용하지 않는다. String userId = SecurityUtil.getCurrentMemberId() 사용 limhs11
                        saveAuthentication(user);
                    } else {
                        throw new CUserNotFoundException();
                    }
                } else {
                    throw new CAuthClaimNotFoundException();
                }
            } else {
                JSONObject jsonResponse = new JSONObject();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                jsonResponse.put("success", false);
                jsonResponse.put("code", 401);
                jsonResponse.put("msg", check);
                LogUtil.ConsoleLogError(HttpStatus.UNAUTHORIZED, "TOKEN_CHECK", "INVALID_TOKEN", request);
                return;
            }
        } else {
            JSONObject jsonResponse = new JSONObject();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            jsonResponse.put("success", false);
            jsonResponse.put("code", 400);
            jsonResponse.put("msg", "INVALID_TOKEN");
            LogUtil.ConsoleLogError(HttpStatus.EXPECTATION_FAILED, "TOKEN_CHECK", "NOT_EXTRACT_ACCESS_TOKEN", request);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * [인증 허가 메소드] 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터 1. 위에서 만든 UserDetailsUser 객체 (유저
     * 정보) 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거) 3. Collection < ? extends
     * GrantedAuthority>로, UserDetails의 User 객체 안에 Set<GrantedAuthority>
     * authorities이 있어서 getter로 호출한 후에, new NullAuthoritiesMapper()로
     * GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    @Deprecated
    public void saveAuthentication(UserVO myUser) {
        String password = myUser.getUserPs();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }
        List<Map<String, Object>> getAdditionalInfo = new ArrayList<>();

        // clnt_id
        Map<String, Object> clntId = new HashMap<>();
        clntId.put("key", "clntId");
        clntId.put("value", myUser.getClntId() != null ? myUser.getClntId():"");

        // hr_id
        Map<String, Object> hrId = new HashMap<>();
        hrId.put("key", "hrId");
        hrId.put("value", myUser.getHrId() != null ?  myUser.getHrId() : "");

        getAdditionalInfo.add(hrId);
        getAdditionalInfo.add(clntId);
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder().username(myUser.getUserId()).password(password).roles(myUser.getRole()).build();
        Authentication authentication = new CustomAuthenticationToken(userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()), getAdditionalInfo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * [json response 공통 메서드]
     * @param request
     * @param response
     * @param status
     * @param success
     * @param code
     * @param message
     * @throws IOException
     */
    private void sendResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, boolean success, int code, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("code", code);
        jsonResponse.put("msg", message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();

        if (success) {
            LogUtil.ConsoleLogInfo(status, "TOKEN_CHECK", message, request);
        } else {
            LogUtil.ConsoleLogError(status, "TOKEN_CHECK", message, request);
        }
    }
}
