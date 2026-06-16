package kr.co.igns.system.common.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.user.service.UserService;
import kr.co.igns.system.setting.service.MainMenuService;
import kr.co.igns.business.comp.company.service.CompanyMyService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Tag(name = " 메뉴 목록 조회")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final MessageService messageService;
	private final MainMenuService mainMenuService;
    private final UserService userService;
    private final CompanyMyService companyMyService;// FIXME comp 프로젝트 내 함수...고민중... 
    
    
	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/base/layout/init")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		//사용자 이름 및 권한 조회 > 프론트에서 store에 담는다.
		String userId = SecurityUtil.getCurrentMemberId();
		BaseVO rtnDto = userService.findOneSimpleUserInfo(userId);
		
		// 1. 관리자이면 관리자 첫화면으로
		
		
		// 2. 사용자 프로젝트 갯수 조회 멀티 사용자인지, 한개 사용자인지 확인
		int totalCount = companyMyService.searchCount(searchVo);
		
		
		

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		result.put("totalCount", totalCount);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	

    @Operation(summary = "[트리 조회]", description = "목록을 이용한 트리를 조회한다")
	@GetMapping(value = "/base/menu/{menuCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeMenu(@ModelAttribute @Valid SpSearchVO searchVo, @PathVariable String menuCd) throws Exception {
		//user 정보를 가져온다.
		String userId = SecurityUtil.getCurrentMemberId();
		BaseVO res = userService.findOne(userId);
				
		
		//menuCd 로 오는 것은 업무적으로  Mypage, Complex, Company, Admin 4가지다.
		// role 은 USER, ADMIN 두가지
		
		
		
		//관리자 메뉴 요청인지 확인 한다.
		
		
		// 사업장 메뉴 요청 처리 한다.
		
		
		
		searchVo.setSearchCd(menuCd);
		ArrayNode list = mainMenuService.treeMenu(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

}
