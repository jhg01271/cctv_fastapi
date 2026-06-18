package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.SafetyAndHealthInfoRegisterVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SafetyAndHealthInfoRegisterDAO {
    List<SafetyAndHealthInfoRegisterVO> getShInfoRegisterList(SpSearchVO searchVo);
    List<SafetyAndHealthInfoRegisterVO> getMyShInfoRegisterList(SpSearchVO searchVo);
    int insertShInfoRegister(SafetyAndHealthInfoRegisterVO vo);
    int updateShInfoRegister(SafetyAndHealthInfoRegisterVO vo);
    int deleteShInfoRegister(SafetyAndHealthInfoRegisterVO vo);
}
