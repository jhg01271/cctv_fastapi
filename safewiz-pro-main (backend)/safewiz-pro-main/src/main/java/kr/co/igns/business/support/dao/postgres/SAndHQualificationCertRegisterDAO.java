package kr.co.igns.business.support.dao.postgres;

import kr.co.igns.business.support.model.SAndHQualificationCertRegisterVO;
import kr.co.igns.system.common.model.SpSearchVO;
import java.util.List;
import java.util.Map;

public interface SAndHQualificationCertRegisterDAO {
    void delete(SAndHQualificationCertRegisterVO vo);
    void update(SAndHQualificationCertRegisterVO vo);
    void insert(SAndHQualificationCertRegisterVO vo);
    
    SAndHQualificationCertRegisterVO search(SAndHQualificationCertRegisterVO vo);
    List<SAndHQualificationCertRegisterVO> searchList(SpSearchVO vo);
    List<SAndHQualificationCertRegisterVO> searchListReport(Map<String, Object> searchList);
}
