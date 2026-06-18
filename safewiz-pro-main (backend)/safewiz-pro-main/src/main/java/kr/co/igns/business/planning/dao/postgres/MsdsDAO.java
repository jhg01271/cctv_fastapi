package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.MsdsHrVO;
import kr.co.igns.business.planning.model.MsdsLawVO;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface MsdsDAO {
    List<MsdsVO> getMsds(SpSearchVO searchVo);
    MsdsVO getMsdsDetail(String workplaceId);
    
    List<MsdsLawVO> getAct(SpSearchVO vo);
    List<MsdsHrVO> getHrList(SpSearchVO vo);
    
    void insertMsds(MsdsVO vo);
    void insertMsdsLaw(MsdsLawVO vo);
    void insertMsdsHr(MsdsHrVO vo);

    void updateMsds(MsdsVO vo);

    void deleteMsds(MsdsVO vo);
   
    void deleteLaw(String msdsId);
    void deleteHr(String msdsId);

    MsdsVO getMsdsById(String workplaceId);
}
