package kr.co.igns.mobile.hseInspections.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.hseInspections.dao.postgres.V1HseInspectionsDAO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailSaveVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsSearchVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1HseInspectionsService {
	private final V1HseInspectionsDAO v1HseInspectionsDAO;
	
	public Map<String, List<Map<String, Integer>>> getHseInspectionsCalendarCount(@Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
        Map<String, List<Map<String, Integer>>> events = new HashMap<>();   

        HseInspectionsVO count = v1HseInspectionsDAO.getHseInspectionsCalendarCount(hseInspectionsSearchVO);

        events.put("DONE", count.getDoneCount() != null ? count.getDoneCount() : new ArrayList<>());
        events.put("UNDONE", count.getUndoneCount() != null ? count.getUndoneCount() : new ArrayList<>());

		return events; 
	}
	
	public List<HseInspectionsVO> getHseInspections(@Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
		List<HseInspectionsVO> hseInspectionsList = v1HseInspectionsDAO.getHseInspections(hseInspectionsSearchVO);
		
		return hseInspectionsList;
	}
	
	public List<HseInspectionsTemplatesVO> getHseInspectionsTemplates(@Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
		List<HseInspectionsTemplatesVO> hseInspectionsTemplatesVOList = v1HseInspectionsDAO.getHseInspectionsTemplates(hseInspectionsSearchVO);
		
		return hseInspectionsTemplatesVOList;
	}
	
	public HseInspectionsTemplatesDetailVO getHseInspectionsTemplatesDetail(@Valid String id, @Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
        String[] parts = id.split("-");

        hseInspectionsSearchVO.setWriteYear(parts[0]);
        hseInspectionsSearchVO.setDocType(parts[1]);
        hseInspectionsSearchVO.setDocNo(parts[2]);
		
		HseInspectionsTemplatesDetailVO hseInspectionsTemplatesDetailVO = v1HseInspectionsDAO.getHseInspectionsTemplatesDetail(hseInspectionsSearchVO);
		
		return hseInspectionsTemplatesDetailVO;
	}
	
	public HseInspectionsDetailVO getHseInspectionsDetail(@Valid String id, @Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
        String[] parts = id.split("-");

        hseInspectionsSearchVO.setWriteYear(parts[0]);
        hseInspectionsSearchVO.setDocType(parts[1]);
        hseInspectionsSearchVO.setDocNo(parts[2]);
		
		HseInspectionsDetailVO hseInspectionsTemplatesDetailVO = v1HseInspectionsDAO.getHseInspectionsDetail(hseInspectionsSearchVO);
		
		return hseInspectionsTemplatesDetailVO;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public HseInspectionsSearchVO saveHseInspectionsDetail(@Valid String compId, @Valid HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO) {
		 
		hseInspectionsDetailSaveVO = hseInspectionsDetailSaveVO.toBuilder()
    	        .writeYear(String.valueOf(LocalDate.now().getYear()))
    	        .docType("SIL")
    	        .hrId(SecurityUtil.getCurrentHrId())
    	        .build();
		
		v1HseInspectionsDAO.saveSafetyInspectionLog(hseInspectionsDetailSaveVO);
		v1HseInspectionsDAO.saveChange(hseInspectionsDetailSaveVO);
		
	    HseInspectionsSearchVO vo = new HseInspectionsSearchVO();
	    vo.setId(String.format("%s-%s-%s", hseInspectionsDetailSaveVO.getWriteYear(), hseInspectionsDetailSaveVO.getDocType(), hseInspectionsDetailSaveVO.getDocNo()));

	    List<HseInspectionsDetailSaveVO.CheckList> checkList = hseInspectionsDetailSaveVO.getCheckList();
	   
	    if (checkList != null && !checkList.isEmpty()) {
		    for (HseInspectionsDetailSaveVO.CheckList checkItem : checkList) {
			    checkItem.setAcceptableYn();
			    v1HseInspectionsDAO.saveSafetyInspectionLogDetail(hseInspectionsDetailSaveVO, checkItem);
		    }
	    }

    	return vo;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public HseInspectionsSearchVO updateHseInspectionsDetail(@Valid String id, @Valid HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO) {
        String[] parts = id.split("-");

		hseInspectionsDetailSaveVO = hseInspectionsDetailSaveVO.toBuilder()
    	        .writeYear(parts[0])
    	        .docType(parts[1])
    	        .docNo(parts[2])
    	        .build();
		
		v1HseInspectionsDAO.updateSafetyInspectionLog(hseInspectionsDetailSaveVO);

	    HseInspectionsSearchVO vo = new HseInspectionsSearchVO();
	    vo.setId(id);

	    List<HseInspectionsDetailSaveVO.CheckList> checkList = hseInspectionsDetailSaveVO.getCheckList();
	   
	    if (checkList != null && !checkList.isEmpty()) {
		    for (HseInspectionsDetailSaveVO.CheckList checkItem : checkList) {
			    checkItem.setAcceptableYn();
			    v1HseInspectionsDAO.updateSafetyInspectionLogDetail(hseInspectionsDetailSaveVO, checkItem);
		    }
	    }

    	return vo;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void deleteHseInspectionsDetail(@Valid String id, @Valid HseInspectionsSearchVO hseInspectionsSearchVO) {
        String[] parts = id.split("-");

        hseInspectionsSearchVO.setWriteYear(parts[0]);
        hseInspectionsSearchVO.setDocType(parts[1]);
        hseInspectionsSearchVO.setDocNo(parts[2]);
        
        v1HseInspectionsDAO.deleteHseInspectionsDetail(hseInspectionsSearchVO);
	}
}
