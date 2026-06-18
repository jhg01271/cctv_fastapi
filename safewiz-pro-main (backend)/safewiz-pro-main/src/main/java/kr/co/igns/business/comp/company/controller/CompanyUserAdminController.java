package kr.co.igns.business.comp.company.controller;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.company.model.CompanyUserVO;
import kr.co.igns.business.comp.company.service.CompanyUserService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.comp.company.controller;
 * ※ 파일명 : CompanyUserController.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 7. 18.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
// [2024-07-23] 패키지 변경 - 신택훈
@Tag(name = "company(회사)", description = "사용자 회사 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompanyUserAdminController {

	private final MessageService messageService;
	private final CompanyUserService companyUserService;
	
	@Operation(summary = "[저장]", description = "입력 정보를 이용해 등록/수정/삭제 한다.")
	@PostMapping(value = "/admin/comp/company/save")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> save(@RequestBody @Valid List<CompanyUserVO> reqVoList) throws Exception {
		List<BaseVO> rtnDtoList = new ArrayList<>();
		companyUserService.removeByUserId(reqVoList.get(0).getUserId());
		for(CompanyUserVO reqVo : reqVoList) {
			BaseVO rtnDto = companyUserService.create(reqVo);
			rtnDtoList.add(rtnDto);
		}

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDtoList);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 하위 정보를 삭제 한다.")
	@DeleteMapping(value = "/admin/comp/company/{userId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String userId) throws Exception {

		int count = companyUserService.removeByUserId(userId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/admin/comp/company/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		int totalCount = companyUserService.searchCount(searchVo);
		List<CompanyUserVO> list = companyUserService.search(searchVo);

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
	@GetMapping(value = "/admin/comp/company/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeCompanyUser(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		ArrayNode list = companyUserService.treeCompanyUser(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/admin/comp/company/searchByCompIdAndUserId")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchByCompIdAndUserId(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		BaseVO rtnDto = companyUserService.searchByCompIdAndUserId(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
}
