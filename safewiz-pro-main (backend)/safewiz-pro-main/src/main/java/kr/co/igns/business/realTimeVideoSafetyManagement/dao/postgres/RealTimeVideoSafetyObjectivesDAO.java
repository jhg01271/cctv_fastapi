package kr.co.igns.business.realTimeVideoSafetyManagement.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.realTimeVideoSafetyManagement.model.RealTimeVideoSafetyObjectivesVO;

import java.util.List;

@Mapper
@Repository
public interface RealTimeVideoSafetyObjectivesDAO {
    List<RealTimeVideoSafetyObjectivesVO> getRealTimeVideoSafetyObjectives(SpSearchVO searchVo);
}
