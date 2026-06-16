package kr.co.igns.business.participation.dao.postgres;

import kr.co.igns.business.participation.model.ConsultationAndParticipationVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConsultationAndParticipationDAO {
    List<ConsultationAndParticipationVO> getConsultationAndParticipation(SpSearchVO searchVo);
}
