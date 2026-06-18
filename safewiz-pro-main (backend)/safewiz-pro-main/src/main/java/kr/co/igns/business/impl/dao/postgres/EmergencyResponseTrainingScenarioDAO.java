package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.EmergencyResponseTrainingScenarioVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmergencyResponseTrainingScenarioDAO {
    List<EmergencyResponseTrainingVO> getTrainingList(SpSearchVO searchVo);
    EmergencyResponseTrainingVO getTrainingDetail(SpSearchVO searchVo);
    List<EmergencyResponseTrainingScenarioVO> getScenarioList(EmergencyResponseTrainingVO vo);
    int insertTraining(EmergencyResponseTrainingVO vo);
    int updateTraining(EmergencyResponseTrainingVO vo);
    int deleteTraining(EmergencyResponseTrainingVO vo);

    int insertScenario(EmergencyResponseTrainingScenarioVO vo);
    int updateScenario(EmergencyResponseTrainingScenarioVO vo);
    int deleteScenario(EmergencyResponseTrainingScenarioVO vo);

    // 일괄출력용
    List<SpSearchVO> getScenarioReport(SpSearchVO spSearchVO);

}
