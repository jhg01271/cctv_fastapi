package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface SafetyAndHealthInfoSurveyDAO {
    
	List<SafetyAndHealthInfoSurveyVO> getSafetyAndHealthInfoSurvey(@Valid SpSearchVO searchVo);
	List<SafetyAndHealthInfoSurveyVO> getSafetyAndHealthInfoSurveyDetail(@Valid SpSearchVO searchVo);
	List<SafetyAndHealthInfoSurveyVO> getProcessDataDetail(@Valid SafetyAndHealthInfoSurveyVO searchVo);
	
	String getProcessName(@Valid String processId);
	SafetyAndHealthInfoSurveyVO getReportData(@Valid SpSearchVO searchVo);
	List<SafetyAndHealthInfoSurveyVO> getReportGridData(@Valid SpSearchVO searchVo);
	
	int insertSafetyAndHealthInfoSurvey(@Valid SafetyAndHealthInfoSurveyVO vo);
	int insertSafetyAndHealthInfoSurveyDetail(@Valid SafetyAndHealthInfoSurveyVO vo);

	int confirmSafetyAndHealthInfoSurvey(@Valid SafetyAndHealthInfoSurveyVO vo);
	int changeSafetyAndHealthInfoSurvey(@Valid SafetyAndHealthInfoSurveyVO vo);
	int changeSafetyAndHealthInfoSurveyUseYn(@Valid SafetyAndHealthInfoSurveyVO vo);
	int changeSafetyAndHealthInfoSurveyDetail(@Valid SafetyAndHealthInfoSurveyVO vo);
	int changeSafetyAndHealthInfoSurveyDetailUseYn(@Valid SafetyAndHealthInfoSurveyVO vo);
	
	int deleteSafetyAndHealthInfoSurvey(@Valid SafetyAndHealthInfoSurveyVO vo);
	int deleteAllSafetyAndHealthInfoSurveyDetail(@Valid SafetyAndHealthInfoSurveyVO vo);
	int deleteSafetyAndHealthInfoSurveyDetail(@Valid SafetyAndHealthInfoSurveyVO vo);

	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);
}
