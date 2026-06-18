package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.impl.service.HazmatChecklistService;
import kr.co.igns.business.impl.service.WHMgmtGuideService;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
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
 * ※ 파일명 : Hazmat Checklist
 * ※ 기능명 : 작업장 위험물/유해화학물질 점검
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 08.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "작업장 위험물/유해화학물질 점검")
@RestController
@RequiredArgsConstructor
public class HazmatChecklistController {
    private final MessageService messageService;
    private final HazmatChecklistService hazmatChecklistService;

    @Operation(summary = "[작업장 위험물/유해화학물질 점검 조회]", description = "작업장 위험물/유해화학물질 점검을 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazmatChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HazmatChecklistVO> list = hazmatChecklistService.getHazmatChecklist(searchVo);

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

    @Operation(summary = "[작업장 위험물/유해화학물질 점검사항 조회]", description = "작업장 위험물/유해화학물질 점검사항을 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getHazmatChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazmatChecklistDetail(@ModelAttribute @Valid HazmatChecklistVO searchVo) throws Exception {
        HazmatChecklistVO list = hazmatChecklistService.getHazmatChecklistDetail(searchVo);

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

    @Operation(summary = "[작업장 위험물/유해화학물질 점검사항 저장]", description = "작업장 위험물/유해화학물질 점검사항을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/hazmatChecklist/saveHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHazmatChecklist(
            @RequestBody HazmatChecklistVO voList)
            throws Exception {
        BaseVO rtnDto = hazmatChecklistService.saveHazmatChecklist(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 점검 삭제]", description = "작업장 위험물/유해화학물질 점검을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/hazmatChecklist/deleteHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHazmatChecklist(@RequestBody List<HazmatChecklistVO> list) throws Exception {
        hazmatChecklistService.deleteHazmatChecklist(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 점검사항 초기화]", description = "작업장 위험물/유해화학물질 점검사항을 초기화한다.")
    @DeleteMapping(value = "/safewizpro/impl/hazmatChecklist/deleteHazmatChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHazmatChecklistDetail(@RequestBody HazmatChecklistVO list) throws Exception {
        hazmatChecklistService.deleteHazmatChecklistDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 점검리스트 보고서]", description = "작업장 위험물/유해화학물질 점검리스트 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/hazmatChecklist/getHazmatChecklistReport")
    public void getHazmatChecklistReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        hazmatChecklistService.getHazmatChecklistReport(request, response, spSearchVO);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 현황표 보고서]", description = "작업장 위험물/유해화학물질 현황표 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/hazmatChecklist/getHazmatStatusReport")
    public void getHazmatStatusReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        hazmatChecklistService.getHazmatStatusReport(request, response, spSearchVO);
    }

    //마지막 점검사항 불러오기
    @Operation(summary = "[마지막 점검사항 불러오기]", description = "마지막 점검사항 불러온다.")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getLastChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLastChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HazmatChecklistDetailVO> list = hazmatChecklistService.getLastChecklist(searchVo);

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

    //점검사항 관리
    @Operation(summary = "[점검사항 관리 조회]", description = "점검사항 관리를 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getDatasetHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetHazmatChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<DatasetHazmatChecklistVO> list = hazmatChecklistService.getDatasetHazmatChecklist(searchVo);

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
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getDatasetHazmatChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetHazmatChecklistDetail(@ModelAttribute @Valid DatasetHazmatChecklistVO vo) throws Exception {
        List<DatasetHazmatChecklistVO> list = hazmatChecklistService.getDatasetHazmatChecklistDetail(vo);

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
    @PostMapping(value = "/safewizpro/impl/hazmatChecklist/saveDatasetHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveDatasetHazmatChecklist(
            @RequestBody List<DatasetHazmatChecklistVO> voList)
            throws Exception {
        List<DatasetHazmatChecklistVO> rtnDto = hazmatChecklistService.saveDatasetHazmatChecklist(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[점검사항 항목 삭제]", description = "점검사항 항목을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/hazmatChecklist/deleteDatasetHazmatChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteDatasetHazmatChecklistDetail(@RequestBody List<DatasetHazmatChecklistVO> list) throws Exception {
        hazmatChecklistService.deleteDatasetHazmatChecklistDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[점검사항 관리 - 예시불러오기]", description = "점검사항 예시를 불러온다.")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getBaseDatasetHazmatChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getBaseDatasetHazmatChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<DatasetHazmatChecklistVO> list = hazmatChecklistService.getBaseDatasetHazmatChecklist(searchVo);

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

    //유해화학물질 - 작업장별 조회
    @Operation(summary = "[작업장별 유해화학물질 조회]", description = "작업장별 유해화학물질을 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatChecklist/getMsdsByWorkplace")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsdsByWorkplace(@ModelAttribute @Valid HazmatChecklistVO searchVo) throws Exception {
        List<MsdsVO> list = hazmatChecklistService.getMsdsByWorkplace(searchVo);

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
}
