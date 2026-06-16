package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.FacilityEquipManageVO;
import kr.co.igns.business.impl.model.FacilityEquipManageDetailVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.validation.Valid;

@Mapper
@Repository
public interface FacilityEquipManageDAO {
	
    List<FacilityEquipManageVO> getFacilityEquipManage(@Valid SpSearchVO searchVo);    
    List<FacilityEquipManageVO> getFacilityEquipManageDetail(@Valid FacilityEquipManageVO searchVo);
    
    int insertFacilityEquipManage(@Valid FacilityEquipManageVO vo);
    
    int updateFacilityEquipManage(@Valid FacilityEquipManageVO vo);
    int updateFacilityEquipManageUseN(@Valid FacilityEquipManageVO vo);
    
    int deleteFacilityEquipManage(@Valid FacilityEquipManageVO vo);
}
