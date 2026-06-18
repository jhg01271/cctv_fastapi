package kr.co.igns.business.realTimeVideoSafetyManagement.service;

import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import kr.co.igns.business.realTimeVideoSafetyManagement.dao.postgres.RealTimeVideoSafetyObjectivesDAO;
import kr.co.igns.business.realTimeVideoSafetyManagement.model.RealTimeVideoSafetyObjectivesVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RealTimeVideoSafetyObjectivesService {
    private final RealTimeVideoSafetyObjectivesDAO realTimeVideoSafetyObjectivesDao;


    public List<RealTimeVideoSafetyObjectivesVO> getRealTimeVideoSafetyObjectives(SpSearchVO searchVo) throws Exception {
        List<RealTimeVideoSafetyObjectivesVO> voList = realTimeVideoSafetyObjectivesDao.getRealTimeVideoSafetyObjectives(searchVo);
        return voList;
    }
    
}
