package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.GroupVO;
import kr.co.igns.system.setting.service.CompGroupService;
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
import java.util.HashMap;
import java.util.List;

@Tag(name = "Comp Group")
@RestController
@RequiredArgsConstructor
public class CompGroupController {
	private final MessageService messageService;
	private final CompGroupService groupService;

	

	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/system/setting/compGroup/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid GroupVO reqVo) throws Exception {

		BaseVO rtnDto = groupService.create(reqVo);
//
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/system/setting/compGroup/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid GroupVO reqVo, @PathVariable String grpCd) throws Exception {
		reqVo.setGrpId(grpCd);
		BaseVO rtnDto = groupService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/system/setting/compGroup/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String grpCd) throws Exception {

		BaseVO rtnDto = groupService.remove(grpCd);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/compGroup/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		int totalCount = groupService.searchCount(searchVo);
		List<GroupVO> list = groupService.search(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("totalCount", totalCount);
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[목록 조회(리스트)]", description = "권한그룹 리스트를 조회한다(lookup, login 등)")
	@GetMapping(value = "/system/setting/compGroup/searchLists")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchLists() throws Exception {
		List<GroupVO> list = groupService.searchLists();

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/system/setting/compGroup/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String grpCd) throws Exception {

		BaseVO rtnDto = groupService.findOne(grpCd);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
}
