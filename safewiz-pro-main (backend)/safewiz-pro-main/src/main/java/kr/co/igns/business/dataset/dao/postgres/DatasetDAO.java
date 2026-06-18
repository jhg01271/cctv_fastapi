package kr.co.igns.business.dataset.dao.postgres;

import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.business.dataset.model.TypeofbusinessVO;
import kr.co.igns.system.common.model.SpSearchVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DatasetDAO {
    // 업종 리스트 조회 (페이지 기반, 검색어 포함)
    List<TypeofbusinessVO> getTypeofbusinessList(SpSearchVO searchVo); // 검색어 추가

    // 설비 리스트 조회 (페이지 기반)
    List<TypeofEquipmentVO> getTypeofEquipmentList(@Param("limit") int limit,
                                                   @Param("offset") int offset, @Param("compId") String compId);
}
