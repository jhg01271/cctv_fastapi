package kr.co.igns.mobile.riskReports.service;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.riskReports.dao.postgres.V1RiskReportsDAO;
import kr.co.igns.mobile.riskReports.model.RiskReportsCountVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsDetailVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSaveVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSearchVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1RiskReportsService {
	private final V1RiskReportsDAO v1RiskReportsDAO;

	public RiskReportsCountVO getRiskReportsCount(@Valid RiskReportsSearchVO riskReportsSearchVO) {
		riskReportsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
		RiskReportsCountVO vo =  v1RiskReportsDAO.getRiskReportsCount(riskReportsSearchVO);
		
		return vo;
	}
	
	public List<RiskReportsVO> getRiskReports(@Valid RiskReportsSearchVO riskReportsSearchVO) {
		riskReportsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
		List<RiskReportsVO> voList = v1RiskReportsDAO.getRiskReports(riskReportsSearchVO);
		return voList;
	}
	
	public RiskReportsDetailVO getRiskReportsDetail(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        riskReportsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        
		RiskReportsDetailVO riskReportsDetailVO = v1RiskReportsDAO.getRiskReportsDetail(riskReportsSearchVO);
		
		return riskReportsDetailVO;
	}
	
    @Transactional(rollbackFor = Throwable.class)
	public void saveRiskReportsSignatures(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        riskReportsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        
		v1RiskReportsDAO.saveRiskReportsSignatures(riskReportsSearchVO);
	}
    
    @Transactional(rollbackFor = Throwable.class)
	public void deleteRiskReportsSignatures(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        riskReportsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        
		v1RiskReportsDAO.deleteRiskReportsSignatures(riskReportsSearchVO);
	}
    
    @Transactional(rollbackFor = Throwable.class)
    public void updateRiskReportsReplies(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        
        v1RiskReportsDAO.updateRiskReportsReplies(riskReportsSearchVO);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public void saveRiskReportsReviewer(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        riskReportsSearchVO.setCreatedBy(SecurityUtil.getCurrentMemberId());

        v1RiskReportsDAO.saveRiskReportsReviewer(riskReportsSearchVO);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRiskReports(@Valid String id, @Valid RiskReportsSearchVO riskReportsSearchVO) {
        String[] parts = id.split("-");

        riskReportsSearchVO.setWriteYear(parts[0]);
        riskReportsSearchVO.setDocType(parts[1]);
        riskReportsSearchVO.setDocNo(parts[2]);
        riskReportsSearchVO.setUpdatedBy(SecurityUtil.getCurrentMemberId());
        
        v1RiskReportsDAO.deleteRiskReports(riskReportsSearchVO);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public RiskReportsSearchVO saveRiskReports(@Valid String compId, @Valid RiskReportsSaveVO riskReportsSaveVO) {
    	riskReportsSaveVO.setWriteYear(String.valueOf(LocalDate.now().getYear()));
    	riskReportsSaveVO.setDocType("WOOHA");
    	riskReportsSaveVO.setType("writer");
    	riskReportsSaveVO.setCompId(compId);
    	riskReportsSaveVO.setHrId(SecurityUtil.getCurrentHrId());
    	
    	v1RiskReportsDAO.saveRiskReports(riskReportsSaveVO);
    	v1RiskReportsDAO.saveWriter(riskReportsSaveVO);
    	// 경험담 저장
    	if(!riskReportsSaveVO.getNewExperiences().isEmpty()) {
    		v1RiskReportsDAO.saveExperiences(riskReportsSaveVO, riskReportsSaveVO.getNewExperiences());
    	}
        // 검토자 업데이트
    	if(riskReportsSaveVO.getReviewerHrId() != null) {
        	riskReportsSaveVO.setType("reviewer");
    	  	v1RiskReportsDAO.saveReviewer(riskReportsSaveVO);
    	}
        // 작성자 서명
        if(riskReportsSaveVO.isWriterSignFlag()) {
    	  	RiskReportsSearchVO riskReportsSearchVO = RiskReportsSearchVO.builder()
    	  			.compId(riskReportsSaveVO.getCompId())
    	  			.writeYear(riskReportsSaveVO.getWriteYear())
    	  			.docType(riskReportsSaveVO.getDocType())
    	  			.docNo(riskReportsSaveVO.getDocNo())
    	  			.hrId(riskReportsSaveVO.getHrId())
    	  			.type("writer")
    	  			.build();
    		
    	  	v1RiskReportsDAO.saveRiskReportsSignatures(riskReportsSearchVO);
        }
        
        RiskReportsSearchVO vo = new RiskReportsSearchVO();
        vo.setId(riskReportsSaveVO.getWriteYear() + "-" + riskReportsSaveVO.getDocType() + "-" + riskReportsSaveVO.getDocNo());

    	return vo;
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public void updateRiskReports(@Valid String id, @Valid RiskReportsSaveVO riskReportsSaveVO) {
        String[] parts = id.split("-");

        riskReportsSaveVO = riskReportsSaveVO.toBuilder()
	        .writeYear(parts[0])
	        .docType(parts[1])
	        .docNo(parts[2])
	        .type("reviewer")
	        .hrId(SecurityUtil.getCurrentHrId())
	        .build();
	        
        // 근로자 참여 업데이트
        if(riskReportsSaveVO.isWorkerOpinionUpdateFlag()) {
        	v1RiskReportsDAO.updateRiskReports(riskReportsSaveVO);
        }
        // 경험담 삭제
        if(!riskReportsSaveVO.getDeleteExperiences().isEmpty()) {
          	v1RiskReportsDAO.deleteExperiences(riskReportsSaveVO, riskReportsSaveVO.getDeleteExperiences());
        }
        // 경험담 업데이트
        if(!riskReportsSaveVO.getExperiences().isEmpty()) {
        	v1RiskReportsDAO.updateExperiences(riskReportsSaveVO, riskReportsSaveVO.getExperiences());
        }
        // 경험담 저장
        if(!riskReportsSaveVO.getNewExperiences().isEmpty()) {
        	v1RiskReportsDAO.saveExperiences(riskReportsSaveVO, riskReportsSaveVO.getNewExperiences());
        }
        // 검토자 업데이트
        if(riskReportsSaveVO.isReviewerUpdateFlag()) {
        	if(riskReportsSaveVO.getReviewerHrId() == null) {
        	  	v1RiskReportsDAO.deleteReviewer(riskReportsSaveVO);
        	} else {
        	  	v1RiskReportsDAO.deleteReviewer(riskReportsSaveVO);
        	  	v1RiskReportsDAO.saveReviewer(riskReportsSaveVO);
        	}
        }
        // 작성자 서명
        if(riskReportsSaveVO.isWriterSignFlag()) {
    	  	RiskReportsSearchVO riskReportsSearchVO = RiskReportsSearchVO.builder()
    	  			.compId(riskReportsSaveVO.getCompId())
    	  			.writeYear(riskReportsSaveVO.getWriteYear())
    	  			.docType(riskReportsSaveVO.getDocType())
    	  			.docNo(riskReportsSaveVO.getDocNo())
    	  			.hrId(riskReportsSaveVO.getHrId())
    	  			.type("writer")
    	  			.build();
    		
    	  	v1RiskReportsDAO.saveRiskReportsSignatures(riskReportsSearchVO);
        } 
    }
}
