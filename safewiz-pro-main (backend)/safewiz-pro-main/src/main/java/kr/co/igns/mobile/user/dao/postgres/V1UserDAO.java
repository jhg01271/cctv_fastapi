package kr.co.igns.mobile.user.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.user.model.UnsignedSignaturesVO;
import kr.co.igns.mobile.user.model.UserSearchVO;

@Mapper
@Repository
public interface V1UserDAO {
	// 미서명 항목 목록 조회
	List<UnsignedSignaturesVO> getUnsignedSignaturesList(UserSearchVO vo);
}
