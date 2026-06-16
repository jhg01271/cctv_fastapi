package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
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
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : EmergencyControlOrganChart
 * ※ 기능명 : 비상통제 조직도
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 31.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "EmergencyResponse", description = "비상 시 대비 및 대응")
@RestController
@RequiredArgsConstructor
public class EmergencyControlOrganChartController {
    private final MessageService messageService;
    private final EmergencyControlOrganChartService emergencyControlOrganChartService;

    @Operation(summary = "[비상통제 조직도 조회]", description = "비상통제 조직도 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyOrgChartList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyOrgChartList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyControlOrganChartVO> list = emergencyControlOrganChartService.getEmergencyOrgChartList(spSearchVO);

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

    @Operation(summary = "[비상통제 조직도 비상사태 유형 조회]", description = "비상통제 조직도 비상사태에 대한 유형 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyTypeList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyTypeList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyRoleVO> list = emergencyControlOrganChartService.getEmergencyTypeList(spSearchVO);

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

    @Operation(summary = "[비상통제 조직도 비상사태에 대한 역할 조회]", description = "비상통제 조직도 비상사태에 대한 역할 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyRoleList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyRoleList(
            @ModelAttribute @Valid EmergencyRoleVO vo) throws Exception {
        List<EmergencyRoleVO> list = emergencyControlOrganChartService.getEmergencyRoleList(vo);

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

    @Operation(summary = "[비상통제 조직도 확정여부 저장]", description = "비상통제 조직도 확정여부를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/chart/confirmEmergencyOrgChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmEmergencyOrgChart(
            @RequestBody @Valid EmergencyControlOrganChartVO vo)
            throws Exception {
        BaseVO rtnDto = emergencyControlOrganChartService.confirmEmergencyOrgChart(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직도 저장]", description = "비상통제 조직도를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/chart/saveEmergencyOrgChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEmergencyOrgChart(
            @RequestBody @Valid EmergencyControlOrganChartVO vo)
            throws Exception {
        BaseVO rtnDto = emergencyControlOrganChartService.saveEmergencyOrgChart(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직도 삭제]", description = "비상통제 조직도를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/chart/deleteEmergencyOrgChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEmergencyOrgChart(@RequestBody List<EmergencyControlOrganChartVO> list) throws Exception {
        BaseVO rtnDto = emergencyControlOrganChartService.deleteEmergencyOrgChart(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    // 비상사태 유형 관리 팝업 관련 API
    @Operation(summary = "[비상통제 조직도 비상사태 유형 조회]", description = "비상통제 조직도 비상사태에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyType(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyRoleVO> list = emergencyControlOrganChartService.getEmergencyType(spSearchVO);

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
    // 비상사태 유형 관리 팝업 관련 API
    @Operation(summary = "[비상통제 조직도 비상사태 유형에 대한 역할 조회]", description = "비상통제 조직도 비상사태에 대한 역할 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyTypeRole")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyTypeRole(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyRoleVO> list = emergencyControlOrganChartService.getEmergencyTypeRole(spSearchVO);

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

    @Operation(summary = "[비상통제 조직도 비상사태 유형 예시 조회]", description = "비상통제 조직도 비상사태에 대한 유형 예시 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/chart/getEmergencyTypeDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyTypeDataSet(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyRoleVO> list = emergencyControlOrganChartService.getEmergencyTypeDataset(spSearchVO);

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

    @Operation(summary = "[비상통제 조직도 비상사태 유형 삭제]", description = "비상통제 조직도 비상사태 유형을 삭제한다.")
    @DeleteMapping(value = "/safewizpro/impl/chart/deleteEmergencyType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEmergencyType(
            @RequestBody @Valid List<EmergencyRoleVO> voList)
            throws Exception {
        BaseVO rtnDto = emergencyControlOrganChartService.deleteEmergencyType(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직도 비상사태 유형 저장]", description = "비상통제 조직도 비상사태 유형을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/chart/saveEmergencyType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEmergencyType(
            @RequestBody @Valid List<EmergencyRoleVO> voList)
            throws Exception {
        BaseVO rtnDto = emergencyControlOrganChartService.saveEmergencyType(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직도 출력]", description = "비상통제 조직도 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/impl/chart/getEmergencyOrgChartReport")
    public void getEmergencyOrgChartReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            emergencyControlOrganChartService.getEmergencyOrgChartReport(request, response, spSearchVO);
    }
}
