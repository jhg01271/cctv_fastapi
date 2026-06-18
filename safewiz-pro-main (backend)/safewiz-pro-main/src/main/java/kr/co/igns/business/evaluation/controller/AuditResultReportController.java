package kr.co.igns.business.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.service.AuditResultReportService;
import kr.co.igns.business.impl.model.MgmtOfChangeVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "getAuditResult", description = "안전보건 내부심사 결과보고서")
@RestController
@RequiredArgsConstructor
public class AuditResultReportController {
    private final MessageService messageService;
    private final AuditResultReportService service;

    @Operation(summary = "[내부심사 결과 조회]", description = "내부심사 결과 데이터 를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getAuditResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAuditResult(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<AuditExecutionPlanVO> list = service.getAuditResult(spSearchVO);

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

    @Operation(summary = "[내부심사 결과 삭제]", description = "내부심사 결과 데이터 를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/deleteAuditResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAuditResult(
        @RequestBody List<AuditExecutionPlanDetailVO> spSearchVO) throws Exception {
        List<AuditExecutionPlanDetailVO> list = service.deleteAuditResult(spSearchVO);

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

    @Operation(summary = "[내부심사 결과 상세 조회]", description = "내부심사 결과 데이터 를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getAuditResultDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAuditResultDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        AuditExecutionPlanDetailVO list = service.getAuditResultDetail(spSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[내부심사 결과 저장]", description = "내부심사 결과 데이터 를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/saveAuditResult")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMgmtOfChange(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") AuditExecutionPlanDetailVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());
		BaseVO rtnDto = service.saveAuditResult(files, vo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    
	}
    
    @Operation(summary = "[내부심사 결과 심사 팀원 저장]", description = "내부심사 결과 심사 팀원 데이터 를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/saveAuditHr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveAuditHr(@RequestBody @Valid AuditExecutionPlanDetailVO vo) throws Throwable {
    	service.saveAuditHr(vo);
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("result", vo);
    	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[수행 계획서 출력]", description = "수행 계획서 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/evaluation/getAuditResultReport")
    public void getAuditResultReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            service.getAuditResultReport(request, response, spSearchVO);
    }
}
