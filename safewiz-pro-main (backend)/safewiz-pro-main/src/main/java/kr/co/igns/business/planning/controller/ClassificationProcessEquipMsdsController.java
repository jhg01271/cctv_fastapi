package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ClassificationProcessEquipMsdsVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.planning.service.ClassificationProcessEquipMsdsService;
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
 * ※ 파일명 : ClassificationProcessEquipMsds
 * ※ 기능명 : 위험성평가 조직도 관리
 * ※ 작성자 : 윤다영
 * ※ 최초생성일 : 2024. 10. 29.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ClassificationProcessEquipMsds")
@RestController
@RequiredArgsConstructor
public class ClassificationProcessEquipMsdsController {
    private final MessageService messageService;
    private final ClassificationProcessEquipMsdsService classificationProcessEquipMsdsService;
    
    @Operation(summary = "[조직별 공정 리스트 조회]", description = "조직별 공정 리스트 조회한다")
    @GetMapping(value = "/safewizpro/planning/getClassProcList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClassProcList(
            @ModelAttribute @Valid ClassificationProcessEquipMsdsVO searchVo,
    		@RequestParam List<String> processIdList) throws Exception {
        List<SafetyAndHealthInfoSurveyVO> list = classificationProcessEquipMsdsService.getClassProcList(searchVo);

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
    
    @Operation(summary = "[공정 별 아이템(equip, msds) 조회]", description = "공정 별 아이템(equip, msds) 조회한다")
    @GetMapping(value = "/safewizpro/planning/getPrcsItemList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcsItemList(
    		@RequestParam String writeYear,
    		@RequestParam List<String> revNoList,
    		@RequestParam List<String> processIdList) throws Exception {
        List<SafetyAndHealthInfoSurveyVO> list = classificationProcessEquipMsdsService.getPrcsItemList(writeYear, revNoList, processIdList);

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
    
    @Operation(summary = "[공정/설비/물질 구분 목록 조회]", description = "공정/설비/물질 구분 목록을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getClassProcEquipMsdsList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClassProcEquipMsdsList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ClassificationProcessEquipMsdsVO> list = classificationProcessEquipMsdsService.getClassProcEquipMsdsList(searchVo);

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
    
    @Operation(summary = "[공정/설비/물질 구분 상세 조회]", description = "공정/설비/물질 구분 상세 정보를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getClassProcEquipMsdsDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClassProcEquipMsdsDetail(
    		@ModelAttribute @Valid ClassificationProcessEquipMsdsVO searchVo) throws Exception {
        
    	ClassificationProcessEquipMsdsVO list = classificationProcessEquipMsdsService.getClassProcEquipMsdsDetail(searchVo);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[공정/설비/물질 구분 등록 및 수정]", description = "공정/설비/물질 구분을 등록한다")
    @PostMapping(value = "/safewizpro/planning/saveClassProcEquipMsds")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveClassProcEquipMsds(
            @RequestBody @Valid ClassificationProcessEquipMsdsVO reqVo)
            throws Exception {
    	
        BaseVO rtnDto = classificationProcessEquipMsdsService.saveClassProcEquipMsds(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[공정/설비/물질 구분 확정 차수 변경]", description = "공정/설비/물질 구분 확정 차수를 변경한다.")
    @PostMapping(value = "/safewizpro/planning/confirmClassProc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmClassProc(@RequestBody @Valid ClassificationProcessEquipMsdsVO vo) throws Exception {
        BaseVO rtnDto = classificationProcessEquipMsdsService.confirmClassProc(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[공정/설비/물질 구분 삭제]", description = "공정/설비/물질 구분을 삭제한다")
    @DeleteMapping(value = "/safewizpro/planning/deleteClassProcEquipMsds")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteOrganizationChart(
            @RequestBody List<ClassificationProcessEquipMsdsVO> list)
            throws Exception {
    	classificationProcessEquipMsdsService.deleteClassProcEquipMsds(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[공정/설비/믈질 출력]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getClassProcReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response
			, @RequestBody SpSearchVO spSearchVO) throws Exception {
		
    	classificationProcessEquipMsdsService.getReport(request, response, spSearchVO);
	}
    
}
