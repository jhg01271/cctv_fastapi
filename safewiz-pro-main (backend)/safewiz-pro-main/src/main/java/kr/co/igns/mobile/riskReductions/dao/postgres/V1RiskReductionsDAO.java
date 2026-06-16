package kr.co.igns.mobile.riskReductions.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.riskReductions.model.RiskAssessmentVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsDetailVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsSaveVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsSearchVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsVO;

@Mapper
@Repository
public interface V1RiskReductionsDAO {
	// 위험성 평가 목록 조회
	List<RiskAssessmentVO> getRiskAssessments(RiskReductionsSearchVO vo);
	// 감소대책 목록 조회
	List<RiskReductionsVO> getRiskReductions(RiskReductionsSearchVO vo);
	// 감소대책 상세 조회
	RiskReductionsDetailVO getRiskReductionsDetail(RiskReductionsSearchVO vo);
	// 감소대책 개선조치 등록/변경
	void saveRiskReductionsDetail(RiskReductionsSaveVO vo);
}
