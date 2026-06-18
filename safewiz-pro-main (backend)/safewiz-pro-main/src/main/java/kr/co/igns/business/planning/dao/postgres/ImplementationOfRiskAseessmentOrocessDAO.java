package kr.co.igns.business.planning.dao.postgres;

import java.util.List;
import java.util.Map;

import kr.co.igns.business.utils.model.UtilsVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.ClassificationOfHazardsVO;
import kr.co.igns.business.planning.model.ImplementationOfRiskAseessmentReductionVO;
import kr.co.igns.business.planning.model.ImplementationOfRiskAseessmentVO;
import kr.co.igns.business.planning.model.RiskAssessmentPlanVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ImplementationOfRiskAseessmentOrocessDAO {
	
	/**
	 * 목록 조회
	 * @param vo
	 * @return
	 */
	List<RiskAssessmentPlanVO> selectRiskImplList(SpSearchVO vo);
	
	/**
	 * 이행 상세 조회
	 * @param vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> selectHazardsDetail(ClassificationOfHazardsVO vo);

	List<ClassificationOfHazardsVO> selectHazardsDetailAll(ClassificationOfHazardsVO vo);

	List<ClassificationOfHazardsVO> selectHazardsDetailPopupAll(ClassificationOfHazardsVO vo);
	
	List<ImplementationOfRiskAseessmentVO> selectReferenceRiskImplList(SpSearchVO vo);

	List<String> selectRiskImplFileList(SpSearchVO vo);

	List<ImplementationOfRiskAseessmentVO> checkRemoveRiskImplList(SpSearchVO vo);
	/**
	 * 위험성평가 이행 입력
	 * @param vo
	 * @return
	 */
	int insertRiskAssImpl(ImplementationOfRiskAseessmentVO vo);
	
	/**
	 * 위험성평가 감소대책 입력
	 * @param vo
	 * @return
	 */
	int insertRiskAssImplReduction(ImplementationOfRiskAseessmentReductionVO vo);
	
	/**
	 * 위험성평가 감소대책 담당자 입력
	 * @param vo
	 * @return
	 */
	int insertRiskAssImplReductionHr(ImplementationOfRiskAseessmentReductionVO vo);
	

	/**
	 * 위험성평가 이행 전체 삭제
	 * @param vo
	 * @return
	 */
	int deleteAllRiskAssImpl(SpSearchVO vo);


	/**
	 * 위험성평가 감소대책 전체 삭제
	 * @param vo
	 * @return
	 */
	int deleteAllRiskAssImplReduction(SpSearchVO vo);
	
	/**
	 * 위험성평가 감소대책 담당자 전체 삭제
	 * @param vo
	 * @return
	 */
	int deleteAllRiskAssImplReductionHr(SpSearchVO vo);

	/**
	 * 위험성평가 감소대책 담당자 삭제
	 * @param vo
	 * @return
	 */
	int deleteRiskAssImplReductionHr(ImplementationOfRiskAseessmentReductionVO vo);

	/**
	 * 출력을 위한 데이터 조회
	 * @param vo
	 * @return
	 */
	List<Map<String, Object>> selectHazardsDetailReport(ClassificationOfHazardsVO vo);
	List<Map<String, Object>> selectAllHazardsDetailReport(ClassificationOfHazardsVO vo);
	int deleteWorkerApprovalInfoAll(UtilsVO vo);

}
