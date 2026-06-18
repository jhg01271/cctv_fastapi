package kr.co.igns.business.comp.master.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.master.model.CompCodeVO;
import kr.co.igns.business.comp.master.service.CompCodeService;
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

@Tag(name = "System Code Admin")
@RestController
@RequiredArgsConstructor
public class CompCodeAdminController {
	private final MessageService messageService;
	private final CompCodeService compCodeService;

	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/admin/comp/master/compCode/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid CompCodeVO reqVo) throws Exception {

		BaseVO rtnDto = compCodeService.create(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/admin/comp/master/compCode/{codeSeq}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid CompCodeVO reqVo, @PathVariable Long codeSeq) throws Exception {
		reqVo.setCodeSeq(codeSeq);
		BaseVO rtnDto = compCodeService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/admin/comp/master/compCode/{codeSeq}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable Long codeSeq) throws Exception {

		BaseVO rtnDto = compCodeService.remove(codeSeq);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/admin/comp/master/compCode/search")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		int totalCount = compCodeService.searchCount(searchVo);
		List<CompCodeVO> list = compCodeService.search(searchVo);

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
	@GetMapping(value = "/admin/comp/master/compCode/tree")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeCompCode(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		ArrayNode list = compCodeService.treeCompCode(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/admin/comp/master/compCode/{codeSeq}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable Long codeSeq) throws Exception {

		BaseVO rtnDto = compCodeService.findOne(codeSeq);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

}
