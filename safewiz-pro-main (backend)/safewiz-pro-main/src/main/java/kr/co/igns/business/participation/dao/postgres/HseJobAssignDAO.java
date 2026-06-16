package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.participation.model.ActPlanVO;
import kr.co.igns.business.participation.model.HseJobAssignVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HseJobAssignDAO {
    //HSE 업무분장
    List<HseJobAssignVO> getJobAssign(SpSearchVO searchVo);
    HseJobAssignVO getJobAssignDetail(SpSearchVO searchVo);

    List<HseJobAssignVO> getMyJobAssign(SpSearchVO searchVo);

    int insertJobAssign(HseJobAssignVO vo);

    int updateJobAssign(HseJobAssignVO vo);

    int deleteJobAssign(HseJobAssignVO vo);

    HseJobAssignVO getJobAssignById(HseJobAssignVO vo);

    //대시보드 연결 조회시 hrId로 docNo 조회
    SpSearchVO getPermitDocNo(SpSearchVO searchVo);
}
