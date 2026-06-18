package kr.co.igns.system.user.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.service.UserService;
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
 * ※ 패키지 : kr.co.igns.base.member.controller
 * ※ 파일명 : UserAdminController.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 07. 12.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "user(사용자)", description = "표준 사용자 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user")
public class UserAdminController {

	private final MessageService messageService;
	private final UserService userService;
	
	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/site/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid UserVO reqVo) throws Exception {

		BaseVO rtnDto = userService.create(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/site/{userId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid UserVO reqVo, @PathVariable String userId) throws Exception {

		reqVo.setUserId(userId);
		BaseVO rtnDto = userService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/site/{userId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String userId) throws Exception {

		BaseVO rtnDto = userService.remove(userId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/site/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		
		// searchVo 초기화 추가 
		searchVo.initialize();
		// count -> totalCount 변수 변경 
		int totalCount = userService.searchCount(searchVo);
		List<UserVO> list = userService.search(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		
		// count -> totalCount 변수 변경
		result.put("totalCount", totalCount);
		result.put("list", list);
		// searchParam 추가
		//result.put("searchParam", searchVo);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/site/{userId}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String userId) throws Exception {

		BaseVO rtnDto = userService.findOne(userId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
}
