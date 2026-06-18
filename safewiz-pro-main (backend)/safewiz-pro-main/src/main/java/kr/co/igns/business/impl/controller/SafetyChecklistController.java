package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.SafetyChecklistHrVO;
import kr.co.igns.business.impl.model.SafetyChecklistItemVO;
import kr.co.igns.business.impl.model.SafetyChecklistVO;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Safety Checklist
 * ※ 기능명 : 안전점검표
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 05.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전점검표")
@RestController
@RequiredArgsConstructor
public class SafetyChecklistController {
    private final MessageService messageService;
    private final SafetyChecklistService safetyChecklistService;

    @Operation(summary = "[설비유형별 안전점검표 존재유무 확인]", description = "설비유형별 안전점검표의 존재유무를 확인한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/hasSafetyChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> hasSafetyChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        boolean list = safetyChecklistService.hasSafetyChecklist(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                messageService.getMessage("readSuccess.msg"), result);
    }

    @Operation(summary = "[안전점검표 조회]", description = "안전점검표를 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/getSafetyChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistVO> list = safetyChecklistService.getSafetyChecklist(searchVo);

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

    @Operation(summary = "[안전점검표 항목 및 사항 조회]", description = "안전점검표 항목 및 사항을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/getSafetyChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyChecklistDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        SafetyChecklistVO list = safetyChecklistService.getSafetyChecklistDetail(searchVo);

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

    @Operation(summary = "[안전점검표 항목 조회]", description = "안전점검표 항목을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/getSafetyChecklistItem")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyChecklistItem(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistItemVO> list = safetyChecklistService.getSafetyChecklistItem(searchVo);

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

    @Operation(summary = "[안전점검표 항목 및 사항 저장]", description = "안전점검표 항목 및 사항 저장한다.")
    @PostMapping(value = "/safewizpro/impl/safetyChecklist/saveSafetyChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyChecklist(
            @RequestBody SafetyChecklistVO voList)
            throws Exception {
        BaseVO rtnDto = safetyChecklistService.saveSafetyChecklist(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전점검표 삭제]", description = "안전점검표를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/safetyChecklist/deleteSafetyChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyChecklist(@RequestBody List<SafetyChecklistVO> list) throws Exception {
        safetyChecklistService.deleteSafetyChecklist(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전점검표 항목 및 사항 삭제]", description = "안전점검표 항목 및 사항을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/safetyChecklist/deleteSafetyChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyChecklistDetail(@RequestBody SafetyChecklistVO list) throws Exception {
        safetyChecklistService.deleteSafetyChecklistDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    //안전점검 담당자 조회
    @Operation(summary = "[안전점검 담당자 조회]", description = "안전점검 담당자를 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/getSafetyCheckInspector")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyCheckInspector(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistHrVO> list = safetyChecklistService.getSafetyCheckInspector(searchVo);

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

    //안전점검 담당자 불러오기
    @Operation(summary = "[안전점검 담당자 불러오기]", description = "설비별 담당자를 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyChecklist/getEqInspector")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEqInspector(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistHrVO> list = safetyChecklistService.getEqInspector(searchVo);

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

    //안전점검 담당자 저장
    @Operation(summary = "[안전점검 담당자 저장]", description = "안전점검 담당자을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/safetyChecklist/saveSafetyCheckInspector")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyCheckInspector(
            @RequestBody List<SafetyChecklistHrVO> voList)
            throws Exception {
        BaseVO rtnDto = safetyChecklistService.saveSafetyCheckInspector(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
}
