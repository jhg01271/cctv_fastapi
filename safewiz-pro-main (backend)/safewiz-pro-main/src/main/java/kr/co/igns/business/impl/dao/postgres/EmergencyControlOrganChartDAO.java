package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmergencyControlOrganChartDAO {
    List<EmergencyControlOrganChartVO> getEmergencyOrgChartList(SpSearchVO searchVo);
    List<EmergencyRoleVO> getEmergencyTypeList(SpSearchVO searchVo);
    List<EmergencyRoleVO> getEmergencyRoleList(EmergencyRoleVO vo);

    int confirmEmergencyOrgChart(EmergencyControlOrganChartVO vo);
    int insertEmergencyOrgChart(EmergencyControlOrganChartVO vo);
    int updateEmergencyOrgChart(EmergencyControlOrganChartVO vo);
    int deleteEmergencyOrgChart(EmergencyControlOrganChartVO vo);

    List<EmergencyRoleVO> getEmergencyType(SpSearchVO searchVo);
    List<EmergencyRoleVO> getEmergencyRole(SpSearchVO vo);
    List<EmergencyRoleVO> getEmergencyTypeDataset(SpSearchVO searchVo);
    List<EmergencyRoleVO> getEmergencyRoleDataset(EmergencyRoleVO vo);

    int deleteEmergencyType(EmergencyRoleVO vo);
    int insertEmergencyType(EmergencyRoleVO vo);
    int updateEmergencyType(EmergencyRoleVO vo);

    int deleteAllEmergencyRole(EmergencyRoleVO vo);
    int deleteEmergencyRole(EmergencyRoleVO vo);
    int insertEmergencyRole(EmergencyRoleVO vo);
    int updateEmergencyRole(EmergencyRoleVO vo);

    List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
}
