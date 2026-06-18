package kr.co.igns.system.user.service;

import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.model.VerificationVO;
import kr.co.igns.business.comp.company.dao.postgres.CompanyDAO;
import kr.co.igns.business.comp.company.dao.postgres.CompanyUserDAO;
import kr.co.igns.business.comp.company.model.CompanyUserVO;
import kr.co.igns.business.comp.company.model.CompanyVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.framework.config.exception.CInviteKeyException;
import kr.co.igns.framework.config.exception.CTokenValicationException;
import kr.co.igns.framework.config.exception.CUserExistException;
import kr.co.igns.framework.config.exception.CUserNotFoundException;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserDAO userDao;
    private final JwtService jwtService;
    private final CompanyDAO companyDAO;
    private final CompanyUserDAO companyUserDao;
    private final UserService userService;
    private final JavaMailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.verify.expiration}")
    private Long verificateExpirationPeriod;
    @Value("${jwt.initialize.expiration}")
    private Long initializeExpirationPeriod;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Value("${user.default-status}")
    private String DEFAULT_STATUS;
    @Value("${user.reset-password-url}")
    private String RESET_PASSWORD_URL;

    public BaseVO signUp(HttpServletResponse response, UserVO userVo) throws Exception {
        UserVO vo = userDao.findOne(userVo.getUserId());
        // email 중복
        if (vo != null) {
            throw new CUserExistException("이미 가입된 이메일입니다.");
        }
        UserVO user = null;
        // 초대 키 있을경우
        if (userVo.getInviteKey() != null && !"".equals(userVo.getInviteKey())) {
            // 토큰 유효 여부 확인
            String vaild = jwtService.isTokenValid(userVo.getInviteKey());

            if ("ok".equals(vaild)) {
                // 이전에 발행한 키인지 확인
                CompanyVO company = companyDAO.findByInviteKey(userVo.getInviteKey());
                if(company == null) {
                    new CInviteKeyException("유효하지 않은 초대키입니다.");
                }

                String password = userVo.getUserPs();
                userService.create(userVo);

                userVo.setUserPs(password);
                // 사용자 사업장 권한 테이블 삽입
                // 기본 권한 배치 방법 필요
                CompanyUserVO companyUser = new CompanyUserVO();
                companyUser.setCompId(company.getCompId());
                companyUser.setUserId(userVo.getUserId());
                companyUserDao.insert(companyUser);

                user = userService.findOne(userVo.getUserId());

                String accessToken = jwtService.createAccessToken(user);
                String refreshToken = jwtService.createRefreshToken(user);
                jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            } else {
                new CTokenValicationException("유효하지 않은 토큰입니다.");
            }

        } else {
            // 초대 키 없을경우
            userService.create(userVo);
            user = userService.findOne(userVo.getUserId());
        }
        return user;
    }

    public String sendCode(String email) throws MessagingException, IOException {
        UserVO vo = userDao.findOne(email);
        // email 중복
        if (vo != null) {
            throw new CUserExistException("이미 가입된 이메일입니다.");
        }
        String verificationCode = generateVerificationCode(6);
        send(email, "[FEMS] 회원가입 인증번호", verificationCode);

        String token = jwtService.createCustomToken("code", verificationCode, verificateExpirationPeriod);

        return token;
    }

    public void sendLink(String email) {
        UserVO vo = userDao.findOne(email);
        if (vo == null) {
            throw new CUserNotFoundException("존재하지 않는 사용자입니다.");
        }
        String token = jwtService.createCustomToken("uid", vo.getUid(), initializeExpirationPeriod);
        String url = RESET_PASSWORD_URL + token;

        send(email, "[FEMS] 비밀번호 초기화", url);
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(USERNAME);
        emailSender.send(message);
    }

    public boolean verifyCertification(VerificationVO vo){
        Optional<String> decodingToken =  jwtService.extractCustomClaim(vo.getCertificationToken(), "code");
        if (decodingToken.isPresent() && decodingToken.get().equals(vo.getCertificationCode())) {
            return true;
        } else {
            return false;
        }
    }
    private String generateVerificationCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0-9 사이의 랜덤 숫자 생성
        }
        return sb.toString();
    }

    public boolean initPassword(String token, String password) throws Exception {
        Optional<String> uid = jwtService.extractCustomClaim(token, "uid");
        if (uid.isPresent()) {
            UserVO user = userDao.findByUid(uid.get());
            String ps = new String(Base64.getDecoder().decode(password));
            user.setUserPs(ps);
            if (!userService.isEncoded(password)) {
                user.passwordEncode(passwordEncoder);
            }
            userDao.update(user);
            return true;
        } else {
            return false;
        }
    }
}
