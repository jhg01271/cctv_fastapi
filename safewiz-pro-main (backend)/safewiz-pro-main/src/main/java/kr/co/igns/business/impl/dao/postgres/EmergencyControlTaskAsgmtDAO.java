package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.EmergencyControlTaskAsgmtVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmergencyControlTaskAsgmtDAO {
    List<EmergencyControlTaskAsgmtVO> getEmergencyControlTaskAsgmtList(SpSearchVO searchVo);
    List<EmergencyControlTaskAsgmtVO> getEmergencyControlTaskAsgmtRoleList(EmergencyControlTaskAsgmtVO vo);
    List<EmergencyControlTaskAsgmtVO> getEmergencyControlTaskAsgmtHrList(EmergencyControlTaskAsgmtVO vo);
    int deleteEmergencyControlTaskAsgmt(EmergencyControlTaskAsgmtVO vo);

    EmergencyControlTaskAsgmtVO getOrgnChartData(SpSearchVO searchVo);

    int insertEmergencyControlTaskAsgmt(EmergencyControlTaskAsgmtVO vo);
    int updateEmergencyControlTaskAsgmt(EmergencyControlTaskAsgmtVO vo);

    int insertEmergencyControlTaskAsgmtRole(EmergencyControlTaskAsgmtVO vo);
    int updateEmergencyControlTaskAsgmtRole(EmergencyControlTaskAsgmtVO vo);
    int deleteEmergencyControlTaskAsgmtHr(EmergencyControlTaskAsgmtVO vo);
    int insertEmergencyControlTaskAsgmtHr(EmergencyControlTaskAsgmtVO vo);

    // 일괄출력용
    List<SpSearchVO> getAllReport(SpSearchVO searchVo);
}
