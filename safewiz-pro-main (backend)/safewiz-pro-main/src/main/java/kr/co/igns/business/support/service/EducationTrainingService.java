package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.EducationTrainingDAO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EducationTrainingService {
    private final EducationTrainingDAO educationTrainingDAO;

    public List<EducationTrainingVO> getEduTraining(SpSearchVO searchVo) throws Exception {
        List<EducationTrainingVO> voList = educationTrainingDAO.getEduTraining(searchVo);
        return voList;
    }
}
