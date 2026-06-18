package kr.co.igns.business.evaluation.dao.postgres;

import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InternalAuditDAO {
    List<InternalAuditVO> getInternalAudit(SpSearchVO searchVo);
}
