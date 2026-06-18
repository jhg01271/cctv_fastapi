package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.participation.model.HsePolicySuggestionVO;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HsePolicyDAO {
    List<HsePolicyVO> getHsePolicyList(SpSearchVO searchVo);

    HsePolicyVO getHsePolicyDetail(SpSearchVO searchVo);

    HsePolicyVO getSampleHsePolicy(SpSearchVO searchVo);

    void insertHsePolicy(HsePolicyVO reqVo);

    void updateHsePolicy(HsePolicyVO reqVo);

    void updateHsePolicyConfirmYn(HsePolicyVO reqVo);

    void deleteHsePolicy(HsePolicyVO reqVo);

    String getCeoHrId();
    String getCeoHrNm();

    List<HsePolicySuggestionVO> getHsePolicySug(SpSearchVO searchVo);

    void insertHsePolicySug(HsePolicySuggestionVO hsePolicySuggestionVO);

    void updateHsePolicySug(HsePolicySuggestionVO hsePolicySuggestionVO);

    void deleteHsePolicySug(HsePolicySuggestionVO hsePolicySuggestionVO);

    // 일괄출력용
    List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
}
