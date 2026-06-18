package kr.co.igns.business.planning.dao.postgres;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsAnalysisVO;
import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ClassificationProcessEquipMsdsDAO {
	
	/**
	 * 조직 별 공정 리스트 조회
	 * @param vo
	 * @return
	 */
	List<SafetyAndHealthInfoSurveyVO> selectClassProcList(ClassificationProcessEquipMsdsVO searchVo);

	/**
	 * 공정 별 아이템(equip, msds) 조회
	 * @param params
	 * @return
	 */
	List<SafetyAndHealthInfoSurveyVO> selectPrcsItemList(Map<String, Object> params);
	
	/**
	 * 공정/설비/물질 구분 목록 조회
	 * @param vo
	 * @return
	 */
	List<ClassificationProcessEquipMsdsVO> selectClassProcEquipMsdsList(SpSearchVO vo);
	
	/**
	 * 공정/설비/물질 구분 상세 조회
	 * @param vo
	 * @return
	 */
	ClassificationProcessEquipMsdsVO selectClassProcEquipMsdsDetail(ClassificationProcessEquipMsdsVO vo);
	
    /**
     * 공정/설비/물질 구분 저장
     * @param vo
     * @return
     */
    int insertClassificationProcessEquipMsds(ClassificationProcessEquipMsdsVO vo);
    
    /**
     * 참여자 저장
     * @param list
     * @return
     */
    int insertClassificationProcessEquipMsdsParticipant(ClassificationProcessEquipMsdsVO list);
    
    /**
     * 공정 저장
     * @param list
     * @return
     */
    int insertClassProcAnalysis(ClassificationProcessEquipMsdsAnalysisVO list);
    
    /**
     * 공정에 대한 equip, msds 저장
     * @param list
     * @return
     */
    int insertClassProcEquipMsds(ClassificationProcessEquipMsdsAnalysisVO list);
    
    /**
     * 공정/설비/물질 구분 수정
     * @param list
     * @return
     */
    int updateClassProcEquipMsds(ClassificationProcessEquipMsdsVO list);

	/**
	 * 공정/설비/물질 구분 차수 변경
	 * @param list
	 * @return
	 */
	int confirmClassProc(ClassificationProcessEquipMsdsVO list);
	
    /**
     * 공정/설비/물질 구분 삭제
     * @param list
     * @return
     */
    int deleteClassProc(ClassificationProcessEquipMsdsVO list);

    /**
     * 참여자 삭제
     * @param list
     * @return
     */
    int deleteClassProcParticipant(ClassificationProcessEquipMsdsVO list);
    
    /**
     * 공정 삭제
     * @param list
     * @return
     */
    int deleteClassProcAnalysis(ClassificationProcessEquipMsdsVO list);
    
    /**
     * 공정관련 아이템(equip, msds) 삭제
     * @param list
     * @return
     */
    int deleteClassProcEquipMsds(ClassificationProcessEquipMsdsVO list);
	
	
	// 일괄출력용
	List<String> getAllReport(SpSearchVO vo);
	
	
}
