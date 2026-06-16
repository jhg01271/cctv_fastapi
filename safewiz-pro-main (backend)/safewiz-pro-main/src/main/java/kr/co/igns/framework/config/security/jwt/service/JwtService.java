package kr.co.igns.framework.config.security.jwt.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.utils.etc.Aes256;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import kr.co.igns.system.user.model.TokenVO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.service.TokenService;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.jwt.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenService tokenService;

    @Value("${sso.auth}")
    private String auth;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.secretKey.api}")
    private String SECRET_KEY_API;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Value("${jwt.api.header}")
    private String API_HEADER;
    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정 JWT의 헤더에 들어오는 값 :
     * 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */

    private static final String ACCESS_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "uid";
    private static final String USER_CLAIM = "user";
    private static final String ROLE_CLAIM = "role";

    /**
     * request 에서 토큰 String 추출
     *
     * @param request
     * @return
     */
    public String extractAccessToken(HttpServletRequest request) throws ServletException {
        Optional<String> act = Optional.ofNullable(request.getHeader(ACCESS_HEADER))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
        if (act.isEmpty()) {
            // 헤더가 없을 때
            throw new ServletException("Missing Authorization header");
        }
        return act.get();
    }


    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(UserVO user) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

                // 클레임으로는 저희는 id 하나만 사용합니다.
                // 추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
                // 추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
                .withClaim(ID_CLAIM, user.getUid())
                .withClaim(ROLE_CLAIM, user.getRoleName())
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용,
        // application-jwt.yml에서
        // 지정한 secret
        // 키로 암호화
    }

    /**
     * AccessToken 생성 메소드
     */
    public String createCustomToken(String claimNm, String claim, Long period) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + period)) // 토큰 만료 시간 설정
                .withClaim(claimNm, claim)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용
    }

    /**
     * RefreshToken 생성 RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken(UserVO user) {
        Date now = new Date();
        Date expirationPeriod = new Date(now.getTime() + refreshTokenExpirationPeriod);
        String token = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)//
                .withClaim(ID_CLAIM, user.getUid())//
                .withClaim(ROLE_CLAIM, user.getRoleName())//
                .withExpiresAt(expirationPeriod)//
                .sign(Algorithm.HMAC512(secretKey));

        // 원하는 형식으로 날짜를 포맷하기 위한 SimpleDateFormat 인스턴스 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // Date 객체를 문자열로 변환
        String formattedDate = sdf.format(expirationPeriod);

        //db에 저장
        TokenVO tokenVo = TokenVO.builder()
                .uid(user.getUid())
                .refreshToken(token)
                .expirationPeriod(formattedDate)
                .ip(user.getIp()).build();
        tokenService.remove(tokenVo);
        tokenService.create(tokenVo);

        return token;
    }


    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenCookie(response, accessToken);
        setRefreshTokenCookie(response, refreshToken);
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        // 쿠키
        ResponseCookie cookie = ResponseCookie.from(accessHeader, accessToken).maxAge(accessTokenExpirationPeriod)
                .path("/").secure(false).httpOnly(false).build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * RefreshToken 쿠키 설정
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        // 쿠키
        ResponseCookie cookie = ResponseCookie.from(refreshHeader, refreshToken).maxAge(refreshTokenExpirationPeriod)
                .path("/").secure(false).httpOnly(true).build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * token에서 uid 추출 전에 JWT.require()로 검증기 생성 verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 uid 추출 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractUid(String token) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey)).build() // 반환된 빌더로 JWT verifier 생성
                    .verify(token) // token을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(ID_CLAIM) // claim(uid) 가져오기
                    .asString());
        } catch (Exception e) {
            LogUtil.ConsoleLogError(org.springframework.http.HttpStatus.UNAUTHORIZED, "INVALID ACCESS TOKEN", e.getMessage(), null);
            return Optional.empty();
        }
    }

    /**
     * token에서 claim 추출 전에 JWT.require()로 검증기 생성 verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 claim 추출 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractCustomClaim(String token, String claimNm) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey)).build() // 반환된 빌더로 JWT verifier 생성
                    .verify(token) // token을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(claimNm) // claim 가져오기
                    .asString());
        } catch (Exception e) {
            LogUtil.ConsoleLogError(org.springframework.http.HttpStatus.UNAUTHORIZED, "INVALID ACCESS TOKEN", e.getMessage(), null);
            return Optional.empty();
        }
    }


    //limhs11 list return에서 dto로 바꿈 
    public TokenResponseDto extractClaimWithAccessToken(String act) {
        TokenResponseDto dto = new TokenResponseDto();
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(act); // accessToken을 검증하고 유효하지 않다면 예외 발생
        dto.setUid(jwt.getClaim(ID_CLAIM).asString());
        dto.setRole(jwt.getClaim(ROLE_CLAIM).asString());
        return dto;
    }

    /**
     * RTR 기법 [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드] jwtService.createRefreshToken()으로
     * 리프레시 토큰 재발급 후 DB에 재발급한 리프레시 토큰 업데이트 후 Flush
     */
    public String reIssueRefreshToken(UserVO user, String refreshToken) {
        String reIssuedRefreshToken = createRefreshToken(user);


        Date now = new Date();
        Date expirationPeriod = new Date(now.getTime() + refreshTokenExpirationPeriod);
        // 원하는 형식으로 날짜를 포맷하기 위한 SimpleDateFormat 인스턴스 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // Date 객체를 문자열로 변환
        String formattedDate = sdf.format(expirationPeriod);

        // 동시 api 요청시 403 반환하는 코드 관련 임시 주석
        //tokenService.removeByRefreshToken(refreshToken);

        LogUtil.ConsoleLogInfo(org.springframework.http.HttpStatus.OK, "[TOKEN_DEBUG]", "[저장 UID : " + user.getUid() + "]:::[저장 ID : " + user.getUserId() + "]:::[REFRESH_TOKEN : " + reIssuedRefreshToken + "]", null);
        return reIssuedRefreshToken;
    }

    /**
     * Token 유효성 체크
     */
    public String isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);

            // 만료일 체크
            DecodedJWT decodedJWT = JWT.decode(token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
                throw new JWTVerificationException("The Token has expired on " + decodedJWT.getExpiresAt());
            }

            return "ok";
        } catch (JWTVerificationException e) {
            return "not ok";
        } catch (Exception e) {
            throw e;
        }
    }

    public String isApiTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY_API)).build().verify(token);

            // 만료일 체크
            DecodedJWT decodedJWT = JWT.decode(token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
                throw new JWTVerificationException("The Token has expired on " + decodedJWT.getExpiresAt());
            }

            return "ok";
        } catch (JWTVerificationException e) {
            return "not ok";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * refreshToken 유효 체크
     */
    public String isEnabledRefreshToken(String uid, String refreshToken) {

        this.isTokenValid(refreshToken);

        TokenVO token = tokenService.findOne(uid, refreshToken);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date expirationDate = null;
        try {
            expirationDate = sdf.parse(token.getExpirationPeriod());
        } catch (ParseException e) {
            e.printStackTrace();
            return "not ok";
        }
        // 현재 날짜와 시간
        Date now = new Date();

        // 날짜 비교
        boolean isExpired = now.before(expirationDate);

        if (token != null && "N".equals(token.getDelYn()) && isExpired) {

            return "ok";
        } else {
            tokenService.remove(token);
            return "not ok";
        }
    }

    /**
     * API AccessToken 생성 메소드
     */
    public String createTokenByUserAndExpiration(UserVO user, Date date) throws ParseException {
        // 공통 클레임 세팅
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(date) // 토큰 만료 시간 설정

                // 클레임으로는 저희는 id 하나만 사용합니다.
                // 추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
                // 추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
                .withClaim(ID_CLAIM, user.getUid())
                .withClaim(USER_CLAIM, Aes256.encryptString(user.getUserId()))
                .withClaim(ROLE_CLAIM, Aes256.encryptString(user.getRoleName()))
                .sign(Algorithm.HMAC512(SECRET_KEY_API)); // HMAC512 알고리즘 사용,
    }

    /**
     * request 에서 토큰 String 추출
     *
     * @param request
     * @return
     */
    public String extractApiToken(HttpServletRequest request) throws ServletException {
        Optional<String> act = Optional.ofNullable(request.getHeader(API_HEADER))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
        if (act.isEmpty()) {
            // 헤더가 없을 때
            throw new ServletException("Missing Authorization-API header");
        }
        return act.get();
    }

    /**
     * token에서 uid 추출 전에 JWT.require()로 검증기 생성 verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 uid, role 추출 유효하지 않다면 빈 Optional 객체 반환
     */
    public TokenResponseDto extractApiClaim(String token) {
        TokenResponseDto dto = new TokenResponseDto();
        try {
            Optional<DecodedJWT> jwt = Optional.ofNullable(JWT.require(Algorithm.HMAC512(SECRET_KEY_API)).build().verify(token));
            String claimId = jwt.get().getClaim(ID_CLAIM).asString();
            String claimUser = Aes256.decryptString(jwt.get().getClaim(USER_CLAIM).asString());
            String claimRole = Aes256.decryptString(jwt.get().getClaim(ROLE_CLAIM).asString());
            dto.setUid(claimId);
            dto.setUserId(claimUser);
            dto.setRole(claimRole);
        } catch (Exception e) {
            LogUtil.ConsoleLogError(org.springframework.http.HttpStatus.UNAUTHORIZED, "INVALID ACCESS TOKEN", e.getMessage(), null);
        }
        return dto;
    }

}
