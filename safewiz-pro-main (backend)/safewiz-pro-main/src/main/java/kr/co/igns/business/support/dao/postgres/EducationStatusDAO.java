package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.EducationStatusVO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EducationStatusDAO {
    List<EducationStatusVO> getEducationStatus(SpSearchVO searchVo);
}
