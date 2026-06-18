package kr.co.igns.mobile.hsePolicy.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.hsePolicy.model.HsePolicyDetailVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyPdfVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyRepliesVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicySearchVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyVO;
import kr.co.igns.system.common.model.FileVO;

@Mapper
@Repository
public interface V1HsePolicyDAO {
	// 안전보건 경영방침 조회
	List<HsePolicyVO> getHsePolicyList(HsePolicySearchVO hsePolicySearchVO);
	// 안전보건 경영방침 상세 조회
	HsePolicyDetailVO getHsePolicyDetail(HsePolicySearchVO hsePolicySearchVO);
	// 안전보건 경영방침 pdf 조회
	HsePolicyPdfVO getHsePolicyPDF(HsePolicySearchVO hsePolicySearchVO);
	// 안전보건 경영방침 이미지 파일 정보 조히(dataType: 0002)
	FileVO getHsePolicyFileInfo(HsePolicyPdfVO hsePolicyPdfVO);
	// 안전보건 경영방침 동의
	void agreeHsePolicy(HsePolicySearchVO hsePolicySearchVO);
	// 안전보건 경영방침 의견 목록 조회
	List<HsePolicyRepliesVO> getHsePolicyReplies(HsePolicySearchVO hsePolicySearchVO);
	// 안전보건 경영방침 의견 등록
	HsePolicySearchVO saveHsePolicyReplies(HsePolicySearchVO hsePolicySearchVO);
}
