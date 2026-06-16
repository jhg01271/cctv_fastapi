package kr.co.igns.business.dataset.service;

import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.business.dataset.model.TypeofbusinessVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.co.igns.business.dataset.dao.postgres.DatasetDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {
    private final DatasetDAO datasetDao;

    // 업종 리스트 조회 (페이지 기반, 검색어 포함)
    public List<TypeofbusinessVO> getTypeofbusiness(SpSearchVO searchVo) throws Exception {
        return datasetDao.getTypeofbusinessList(searchVo); // searchTerm 추가
    }

    // 설비 리스트 조회 (페이지 기반)
    public List<TypeofEquipmentVO> getTypeofEquipment(int page, int pageSize, String compId) throws Exception {
        return datasetDao.getTypeofEquipmentList(pageSize, page, compId);
    }
}
