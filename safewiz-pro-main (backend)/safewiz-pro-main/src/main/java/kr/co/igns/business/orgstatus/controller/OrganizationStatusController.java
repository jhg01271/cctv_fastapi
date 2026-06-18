package kr.co.igns.business.orgstatus.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.company.service.CompanyMyService;
import kr.co.igns.business.comp.company.service.CompanyService;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.orgstatus.service.OrganizationStatusService;
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
 * ※ 패키지 : kr.co.igns.fems.company.controller
 * ※ 파일명 : CompanyController.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 15.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.orgstatus.controller
 * ※ 파일명 : OrganizationStatus.java
 * ※ 기능명 :
 * ※ 작성자 : 김성현
 * ※ 최초생성일 : 2024. 9. 04.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 *
 * 
 * </pre>
 */
@Tag(name = "OrganizationStatus(부서 상황)", description = "부서 상황 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class OrganizationStatusController {

	private final MessageService messageService;
	private final CompanyService companyService;
    private final CompanyMyService compService;    
    
    private final OrganizationStatusService organizationStatusService; 


    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/safewizpro/orgstatus/search_01")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> AAA() throws Exception {
    	
    	//ResponseEntity<HashMap<String, Object>> rtn =
    	
    	//ResponseEntity<SingleResponseDto<HashMap<String, Object>>> response = 
    	//	    new ResponseEntity<>()
    		    
    	  
    	HashMap<String, Object> list = new HashMap<>();
    	list.put("AA", "BB");
    	
		System.out.println("AAAA");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, "code", "message", list);
    }
		
    
	@Operation(summary = "[부서 상황 메인]", description = "년도별 부서 상황을 조회한다")
	@GetMapping(value = "/safewizpro/orgstatus/getOrgnStatus")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid OrganizationStatusVO searchVo) throws Exception {
		List<OrganizationStatusVO> list = organizationStatusService.search(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
	@Operation(summary = "[부서 상황 상세]", description = "년도별 부서의 상세 내용을 조회한다")
	@GetMapping(value = "/safewizpro/orgstatus/getOrgnStatusDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search_Detail(@ModelAttribute @Valid OrganizationStatusVO searchVo) throws Exception {
		
		List<OrganizationStatusVO> list = organizationStatusService.searchDetail(searchVo);
		List<UtilsVO> signList = organizationStatusService.getSignature(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		result.put("signList", signList);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
	@Operation(summary = "[부서 상황 상세 신규 서명 저장]", description = "부서 상황 상세 신규 저장 시 서명정보를 한꺼번에 저장한다.")
	@PostMapping(value = "/safewizpro/orgstatus/insertNewSignature")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertNewSignature(@RequestBody @Valid List<UtilsVO> saveVo) throws Exception {
		System.out.println("aaaaaaaa");
		List<UtilsVO> rtnDto = organizationStatusService.insertSignature(saveVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	
	@Operation(summary = "[서명 정보 저장]", description = "서명 정보를 저장한다.")
	@PostMapping(value = "/safewizpro/orgstatus/setSignature")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> setSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		//saveVO.setStatusId(codeSeq);
		BaseVO rtnDto = organizationStatusService.setSignature(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[부서 상황 추가]", description = "부서 상황 신규 데이터를 추가한다")
	@PostMapping(value = "/safewizpro/orgstatus/setOrgnStatus")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid OrganizationStatusVO saveVo) throws Exception {
		System.out.println("EEEEEEEE" + saveVo);
		//saveVO.setStatusId(codeSeq);
		BaseVO rtnDto = organizationStatusService.insert(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[부서 상황 상세 추가 및 변경]", description = "부서 상황 신규 데이터를 추가 및 변경한다")
	@PostMapping(value = "/safewizpro/orgstatus/setOrgnStatusDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createDetail(@RequestBody @Valid List<OrganizationStatusVO> saveVo) throws Exception {
		List<OrganizationStatusVO> rtnDto = organizationStatusService.setDetail(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[부서 상황 변경]", description = "부서 상황 데이터를 변경한다")
	@PostMapping(value = "/safewizpro/orgstatus/updateOrgnStatus")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> update(@RequestBody @Valid OrganizationStatusVO saveVo) throws Exception {
		BaseVO rtnDto = organizationStatusService.update(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	
	@Operation(summary = "[부서 상황 삭제]", description = "선택된 데이터를 삭제한다")
	@PostMapping(value = "/safewizpro/orgstatus/delOrgnStatus")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> delete(@RequestBody @Valid List<OrganizationStatusVO> saveVo) throws Exception {
		List<OrganizationStatusVO> rtnDto = organizationStatusService.delete(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[부서 상황 상세 삭제]", description = "선택된 데이터를 삭제한다")
	@PostMapping(value = "/safewizpro/orgstatus/delOrgnStatusDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteDetail(@RequestBody @Valid List<OrganizationStatusVO> saveVo) throws Exception {
		List<OrganizationStatusVO> rtnDto = organizationStatusService.deleteDetail(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[서명 삭제]", description = "해당 서명을 삭제한다.")
	@PostMapping(value = "/safewizpro/orgstatus/delOrgnStatusSignature")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		UtilsVO rtnDto = organizationStatusService.deleteSignature(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[부서 상황 삭제(특정년도, 특정조직 전체)]", description = "선택된 데이터를 삭제한다")
	@PostMapping(value = "/safewizpro/orgstatus/delOrgnStatusAll")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAll(@RequestBody @Valid OrganizationStatusVO saveVo) throws Exception {
		
		//saveVO.setStatusId(codeSeq);
		BaseVO rtnDto = organizationStatusService.deleteOrg(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[부서 상황 출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/orgstatus/getOrgnStatusReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
		
		 organizationStatusService.getReport(request, response, vo);
		}

	
//	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
//	@GetMapping(value = "/comp/company/site/search")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid FemsSearchVO searchVo) throws Exception {
//
//		int totalCount = companyService.searchCount(searchVo);
//		List<OrganizationStatusVO> list = companyService.search(searchVo);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("totalCount", totalCount);
//		result.put("list", list);
//		if (list == null || list.isEmpty()) {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
//		} else {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//		}
//	}
//	
//	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
//	@GetMapping(value = "/comp/company/site/{compId}")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String compId) throws Exception {
//
//		BaseVO rtnDto = companyService.findOne(compId);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", rtnDto);
//
//		if (rtnDto == null) {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
//		} else {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//		}
//	}
	

    

}
