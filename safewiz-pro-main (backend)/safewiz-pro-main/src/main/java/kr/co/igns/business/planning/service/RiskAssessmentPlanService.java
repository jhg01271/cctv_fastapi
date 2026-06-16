package kr.co.igns.business.planning.service;

import kr.co.igns.business.planning.dao.postgres.RiskAssessmentPlanDAO;
import kr.co.igns.business.planning.model.RiskAssessmentPlanVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskAssessmentPlanService {
    private final RiskAssessmentPlanDAO riskAssessmentPlanDAO;

    public List<RiskAssessmentPlanVO> getRiskAssessmentPlanList(SpSearchVO vo) throws Exception {
        return riskAssessmentPlanDAO.getRiskAssessmentPlanList(vo);
    }

    public List<RiskAssessmentPlanVO> getRiskAssessmentPlanOrgnPopupList(SpSearchVO vo) throws Exception {
        return riskAssessmentPlanDAO.getRiskAssessmentPlanOrgnPopupList(vo);
    }

    public List<RiskAssessmentPlanVO> getRiskAssessmentPlanPrevPopupList(RiskAssessmentPlanVO vo) throws Exception {
        return riskAssessmentPlanDAO.getRiskAssessmentPlanPrevPopupList(vo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertRiskAssessmentPrevPlan(List<RiskAssessmentPlanVO> voList) throws Exception {
        for (RiskAssessmentPlanVO plan : voList){
            riskAssessmentPlanDAO.insertRiskAssessmentPlan(plan);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertRiskAssessmentPlan(RiskAssessmentPlanVO vo) throws Exception {
        vo.setCompId(SecurityUtil.getCurrentCompId());
        riskAssessmentPlanDAO.insertRiskAssessmentPlan(vo);
    }

    public RiskAssessmentPlanVO getRiskAssessmentPlanDetail(RiskAssessmentPlanVO vo) throws Exception {
        return riskAssessmentPlanDAO.getRiskAssessmentPlanDetail(vo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateRiskAssessmentPlan(RiskAssessmentPlanVO vo) throws Exception {
        riskAssessmentPlanDAO.updateRiskAssessmentPlan(vo);
    }

    public void deleteRiskAssessmentPlan(List<RiskAssessmentPlanVO> vo) throws Exception {
        for (RiskAssessmentPlanVO riskAssessmentPlanVO : vo) {
            riskAssessmentPlanDAO.deleteRiskAssessmentPlan(riskAssessmentPlanVO);
        }
    }
    
    /**
     * 위험성평가 계획 > 요인, 감소대책, 평균 위험도 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public RiskAssessmentPlanVO getRiskAvgCount(SpSearchVO vo) throws Exception {
        return riskAssessmentPlanDAO.selectRiskAvgCount(vo);
    }
}
