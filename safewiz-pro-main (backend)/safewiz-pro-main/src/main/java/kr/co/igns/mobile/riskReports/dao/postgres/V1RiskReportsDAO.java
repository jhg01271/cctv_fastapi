package kr.co.igns.mobile.riskReports.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.riskReports.model.RiskReportsCountVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsDetailVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSaveVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSaveVO.Experiences;
import kr.co.igns.mobile.riskReports.model.RiskReportsSearchVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsVO;

@Mapper
@Repository
public interface V1RiskReportsDAO {
	// 근로자 참여 누적횟수 조회
	public RiskReportsCountVO getRiskReportsCount(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 목록 조회
	public List<RiskReportsVO> getRiskReports(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 상세 조회
	public RiskReportsDetailVO getRiskReportsDetail(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 서명 등록
	public void saveRiskReportsSignatures(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 서명 취소
	public void deleteRiskReportsSignatures(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여의 검토자 의견 변경
	public void updateRiskReportsReplies(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 검토자 저장
	public void saveRiskReportsReviewer(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 삭제
	public void deleteRiskReports(RiskReportsSearchVO riskReportsSearchVO);
	// 근로자 참여 저장
	public void saveRiskReports(RiskReportsSaveVO riskReportsSaveVO);
	// 근로자 참여 업데이트
	public void updateRiskReports(RiskReportsSaveVO riskReportsSaveVO);
	// 경험담 업데이트 
	public void updateExperiences(@Param("riskReportsSaveVO") RiskReportsSaveVO riskReportsSaveVO, @Param("experiences") List<Experiences> experiences);
	// 경험담 삭제
	public void deleteExperiences(@Param("riskReportsSaveVO") RiskReportsSaveVO riskReportsSaveVO, @Param("experiences") List<String> experiences);
	// 경험담저장
	public void saveExperiences(@Param("riskReportsSaveVO") RiskReportsSaveVO riskReportsSaveVO, @Param("experiences") List<String> experiences);
	// 검토자 삭제
	public void deleteReviewer(RiskReportsSaveVO riskReportsSaveVo);
	// 작성자 저장
	public void saveWriter(RiskReportsSaveVO riskReportsSaveVo);
	// 검토자 저장
	public void saveReviewer(RiskReportsSaveVO riskReportsSaveVo);

}
