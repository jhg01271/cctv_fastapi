package kr.co.igns.business.impl.dao.postgres;

import java.util.List;

import kr.co.igns.business.impl.model.MgmtOfChangeVO;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.system.common.model.SpSearchVO;

public interface MgmtOfChangeDAO {


    List<MgmtOfChangeVO> getMgmtOfChange(SpSearchVO vo);
	MgmtOfChangeVO getMgmtOfChangeDetail(SpSearchVO vo);

    void insertMgmtOfChange(MgmtOfChangeVO vo);
	void updateMgmtOfChange(MgmtOfChangeVO vo);
	void deleteMgmtOfChange(SpSearchVO vo);
}
