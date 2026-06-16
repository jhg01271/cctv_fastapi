package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.impl.model.EmergencyRoleVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ClassificationOfHazardsVO;
import kr.co.igns.business.planning.model.ResultOfRiskAssessmentTrainingVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.planning.service.ClassificationOfHazardsService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller;
 * ※ 파일명 : ClassificationOfHazardsController
 * ※ 기능명 : 유해위험요인분류
 * ※ 작성자 : 김현재
 * ※ 최초생성일 : 2024. 11. 05.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */



@Tag(name = "ClassificationOfHazards")
@RestController
@RequiredArgsConstructor
public class ClassificationOfHazardsController {
	private final MessageService messageService;
	private final ClassificationOfHazardsService classsificationOfHazardsService;
	
	@Operation(summary = "[유해위험요인분류 조회]", description = "유해위험요인분류를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getHazardsList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazardsList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getHazardsList(searchVo);
		
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

	@Operation(summary = "[위험성 평가 이행 유해위험요인분류 조회]", description = "위험성 평가 이행 유해위험요인분류를 조회한다")
	@GetMapping(value = "/safewizpro/planning/selectHazardsList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> selectHazardsList(@ModelAttribute @Valid ClassificationOfHazardsVO vo) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.selectHazardsList(vo);

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
	
	@Operation(summary = "[유해위험요인분류상세-공정조회]", description = "유해위험요인분류상세 페이지에서 공정을 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getProcessList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getProcssList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getProcssList(searchVo);
		
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
	
	@Operation(summary = "[유해위험요인분류상세-상세조회]", description = "유해위험요인분류상세 페이지 상세 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getHazardsDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazardsDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getHazardsDetail(searchVo);
		
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
	
	@Operation(summary = "[유해위험요인분류상세-공정에 해당하는 설비,위험물질 조회]", description = "유해위험요인분류상세 페이지에서 공정에 해당하는 설비,위험물질을 조회한다..")
    @GetMapping(value = "/safewizpro/planning/getProcessEquipmentAndMsds")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getProcessEquipmentAndMsds(@RequestParam String processId) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getProcessEquipmentAndMsds(processId);
		
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
	
	@Operation(summary = "[유해위험요인분류수정시 분류목록 조회]", description = "유해위험요인분류상세 수정시 공정에 해당하는 위험요인분류목록을 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getSavedFactorList")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSavedFactorList(@ModelAttribute @Valid ClassificationOfHazardsVO vo) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getSavedFactorList(vo);
		
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

	@Operation(summary = "[유해위험요인분류상세저장]", description = "유해위험요인분류상세를 저장한다.")
    @PostMapping(value = "/safewizpro/planning/saveHazards")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHazards(
			@RequestBody @Valid ClassificationOfHazardsVO reqVo) throws Exception{
		  
		BaseVO rtnDto = classsificationOfHazardsService.saveHazards(reqVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
		if(rtnDto == null) {
			result.put("result", "dupe");
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
	                messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
					messageService.getMessage("createSuccess.msg"), result);
		}
	}

	@Operation(summary = "[유해위험요인분류 전년도 불러오기 저장]", description = "유해위험요인분류 전년도 데이터를 저장한다.")
	@PostMapping(value = "/safewizpro/planning/savePrevHazards")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePrevHazards(
			@RequestBody @Valid List<ClassificationOfHazardsVO> reqVo) throws Exception{

		classsificationOfHazardsService.savePrevHazards(reqVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", reqVo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}

	@Operation(summary = "[유해위험요인분류삭제]", description = "유해위험요인분류목록를 삭제한다.")
    @PostMapping(value = "/safewizpro/planning/deleteHazards")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHazards(
			@RequestBody @Valid List<ClassificationOfHazardsVO> reqVo) throws Exception{
		classsificationOfHazardsService.deleteHazards(reqVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
		
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[유해위험요인분류 출력]", description = "유해위험요인분류를 출력한다.")
	@PostMapping(value = "/safewizpro/planning/getClassificationOfHazardsReports")
	public void getClassificationOfHazardsReports(HttpServletRequest request, HttpServletResponse response,
            @RequestBody @Valid SpSearchVO vo) throws Exception {
		 
		classsificationOfHazardsService.getClassificationOfHazardsReports(request,response,vo);
	}

	// 팝업 관련 API
	@Operation(summary = "[유해위험요인분류 조회]", description = "유해위험요인분류 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getClassData")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClassData(@RequestParam String searchText) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getClassData(searchText);

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
	@Operation(summary = "[유해위험요인분류 예시 조회]", description = "유해위험요인분류 예시 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getSampleClassData")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSampleClassData(@RequestParam String searchText) throws Exception{
		List<ClassificationOfHazardsVO> list = classsificationOfHazardsService.getSampleClassData(searchText);

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

	@Operation(summary = "[유해위험요인분류 데이터셋 삭제]", description = "유해위험요인분류 데이터셋을 삭제한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteClassData")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteClassData(
            @RequestBody @Valid  List<ClassificationOfHazardsVO> voList)
            throws Exception {
        BaseVO rtnDto = classsificationOfHazardsService.deleteClassData(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

	@Operation(summary = "[유해위험요인분류 데이터셋 저장]", description = "유해위험요인분류 데이터셋을 저장한다.")
    @PostMapping(value = "/safewizpro/planning/saveClassData")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveClassData(
			@RequestBody @Valid List<ClassificationOfHazardsVO> voList) throws Exception{

		BaseVO rtnDto = classsificationOfHazardsService.saveClassData(voList);

		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
		if(rtnDto == null) {
			result.put("result", "dupe");
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
	                messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
					messageService.getMessage("createSuccess.msg"), result);
		}
	}
}
