package kr.co.igns.business.improvement.dao.postgres;

import java.util.List;

import kr.co.igns.business.improvement.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface NonconformityCorrectiveDAO {
    
    List<ContinualImprovementVO> getParticipation(SpSearchVO searchVO);
    
    List<NearMissReportVO> getNearMissReport(SpSearchVO searchVo);
    NearMissReportVO getNearMissReportDetail(String docNo);
    
    NearMissReportVO getOrgnHead(String actionOrgnId);

    void insertNearMissReport(NearMissReportVO vo);

    void updateNearMissReport(NearMissReportVO vo);

    void deleteNearMissReport(NearMissReportVO vo);
    
    void printNearMissReport(NearMissReportVO vo);
    
    List<FileVO> getNearMissReportFileList(NearMissReportVO vo);

    NearMissReportVO getFileId(NearMissReportVO vo);

    NearMissReportVO getNearMissReportById(String reportId);
    
    List<CorrectiveActionRequestVO> getCorrectiveActionRequest(CorrectiveActionRequestVO vo);

    CorrectiveActionRequestVO getCorrectiveActionRequestDetail(SpSearchVO vo);
	
	CorrectiveActionRequestVO getCorrectiveActionRequestById(String requestId);
	
	void insertCorrectiveActionRequest(CorrectiveActionRequestVO vo);

	void updateCorrectiveActionRequest(CorrectiveActionRequestVO vo);

	void deleteCorrectiveActionRequest(CorrectiveActionRequestVO vo);
	
	List<IncidentReportVO> getIncidentReport(SpSearchVO searchVo);
	
	//List<IncidentReportVO> getIncidentDetailReport(IncidentReportVO vo);
	IncidentReportVO getIncidentDetailReport(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailManage(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailExtent(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailOpinion(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailManagechk(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailExtentchk(IncidentReportVO vo);
	
	List<IncidentReportVO> getIncidentDetailOpinionchk(IncidentReportVO vo);
	
	
	//List<IncidentReportVO> getIncidentState(IncidentReportVO vo);
	IncidentReportVO getIncidentState(IncidentReportVO vo);

	
	String getdoc(String writeYear);

	String getStatementdocSeq(IncidentReportVO vo);
	
	void insertIncidentReport(IncidentReportVO vo);
	
	void insertIncidentReportDetail(IncidentReportVO vo);
	
	void insertIncidentReportManage(IncidentReportVO vo);
	
	void insertIncidentReportOpinion(IncidentReportVO vo);
	
	void insertIncidentReportStatment(IncidentReportVO vo);
	
	void updateIncidentReport(IncidentReportVO vo);
	
	void updateIncidentReportuseYn(IncidentReportVO vo);
	
	void updateIncidentReportDetail(IncidentReportVO vo);
	
	void UpdateIncidentReportManage(IncidentReportVO vo);
	
	void UpdateIncidentReportOpinion(IncidentReportVO vo);
	
	void updateIncidentReportStatment(IncidentReportVO vo);
	
	void deleteIncidentReport(IncidentReportVO vo);
	
	void deleteIncidentDetail(IncidentReportVO vo);
	
	void deleteIncidentHospi(IncidentReportVO vo);

	void deleteIncidentOpinion(IncidentReportVO vo);
	
	void DeteleStatement(IncidentReportVO vo);
	
	IncidentReportVO IncidentReportPrint(SpSearchVO vo);
	
	IncidentReportVO IncidentReportPrintcheck(SpSearchVO vo);
	
	IncidentReportVO IncidentReportStatementPrint(SpSearchVO vo);
	
	IncidentReportVO WitnessReportStatementPrint(SpSearchVO vo);
	
	String getDetailSeq(String writeYear, String docNo);
	
	List<FileVO> getIncidentReportFileList(IncidentReportVO vo);
	
	IncidentReportVO getIncidentPersonInfo(SpSearchVO vo);


	// 일괄출력
	List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
}
