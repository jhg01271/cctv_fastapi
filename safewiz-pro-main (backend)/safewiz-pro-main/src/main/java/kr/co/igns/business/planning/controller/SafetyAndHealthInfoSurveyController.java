package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.planning.model.WorkersOpinionsOnHazardsVO;
import kr.co.igns.business.planning.service.RiskAssessmentService;
import kr.co.igns.business.planning.service.SafetyAndHealthInfoSurveyService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : RiskAssessment
 * ※ 기능명 : 위험성 평가
 * ※ 작성자 : 박성학
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RiskAssessment", description = "위험성 평가")
@RestController
@RequiredArgsConstructor
public class SafetyAndHealthInfoSurveyController {
    private final MessageService messageService;
    private final SafetyAndHealthInfoSurveyService safetyAndHealthInfoSurveyService;

    @Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 조회]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getSafetyAndHealthInfoSurvey")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthInfoSurvey(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
    	System.out.println("getSafetyAndHealthInfoSurvey :::::::::::::::::::::::::::::::::::::::::::");
        List<SafetyAndHealthInfoSurveyVO> list = safetyAndHealthInfoSurveyService.getSafetyAndHealthInfoSurvey(searchVo);

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
    
    @Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 상세 조회]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 상세 를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getSafetyAndHealthInfoSurveyDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyAndHealthInfoSurveyDetail(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
    	System.out.println("getSafetyAndHealthInfoSurveyDetail :::::::::::::::::::::::::::::::::::::::::::");
        List<SafetyAndHealthInfoSurveyVO> list = safetyAndHealthInfoSurveyService.getSafetyAndHealthInfoSurveyDetail(searchVo);

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
    
    @Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 기준정보 반영 조회]", description = "위험성평가 사전 준비자료 > 공정 조회 시 해당 공정의 설비/msds 정보를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getProcessDataDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getProcessDataDetail(
            @ModelAttribute @Valid SafetyAndHealthInfoSurveyVO searchVo) throws Exception {
    	System.out.println("getProcessDataDetail :::::::::::::::::::::::::::::::::::::::::::");
        List<SafetyAndHealthInfoSurveyVO> list = safetyAndHealthInfoSurveyService.getProcessDataDetail(searchVo);

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
    
	@Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 마스터 추가]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 데이터를 추가한다.")
	@PostMapping(value = "/safewizpro/planning/setSafetyAndHealthInfoSurvey")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createSafetyAndHealthInfoSurvey(@RequestBody @Valid SafetyAndHealthInfoSurveyVO saveVo) throws Exception {
		SafetyAndHealthInfoSurveyVO rtnDto = safetyAndHealthInfoSurveyService.createSafetyAndHealthInfoSurvey(saveVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 상세 추가]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 상세데이터를 추가한다.")
	@PostMapping(value = "/safewizpro/planning/setSafetyAndHealthInfoSurveyDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createSafetyAndHealthInfoSurveyDetail(@RequestBody @Valid List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
		List<SafetyAndHealthInfoSurveyVO> rtnDto = safetyAndHealthInfoSurveyService.createSafetyAndHealthInfoSurveyDetail(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 확정 차수 변경]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 확정 차수를 변경한다.")
	@PostMapping(value = "/safewizpro/planning/confirmSafetyAndHealthInfoSurvey")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmSafetyAndHealthInfoSurvey(@RequestBody @Valid SafetyAndHealthInfoSurveyVO vo) throws Exception {
		SafetyAndHealthInfoSurveyVO rtnDto = safetyAndHealthInfoSurveyService.confirmSafetyAndHealthInfoSurvey(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사삭제]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 master 데이터를 삭제한다.")
	@PostMapping(value = "/safewizpro/planning/delSafetyAndHealthInfoSurvey")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyAndHealthInfoSurvey(@RequestBody @Valid List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
		List<SafetyAndHealthInfoSurveyVO> rtnDto = safetyAndHealthInfoSurveyService.deleteSafetyAndHealthInfoSurvey(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>(); 
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[위험성평가 사전 준비자료 > 안전보건정보 조사 상세 삭제]", description = "위험성평가 사전 준비자료 > 안전보건정보 조사 상세데이터를 삭제한다.")
	@PostMapping(value = "/safewizpro/planning/delSafetyAndHealthInfoSurveyDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyAndHealthInfoSurveyDetail(@RequestBody @Valid List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
		List<SafetyAndHealthInfoSurveyVO> rtnDto = safetyAndHealthInfoSurveyService.deleteSafetyAndHealthInfoSurveyDetail(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>(); 
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[안전보건정보조사 리포트 다수]", description = "출력물 다운로드 다수")
	@PostMapping(value = "/safewizpro/planning/getSafetyAndHealthInfoSurveyReports")
	public void getReports(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
		 
		safetyAndHealthInfoSurveyService.getReports(request, response, vo);
	}
}
