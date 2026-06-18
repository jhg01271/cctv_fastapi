package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.impl.service.PermitToWorkService;
import kr.co.igns.business.impl.service.SafetyChecklistService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Permit To Work
 * ※ 기능명 : 안전작업 허가서
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 07.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전작업 허가서")
@RestController
@RequiredArgsConstructor
public class PermitToWorkController {
    private final MessageService messageService;
    private final PermitToWorkService permitToWorkService;

    @Operation(summary = "[안전작업 허가서 조회]", description = "안전작업 허가서를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getPermitToWork")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPermitToWork(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<PermitToWorkVO> list = permitToWorkService.getPermitToWork(searchVo);

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

    @Operation(summary = "[안전작업 허가서 상세 조회]", description = "안전작업 허가서 상세를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getPermitToWorkDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPermitToWorkDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        PermitToWorkVO list = permitToWorkService.getPermitToWorkDetail(searchVo);

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

    @Operation(summary = "[안전작업 허가서 저장]", description = "안전작업 허가서를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/permitToWork/savePermitToWork")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePermitToWork(
            @RequestBody PermitToWorkVO voList)
            throws Exception {
        BaseVO rtnDto = permitToWorkService.savePermitToWork(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전작업 허가서 파일 저장]", description = "안전작업 허가서 첨부파일을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/permitToWork/savePermitToWorkFiles")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePermitToWorkFiles(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") PermitToWorkVO vo) throws Exception {
    	permitToWorkService.savePermitToWorkFiles(files, vo);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("result", null);
    	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
    			messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전작업 허가서 삭제]", description = "안전작업 허가서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/permitToWork/deletePermitToWorkList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePermitToWorkList(@RequestBody List<PermitToWorkVO> list) throws Exception {
        permitToWorkService.deletePermitToWorkList(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전작업 허가서 점검사항 삭제]", description = "안전작업 점검사항 허가서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/permitToWork/deletePermitToWork")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePermitToWork(@RequestBody PermitToWorkVO vo) throws Exception {
        permitToWorkService.deletePermitToWork(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전작업 허가서 보고서]", description = "안전작업 허가서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/permitToWork/getPermitToWorkReport")
    public void getPermitToWorkReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        permitToWorkService.getPermitToWorkReport(request, response, spSearchVO);
    }

    //점검사항 관리
    @Operation(summary = "[점검사항 관리 조회]", description = "점검사항 관리를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getSafetyWorkChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyWorkChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyWorkChecklistVO> list = permitToWorkService.getSafetyWorkChecklist(searchVo);

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

    @Operation(summary = "[점검사항 관리 항목 조회]", description = "점검사항 관리 항목를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getSafetyWorkChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyWorkChecklistDetail(@ModelAttribute @Valid SafetyWorkChecklistVO vo) throws Exception {
        List<SafetyWorkChecklistVO> list = permitToWorkService.getSafetyWorkChecklistDetail(vo);

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

    @Operation(summary = "[점검사항 관리 저장]", description = "점검사항를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/permitToWork/saveSafetyWorkChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyWorkChecklist(
            @RequestBody SafetyWorkChecklistVO voList)
            throws Exception {
        BaseVO rtnDto = permitToWorkService.saveSafetyWorkChecklist(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[점검사항 항목 삭제]", description = "점검사항 항목을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/permitToWork/deleteSafetyWorkChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyWorkChecklistDetail(@RequestBody List<SafetyWorkChecklistVO> list) throws Exception {
        permitToWorkService.deleteSafetyWorkChecklistDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    //안전기구 관리
    @Operation(summary = "[안전기구 조회]", description = "안전기구를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getSafetyEquipment")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyEquipment(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyEquipmentVO> list = permitToWorkService.getSafetyEquipment(searchVo);

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

    @Operation(summary = "[안전기구 항목 조회]", description = "안전기구 항목를 조회한다")
    @GetMapping(value = "/safewizpro/impl/permitToWork/getSafetyEquipmentDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyEquipmentDetail(@ModelAttribute @Valid SafetyEquipmentVO vo) throws Exception {
        List<SafetyEquipmentVO> list = permitToWorkService.getSafetyEquipmentDetail(vo);

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

    @Operation(summary = "[안전기구 저장]", description = "안전기구를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/permitToWork/saveSafetyEquipment")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyEquipment(
            @RequestBody SafetyEquipmentVO voList)
            throws Exception {
        BaseVO rtnDto = permitToWorkService.saveSafetyEquipment(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전기구 항목 삭제]", description = "안전기구 항목을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/permitToWork/deleteSafetyEquipmentDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyEquipmentDetail(@RequestBody List<SafetyEquipmentVO> list) throws Exception {
        permitToWorkService.deleteSafetyEquipmentDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

}
