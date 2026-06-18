package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SafetyInspectionLogDAO {
    List<SafetyInspectionLogVO> getSafetyInspectionLogList(SpSearchVO searchVo);
    SafetyInspectionLogVO getSafetyInspectionLog(SafetyInspectionLogSearchVO searchVo);
    List<SafetyInspectionLogDetailVO> getSafetyInspectionLogDetail(SafetyInspectionLogSearchVO searchVo);
    SafetyInspectionLogVO getEquipInfo(SafetyInspectionLogSearchVO searchVo);
    List<EquipVO> getSafetyInspectionStdEqList(SpSearchVO searchVo);
    List<String> getSafetyInspectionLogDates(SafetyInspectionLogSearchVO searchVo);

    int insertSafetyInspectionLog(SafetyInspectionLogVO vo);
    int insertSafetyInspectionLogDetail(SafetyInspectionLogDetailVO vo);

    int updateSafetyInspectionLog(SafetyInspectionLogVO vo);
    int updateSafetyInspectionLogDetail(SafetyInspectionLogDetailVO vo);

    int deleteSafetyInspectionLog(SafetyInspectionLogVO vo);
    int deleteSafetyInspectionLogDetail(SafetyInspectionLogVO vo);

    SafetyInspectionLogVO getSafetyInspectionLogById(SafetyInspectionLogVO vo);
    SafetyInspectionLogDetailVO getSafetyInspectionLogDetailById(SafetyInspectionLogDetailVO vo);
}

