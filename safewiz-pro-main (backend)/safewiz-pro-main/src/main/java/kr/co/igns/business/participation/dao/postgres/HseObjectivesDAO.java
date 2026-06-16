package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.participation.model.HseObjectivesVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HseObjectivesDAO {
    List<HseObjectivesVO> getHseObjectives(SpSearchVO searchVo);
    List<HseObjectivesVO> getHseOrganizationChart(SpSearchVO searchVo);
}
