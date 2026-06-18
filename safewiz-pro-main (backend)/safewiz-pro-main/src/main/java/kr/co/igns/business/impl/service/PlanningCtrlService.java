package kr.co.igns.business.impl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.business.impl.dao.postgres.PlanningCtrlDAO;
import kr.co.igns.business.impl.model.PlanningCtrlVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanningCtrlService {
    private final PlanningCtrlDAO planningCtrlDAO;

    public List<PlanningCtrlVO> getPlanningCtrl(SpSearchVO searchVo) throws Exception {
        List<PlanningCtrlVO> voList = planningCtrlDAO.getPlanningCtrl(searchVo);
        return voList;
    }
}
