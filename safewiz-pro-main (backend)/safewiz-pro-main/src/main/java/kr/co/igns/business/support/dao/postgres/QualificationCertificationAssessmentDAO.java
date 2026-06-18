package kr.co.igns.business.support.dao.postgres;

import java.util.List;

import kr.co.igns.business.support.model.EducationTrainingVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.support.model.QualificationCertificationAssessmentVO;
import kr.co.igns.business.support.model.QualificationCertificationDocVo;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface QualificationCertificationAssessmentDAO {

    List<EducationTrainingVO> getQualificationManagement(SpSearchVO spSearchVO);
    int insertDocDetail(QualificationCertificationDocVo vo);
    int updateDocDetail(QualificationCertificationDocVo vo);
    List<QualificationCertificationDocVo> getEvaluationList(SpSearchVO vo);
    List<QualificationCertificationDocVo> getEvaluationDetailList(SpSearchVO vo);
    List<QualificationCertificationDocVo> getAllEvaluationDetailList(SpSearchVO vo);
    List<QualificationCertificationDocVo> getEvaluationDataSetList(SpSearchVO vo);
    int insertEvaluationList (QualificationCertificationDocVo vo);  //내부심사원 자격인증 평가표 관리 데이터 추가
    int updateEvaluationList (QualificationCertificationDocVo vo);  //내부심사원 자격인증 평가표 관리 데이터 저장


    List<QualificationCertificationAssessmentVO> searchList(SpSearchVO searchVo);
    QualificationCertificationAssessmentVO search(SpSearchVO searchVo);


    void delete(QualificationCertificationAssessmentVO vo);
    void update(QualificationCertificationAssessmentVO vo);
    void insert(QualificationCertificationAssessmentVO vo);
}
