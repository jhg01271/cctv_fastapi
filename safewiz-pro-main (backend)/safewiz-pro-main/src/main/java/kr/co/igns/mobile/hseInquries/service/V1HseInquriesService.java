package kr.co.igns.mobile.hseInquries.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.hseInquries.dao.postgres.V1HseInquriesDAO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesActionVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSaveVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSearchVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1HseInquriesService {
	private final V1HseInquriesDAO v1HseInquriesDAO;
	
	public List<HseInquriesVO> getHseInquriesList(@Valid HseInquriesSearchVO hseInquriesSearchVO) {
		hseInquriesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
		List<HseInquriesVO> voList = v1HseInquriesDAO.getHseInquriesList(hseInquriesSearchVO);
		
		return voList;
	}
	
	public HseInquriesVO getHseInquriesDetail(@Valid String id, @Valid HseInquriesSearchVO hseInquriesSearchVO) {
        String[] parts = id.split("-");
  
        hseInquriesSearchVO.setWriteYear(parts[0]);
        hseInquriesSearchVO.setDocType(parts[1]);
        hseInquriesSearchVO.setDocNo(parts[2]);
        
		HseInquriesVO vo = v1HseInquriesDAO.getHseInquriesDetail(hseInquriesSearchVO);
		
		return vo;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public HseInquriesSearchVO.Id saveHseInquries(@Valid HseInquriesSaveVO hseInquriesSaveVO) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDate = currentDate.format(formatter);
        
		hseInquriesSaveVO = hseInquriesSaveVO.toBuilder()
    	        .writeYear(String.valueOf(LocalDate.now().getYear()))
    	        .docType("SHR")
    	        .receiptDt(formattedDate)
    	        .hrId(SecurityUtil.getCurrentHrId())
    	        .build();
		
	
		v1HseInquriesDAO.saveHseInquries(hseInquriesSaveVO);
		
		HseInquriesSearchVO.Id data = new HseInquriesSearchVO.Id();
        data.setId(String.format("%s-%s-%s", hseInquriesSaveVO.getWriteYear(), hseInquriesSaveVO.getDocType(), hseInquriesSaveVO.getDocNo()));
        
        return data;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public HseInquriesSearchVO.Id updateHseInquries(@Valid String id, @Valid HseInquriesSaveVO hseInquriesSaveVO) {
        String[] parts = id.split("-");
        
		hseInquriesSaveVO = hseInquriesSaveVO.toBuilder()
    	        .writeYear(parts[0])
    	        .docType(parts[1])
    	        .docNo(parts[2])
    	        .build();

		v1HseInquriesDAO.updateHseInquries(hseInquriesSaveVO);
		
		HseInquriesSearchVO.Id data = new HseInquriesSearchVO.Id();
        data.setId(id);
        
        return data;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void deleteHseInquries(@Valid String id) {
        String[] parts = id.split("-");

        HseInquriesSearchVO vo = new HseInquriesSearchVO();
        vo.setWriteYear(parts[0]);
        vo.setDocType(parts[1]);
        vo.setDocNo(parts[2]);
        vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
        
        v1HseInquriesDAO.deleteHseInquries(vo);
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void updateHseInquriesAction(@Valid String id, @Valid HseInquriesActionVO hseInquriesActionVO) {
        String[] parts = id.split("-");
        
        hseInquriesActionVO = hseInquriesActionVO.toBuilder()
		        .writeYear(parts[0])
		        .docType(parts[1])
		        .docNo(parts[2])
		        .build();	
        
        v1HseInquriesDAO.updateHseInquriesAction(hseInquriesActionVO);
	}
}
