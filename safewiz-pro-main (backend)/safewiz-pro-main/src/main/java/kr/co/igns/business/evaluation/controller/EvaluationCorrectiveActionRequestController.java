package kr.co.igns.business.evaluation.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.EvaluationCorrectiveActionRequestVO;
import kr.co.igns.business.evaluation.service.EvaluationCorrectiveActionRequestService;
import kr.co.igns.business.improvement.model.CorrectiveActionRequestVO;
import kr.co.igns.business.improvement.model.IncidentReportVO;
import kr.co.igns.business.improvement.model.NearMissReportVO;
import kr.co.igns.business.improvement.model.NonconformityCorrectiveVO;
import kr.co.igns.business.improvement.service.NonconformityCorrectiveService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.evaluation.controller
 * ※ 파일명 : NonconformityCorrective
 * ※ 기능명 : 시정조치 요구서
 * ※ 작성자 : 김동건
 * ※ 최초생성일 : 2024. 12. 10.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "시정조치 요구서", description = "시정조치 요구서")
@RestController
@RequiredArgsConstructor
public class EvaluationCorrectiveActionRequestController {
	private final MessageService messageService;
	private final EvaluationCorrectiveActionRequestService evaluationCorrectiveActionRequestService;

	@Operation(summary = "[시정조치 요구서 조회]", description = "시정조치 요구서를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/getCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequest(
    		@RequestParam(value="writeYear", required=false) String writeYear) throws Exception {
		
		EvaluationCorrectiveActionRequestVO vo = new EvaluationCorrectiveActionRequestVO();
		if(writeYear != null)  vo.setWriteYear(writeYear);
		
		
        List<EvaluationCorrectiveActionRequestVO> list = evaluationCorrectiveActionRequestService.getCorrectiveActionRequest(vo);

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
	
	
	@Operation(summary = "[시정조치 요구서 상세 조회]", description = "시정조치 요구서 상세정보를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/getCorrectiveActionRequestDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequestDetail(
    		@RequestParam("writeYear") String writeYear,
    		@RequestParam("docNo") String docNo
    		) throws Exception {
        
		SpSearchVO vo = new SpSearchVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
        
    	EvaluationCorrectiveActionRequestVO list = evaluationCorrectiveActionRequestService.getCorrectiveActionRequestDetail(vo);
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
	
	
	@Operation(summary = "[시정조치 요구서 등록]", description = "시정조치 요구서를 등록한다.")
    @PostMapping(value = "/safewizpro/evaluation/insertCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertCorrectiveActionRequest (@RequestPart(value = "info") EvaluationCorrectiveActionRequestVO vo) throws Exception{
    	
        BaseVO rtnDto = evaluationCorrectiveActionRequestService.insertCorrectiveActionRequest(vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    

    @Operation(summary = "[시정조치 요구서 수정]", description = "시정조치 요구서 한건의 정보를 수정한다.")
    @PutMapping(value = "/safewizpro/evaluation/updateCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateCorrectiveActionRequest( @RequestPart(value = "info")  EvaluationCorrectiveActionRequestVO vo) throws Exception
    {	
        BaseVO rtnDto = evaluationCorrectiveActionRequestService.updateCorrectiveActionRequest(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[시정조치 요구서 단일 삭제]", description = "시정조치 요구서 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/deleteCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCorrectiveActionRequest(EvaluationCorrectiveActionRequestVO vo) throws Exception {
    	
        BaseVO rtnDto = evaluationCorrectiveActionRequestService.deleteCorrectiveActionRequest(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[시정조치 요구서 다중 삭제]", description = "시정조치 요구서 다중 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/evaluation/deleteCorrectiveActionRequests")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCorrectiveActionRequests(@RequestBody List<EvaluationCorrectiveActionRequestVO> list) throws Exception {
    	
    	evaluationCorrectiveActionRequestService.deleteCorrectiveActionRequests(list);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
    
    @Operation(summary = "[시정조치 요구서 출력]", description = "시정조치 요구서 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/evaluation/printCorrectiveActionRequests")
	public void printCorrectiveActionRequests(HttpServletRequest request, HttpServletResponse response, @RequestBody SpSearchVO spSearchVO) throws Exception {
    	evaluationCorrectiveActionRequestService.printCorrectiveActionRequests(request,response, spSearchVO);
	}
	
}
