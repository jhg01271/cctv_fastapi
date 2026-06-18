package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import kr.co.igns.business.planning.model.RiskOrgnRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.RiskAssessmentOrganChartVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface RiskAssessmentOrganChartDAO {
	/**
	 * 위험성평가 조직 역할 구성 데이터 조회
	 * @param searchVo
	 * @return
	 */
	List<RiskOrgnRoleVO> getRiskOrgList (SpSearchVO searchVo);

	/**
	 * 위험성평가 조직 역할 구성 예시 데이터 조회
	 * @param searchVo
	 * @return
	 */
	List<RiskOrgnRoleVO> getDataSetRiskOrgList (SpSearchVO searchVo);

	/**
	 * 위험성평가 조직 역할 구성 데이터 저장
	 * @param vo
	 * @return
	 */
	int insertRiskOrgList(RiskOrgnRoleVO vo);

	/**
	 * 위험성평가 조직 역할 구성 데이터 수정
	 * @param vo
	 * @return
	 */
	int updateRiskOrgList(RiskOrgnRoleVO vo);

	/**
	 * 전체인원 조회
	 * @param searchVo
	 * @return
	 */
	List<HrVO> getMembers(SpSearchVO searchVo);

    /**
     * chart 상세 조회
     * @param chartId
     * @return
     */
    RiskAssessmentOrganChartVO getChartDetail(String chartId);
    
    /**
     * 조직 목록 조회
     * @param searchVo
     * @return
     */
    List<RiskAssessmentOrganChartVO> getRiskAssessmentOrganChartList(SpSearchVO searchVo);

    /**
     * 조직 상세 조회
     * @param chartId
     * @return
     */
    RiskAssessmentOrganChartVO getRiskAssessmentOrganChartDetail(String chartId);
    
    /**
     * 위험성평가 조직도 저장
     * @param vo
     * @return
     */
    int insertRiskAssessmentOrganChart(RiskAssessmentOrganChartVO vo);
    
    /**
     * 위험성평가 조직도 수정
     * @param vo
     * @return
     */
    int updateRiskAssessmentOrganChart(RiskAssessmentOrganChartVO vo);
    
    /**
     * 위험성평가 조직도 확정여부 수정
     * @param vo
     * @return
     */
    int confirmRiskAssessmentOrganChart(RiskAssessmentOrganChartVO vo);
    
    /**
     * 위험성평가 조직도 삭제
     * @param vo
     * @return
     */
    int deleteRiskAssessmnetOrganChart(RiskAssessmentOrganChartVO vo);
}
