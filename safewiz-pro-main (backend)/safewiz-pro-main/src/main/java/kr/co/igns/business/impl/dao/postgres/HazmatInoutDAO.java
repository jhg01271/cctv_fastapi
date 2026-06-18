package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HazmatInoutDAO {
    List<HazmatInoutVO> getHazmatInout(SpSearchVO searchVo);
    List<HazmatInoutDetailVO> getHazmatInoutDetail(SpSearchVO searchVo);
    List<HazmatInoutDetailVO> getHazmatInoutByMonth(SpSearchVO searchVo);

    int insertHazmatInout(HazmatInoutVO vo);
    int insertHazmatInoutDetail(HazmatInoutDetailVO vo);

    int updateHazmatInout(HazmatInoutVO vo);
    int updateHazmatInoutDetail(HazmatInoutDetailVO vo);

    int deleteHazmatInout(HazmatInoutVO vo);
    int deleteHazmatInoutDetail(HazmatInoutDetailVO vo);

    HazmatInoutVO getHazmatInoutById(HazmatInoutVO vo);
    HazmatInoutDetailVO getHazmatInoutDetailById(HazmatInoutDetailVO vo);

}

