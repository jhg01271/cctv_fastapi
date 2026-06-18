package kr.co.igns.business.improvement.dao.postgres;

import java.util.List;
import java.util.Map;

import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryInspectionVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryOrgnVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryRequestVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryVO;

@Mapper
@Repository
public interface SafetyMgmtOfHazardousMachineryDAO {

	void insertSafetyMgmt(SafetyMgmtOfHazardousMachineryVO safetyMgmt);

	void insertSafetyMgmtOrgn(SafetyMgmtOfHazardousMachineryOrgnVO orgn);

	void insertSafetyMgmtInspection(SafetyMgmtOfHazardousMachineryInspectionVO inspection);
	
	String getDocNo(SafetyMgmtOfHazardousMachineryVO safetyMgmt);

	void updateSafetyMgmt(SafetyMgmtOfHazardousMachineryVO safetyMgmt);

	void updateSafetyMgmtInspection(SafetyMgmtOfHazardousMachineryInspectionVO item);

	List<SafetyMgmtOfHazardousMachineryOrgnVO> getSafetyMgmtOrgnByDocNo(SafetyMgmtOfHazardousMachineryOrgnVO orgn);
	
	List<SafetyMgmtOfHazardousMachineryRequestVO> getSafetyMgmts(SpSearchVO spSearchVO);
	List<SafetyMgmtOfHazardousMachineryRequestVO> getSafetyMgmtsByDocNo(Map<String, Object> params);

	SafetyMgmtOfHazardousMachineryVO getSafetyMgmt(SafetyMgmtOfHazardousMachineryVO vo);

	List<SafetyMgmtOfHazardousMachineryOrgnVO> getSafetyMgmtOrgns(SafetyMgmtOfHazardousMachineryVO safetyMgmt);

	List<SafetyMgmtOfHazardousMachineryInspectionVO> getSafetyMgmtInspections(SafetyMgmtOfHazardousMachineryVO safetyMgmt);
	
	int deleteSafetyMgmt(SafetyMgmtOfHazardousMachineryVO vo);

	void deleteSafetyMgmtInspection(SafetyMgmtOfHazardousMachineryInspectionVO deleteList);

	void deleteSafetyMgmtOrgn(String docSeq);
}
