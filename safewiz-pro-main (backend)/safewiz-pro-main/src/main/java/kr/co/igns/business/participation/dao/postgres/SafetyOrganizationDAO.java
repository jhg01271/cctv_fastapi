package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.participation.model.SafetyOrgnRoleVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SafetyOrganizationDAO {

    int insertOrganizationChart(SafetyOrgChartVO vo);

    int updateOrganizationChart(SafetyOrgChartVO vo);
    
    int deleteOrganizationChart(SafetyOrgChartVO vo);

    int confirmOrganizationChart(SafetyOrgChartVO vo);

    int insertSafetyOrgList(SafetyOrgnRoleVO vo);

    int updateSafetyOrgList(SafetyOrgnRoleVO vo);

    SafetyOrgChartVO getChartDetail(String chartId);

    List<SafetyOrgnRoleVO> getSafetyOrgList(SpSearchVO searchVo);
    List<SafetyOrgnRoleVO> getDataSetSafetyOrgList(SpSearchVO searchVo);
    List<SafetyOrgChartVO> getChartList(SpSearchVO searchVo);

    List<HrVO> getSafetyOrgnListById(SpSearchVO searchVo);
}
