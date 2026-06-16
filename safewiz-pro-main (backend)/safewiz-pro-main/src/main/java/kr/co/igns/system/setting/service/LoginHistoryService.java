package kr.co.igns.system.setting.service;

import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.LoginHistoryDAO;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {
    private final LoginHistoryDAO loginHistoryDao;

    public List<LoginHistoryVO> getLoginHistory(SpSearchVO searchVo) throws Exception {
        List<LoginHistoryVO> voList = loginHistoryDao.getLoginHistoryList(searchVo);
        return voList;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return loginHistoryDao.searchCount(searchVo);
    }

    public LoginHistoryVO getLastLoginDetails(SpSearchVO searchVo) throws Exception {
        LoginHistoryVO vo = loginHistoryDao.getLastLoginDetails(searchVo);
        return vo;
    }
}
