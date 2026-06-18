package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.PartnerVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PartnerDAO {
    List<PartnerVO> getPartnerList(SpSearchVO searchVo);
    PartnerVO getPartnerDetail(SpSearchVO searchVo);

    int insertPartner(PartnerVO vo);

    int updatePartner(PartnerVO vo);

    int deletePartner(PartnerVO vo);

    PartnerVO getPartnerById(String partCompId);

    // 거래품목 관리 관련 API
    List<PartnerVO> getPartCompItemDataset(SpSearchVO spSearchVO);
    List<PartnerVO> getSamplePartCompItemDataset(SpSearchVO spSearchVO);
    int insertPartCompItemDataset(PartnerVO vo);
    int updatePartCompItemDataset(PartnerVO vo);
    int deletePartCompItemDataset(PartnerVO vo);
}
