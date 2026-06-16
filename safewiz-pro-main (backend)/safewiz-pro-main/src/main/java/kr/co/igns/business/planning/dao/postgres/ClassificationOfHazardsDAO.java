package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.ClassificationOfHazardsVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ClassificationOfHazardsDAO {
	/**
	 * 유해위험요인 분류 조회
	 * 
	 * @param vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> getHazardsList(SpSearchVO vo);
	List<ClassificationOfHazardsVO> selectHazardsList(ClassificationOfHazardsVO Vo);
	/**
	 * 위험성평가표 상세 조회
	 * 
	 * @param Vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> selectHazardDetail(SpSearchVO Vo);
	

	/**
	 * 공정,차수 중복되는 게 있는 지 확인
	 * 
	 * @param vo
	 * @return
	 */
	ClassificationOfHazardsVO getHazardDupe(ClassificationOfHazardsVO Vo);
	
	/**
	 * 유해위험요인 분류상세 공정 조회
	 * 
	 * @param vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> getProcessList(SpSearchVO vo);

	/**
	 * 유해위험요인 분류상세 공정에 해당하는 설비,위험물질 조회
	 * 
	 * @param processId
	 * @return
	 */
	List<ClassificationOfHazardsVO> getProcessEquipmentAndMsds(String processId);

	/**
	 * 유해위험요인 분류상세 수정시 공정에 해당하는 유해위험요인목록 조회
	 * 
	 * @param vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> getSavedFactorList(ClassificationOfHazardsVO vo);

	/**
	 * 출력시 유해위험분류 조회
	 * 
	 * @param vo
	 * @return
	 */
	List<ClassificationOfHazardsVO> getPrintFactorList(SpSearchVO Vo);

	/**
	 * 유해위험요인 분류상세 공정 저장
	 * 
	 * @param vo
	 * @return
	 */
	int saveHazards(ClassificationOfHazardsVO vo);

	/**
	 * 유해위험요인 분류상세 저장
	 * 
	 * @param vo
	 * @return
	 */
	int saveHazardsList(ClassificationOfHazardsVO vo);

	/**
	 * 유해위험요인 마스터 정보 수정
	 * 
	 * @param vo
	 * @return
	 */
	int updateHazards(ClassificationOfHazardsVO vo);

	/**
	 * 유해위험요인 삭제
	 * 
	 * @param vo
	 * @return
	 */
	int deleteHazards(ClassificationOfHazardsVO vo);

	/**
	 * 유해위험요인 삭제
	 * 
	 * @param vo
	 * @return
	 */
	int deleteHazardsMaster(ClassificationOfHazardsVO vo);

	/**
	 * 유해위험요인 상세 삭제
	 * 
	 * @param vo
	 * @return
	 */
	int deleteHazardsDetail(ClassificationOfHazardsVO vo);

	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);

	List<ClassificationOfHazardsVO> getClassData(SpSearchVO vo);
	List<ClassificationOfHazardsVO> getSampleClassData(SpSearchVO vo);
	void insertClassData(ClassificationOfHazardsVO vo);
	void updateClassData(ClassificationOfHazardsVO vo);
	void deleteClassData(ClassificationOfHazardsVO vo);

}
