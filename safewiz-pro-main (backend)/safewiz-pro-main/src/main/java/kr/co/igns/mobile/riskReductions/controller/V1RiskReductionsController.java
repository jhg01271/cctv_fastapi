package kr.co.igns.mobile.riskReductions.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.riskReductions.model.RiskAssessmentVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsDetailVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsSearchVO;
import kr.co.igns.mobile.riskReductions.model.RiskReductionsVO;
import kr.co.igns.mobile.riskReductions.service.V1RiskReductionsService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.riskReductions.controller
 * ※ 파일명 : V1RiskReductionsController
 * ※ 기능명 : 위험성평가 이행 
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 17.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "RiskReductions", description = "위험성평가 이행")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1RiskReductionsController {
//    private final MessageService messageService;
//    private final V1RiskReductionsService v1RiskReductionsService;
//
//    @Operation(summary = "[위험성평가 목록 조회]", description = "속된 사업장의 전체 위험성평가 목록을 조회하는 API")
//    @GetMapping(value = "/riskAssessments")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessments(
//            @ModelAttribute @Valid RiskReductionsSearchVO riskReductionsSearchVO) throws Exception {
//    	List<RiskAssessmentVO> list = v1RiskReductionsService.getRiskAssessments(riskReductionsSearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("list", list);
//
//        if (list == null || list.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), result);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), result);
//        }
//    }
//
//    @Operation(summary = "[감소대책 목록 조회]", description = "사업장의 위험성평가 이행의 감소대책 목록을 조회")
//    @GetMapping(value = "/riskReductions")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskReductions(
//            @ModelAttribute @Valid RiskReductionsSearchVO riskReductionsSearchVO) throws Exception {
//    	List<RiskReductionsVO> list = v1RiskReductionsService.getRiskReductions(riskReductionsSearchVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("list", list);
//
//        if (list == null || list.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), result);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), result);
//        }
//    }
//
//    @Operation(summary = "[감소대책 상세 조회]", description = "사업장의 위험성평가 이행의 감소대책 상세 정보를 조회")
//    @GetMapping(value = "/riskReductions/{id}")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskReductionsDetail(
//    		@PathVariable("id") String id, @ModelAttribute @Valid RiskReductionsSearchVO riskReductionsSearchVO) throws Exception {
//    	RiskReductionsDetailVO data = v1RiskReductionsService.getRiskReductionsDetail(id, riskReductionsSearchVO);
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
//    @Operation(summary = "[감소대책 개선조치 등록/변경]", description = "사업장의 위험성평가 이행 감소대책을 개선조치를 등록/변경하는 API")
//    @PostMapping(value = "/riskReductions/{id}/implementations", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskReductionsDetail(
//    		MultipartHttpServletRequest mtfRequest, HttpServletRequest request,
//            @PathVariable("id") String id,
//            @RequestPart(value = "content", required = false) String content,
//            @RequestPart(value = "compId") String compId,
//            @RequestPart(value = "beforeFiles", required = false)  List<MultipartFile> beforeFiles,
//            @RequestPart(value = "afterFiles", required = false) List<MultipartFile> afterFiles,
//            @RequestPart(value = "deleteFiles", required = false) List<String> deleteFiles) throws Exception {
//    	boolean flag = v1RiskReductionsService.saveRiskReductionsDetail(id, compId, content, beforeFiles, afterFiles, deleteFiles);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", flag);
//
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
//				   messageService.getMessage("updateSuccess.msg"), result);
//    }
}
