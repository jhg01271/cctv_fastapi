package kr.co.igns.system.user.service;

import kr.co.igns.business.comp.company.dao.postgres.CompanyUserDAO;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.system.common.dao.postgres.FileDAO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import kr.co.igns.system.user.entity.Role;
import kr.co.igns.system.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.service
 * ※ 파일명 : UserService.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 12.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDao;
    private final FileDAO fileDAO;
    private final CompanyUserDAO companyUserDao;
    private final PasswordEncoder passwordEncoder;
    private final FileProperty property;


    @Value("${spring.mail.host}")
    private String HOST;
    @Value("${spring.mail.port}")
    private Integer PORT;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Value("${spring.mail.password}")
    private String PASSWORD;
    @Value("${user.default-status}")
    private String DEFAULT_STATUS;

    public BaseVO create(UserVO userVo) throws Exception {
        encryptUser(userVo);
        userVo.setRole(Role.USER.name());
        userDao.insert(userVo);

        return userVo;
    }

    public BaseVO modify(UserVO userVo) throws Exception {
        //String userId = SecurityUtil.getCurrentMemberId();
        userVo.setUserId(userVo.getUserId());
        UserVO vo = userDao.findOne(userVo.getUserId());
        if (vo == null)
            return null;
        vo = (UserVO) SpUtils.objectMap(userVo, vo);

        userDao.update(vo);

        return vo;
    }

    public BaseVO remove(String userId) throws Exception {
        BaseVO vo = userDao.findOne(userId);
        removeSingle(userId);

        return vo;
    }

    private int removeSingle(String userId) throws Exception {
        userDao.delete(userId);
        return userDao.updateUseYn(userId);
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return userDao.searchCount(searchVo);
    }

    public List<UserVO> search(SpSearchVO searchVo) throws Exception {
        List<UserVO> users = userDao.search(searchVo);
        for (UserVO user : users) {
            decryptUser(user);
        }
        return users;
    }

    public UserVO findOne(String userId) throws Exception {
        UserVO vo = userDao.findOne(userId);
        //decryptUser(vo);
        return vo;
    }

    public UserVO findOne() throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        UserVO vo = userDao.findOne(userId);
        UserVO vo2 = userDao.getOrgnHead(vo.getHrId());
        UserVO vo3 = userDao.getJbrpByUserId(userId);
        if (vo2 != null) {
            vo.setOrgnHeadId(vo2.getOrgnHeadId());
            vo.setOrgnHeadNm(vo2.getOrgnHeadNm());
        }
        if (vo3 != null) {
            vo.setJbrpId(vo3.getJbrpId());
            vo.setJbrpNm(vo3.getJbrpNm());
        }
        //decryptUser(vo);
        return vo;
    }

    /**
     * 최소 정보만 리턴
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public UserVO findOneSimpleUserInfo(String userId) throws Exception {
        UserVO vo = userDao.findOne(userId);
        UserVO rtnVo = new UserVO();
        rtnVo.setUid(vo.getUid());
        rtnVo.setUserId(vo.getUserId());
        rtnVo.setUserNm(vo.getUserNm());
        rtnVo.setRole(vo.getRole());

        return rtnVo;
    }

    // 사용자 정보 인코딩
    public void encryptUser(UserVO userVo) {
        if (userVo.getPhone() != null) userVo.setPhone(Aes256.encryptString(userVo.getPhone()));
        if (userVo.getUserNm() != null) userVo.setUserNm(Aes256.encryptString(userVo.getUserNm()));
        if (!isEncoded(userVo.getUserPs())) {
            userVo.setUserPs(new String(Base64.getDecoder().decode(userVo.getUserPs())));
            userVo.passwordEncode(passwordEncoder);
        }
    }

    // 사용자 정보 디코딩
    private void decryptUser(UserVO userVo) {
        if (userVo.getPhone() != null) userVo.setPhone(Aes256.decryptString(userVo.getPhone()));
        if (userVo.getPhone() != null) userVo.setUserNm(Aes256.decryptString(userVo.getUserNm()));
    }

    public boolean isEncoded(String userPs) {
        return userPs.startsWith("{bcrypt}") || userPs.startsWith("$2a$");
    }

    public boolean isCorrectPassword(String userId, String password) {
        UserVO current = userDao.findOne(userId);
        String decodingPassword = new String(Base64.getDecoder().decode(password));

        if (decodingPassword.isEmpty() || passwordEncoder.matches(decodingPassword, current.getUserPs())) {
            return true;
        } else {
            return false;
        }
    }

    //아이디 찾기
    public BaseVO findId(UserVO vo) throws Exception {
        vo.setEmail(Aes256.encryptString(vo.getEmail()));
        return userDao.findId(vo);
    }

    //비밀번호 찾기
    public boolean findPassword(UserVO vo) throws Exception {
        //이메일 인증 처리해야댐
        vo.setEmail(Aes256.encryptString(vo.getEmail()));
        return userDao.findPassword(vo) > 0;
    }

    public UserVO searchMyInfo() throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();

        UserVO param = new UserVO();
        param.setUserId(userId);

        UserVO result = userDao.searchMyInfo(param);
        result.setLoginDevice(userDao.getMyLastLoginDevice(userId));
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveMyInfo(UserVO userVo) throws Exception {
        if (!"".equals(userVo.getUserPs()) && userVo.getUserPs() != null) {
            userVo.setUserPs(new String(Base64.getDecoder().decode(userVo.getUserPs())));
            userVo.passwordEncode(passwordEncoder);
        }
        //safewizpro 알림 허용 거부 시, 토큰 null 처리
        if("N".equals(userVo.getSafewizProYn())){
            userVo.setFcmToken(null);
            userDao.updateFcmToken(userVo);
        }
        userDao.saveMyInfo(userVo);
    }

    public String updateFcmToken(UserVO userVo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        userVo.setUserId(userId);
        userDao.updateFcmToken(userVo);
        String fcmToken = userDao.getTokenByUserId(userId);
        return fcmToken;
    }

    //
    public List<String> getAllTokenUser(UserVO vo) throws Exception {
        List<UserVO> userVo = userDao.getAllTokenUser(vo);
        List<String> listVo = new ArrayList<String>();
        for (UserVO user : userVo) {
            listVo.add(user.getUserId());
        }
        return listVo;
    }
}
