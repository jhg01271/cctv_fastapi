package kr.co.igns.mobile.hsePolicy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.participation.service.HsePolicyService;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.hsePolicy.dao.postgres.V1HsePolicyDAO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyDetailVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyPdfVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyRepliesVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicySearchVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.setting.service.FemsFileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class V1HsePolicyService {
	private final FileProperty property;
	private final FemsFileService femsFileService;
	private final NasFileService nasFileService;
    private final HsePolicyService hsePolicyService;
	private final V1HsePolicyDAO v1HsePolicyDAO;
	
//	public List<HsePolicyVO> getHsePolicy(@Valid HsePolicySearchVO hsePolicySearchVO) {
//		List<HsePolicyVO> hsePolicyList = v1HsePolicyDAO.getHsePolicyList(hsePolicySearchVO);
//
//		return hsePolicyList;
//	}
//
//	public HsePolicyDetailVO getHsePolicyDetail(@Valid String id, @Valid HsePolicySearchVO hsePolicySearchVO) {
//        String[] parts = id.split("-");
//
//        hsePolicySearchVO.setWriteYear(parts[0]);
//        hsePolicySearchVO.setDocType(parts[1]);
//        hsePolicySearchVO.setDocNo(parts[2]);
//        hsePolicySearchVO.setHrId(SecurityUtil.getCurrentHrId());
//
//		HsePolicyDetailVO hsePolicyDetailVO = v1HsePolicyDAO.getHsePolicyDetail(hsePolicySearchVO);
//
//		return hsePolicyDetailVO;
//	}
//
//	public void getHsePolicyPDF(HttpServletRequest request, HttpServletResponse response, String id, HsePolicySearchVO hsePolicySearchVO) throws Exception {
//        String[] parts = id.split("-");
//
//        hsePolicySearchVO.setWriteYear(parts[0]);
//        hsePolicySearchVO.setDocType(parts[1]);
//        hsePolicySearchVO.setDocNo(parts[2]);
//
//        HsePolicyPdfVO hsePolicyPdfVO = v1HsePolicyDAO.getHsePolicyPDF(hsePolicySearchVO);
//
//        if(hsePolicyPdfVO == null) {
//    		return;
//        }
//
//        if(hsePolicyPdfVO.getDataType().equals("0002")) {
//        	hsePolicyPdfVO.setCompId(hsePolicySearchVO.getCompId());
//        	FileVO fileVO = v1HsePolicyDAO.getHsePolicyFileInfo(hsePolicyPdfVO);
//
//    		if ("local".equals(property.getTarget())) {
//    			femsFileService.download(request, response, fileVO.getFileId());
//    		} else if ("nas".equals(property.getTarget())) {
//    			nasFileService.download(request, response, fileVO.getFileId());
//    		}
//        } else if(hsePolicyPdfVO.getDataType().equals("0001")) {
//        	SpSearchVO spSearchVO = SpSearchVO.builder()
//        			.compId(hsePolicySearchVO.getCompId())
//					.writeYear(hsePolicySearchVO.getWriteYear())
//					.docNo(hsePolicySearchVO.getDocNo())
//					.docType(hsePolicySearchVO.getDocType())
//					.build();
//
//            hsePolicyService.getHsePolicyReport(request, response, spSearchVO);
//        } else {
//        	throw new IllegalArgumentException("Invalid Data type: " + hsePolicyPdfVO.getDataType());
//        }
//	}
//
//    @Transactional(rollbackFor = Throwable.class)
//	public void agreeHsePolicy(@Valid String id, @Valid HsePolicySearchVO hsePolicySearchVO) {
//        String[] parts = id.split("-");
//
//        hsePolicySearchVO.setWriteYear(parts[0]);
//        hsePolicySearchVO.setDocType(parts[1]);
//        hsePolicySearchVO.setDocNo(parts[2]);
//        hsePolicySearchVO.setHrId(SecurityUtil.getCurrentHrId());
//
//        v1HsePolicyDAO.agreeHsePolicy(hsePolicySearchVO);
//	}
//
//	public List<HsePolicyRepliesVO> getHsePolicyReplies(@Valid String id, @Valid HsePolicySearchVO hsePolicySearchVO) {
//        String[] parts = id.split("-");
//
//        hsePolicySearchVO.setWriteYear(parts[0]);
//        hsePolicySearchVO.setDocType(parts[1]);
//        hsePolicySearchVO.setDocNo(parts[2]);
//
//		List<HsePolicyRepliesVO> hsePolicyRepliesVOList = v1HsePolicyDAO.getHsePolicyReplies(hsePolicySearchVO);
//
//		return hsePolicyRepliesVOList;
//	}
//
//    @Transactional(rollbackFor = Throwable.class)
//	public void saveHsePolicyReplies(@Valid String id, @Valid HsePolicySearchVO hsePolicySearchVO) {
//        String[] parts = id.split("-");
//
//        hsePolicySearchVO.setWriteYear(parts[0]);
//        hsePolicySearchVO.setDocType(parts[1]);
//        hsePolicySearchVO.setDocNo(parts[2]);
//        hsePolicySearchVO.setHrId(SecurityUtil.getCurrentHrId());
//
//        v1HsePolicyDAO.saveHsePolicyReplies(hsePolicySearchVO);
//	}
}
