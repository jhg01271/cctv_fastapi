package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.ClientCompVO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ClientCompDAO {
    List<ClientVO> getClientList(SpSearchVO searchVo);
    List<ClientCompVO> getCompList(SpSearchVO searchVo);
    int insertCompByClient(ClientCompVO vo);
    int deleteCompByClient(ClientCompVO vo);

}
