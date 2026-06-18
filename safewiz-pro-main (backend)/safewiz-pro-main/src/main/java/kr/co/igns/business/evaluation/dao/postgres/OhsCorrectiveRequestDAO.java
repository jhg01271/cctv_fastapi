package kr.co.igns.business.evaluation.dao.postgres;

import java.util.List;

import kr.co.igns.business.evaluation.model.OhsCorrectiveRequestVO;
import kr.co.igns.system.common.model.SpSearchVO;

public interface OhsCorrectiveRequestDAO {
    void updateCorrectiveRequest(OhsCorrectiveRequestVO vo);
    void insertCorrectiveRequest(OhsCorrectiveRequestVO vo);
    void deleteCorrectiveRequest(SpSearchVO vo);
    List<OhsCorrectiveRequestVO> getCorrectiveRequest(SpSearchVO vo);
    OhsCorrectiveRequestVO getCorrectiveRequestDetail(SpSearchVO vo);
}
