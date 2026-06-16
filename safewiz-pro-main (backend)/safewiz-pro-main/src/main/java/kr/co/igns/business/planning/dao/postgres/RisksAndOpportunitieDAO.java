package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.RisksAndOpportunitieVO;

@Mapper
@Repository
public interface RisksAndOpportunitieDAO {
	// 메인 조회
	List<RisksAndOpportunitieVO> getRisksAndOpportunities(RisksAndOpportunitieVO vo);
	// 상세 조회
	List<RisksAndOpportunitieVO> getRiskDetail(RisksAndOpportunitieVO vo);
	List<RisksAndOpportunitieVO> getOppDetail(RisksAndOpportunitieVO vo);
	List<RisksAndOpportunitieVO> getParDetail(RisksAndOpportunitieVO vo);
	// 평가기준표 api
	List<RisksAndOpportunitieVO> getAsmtApi(RisksAndOpportunitieVO vo);
	
	// 메인 저장,수정
	int insertRisksAndOpp(RisksAndOpportunitieVO vo);
	int updateRisksAndOpp(RisksAndOpportunitieVO vo);
    
    // 상세 저장,수정
    int insertRiskDetail(RisksAndOpportunitieVO vo);
    int insertOppDetail(RisksAndOpportunitieVO vo);
    int insertParDetail(RisksAndOpportunitieVO vo);
    
    int updateRiskDetail(RisksAndOpportunitieVO vo);
    int updateOppDetail(RisksAndOpportunitieVO vo);
    int updateParDetail(RisksAndOpportunitieVO vo);
    
	// 메인 삭제
    int deleteRisksAndOpportunitesMain(RisksAndOpportunitieVO vo);
    // TASK useYn='N' 처리
    int customUpdateTaskYn(@Param("reqInfoKey") String reqInfoKey);
    // 상세 삭제
    int deleteRisk(RisksAndOpportunitieVO vo);
    int deleteOpp(RisksAndOpportunitieVO vo);
    int deletePar(RisksAndOpportunitieVO vo);
	/* 출력물 */
	// 1. 리스크 관리 대장
	List<RisksAndOpportunitieVO> getReportRisk(RisksAndOpportunitieVO vo);
	List<RisksAndOpportunitieVO> getReportRisk2(RisksAndOpportunitieVO vo);
	// 2. 기회 관리 대장
	List<RisksAndOpportunitieVO> getReportOpp(RisksAndOpportunitieVO vo);
	List<RisksAndOpportunitieVO> getReportOpp2(RisksAndOpportunitieVO vo);
	// 3. 리스크 및 기회 평가 참여자
	List<RisksAndOpportunitieVO> getReportPar(RisksAndOpportunitieVO vo);
	List<RisksAndOpportunitieVO> getReportPar2(RisksAndOpportunitieVO vo);

	// 일괄출력용
	List<RisksAndOpportunitieVO> getAllReport(SpSearchVO vo);


}
