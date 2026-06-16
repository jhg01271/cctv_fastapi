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
public interface WorkersOpinionsOnHazardsDAO {
	
	List<WorkersOpinionsOnHazardsVO> getWorkersOpinionsOnHazards(@Valid SpSearchVO searchVo);
	List<WorkersOpinionsOnHazardsVO> getWorkersOpinionsOnHazardsDetail(@Valid SpSearchVO searchVo);
	String getHrId(@Valid SpSearchVO searchVo);
	List<WorkersOpinionsOnHazardsVO> getWorkeryDupe(@Valid WorkersOpinionsOnHazardsVO searchVo);
	List<WorkersOpinionsOnHazardsVO> getWorkerHr(@Valid WorkersOpinionsOnHazardsVO searchVo);

	String getWorkerHrName(@Valid String hrId);
	List<WorkersOpinionsOnHazardsVO> getWorkerReportHrList(@Valid WorkersOpinionsOnHazardsVO searchVo);
	WorkersOpinionsOnHazardsVO getWorkerReportDetail(@Valid WorkersOpinionsOnHazardsVO searchVo);
	List<WorkersOpinionsOnHazardsVO> getWorkerReportDetailExp(@Valid WorkersOpinionsOnHazardsVO searchVo);
	
	int insertWorkersOpinionsOnHazardsDetail(@Valid WorkersOpinionsOnHazardsVO vo);
	int insertWorkersOpinionsOnHazardsExp(@Valid WorkersOpinionsOnHazardsVO vo);
	
	int changeWorkersOpinionsOnHazardsDetail(@Valid WorkersOpinionsOnHazardsVO vo);
	int changeWorkersOpinionsOnHazardsExp(@Valid WorkersOpinionsOnHazardsVO vo);
	int changeWorkersOpinionsOnHazardsUseN(@Valid WorkersOpinionsOnHazardsVO vo); 
	
	int deleteWorkerOpinonsOnHarzardsDetail(@Valid WorkersOpinionsOnHazardsVO vo);
	int deleteWorkerOpinonsOnHarzardsExp(@Valid WorkersOpinionsOnHazardsVO vo);

}
