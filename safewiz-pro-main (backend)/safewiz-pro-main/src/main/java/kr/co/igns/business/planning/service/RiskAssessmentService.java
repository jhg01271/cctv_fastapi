package kr.co.igns.business.planning.service;


import java.util.List;
import org.springframework.stereotype.Service;
import kr.co.igns.business.planning.dao.postgres.RiskAssessmentDAO;
import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class RiskAssessmentService { 
    private final RiskAssessmentDAO riskAssessmentDao;

    public List<RiskAssessmentVO> getRiskAssessment(SpSearchVO searchVo) throws Exception {
        List<RiskAssessmentVO> voList = riskAssessmentDao.getRiskAssessment(searchVo);
        
        return voList;
    }
}

