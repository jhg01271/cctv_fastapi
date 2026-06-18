package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SafetyChecklistDAO {
    int  hasSafetyChecklist(SpSearchVO searchVo);

    List<SafetyChecklistVO> getSafetyChecklist(SpSearchVO searchVo);
    SafetyChecklistVO getSafetyChecklistDetail(SpSearchVO searchVo);
    List<SafetyChecklistItemVO> getSafetyChecklistItem(SpSearchVO searchVo);
    List<SafetyChecklistDetailVO> getSafetyChecklistDetailList(SpSearchVO searchVo);
    List<SafetyChecklistHistoryVO> getSafetyChecklistHistoryList(SpSearchVO searchVo);

    int insertSafetyChecklist(SafetyChecklistVO vo);
    int insertSafetyChecklistItem(SafetyChecklistItemVO vo);
    int insertSafetyChecklistDetail(SafetyChecklistDetailVO vo);

    int updateSafetyChecklist(SafetyChecklistVO vo);
    int updateSafetyChecklistItem(SafetyChecklistItemVO vo);
    int updateSafetyChecklistDetail(SafetyChecklistDetailVO vo);
    int insertSafetyCheckListHistory(SafetyChecklistVO vo);

    int deleteSafetyChecklist(SafetyChecklistVO vo);
    int deleteSafetyChecklistItem(SafetyChecklistItemVO vo);
    int deleteSafetyChecklistDetail(SafetyChecklistDetailVO vo);

    SafetyChecklistVO getSafetyChecklistById(SafetyChecklistVO vo);
    SafetyChecklistItemVO getSafetyChecklistItemById(SafetyChecklistItemVO vo);
    SafetyChecklistDetailVO getSafetyChecklistDetailById(SafetyChecklistDetailVO vo);

    //안전점검 담당자 관리
    List<SafetyChecklistHrVO> getSafetyCheckInspector(SpSearchVO searchVo);
    List<SafetyChecklistHrVO> getEqInspector(SpSearchVO searchVo);
    List<HrVO> getSafetyCheckInspectorByEq(SafetyChecklistHrVO vo);
    int insertSafetyCheckInspector(HrVO vo);
    int deleteSafetyCheckInspector(HrVO vo);
}

