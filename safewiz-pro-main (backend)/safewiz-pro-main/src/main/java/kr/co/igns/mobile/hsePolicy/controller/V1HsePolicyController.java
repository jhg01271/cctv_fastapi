package kr.co.igns.mobile.hsePolicy.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyDetailVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyRepliesVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicySearchVO;
import kr.co.igns.mobile.hsePolicy.model.HsePolicyVO;
import kr.co.igns.mobile.hsePolicy.service.V1HsePolicyService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.hsePolicy.controller
 * ※ 파일명 : Committees
 * ※ 기능명 : 안전보건 경영방침
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 02.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "HsePolicy", description = "안전보건 경영방침")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1HsePolicyController {
//    private final MessageService messageService;
//    private final V1HsePolicyService v1HsePolicyService;
//
//    @Operation(summary = "[안전보건 경영방침 목록 조회]", description = "사업장의 안전보건 경영방침 목록을 조회하는 API")
//    @GetMapping(value = "/hsePolicies")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicy(
//            @ModelAttribute @Valid HsePolicySearchVO hsePolicySearchVO) throws Exception {
//    	List<HsePolicyVO> list = v1HsePolicyService.getHsePolicy(hsePolicySearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("list", list);
//
//        if (result == null || result.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), result);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), result);
//        }
//    }
//
//    @Operation(summary = "[안전보건 경영방침 상세 조회]", description = "안전보건 경영방침 상세 조회 API")
//    @GetMapping(value = "/hsePolicies/{id}")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyDetail(
//    		@PathVariable("id") String id, @ModelAttribute @Valid HsePolicySearchVO hsePolicySearchVO) throws Exception {
//    	HsePolicyDetailVO data = v1HsePolicyService.getHsePolicyDetail(id, hsePolicySearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", data);
//
//        if (result == null || result.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), result);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), result);
//        }
//    }
//
//	@Operation(summary = "[안전보건 경영방침 PDF 다운로드]", description = "특정 안전보건 경영방침의 내용을 담고 있는 PDF 다운로드 API")
//	@PostMapping(value="/hsePolicies/{id}/pdf")
//	public void getHsePolicyPDF(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable("id") String id, @RequestBody HsePolicySearchVO hsePolicySearchVO) throws Exception {
//		v1HsePolicyService.getHsePolicyPDF(request, response, id, hsePolicySearchVO);
//	}
//
//    @Operation(summary = "[안전보건 경영방침 동의]", description = "현재 사용자가 특정 안전보건 경영방침을 동의하는 API")
//    @PostMapping(value = "/hsePolicies/{id}/agreements")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> agreeHsePolicy(
//    		@PathVariable("id") String id, @RequestBody HsePolicySearchVO hsePolicySearchVO) throws Exception {
//    	v1HsePolicyService.agreeHsePolicy(id, hsePolicySearchVO);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", hsePolicySearchVO);
//
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
//				   messageService.getMessage("updateSuccess.msg"), result);
//    }
//
//    @Operation(summary = "[안전보건 경영방침 의견 목록 조회]", description = "특정 안전보건 경영방침에 등록된 근로자 의견 목록을 조회하는 API")
//    @GetMapping(value = "/hsePolicies/{id}/replies")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyReplies(
//    		@PathVariable("id") String id, @ModelAttribute @Valid HsePolicySearchVO hsePolicySearchVO) throws Exception {
//    	List<HsePolicyRepliesVO> list = v1HsePolicyService.getHsePolicyReplies(id, hsePolicySearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("list", list);
//
//        if (result == null || result.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), result);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), result);
//        }
//    }
//
//    @Operation(summary = "[안전보건 경영방침 의견 등록]", description = "특정 안전보건 경영방침에 댓글을 등록하는 API")
//    @PostMapping(value = "/hsePolicies/{id}/replies")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHsePolicyReplies(
//    		@PathVariable("id") String id, @RequestBody @Valid HsePolicySearchVO hsePolicySearchVO) throws Exception {
//    	v1HsePolicyService.saveHsePolicyReplies(id, hsePolicySearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", hsePolicySearchVO);
//
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
//				   messageService.getMessage("updateSuccess.msg"), result);
//    }
}
