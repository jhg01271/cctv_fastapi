package kr.co.igns.business.planning.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.RisksAndOpportunitieVO;
import kr.co.igns.business.planning.service.RisksAndOpportunitieService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller
 * ※ 파일명 : RisksAndOpportunitie
 * ※ 기능명 : 리스크 및 기회 관리대장
 * ※ 작성자 : 손상규
 * ※ 최초생성일 : 2024. 10. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RisksAndOpportunitie", description = "관리대장")
@RestController
@RequiredArgsConstructor
public class RisksAndOpportunitieController {
	private final MessageService messageService;
    private final RisksAndOpportunitieService risksAndOpportunitieService;
    
    @Operation(summary = "[관리대장 메인 조회]", description = "관리대장 메인을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRisksAndOpportunities")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRisksAndOpportunities(
            @ModelAttribute @Valid RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> list = risksAndOpportunitieService.getRisksAndOpportunities(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[관리대장 상세 조회 (리스크)]", description = "관리대장 상세 조회를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskDetail(
    		@ModelAttribute @Valid RisksAndOpportunitieVO vo) throws Exception {
    	List<RisksAndOpportunitieVO> list = risksAndOpportunitieService.getRiskDetail(vo);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("list", list);
    	
    	if (list == null || list.isEmpty()) {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
    				messageService.getMessage("noResultFound.msg"), result);
    	} else {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
    				messageService.getMessage("readSuccess.msg"), result);
    	}
    }
    @Operation(summary = "[관리대장 상세 조회] (기회)", description = "관리대장 상세 조회를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getOppDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOppDetail(
    		@ModelAttribute @Valid RisksAndOpportunitieVO vo) throws Exception {
    	List<RisksAndOpportunitieVO> list = risksAndOpportunitieService.getOppDetail(vo);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("list", list);
    	
    	if (list == null || list.isEmpty()) {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
    				messageService.getMessage("noResultFound.msg"), result);
    	} else {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
    				messageService.getMessage("readSuccess.msg"), result);
    	}
    }
    @Operation(summary = "[관리대장 상세 조회] (참여자)", description = "관리대장 상세 조회를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getParDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getParDetail(
    		@ModelAttribute @Valid RisksAndOpportunitieVO vo) throws Exception {
    	List<RisksAndOpportunitieVO> list = risksAndOpportunitieService.getParDetail(vo);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("list", list);
    	
    	if (list == null || list.isEmpty()) {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
    				messageService.getMessage("noResultFound.msg"), result);
    	} else {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
    				messageService.getMessage("readSuccess.msg"), result);
    	}
    }
    
    @Operation(summary = "[평가기준표 api]", description = "평가기준표 상세 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getAsmtApi")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAsmtApi(
    		@ModelAttribute @Valid RisksAndOpportunitieVO vo) throws Exception {
    	List<RisksAndOpportunitieVO> list = risksAndOpportunitieService.getAsmtApi(vo);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("list", list);
    	
    	if (list == null || list.isEmpty()) {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
    				messageService.getMessage("noResultFound.msg"), result);
    	} else {
    		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
    				messageService.getMessage("readSuccess.msg"), result);
    	}
    }
    
    @Operation(summary = "[관리대장 저장]", description = "관리대장 메인, 상세 내용을 저장한다")
    @PostMapping(value = "/safewizpro/planning/saveRisksAndOpportunities")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRisksAndOpportunities(@RequestBody @Valid List<RisksAndOpportunitieVO> reqVo) throws Exception {
    	risksAndOpportunitieService.saveRisksAndOpportunities(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[관리대장 메인 삭제]", description = "관리대장 메인을 삭제한다")
    @PostMapping(value = "/safewizpro/planning/deleteRisksAndOpportunitesMain")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRisksAndOpportunitesMain(@RequestBody @Valid List<RisksAndOpportunitieVO> reqVo) throws Exception {
    	risksAndOpportunitieService.deleteRisksAndOpportunitesMain(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[관리대장 상세 삭제]", description = "관리대장 상세를 삭제한다")
    @PostMapping(value = "/safewizpro/planning/deleteRisksAndOpportunitesDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRisksAndOpportunitesDetail(@RequestBody @Valid List<RisksAndOpportunitieVO> reqVo) throws Exception {
    	risksAndOpportunitieService.deleteRisksAndOpportunitesDetail(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    /* --------------------------------------- 작업중 --------------------------------------- */
    @Operation(summary = "[리스크 및 기회 관리대장 리포트]", description = "리스크 및 기회 관리대장 리포트 정보를 조회한다")
    @PostMapping(value = "/safewizpro/planning/getRisksAndOpportunitiesReport")
    public void getRisksAndOpportunitiesReport(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid List<RisksAndOpportunitieVO> spSearchVO) throws Exception {
		risksAndOpportunitieService.getRisksAndOpportunitiesReport(request, response, spSearchVO);
    }
    
    @Operation(summary = "[리스크 및 기회 관리대장 리포트(카드)]", description = "리스크 및 기회 관리대장(카드) 리포트 정보를 조회한다")
    @PostMapping(value = "/safewizpro/planning/getRisksAndOpportunitiesCardReport")
    public void getRisksAndOpportunitiesCardReport(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid List<RisksAndOpportunitieVO> spSearchVO) throws Exception {
		risksAndOpportunitieService.getRisksAndOpportunitiesCardReport(request, response, spSearchVO);
    }
}
