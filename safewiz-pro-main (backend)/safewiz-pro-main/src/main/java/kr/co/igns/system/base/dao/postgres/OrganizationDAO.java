package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.OrgChartVO;
import kr.co.igns.system.base.model.OrganizationVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrganizationDAO {
    List<OrganizationVO> getOrganizationList(SpSearchVO searchVo);
    OrganizationVO getOrganizationDetail(SpSearchVO searchVo);

    int insertOrganization(OrganizationVO vo);

    int updateOrganization(OrganizationVO vo);

    int deleteOrganization(OrganizationVO vo);

    OrganizationVO getOrgnById(String orgnId);

    //조직 맵 조회 - 전체
    List<BaseMapVO> getOrgnMap(BaseMapVO vo);
    //조직 맵 조회 - useYn = Y인 것만 조회
    List<BaseMapVO> getOrgnMapByUseYn(BaseMapVO vo);
    List<BaseMapVO> getOrgnMapByUseYnBatch(@Param("targetType") String targetType, @Param("targetIds") List<String> targetIds);

    //조직 맵 데이터 추가
    int addOrgnMap(BaseMapVO vo);
    //조직 맵 데이터 수정
    int updateOrgnMap(BaseMapVO vo);
    //조직 맵 데이터 수정(useyn만 업데이트)
    int updateUseYnOrgnMap(BaseMapVO vo);
    //조직 맵 데이터  삭제
    int deleteOrgnMap(BaseMapVO vo);

    int insertOrganizationChart(OrgChartVO vo);

    int updateOrganizationChart(OrgChartVO vo);
    
    int deleteOrganizationChart(OrgChartVO vo);

    int confirmOrganizationChart(OrgChartVO vo);

    OrgChartVO getChartDetail(String chartId);
    
    List<OrgChartVO> getChartList(SpSearchVO searchVo);
}
