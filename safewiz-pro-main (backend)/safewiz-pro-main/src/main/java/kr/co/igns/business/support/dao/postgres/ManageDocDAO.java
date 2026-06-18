package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.ManageDocVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ManageDocDAO {
    List<ManageDocVO> getManageDoc(SpSearchVO searchVo);
    List<ManageDocVO> getManageDocDetail(SpSearchVO searchVo);

    int insertManageDoc(ManageDocVO vo);

    int updateManageDoc(ManageDocVO vo);

    int deleteManageDoc(ManageDocVO vo);

    ManageDocVO getManageDocById(ManageDocVO vo);
}
