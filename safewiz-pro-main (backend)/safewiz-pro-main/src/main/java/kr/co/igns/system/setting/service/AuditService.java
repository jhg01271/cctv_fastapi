package kr.co.igns.system.setting.service;

import java.util.List;

import javax.transaction.Transactional;

import kr.co.igns.system.setting.dao.postgres.AuditDAO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.springframework.stereotype.Service;

import kr.co.igns.system.setting.model.AuditVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AuditService {
	private final AuditDAO auditDAO;

	public BaseVO create(AuditVO reqVo) throws Exception {
		auditDAO.insert(reqVo);
		return reqVo;

	}
	public List<AuditVO> search(SpSearchVO searchVO) throws Exception {
		List<AuditVO> voList = auditDAO.search(searchVO);
		return voList;
	}

}
