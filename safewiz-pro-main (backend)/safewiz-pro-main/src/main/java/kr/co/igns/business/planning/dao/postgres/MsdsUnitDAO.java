package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.MsdsHrVO;
import kr.co.igns.business.planning.model.MsdsLawVO;
import kr.co.igns.business.planning.model.MsdsUnitVO;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface MsdsUnitDAO {
    List<MsdsUnitVO> getMsdsUnit(SpSearchVO searchVo);
    List<MsdsUnitVO> getMsdsUnitDataset(SpSearchVO searchVo);
    
    void insertMsdsUnit(MsdsUnitVO vo);

    void updateMsdsUnit(MsdsUnitVO vo);

    void deleteMsdsUnit(MsdsUnitVO vo);
   


    MsdsUnitVO getMsdsUnitById(MsdsUnitVO unitId);
}
