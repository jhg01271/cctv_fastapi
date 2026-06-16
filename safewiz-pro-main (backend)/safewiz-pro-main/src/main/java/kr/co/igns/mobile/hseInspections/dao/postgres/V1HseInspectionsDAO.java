package kr.co.igns.mobile.hseInspections.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailSaveVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsSearchVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsVO;

@Mapper
@Repository
public interface V1HseInspectionsDAO {
	// 안전점검 달력 count 조회
	HseInspectionsVO getHseInspectionsCalendarCount(HseInspectionsSearchVO hseInspectionsSearchVO);
	HseInspectionsVO getHseInspectionsCalendarCompleteCount(HseInspectionsSearchVO hseInspectionsSearchVO);
	HseInspectionsVO getHseInspectionsCalendarUnCompleteCount(HseInspectionsSearchVO hseInspectionsSearchVO);
	// 안전점검 조회
	List<HseInspectionsVO> getHseInspections(HseInspectionsSearchVO hseInspectionsSearchVO);
	// 안전점검 점검표 목록 조회
	List<HseInspectionsTemplatesVO> getHseInspectionsTemplates(HseInspectionsSearchVO hseInspectionsSearchVO);
	// 안전점검 점검표 상세 조회
	HseInspectionsTemplatesDetailVO getHseInspectionsTemplatesDetail(HseInspectionsSearchVO hseInspectionsSearchVO);
	// 안전점검 상세 조회
	HseInspectionsDetailVO getHseInspectionsDetail(HseInspectionsSearchVO hseInspectionsSearchVO);
	// 안전점검 상세 저장
	void saveSafetyInspectionLog(@Param("hseInspectionsDetailSaveVO") HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO);
	void saveSafetyInspectionLogDetail(
		@Param("hseInspectionsDetailSaveVO") HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO, 
		@Param("checkList") HseInspectionsDetailSaveVO.CheckList checkList
	);
	// 점검자 저장
	void saveChange(HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO);
	// 안전점검 상세 업데이트
	void updateSafetyInspectionLog(HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO);
	void updateSafetyInspectionLogDetail(
		@Param("hseInspectionsDetailSaveVO") HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO, 
		@Param("checkList") HseInspectionsDetailSaveVO.CheckList checkList
	);
	// 안전점검 상세 삭제
	void deleteHseInspectionsDetail(HseInspectionsSearchVO hseInspectionsSearchVO);
}
