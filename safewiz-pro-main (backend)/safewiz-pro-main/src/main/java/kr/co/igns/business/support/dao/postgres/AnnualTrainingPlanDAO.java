package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AnnualTrainingPlanDAO {
    List<TrainingCoursesVO> getSubjectsMngList(SpSearchVO searchVo);
    List<TrainingCoursesVO> getDataSetSubjectsMngList(SpSearchVO searchVo);
    int insertSubjectsMngList(TrainingCoursesVO vo);
    int updateSubjectsMngList(TrainingCoursesVO vo);
    int deleteSubjectsMngList(TrainingCoursesVO vo);


    List<TrainingCenterVO> getEduMngList(SpSearchVO searchVo);
    List<TrainingCenterVO> getDataSetEduMngList(SpSearchVO searchVo);
    int insertEduMngList(TrainingCenterVO vo);
    int updateEduMngList(TrainingCenterVO vo);
    int deleteEduMngList(TrainingCenterVO vo);


    List<TrainningInstructorVO> getInstMngList(SpSearchVO searchVo);
    List<TrainningInstructorVO> getDataSetInstMngList(SpSearchVO searchVo);
    int insertInstMngList(TrainningInstructorVO vo);
    int updateInstMngList(TrainningInstructorVO vo);
    int deleteInstMngList(TrainningInstructorVO vo);


    List<AnnualTrainingPlanVO> getAnnualTrainingPlan(SpSearchVO searchVo);

    int insertAnnualTrainingPlan(AnnualTrainingPlanVO vo);

    int updateAnnualTrainingPlan(AnnualTrainingPlanVO vo);

    int deleteAnnualTrainingPlan(AnnualTrainingPlanVO vo);

    AnnualTrainingPlanVO getAnnualTrainingPlanById(AnnualTrainingPlanVO vo);

    //강사 조회, 저장, 삭제
    List<TrainingInstructorVO> getTrainingInstructor(SpSearchVO searchVo);

    int insertTrainingInstructor(TrainingInstructorVO vo);
    int updateTrainingInstructor(TrainingInstructorVO vo);
    int deleteTrainingInstructor(TrainingInstructorVO vo);

    TrainingInstructorVO getTrainingInstructorById(TrainingInstructorVO vo);

    //전년도 불러오기 기능
    List<AnnualTrainingPlanVO> getCurrentAndPreviousYear(SpSearchVO searchVo);

    // 일괄출력
    List<String> getAllReport(SpSearchVO spSearchVO);
    List<HrVO> getAnnualTrainingPlanAnnualLearner(AnnualTrainingPlanVO vo);
    int deleteAnnualTrainingPlanAnnualLearner(AnnualTrainingPlanVO vo);
    int insertAnnualTrainingPlanAnnualLearner(HrVO vo);
}
