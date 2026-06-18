package kr.co.igns.mobile.signatures.service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.signatures.dao.postgres.SignaturesDAO;
import kr.co.igns.mobile.signatures.model.ApprovalSignaturesVO;
import kr.co.igns.mobile.signatures.model.CommitteeSignaturesVO;
import kr.co.igns.mobile.signatures.model.SignaturesSearchVO;
import kr.co.igns.mobile.signatures.model.SignaturesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class V1SignaturesService {
    private final SignaturesDAO signaturesDAO;

    public CommitteeSignaturesVO getCommitteesSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        CommitteeSignaturesVO committeeSignautresVO = signaturesDAO.getCommitteeSignatures(signaturesSearchVO);

        return committeeSignautresVO;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveCommitteeSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocNo(parts[2]);

        signaturesDAO.saveCommitteeSignatures(signaturesSearchVO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteCommitteeSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocNo(parts[2]);

        signaturesDAO.deleteCommitteeSignatures(signaturesSearchVO);
    }

    public List<SignaturesVO> getTrainingsSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        List<SignaturesVO> vo = signaturesDAO.getTrainingsSignatures(signaturesSearchVO);

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveTrainingsSignatures(@Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocNo(parts[2]);

        if (signaturesSearchVO.getDocType().equals("TRR")) {
            signaturesDAO.saveTrainingsTRRSignatures(signaturesSearchVO);
        } else if (signaturesSearchVO.getDocType().equals("RORAT")) {
            signaturesDAO.saveTrainingsRORATSignatures(signaturesSearchVO);
        } else {
            throw new IllegalArgumentException("Invalid document type: " + signaturesSearchVO.getDocType());
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteTrainingsSignatures(@Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocNo(parts[2]);

        if (signaturesSearchVO.getDocType().equals("TRR")) {
            signaturesDAO.deleteTrainingsTRRSignatures(signaturesSearchVO);
        } else if (signaturesSearchVO.getDocType().equals("RORAT")) {
            signaturesDAO.deleteTrainingsRORATSignatures(signaturesSearchVO);
        } else {
            throw new IllegalArgumentException("Invalid document type: " + signaturesSearchVO.getDocType());
        }
    }

    public ApprovalSignaturesVO getApprovalSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        ApprovalSignaturesVO approvalSignaturesVO = new ApprovalSignaturesVO();
	    switch (signaturesSearchVO.getDocType()) {
		    case "OHC":
		    case "PAC":
			    approvalSignaturesVO = signaturesDAO.getApprovalCommitteeSignatures(signaturesSearchVO);
			    break;
		    case "RORAT":
			    approvalSignaturesVO = signaturesDAO.getApprovalRORATSignatures(signaturesSearchVO);
			    break;
		    case "TRR":
			    approvalSignaturesVO = signaturesDAO.getApprovalTRRSignatures(signaturesSearchVO);
			    break;
		    default:
			    throw new IllegalArgumentException("Invalid document type: " + signaturesSearchVO.getDocType());
	    }

        return approvalSignaturesVO;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveApprovalSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);
	    switch (signaturesSearchVO.getDocType()) {
		    case "OHC":
		    case "PAC":
			    signaturesDAO.saveApprovalCommitteeSignatures(signaturesSearchVO);
			    break;
		    case "RORAT":
			    signaturesDAO.saveApprovalRORATSignatures(signaturesSearchVO);
			    break;
		    case "TRR":
			    signaturesDAO.saveApprovalTRRSignatures(signaturesSearchVO);
			    break;
		    default:
			    throw new IllegalArgumentException("Invalid document type: " + signaturesSearchVO.getDocType());
	    }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteApprovalSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);
	    switch (signaturesSearchVO.getDocType()) {
		    case "OHC":
		    case "PAC":
			    signaturesDAO.deleteApprovalCommitteeSignatures(signaturesSearchVO);
			    break;
		    case "RORAT":
			    signaturesDAO.deleteApprovalRORATSignatures(signaturesSearchVO);
			    break;
		    case "TRR":
			    signaturesDAO.deleteApprovalTRRSignatures(signaturesSearchVO);
			    break;
		    default:
			    throw new IllegalArgumentException("Invalid document type: " + signaturesSearchVO.getDocType());
	    }
    }

    public List<SignaturesVO> getTbmSignatures(
            @Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        String[] parts = id.split("-");

        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        return signaturesDAO.getTbmSignatures(signaturesSearchVO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveTbmSignatures(@Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        signaturesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        String[] parts = id.split("-");
        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        signaturesDAO.saveTbmSignatures(signaturesSearchVO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteTbmSignatures(@Valid String id, @Valid SignaturesSearchVO signaturesSearchVO) {
        signaturesSearchVO.setHrId(SecurityUtil.getCurrentHrId());
        String[] parts = id.split("-");
        signaturesSearchVO.setWriteYear(parts[0]);
        signaturesSearchVO.setDocType(parts[1]);
        signaturesSearchVO.setDocNo(parts[2]);

        signaturesDAO.deleteTbmSignatures(signaturesSearchVO);
    }
}
