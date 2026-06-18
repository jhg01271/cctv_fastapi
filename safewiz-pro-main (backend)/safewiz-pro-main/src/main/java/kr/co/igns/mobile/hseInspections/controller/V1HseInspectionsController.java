package kr.co.igns.mobile.hseInspections.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailSaveVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsSearchVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesDetailVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsTemplatesVO;
import kr.co.igns.mobile.hseInspections.model.HseInspectionsVO;
import kr.co.igns.mobile.hseInspections.service.V1HseInspectionsService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.hseInspections.controller
 * ※ 파일명 : V1HseInspectionsController
 * ※ 기능명 : 위원회 일정 
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 10.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "HseInspections", description = "안전점검")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1HseInspectionsController {
    private final MessageService messageService;
    private final V1HseInspectionsService v1HseInspectionsService;
    
    @Operation(summary = "[안전점검 캘린더 count 조회]", description = "안전점검 캘린더 일정 개수를 조회하는 API")
    @GetMapping(value = "/hseInspections/calendar/count")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInspectionsCalendarCount(
            @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
    	Map<String, List<Map<String, Integer>>> data = v1HseInspectionsService.getHseInspectionsCalendarCount(hseInspectionsSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[안전점검 일정 조회]", description = "사업장에서 수행된 지난 안전점검 수행 내역을 조회하는 API")
    @GetMapping(value = "/hseInspections")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInspections(
            @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
        List<HseInspectionsVO> list = v1HseInspectionsService.getHseInspections(hseInspectionsSearchVO);
        
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
    
    @Operation(summary = "[안전점검 점검표 목록 조회]", description = "사업장에서 수행해야 할 안전점검 점검표(템플릿) 목록을 조회하는 API")
    @GetMapping(value = "/hseInspections/templates")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInspectionsTemplates(
            @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
        List<HseInspectionsTemplatesVO> list = v1HseInspectionsService.getHseInspectionsTemplates(hseInspectionsSearchVO);
        
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
    
    @Operation(summary = "[안전점검 점검표 상세 조회]", description = "사업장에서 수행해야 할 안전점검 점검표(템플릿) 상세 정보를 조회하는 API")
    @GetMapping(value = "/hseInspections/templates/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInspectionsTemplatesDetail(
    		@PathVariable("id") String id, @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
    	HseInspectionsTemplatesDetailVO data = v1HseInspectionsService.getHseInspectionsTemplatesDetail(id, hseInspectionsSearchVO);
 
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[안전점검 상세 조회]", description = "사업장의 특정 안전점검 수행의 상세 정보를 조회하는 API")
    @GetMapping(value = "/hseInspections/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInspectionsDetail(
    		@PathVariable("id") String id, @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
    	HseInspectionsDetailVO data = v1HseInspectionsService.getHseInspectionsDetail(id, hseInspectionsSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    @Operation(summary = "[안전점검 등록]", description = "자신이 수행한 안전점검을 등록하는 API")
    @PostMapping(value = "/hseInspections")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHseInspectionsDetail(
    		@Param("compId") String compId, @RequestBody HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO) throws Exception {
    	HseInspectionsSearchVO data = v1HseInspectionsService.saveHseInspectionsDetail(compId, hseInspectionsDetailSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전점검 변경]", description = "자신이 등록한 안전점검의 상세 정보를 변경하는 API")
    @PutMapping(value = "/hseInspections/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHseInspectionsDetail(
    		@PathVariable("id") String id, @RequestBody HseInspectionsDetailSaveVO hseInspectionsDetailSaveVO) throws Exception {
    	HseInspectionsSearchVO data = v1HseInspectionsService.updateHseInspectionsDetail(id, hseInspectionsDetailSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전점검 삭제]", description = "자신이 등록한 안전점검을 삭제하는 API")
    @DeleteMapping(value = "/hseInspections/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHseInspectionsDetail(
    		@PathVariable("id") String id, @ModelAttribute @Valid HseInspectionsSearchVO hseInspectionsSearchVO) throws Exception {
    	v1HseInspectionsService.deleteHseInspectionsDetail(id, hseInspectionsSearchVO);
    	
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", hseInspectionsSearchVO);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
}
