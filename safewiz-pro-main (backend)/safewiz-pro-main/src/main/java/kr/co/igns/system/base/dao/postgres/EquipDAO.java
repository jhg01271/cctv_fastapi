package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface EquipDAO {
    List<EquipVO> getEquip(SpSearchVO searchVo);
    EquipVO getEquipDetail(String workplaceId);
    List<EquipVO> getEquipmentByType(SpSearchVO searchVo);
    List<EquipVO> getStdEqList(SpSearchVO searchVo);
    
    int insertEquip(EquipVO vo);

    int updateEquip(EquipVO vo);

    int deleteEquip(EquipVO vo);

    EquipVO getEquipById(String workplaceId);

    List<EquipVO> getStdEquips(SpSearchVO searchVo);
    void deleteStdEquips(TypeofEquipmentVO eqVo);
    void updateStdEquips(TypeofEquipmentVO eqVo);
    void insertStdEquips(TypeofEquipmentVO eqVo);

}
