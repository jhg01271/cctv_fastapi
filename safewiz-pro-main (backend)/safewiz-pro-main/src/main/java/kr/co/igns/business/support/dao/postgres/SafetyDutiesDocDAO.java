package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.SafetyDutiesDocVO;
import kr.co.igns.business.support.model.SafetyDutiesUserVO;
import kr.co.igns.system.common.model.SpSearchVO;
import java.util.List;

public interface SafetyDutiesDocDAO {
    void delete(SafetyDutiesDocVO vo);
    void update(SafetyDutiesDocVO vo);
    void insert(SafetyDutiesDocVO vo);
    void insertMember(SafetyDutiesUserVO vo);
    void updateMember(SafetyDutiesUserVO vo);
    void deleteMember(SafetyDutiesUserVO vo);
    
    SafetyDutiesDocVO search(SpSearchVO vo);
    
    List<SafetyDutiesDocVO> searchList(SpSearchVO vo);
    List<SafetyDutiesDocVO> getSafetyDutyType(SpSearchVO vo);
    List<SafetyDutiesUserVO> filteredSearchMember(SafetyDutiesDocVO vo);
    List<SafetyDutiesUserVO> searchMember(SafetyDutiesDocVO vo);

    SafetyDutiesDocVO searchDataset(SafetyDutiesDocVO vo);
}
