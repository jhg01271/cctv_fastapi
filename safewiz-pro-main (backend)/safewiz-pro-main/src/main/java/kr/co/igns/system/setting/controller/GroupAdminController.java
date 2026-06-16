package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.GroupMemberDeleteDto;
import kr.co.igns.system.setting.model.GroupMemberVO;
import kr.co.igns.system.setting.model.GroupVO;
import kr.co.igns.system.setting.service.GroupService;
import kr.co.igns.system.base.model.HrVO;
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

@Tag(name = "Group Admin")
@RestController
@RequiredArgsConstructor
public class GroupAdminController {
	private final MessageService messageService;
	private final GroupService groupService;

	@Operation(summary = "[등록]", description = "입력 정보를 이용해 등록 한다.")
	@PostMapping(value = "/system/setting/group/create")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> create(@RequestBody @Valid GroupVO reqVo) throws Exception {

		BaseVO rtnDto = groupService.create(reqVo);
//
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
	@PutMapping(value = "/system/setting/group/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid GroupVO reqVo, @PathVariable String grpCd) throws Exception {
		reqVo.setGrpId(grpCd);
		BaseVO rtnDto = groupService.modify(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/group/search")
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

    @Operation(summary = "[그룹별 사용자 조회]", description = "목록을 조회한다")
	@GetMapping(value = "/system/setting/group/searchMember")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchMember(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

		searchVo.initialize();
		List<HrVO> list = groupService.searchMember(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[등록]", description = "그룹별 사용자 정보를 등록 한다.")
	@PostMapping(value = "/system/setting/group/createMember")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createMember(@RequestBody @Valid List<GroupMemberVO> reqVo) throws Exception {

		int rtnDto = groupService.createMember(reqVo);
//
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	

	@Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/system/setting/group/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String grpCd,@RequestParam String compId) throws Exception {
		GroupVO reqVo = new GroupVO();
		reqVo.setGrpId(grpCd);
		reqVo.setCompId(compId);
		BaseVO rtnDto = groupService.remove(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	@Operation(summary = "[그룹 멤버 삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
	@DeleteMapping(value = "/system/setting/groupMember")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> removeMember(@ModelAttribute @Valid GroupMemberDeleteDto searchVo) throws Exception {

		int rtnDto = groupService.removeMember(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
	@GetMapping(value = "/system/setting/group/{grpCd}")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String grpCd,@RequestParam String compId) throws Exception {
		GroupVO reqVo = new GroupVO();
		reqVo.setGrpId(grpCd);		
		reqVo.setCompId(compId);		
		BaseVO rtnDto = groupService.findOne(reqVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();		
		result.put("result", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	// 데이터셋 API
	@Operation(summary = "[그룹목록 데이터셋 조회]", description = "그룹목록 데이터셋 정보를 조회 한다.")
	@GetMapping(value = "/system/setting/group/dataset/getDatasetGroupList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetGroupList(SpSearchVO vo) throws Exception {
		List<GroupVO> rtnDto = groupService.getDatasetGroupList(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", rtnDto);

		if (rtnDto == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[데이터 셋 저장]", description = "불러온 데이터셋을 저장한다.")
	@PostMapping(value = "/system/setting/group/dataset/saveGroupDataset")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveGroupDataset(@RequestBody @Valid List<GroupVO> reqVo) throws Exception {

		groupService.saveGroupDataset(reqVo);
//
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[유저의 그룹 존재 여부 조회]", description = "hr_id를 이용해 유저의 그룹 존재 여부를 확인한다.")
	@PostMapping(value = "/system/setting/group/findUserGroup")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findUserGroup(@RequestBody List<String> userHrList) throws Exception {
		List<GroupVO> list = groupService.findUserGroup(userHrList);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);

		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
}
