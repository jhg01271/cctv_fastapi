package kr.co.igns.mobile.riskReductions.service;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.riskReductions.dao.postgres.V1RiskReductionsDAO;
import kr.co.igns.mobile.riskReductions.model.RiskAssessmentVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsDetailVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsSaveVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsSearchVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.service.FemsFileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1RiskReductionsService {
//	private final V1RiskReductionsDAO v1RiskReductionsDAO;
//	private final FemsFileService femsFileService;
//	private final NasFileService nasFileService;
//    private final FileService fileService;
//	private final FileProperty property;
//
//	public List<RiskAssessmentVO> getRiskAssessments(@Valid RiskReductionsSearchVO reductionsSearchVO) {
//		List<RiskAssessmentVO> voList = v1RiskReductionsDAO.getRiskAssessments(reductionsSearchVO);
//
//		return voList;
//	}
//
//	public List<RiskReductionsVO> getRiskReductions(@Valid RiskReductionsSearchVO reductionsSearchVO) {
//		String riskAssessmentId = reductionsSearchVO.getRiskAssessmentId();
//		if (riskAssessmentId != null && !riskAssessmentId.isEmpty()) {
//		    String[] parts = riskAssessmentId.split("-");
//
//	        reductionsSearchVO.setWriteYear(parts[0]);
//	        reductionsSearchVO.setDocType(parts[1]);
//	        reductionsSearchVO.setDocNo(parts[2]);
//		}
//
//		reductionsSearchVO.setHrId(SecurityUtil.getCurrentHrId());
//
//		List<RiskReductionsVO> voList = v1RiskReductionsDAO.getRiskReductions(reductionsSearchVO);
//
//		return voList;
//	}
//
//	public RiskReductionsDetailVO getRiskReductionsDetail(@Valid String id, @Valid RiskReductionsSearchVO reductionsSearchVO) {
//	    String[] parts = id.split("-");
//
//        reductionsSearchVO.setWriteYear(parts[0]);
//        reductionsSearchVO.setDocType(parts[1]);
//        reductionsSearchVO.setDocNo(parts[2]);
//        reductionsSearchVO.setDocSeq(parts[3]);
//        reductionsSearchVO.setDocSeqDetail(parts[4]);
//
//		RiskReductionsDetailVO vo = v1RiskReductionsDAO.getRiskReductionsDetail(reductionsSearchVO);
//		return vo;
//	}
//
//    @Transactional(rollbackFor = Throwable.class)
//	public boolean saveRiskReductionsDetail(
//			String id,
//			String compId,
//			String content,
//			List<MultipartFile> beforeFiles,
//			List<MultipartFile> afterFiles,
//			List<String> deleteFiles) throws Exception {
//        String[] parts = id.split("-");
//
//        if(content != null && !content.isEmpty()) {
//            RiskReductionsSaveVO vo = RiskReductionsSaveVO.builder()
//            		.writeYear(parts[0])
//            		.docType(parts[1])
//            		.docNo(parts[2])
//            		.docSeq(parts[3])
//            		.docSeqDetail(parts[4])
//            		.content(content)
//            		.updatedBy(SecurityUtil.getCurrentMemberId())
//            		.build();
//            // 감소대책 개선조치 등록/변경
//            v1RiskReductionsDAO.saveRiskReductionsDetail(vo);
//        }
//
//        String beforeTargetId = parts[0] + parts[2] + parts[3] + parts[4] + "B";
//        beforeTargetId = StringUtils.isEmpty(beforeTargetId) ? SpUtils.getUuid() : beforeTargetId;
//
//        String afterTargetId = parts[0] + parts[2] + parts[3] + parts[4] + "A";
//        afterTargetId = StringUtils.isEmpty(afterTargetId) ? SpUtils.getUuid() : afterTargetId;
//        // 파일 저장
//		fileService.saveFile(beforeFiles, "RAP", beforeTargetId, compId);
//		fileService.saveFile(afterFiles, "RAP", afterTargetId, compId);
//		// 파일 삭제
//		if(deleteFiles != null && !deleteFiles.isEmpty()) {
//			for (String fileId : deleteFiles) {
//				if ("local".equals(property.getTarget())) {
//					femsFileService.deleteById(fileId);
//				} else if ("nas".equals(property.getTarget())) {
//					nasFileService.deleteById(fileId);
//				}
//			}
//		}
//
//		return true;
//	}
}
