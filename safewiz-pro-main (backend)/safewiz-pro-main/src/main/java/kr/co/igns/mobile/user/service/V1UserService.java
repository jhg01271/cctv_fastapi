package kr.co.igns.mobile.user.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.user.dao.postgres.V1UserDAO;
import kr.co.igns.mobile.user.model.UnsignedSignaturesVO;
import kr.co.igns.mobile.user.model.UserSearchVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1UserService {
	private final V1UserDAO v1UserDAO;
	
	public List<UnsignedSignaturesVO> getUnsignedSignaturesList(@Valid UserSearchVO userSearchVO) {
		userSearchVO.setHrId(SecurityUtil.getCurrentHrId());
		List<UnsignedSignaturesVO> voList = v1UserDAO.getUnsignedSignaturesList(userSearchVO);
		
		return voList;
	}
}
