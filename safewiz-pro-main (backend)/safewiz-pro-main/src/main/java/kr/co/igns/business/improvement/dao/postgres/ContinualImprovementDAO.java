package kr.co.igns.business.improvement.dao.postgres;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.improvement.model.ContinualImprovementVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ContinualImprovementDAO {

	List<ContinualImprovementVO> getContinualImprovementTitleMenu(SpSearchVO searchVo);
}
