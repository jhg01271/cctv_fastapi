package kr.co.igns.system.user.dao.postgres;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import kr.co.igns.system.user.model.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.dao.postgres
 * ※ 파일명 : UserDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 12.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Mapper
@Repository
public interface UserDAO {

	int insert(UserVO vo);

	int update(UserVO vo);

	int updateUseYn(String userId);

	int delete(String userId);

	int searchCount(SpSearchVO searchVo);

	List<UserVO> search(SpSearchVO searchVo);

	UserVO findOne(String userId);

	UserVO findByUid(String uid);

	UserVO findId(UserVO vo);
	int findPassword(UserVO vo);

	UserVO searchMyInfo(UserVO userVo);
	void saveMyInfo(UserVO userVo);

	UserVO getOrgnHead(String userId);

	UserVO getJbrpByUserId(String userId);

	int saveLoginHistory(LoginHistoryVO vo);

	int updateLoginUserInfo(LoginHistoryVO vo); // user_info 테이블 업데이트
	int increaseLoginFailCnt(LoginHistoryVO vo); // user_info 테이블 로그인 실패 시 - 로그인 실패 횟수 증가
	int resetLoginFailCnt(LoginHistoryVO vo); // user_info 테이블 로그인 실패 횟수 초기화

	int updateFcmToken(UserVO vo);

	List<UserVO> getAllTokenUser(UserVO vo);

	String getTokenByUserId(String userId);

	String getMyLastLoginDevice (String userId);



    // 임시 파일 디렉토리 변경용도
	List<FileVO> getSystemFileList();
	void updateSystemFile(String fileId);
	List<FileVO> getBaseFileList();
	void updateBaseFile(String fileId);
	List<FileVO> getNormalFileList();
	void updateNormalFile(String fileId, String year);

    // 토큰 관련
    int insertApiTokenUser(@Param("userId") String userId);

}
