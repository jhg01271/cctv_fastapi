package kr.co.igns.mobile.hseInquries.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

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
import kr.co.igns.mobile.hseInquries.model.HseInquriesActionVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSaveVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesSearchVO;
import kr.co.igns.mobile.hseInquries.model.HseInquriesVO;
import kr.co.igns.mobile.hseInquries.service.V1HseInquriesService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.hseInquries.controller
 * ※ 파일명 : V1HseInquriesController
 * ※ 기능명 : 안전보건 상담
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 13.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "HseInquries", description = "안전보건 상담")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1HseInquriesController {
    private final MessageService messageService;
    private final V1HseInquriesService v1HseInquriesService;
    
    @Operation(summary = "[안전보건 상담 목록 조회]", description = "사업장내 등록된 안전보건 상담 목록을 조회하는 API")
    @GetMapping(value = "/hseInquries")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInquriesList(
            @ModelAttribute @Valid HseInquriesSearchVO hseInspectionsSearchVO) throws Exception {
    	List<HseInquriesVO> list = v1HseInquriesService.getHseInquriesList(hseInspectionsSearchVO);

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
    
    @Operation(summary = "[안전보건 상담 상세 조회]", description = "사업장내 등록된 안전보건 상담 상세 정보를 조회하는 API")
    @GetMapping(value = "/hseInquries/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseInquriesDetail(
    		@PathVariable("id") String id, @ModelAttribute @Valid HseInquriesSearchVO hseInquriesSearchVO) throws Exception {
    	HseInquriesVO data = v1HseInquriesService.getHseInquriesDetail(id, hseInquriesSearchVO);

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
    
    @Operation(summary = "[안전보건 상담 등록]", description = "특정 사업장내에 안전보건 상담을 등록하는 API")
    @PostMapping(value = "/hseInquries")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHseInquries(
    		@RequestBody HseInquriesSaveVO hseInquriesSaveVO) throws Exception {
        HseInquriesSearchVO.Id data = v1HseInquriesService.saveHseInquries(hseInquriesSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전보건 상담 변경]", description = "사업장내 자신의 안전보건 상담의 정보를 변경하는 API")
    @PutMapping(value = "/hseInquries/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHseInquries(
    		@PathVariable("id") @Valid String id, @RequestBody HseInquriesSaveVO hseInquriesSaveVO) throws Exception {
        HseInquriesSearchVO.Id data = v1HseInquriesService.updateHseInquries(id, hseInquriesSaveVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전보건 상담 삭제]", description = "사업장내 등록된 자신의 안전보건 상담 상세 정보를 삭제하는 API")
    @DeleteMapping(value = "/hseInquries/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHseInquries(@PathVariable("id") @Valid String id) throws Exception {
        v1HseInquriesService.deleteHseInquries(id);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", id);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전보건 상담 조치 등록/변경]", description = "사업장내 자신의 안전보건 상담의 조치 정보를 변경하는 API")
    @PutMapping(value = "/hseInquries/{id}/actions")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHseInquriesAction(
    		@PathVariable("id") @Valid String id, @RequestBody HseInquriesActionVO hseInquriesActionVO) throws Exception {
    	v1HseInquriesService.updateHseInquriesAction(id, hseInquriesActionVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", id);

		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
				   messageService.getMessage("updateSuccess.msg"), result);
    }
}
