package kr.co.igns.business.evaluation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.business.evaluation.dao.postgres.MonitoringCtrlDAO;
import kr.co.igns.business.evaluation.model.MonitoringCtrlVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoringCtrlService {
    private final MonitoringCtrlDAO monitoringCtrlDAO;

    public List<MonitoringCtrlVO> getMonitoringCtrl(SpSearchVO searchVo) throws Exception {
        List<MonitoringCtrlVO> voList = monitoringCtrlDAO.getMonitoringCtrl(searchVo);
        return voList;
    }
}
