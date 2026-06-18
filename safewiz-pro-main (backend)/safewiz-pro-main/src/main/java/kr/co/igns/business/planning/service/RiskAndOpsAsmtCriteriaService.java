package kr.co.igns.business.planning.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.business.planning.dao.postgres.RiskAndOpsAsmtCriteriaDAO;
import kr.co.igns.business.planning.model.RiskAndOpsAsmtCriteriaVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiskAndOpsAsmtCriteriaService {
	private final RiskAndOpsAsmtCriteriaDAO riskAndOpsAsmtCriteriaDAO;
    private final ReportService reportService;
    private final UtilsService utilsService;
    
    // 리스크와 기회 첫페이지 조회
    public List<RiskAndOpsAsmtCriteriaVO> getRiskMain(SpSearchVO searchVo) throws Exception {
        List<RiskAndOpsAsmtCriteriaVO> voList = riskAndOpsAsmtCriteriaDAO.getRiskMain(searchVo);
        return voList;
    }
    
    //메인 조회
    public List<RiskAndOpsAsmtCriteriaVO> getRiskAndOpsAsmtCriteria(SpSearchVO searchVo) throws Exception {
    	List<RiskAndOpsAsmtCriteriaVO> voList = riskAndOpsAsmtCriteriaDAO.getRiskAndOpsAsmtCriteria(searchVo);
    	return voList;
    }
    
    //디테일 조회
    public List<RiskAndOpsAsmtCriteriaVO> getRiskAndOpsAsmtCriteriaDetailList(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO) throws Exception {
    	List<RiskAndOpsAsmtCriteriaVO> voList = riskAndOpsAsmtCriteriaDAO.getRiskAndOpsAsmtCriteriaDetailList(riskAndOpsAsmtCriteriaVO);
    	return voList;
    }
    
    //데이터셋 조회
    public List<RiskAndOpsAsmtCriteriaVO> getDatasetAsmtList(RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO) throws Exception {
    	List<RiskAndOpsAsmtCriteriaVO> voList = riskAndOpsAsmtCriteriaDAO.getDatasetAsmtList(riskAndOpsAsmtCriteriaVO);
    	return voList;
    }
    
    // 저장
    public List<RiskAndOpsAsmtCriteriaVO> saveRiskAndOpsAsmtCriteria(List<RiskAndOpsAsmtCriteriaVO> voList) throws Exception {
    	

        String criteriaId = riskAndOpsAsmtCriteriaDAO.getMaxAsmtCriteria(voList.get(0).getCompId()); // compId를 String으로 전달

        List<RiskAndOpsAsmtCriteriaVO> resultList = new ArrayList<>();
        
        if (voList.stream().anyMatch(vo -> "I".equals(vo.getCmd()))) {
            // 신규 데이터가 존재할 때만 삭제
            riskAndOpsAsmtCriteriaDAO.deleteAsmtGrade(voList.get(0));
        }

        for (RiskAndOpsAsmtCriteriaVO vo : voList) {
            
            // 신규 등록일 경우
            if ("I".equals(vo.getCmd())) {
            	System.out.println("탈꺼니!!!!!!!!!!");
            	if (vo.getMainUseYn() == null || vo.getMainUseYn()) {
            		System.out.println("탔니!!!!!!!!!!");
        	        vo.setUseYn("Y");
        	    } else {
        	        vo.setUseYn("N");
        	    }
            	System.out.println("들고있니!!!!!!!!!" + vo.getMainUseYn());
            	vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            	if(vo.getCriteriaId() == null || vo.getCriteriaId() == "") {
            		vo.setCriteriaId(criteriaId);
            	}
                riskAndOpsAsmtCriteriaDAO.insertRiskAndOpsAsmtCriteriaMain(vo);
                if(vo.getOrdSeq() == null || vo.getOrdSeq() == "") {
                	vo.setOrdSeq("99");
                }
                System.out.println("@@@@@@@@@@@@@@@@@@@"+ vo);

                riskAndOpsAsmtCriteriaDAO.insertRiskAndOpsAsmtCriteriaDetail(vo);
            } else {
            	if (vo.getMainUseYn() != null) {
            	    if (vo.getMainUseYn()) {
            	        vo.setUseYn("Y");
            	    } else {
            	        vo.setUseYn("N");
            	    }
            	    vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            	    riskAndOpsAsmtCriteriaDAO.saveMainUseYn(vo);
            	}
            	if(vo.getOrdSeq() == null || vo.getOrdSeq() == "") {
                	vo.setOrdSeq("99");
                }
            	vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                riskAndOpsAsmtCriteriaDAO.updateRiskAndOpsAsmtCriteriaDetail(vo);
            }
            
            resultList.add(vo); // 결과 리스트에 추가
        }
        
        return resultList;
    }
    
    // 메인확정여부 저장
    public List<RiskAndOpsAsmtCriteriaVO> saveMainConfirm(List<RiskAndOpsAsmtCriteriaVO> voList) throws Exception {

        List<RiskAndOpsAsmtCriteriaVO> resultList = new ArrayList<>();

        for (RiskAndOpsAsmtCriteriaVO vo : voList) {    
        	vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            riskAndOpsAsmtCriteriaDAO.saveMainConfirm(vo);
            resultList.add(vo); // 결과 리스트에 추가
        }
        
        return resultList;
    }
    
    // 메인삭제
    public List<RiskAndOpsAsmtCriteriaVO> deleteRiskAndOpsAsmtCriteriaMain(List<RiskAndOpsAsmtCriteriaVO> voList) throws Exception {
    	
    	List<RiskAndOpsAsmtCriteriaVO> resultList = new ArrayList<>();
    	
    	for (RiskAndOpsAsmtCriteriaVO vo : voList) {   
    		vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
    		riskAndOpsAsmtCriteriaDAO.deleteRiskAndOpsAsmtCriteriaMain(vo);
    		resultList.add(vo); // 결과 리스트에 추가
    	}
    	
    	return resultList;
    }
    // 디테일삭제
    public List<RiskAndOpsAsmtCriteriaVO> deleteRiskAndOpsAsmtCriteriaDetail(List<RiskAndOpsAsmtCriteriaVO> voList) throws Exception {
    	
    	List<RiskAndOpsAsmtCriteriaVO> resultList = new ArrayList<>();
    	
    	for (RiskAndOpsAsmtCriteriaVO vo : voList) {    
    		vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
    		riskAndOpsAsmtCriteriaDAO.deleteRiskAndOpsAsmtCriteriaDetail(vo);
    		resultList.add(vo); // 결과 리스트에 추가
    	}
    	
    	return resultList;
    }
}
