package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.SafetyInspectionLogSearchVO;
import kr.co.igns.business.impl.model.SafetyInspectionLogVO;
import kr.co.igns.business.impl.service.SafetyInspectionLogService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.common.model.BaseVO;
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
 * ※ 패키지 : ;
 * ※ 파일명 : Safety Inspection Log
 * ※ 기능명 : 안전점검일지
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 05.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전점검일지")
@RestController
@RequiredArgsConstructor
public class SafetyInspectionLogController {
    private final MessageService messageService;
    private final SafetyInspectionLogService safetyInspectionLogService;

    @Operation(summary = "[설비별 안전점검일지 목록 조회]", description = "설비별 안전점검일지 목록을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyInspectionLogList(@ModelAttribute @Valid SafetyInspectionLogSearchVO searchVo) throws Exception {
        List<SafetyInspectionLogVO> list = safetyInspectionLogService.getSafetyInspectionLogList(searchVo);

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

    @Operation(summary = "[설비별 안전점검일지 항목 및 사항 조회]", description = "설비별 안전점검일지 항목 및 사항을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLog")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyInspectionLog(@ModelAttribute @Valid SafetyInspectionLogSearchVO searchVo) throws Exception {
        SafetyInspectionLogVO list = safetyInspectionLogService.getSafetyInspectionLog(searchVo);

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

    @Operation(summary = "[설비별 안전점검일지 설비명 조회 (스케쥴러 라우터 이동용)]", description = "설비별 안전점검일지 설비명 및 설비유형 명을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyInspectionLog/getEquipInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEquipInfo(@ModelAttribute @Valid SafetyInspectionLogSearchVO searchVo) throws Exception {
        SafetyInspectionLogVO list = safetyInspectionLogService.getEquipInfo(searchVo);

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

    @Operation(summary = "[설비별 안전점검일지 설비유형 조회]", description = "설비별 안전점검일지 설비유형 목록을 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionStdEqList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyInspectionStdEqList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EquipVO> list = safetyInspectionLogService.getSafetyInspectionStdEqList(searchVo);

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

    @Operation(summary = "[설비별 안전점검일지 일자조회]", description = "설비별 안전점검일지 일자를 조회한다")
    @GetMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogDates")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyInspectionLogDates(@ModelAttribute @Valid SafetyInspectionLogSearchVO searchVo) throws Exception {
        List<String> list = safetyInspectionLogService.getSafetyInspectionLogDates(searchVo);

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

    @Operation(summary = "[안전점검일지 저장]", description = "안전점검일지 저장한다.")
    @PostMapping(value = "/safewizpro/impl/safetyInspectionLog/saveSafetyInspectionLog")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyInspectionLog(
            @RequestBody SafetyInspectionLogVO voList)
            throws Exception {
        BaseVO rtnDto = safetyInspectionLogService.saveSafetyInspectionLog(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전점검일지 삭제]", description = "안전점검일지를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/safetyInspectionLog/deleteSafetyInspectionLog")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyInspectionLog(@RequestBody SafetyInspectionLogVO reqVo) throws Exception {
        BaseVO rtnDto = safetyInspectionLogService.deleteSafetyInspectionLog(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[설비별 안전점검표 보고서]", description = "설비별 안전점검표 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogReport")
    public void getSafetyInspectionLogReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        safetyInspectionLogService.getSafetyInspectionLogReport(request, response, spSearchVO);
    }

    @Operation(summary = "[설비별 안전점검표 보고서]", description = "설비별 안전점검표 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/safetyInspectionLog/getSafetyInspectionLogReportList")
    public void getSafetyInspectionLogReportList(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        safetyInspectionLogService.getSafetyInspectionLogReportList(request, response, spSearchVO);
    }

}
