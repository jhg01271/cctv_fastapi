package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import kr.co.igns.system.setting.model.ClientVO;

import java.util.List;

@Mapper
@Repository
public interface ClientDAO {
    List<ClientVO> getClientList(SpSearchVO searchVo);
    ClientVO getClientDetail(String clntId);

    int searchCount(SpSearchVO searchVo);

    int insertClient(ClientVO vo);

    int updateClient(ClientVO vo);

    int deleteClient(ClientVO vo);

    ClientVO getClientById(String clntId);
}
