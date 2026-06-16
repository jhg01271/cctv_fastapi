package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.impl.service.ContractorAssmtReportService;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
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

@Tag(name = "ContractorAssmtReport", description = "협력업체 안전보건 평가보고서")
@RestController
@RequiredArgsConstructor
public class ContractorAssmtReportController {
    private final MessageService messageService;
    private final ContractorAssmtReportService service;

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 조회]", description = "협력업체 안전보건 평가보고서에 대한 평가항목 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getEvaluationType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationType(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorChecklistVO> list = service.getEvaluationType(spSearchVO);

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

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 조회]", description = "협력업체 안전보건 평가보고서에 대한 평가항목(상세) 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getEvaluationTypeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationTypeDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorChecklistDetailVO> list = service.getEvaluationTypeDetail(spSearchVO);

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

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 예시 조회]", description = "협력업체 안전보건 평가보고서에 대한 평가항목을 예시 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getEvaluationTypeDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationTypeDataset(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorChecklistVO> list = service.getEvaluationTypeDataset(spSearchVO);

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

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 예시 상세 조회]", description = "협력업체 안전보건 평가보고서에 평가항목을 예시 데이터(상세)를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getEvaluationTypeDetailDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationTypeDetailDataset(
            @ModelAttribute @Valid ContractorChecklistVO spSearchVO) throws Exception {
        List<ContractorChecklistDetailVO> list = service.getEvaluationTypeDetailDataset(spSearchVO);

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

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 저장]", description = "협력업체 안전보건 평가보고서 평가항목을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/contractorAssmt/saveEvaluationType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationType(
            @RequestBody @Valid List<ContractorChecklistVO> voList)
            throws Exception {
        BaseVO rtnDto = service.saveEvaluationType(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[협력업체 안전보건 평가보고서 평가항목 삭제]", description = "협력업체 안전보건 평가항목을 삭제한다.")
    @DeleteMapping(value = "/safewizpro/impl/contractorAssmt/deleteEvaluationType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEvaluationType(
            @RequestBody @Valid List<ContractorChecklistVO> voList)
            throws Exception {
        BaseVO rtnDto = service.deleteEvaluationType(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[협력업체 안전보건 평가보고서 삭제]", description = "협력업체 안전보건 평가보고서를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/contractorAssmt/deleteAssmtReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAssmtReport( @RequestBody @Valid List<ContractorAssmtReportVO> vo) throws Exception {
        service.deleteAssmtReport(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", vo.get(0));
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[협력업체 안전보건 평가보고서 저장]", description = "협력업체 안전보건 평가보고서를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/contractorAssmt/saveAssmtReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveReport( @RequestBody @Valid List<ContractorAssmtReportVO> vo) throws Exception {
        List<ContractorAssmtReportVO> resultList = service.saveAssmtReport(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", resultList);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[협력업체 안전보건 평가보고서 조회]", description = "협력업체 안전보건 평가보고서에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getAssmtReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAssmtReport(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorAssmtReportVO> list = service.getAssmtReport(spSearchVO);

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

    @Operation(summary = "[협력업체 안전보건 평가보고서 조회]", description = "협력업체 안전보건 평가보고서에 대한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/contractorAssmt/getAssmtReportDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAssmtReportDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorAssmtReportVO> list = service.getAssmtReportDetail(spSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }


    @Operation(summary = "[조사표 출력]", description = "조사표 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/contractorAssmt/getReport")
    public void getReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
                service.getReport(request, response, spSearchVO);
    }
}
