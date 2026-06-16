package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LoginHistoryDAO {
    List<LoginHistoryVO> getLoginHistoryList(SpSearchVO searchVo);

    int searchCount(SpSearchVO searchVo);

    LoginHistoryVO getLastLoginDetails(SpSearchVO searchVo);
}
