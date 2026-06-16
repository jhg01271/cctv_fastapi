package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.WpVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WpDAO {
    List<WpVO> getWp(SpSearchVO searchVo);
    WpVO getWpDetail(SpSearchVO searchVo);

    int insertWp(WpVO vo);

    int updateWp(WpVO vo);

    int deleteWp(WpVO vo);

    WpVO getWpById(String workplaceId);

    //작업장 맵 조회 - 전체
    List<BaseMapVO> getWpMap(BaseMapVO vo);
    //작업장 맵 조회 - useYn = Y인 것만 조회
    List<BaseMapVO> getWpMapByUseYn(BaseMapVO vo);
    //작업장 맵 데이터 추가
    int addWpMap(BaseMapVO vo);
    //작업장 맵 데이터 수정
    int updateWpMap(BaseMapVO vo);
    //작업장 맵 데이터 수정(useyn만 업데이트)
    int updateUseYnWpMap(BaseMapVO vo);
    //작업장 맵 데이터  삭제
    int deleteWpMap(BaseMapVO vo);
}
