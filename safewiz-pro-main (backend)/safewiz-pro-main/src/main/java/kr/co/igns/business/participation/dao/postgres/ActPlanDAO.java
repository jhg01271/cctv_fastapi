package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.participation.model.ActPlanDetailVO;
import kr.co.igns.business.participation.model.ActPlanPerformanceDetailVO;
import kr.co.igns.business.participation.model.ActPlanPerformanceVO;
import kr.co.igns.business.participation.model.ActPlanVO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ActPlanDAO {

    List<ActPlanVO> getActPlanMaster(SpSearchVO searchVo);
    int insertActPlanMaster(ActPlanVO vo);

    //전사 목표, 추진 목표 계획
    List<ActPlanVO> getActPlan(SpSearchVO searchVo);
    ActPlanVO getActPlanDetail(SpSearchVO searchVo);

    //목표 및 항목 리스트 조회
    List<ActPlanVO> getObjective(SpSearchVO searchVo);
    List<ActPlanVO> getActionObjective(SpSearchVO searchVo);

    List<ActPlanPerformanceVO> getActionObjectiveOrgnList(SpSearchVO searchVo);
    List<SafetyAndHealthObjectivesVO> getSafetyHealthObjOrgnList(SpSearchVO searchVo);

    int insertActPlan(ActPlanVO vo);
    int insertObjective(ActPlanVO vo);    //전사 목표 테이블 데이터 생성
    int updateObjective(ActPlanVO vo);    //전사 목표 테이블 데이터 수정

    int updateActPlan(ActPlanVO vo);

    int deleteActPlan(ActPlanVO vo);

    ActPlanVO getActPlanById(ActPlanVO vo);

    //추진 계획 - 월별
    List<ActPlanDetailVO> getActionMonthly(SpSearchVO searchVo);
    List<ActPlanDetailVO> getActionMonthlyDetail(SpSearchVO searchVo);

    //목표 및 항목 리스트 조회
    List<ActPlanDetailVO> getDetailItem(SpSearchVO searchVo);
    int updateActionDetailItem(ActPlanDetailVO vo);    // 세부 항목 저장
    int insertDetailItem(ActPlanDetailVO vo);    //상세 항목 테이블 데이터 생성

    int insertActionMonthly(ActPlanDetailVO vo);

    int updateActionMonthly(ActPlanDetailVO vo);

    int deleteActionMonthly(ActPlanDetailVO vo);
    int deleteActionMonthlyByDetailItem(ActPlanDetailVO vo);

    ActPlanDetailVO getActionMonthlyById(ActPlanDetailVO vo);

    //실적, 예산
    List<ActPlanPerformanceVO> getActionPerformance(SpSearchVO searchVo);
    ActPlanPerformanceVO getActionPerformanceDetail(SpSearchVO searchVo);
    ActPlanPerformanceVO getActionPerformanceById(ActPlanPerformanceVO vo);
    ActPlanPerformanceDetailVO getActionPerformanceDetailById(ActPlanPerformanceDetailVO vo);

    List<ActPlanPerformanceDetailVO> getPerformanceList(SpSearchVO searchVo);   //실적 증빙 자료 조회
    List<HrVO> getPerformanceHrList(ActPlanPerformanceVO vo);   //실적 담당자 조회

    int insertActionPerformance(ActPlanPerformanceVO vo);
    int updateActionPerformance(ActPlanPerformanceVO vo);
    int updateActionPerformanceUseYn(ActPlanPerformanceVO vo);
    int deleteActionPerformance(ActPlanPerformanceVO vo);
    int deleteActionPerformanceByOrgnId(ActPlanPerformanceVO vo);

    int insertActionPerformanceDetail(ActPlanPerformanceDetailVO vo);
    int updateActionPerformanceDetail(ActPlanPerformanceDetailVO vo);
    int updateActionPerformanceDetailUseYn(ActPlanPerformanceDetailVO vo);
    int deleteActionPerformanceDetail(ActPlanPerformanceVO vo); //조직별 실적 증빙 자료 모두 삭제

    int deleteActionPerformanceHrList(ActPlanPerformanceVO vo); // 실적 담당자 삭제
    int insertActionPerformanceHrList(ActPlanPerformanceVO vo); // 실적 담당자 저장

    HseKeyPerformanceResultVO getOnlyActionPerformance(ActPlanDetailVO vo);
}
