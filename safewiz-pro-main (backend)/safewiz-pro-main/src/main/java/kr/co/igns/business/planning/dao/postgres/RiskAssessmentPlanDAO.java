package kr.co.igns.business.planning.dao.postgres;

import kr.co.igns.business.planning.model.RiskAssessmentPlanVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RiskAssessmentPlanDAO {

    List<RiskAssessmentPlanVO> getRiskAssessmentPlanList(SpSearchVO vo);

    List<RiskAssessmentPlanVO> getRiskAssessmentPlanOrgnPopupList(SpSearchVO vo);

    List<RiskAssessmentPlanVO> getRiskAssessmentPlanPrevPopupList(RiskAssessmentPlanVO vo);

    void insertRiskAssessmentPrevPlan(RiskAssessmentPlanVO vo);

    void insertRiskAssessmentPlan(RiskAssessmentPlanVO vo);

    RiskAssessmentPlanVO getRiskAssessmentPlanDetail(RiskAssessmentPlanVO vo);

    void updateRiskAssessmentPlan(RiskAssessmentPlanVO vo);

    void updateRiskAssessmentPlanCpemData(RiskAssessmentPlanVO vo);

    void deleteRiskAssessmentPlan(RiskAssessmentPlanVO vo);
    
    /**
     * 위험성평가 계획 > 요인, 감소대책, 평균 위험도 조회
     * @param vo
     * @return
     */
    RiskAssessmentPlanVO selectRiskAvgCount(SpSearchVO vo);
}
