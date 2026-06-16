package kr.co.igns.mobile.committees.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.committees.dao.postgres.CommitteesDAO;
import kr.co.igns.mobile.committees.model.CommitteesDetailVO;
import kr.co.igns.mobile.committees.model.CommitteesSearchVO;
import kr.co.igns.mobile.committees.model.CommitteesVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1CommitteesService {
	private final CommitteesDAO committeesDAO;
	
	public Map<String, List<Map<String, Integer>>> getCommitteesCalendarCount(@Valid CommitteesSearchVO committeesSearchVO) {
        Map<String, List<Map<String, Integer>>> events = new HashMap<>();   
        
        committeesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        List<CommitteesVO> vo = committeesDAO.getCommitteesCalendarCount(committeesSearchVO);
        
        if (vo == null || vo.isEmpty()) {
        	events.put("OHC", new ArrayList<>());
            events.put("PAC", new ArrayList<>());
        } else {
            events.put("OHC", vo.size() > 0 ? vo.get(0).getCalendarCount() : new ArrayList<>());
            events.put("PAC", vo.size() > 1 ? vo.get(1).getCalendarCount() : new ArrayList<>());
        }

		return events;
	}
	
	public List<CommitteesVO> getCommittees(@Valid CommitteesSearchVO committeesSearchVO) {
	    committeesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
	    
		List<CommitteesVO> voList = committeesDAO.getCommittees(committeesSearchVO);
		return voList;
	}
	
	public CommitteesDetailVO getCommitteesDetail(@Valid String id, @Valid CommitteesSearchVO committeesSearchVO) {
        String[] parts = id.split("-");

        committeesSearchVO.setWriteYear(parts[0]);
        committeesSearchVO.setDocType(parts[1]);
        committeesSearchVO.setDocNo(parts[2]);
        committeesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        
		CommitteesDetailVO vo = committeesDAO.getCommitteesDetail(committeesSearchVO);

		return vo;
	}
}
