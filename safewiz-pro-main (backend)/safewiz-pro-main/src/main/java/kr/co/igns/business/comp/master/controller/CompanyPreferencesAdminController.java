package kr.co.igns.business.comp.master.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.master.model.CompanyPreferencesVO;
import kr.co.igns.business.comp.master.service.CompanyPreferencesService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.factory.controller
 * ※ 파일명 : CompanyAdminController.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "company(회사)", description = "표준 회사 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompanyPreferencesAdminController {

	private final MessageService messageService;
	private final CompanyPreferencesService companyPreferencesService;

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/admin/fems/factory/site/{compId}/preferences")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String compId) throws Exception {

		BaseVO rtnDto = companyPreferencesService.findOne(compId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/admin/fems/factory/site/{compId}/preferences")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid CompanyPreferencesVO reqVo, @PathVariable String compId) throws Exception {

		reqVo.setCompId(compId);
		BaseVO rtnDto = companyPreferencesService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/admin/fems/factory/site/{compId}/preferences")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String compId) throws Exception {

		BaseVO rtnDto = companyPreferencesService.remove(compId);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/admin/fems/factory/site/{compId}/preferences")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid CompanyPreferencesVO reqVo, @PathVariable String compId) throws Exception {

		BaseVO rtnDto = companyPreferencesService.create(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

//	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
//	@GetMapping(value = "/admin/fems/factory/site/search")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid FemsSearchVO searchVo) throws Exception {
//
//		int count = companyPreferencesService.searchCount(searchVo);
//		List<CompanyPreferencesVO> list = companyPreferencesService.search(searchVo);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put( "count", count);
//		result.put("list", list);
//		if (list == null || list.size() < 1) {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
//		} else {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//		}
//	}
	
}
