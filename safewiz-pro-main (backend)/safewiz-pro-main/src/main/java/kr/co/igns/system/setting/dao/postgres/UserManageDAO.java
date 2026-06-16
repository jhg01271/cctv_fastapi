package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.UserManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserManageDAO {
    List<UserManageVO> getUserList(SpSearchVO searchVo);
    UserManageVO getUserDetail(String userId);

    int searchCount(SpSearchVO searchVo);
    int insertUser(UserManageVO vo);
    int updateUser(UserManageVO vo);
    int deleteUser(UserManageVO vo);
    String getBeforeHrInfoCompMap(UserManageVO vo);
    int insertHrInfoCompMap(UserManageVO vo);
    int updateHrInfoCompMap(UserManageVO vo);
    int deleteHrInfoCompMap(UserManageVO vo);

    UserManageVO getUserByCd(UserManageVO vo);
}
