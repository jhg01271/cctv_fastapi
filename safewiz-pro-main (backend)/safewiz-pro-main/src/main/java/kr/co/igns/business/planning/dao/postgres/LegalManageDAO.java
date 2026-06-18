package kr.co.igns.business.planning.dao.postgres;

import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface LegalManageDAO {
    List<LegalManageVO> getLegalManageList(LegalManageVO vo);
    
    List<LegalManageVO> getLegalManageTypeList(LegalManageVO vo);
    
    List<LegalManageVO> getDatasetLegalManageType(SpSearchVO vo);
    
    List<LegalManageVO> getLegalManageListPopup(LegalManageVO vo);
    
    List<LegalManageVO> getLegalManageDetailMasterList(LegalManageVO vo);
        
    List<LegalManageDetailVO> getLegalManageDetail(SpSearchVO searchVo);

    String getDocNo(LegalManageVO searchVo);
    
    Integer getLegalNm(LegalManageVO vo);
    List<LegalManageVO> getValidLegalDivFg(LegalManageVO vo);
    
    void insertLegalManage(LegalManageVO reqVo);
    void updateLegalManage(LegalManageVO reqVo);
    
    int confirmLegalManage(LegalManageVO reqVo);
    
    void insertLegalManageDetail(Map<String, Object> reqVo);
    void updateLegalManageDetail(Map<String, Object> reqVo);

    void insertLegalManageType(LegalManageVO reqVo);
    void updateLegalManageType(LegalManageVO reqVo);
    
    void deleteLegalManage(LegalManageVO reqVo);
    void deleteLegalManageType(LegalManageVO reqVo);
    void deleteLegalManageDetail(LegalManageDetailVO reqVo);

    // 출력물 용 데이터 조회
    LegalManageVO getReportData(LegalManageVO searchVo);
 	List<LegalManageDetailVO>  getReportGridData(LegalManageVO searchVo);
 	List<LegalManageDetailVO>  getReportGridDataAll(SpSearchVO searchVo);


    List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
    List<SpSearchVO> getAllReportDetail(SpSearchVO spSearchVO);
    LegalManageDetailVO getReportData1(SpSearchVO spSearchVO);


}
