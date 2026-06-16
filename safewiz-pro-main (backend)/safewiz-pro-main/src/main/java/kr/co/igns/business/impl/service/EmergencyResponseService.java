package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.EmergencyResponseDAO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyResponseService {
    private final EmergencyResponseDAO emergencyResponseDAO;

    public List<EmergencyResponseVO> getEmergencyResponse(SpSearchVO searchVo) throws Exception {
        List<EmergencyResponseVO> voList = emergencyResponseDAO.getEmergencyResponse(searchVo);
        return voList;
    }
}
