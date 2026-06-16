package kr.co.igns.business.participation.service;

import kr.co.igns.business.participation.dao.postgres.HseObjectivesDAO;
import kr.co.igns.business.participation.model.HseObjectivesVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HseObjectivesService {
    private final HseObjectivesDAO hseObjectivesDao;


    public List<HseObjectivesVO> getHseObjectives(SpSearchVO searchVo) throws Exception {
        List<HseObjectivesVO> voList = hseObjectivesDao.getHseObjectives(searchVo);
        return voList;
    }
    public List<HseObjectivesVO> getHseOrganizationChart(SpSearchVO searchVo) throws Exception {
        List<HseObjectivesVO> voList = hseObjectivesDao.getHseOrganizationChart(searchVo);
        return voList;
    }
}
