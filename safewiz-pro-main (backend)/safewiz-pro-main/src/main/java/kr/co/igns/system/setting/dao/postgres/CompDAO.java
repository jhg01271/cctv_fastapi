package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompDAO {
    List<CompVO> getCompList(SpSearchVO searchVo);
    List<CompVO> getCompListByClnt(SpSearchVO searchVo);
    List<CompVO> getCompListByUserId(SpSearchVO searchVo);
    List<CompVO> getCompListByMaster(SpSearchVO searchVo);
    List<CompVO> getCompListByHr(SpSearchVO searchVo);
    
    CompVO getCompDetail(String compId);
    List<SpSearchVO> getCompClntDetail(String compId);

    int searchCount(SpSearchVO searchVo);

    int insertComp(CompVO vo);

    int updateComp(CompVO vo);

    int deleteComp(CompVO vo);

    CompVO getCompById(CompVO vo);
}
