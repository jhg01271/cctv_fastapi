package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.WHMgmtGuideVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WHMgmtGuideDAO {
    List<WHMgmtGuideVO> getWHMgmtGuide(SpSearchVO searchVo);
    WHMgmtGuideVO getWHMgmtGuideDetail(SpSearchVO searchVo);

    int insertWHMgmtGuide(WHMgmtGuideVO vo);
    int insertWHMgmtGuideCounsel(WHMgmtGuideVO vo);
    int insertWHMgmtGuideRequest(WHMgmtGuideVO vo);

    int updateWHMgmtGuide(WHMgmtGuideVO vo);
    int updateWHMgmtGuideCounsel(WHMgmtGuideVO vo);
    int updateWHMgmtGuideRequest(WHMgmtGuideVO vo);

    int deleteWHMgmtGuide(WHMgmtGuideVO vo);
    int deleteWHMgmtGuideCounsel(WHMgmtGuideVO vo);
    int deleteWHMgmtGuideRequest(WHMgmtGuideVO vo);

    WHMgmtGuideVO getWHMgmtGuideById(WHMgmtGuideVO vo);
    WHMgmtGuideVO getWHMgmtGuideCounselById(WHMgmtGuideVO vo);
    WHMgmtGuideVO getWHMgmtGuideRequestById(WHMgmtGuideVO vo);

    //인원 매핑
    List<HrVO> getWHMgmtGuideHr(SpSearchVO searchVo);
    int insertWHMgmtGuideHr(HrVO vo);
    int deleteWHMgmtGuideHr(HrVO vo);

    // 조직 불러오기
    List<WHMgmtGuideVO> getNonExistCntHrToday(WHMgmtGuideVO vo);
    List<WHMgmtGuideVO> getOrgnHrList(WHMgmtGuideVO vo);
}

