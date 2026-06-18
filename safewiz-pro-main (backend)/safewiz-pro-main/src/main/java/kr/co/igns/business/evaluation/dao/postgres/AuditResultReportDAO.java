package kr.co.igns.business.evaluation.dao.postgres;

import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.AuditResultDetaillVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuditResultReportDAO {
    List<AuditExecutionPlanVO> getAuditResultReport(SpSearchVO searchVo);
    List<AuditExecutionPlanDetailVO> getAuditResultReportOrgn(AuditExecutionPlanVO vo);
    AuditExecutionPlanDetailVO getAuditExecutionPlan(SpSearchVO searchVo);
    AuditExecutionPlanDetailVO getAuditResultReportSingle(AuditExecutionPlanDetailVO vo);
    List<HrVO> getAuditExecutionPlanHr(AuditExecutionPlanDetailVO vo);
    List<AuditResultDetaillVO> getAuditResultReportDetail(AuditExecutionPlanDetailVO vo);
    List<HrVO> getAuditResultReportHr(AuditExecutionPlanDetailVO vo);
    void deleteAuditReport(AuditExecutionPlanDetailVO vo);
    void updateAuditResult(AuditExecutionPlanDetailVO vo);
    void insertAuditResult(AuditExecutionPlanDetailVO vo);
    void updateAuditResultDetail(AuditResultDetaillVO vo);
    void insertAuditResultDetail(AuditResultDetaillVO vo);
    void deleteAuditHr(AuditExecutionPlanDetailVO vo);
    void insertAuditHr(HrVO vo);
}
    