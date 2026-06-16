package kr.co.igns.business.evaluation.dao.postgres;

import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuditExecutionPlanDAO {
    List<AuditExecutionPlanVO> getAuditExecutionPlan(SpSearchVO searchVo);
    List<AuditExecutionPlanDetailVO> getAuditExecutionPlanOrgn(AuditExecutionPlanVO vo);
    List<HrVO> getAuditExecutionPlanOrgnHr(AuditExecutionPlanDetailVO vo);
    List<HrVO> getJudgeList(SpSearchVO searchVo);

    int insertAuditExecutionPlan(AuditExecutionPlanVO vo);
    int updateAuditExecutionPlan(AuditExecutionPlanVO vo);
    int deleteAuditExecutionPlan(AuditExecutionPlanVO vo);

    int insertAuditExecutionPlanOrgn(AuditExecutionPlanDetailVO vo);
    int updateAuditExecutionPlanOrgn(AuditExecutionPlanDetailVO vo);

    int deleteAuditExecutionPlanOrgnHr(AuditExecutionPlanDetailVO vo);
    int insertAuditExecutionPlanOrgnHr(HrVO vo);



}
