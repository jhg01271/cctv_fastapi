package kr.co.igns.system.user.dao.postgres;

import kr.co.igns.system.user.model.TokenVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TokenDAO {
    int insert(TokenVO vo);
    int update(TokenVO vo);
    int updateDelYn(@Param("uid") String uid, @Param("refreshToken") String refreshToken);
    int delete(@Param("uid") String uid, @Param("refreshToken") String refreshToken);
    List<TokenVO> search(SpSearchVO vo);
    TokenVO findOne(@Param("uid") String uid);
    TokenVO findByRefreshToken(@Param("refreshToken") String refreshToken);

    // 사용자 refresh 토큰 관련
    List<TokenVO> getUserToken(SpSearchVO vo);
    int updateUserTokenDelYn(TokenVO vo);
    // API 토큰 관련
    List<TokenVO> getApiToken(SpSearchVO vo);
    int insertApiToken(TokenVO vo);
    int updateApiDelYn(TokenVO vo);
    int countByToken(@Param("apiToken") String apiToken);

}
