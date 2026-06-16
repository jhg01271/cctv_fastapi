package kr.co.igns.business.evaluation.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.evaluation.model.EvaluationCorrectiveActionRequestVO;
import kr.co.igns.business.improvement.model.CorrectiveActionRequestVO;
import kr.co.igns.business.improvement.model.IncidentReportVO;
import kr.co.igns.business.improvement.model.NearMissReportVO;
import kr.co.igns.business.improvement.model.NonconformityCorrectiveVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface EvaluationCorrectiveActionRequestDAO {
    
	List<EvaluationCorrectiveActionRequestVO> getCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo);
	EvaluationCorrectiveActionRequestVO getCorrectiveActionRequestDetail(SpSearchVO vo);
	
	void insertCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo);

	void updateCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo);

	void deleteCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo);

	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
	
}
