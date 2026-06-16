package kr.co.igns.business.improvement.contoller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.improvement.model.ProvisionAndMgmtPPEVO;
import kr.co.igns.business.improvement.service.ProvisionAndMgmtPPEService;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
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
 * ※ 파일명 : ContinualImprovementController
 * ※ 기능명 : 지속적 개선
 * ※ 작성자 : 이경수
 * ※ 최초생성일 : 2024. 11. 07.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "지속적 개선", description = "지속적 개선")
@RestController
@RequiredArgsConstructor
public class ProvisionAndMgmtPPEController {
	private final MessageService messageService;
	private final ProvisionAndMgmtPPEService provisionAndMgmtPPEService;

	
	@Operation(summary = "[안전보호구 지급 및 관리 마스터 목록 조회]", description = "안전보호구 지급 및 관리 마스터 목록 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getProvisionAndMgmtPPEList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getProvisionAndMgmtPPEList(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		List<ProvisionAndMgmtPPEVO> list = provisionAndMgmtPPEService.getProvisionAndMgmtPPEList(searchVo);

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

	@Operation(summary = "[안전보호구 지급 및 관리 상세 목록 조회]", description = "안전보호구 지급 및 관리 상세 목록 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getProvisionAndMgmtPPEDetailList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getProvisionAndMgmtPPEDetailList(
			@ModelAttribute @Valid ProvisionAndMgmtPPEVO searchVo) throws Exception {
		List<ProvisionAndMgmtPPEVO> list = provisionAndMgmtPPEService.getProvisionAndMgmtPPEDetailList(searchVo);

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
	
	@Operation(summary = "[안전보호구 지급 및 관리 상세  저장]", description = "안전보호구 지급 및 관리 상세 등록한다.")
	@PostMapping(value = "/safewizpro/improvement/insertProvisionAndMgmtPPE")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo) throws Exception {
		BaseVO rtnDto = provisionAndMgmtPPEService.insertProvisionAndMgmtPPE(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
	

	@Operation(summary = "[안전보호구 지급 및 관리 상세 삭제]", description = "안전보호구 지급 및 관리 상세 삭제한다.")
	@PostMapping(value = "/safewizpro/improvement/deleteProvisionAndMgmtPPE")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo) throws Exception {
		BaseVO rtnDto = provisionAndMgmtPPEService.deleteProvisionAndMgmtPPE(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[안전보호구 품목 조회]", description = "안전보호구 품목을 조회한다")
	@GetMapping(value = "/safewizpro/improvement/getDatasetPPEList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetPPEList(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		List<ProvisionAndMgmtPPEVO> list = provisionAndMgmtPPEService.getDatasetPPEList(searchVo);

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

	@Operation(summary = "[부서 상황 출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/improvement/getReportProvisionAndMgmtPPE")
	public void getReportProvisionAndMgmtPPE(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody List<ProvisionAndMgmtPPEVO> voList) throws Exception {
	
		provisionAndMgmtPPEService.getReportProvisionAndMgmtPPE(request, response, voList);
	}
	
	@Operation(summary = "[안전보호구 품목 출력]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/improvement/getReportPPEManagement")
	public void getReportPPEManagement(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
	 
		provisionAndMgmtPPEService.getReportPPEManagement(request, response, vo);
	}
	
	//2024.12.20
	@Operation(summary = "[선택된 안전보호구 품목 출력]", description = "선택된 안전보호구 출력물 다운로드")
	@PostMapping(value = "/safewizpro/improvement/getReportPPEManagementchk")
	public void getReportPPEManagementchk(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody List<ProvisionAndMgmtPPEVO> checkedValues) throws Exception {
	 
		System.out.println("checkedValues :"+checkedValues);
		provisionAndMgmtPPEService.getReportPPEManagementchk(request, response, checkedValues);
	}
	
	
	@Operation(summary = "[안전보호구 품목 저장]", description = "안전보호구 품목을 등록한다.")
	@PostMapping(value = "/safewizpro/improvement/saveDatasetPPE")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveDatasetPPE(@RequestBody @Valid List<ProvisionAndMgmtPPEVO> voList) throws Exception {
		BaseVO rtnDto = provisionAndMgmtPPEService.saveDatasetPPE(voList);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
	

	@Operation(summary = "[안전보호구 품목 삭제]", description = "안전보호구 품목을 등록한다.")
	@PostMapping(value = "/safewizpro/improvement/deleteDatasetPPE")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteDatasetPPE(@RequestBody @Valid List<ProvisionAndMgmtPPEVO> voList) throws Exception {
		BaseVO rtnDto = provisionAndMgmtPPEService.deleteDatasetPPE(voList);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
	/*
	@Operation(summary = "[아차사고 보고서 수정]", description = "report_id 이용해 한건의 정보를 수정 한다.") 
	@PutMapping(value = "/safewizpro/improvement/updateNearMissReport/{reportId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateNearMissReport(
			@RequestPart(value = "info") ProvisionAndMgmtPPEVO reqVO, @PathVariable String reportId) throws Exception {
		
		BaseVO rtnDto = provisionAndMgmtPPEService.updateDatasetPPE(reqVO);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}
	*/
	/*
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
	@GetMapping(value = "/safewizpro/improvement/getMsdsDetail/{msdsId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsdsDetail(@PathVariable String msdsId)
			throws Exception {
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setSearchText(msdsId);
		NearMissReportVO list = nonconformityCorrectiveService.getNearMissReportDetail(searchVo);

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

	@Operation(summary = "[아차사고 보고서 등록]", description = "아차사고 보고서를 등록한다.")
	@PostMapping(value = "/safewizpro/improvement/insertNearMissReport")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertNearMissReport(NearMissReportVO vo) throws Exception {
		BaseVO rtnDto = nonconformityCorrectiveService.insertNearMissReport(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[아차사고 보고서 수정]", description = "report_id 이용해 한건의 정보를 수정 한다.") 
	@PutMapping(value = "/safewizpro/improvement/updateNearMissReport/{reportId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateNearMissReport(
			@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
			@RequestPart(value = "info") NearMissReportVO reqVO, @PathVariable String reportId) throws Exception {
		reqVO.setReportId(reportId);
		BaseVO rtnDto = nonconformityCorrectiveService.updateNearMissReport(files, reqVO);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[아차사고 보고서 삭제]", description = "report_id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/improvement/deleteNearMissReport/{msdsId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteNearMissReport(
			@PathVariable String reportId) throws Exception {
		NearMissReportVO param = new NearMissReportVO();
		param.setReportId(reportId);
		BaseVO rtnDto = nonconformityCorrectiveService.deleteNearMissReport(param);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[MSDS 일괄 삭제]", description = "process_id list에 해당되는 조직을 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/improvement/deleteNearMissReports")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteNearMissReports(
			@RequestBody List<NearMissReportVO> list) throws Exception {
		nonconformityCorrectiveService.deleteNearMissReports(list);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[시정조치 요구서 조회]", description = "시정조치 요구서를 조회한다")
    @GetMapping(value = "/safewizpro/improvement/getCorrectiveActionRequest")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequest(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<CorrectiveActionRequestVO> list = nonconformityCorrectiveService.getCorrectiveActionRequest(searchVo);

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
    @GetMapping(value = "/safewizpro/improvement/getCorrectiveActionRequest/{msdsId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCorrectiveActionRequestDetail(
            @PathVariable String msdsId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(msdsId);
        
        CorrectiveActionRequestVO list = nonconformityCorrectiveService.getCorrectiveActionRequestDetail(searchVo);

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
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertCorrectiveActionRequest (
    		CorrectiveActionRequestVO vo) throws Exception
    {
    	
        BaseVO rtnDto = nonconformityCorrectiveService.insertCorrectiveActionRequest( vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    

    @Operation(summary = "[시정조치 요구서 수정]", description = "requestId 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/safewizpro/improvement/updateCorrectiveActionRequest/{requestId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateCorrectiveActionRequest(
            @NotNull @RequestPart(   value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info")  CorrectiveActionRequestVO vo,
            @PathVariable String requestId) throws Exception
    {
        vo.setRequestId(requestId);
        BaseVO rtnDto = nonconformityCorrectiveService.updateCorrectiveActionRequest(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[시정조치 요구서 삭제]", description = "requestId를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/improvement/deleteCorrectiveActionRequest/{msdsId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCorrectiveActionRequest(@PathVariable String requestId)
            throws Exception {
    	CorrectiveActionRequestVO param = new CorrectiveActionRequestVO();
        param.setRequestId(requestId);
        BaseVO rtnDto = nonconformityCorrectiveService.deleteCorrectiveActionRequest(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    */
}
