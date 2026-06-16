package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import kr.co.igns.business.support.model.TrainingLocationVO;
import java.util.List;

@Mapper
@Repository
public interface TrainingPlanImplDAO {

    List<TrainingLocationVO> getLocMngList(SpSearchVO searchVo);
    List<TrainingLocationVO> getDataSetLocMngList(SpSearchVO searchVo);
    int insertLocMngList(TrainingLocationVO vo);
    int updateLocMngList(TrainingLocationVO vo);
    int deleteLocMngList(TrainingLocationVO vo);

    List<TrainingPlanImplVO> getTrainingPlanImpl(SpSearchVO searchVo);
    TrainingPlanImplVO getTrainingPlanImplDetail(SpSearchVO searchVo);

    int insertTrainingPlanImpl(TrainingPlanImplVO vo);
    int insertTrainingPlanImplHr(TrainingPlanImplHrVO vo);

    int updateTrainingPlanImpl(TrainingPlanImplVO vo);

    int deleteTrainingPlanImpl(TrainingPlanImplVO vo);
    int deleteTrainingPlanImplHr(TrainingPlanImplVO vo);

    TrainingPlanImplVO getTrainingPlanImplById(TrainingPlanImplVO vo);

    //전년도 불러오기 기능
    List<TrainingPlanImplVO> getCurrentAndPreviousYear(SpSearchVO searchVo);

    // 일괄출력
    List<String> getAllReport(SpSearchVO searchVo);

    List<HrVO> getTrainingPlanImplementationLearner(TrainingPlanImplVO vo);
    int insertTrainingPlanImplementationLearner(HrVO vo);
    int deleteTrainingPlanImplementationLearner(TrainingPlanImplVO vo);

}
