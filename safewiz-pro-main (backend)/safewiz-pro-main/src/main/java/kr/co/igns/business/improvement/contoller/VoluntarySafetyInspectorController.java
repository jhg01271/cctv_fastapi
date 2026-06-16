package kr.co.igns.business.improvement.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.improvement.model.VoluntarySafetyInspectorVO;
import kr.co.igns.business.improvement.service.VoluntarySafetyInspectorService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : VoluntarySafetyInspector
 * ※ 기능명 : 위험기계 및 기구 안전관리 자체 검사원 등록부
 * ※ 작성자 : 장석천
 * ※ 최초생성일 : 2024. 11. 19.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "VoluntarySafetyInspector", description = "위험기계 및 기구 안전관리 자체 검사원 등록부")
@RestController
@RequiredArgsConstructor
public class VoluntarySafetyInspectorController {

    private final VoluntarySafetyInspectorService voluntarySafetyInspectorService;
    private final MessageService messageService;

    @Operation(summary = "자체검사원 등록부 목록 조회")
    @GetMapping(value = "/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getVoluntarySafetyInspectorList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<VoluntarySafetyInspectorVO> resultList = voluntarySafetyInspectorService.getVoluntarySafetyInspectorList(searchVo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null || resultList.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "자체검사원 등록부 목록 저장")
    @PostMapping(value = "/safewizpro/improvement/voluntarySafetyInspector/saveVoluntarySafetyInspectorList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveVoluntarySafetyInspectorList(
            @RequestBody List<VoluntarySafetyInspectorVO> voluntarySafetyInspectorList) throws Exception {

        voluntarySafetyInspectorService.saveVoluntarySafetyInspector(voluntarySafetyInspectorList);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", voluntarySafetyInspectorList);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), hashMap);
    }

    @Operation(summary = "자체검사원 등록부 삭제")
    @PostMapping(value = "/safewizpro/improvement/voluntarySafetyInspector/deleteVoluntarySafetyInspector")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteVoluntarySafetyInspector(
            @RequestBody List<VoluntarySafetyInspectorVO> voluntarySafetyInspectorList) throws Exception {

        voluntarySafetyInspectorService.deleteVoluntarySafetyInspector(voluntarySafetyInspectorList);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", null);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), hashMap);
    }

    @Operation(summary = "자체검사원 등록부 출력")
    @PostMapping(value = "/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorReport")
    public void getVoluntarySafetyInspectorReport(
            HttpServletRequest request, HttpServletResponse response,@RequestBody @Valid SpSearchVO searchVo) throws Exception {
        voluntarySafetyInspectorService.getVoluntarySafetyInspectorReport(request, response, searchVo);
    }
    
    @Operation(summary = "자체검사원 등록부 출력")
    @PostMapping(value = "/safewizpro/improvement/voluntarySafetyInspector/getVoluntarySafetyInspectorReportApichk")
    public void getVoluntarySafetyInspectorReportApichk(
            HttpServletRequest request, HttpServletResponse response,@RequestBody @Valid SpSearchVO searchVo) throws Exception {
    	
        voluntarySafetyInspectorService.getVoluntarySafetyInspectorReportApichk(request, response, searchVo);
    }
}
