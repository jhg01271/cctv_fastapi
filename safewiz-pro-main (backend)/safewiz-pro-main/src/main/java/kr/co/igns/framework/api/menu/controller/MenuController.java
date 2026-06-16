//package kr.co.igns.framework.api.menu.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.framework.api.menu.dto.MenuTreeResponstDto;
//import kr.co.igns.framework.api.menu.service.MenuService;
//import kr.co.igns.framework.config.exception.ExceptionAdvice;
//import kr.co.igns.framework.config.response.CommonResult;
//import kr.co.igns.framework.config.response.ResponseService;
//import lombok.RequiredArgsConstructor;
//
///**
// *
// * @ 패키지 : kr.co.igns.framework.comm.controller @ 파일명 : MenuController.java @
// * 기능명 : 메뉴 관리 Controller @ 작성자 : 김성준 @ 최초생성일 : 2022. 4. 5. @ 프로그램 설명 : 메뉴 관리
// * Controller
// *
// * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
// */
//@Tag(name = "F006. Menu")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/igns/common")
//public class MenuController {
//
//	private final ResponseService responseService;
//	private final ExceptionAdvice exceptionAdvice;
//	private final MenuService menuService;
//
//	@Operation(summary = "사용자 메뉴 조회(권한)", description = "사용자 권한 메뉴 조회")
//	@GetMapping("/menuByUser")
//	public CommonResult getMenuByUser(HttpServletRequest request) {
//		String userId = "abc@i-gns.co.kr";
//		if (request.getAttribute("userId") != null)
//			userId = request.getAttribute("userId").toString();
//		Map<String, MenuTreeResponstDto> res = menuService.getMenuByAuth(userId, "W");
//		List<Object> menuList = res.values().stream().collect(Collectors.toCollection(ArrayList::new));
//		if (menuList.size() < 1) {
//			return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")),
//					exceptionAdvice.getMessage("noResultFound.msg"));
//		}
//		return responseService.getListResult(menuList);
//	}
//
//}
