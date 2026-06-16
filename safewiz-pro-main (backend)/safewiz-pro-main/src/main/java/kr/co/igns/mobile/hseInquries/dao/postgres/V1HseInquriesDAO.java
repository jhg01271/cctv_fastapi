package kr.co.igns.mobile.hseInquries.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.hseInquries.model.HseInquriesActionVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSaveVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSearchVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesVO;

@Mapper
@Repository
public interface V1HseInquriesDAO {
	// 안전점검 상담 목록 조회
	List<HseInquriesVO> getHseInquriesList(HseInquriesSearchVO vo);
	// 안전점검 상담 상세 조회
	HseInquriesVO getHseInquriesDetail(HseInquriesSearchVO vo);
	// 안전점검 상담 등록
	void saveHseInquries(HseInquriesSaveVO vo);
	// 안전보건 상담 변경
	void updateHseInquries(HseInquriesSaveVO vo);
	// 안전보건 상담 삭제
	void deleteHseInquries(HseInquriesSearchVO vo);
	// 안전보건 상담 조치 등록/변경
    void updateHseInquriesAction(HseInquriesActionVO vo);
}
