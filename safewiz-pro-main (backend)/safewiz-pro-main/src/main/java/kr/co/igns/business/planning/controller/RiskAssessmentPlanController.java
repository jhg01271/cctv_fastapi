package kr.co.igns.business.planning.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.RiskAssessmentPlanVO;
import kr.co.igns.business.planning.service.RiskAssessmentPlanService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : RiskAssessmentPlan
 * ※ 기능명 : 위험성평가 계획
 * ※ 작성자 : 장석천
 * ※ 최초생성일 : 2024. 11. 06.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RiskAssessmentPlan", description = "위험성평가 계획")
@RestController
@RequiredArgsConstructor
public class RiskAssessmentPlanController {

    private final RiskAssessmentPlanService riskAssessmentPlanService;
    private final MessageService messageService;

    @Operation(summary = "[위험성평가 계획 목록조회]")
    @GetMapping(value = "/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentPlan(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<RiskAssessmentPlanVO> resultList = riskAssessmentPlanService.getRiskAssessmentPlanList(searchVo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null || resultList.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 계획 조직(공정/설비/물질 구분) 팝업 조회]")
    @GetMapping(value = "/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanOrgnPopupList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentPlanOrgnPopupList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<RiskAssessmentPlanVO> resultList = riskAssessmentPlanService.getRiskAssessmentPlanOrgnPopupList(searchVo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null || resultList.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 계획 이전 정보 버튼 팝업 조회]")
    @GetMapping(value = "/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanPrevPopupList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentPlanPrevPopupList(
            @ModelAttribute @Valid RiskAssessmentPlanVO vo) throws Exception {

        List<RiskAssessmentPlanVO> resultList = riskAssessmentPlanService.getRiskAssessmentPlanPrevPopupList(vo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null || resultList.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 계획 목록 추가]")
    @PostMapping(value = "/safewizpro/planning/riskAssessmentPlan/insertRiskAssessmentPrevPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertRiskAssessmentPrevPlan(
            @RequestBody @Valid List<RiskAssessmentPlanVO> reqVO) throws Exception {

        riskAssessmentPlanService.insertRiskAssessmentPrevPlan(reqVO);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", reqVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), hashMap);
    }

    @Operation(summary = "[위험성평가 계획 추가]")
    @PostMapping(value = "/safewizpro/planning/riskAssessmentPlan/insertRiskAssessmentPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertRiskAssessmentPlan(
            @RequestBody @Valid RiskAssessmentPlanVO reqVO) throws Exception {

        riskAssessmentPlanService.insertRiskAssessmentPlan(reqVO);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", reqVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), hashMap);
    }

    @Operation(summary = "[위험성평가 계획 상세조회]")
    @GetMapping(value = "/safewizpro/planning/riskAssessmentPlan/getRiskAssessmentPlanDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentPlanDetail(
            @ModelAttribute @Valid RiskAssessmentPlanVO vo) throws Exception {

        vo = riskAssessmentPlanService.getRiskAssessmentPlanDetail(vo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", vo);

        if (vo == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 계획 수정]")
    @PostMapping(value = "/safewizpro/planning/riskAssessmentPlan/updateRiskAssessmentPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateRiskAssessmentPlan(
            @RequestBody @Valid RiskAssessmentPlanVO reqVO) throws Exception {

        riskAssessmentPlanService.updateRiskAssessmentPlan(reqVO);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", reqVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), hashMap);
    }

    @Operation(summary = "[위험성평가 계획 삭제]")
    @PostMapping(value = "/safewizpro/planning/riskAssessmentPlan/deleteRiskAssessmentPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskAssessmentPlan(
            @RequestBody @Valid List<RiskAssessmentPlanVO> voList) throws Exception {

        riskAssessmentPlanService.deleteRiskAssessmentPlan(voList);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", null);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), hashMap);
    }
    
    @Operation(summary = "[위험성평가 계획 요인, 감소대책, 평균 위험도 조회]")
    @GetMapping(value = "/safewizpro/planning/riskAssessmentPlan/getRiskAvgCount")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAvgCount(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

    	RiskAssessmentPlanVO resultList = riskAssessmentPlanService.getRiskAvgCount(searchVo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

}
