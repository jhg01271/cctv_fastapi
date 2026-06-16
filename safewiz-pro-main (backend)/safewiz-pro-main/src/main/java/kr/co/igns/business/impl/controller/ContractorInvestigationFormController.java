package kr.co.igns.business.impl.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.impl.service.ContractorInvestigationFormService;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.PartnerVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : ContractorInvestigationForm
 * ※ 기능명 : 계약 대상업체 조사표
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 18.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ContractorInvestigationForm", description = "계약 대상업체 조사표")
@RestController
@RequiredArgsConstructor
public class ContractorInvestigationFormController {
    private final MessageService messageService;
    private final ContractorInvestigationFormService contractorInvestigationFormService;

    @Operation(summary = "[조사표 조회]", description = "조사표에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getInvestigationFormList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInvestigationFormList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorInvestigationFormVO> list = contractorInvestigationFormService.getInvestigationFormList(spSearchVO);

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

    @Operation(summary = "[협력사 등록]", description = "협력사를 등록한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/insertPartner", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertPartner(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @NotNull @RequestPart(value = "files2", required = false) List<MultipartFile> files2,
            @RequestPart(value = "info") PartnerVO reqVo)
            throws Exception {
        BaseVO rtnDto = contractorInvestigationFormService.insertPartner(files,files2, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조사표 저장]", description = "조사표 데이터를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/saveInvestigationForm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveInvestigationForm(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") ContractorInvestigationFormVO vo)
            throws Exception {
        BaseVO rtnDto = contractorInvestigationFormService.saveInvestigationForm(files,vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }


    @Operation(summary = "[조사표 현재 상태 조회]", description = "조사표에 현재 상태에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getFinalUseInspectionType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getFinalUseInspectionType(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorInvestigationFormDetailVO> list = contractorInvestigationFormService.getFinalUseInspectionType(spSearchVO);

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

    @Operation(summary = "[조사표 삭제]", description = "조사표 데이터를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/impl/investigation/deleteInvestigationForm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteInvestigationForm(
            @RequestBody List<ContractorInvestigationFormVO> voList)
            throws Exception {
        BaseVO rtnDto = contractorInvestigationFormService.deleteInvestigationForm(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조사표 출력]", description = "조사표 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/getInvestigationReport")
    public void getTrainingReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            contractorInvestigationFormService.getInvestigationReport(request, response, spSearchVO);
    }


    // 점검사항 관리 팝업 관련 API
    @Operation(summary = "[점검항목 조회]", description = "점검항목에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getInspectionType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInspectionType(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorInvestigationFormDetailVO> list = contractorInvestigationFormService.getInspectionType(spSearchVO);

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

    // 점검사항 관리 팝업 관련 API
    @Operation(summary = "[점검사항 조회]", description = "점검사항에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getInspectionTypeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInspectionTypeDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorInvestigationFormDetailVO> list = contractorInvestigationFormService.getInspectionTypeDetail(spSearchVO);

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

    @Operation(summary = "[점검사항 예시 조회]", description = "점검사항에 대한 유형 예시 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getInspectionTypeDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInspectionTypeDataset(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorInvestigationFormDetailVO> list = contractorInvestigationFormService.getInspectionTypeDataset(spSearchVO);

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

    @Operation(summary = "[점검사항 저장]", description = "점검사항 데이터를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/saveInspectionType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveInspectionType(
            @RequestBody @Valid List<ContractorInvestigationFormDetailVO> vo)
            throws Exception {
        List<ContractorInvestigationFormDetailVO> rtnDto = contractorInvestigationFormService.saveInspectionType(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[점검사항 삭제]", description = "점검사항 데이터를 삭제한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/deleteInspectionType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteInspectionType(
            @RequestBody @Valid List<ContractorInvestigationFormDetailVO> voList)
            throws Exception {
        List<ContractorInvestigationFormDetailVO> list = contractorInvestigationFormService.deleteInspectionType(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }


    @Operation(summary = "[조사표 조회]", description = "조사표에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/investigation/getPassScore")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPassScore(
            @ModelAttribute @Valid ContractorInvestigationFormVO searchVo) throws Exception {
        ContractorInvestigationFormVO vo = contractorInvestigationFormService.getPassScore(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        if(vo != null){
            result.put("result", vo.getScore());
        }

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[년도별 협력사 합격 점수 저장]", description = "년도별 협력사의 합격 점수를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/investigation/savePassScore")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePassScore(
           @RequestBody @Valid ContractorInvestigationFormVO vo)
            throws Exception {
        ContractorInvestigationFormVO rtnDto = contractorInvestigationFormService.savePassScore(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
}
