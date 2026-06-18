package kr.co.igns.business.participation.service;

import kr.co.igns.business.participation.dao.postgres.ConsultationAndParticipationDAO;
import kr.co.igns.business.participation.model.ConsultationAndParticipationVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationAndParticipationService {
    private final ConsultationAndParticipationDAO consultationAndParticipationDao;

    public List<ConsultationAndParticipationVO> getConsultationAndParticipation(SpSearchVO searchVo) throws Exception {
        List<ConsultationAndParticipationVO> voList = consultationAndParticipationDao.getConsultationAndParticipation(searchVo);
        return voList;
    }
}
