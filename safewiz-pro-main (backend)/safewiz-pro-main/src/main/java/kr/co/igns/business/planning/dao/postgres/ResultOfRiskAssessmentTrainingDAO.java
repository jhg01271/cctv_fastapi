package kr.co.igns.business.planning.dao.postgres;

import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.business.planning.model.ResultOfRiskAssessmentTrainingVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResultOfRiskAssessmentTrainingDAO {

    List<ResultOfRiskAssessmentTrainingVO> getResultOfRiskAssessmentTraining(SpSearchVO searchVo);

    void insertResultOfRiskAssessmentTraining(ResultOfRiskAssessmentTrainingVO vo);

    ResultOfRiskAssessmentTrainingVO getResultOfRiskAssessmentTrainingDetail(SpSearchVO vo);

    void updateResultOfRiskAssessmentTraining(ResultOfRiskAssessmentTrainingVO vo);

    void deleteResultOfRiskAssessmentTraining(ResultOfRiskAssessmentTrainingVO vo);

    List<FileVO> getResultOfRiskAssessmentTrainingFileList(SpSearchVO vo);

    List<SignatureDto> getResultOfRiskAssessmentTrainingAttendees(SpSearchVO vo);


    // 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);

}
