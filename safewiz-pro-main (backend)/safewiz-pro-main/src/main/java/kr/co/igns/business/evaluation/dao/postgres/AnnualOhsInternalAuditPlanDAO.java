package kr.co.igns.business.evaluation.dao.postgres;

import kr.co.igns.business.evaluation.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AnnualOhsInternalAuditPlanDAO {
    List<AnnualOhsInternalAuditPlanVO> getAnnualOhsInternalAuditPlan(SpSearchVO searchVo);
    List<AnnualOhsInternalAuditPlanDetailVO> getAnnualOhsInternalAuditDetailPlan(AnnualOhsInternalAuditPlanVO vo);

    int insertAnnualOhsInternalAuditPlan(AnnualOhsInternalAuditPlanVO vo);
    int updateAnnualOhsInternalAuditPlan(AnnualOhsInternalAuditPlanVO vo);
    int deleteAnnualOhsInternalAuditPlan(AnnualOhsInternalAuditPlanVO vo);

    int deleteAnnualOhsInternalAuditPlanDetail(AnnualOhsInternalAuditPlanVO vo);
    int insertAnnualOhsInternalAuditPlanDetail(AnnualOhsInternalAuditPlanDetailVO vo);
}
