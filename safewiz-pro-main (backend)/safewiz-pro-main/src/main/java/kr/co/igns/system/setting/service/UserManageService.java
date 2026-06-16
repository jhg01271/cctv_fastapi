package kr.co.igns.system.setting.service;

import kr.co.igns.framework.comm.model.CurrentWeatherVO.Sys;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.UserManageDAO;
import kr.co.igns.system.setting.model.UserManageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManageService {
    private final UserManageDAO userDao;
    private final PasswordEncoder passwordEncoder;

    public List<UserManageVO> getUser(SpSearchVO searchVo) throws Exception {
        List<UserManageVO> voList = userDao.getUserList(searchVo);
        for (UserManageVO user : voList) {
            decryptUser(user);
        }
        return voList;
    }

    public UserManageVO getUserDetail(String userId) throws Exception {
        UserManageVO voList = userDao.getUserDetail(userId);
        decryptUser(voList);
        return voList;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return userDao.searchCount(searchVo);
    }

    // 사용자 정보 디코딩
    private void decryptUser(UserManageVO userVo) {
        userVo.setPhone(Aes256.decryptString(userVo.getPhone()));
        userVo.setEmail(Aes256.decryptString(userVo.getEmail()));
    }


    public BaseVO insertUser(UserManageVO reqVo) throws Exception {
        if (reqVo.getPhone() != null) reqVo.setPhone(Aes256.encryptString(reqVo.getPhone()));
        if (reqVo.getEmail() != null) reqVo.setEmail(Aes256.encryptString(reqVo.getEmail()));

        reqVo.setUserPs(new String(Base64.getDecoder().decode(reqVo.getUserPs())));
        reqVo.passwordEncode(passwordEncoder);
        reqVo.setClntId(reqVo.getTargetClntId());
        userDao.insertUser(reqVo);
        
        // 사용자 관리에서 호출된 경우에만 '관리자 권한' 사용자의 사업장 매핑 정보 추가
        
        /* 사용자 관리에서 호출된 건지 hrService에서 호출된 것인지 판단. 
         * 사용자 관리에서 호출하는 경우   orgnCompYn 빈값 / 인원 관리에서 호출하는 경우  orgnCompYn='Y'
         * (참고) 관리자 권한 사용자는 인원관리에서 관리 안하고 사용자관리에서 관리함
         */
        if (reqVo.getOrgnCompYn() != null && !"Y".equals(reqVo.getOrgnCompYn()) && !"USER".equals(reqVo.getAuthGroupCd())) { 
            /* 사용자 관리에서 호출된 경우, 사업장 매핑 정보 등록  - 관리자 권한의 사용자에 해당 */
            if (reqVo.getCompId().contains(";")) {
                String[] splitText = reqVo.getCompId().split(";");
                for (int i = 0; i < splitText.length; i++) {
                    reqVo.setCompId(splitText[i]);
                    userDao.insertHrInfoCompMap(reqVo);
                }
            } else {
                userDao.insertHrInfoCompMap(reqVo);
            }
        } else {
            /* hrService에서 호출된 insertUser일 경우에는 사업장 매핑 정보 처리 안함  */
        }
        return reqVo;
    }


    public boolean isEncoded(String userPs) {
        return userPs.startsWith("{bcrypt}") || userPs.startsWith("$2a$");
    }

    public BaseVO updateUser(UserManageVO reqVo) throws Exception {
        reqVo.setClntId(reqVo.getCurrentClntId());
        UserManageVO vo = userDao.getUserByCd(reqVo);

        if (vo == null)
            return null;

        if (!"".equals(reqVo.getUserPs()) && reqVo.getUserPs() != null) {
            // 비밀번호 변경 시
            reqVo.setUserPs(new String(Base64.getDecoder().decode(reqVo.getUserPs())));
            reqVo.passwordEncode(passwordEncoder);
        } else {
            reqVo.setUserPs(vo.getUserPs());
        }

        vo = (UserManageVO) SpUtils.objectMap(reqVo, vo);
        vo.setClntId(reqVo.getTargetClntId());
        if (vo.getEmail() != null) vo.setEmail(Aes256.encryptString(vo.getEmail()));
        if (vo.getPhone() != null) vo.setPhone(Aes256.encryptString(vo.getPhone()));
        userDao.updateUser(vo);

        // 사용자 관리에서 호출된 경우에만 '관리자 권한' 사용자의 사업장 매핑 정보 수정
        
        /* 사용자 관리에서 호출된 건지 hrService에서 호출된 것인지 판단. 
         * 사용자 관리에서 호출하는 경우   orgnCompYn 빈값 / 인원 관리에서 호출하는 경우  orgnCompYn='Y'
         * (참고) 관리자 권한 사용자는 인원관리에서 관리 안하고 사용자관리에서 관리함
         */
        if (reqVo.getOrgnCompYn() != null && !"Y".equals(reqVo.getOrgnCompYn()) && !"USER".equals(reqVo.getAuthGroupCd())) {
            /* 사용자 관리에서 호출된 경우, 사업장 매핑 정보 등록  - 관리자 권한의 사용자에 해당
             * 사용자 관리에서 호출하는 경우   orgnCompYn 빈값
             */
            userDao.deleteHrInfoCompMap(reqVo);
            if (reqVo.getCompId().contains(";")) {
                reqVo.setHrId(vo.getHrId());
                String[] splitText = reqVo.getCompId().split(";");
                for (int i = 0; i < splitText.length; i++) {
                    reqVo.setCompId(splitText[i]);
                    userDao.insertHrInfoCompMap(reqVo);
                }
            } else {
                userDao.insertHrInfoCompMap(reqVo);
            }
        } else {
            /* hrService에서 호출된 updateUser일 경우에는 사업장 매핑 정보 처리 안함  
             * 2026.01.13 hrService에서 updateUser 호출안함
             */
        }
        return vo;
    }

    public BaseVO deleteUser(UserManageVO reqVo) throws Exception {
        BaseVO vo = userDao.getUserByCd(reqVo);
        userDao.deleteUser(reqVo);
        userDao.deleteHrInfoCompMap(reqVo);
        return vo;
    }

    public void deleteUsers(List<UserManageVO> list) throws Exception {
        for (UserManageVO tmp : list) {
            userDao.deleteUser(tmp);
            userDao.deleteHrInfoCompMap(tmp);
        }
    }

    public List<UserManageVO> insertHrInfoCompMap(UserManageVO vo) throws Exception {
        List<UserManageVO> voList = new ArrayList<>();
        if (vo.getCompId().contains(";")) {
            String[] splitText = vo.getCompId().split(";");
            for (int i = 0; i < splitText.length; i++) {
                vo.setCompId(splitText[i]);
                userDao.insertHrInfoCompMap(vo);
                voList.add(vo);
            }
        } else {
            userDao.insertHrInfoCompMap(vo);
            voList.add(vo);
        }
        return voList;
    }

    public List<UserManageVO> updateHrInfoCompMap(UserManageVO vo) throws Exception {
        List<UserManageVO> voList = new ArrayList<>();
        userDao.deleteHrInfoCompMap(vo);
        if (vo.getCompId().contains(";")) {
            String[] splitText = vo.getCompId().split(";");
            for (int i = 0; i < splitText.length; i++) {
                vo.setCompId(splitText[i]);
                userDao.insertHrInfoCompMap(vo);
                voList.add(vo);
            }
        } else {
            userDao.insertHrInfoCompMap(vo);
            voList.add(vo);
        }

        return voList;
    }
}
