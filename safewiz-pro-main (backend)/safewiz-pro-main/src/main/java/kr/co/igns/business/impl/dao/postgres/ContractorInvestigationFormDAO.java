package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.ContractorInvestigationFormDetailVO;
import kr.co.igns.business.impl.model.ContractorInvestigationFormVO;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContractorInvestigationFormDAO {
    List<ContractorInvestigationFormVO> getInvestigationFormList(SpSearchVO searchVo);
    List<ContractorInvestigationFormDetailVO> getInvestigationFormDetail(ContractorInvestigationFormVO vo);

    List<ContractorInvestigationFormDetailVO> getFinalUseInspectionType(SpSearchVO searchVo);
    int insertInvestigationForm(ContractorInvestigationFormVO vo);
    int updateInvestigationForm(ContractorInvestigationFormVO vo);
    int insertInvestigationFormDetail(ContractorInvestigationFormDetailVO vo);
    int updateInvestigationFormToPartCompId(ContractorInvestigationFormVO vo);
    int updateInvestigationFormDetail(ContractorInvestigationFormDetailVO vo);
    int deleteInvestigationForm(ContractorInvestigationFormVO vo);
    int deleteInvestigationFormDetail(ContractorInvestigationFormVO vo);

    ContractorInvestigationFormVO getPassScore(ContractorInvestigationFormVO vo);
    int savePassScore(ContractorInvestigationFormVO vo);
    int updatePassScore(ContractorInvestigationFormVO vo);


    // 점검사항 관리 관련 API
    List<ContractorInvestigationFormDetailVO> getInspectionType(SpSearchVO searchVo);
    List<ContractorInvestigationFormDetailVO> getInspectionTypeDetail(ContractorInvestigationFormDetailVO vo);
    List<ContractorInvestigationFormDetailVO> getInspectionTypeDataset(SpSearchVO searchVo);
    List<ContractorInvestigationFormDetailVO> getInspectionTypeDatasetDetail(ContractorInvestigationFormDetailVO vo);
    int insertInspectionType(ContractorInvestigationFormDetailVO vo);
    int updateInspectionType(ContractorInvestigationFormDetailVO vo);
    int deleteInspectionType(ContractorInvestigationFormDetailVO vo);
    int insertInspectionTypeDetail(ContractorInvestigationFormDetailVO vo);
    int updateInspectionTypeDetail(ContractorInvestigationFormDetailVO vo);
    int deleteInspectionTypeDetail(ContractorInvestigationFormDetailVO vo);

}
