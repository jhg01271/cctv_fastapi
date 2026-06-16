package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.EmergencyResponseTrainingResultVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingScenarioVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmergencyResponseTrainingResultDAO {
    List<EmergencyResponseTrainingResultVO> getResultList(EmergencyResponseTrainingVO vo);
    List<EmergencyResponseTrainingResultVO> getResultDetail(EmergencyResponseTrainingVO vo);
    List<EmergencyResponseTrainingVO> getValidSenarioList(EmergencyResponseTrainingVO vo);

    int insertResult(EmergencyResponseTrainingResultVO vo);
    int updateResult(EmergencyResponseTrainingResultVO vo);
    int deleteResult(EmergencyResponseTrainingResultVO vo);

    // 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);
}
