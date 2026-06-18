package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContractorRegisterDAO {
    List<ContractorRegisterVO> getConstractorRegisterList(SpSearchVO searchVo);
    List<ContractorRegisterVO> getConstractorRegisterDetail(SpSearchVO searchVo);
    List<HrVO> getConstractorRegisterHrList(ContractorRegisterVO vo);
    List<ContractorRegisterVO> getSelectedConstractorRegisterList(String searchText);



}
