package kr.co.igns.business.improvement.dao.postgres;

import kr.co.igns.business.improvement.model.VoluntarySafetyInspectorVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VoluntarySafetyInspectorDAO {

    List<VoluntarySafetyInspectorVO> getVoluntarySafetyInspectorList(SpSearchVO searchVo);
    
    List<VoluntarySafetyInspectorVO> getVoluntarySafetyInspectorListchk(SpSearchVO searchVo);

    void insertVoluntarySafetyInspector(VoluntarySafetyInspectorVO vo);

    void updateVoluntarySafetyInspector(VoluntarySafetyInspectorVO vo);

    void deleteVoluntarySafetyInspector(VoluntarySafetyInspectorVO vo);
}
