package kr.co.igns.business.evaluation.dao.postgres;

import kr.co.igns.business.evaluation.model.*;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MonitoringManageDAO {
    List<EvaluationReportVO> getEvaluationReportList(SpSearchVO searchVo);
    List<EvaluationReportDetailVO> getEvaluationReportDetail(SpSearchVO searchVo);
    EvaluationReportDetailVO getEvaluationReportDetailByChecked (SpSearchVO searchVo);

    int insertEvaluationReport(EvaluationReportVO vo);
    int insertEvaluationReportDetail(EvaluationReportDetailVO vo);

    int updateEvaluationReport(EvaluationReportVO vo);
    int updateEvaluationReportDetail(EvaluationReportDetailVO vo);
    int updateEvaluationReportPerformance(EvaluationReportDetailVO vo);

    int deleteEvaluationReport(EvaluationReportVO vo);
    int deleteEvaluationReportDetail(EvaluationReportDetailVO vo);
    int deleteEvaluationReportDetail2(EvaluationReportDetailVO vo);

    EvaluationReportVO getEvaluationReportById(EvaluationReportVO vo);
    EvaluationReportDetailVO getEvaluationReportDetailById(EvaluationReportDetailVO vo);
    EvaluationReportDetailVO getEvaluationChecklistCriteria(EvaluationReportDetailVO vo);
    //#region 평가항목 관리 팝업
    List<EvaluationChecklistVO> getEvaluationChecklist(SpSearchVO searchVo);
    List<EvaluationChecklistVO> getEvaluationChecklistDataSet(SpSearchVO searchVo);
    List<EvaluationChecklistVO> getEvaluationChecklistByUseYn(SpSearchVO searchVo);
    List<EvaluationChecklistVO> getEvaluationChecklistDetail(EvaluationChecklistVO vo);
    List<EvaluationChecklistVO> getEvaluationChecklistDetailDataSet(EvaluationChecklistVO vo);
    List<EvaluationChecklistVO> getEvaluationChecklistDetailBySearchText(SpSearchVO vo);
    int insertEvaluationChecklist(EvaluationChecklistVO vo);
    int updateEvaluationChecklist(EvaluationChecklistVO vo);
    int insertEvaluationChecklistDetail(EvaluationChecklistVO vo);
    int updateEvaluationChecklistDetail(EvaluationChecklistVO vo);
    int deleteEvaluationChecklist(EvaluationChecklistVO vo);
    int deleteEvaluationChecklistDetail(EvaluationChecklistVO vo);
    int deleteEvaluationChecklistDetailData(EvaluationChecklistVO vo);
    EvaluationChecklistVO getEvaluationChecklistById(EvaluationChecklistVO vo);
    EvaluationChecklistVO getEvaluationChecklistDetailById(EvaluationChecklistVO vo);

    //예시 불러오기
    List<EvaluationChecklistVO> getBaseEvaluationChecklist(SpSearchVO searchVo);
    List<EvaluationChecklistVO> getBaseEvaluationChecklistDetail(EvaluationChecklistVO vo);
    //#endregion

    //준수평가표
    List<LegalComplianceEvaluationVO> getLegalComplianceEvaluationList(SpSearchVO searchVo);
    List<LegalComplianceEvaluationDetailVO> getLegalComplianceEvaluationDetailList(SpSearchVO searchVo);
    List<LegalComplianceEvaluationDetailOrgnVO> getLegalComplianceEvaluationDetailOrgnList(LegalComplianceEvaluationDetailVO vo);

    int insertLegalComplianceEvaluation(LegalComplianceEvaluationVO vo);
    int insertLegalComplianceEvaluationDetail(LegalComplianceEvaluationDetailVO vo);
    int insertLegalComplianceEvaluationDetailOrgn(LegalComplianceEvaluationDetailOrgnVO vo);

    int updateLegalComplianceEvaluation(LegalComplianceEvaluationVO vo);
    int updateLegalComplianceEvaluationDetail(LegalComplianceEvaluationDetailVO vo);

    int deleteLegalComplianceEvaluation(LegalComplianceEvaluationVO vo);
    int deleteLegalComplianceEvaluationDetail(LegalComplianceEvaluationDetailVO vo);
    int deleteLegalComplianceEvaluationDetailOrgn(LegalComplianceEvaluationDetailOrgnVO vo);

    // 일괄출력용
    List<String> getAllReport(SpSearchVO spSearchVO);
    List<String> getAllEvaluationComplianceReport(SpSearchVO spSearchVO);
}

