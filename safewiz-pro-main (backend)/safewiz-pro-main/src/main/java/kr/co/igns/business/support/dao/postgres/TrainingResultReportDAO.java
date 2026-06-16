package kr.co.igns.business.support.dao.postgres;

import java.util.List;

import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.support.model.TrainingResultReportHrVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface TrainingResultReportDAO {
    List<TrainingResultReportVO> getTrainingResult(SpSearchVO searchVo);
    TrainingResultReportVO getTrainingResultDetail(SpSearchVO searchVo);
    List<HrVO> getTrainingResultDetailPlanHrList(SpSearchVO searchVo);
    List<FileVO> getTrainingResultDetailFileList(TrainingResultReportVO searchVo);
    int insertTrainingResultReport(TrainingResultReportVO vo);
    
    int insertTrainingResultReportHr(TrainingResultReportHrVO vo);

    int updateTrainingResultReport(TrainingResultReportVO vo);

    int deleteTrainingResultReport(TrainingResultReportVO vo);
    
    int deleteTrainingResultReportHr(TrainingResultReportVO vo);

    TrainingResultReportVO getTrainingResultReportById(TrainingResultReportVO vo);

    //전년도 불러오기 기능
    List<TrainingResultReportVO> getCurrentAndPreviousYear(SpSearchVO searchVo);

    //일괄출력용
    List<String> getAllReport(SpSearchVO searchVo);
}
