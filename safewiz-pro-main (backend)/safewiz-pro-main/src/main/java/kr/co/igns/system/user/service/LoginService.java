package kr.co.igns.system.user.service;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserDAO userDao;
    private String loginUserId;

    @Override
    public UserDetails loadUserByUsername(String userId) throws AuthenticationException {
        //TODO : 여기서 권한 인증 처리 필요
        loginUserId = userId;
        UserVO user = userDao.findOne(userId);
        boolean statusFlag = user != null ? false : true;
        if (user == null) {
            new UsernameNotFoundException("해당 ID가 존재하지 않습니다.");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getUserPs())
                .roles(user.getRole())
                .disabled(statusFlag)
                .build();
    }

    /**
     * 로그인 이력 저장 (LoginSuccessHandler, LoginFailHandler)
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveLoginHistory(HttpServletRequest request, String loginSuccessYn) throws Exception {
        // ip
        String ip = getClientIp(request);

        // User-Agent 확인
        String userAgent = request.getHeader("User-Agent");

        LoginHistoryVO vo = new LoginHistoryVO();
        vo.setUserId(loginUserId);
        vo.setLoginIp(ip);
        vo.setLoginDevice(isMobileDevice(userAgent));
        vo.setLoginSuccessYn(loginSuccessYn);

        //로그인 이력 저장
        userDao.saveLoginHistory(vo);
        if("Y".equals(loginSuccessYn)) {
            userDao.updateLoginUserInfo(vo); // 로그인 정보 업데이트
        }else {
            userDao.increaseLoginFailCnt(vo); // 로그인 실패 시 - 로그인 실패 횟수 증가
        }
    }

    /**
     * 클라이언트 IP 주소 가져오기
     */
    public String getClientIp(HttpServletRequest request) {

        // IP문제 해결하지 못하여 우선적으로 로그인 시 클라이언트에서 헤더에 IP를 실어서 보내도록 수정함. (2025.09.05 LJH)
        String ip = request.getHeader("X-Real-Client-IP");


        // IP문제 해결하지 못하여 우선적으로 로그인 시 클라이언트에서 헤더에 IP를 실어서 보내도록 수정함. (2025.09.05 LJH)
//        String ip = request.getHeader("X-Forwarded-For");
//        if(ip != null) {
//            String[] tempIp = ip.replace(" ", "").split(","); // 프록시 서버 분리 (client ip, proxy1 ip, proxy2 ip, ...)
//            ip = tempIp[0];
//        }
        System.out.println("### ######################################################### ###");
        System.out.println("###                       IP INFORMATION                      ###");
        System.out.println("### ######################################################### ###");
        System.out.println("###                                                           ###");
        System.out.println("### X-Forwarded-For ==> " + request.getHeader("X-Forwarded-For"));
        System.out.println("### X-Real-Client-IP ==> " + request.getHeader("X-Real-Client-IP"));
        System.out.println("### Proxy-Client-IP ==> " + request.getHeader("Proxy-Client-IP"));
        System.out.println("### WL-Proxy-Client-IP ==> " + request.getHeader("WL-Proxy-Client-IP"));
        System.out.println("### HTTP_CLIENT_IP ==> " + request.getHeader("HTTP_CLIENT_IP"));
        System.out.println("### HTTP_X_FORWARDED_FOR ==> " + request.getHeader("HTTP_X_FORWARDED_FOR"));
        System.out.println("### request.getRemoteAddr() ==> " + request.getRemoteAddr());
        System.out.println("###                                                           ###");
        System.out.println("### ######################################################### ###");


        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        //다 공인 ip 못들고옴, nginx.config 수정하고 application.yml -forward-headers-strategy: framework도 추가해봄....
//        String clientIp = request.getHeader("X-Real-IP");
//        if (clientIp == null || clientIp.isEmpty()) {
//            clientIp = request.getHeader("X-Forwarded-For");
//        }
//        if (clientIp == null || clientIp.isEmpty()) {
//            clientIp = request.getRemoteAddr();
//        }
//        String ip = request.getRemoteAddr();
        // IPv6 localhost 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * User-Agent를 기반으로 모바일 장치인지 확인
     */
    public String isMobileDevice(String userAgentString) {
        if (userAgentString == null) return "";

        // UserAgent 객체를 생성하여 User-Agent 파싱
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // 장치 타입 확인 (모바일, 데스크탑, 태블릿 등)
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        String osName = userAgent.getOperatingSystem().getName().toLowerCase();  // OS 이름 소문자 변환

        //dart는 따로 조건으로 처리
        if (deviceType == DeviceType.MOBILE || userAgentString.contains("Dart")) {
            if (osName.contains("android")) {
                return "android";
            } else if (osName.contains("ios") || osName.contains("iphone") || osName.contains("mac os x")) {
                // iOS는 일부 브라우저에서 Mac OS X로 나올 수도 있음
                return "ios";
            }
            return "mobile";
        } else if (deviceType == DeviceType.TABLET) {
            return "tablet";
        } else if (deviceType == DeviceType.COMPUTER) {
            return "web";
        } else {
            return "other";
        }
    }
}
