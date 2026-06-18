package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

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
import kr.co.igns.business.planning.model.RiskAndOpsAsmtCriteriaVO;
import kr.co.igns.business.planning.service.RiskAndOpsAsmtCriteriaService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller
 * ※ 파일명 : RiskAndOpsAsmtCriteria
 * ※ 기능명 : 리스크 및 기회 평가 기준표
 * ※ 작성자 : 손상규
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RiskAndOpsAsmtCriteria", description = "평가 기준표")
@RestController
@RequiredArgsConstructor
public class RiskAndOpsAsmtCriteriaController {
	private final MessageService messageService;
    private final RiskAndOpsAsmtCriteriaService riskAndOpsAsmtCriteriaService;
    
    @Operation(summary = "[리스크와 기회 첫페이지 조회]", description = "리스크와 기회 첫페이지를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskMain")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskMain(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<RiskAndOpsAsmtCriteriaVO> list = riskAndOpsAsmtCriteriaService.getRiskMain(searchVo);

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
    
    @Operation(summary = "[평가 기준표 조회]", description = "평가 기준표를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskAndOpsAsmtCriteria")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAndOpsAsmtCriteria(
    		@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
    	List<RiskAndOpsAsmtCriteriaVO> list = riskAndOpsAsmtCriteriaService.getRiskAndOpsAsmtCriteria(searchVo);
    	
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
  
    @Operation(summary = "[평가 기준표 상세 조회]", description = "평가 기준표를 상세 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskAndOpsAsmtCriteriaDetailList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAndOpsAsmtCriteriaDetailList(
            @ModelAttribute @Valid RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO) throws Exception {
        List<RiskAndOpsAsmtCriteriaVO> list = riskAndOpsAsmtCriteriaService.getRiskAndOpsAsmtCriteriaDetailList(riskAndOpsAsmtCriteriaVO);

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
    
    @Operation(summary = "[평가 기준표 데이터셋 조회]", description = "평가 기준표 데이터셋을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getDatasetAsmtList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetAsmtList(
    		@ModelAttribute @Valid RiskAndOpsAsmtCriteriaVO riskAndOpsAsmtCriteriaVO) throws Exception {
    	List<RiskAndOpsAsmtCriteriaVO> list = riskAndOpsAsmtCriteriaService.getDatasetAsmtList(riskAndOpsAsmtCriteriaVO);
    	
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
    
    @Operation(summary = "[평가 기준표 저장]", description = "평가 기준표 메인, 상세 내용을 저장한다")
    @PostMapping(value = "/safewizpro/planning/saveRiskAndOpsAsmtCriteria")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskAndOpsAsmtCriteria(@RequestBody @Valid List<RiskAndOpsAsmtCriteriaVO> saveVo) throws Exception {

        List<RiskAndOpsAsmtCriteriaVO> rtnDto = riskAndOpsAsmtCriteriaService.saveRiskAndOpsAsmtCriteria(saveVo);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", rtnDto);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[평가 기준표 확정여부 저장]", description = "평가 기준표 확정여부를 저장한다")
    @PostMapping(value = "/safewizpro/planning/saveMainConfirm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMainConfirm(@RequestBody @Valid List<RiskAndOpsAsmtCriteriaVO> saveVo) throws Exception {
    	
    	List<RiskAndOpsAsmtCriteriaVO> rtnDto = riskAndOpsAsmtCriteriaService.saveMainConfirm(saveVo);
    	
    	HashMap<String, Object> result = new HashMap<>();
    	result.put("result", rtnDto);
    	
    	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[평가 기준표 삭제]", description = "평가 기준표 메인을 삭제한다")
    @PostMapping(value = "/safewizpro/planning/deleteRiskAndOpsAsmtCriteriaMain")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskAndOpsAsmtCriteriaMain(@RequestBody @Valid List<RiskAndOpsAsmtCriteriaVO> saveVo) throws Exception {
    	
    	List<RiskAndOpsAsmtCriteriaVO> rtnDto = riskAndOpsAsmtCriteriaService.deleteRiskAndOpsAsmtCriteriaMain(saveVo);
    	
    	HashMap<String, Object> result = new HashMap<>();
    	result.put("result", rtnDto);
    	
    	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }
    @Operation(summary = "[평가 기준표 삭제]", description = "평가 기준표 디테일을 삭제한다")
    @PostMapping(value = "/safewizpro/planning/deleteRiskAndOpsAsmtCriteriaDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskAndOpsAsmtCriteriaDetail(@RequestBody @Valid List<RiskAndOpsAsmtCriteriaVO> saveVo) throws Exception {
    	
    	List<RiskAndOpsAsmtCriteriaVO> rtnDto = riskAndOpsAsmtCriteriaService.deleteRiskAndOpsAsmtCriteriaDetail(saveVo);
    	
    	HashMap<String, Object> result = new HashMap<>();
    	result.put("result", rtnDto);
    	
    	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }
}
