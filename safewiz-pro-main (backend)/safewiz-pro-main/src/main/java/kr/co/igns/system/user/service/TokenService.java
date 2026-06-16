package kr.co.igns.system.user.service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.user.dao.postgres.TokenDAO;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.model.TokenVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenDAO tokenDAO;
    private final UserDAO userDao;

    public TokenVO create(TokenVO tokenVo) {
        tokenDAO.insert(tokenVo);
        return tokenVo;
    }

    public TokenVO remove(TokenVO tokenVo) {
        System.out.println("######## ??1 ");
        TokenVO vo = tokenDAO.findOne(tokenVo.getUid());
//        if (vo != null) removeSingle(vo);
        return vo;
    }

    public TokenVO removeByRefreshToken(String token) {
        TokenVO vo = tokenDAO.findByRefreshToken(token);
        if (vo != null) removeSingle(vo);
        return vo;
    }

    public int removeSingle(TokenVO tokenVo) {
        return tokenDAO.updateDelYn(tokenVo.getUid(), tokenVo.getRefreshToken());
//        return tokenDAO.delete(tokenVo.getUid(), tokenVo.getRefreshToken());
    }

    public TokenVO findOne(String uid, String refreshToken) {
        TokenVO vo = tokenDAO.findOne(uid);
        return vo;
    }

    public List<TokenVO> search(SpSearchVO searchVo) throws Exception {
        List<TokenVO> tokens = tokenDAO.search(searchVo);
        return tokens;
    }
    /**
     * 토큰 관련  API
     * */

    /**** 사용자 refresh 토큰 ****/
    public List<TokenVO> getUserToken(SpSearchVO vo) throws ParseException {
        return tokenDAO.getUserToken(vo);
    }
    // API AccessToken 제거 메소드
    @Transactional(rollbackFor = Throwable.class)
    public void removeUserToken(List<TokenVO> voList) throws ParseException {
        for (TokenVO tokenVo : voList) {
            tokenDAO.updateUserTokenDelYn(tokenVo);
        }
    }

    /**** API 토큰 ****/
    // API AccessToken 조회 메소드
    public List<TokenVO> getApiToken(SpSearchVO vo) throws ParseException {
        return tokenDAO.getApiToken(vo);
    }

    // API AccessToken 생성 메소드
    @Transactional(rollbackFor = Throwable.class)
    public TokenVO createApiToken(List<TokenVO> voList, JwtService jwtService) throws ParseException {
        for (TokenVO tokenVo : voList) {
            UserVO initUser = userDao.findOne(tokenVo.getUserId()); // 최초인지 구별
            if (initUser == null) {
                userDao.insertApiTokenUser(tokenVo.getUserId()); // 최초라면 사용자 정보 insert
            }
            UserVO user = userDao.findOne(tokenVo.getUserId()); // 최초인지 구별
            String date = DateUtils.formatDateForDB(tokenVo.getExpirationPeriod());
            // 문자열을 LocalDate로 파싱
            LocalDate localDate = LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            // 23:59:59 로 설정
            LocalDateTime localDateTime = localDate.atTime(23, 59, 59);

            // 토큰 발급
            // java.util.Date 로 변환
            Date expireAt = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String accessToken = jwtService.createTokenByUserAndExpiration(user, expireAt);
            tokenVo.setToken(accessToken);
            tokenVo.setUid(user.getUid());
            tokenVo.setCreatedBy(SecurityUtil.getCurrentMemberId());

            // 토큰 만료일
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String expireAtStr = sdf.format(expireAt);
            tokenVo.setExpirationPeriod(expireAtStr);
            tokenDAO.insertApiToken(tokenVo);
        }
        return voList.get(0);
    }

    // API AccessToken 제거 메소드
    @Transactional(rollbackFor = Throwable.class)
    public void removeApiToken(List<TokenVO> voList) throws ParseException {
        for (TokenVO tokenVo : voList) {
            tokenDAO.updateApiDelYn(tokenVo);
        }
    }

    public int checkWhiteList(String apiToken) {
        return tokenDAO.countByToken(apiToken);
    }
}
