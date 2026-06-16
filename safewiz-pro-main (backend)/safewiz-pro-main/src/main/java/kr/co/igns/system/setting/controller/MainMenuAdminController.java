package kr.co.igns.system.setting.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.MainMenuVO;
import kr.co.igns.system.setting.service.MainMenuService;
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

@Tag(name = "MainMenu Admin")
@RestController
@RequiredArgsConstructor
public class MainMenuAdminController {
	private final MessageService messageService;
	private final MainMenuService mainMenuService;

	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/system/setting/menuAdmin/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid MainMenuVO reqVo)
			throws Exception {
		BaseVO rtnDto = mainMenuService.create(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/system/setting/menuAdmin/{menuId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid MainMenuVO reqVO,
			@PathVariable String menuId) throws Exception {
		reqVO.setMenuId(menuId);
		BaseVO rtnDto = mainMenuService.modify(reqVO);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 하위 정보를 삭제 한다.")
	@DeleteMapping(value = "/system/setting/menuAdmin/{menuId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String menuId)
			throws Exception {

		BaseVO rtnDto = mainMenuService.remove(menuId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
				messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/menuAdmin/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		int count = mainMenuService.searchCount(searchVo);
		List<MainMenuVO> list = mainMenuService.searchAdmin(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[트리 조회]", description = "목록을 이용한 트리를 조회한다")
	@GetMapping(value = "/system/setting/menuAdmin/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeMenu(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		int count = mainMenuService.searchCount(searchVo);
		ArrayNode list = mainMenuService.treeMenuAdmin(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("totalCount", count);
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/system/setting/menuAdmin/{menuId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String menuId)
			throws Exception {

		BaseVO rtnDto = mainMenuService.findOne(menuId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

}
