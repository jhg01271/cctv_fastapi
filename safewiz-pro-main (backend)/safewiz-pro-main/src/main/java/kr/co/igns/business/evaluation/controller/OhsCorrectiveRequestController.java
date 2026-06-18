package kr.co.igns.business.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanDetailVO;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.OhsCorrectiveRequestVO;
import kr.co.igns.business.evaluation.service.OhsCorrectiveRequestService;
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

@Tag(name = "getCorrectiveRequest", description = "안전보건 시정조치 요구서")
@RestController
@RequiredArgsConstructor
public class OhsCorrectiveRequestController {
    private final MessageService messageService;
    private final OhsCorrectiveRequestService service;

    @Operation(summary = "[시정조치 요구서 조회]", description = "시정조치 요구서 데이터 를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getCorrectiveRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveRequest(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<OhsCorrectiveRequestVO> list = service.getCorrectiveRequest(spSearchVO);

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

    @Operation(summary = "[시정조치 요구서 상세 조회]", description = "시정조치 요구서 데이터 를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getCorrectiveRequestDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveRequestDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
            OhsCorrectiveRequestVO list = service.getCorrectiveRequestDetail(spSearchVO);

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

    @Operation(summary = "[시정조치 요구서 저장]", description = "시정조치 요구서 데이터 를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/saveCorrectiveRequest")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCorrectiveRequest(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") OhsCorrectiveRequestVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());
		BaseVO rtnDto = service.saveCorrectiveRequest(files, vo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    
	}

    @Operation(summary = "[시정조치 요구서 저장]", description = "시정조치 요구서 데이터 를 저장한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/deleteCorrectiveRequest")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMgmtOfChange(
        @RequestBody @Valid List<SpSearchVO> voList) throws Throwable {
		BaseVO rtnDto = service.deleteCorrectiveRequest(voList);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    
	}
    
    @Operation(summary = "[시정조치 요구서 출력]", description = "시정조치 요구서+ 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/evaluation/getCorrectiveRequestReport")
    public void getCorrectiveRequestReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            service.getCorrectiveRequestReport(request, response, spSearchVO);
    }
}
