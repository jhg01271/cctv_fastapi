package kr.co.igns.business.evaluation.service;

import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.business.evaluation.dao.postgres.InternalAuditDAO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalAuditService {
    private final InternalAuditDAO internalAuditDAO;

    public List<InternalAuditVO> getInternalAudit(SpSearchVO searchVo) throws Exception {
        List<InternalAuditVO> voList = internalAuditDAO.getInternalAudit(searchVo);
        return voList;
    }
}
