package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.RiskAndOpsAsmtCriteriaVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface RiskAndOpsAsmtCriteriaDAO {
	
	//리스크와 기회 첫페이지 조회
	List<RiskAndOpsAsmtCriteriaVO> getRiskMain(SpSearchVO searchVo);
	//메인 조회
	List<RiskAndOpsAsmtCriteriaVO> getRiskAndOpsAsmtCriteria(SpSearchVO searchVo);
	//신규 로우 추가시 차수 max
	String getMaxAsmtCriteria(String compId);
	//디테일 조회
	List<RiskAndOpsAsmtCriteriaVO> getRiskAndOpsAsmtCriteriaDetailList(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//데이터셋
	List<RiskAndOpsAsmtCriteriaVO> getDatasetAsmtList(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//메인 저장
	int insertRiskAndOpsAsmtCriteriaMain(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//디테일 저장
	int insertRiskAndOpsAsmtCriteriaDetail(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//디테일 수정
	int updateRiskAndOpsAsmtCriteriaDetail(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//디테일에서 메인 사용여부 저장
	int saveMainUseYn(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//메인 확정여부 저장
	int saveMainConfirm(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//메인 삭제
	int deleteRiskAndOpsAsmtCriteriaMain(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//디테일 삭제
	int deleteRiskAndOpsAsmtCriteriaDetail(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
	//리스크등급기준 이전데이터삭제
	int deleteAsmtGrade(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO);
}
