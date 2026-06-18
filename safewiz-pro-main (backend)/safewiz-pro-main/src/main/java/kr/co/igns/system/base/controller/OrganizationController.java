package kr.co.igns.system.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.OrgChartVO;
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
public class OrganizationController {
    private final MessageService messageService;
    private final OrganizationService organizationService;

    @Operation(summary = "[조직 조회]", description = "조직을 조회한다")
    @GetMapping(value = "/system/base/organization/getOrganization")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganization(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<OrganizationVO> list = organizationService.getOrganization(searchVo);

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

    @Operation(summary = "[조직 차트 조회]", description = "조직도 목록을 조회한다")
    @GetMapping(value = "/system/base/organization/getOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationChart(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<OrgChartVO> list = organizationService.getOrganizationChart(searchVo);

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
    @GetMapping(value = "/system/base/organization/getOrganizationChartDetail/{chartId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationChartDetail(
        @PathVariable String chartId) throws Exception {
        
        OrgChartVO list = organizationService.getOrganizationChartDetail(chartId);
        
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
    @PutMapping(value = "/system/base/confirmOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmOrganizationChart(
            @RequestBody @Valid OrgChartVO reqVO) throws Exception {
        BaseVO rtnDto = organizationService.confirmOrganizationChart(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 일괄 삭제]", description = "조직도를 삭제 한다.")
    @DeleteMapping(value = "/system/base/organization/deleteOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteOrganizationChart(
            @RequestBody List<OrgChartVO> list)
            throws Exception {
        organizationService.deleteOrganizationChart(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[조직 상세 조회]", description = "조직 상세정보를 조회한다")
    @GetMapping(value = "/system/base/organization/getOrganizationDetail/{orgnId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrganizationDetail(
            @PathVariable String orgnId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(orgnId);
        OrganizationVO list = organizationService.getOrganizationDetail(searchVo);

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

    @Operation(summary = "[조직 등록]", description = "조직을 등록한다.")
    @PostMapping(value = "/system/base/organization/insertOrganization")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertOrganization(
            @RequestBody @Valid OrganizationVO reqVo)
            throws Exception {
        BaseVO rtnDto = organizationService.insertOrganization(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 등록]", description = "조직을 등록한다.")
    @PostMapping(value = "/system/base/organization/insertOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertOrganizationChart(
            @RequestBody @Valid OrgChartVO reqVo)
            throws Exception {
        BaseVO rtnDto = organizationService.insertOrganizationChart(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조직 수정]", description = "orgn_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/organization/{orgnId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateOrganization(
            @RequestBody @Valid OrganizationVO reqVO,
            @PathVariable String orgnId) throws Exception {
        reqVO.setOrgnId(orgnId);
        BaseVO rtnDto = organizationService.updateOrganization(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 수정]", description = "orgn_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/organizationChart/{chartId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateOrganizationChart(
            @RequestBody @Valid OrgChartVO reqVO,
            @PathVariable String chartId) throws Exception {
        reqVO.setChartId(chartId);
        BaseVO rtnDto = organizationService.updateOrganizationChart(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[조직 삭제]", description = "orgn_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/organization/{orgnId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteOrganization(@PathVariable String orgnId)
            throws Exception {
        OrganizationVO param = new OrganizationVO();
        param.setOrgnId(orgnId);
        BaseVO rtnDto = organizationService.deleteOrganization(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[조직 일괄 삭제]", description = "orgn_id를 list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/organization/deleteOrganizations")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteOrganizations(
            @RequestBody List<OrganizationVO> list)
            throws Exception {
        organizationService.deleteOrganizations(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[조직도 출력]", description = "조직도 레포트를 제작한다")
    @PostMapping(value = "/system/base/organization/getOrgChartReport")
    public void getOrgChartReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            organizationService.getOrgChartReport(request, response, spSearchVO);
    }

    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/organization/downloadOrgnExcelTemplate/{id}")
    public void downloadHrExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        organizationService.downloadOrgnExcelTemplate(request, response, id);
    }

    @Operation(summary = "[조직 등록 - 엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/organization/insertOrgnExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertHrExcel(
            @RequestBody List<OrganizationVO> list)
            throws Exception {
        List<OrganizationVO> orgnList = organizationService.insertOrgnExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", orgnList);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

}
