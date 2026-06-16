package kr.co.igns.business.notice.dao.postgres;

import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.business.notice.model.NoticeVO;
import kr.co.igns.business.notice.model.OutputVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.jasper.tagplugins.jstl.core.Out;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OutputDAO {

	List<OutputVO> getOutputList(SpSearchVO vo);

	List<OutputVO> getDetailList(OutputVO vo);
	int getCountTBM(OutputVO vo); // TBM

	// 위험성평가
	int getCountSAHIS(OutputVO vo); // 안전보건정보 조사
	int getCountCPE(OutputVO vo); // 공정/설비/물질 구분
	int getCountCHD(OutputVO vo); // 유해 위험요인 분류
	int getCountWOOHA(OutputVO vo); // 유해 위험요인 근로자 의견
	int getCountRORAT(OutputVO vo); // 위험성평가 교육/참여 결과
	int getCountRAP(OutputVO vo); // 위험성평가 이행

	// 비상대응 훈련
	List<String> getECOCData(OutputVO vo); // 비상통제 조직별 업무 분장
	int getCountECA(OutputVO vo); // 비상통제 조직별 업무 분장
	int getCountERT(OutputVO vo); // 비상대응 훈련

	// 조직의 상황
	List<OutputVO> getORGSTList(OutputVO vo);

	// 법규 관리 및 준수평가
	int getCountLGM(OutputVO vo);
	int getCountLGR(OutputVO vo);

	// 리스크와 기회
	List<OutputVO> getRAOList(OutputVO vo);

	// 시정조치 요구서
	List<OutputVO> getCARList(OutputVO vo);

	// 모니터링 및 측정 관리
	int getCountER(OutputVO vo);

	// 교육 훈련
	int getCountEDU(OutputVO vo);

	// HSE 목표 및 안전보건환경 예산
	int getCountPLC(OutputVO vo);
	int getCountOBJ(OutputVO vo);
	int getCountOBJM(OutputVO vo);
	int getCountOBJB(OutputVO vo);

	 // 안전보건목표 및 추진실적
	int getCountSHO(OutputVO vo);
	int getCountSHOP(OutputVO vo);

}
