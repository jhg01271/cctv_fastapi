package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.RiskAssessmentOrganChartVO;
import kr.co.igns.business.planning.model.RiskOrgnRoleVO;
import kr.co.igns.business.planning.service.RiskAssessmentOrganChartService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller;
 * ※ 파일명 : RiskAssessmentOrganChartController
 * ※ 기능명 : 위험성평가 조직도 관리
 * ※ 작성자 : 윤다영
 * ※ 최초생성일 : 2024. 10. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RiskAssessmentOrganChart")
@RestController
@RequiredArgsConstructor
public class RiskAssessmentOrganChartController {
    private final MessageService messageService;    
    private final RiskAssessmentOrganChartService riskAssessmentOrganChartService;

    @Operation(summary = "[위험성평가 조직 역할 구성 데이터 조회]", description = "위험성평가 조직 역할 구성 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskOrgList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<RiskOrgnRoleVO> list = riskAssessmentOrganChartService.getRiskOrgList(searchVo);

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

    @Operation(summary = "[위험성평가 조직 역할 구성 예시 데이터 조회]", description = "위험성평가 조직 역할 구성 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getDataSetRiskOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetRiskOrgList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<RiskOrgnRoleVO> list = riskAssessmentOrganChartService.getDataSetRiskOrgList(searchVo);

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

    @Operation(summary = "[위험성평가 조직 역할 구성 데이터 등록]", description = "위험성평가 조직 역할 구성 데이터를 저장, 수정, 삭제한다.")
    @PostMapping(value = "/safewizpro/planning/saveRiskOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskOrgList(
            @RequestBody @Valid List<RiskOrgnRoleVO> reqVo)
            throws Exception {
        List<RiskOrgnRoleVO> rtnDto = riskAssessmentOrganChartService.saveRiskOrgList(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }


    
    @Operation(summary = "[인원 조회]", description = "인원을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getMembers")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMembers(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HrVO> list = riskAssessmentOrganChartService.getMembers(searchVo);

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
    
    @Operation(summary = "[조직 차트 목록 조회]", description = "조직도 목록을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskAssessmentOrganChartList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentOrganChartList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<RiskAssessmentOrganChartVO> list = riskAssessmentOrganChartService.getRiskAssessmentOrganChartList(searchVo);

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
    
    @Operation(summary = "[조직 차트 상세 조회]", description = "조직도 상세 정보를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskAssessmentOrganChartDetail/{chartId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentOrganChartDetail(
        @PathVariable String chartId) throws Exception {
        
        RiskAssessmentOrganChartVO list = riskAssessmentOrganChartService.getRiskAssessmentOrganChartDetail(chartId);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[조직도 등록 및 수정]", description = "위험성평가 조직을 등록한다.")
    @PostMapping(value = "/safewizpro/planning/saveRiskAssessmentOrganChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskAssessmentOrganChart(
            @RequestBody @Valid RiskAssessmentOrganChartVO reqVo)
            throws Exception {
    	
        BaseVO rtnDto = riskAssessmentOrganChartService.saveRiskAssessmentOrganChart(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[조직도 확정]", description = "조직도 확정 여부를 수정한다")
    @PutMapping(value = "/safewizpro/planning/confirmRiskAssessmentOrganChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmRiskAssessmentOrganChart(
            @RequestBody @Valid RiskAssessmentOrganChartVO reqVO) throws Exception {
        BaseVO rtnDto = riskAssessmentOrganChartService.confirmRiskAssessmentOrganChart(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[조직도 일괄 삭제]", description = "조직도를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteRiskAssessmentOrganChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskAssessmentOrganChart(
            @RequestBody List<RiskAssessmentOrganChartVO> list)
            throws Exception {
    	riskAssessmentOrganChartService.deleteRiskAssessmentOrganChart(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[위험성평가 조직도 출력]", description = "위험성평가 조직도 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/planning/getRiskAssessmentOrganChartReport")
    public void getRiskAssessmentOrganChartReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            riskAssessmentOrganChartService.getRiskAssessmentOrganChartReport(request, response, spSearchVO);
    }
}
