package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.JobMgmtVO;
import kr.co.igns.business.support.model.JobLevelVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobCompetencyAssessmentDAO {
    List<JobCompetencyAssessmentDetailVO> getCardJobManageList(SpSearchVO searchVo);
    List<JobMgmtVO> getJobManageList(SpSearchVO searchVo);
    List<JobMgmtVO> getEsgSetJobManageList(SpSearchVO searchVo);
    List<JobMgmtVO> getDataSetJobManageList(SpSearchVO searchVo);
    int insertJobManageList (JobMgmtVO vo);
    int updateJobManageList (JobMgmtVO vo);
    int deleteJobManageList (JobMgmtVO vo);

    List<JobLevelVO> getJobLevelManageList(SpSearchVO searchVo);
    List<JobLevelVO> getDataSetJobLevelManageList(SpSearchVO searchVo);
    int insertJobLevelManageList (JobLevelVO vo);
    int updateJobLevelManageList (JobLevelVO vo);
    int deleteJobLevelManageList (JobLevelVO vo);

    List<JobCompetencyAssessmentVO> getJobCompAssessList(SpSearchVO searchVo);
    List<JobCompetencyAssessmentVO> getPreJobCompAssessList(SpSearchVO searchVo);
    List<JobCompetencyAssessmentDetailVO> getJobCompAssessDetail(SpSearchVO searchVo);
    int insertJobCompAssess(JobCompetencyAssessmentVO vo);
    int updateJobCompAssess(JobCompetencyAssessmentVO vo);

    int insertJobCompAssessDetail(JobCompetencyAssessmentDetailVO vo);
    int updateJobCompAssessDetail(JobCompetencyAssessmentDetailVO vo);

    int deleteJobCompAssess(JobCompetencyAssessmentVO vo);
    int deleteJobCompAssessDetail(JobCompetencyAssessmentDetailVO vo);

    // 일괄 출력
    List<String> getAllReport(SpSearchVO spSearchVO);
}
