package kr.co.igns.business.comp.company.controller;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.company.model.CompanyVO;
import kr.co.igns.business.comp.company.service.CompanyService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
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
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.controller
 * ※ 파일명 : CompanyAdminController.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "company(회사)", description = "표준 회사 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompanyAdminController {

	private final MessageService messageService;
	private final CompanyService companyService;
	
	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/admin/comp/company/site/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid CompanyVO reqVo) throws Exception {

		BaseVO rtnDto = companyService.create(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/admin/comp/company/site/{compId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid CompanyVO reqVo, @PathVariable String compId) throws Exception {

		reqVo.setCompId(compId);
		BaseVO rtnDto = companyService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/admin/comp/company/site/{compId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String compId) throws Exception {

		BaseVO rtnDto = companyService.remove(compId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/admin/comp/company/site/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		int totalCount = companyService.searchCount(searchVo);
		List<CompanyVO> list = companyService.search(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("totalCount", totalCount);
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
	@Operation(summary = "[트리 조회]", description = "목록으로 트리를 조회한다")
	@GetMapping(value = "/admin/comp/company/site/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeCompany(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		ArrayNode list = companyService.treeCompany(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/admin/comp/company/site/{compId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String compId) throws Exception {

		BaseVO rtnDto = companyService.findOne(compId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[초대 키 조회]", description = "초대 키를 생성 조회한다")
	@PostMapping(value = "/admin/comp/company/site/{compId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateInvatekey(@PathVariable String compId) throws Exception {
		String inviteKey = companyService.updateInvatekey(compId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("key", inviteKey);
		if (inviteKey == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}


}
