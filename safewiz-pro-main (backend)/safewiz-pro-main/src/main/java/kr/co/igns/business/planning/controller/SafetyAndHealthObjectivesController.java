package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.planning.service.SafetyAndHealthObjectivesService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.OrganizationVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : SafetyAndHealthObjectives
 * ※ 기능명 : 안전보건목표
 * ※ 작성자 : 이희원
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "SafetyAndHealthObjectives", description = "안전보건목표")
@RestController
@RequiredArgsConstructor
public class SafetyAndHealthObjectivesController {
	private final MessageService messageService;
	private final SafetyAndHealthObjectivesService service;

	@Operation(summary = "[안전보건목표 메인 조회]", description = "안전보건목표 메인 정보를 조회한다")
	@GetMapping(value = "/safewizpro/planning/getSafetyAndHealthObjectives")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthObjectives(
			@ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
		System.out.println("getSafetyAndHealthObjectives :::::::::::::::::::::::::::::::::::::::::::");
		List<SafetyAndHealthObjectivesVO> list = service.getSafetyAndHealthObjectives(vo);

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

	@Operation(summary = "[안전보건목표 상세 메인 조회]", description = "안전보건목표 상세 메인 정보를 조회한다")
	@GetMapping(value = "/safewizpro/planning/getMainSafetyAndHealthObjective")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMainSafetyAndHealthObjective(
			@ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
		List<SafetyAndHealthObjectivesVO> list = service.getMainSafetyAndHealthObjective(vo);

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

	@Operation(summary = "[안전보건목표 상세 조회]", description = "안전보건목표 메인 상세 정보를 조회한다")
	@GetMapping(value = "/safewizpro/planning/getSafetyAndHealthObjectivesDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthObjectivesDetail(
			@ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
		System.out.println("getSafetyAndHealthObjectivesDetail :::::::::::::::::::::::::::::::::::::::::::");
		List<SafetyAndHealthObjectivesVO> list = service.getSafetyAndHealthObjectivesDetail(vo);

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

	@Operation(summary = "[안전보건목표 조직조회]", description = "안전보건목표에 등록되지 않은 조직들을 조회한다.")
	@GetMapping(value = "/safewizpro/planning/getOrganizationInSafetyAndHealthObjectives")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationInSafetyAndHealthObjectives(
			@ModelAttribute @Valid SafetyAndHealthObjectivesVO vo) throws Exception {
		List<OrganizationVO> list = service.getOrganizationInSafetyAndHealthObjectives(vo);

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

	@Operation(summary = "[안전보건목표 저장]", description = "안전보건목표 메인, 상세 내용을 저장한다")
	@PostMapping(value = "/safewizpro/planning/saveSafetyAndHealthObjectives")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyAndHealthObjectives(
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {
		service.saveSafetyAndHealthObjectives(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[안전보건목표 저장]", description = "안전보건목표 메인, 상단만 저장한다.")
	@PostMapping(value = "/safewizpro/planning/saveSafetyAndHealthObjectivesTop")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyAndHealthObjectivesTop(
			@RequestBody @Valid SafetyAndHealthObjectivesVO reqVo) throws Exception {
		service.saveSafetyAndHealthObjectivesTop(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[안전보건목표 메인 삭제]", description = "안전보건목표 메인을 삭제한다")
	@PostMapping(value = "/safewizpro/planning/delSafetyAndHealthObjectives")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> delSafetyAndHealthObjectives(
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {
		service.delSafetyAndHealthObjectives(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[안전보건목표 상세 삭제]", description = "안전보건목표 상세를 삭제한다")
	@PostMapping(value = "/safewizpro/planning/delSafetyAndHealthObjectivesDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> delSafetyAndHealthObjectivesDetail(
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {
		service.delSafetyAndHealthObjectivesDetail(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[안전보건목표 리포트]", description = "안전보건목표 리포트를 제작한다.")
	@PostMapping(value = "/safewizpro/planning/getSafetyHealthObjectivesReport")
	public void getSafetyHealthObjectivesReport(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
		service.getSafetyHealthObjectivesReport(request, response, spSearchVO);
	}

	@Operation(summary = "[안전보건목표 리포트]", description = "안전보건목표 리포트를 제작한다.사용유무가 Y인것만 출력")
	@PostMapping(value = "/safewizpro/planning/getSafetyHealthObjectivesReportUseYn")
	public void getSafetyHealthObjectivesReportUseYn(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
		service.getSafetyHealthObjectivesReportUseYn(request, response, spSearchVO);
	}

	@Operation(summary = "[안전보건목표 리포트]", description = "메인화면에서 안전보건목표 리포트를 제작한다")
	@PostMapping(value = "/safewizpro/planning/getSafetyHealthObjectivesReportMain")
	public void getSafetyHealthObjectivesReportMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
		service.getSafetyHealthObjectivesReportMain(request, response, spSearchVO);
	}
}
