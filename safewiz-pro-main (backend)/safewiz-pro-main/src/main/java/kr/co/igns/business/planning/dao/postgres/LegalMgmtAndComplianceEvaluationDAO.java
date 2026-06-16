package kr.co.igns.business.planning.dao.postgres;

import kr.co.igns.business.planning.model.LegalMgmtAndComplianceEvaluationVO;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LegalMgmtAndComplianceEvaluationDAO {
    List<LegalMgmtAndComplianceEvaluationVO> getLegalMgmtAndComplianceEvaluation(SpSearchVO searchVo);
}
