//package kr.co.igns.framework.api.menu.controller;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sun.istack.NotNull;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.framework.api.menu.dto.MenuRequestDto;
//import kr.co.igns.framework.api.menu.dto.MenuResponseDto;
//import kr.co.igns.framework.api.menu.service.MenuManageService;
//import kr.co.igns.framework.config.exception.ExceptionAdvice;
//import kr.co.igns.framework.config.response.CommonResult;
//import kr.co.igns.framework.config.response.ResponseService;
//import lombok.RequiredArgsConstructor;
//
//@Tag(name="F000. MenuManage")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/igns/admin/v1")
//public class MenuManageController {
//
//	private final ResponseService responseService;
//	private final MenuManageService service;
//	private final ExceptionAdvice exceptionAdvice;
//
//	// 1. 메뉴 SELECT
//		@Operation(summary = "[메뉴 조회]", description = "메뉴관리화면에서 모든 메뉴을 조회한다.")
//		@GetMapping(value = "/getMenuManage")
//		public CommonResult getMenuManage() {
//			List<MenuResponseDto> res = service.getMenuManage();
//			if (res.size() < 1) {
//				return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")),
//						exceptionAdvice.getMessage("noResultFound.msg"));
//			}
//			return responseService.getListResult(res);
//		}
//
//		// 2. INSERT & UPDATE
//		@Operation(summary = "[메뉴 등록 및 수정]", description = "메뉴를 저장한다. 프론트단의 __created__ 여부로 insert, update를 구분한다.")
//		@PostMapping(value = "/saveMenuManage")
//		private CommonResult saveMenuManage(HttpServletRequest request, @NotNull @RequestBody List<MenuRequestDto> param) throws Exception {
//			int resultCode = service.saveMenuManage(request.getAttribute("userId").toString(), param, -1);
//			if (resultCode < 1) {
//				// 에러처리는 아래와 같이 처리함. 오류코드 등록은 exception_ko.yml에 등록하고 사용
//				// return
//				// responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")),
//				// exceptionAdvice.getMessage("noResultFound.msg"));
//			}
//			return responseService.getSuccessResult();
//		}
//
//		// 3. DELETE
//		@Operation(summary = "[메뉴 삭제]", description = "메뉴를 삭제한다.")
//		@DeleteMapping(value = "/deleteMenuManage")
//		public CommonResult delete(HttpServletRequest request, @RequestBody List<MenuRequestDto> param) {
//			service.deleteMenuManage(param);
//			return responseService.getSuccessResult();
//		}
//}
//
