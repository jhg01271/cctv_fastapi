package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmergencyResponseDAO {
    List<EmergencyResponseVO> getEmergencyResponse(SpSearchVO searchVo);
}
