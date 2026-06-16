package kr.co.igns.business.impl.dao.postgres;

import java.util.List;

import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.system.common.model.SpSearchVO;

public interface HseKeyPerformanceResultDAO {
    
    // 메인 화면 조회

    List<RiskAssessmentVO> getHsePerformance(SpSearchVO vo);

    List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

    List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo);

    List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesReport(SpSearchVO vo);
    
    List<HseKeyPerformanceResultVO> getDetail(SafetyAndHealthObjectivesVO vo);

    void insert(HseKeyPerformanceResultVO vo);

    void update(HseKeyPerformanceResultVO vo);

    void delete(HseKeyPerformanceResultVO vo);

    SafetyAndHealthObjectivesVO getReportData(SafetyAndHealthObjectivesVO vo);

    // 일괄출력
    List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
    
    List<TaskVO> getTaskList(HseKeyPerformanceResultVO vo);
    void updateTask(TaskVO vo);
}
