package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.EducationStatusDAO;
import kr.co.igns.business.support.dao.postgres.EducationTrainingDAO;
import kr.co.igns.business.support.model.EducationStatusVO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationStatusService {
    private final EducationStatusDAO educationStatusDAO;

    public List<EducationStatusVO> getEducationStatus(SpSearchVO searchVo) throws Exception {
        List<EducationStatusVO> voList = educationStatusDAO.getEducationStatus(searchVo);
        return voList;
    }
}
