package kr.co.igns.mobile.riskReports.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.riskReports.model.RiskReportsCountVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsDetailVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSaveVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsSearchVO;
import kr.co.igns.mobile.riskReports.model.RiskReportsVO;
import kr.co.igns.mobile.riskReports.service.V1RiskReportsService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.riskReports.controller
 * ※ 파일명 : Committees
 * ※ 기능명 : 근로자 참여
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 04.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "RiskReports", description = "근로자 참여")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1RiskReportsController {
    private final MessageService messageService;
    private final V1RiskReportsService v1RiskReportsService;
    
    @Operation(summary = "[근로자 참여 누적횟수 조회]", description = "사업장 내에서 자신이 보고한 유해위험요인 근로자 참여 누적개수를 조회하는 API")
    @GetMapping(value = "/riskReports/count")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskReportsCount(
            @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	RiskReportsCountVO data = v1RiskReportsService.getRiskReportsCount(riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[근로자 참여 목록 조회]", description = "사업장 내에서 자신이 보고한 유해위험요인 근로자 참여 목록 조회하는 API")
    @GetMapping(value = "/riskReports")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskReports(
            @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	List<RiskReportsVO> list = v1RiskReportsService.getRiskReports(riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[근로자 참여 상세 조회]", description = "사업장 내에서 자신이 보고한 유해위험요인 근로자 참여의 상세 정보를 조회하는 API")
    @GetMapping(value = "/riskReports/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskReportsDetail(
    		@PathVariable("id") String id, @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	RiskReportsDetailVO data = v1RiskReportsService.getRiskReportsDetail(id, riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[근로자 참여 서명 등록]", description = "사업장내 유해위험요인 근로자 참여에 자신을 서명을 등록하는 API")
    @PutMapping(value = "/riskReports/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskReportsSignatures(
    		@PathVariable("id") String id, @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	v1RiskReportsService.saveRiskReportsSignatures(id, riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", riskReportsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여 서명 취소]", description = "사업장내 유해위험요인 근로자 참여에 등록된 자신의 서명을 취소하는 API")
    @DeleteMapping(value = "/riskReports/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskReportsSignatures(
    		@PathVariable("id") String id, @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	v1RiskReportsService.deleteRiskReportsSignatures(id, riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", riskReportsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여의 검토자 의견 변경]", description = "사업장내 유해위험요인 근로자 참여의 검토자 의견을 변경하는 API")
    @PutMapping(value = "/riskReports/{id}/replies")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateRiskReportsReplies(
    		@PathVariable("id") String id, @RequestBody RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	v1RiskReportsService.updateRiskReportsReplies(id, riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", riskReportsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여 검토자 저장]", description = "사업장내 유해위험요인 근로자 참여의 검토자를 저장하는 API")
    @PostMapping(value = "/riskReports/{id}/reviewer")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskReportsReviewer(
    		@PathVariable("id") String id, @RequestBody RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	v1RiskReportsService.saveRiskReportsReviewer(id, riskReportsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", riskReportsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여 삭제하기]", description = "사업장내 유해위험요인 근로자 참여를 삭제하는 API")
    @DeleteMapping(value = "/riskReports/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteRiskReports(
    		@PathVariable("id") String id, @ModelAttribute @Valid RiskReportsSearchVO riskReportsSearchVO) throws Exception {
    	v1RiskReportsService.deleteRiskReports(id, riskReportsSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", riskReportsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여 등록]", description = "사업장내 신규 유해위험요인 근로자 참여를 등록하는 API")
    @PostMapping(value = "/riskReports")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveRiskReports(
    		@Param("compId") String compId, @RequestBody RiskReportsSaveVO riskReportsSaveVO) throws Exception {
    	RiskReportsSearchVO data = v1RiskReportsService.saveRiskReports(compId, riskReportsSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[근로자 참여 변경]", description = "사업장내 유해위험요인 근로자 참여의 정보를 갱신하는 API")
    @PutMapping(value = "/riskReports/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateRiskReports(
    		@PathVariable("id") String id, @RequestBody RiskReportsSaveVO riskReportsSaveVO) throws Exception {
    	v1RiskReportsService.updateRiskReports(id, riskReportsSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", new RiskReportsSearchVO());

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
}
