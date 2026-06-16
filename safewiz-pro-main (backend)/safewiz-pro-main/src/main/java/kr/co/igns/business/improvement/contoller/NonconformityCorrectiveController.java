package kr.co.igns.business.improvement.contoller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.improvement.model.*;
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
 * ※ 패키지 : kr.co.igns.business.improvement.controller
 * ※ 파일명 : NonconformityCorrective
 * ※ 기능명 : 부적합 및 시정조치
 * ※ 작성자 : 박성학
 * ※ 최초생성일 : 2024. 11. 01.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "부적합 및 시정조치", description = "부적합 및 시정조치")
@RestController
@RequiredArgsConstructor
public class NonconformityCorrectiveController {
	private final MessageService messageService;
	private final NonconformityCorrectiveService nonconformityCorrectiveService;

	@Operation(summary = "[부적합 및 시정조치 목록]", description = "부적합 및 시정조치를 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getNonParticipation")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getNonParticipation(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		HashMap<String, Object> result = new HashMap<String, Object>();
		List<ContinualImprovementVO> list = nonconformityCorrectiveService.getParticipation(searchVo);
        result.put("list", list);
		if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
	}
	
	@Operation(summary = "[아차사고 보고서 조회]", description = "아차사고 보고서를 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getNearMissReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getNearMissReport(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		List<NearMissReportVO> list = nonconformityCorrectiveService.getNearMissReport(searchVo);

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

	@Operation(summary = "[아차사고 보고서 상세 조회]", description = "아차사고 보고서 상세정보를 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getNearMissReportDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getNearMissReportDetail(
    		@RequestParam String docNo) throws Exception {
        NearMissReportVO vo = nonconformityCorrectiveService.getNearMissReportDetail(docNo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", vo);

        if (vo == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }	


	@Operation(summary = "[아차사고 보고서 (선택된 조직에 대한 HEAD 조회)]", description = "아차사고 보고서 상세정보를 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getOrgnHead")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrgnHead(
    		@RequestParam String actionOrgnId) throws Exception {
        NearMissReportVO vo = nonconformityCorrectiveService.getOrgnHead(actionOrgnId);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", vo);

        if (vo == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }	
	
	
	@Operation(summary = "[아차사고 보고서 등록]", description = "아차사고 보고서를 등록한다.")
	@PostMapping(value = "/safewizpro/improvement/insertNearMissReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertNearMissReport(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
			 																		   	   @RequestPart(value = "info") NearMissReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.insertNearMissReport(files,vo);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result",rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[아차사고 보고서 수정]", description = "report_id 이용해 한건의 정보를 수정 한다.") 
	@PutMapping(value = "/safewizpro/improvement/updateNearMissReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateNearMissReport(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
		   	   @RequestPart(value = "info") NearMissReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.updateNearMissReport(files, vo);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
			   messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[아차사고 보고서 삭제]", description = "doc_no를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/improvement/deleteNearMissReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteNearMissReport(NearMissReportVO vo) throws Exception {
        BaseVO rtnDto = nonconformityCorrectiveService.deleteNearMissReport(vo);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
			   messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[아차사고 보고서 일괄 삭제]", description = "doc_no list에 해당되는 조직을 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/improvement/deleteNearMissReports")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteNearMissReports(
			@RequestBody List<NearMissReportVO> list) throws Exception {
		nonconformityCorrectiveService.deleteNearMissReports(list);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[아차사고 보고서 출력]", description = "doc_no list에 해당되는 데이터의 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/printNearMissReports")
	public void printNearMissReports(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody  SpSearchVO spSearchVO) throws Exception {
		nonconformityCorrectiveService.printNearMissReports(request,response,spSearchVO); 
	}
	
	
	@Operation(summary = "[시정조치 요구서 조회]", description = "시정조치 요구서를 조회한다")
    @GetMapping(value = "/safewizpro/improvement/getCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequest(
    		@RequestParam(value="writeYear", required=false) String writeYear) throws Exception {
		
		CorrectiveActionRequestVO vo = new CorrectiveActionRequestVO();
		if(writeYear != null)  vo.setWriteYear(writeYear);
		
		
        List<CorrectiveActionRequestVO> list = nonconformityCorrectiveService.getCorrectiveActionRequest(vo);

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
    @GetMapping(value = "/safewizpro/improvement/getCorrectiveActionRequestDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequestDetail(
    		@RequestParam("writeYear") String writeYear,
    		@RequestParam("docNo") String docNo
    		) throws Exception {
        
    	SpSearchVO vo = new SpSearchVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
        
        CorrectiveActionRequestVO list = nonconformityCorrectiveService.getCorrectiveActionRequestDetail(vo);
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
    @PostMapping(value = "/safewizpro/improvement/insertCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertCorrectiveActionRequest (@RequestPart(value = "info") CorrectiveActionRequestVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertCorrectiveActionRequest(vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    

    @Operation(summary = "[시정조치 요구서 수정]", description = "시정조치 요구서 한건의 정보를 수정한다.")
    @PutMapping(value = "/safewizpro/improvement/updateCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateCorrectiveActionRequest( @RequestPart(value = "info")  CorrectiveActionRequestVO vo) throws Exception
    {	
        BaseVO rtnDto = nonconformityCorrectiveService.updateCorrectiveActionRequest(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[시정조치 요구서 단일 삭제]", description = "시정조치 요구서 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/improvement/deleteCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCorrectiveActionRequest(CorrectiveActionRequestVO vo) throws Exception {
    	
        BaseVO rtnDto = nonconformityCorrectiveService.deleteCorrectiveActionRequest(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[시정조치 요구서 다중 삭제]", description = "시정조치 요구서 다중 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/improvement/deleteCorrectiveActionRequests")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCorrectiveActionRequests(@RequestBody List<CorrectiveActionRequestVO> list) throws Exception {
    	
		nonconformityCorrectiveService.deleteCorrectiveActionRequests(list);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
    
    @Operation(summary = "[시정조치 요구서 출력]", description = "시정조치 요구서 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/printCorrectiveActionRequests")
	public void printCorrectiveActionRequests(HttpServletRequest request, HttpServletResponse response, @RequestBody SpSearchVO spSearchVO) throws Exception {
		nonconformityCorrectiveService.printCorrectiveActionRequests(request,response, spSearchVO);
	}
    
    
    //재해발생보고서 추가 20241111 마환구
    @Operation(summary = "[재해발생 보고서 조회]", description = "재해발생 보고서를 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentReport")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentReport(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
    	
		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentReport(searchVo);
	
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
    
    //재해발생보고서 사고처리조회 추가 20241112 마환구
    @Operation(summary = "[재해발생 보고서 상세조회]", description = "재해발생 보고서를 상세조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentDetailReport")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentDetailReport(
			@RequestParam("writeYear") String writeYear, 
			@RequestParam("docNo") String docNo) throws Exception {
    	
    	
    	IncidentReportVO vo = new IncidentReportVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
    	
		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentDetailReport(vo);
	
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
    
  //재해발생보고서 사고처리조회 추가 20241112 마환구
    @Operation(summary = "[재해발생 보고서 상세조회]", description = "재해발생 보고서를 상세조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentDetailManage")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentDetailManage(
			@RequestParam("writeYear") String writeYear, 
			@RequestParam("docNo") String docNo) throws Exception {
    	
    	
    	IncidentReportVO vo=new IncidentReportVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
    	
    	//IncidentReportVO list = nonconformityCorrectiveService.getIncidentDetailManage(vo);

		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentDetailManage(vo);
	
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
    
    //재해발생보고서 사고경위 조회 추가 20241112 마환구
    @Operation(summary = "[재해발생 보고서 사고경위조회]", description = "재해발생 보고서를 사고경위조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentDetailExtent")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentDetailExtent(
			@RequestParam("writeYear") String writeYear, 
			@RequestParam("docNo") String docNo) throws Exception {
    	
    	
    	IncidentReportVO vo=new IncidentReportVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
    	
		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentDetailExtent(vo);
	
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
    

    @Operation(summary = "[재해발생 보고서 사고경위조회]", description = "재해발생 보고서를 사고경위조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentDetailOpinion")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentDetailOpinion(
			@RequestParam("writeYear") String writeYear, 
			@RequestParam("docNo") String docNo) throws Exception {
    	
    	
    	IncidentReportVO vo=new IncidentReportVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
    	
		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentDetailOpinion(vo); 
	
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
    
    //재해발생보고서 예방및의견 조회 추가 20241112 마환구
    @Operation(summary = "[재해발생 보고서 진술서조회]", description = "재해발생 보고서를 진술서조회한다")
	@GetMapping(value = "/safewizpro/improvement/getIncidentState")
    
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getIncidentState(
			@RequestParam("writeYear") String writeYear, 
			@RequestParam("docNo") String docNo,
			@RequestParam("statementType") String statementType, @RequestParam("type") String type) throws Exception {
    	
    	
    	IncidentReportVO vo=new IncidentReportVO();
    	vo.setWriteYear(writeYear);
    	vo.setDocNo(docNo);
    	vo.setStatementType(statementType);
    	vo.setType(type);
    	
		List<IncidentReportVO> list = nonconformityCorrectiveService.getIncidentState(vo); 
	
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
    
  //재해발생보고서 예방및의견 조회 추가 20241112 마환구1111
    @Operation(summary = "[재해발생 보고서 진술서조회]", description = "재해발생 보고서를 진술서조회한다")
	@GetMapping(value = "/safewizpro/improvement/getdoc")
	public ResponseEntity<SingleResponseDto<String>> getdoc(@RequestParam("writeYear") String writeYear) throws Exception {
    	
		String list = nonconformityCorrectiveService.getdoc(writeYear);
		
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), list);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), list);
		}
	}
    
    
    //재해발생 보고서 저장추가 20241114
    @Operation(summary = "[재해발생 보고서 등록]", description = "재해발생 보고서를 등록한다.")
    @PostMapping(value = "/safewizpro/improvement/insertIncidentReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertIncidentReport (@RequestPart(value = "info") IncidentReportVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertIncidentReport(vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    //재해발생 보고서 상세내역 저장추가 20241114
    @Operation(summary = "[재해발생 보고서 상세내역 등록]", description = "재해발생 보고서 상세내역을 등록한다.")
    @PostMapping(value = "/safewizpro/improvement/insertIncidentReportDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertIncidentReportDetail (@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
    		@RequestPart(value = "info") IncidentReportVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertIncidentReportDetail(files,vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    //재해발생 보고서 상세내역 저장추가 20241115
    @Operation(summary = "[재해발생 보고서 상세내역 등록]", description = "재해발생 보고서 상세내역을 등록한다.")
    @PostMapping(value = "/safewizpro/improvement/insertIncidentReportManage")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertIncidentReportManage (@RequestPart(value = "info") IncidentReportVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertIncidentReportManage(vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    
    //재해발생 보고서 상세내역 저장추가 20241115
    @Operation(summary = "[재해발생 보고서 대책방안 및 의견 등록]", description = "재해발생 보고서 대책방안 및 의견을 등록한다.")
    @PostMapping(value = "/safewizpro/improvement/insertIncidentReportOpinion")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertIncidentReportOpinion (@RequestPart(value = "info") IncidentReportVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertIncidentReportOpinion(vo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    //재해발생 진술서 저장추가 20241115
    @Operation(summary = "[재해발생 보고서 진술서 등록]", description = "재해발생 보고서 진술서 등록한다.")
    @PostMapping(value = "/safewizpro/improvement/insertIncidentReportStatment")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertIncidentReportStatment (@RequestPart(value = "info") IncidentReportVO vo) throws Exception{
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertIncidentReportStatment(vo);
        
        String docSeq = rtnDto.getDocSeq();
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("docSeq", docSeq);
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[재해발생 보고서 사용여부 수정]", description = "재해발생 보고서 수정") 
	@PutMapping(value = "/safewizpro/improvement/updateIncidentReportuseYn")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateIncidentReportuseYn(
		   	   @RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.updateIncidentReportuseYn(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[재해발생 보고서 수정]", description = "재해발생 보고서 수정") 
	@PutMapping(value = "/safewizpro/improvement/updateIncidentReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateIncidentReport(
		   	   @RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.updateIncidentReport(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[재해발생 보고서 상세수정]", description = "재해발생 보고서 상세수정") 
	@PutMapping(value = "/safewizpro/improvement/updateIncidentReportDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateIncidentReportDetail(
			@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,   
			@RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.updateIncidentReportDetail(files, vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[재해발생 보고서 사고처리수정]", description = "재해발생 보고서 사고처리수정") 
	@PutMapping(value = "/safewizpro/improvement/UpdateIncidentReportManage")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> UpdateIncidentReportManage(
		   	   @RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.UpdateIncidentReportManage(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[재해발생 보고서 사고처리수정]", description = "재해발생 보고서 사고처리수정") 
	@PutMapping(value = "/safewizpro/improvement/UpdateIncidentReportOpinion")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> UpdateIncidentReportOpinion(
		   	   @RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.UpdateIncidentReportOpinion(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
    @Operation(summary = "[재해발생 보고서 진술서수정]", description = "재해발생 보고서 진술서수정") 
	@PutMapping(value = "/safewizpro/improvement/updateIncidentReportStatment")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateIncidentReportStatment(
		   	   @RequestPart(value = "info") IncidentReportVO vo)
		throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.updateIncidentReportStatment(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
    
	@Operation(summary = "[재해발생 보고서 삭제]", description = "재해발생 보고서")
	@DeleteMapping(value = "/safewizpro/improvement/deleteIncidentReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteIncidentReport(IncidentReportVO vo) throws Exception {
		
        nonconformityCorrectiveService.deleteIncidentReport(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	} 
	
	@Operation(summary = "[재해발생 보고서 다중 삭제]", description = "재해발생 보고서")
	@DeleteMapping(value = "/safewizpro/improvement/deleteIncidentReports")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteIncidentReport(
			@RequestBody List<IncidentReportVO> vo) throws Exception {
		
        nonconformityCorrectiveService.deleteIncidentReport(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	} 
	
	@Operation(summary = "[재해발생 보고서 출력]", description = "doc_no list에 해당되는 데이터의 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/IncidentReportPrint")
	public void IncidentReportPrint(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody SpSearchVO spSearchV) throws Exception {
		nonconformityCorrectiveService.IncidentReportPrint(request,response,spSearchV); 
	}
	
	@Operation(summary = "[재해발생 보고서 출력]", description = "상세화면에서 체크가 된 것들을 출력한다.")
	@PostMapping(value = "/safewizpro/improvement/IncidentReportPrintchecked")
	public void IncidentReportPrintchecked(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<Object> param) throws Exception {
		
		nonconformityCorrectiveService.IncidentReportPrintchecked(request,response,param); 
	}
	
	@Operation(summary = "[재해발생 사고자 진술서출력]", description = "doc_no list에 해당되는 데이터의 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/IncidentReportStatementPrint")
	public void IncidentReportStatementPrint(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody SpSearchVO spSearchVO) throws Exception {
			
			
		nonconformityCorrectiveService.IncidentReportStatementPrint(request,response,spSearchVO);
	}
	
	@Operation(summary = "[재해발생 목격자 진술서출력]", description = "doc_no list에 해당되는 데이터의 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/WitnessReportStatementPrint")
	public void WitnessReportStatementPrint(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody SpSearchVO spSearchVO) throws Exception {
		nonconformityCorrectiveService.WitnessReportStatementPrint(request,response,spSearchVO);
	}
	
	@Operation(summary = "[재해발생 보고서 통합출력]", description = "doc_no list에 해당되는 데이터의 리포트를 출력한다")
	@PostMapping(value = "/safewizpro/improvement/IncidentReportCombine")
	public void IncidentReportCombine(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody SpSearchVO spSearchVO) throws Exception {
		nonconformityCorrectiveService.IncidentReportCombine(request,response,spSearchVO);
	}
	
	@Operation(summary = "[재해발생 보고서 사고경위 삭제]", description = "재해발생 보고서")
	@DeleteMapping(value = "/safewizpro/improvement/deleteIncidentDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteIncidentDetail(
			@RequestBody List<IncidentReportVO> vo) throws Exception {
		
        nonconformityCorrectiveService.deleteIncidentDetail(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[재해발생 보고서 사고처리 삭제]", description = "재해발생 보고서")
	@DeleteMapping(value = "/safewizpro/improvement/deleteIncidentHospi")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteIncidentHospi(
			@RequestBody List<IncidentReportVO> vo) throws Exception {
		
        nonconformityCorrectiveService.deleteIncidentHospi(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[재해발생 보고서 대책방안 및 관리자의견 삭제]", description = "재해발생 보고서")
	@DeleteMapping(value = "/safewizpro/improvement/deleteIncidentOpinion")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteIncidentOpinion(
			@RequestBody List<IncidentReportVO> vo) throws Exception {
		
        nonconformityCorrectiveService.deleteIncidentOpinion(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
	
    //재해발생보고서 진술서 삭제 마환구
    @Operation(summary = "[재해발생 진술서삭제]", description = "재해발생 진술서 삭제")
	@DeleteMapping(value = "/safewizpro/improvement/DeteleStatement")
    
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteStatement(
            @RequestBody IncidentReportVO vo) throws Exception {

        System.out.println("writeYear: " + vo.getWriteYear());
        System.out.println("docNo: " + vo.getDocNo());
        System.out.println("statementType: " + vo.getStatementType());

        nonconformityCorrectiveService.DeteleStatement(vo);
	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
		}
}
