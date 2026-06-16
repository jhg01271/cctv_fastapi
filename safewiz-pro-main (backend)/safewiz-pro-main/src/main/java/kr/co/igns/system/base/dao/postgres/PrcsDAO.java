package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.*;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PrcsDAO {
    List<PrcsVO> getPrcs(SpSearchVO searchVo);
    PrcsVO getPrcsDetail(SpSearchVO searchVo);

    int insertPrcs(PrcsVO vo);

    int updatePrcs(PrcsVO vo);

    int deletePrcs(PrcsVO vo);

    PrcsVO getPrcsById(String workplaceId);

    //작업내용 - 차수 관리
    List<PrcsCnfmVO> getPrcsRevList(String processId);
    int insertPrcsRev(PrcsCnfmVO vo);
    PrcsCnfmVO getPrcsRevMax();
    int updatePrcsRev(PrcsCnfmVO vo);

    //작업내용 - 확정
    int updatePrcsRevCnfm(PrcsCnfmVO vo);
    
    //작업내용 - 작업 내용 관리
    List<PrcsWorkVO> getPrcsCnfmWorkList(String processId);
    List<PrcsWorkVO> getPrcsWorkList(PrcsCnfmVO prcsCnfmVO);
    int insertPrcsWork(PrcsWorkVO vo);
    int updatePrcsWork(PrcsWorkVO vo);
    int updatePrcsWorkCnfm(PrcsWorkVO vo);

    //작업내용 맵 조회 - 전체
    List<PrcsWorkMapVO> getPrcsWorkMap(BaseMapVO vo);
    //작업내용 맵 조회 - useYn = Y인 것만 조회
    List<PrcsWorkVO> getPrcsWorkMapByUseYn(BaseMapVO vo);
    //작업내용 맵 데이터 추가
    int addPrcsWorkMap(PrcsWorkMapVO vo);
    //작업내용 맵 데이터 수정
    int updatePrcsWorkMap(PrcsWorkMapVO vo);
    //작업내용 맵 데이터 수정(useyn만 업데이트)
    int updateUseYnPrcsWorkMap(PrcsWorkMapVO vo);
    //작업내용 맵 데이터  삭제
    int deletePrcsWorkMap(PrcsWorkMapVO vo);

}
