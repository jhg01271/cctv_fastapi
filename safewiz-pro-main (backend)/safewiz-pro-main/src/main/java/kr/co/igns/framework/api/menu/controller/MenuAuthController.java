//package kr.co.igns.framework.api.menu.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.constraints.NotNull;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.framework.api.menu.dto.MenuAuthRequestDto;
//import kr.co.igns.framework.api.menu.dto.MenuTreeResponstDto;
//import kr.co.igns.framework.api.menu.service.MenuAuthService;
//import kr.co.igns.framework.config.message.MessageService;
//import kr.co.igns.framework.config.response.ListResponseDto;
//import kr.co.igns.framework.config.response.ResponseUtil;
//import kr.co.igns.framework.config.response.SingleResponseDto;
//import lombok.RequiredArgsConstructor;
//
//@Tag(name = "MenuAuthManage")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/igns/admin/v1")
//public class MenuAuthController {
//
////	private final MenuAuthService menuAuthservice;
//	private final MessageService messageService;
//
//	@Operation(summary = "[권한별 메뉴 조회]", description = "권한별 메뉴관리화면에서 권한별 메뉴를 조회한다.")
//	@PostMapping(value = "/listMenuOfgroupManage")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> listMenuOfgroupManage(@RequestBody MenuAuthRequestDto dto) {
//		Map<String, MenuTreeResponstDto> resultMap = menuAuthservice.listMenuOfgroupManage(dto);
//		List<MenuTreeResponstDto> menuList = resultMap.values().stream().collect(Collectors.toCollection(ArrayList::new));
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("list", menuList);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//	}
//
//	@Operation(summary = "[권한별 메뉴 업데이트]", description = "권한별 메뉴를 업데이트 한다.")
//	@PostMapping(value = "/saveMenuOfgroupManage")
//	private ResponseEntity<SingleResponseDto<String>> saveMenuOfgroupManage(HttpServletRequest request, @NotNull @RequestBody MenuAuthRequestDto dto) throws Exception {
//		menuAuthservice.saveMenuOfgroupManage(request.getAttribute("userId").toString(), dto);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), null);
//	}
//}
