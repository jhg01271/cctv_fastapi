package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.PlanningCtrlVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlanningCtrlDAO {
    List<PlanningCtrlVO> getPlanningCtrl(SpSearchVO searchVo);
}
