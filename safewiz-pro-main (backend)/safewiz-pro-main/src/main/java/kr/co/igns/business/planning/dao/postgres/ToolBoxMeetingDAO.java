package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import kr.co.igns.business.planning.model.ToolBoxMeetingSettingVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.ToolBoxMeetingVO;

@Mapper
@Repository
public interface ToolBoxMeetingDAO {
	List<ToolBoxMeetingVO> searchData(ToolBoxMeetingVO vo);
	List<ToolBoxMeetingVO> searchDataDetail(ToolBoxMeetingVO vo);
	void insertMain(ToolBoxMeetingVO vo);
	void updateMain(ToolBoxMeetingVO vo);
	void deleteMain(ToolBoxMeetingVO vo);
	
	List<ToolBoxMeetingVO> searchPotenialRisk(ToolBoxMeetingVO vo);
	void insertPotentialRisk(ToolBoxMeetingVO vo);
	void updatePotentialRisk(ToolBoxMeetingVO vo);
	void deletePotentialRisk(ToolBoxMeetingVO vo);
	
	List<ToolBoxMeetingVO> searchKeyRisk(ToolBoxMeetingVO vo);
	void insertKeyRisk(ToolBoxMeetingVO vo);
	void updateKeyRisk(ToolBoxMeetingVO vo);
	void deleteKeyRisk(ToolBoxMeetingVO vo);
	void deleteKeyRiskPotential(ToolBoxMeetingVO vo);
	
	List<ToolBoxMeetingVO> searchSafeCheck(ToolBoxMeetingVO vo);
	void insertSafeCheck(ToolBoxMeetingVO vo);
	void updateSafeCheck(ToolBoxMeetingVO vo);
	void deleteSafeCheck(ToolBoxMeetingVO vo);
	void deleteSafeCheckPotential(ToolBoxMeetingVO vo);
	void insertSelectRiskAssessment(ToolBoxMeetingVO vo);
	void deleteSelectRiskAssessment(ToolBoxMeetingVO vo);
	
	List<ToolBoxMeetingVO> searchSelectedDatas(ToolBoxMeetingVO vo); //위험성평가 선택, 공정 선택 데이터
	
	ToolBoxMeetingVO searchReport(SpSearchVO vo);
	List<ToolBoxMeetingVO> searchReportPotentialRisk(SpSearchVO vo);
	List<ToolBoxMeetingVO> searchReportKeyRisk(SpSearchVO vo);
	List<ToolBoxMeetingVO> searchReportSafeCheck(SpSearchVO vo);
	
	List<ToolBoxMeetingVO> searchKeyRiskCount(ToolBoxMeetingVO vo);

	List<ToolBoxMeetingSettingVO> searchSetting(ToolBoxMeetingSettingVO vo);
	void insertSetting(ToolBoxMeetingSettingVO vo);
	void updateSetting(ToolBoxMeetingSettingVO vo);


	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);
}

