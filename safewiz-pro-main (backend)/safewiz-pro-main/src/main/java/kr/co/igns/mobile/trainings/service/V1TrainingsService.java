package kr.co.igns.mobile.trainings.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.trainings.dao.postgres.TrainingsDAO;
import kr.co.igns.mobile.trainings.model.TrainingsDetailVO;
import kr.co.igns.mobile.trainings.model.TrainingsSearchVO;
import kr.co.igns.mobile.trainings.model.TrainingsVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1TrainingsService {
	private final TrainingsDAO trainingsDAO;
	
	public Map<String, List<Map<String, Integer>>> getTrainingsCalendarCount(@Valid TrainingsSearchVO trainingsSearchVO) {
        Map<String, List<Map<String, Integer>>> events = new HashMap<>();   
        
        trainingsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        TrainingsVO roratCount = trainingsDAO.getTrainingsRORATCalendarCount(trainingsSearchVO);
        TrainingsVO trrCount = trainingsDAO.getTrainingsTRRCalendarCount(trainingsSearchVO);
        
        events.put("RORAT", roratCount != null ? roratCount.getCalendarCount() : new ArrayList<>());
        events.put("TRR", trrCount != null ? trrCount.getCalendarCount() : new ArrayList<>());
        
		return events;
	}
	
	public List<TrainingsVO> getTrainings(@Valid TrainingsSearchVO trainingsSearchVO) {
        trainingsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
            
		List<TrainingsVO> voList = trainingsDAO.getTrainings(trainingsSearchVO);

		return voList;
	}
	
	public TrainingsDetailVO getTrainingsDetail(@Valid String id, @Valid TrainingsSearchVO trainingsSearchVO) {
        String[] parts = id.split("-");

        trainingsSearchVO.setWriteYear(parts[0]);
        trainingsSearchVO.setDocType(parts[1]);
        trainingsSearchVO.setDocNo(parts[2]);
        trainingsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        
        TrainingsDetailVO vo = new TrainingsDetailVO();
        
        if(trainingsSearchVO.getDocType().equals("RORAT")) {
        	vo = trainingsDAO.getTrainingsRORATDetail(trainingsSearchVO);
        } else if(trainingsSearchVO.getDocType().equals("TRR")) {
        	vo = trainingsDAO.getTrainingsTRRDetail(trainingsSearchVO);
        }
	
		return vo;
	}
}
