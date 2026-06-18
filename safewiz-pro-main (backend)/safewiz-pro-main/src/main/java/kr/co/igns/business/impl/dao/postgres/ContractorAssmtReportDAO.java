package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.ContractorAssmtReportHrVO;
import kr.co.igns.business.impl.model.ContractorAssmtReportVO;
import kr.co.igns.business.impl.model.ContractorChecklistDetailVO;
import kr.co.igns.business.impl.model.ContractorChecklistVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContractorAssmtReportDAO {
    List<ContractorChecklistVO> getEvaluationType(SpSearchVO searchVo);
    List<ContractorChecklistDetailVO> getEvaluationTypeDetail(SpSearchVO searchVo);
    List<ContractorChecklistVO> getEvaluationTypeDataset(SpSearchVO searchVo);
    List<ContractorChecklistDetailVO> getEvaluationDetailDataset(ContractorChecklistVO vo);

    void insertEvaluationType(ContractorChecklistVO vo);
    void updateEvaluationType(ContractorChecklistVO vo);
    void insertEvaluationTypeDetail(ContractorChecklistDetailVO vo);
    void updateEvaluationTypeDetail(ContractorChecklistDetailVO vo);

    void deleteEvaluationType(ContractorChecklistVO vo);
    void deleteEvaluationTypeDetailAll(ContractorChecklistVO vo);
    void deleteEvaluationTypeDetail(ContractorChecklistDetailVO vo);

    void insertReport(ContractorAssmtReportVO vo);
    void updateReport(ContractorAssmtReportVO vo);
    void deleteReport(ContractorAssmtReportVO vo);

    void insertAssmtHr(ContractorAssmtReportHrVO vo);
    void deleteAssmtHr(ContractorAssmtReportVO vo);

    void deleteEvaluation(ContractorAssmtReportVO vo);
    void insertEvaluation(ContractorChecklistVO vo);
    void updateEvaluation(ContractorChecklistVO vo);
    
    void deleteEvaluationDetail(ContractorChecklistVO vo);
    void insertEvaluationDetail(ContractorChecklistDetailVO vo);
    void updateEvaluationDetail(ContractorChecklistDetailVO vo);

    List<ContractorAssmtReportVO> getAssmtReport(SpSearchVO searchVo);
    List<ContractorAssmtReportVO> getAssmtReportDetail(SpSearchVO searchVo);
    ContractorAssmtReportVO getAssmetReportDetailToDocNo (SpSearchVO searchVo);
    List<ContractorAssmtReportHrVO> getAssmtHr(ContractorAssmtReportVO searchVo);
    List<ContractorChecklistVO> getEvaluation(ContractorAssmtReportVO searchVo);
    List<ContractorChecklistDetailVO> getEvaluationDetail(ContractorChecklistVO vo);
}
