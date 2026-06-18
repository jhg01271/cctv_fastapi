package kr.co.igns.business.utils.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.controller
 * ※ 파일명 : CompanyController.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 15.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.utils.controller
 * ※ 파일명 : UtilsController.java
 * ※ 기능명 :
 * ※ 작성자 : 이경수
 * ※ 최초생성일 : 2024. 10. 23.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 *
 * 
 * </pre>
 */
@Tag(name = "BusinessUtils", description = "유틸리티")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UtilsController {

	private final MessageService messageService;
	private final UtilsService utilsService;

	@Operation(summary = "[서명 정보 리스트 조회]", description = "서명 정보를 조회한다")
	@GetMapping(value = "/safewizpro/utils/searchCompList")
	public ResponseEntity<SingleResponseDto<LinkedHashMap<String, Object>>> searchCompList(@ModelAttribute @Valid UtilsVO searchVo) throws Exception {
		List<UtilsVO> list = utilsService.searchCompList(searchVo.getClntId());
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

    
	@Operation(summary = "[서명 정보 리스트 조회]", description = "서명 정보를 조회한다")
	@GetMapping(value = "/safewizpro/utils/searchApprovalInfo")
	public ResponseEntity<SingleResponseDto<LinkedHashMap<String, Object>>> searchApprovalInfo(@ModelAttribute @Valid UtilsVO searchVo) throws Exception {
		
		List<UtilsVO> list = utilsService.getApprovalInfo(searchVo);

		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	@Operation(summary = "[서명 정보 리스트 조회]", description = "서명 정보를 조회한다")
	@GetMapping(value = "/safewizpro/utils/searchApprovalInfoSimple")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchApprovalInfoSimple(@ModelAttribute @Valid UtilsVO searchVo) throws Exception {

		List<UtilsVO> list = utilsService.getApprovalInfoSimple(searchVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
	@Operation(summary = "[서명자 단건 저장]", description = "신규 서명자 정보를 단건 저장한다.")
	@PostMapping(value = "/safewizpro/utils/insertApprovalInfo")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {

		BaseVO rtnDto = utilsService.insertApprovalInfo(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	

	@Operation(summary = "[신규 서명자 다중 저장]", description = "신규 서명자 정보를 다중 저장한다.")
	@PostMapping(value = "/safewizpro/utils/insertApprovalInfoList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertSignatureList(@RequestBody @Valid List<UtilsVO> saveVo) throws Exception {
		List<UtilsVO> rtnDto = utilsService.insertApprovalInfoList(saveVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

    @Operation(summary = "[TASK useYn 처리]", description = "마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜준다.")
    @PostMapping(value = "/safewizpro/utils/updateTaskUseYn")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateTaskUseYn(@RequestBody @Valid UtilsVO saveVo) throws Exception {
        BaseVO rtnDto = utilsService.updateTaskUseYn(saveVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }


    //	@Operation(summary = "[신규 서명자 다중 저장]", description = "신규 서명자 정보를 다중 저장한다.")
//	@PostMapping(value = "/safewizpro/utils/insertApprovalInfoListMulti")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertSignatureListMulti(@RequestBody @Valid List<UtilsVO> saveVo) throws Exception {
//		List<UtilsVO> rtnDto = utilsService.insertApprovalInfoListMulti(saveVo);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", rtnDto);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
//	}
//	@Operation(summary = "[서명자 서명 등록]", description = "서명자 서명을 등록한다.")
//	@PostMapping(value = "/safewizpro/utils/updateApprovalInfo")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {
//		//saveVO.setStatusId(codeSeq);
//		BaseVO rtnDto = utilsService.updateApprovalInfo(saveVo);
//		// Task 안들어 있음
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", rtnDto);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
//	}
	@Operation(summary = "[서명자 서명 등록]", description = "서명자 서명을 등록한다.")
	@PostMapping(value = "/safewizpro/utils/updateApprovalInfoSign")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateSignatureSign(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		//saveVO.setStatusId(codeSeq);
		saveVo.setHrUserId(saveVo.getUserId());
		BaseVO rtnDto = utilsService.updateApprovalInfoSign(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[서명자 서명 등록 취소]", description = "서명자 서명 등록을 취소한다.")
	@PostMapping(value = "/safewizpro/utils/updateApprovalInfoSignCancel")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateSignatureSignCancel(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		//saveVO.setStatusId(codeSeq);
		BaseVO rtnDto = utilsService.updateApprovalInfoSignCancel(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[서명자 정보 삭제]", description = "해당 서명자 정보를 삭제한다.")
	@PostMapping(value = "/safewizpro/utils/deleteApprovalInfo")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		BaseVO rtnDto = utilsService.deleteApprovalInfo(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}

	@Operation(summary = "[서명자 정보 null로 업데이트]", description = "해당 서명자 인원 정보를 제거")
	@PostMapping(value = "/safewizpro/utils/nullUpdateApprovalInfo")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> nullUpdateApprovalInfo(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		BaseVO rtnDto = utilsService.nullUpdateApprovalInfo(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
	}
    

	@Operation(summary = "[사용자 서명 등록]", description = "사용자 서명을 등록한다.")
	@PostMapping(value = "/safewizpro/utils/updateUserSignature")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateUserSignature(@RequestBody @Valid UtilsVO saveVo) throws Exception {
		//saveVO.setStatusId(codeSeq);
		BaseVO rtnDto = utilsService.updateUserSignature(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
}
