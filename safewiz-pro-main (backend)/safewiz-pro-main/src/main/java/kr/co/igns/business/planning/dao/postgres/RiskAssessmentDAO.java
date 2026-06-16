package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.planning.model.WorkersOpinionsOnHazardsVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface RiskAssessmentDAO {
    List<RiskAssessmentVO> getRiskAssessment(SpSearchVO searchVo);
}
