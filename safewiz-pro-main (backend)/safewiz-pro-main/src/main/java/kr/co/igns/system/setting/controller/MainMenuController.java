package kr.co.igns.system.setting.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.MainMenuVO;
import kr.co.igns.system.setting.service.MainMenuService;
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

@Tag(name = "MainMenu")
@RestController
@RequiredArgsConstructor
public class MainMenuController {
	private final MessageService messageService;
	private final MainMenuService mainMenuService;

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/menu/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		List<MainMenuVO> list = mainMenuService.search(searchVo);

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

	@Operation(summary = "[트리 조회]", description = "목록을 이용한 트리를 조회한다")
	@GetMapping(value = "/system/setting/menu/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeMenu(
			@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		ArrayNode list = mainMenuService.treeMenu(searchVo);

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

}
