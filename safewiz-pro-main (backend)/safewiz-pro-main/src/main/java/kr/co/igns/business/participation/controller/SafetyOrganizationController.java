package kr.co.igns.business.participation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.participation.model.SafetyOrgnRoleVO;
import kr.co.igns.business.participation.service.SafetyOrganizationService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.OrganizationVO;
import kr.co.igns.system.base.service.OrganizationService;
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
 * ※ 파일명 : Organization
 * ※ 기능명 : 조직 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Organization")
@RestController
@RequiredArgsConstructor
public class SafetyOrganizationController {
    private final MessageService messageService;
    private final SafetyOrganizationService organizationService;

    @Operation(summary = "[안전보건 조직구성 조회]", description = "안전보건 조직구성 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/participation/getSafetyOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyOrgList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<SafetyOrgnRoleVO> list = organizationService.getSafetyOrgList(searchVo);

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

    @Operation(summary = "[안전보건 조직구성 예시 불러오기]", description = "안전보건 조직구성 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/participation/getDataSetSafetyOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetSafetyOrgList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<SafetyOrgnRoleVO> list = organizationService.getDataSetSafetyOrgList(searchVo);

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

    @Operation(summary = "[조직도 등록]", description = "조직을 등록한다.")
    @PostMapping(value = "/safewizpro/participation/saveSafetyOrgList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyOrgList(
            @RequestBody @Valid List<SafetyOrgnRoleVO> reqVo)
            throws Exception {
        List<SafetyOrgnRoleVO> rtnDto = organizationService.saveSafetyOrgList(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조직 차트 조회]", description = "조직도 목록을 조회한다")
    @GetMapping(value = "/safewizpro/participation/getOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationChart(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyOrgChartVO> list = organizationService.getOrganizationChart(searchVo);

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

    @Operation(summary = "[조직 차트 상세 조회]", description = "조직도 상세 정보를 조회한다")
    @GetMapping(value = "/safewizpro/participation/getOrganizationChartDetail/{chartId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationChartDetail(
        @PathVariable String chartId) throws Exception {
        
        SafetyOrgChartVO list = organizationService.getOrganizationChartDetail(chartId);
        
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

    @Operation(summary = "[조직도 확정]", description = "orgn_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/safewizpro/participation/confirmOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmOrganizationChart(
            @RequestBody @Valid SafetyOrgChartVO reqVO) throws Exception {
        BaseVO rtnDto = organizationService.confirmOrganizationChart(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 일괄 삭제]", description = "조직도를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/deleteOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteOrganizationChart(
            @RequestBody List<SafetyOrgChartVO> list)
            throws Exception {
        organizationService.deleteOrganizationChart(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 등록]", description = "조직을 등록한다.")
    @PostMapping(value = "/safewizpro/participation/insertOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertOrganizationChart(
            @RequestBody @Valid SafetyOrgChartVO reqVo)
            throws Exception {
        BaseVO rtnDto = organizationService.insertOrganizationChart(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 수정]", description = "orgn_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/safewizpro/participation/organizationChart/{chartId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateOrganizationChart(
            @RequestBody @Valid SafetyOrgChartVO reqVO,
            @PathVariable String chartId) throws Exception {
        reqVO.setChartId(chartId);
        BaseVO rtnDto = organizationService.updateOrganizationChart(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 출력]", description = "조직도 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/participation/getShOrgChartReport")
    public void getShOrgChartReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            organizationService.getShOrgChartReport(request, response, spSearchVO);
    }


    @Operation(summary = "[안전보건 조직 인원의 조직 ID 조회]", description = "안전보건 조직 인원의 조직을 ID로 조회한다")
    @GetMapping(value = "/safewizpro/participation/getSafetyOrgnListById")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyOrgnListById(
            @RequestParam List<String> idList) throws Exception {
        List<HrVO> list = organizationService.getSafetyOrgnListById(idList);

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
