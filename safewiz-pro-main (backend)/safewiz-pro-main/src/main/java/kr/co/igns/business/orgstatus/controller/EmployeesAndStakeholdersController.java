package kr.co.igns.business.orgstatus.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.orgstatus.model.EmployeesAndStakeholdersVO;
import kr.co.igns.business.orgstatus.service.EmployeesAndStakeholdersService;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.orgstatus.controller
 * ※ 파일명 : EmployeesAndStakeholders
 * ※ 기능명 : 
 * ※ 작성자 : 염인식
 * ※ 최초생성일 : 2024. 09. 05.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "employeesAndStakeholders", description = "근로자 및 이해관계자")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EmployeesAndStakeholdersController {
	private final MessageService messageService;
    private final EmployeesAndStakeholdersService employeesAndStakeholdersService;
    
    @Operation(summary = "[근로자 및 이해관계자 년도 조회]", description = "근로자 및 이해관계자 년도를 조회한다")
    @GetMapping(value = "/safewizpro/orgstatus/searchYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchYear() throws Exception {
        List<EmployeesAndStakeholdersVO> list = employeesAndStakeholdersService.searchYear(); 

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[근로자 및 이해관계자 조회]", description = "근로자 및 이해관계자를 조회한다")
    @GetMapping(value = "/safewizpro/orgstatus/searchEmployeesStakeholders")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchEmployeesStakeholders(@ModelAttribute @Valid EmployeesAndStakeholdersVO reqVo) throws Exception {
        List<EmployeesAndStakeholdersVO> list = employeesAndStakeholdersService.searchEmployeesStakeholders(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    
    @Operation(summary = "[삭제]", description = "근로자 및 이해관계자를 삭제 한다.")
	@DeleteMapping(value = "/safewizpro/orgstatus/deleteEmployeesStakeholdersList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEmployeesStakeholdersList(@RequestBody @Valid List<EmployeesAndStakeholdersVO> reqVo) throws Exception {
    	
    	employeesAndStakeholdersService.deleteEmployeesStakeholdersList(reqVo);
    	//
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}
    
    @Operation(summary = "[조회]", description = "근로자 및 이해관계자 상세 내용을 조회한다.")
    @GetMapping(value = "/safewizpro/orgstatus/searchEmployeesStakeholdersDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchEmployeesStakeholdersDetail(@ModelAttribute @Valid EmployeesAndStakeholdersVO reqVo) throws Exception {
    	List<EmployeesAndStakeholdersVO> list = employeesAndStakeholdersService.searchEmployeesStakeholdersDetail(reqVo);
    	
    	//서명정보 조회
    	UtilsVO utilsVO = new UtilsVO();
    	utilsVO.setTargetType(reqVo.getDocType());
    	utilsVO.setTargetId(reqVo.getWriteYear() + reqVo.getDocNo());
//    	List<UtilsVO> list2 = employeesAndStakeholdersService.getApprovalInfo(utilsVO);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("list", list);
//    	result.put("list2", list2);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    
    
    @Operation(summary = "[추가]", description = "근로자 및 이해관계자 상세 내용을 추가 한다.")
    @PostMapping(value = "/safewizpro/orgstatus/saveEmployeesStakeholdersDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> addEmployeesStakeholdersDetail(@RequestBody @Valid List<EmployeesAndStakeholdersVO> reqVo) throws Exception {
    	employeesAndStakeholdersService.addEmployeesStakeholdersDetail(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[삭제]", description = "근로자 및 이해관계자 상세 내용을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/orgstatus/deleteEmployeesStakeholdersDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEmployeesStakeholdersDetail(@RequestBody @Valid List<EmployeesAndStakeholdersVO> reqVo) throws Exception {
    	employeesAndStakeholdersService.deleteEmployeesStakeholdersDetail(reqVo);
    	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
    
	@Operation(summary = "[이해관계자의 요구사항과 기대 분석표 출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/orgstatus/getEmployeesReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
		
		employeesAndStakeholdersService.getReport(request, response, vo);
	}
	
	
	@Operation(summary = "[근로자 및 이해관계자 서명자 저장]", description = "서명자 저장")
	@PostMapping(value = "/safewizpro/orgstatus/saveApprovalInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertApprovalInfo(@RequestBody @Valid List<UtilsVO> reqVo) throws Exception {		String  a = "";
    	employeesAndStakeholdersService.insertApprovalInfo(reqVo);
    	// Todo :  Task 안들어 있음 여기서 서명 타는건 추후 삭제 필요
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
	
	@Operation(summary = "[근로자 및 이해관계자 서명자 삭제]", description = "서명자 삭제")
	@DeleteMapping(value = "/safewizpro/orgstatus/deleteApprovalInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteApprovalInfo(@RequestBody @Valid List<UtilsVO> reqVo) throws Exception {
    	employeesAndStakeholdersService.deleteApprovalInfo(reqVo);
		// Todo :  Task 안들어 있음 여기서 서명 타는건 추후 삭제 필요
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
	
	@Operation(summary = "[근로자 및 이해관계자 서명 입력]", description = "서명 입력")
	@PostMapping(value = "/safewizpro/orgstatus/updateApprovalInfoSign")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateApprovalInfoSign(@RequestBody @Valid UtilsVO reqVo) throws Exception {
    	employeesAndStakeholdersService.updateApprovalInfoSign(reqVo);
		// Todo :  Task 안들어 있음 여기서 서명 타는건 추후 삭제 필요
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
	
	@Operation(summary = "[근로자 및 이해관계자 서명 입력]", description = "서명 취소")
	@PostMapping(value = "/safewizpro/orgstatus/updateApprovalInfoSignCancel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateApprovalInfoSignCancel(@RequestBody @Valid UtilsVO reqVo) throws Exception {
    	employeesAndStakeholdersService.updateApprovalInfoSignCancel(reqVo);
		// Todo :  Task 안들어 있음 여기서 서명 타는건 추후 삭제 필요
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
	
	
    
}
