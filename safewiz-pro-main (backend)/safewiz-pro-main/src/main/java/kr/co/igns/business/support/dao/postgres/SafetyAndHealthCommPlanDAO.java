package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.SafetyAndHealthCommPlanDetailVO;
import kr.co.igns.business.support.model.SafetyAndHealthCommPlanVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SafetyAndHealthCommPlanDAO {
    List<SafetyAndHealthCommPlanVO> getShCommPlanList(SpSearchVO searchVo);
    List<SafetyAndHealthCommPlanDetailVO> getShCommPlanDetail(SafetyAndHealthCommPlanVO vo);
    int insertShCommPlan(SafetyAndHealthCommPlanVO vo);
    int updateShCommPlan(SafetyAndHealthCommPlanVO vo);
    int deleteShCommPlan(SafetyAndHealthCommPlanVO vo);

    int insertShCommPlanDetail(SafetyAndHealthCommPlanDetailVO vo);
    int updateShCommPlanDetail(SafetyAndHealthCommPlanDetailVO vo);
    int deleteShCommPlanDetail(SafetyAndHealthCommPlanDetailVO vo);
}
