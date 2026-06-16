package kr.co.igns.business.impl.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.planning.model.RiskAssessmentVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.HseKeyPerformanceResultVO;
import kr.co.igns.business.impl.service.HseKeyPerformanceResultService;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.planning.service.SafetyAndHealthObjectivesService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;


@Tag(name = "SafetyAndHealthObjectives", description = "안전보건목표")
@RestController
@RequiredArgsConstructor
public class HseKeyPerformanceResultController {

    private final MessageService messageService;
    private final HseKeyPerformanceResultService service;

    @Operation(summary = "[HSE 실적 조회]", description = "HSE 실적 메뉴를 조회한다")
    @GetMapping(value = "/safewizpro/impl/getHsePerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePerformance(
            @ModelAttribute @Valid SpSearchVO vo) throws Exception {
        List<RiskAssessmentVO> list = service.getHsePerformance(vo);

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

    @Operation(summary = "[안전보건목표 메인 조회]", description = "안전보건목표 메인 정보를 조회한다")
    @GetMapping(value = "/safewizpro/impl/getSafetyAndHealthObjectives")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthObjectives(
            @ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
        List<SafetyAndHealthObjectivesVO> list = service.getSafetyAndHealthObjectives(vo);

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
    
    @Operation(summary = "[안전보건목표 상세 조회]", description = "안전보건목표 메인 상세 정보를 조회한다")
    @GetMapping(value = "/safewizpro/impl/getSafetyAndHealthObjectivesDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthObjectivesDetail(
            @ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
        List<SafetyAndHealthObjectivesVO> list = service.getSafetyAndHealthObjectivesDetail(vo);

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

    @Operation(summary = "[HSE 추진실적 저장]", description = "HSE 추진실적을 저장한다")
    @PostMapping(value = "/safewizpro/impl/saveHseKeyPerformanceResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyAndHealthObjectives(@RequestBody @Valid List<HseKeyPerformanceResultVO> reqVo) throws Exception {
    	service.save(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[HSE 추진실적 삭제]", description = "HSE 추진실적을 삭제한다")
    @DeleteMapping(value = "/safewizpro/impl/delHseKeyPerformanceResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> delHseKeyPerformanceResult(@RequestBody @Valid List<HseKeyPerformanceResultVO> reqVo) throws Exception {
    	service.delete(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[HSE 추진실적 리포트]", description = "HSE 추진실적 리포트를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/getHseKeyPerformanceResultReport")
    public void getSafetyHealthObjectivesReport(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid SpSearchVO spSearchVO) throws Exception {
        service.getReport(request, response, spSearchVO);
    }

    @Operation(summary = "[HSE 추진실적 리포트(목록)]", description = "HSE 추진실적 리포트를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/getHseKeyPerformanceResultReportList")
    public void getSafetyHealthObjectivesReportList(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid SpSearchVO spSearchVO) throws Exception {
        service.getReportList(request, response, spSearchVO);
    }
}
