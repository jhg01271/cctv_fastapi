package kr.co.igns.mobile.signatures.dao.postgres;

import kr.co.igns.mobile.signatures.model.ApprovalSignaturesVO;
import kr.co.igns.mobile.signatures.model.CommitteeSignaturesVO;
import kr.co.igns.mobile.signatures.model.SignaturesSearchVO;
import kr.co.igns.mobile.signatures.model.SignaturesVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Mapper
@Repository
public interface SignaturesDAO {
    // 위원회 서명 리스트 조회
    CommitteeSignaturesVO getCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    // 위원회 서명 저장
    void saveCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    // 위원회 서명 취소
    void deleteCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    // 교육 서명 리스트 조회
    List<SignaturesVO> getTrainingsSignatures(SignaturesSearchVO signaturesSearchVO);

    // 교육 서명 저장
    void saveTrainingsTRRSignatures(SignaturesSearchVO signaturesSearchVO);

    void saveTrainingsRORATSignatures(SignaturesSearchVO signaturesSearchVO);

    // 교육 서명 취소
    void deleteTrainingsTRRSignatures(SignaturesSearchVO signaturesSearchVO);

    void deleteTrainingsRORATSignatures(SignaturesSearchVO signaturesSearchVO);

    // 결재자 서명 조회
    ApprovalSignaturesVO getApprovalCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    ApprovalSignaturesVO getApprovalRORATSignatures(SignaturesSearchVO signaturesSearchVO);

    ApprovalSignaturesVO getApprovalTRRSignatures(SignaturesSearchVO signaturesSearchVO);

    // 결재자 서명 저장
    void saveApprovalCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    void saveApprovalRORATSignatures(SignaturesSearchVO signaturesSearchVO);

    void saveApprovalTRRSignatures(SignaturesSearchVO signaturesSearchVO);

    // 결재자 서명 취소
    void deleteApprovalCommitteeSignatures(SignaturesSearchVO signaturesSearchVO);

    void deleteApprovalRORATSignatures(SignaturesSearchVO signaturesSearchVO);

    void deleteApprovalTRRSignatures(SignaturesSearchVO signaturesSearchVO);

    // TBM 서명 리스트 조회
    List<SignaturesVO> getTbmSignatures(@Valid SignaturesSearchVO signaturesSearchVO);

    // TBM 서명 저장
    void saveTbmSignatures(@Valid SignaturesSearchVO signaturesSearchVO);

    // TBM 서명 취소
    void deleteTbmSignatures(@Valid SignaturesSearchVO signaturesSearchVO);
}
