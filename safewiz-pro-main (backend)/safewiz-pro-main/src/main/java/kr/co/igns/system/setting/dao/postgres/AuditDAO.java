package kr.co.igns.system.setting.dao.postgres;

import kr.co.igns.system.setting.model.AuditVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface AuditDAO {
    int insert(AuditVO vo);

    List<AuditVO> search(SpSearchVO vo);
}
