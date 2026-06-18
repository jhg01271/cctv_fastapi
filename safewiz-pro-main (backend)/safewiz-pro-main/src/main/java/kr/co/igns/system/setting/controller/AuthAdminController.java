package kr.co.igns.system.setting.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.AuthVO;
import kr.co.igns.system.setting.service.AuthService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Tag(name = "Auth Admin")
@RestController
@RequiredArgsConstructor
public class AuthAdminController {
	private final MessageService messageService;
	private final AuthService authService;

	@Operation(summary = "[저장]", description = "입력 정보를 이용해 등록/수정/삭제 한다.")
	@PostMapping(value = "/system/setting/auth/save")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> save(@RequestBody @Valid List<AuthVO> reqVoList) throws Exception {
		List<BaseVO> rtnDtoList = new ArrayList<>();
		authService.removeByGrpCdAndParam(reqVoList.get(0));
		for(AuthVO reqVo : reqVoList) {
			BaseVO rtnDto = authService.create(reqVo);
			rtnDtoList.add(rtnDto);
		}

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDtoList);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[저장]", description = "입력 정보를 이용해 등록/수정/삭제 한다.")
	@PostMapping(value = "/system/setting/auth/saveGroup")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveGroup(@RequestBody @Valid List<AuthVO> reqVoList) throws Exception {
		List<BaseVO> rtnDtoList = new ArrayList<>();
		if(reqVoList.size()>0) {
			authService.saveGroup(reqVoList);
		}

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDtoList);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[저장]", description = "입력 정보를 이용해 등록/수정/삭제 한다.")
	@PostMapping(value = "/system/setting/auth/saveMember")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMember(@RequestBody @Valid List<AuthVO> reqVoList) throws Exception {
		List<BaseVO> rtnDtoList = new ArrayList<>();
		System.out.println(reqVoList);
		authService.saveMember(reqVoList);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDtoList);
	
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 하위 정보를 삭제 한다.")
	@DeleteMapping(value = "/system/setting/auth/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String grpCd) throws Exception {

		int count = authService.removeByGrpCd(grpCd);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/auth/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		int totalCount = authService.searchCount(searchVo);
		List<AuthVO> list = authService.search(searchVo);

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
	@GetMapping(value = "/system/setting/auth/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeAuth(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		int totalCount = authService.searchCountMenu(searchVo);
		ArrayNode list = authService.treeAuth(searchVo);

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
	@GetMapping(value = "/system/setting/auth/groupTree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> groupTree(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		int totalCount = authService.searchCountMenu(searchVo);
		ArrayNode list = authService.groupTreeAuth(searchVo);

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
	@GetMapping(value = "/system/setting/auth/userTree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> userTree(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		ArrayNode list = authService.userTreeAuth(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}


	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/system/setting/auth/{menuSeq}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable Long menuSeq) throws Exception {

		BaseVO rtnDto = authService.findOne(menuSeq);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

}
