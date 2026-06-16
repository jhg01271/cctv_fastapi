package kr.co.igns.business.support.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.participation.model.HseObjectivesVO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.igns.business.support.model.QualificationCertificationAssessmentVO;
import kr.co.igns.business.support.model.QualificationCertificationDocVo;
import kr.co.igns.business.support.service.QualificationCertificationAssessmentService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class QualificationCertificationAssessmentController {

    private final MessageService messageService;
    private final QualificationCertificationAssessmentService service;

    @Operation(summary = "[메뉴 목록 조회]", description = "메뉴 목록 조회")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/getQualificationManagement")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getQualificationManagement(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EducationTrainingVO> list = service.getQualificationManagement(spSearchVO);

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

    @Operation(summary = "[평가표 항목 저장]", description = "평가표 항목 저장")
    @PostMapping(value = "/safewizpro/support/QualificationCertification/saveDocDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveDocDetail(
            @RequestBody List<QualificationCertificationDocVo> voList)
            throws Exception {
        BaseVO rtnDto = service.saveDocDetail(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[평가표 항목 조회]", description = "평가표 항목 조회")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/getEvaluationList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<QualificationCertificationDocVo> list = service.getEvaluationList(spSearchVO);

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

    @Operation(summary = "[평가표 항목 상세 조회]", description = "평가표 항목 상세 조회")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/getEvaluationDetailList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationDetailList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationDocVo> list = service.getEvaluationDetailList(searchVo);

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

    @Operation(summary = "[평가표 항목 상세 조회]", description = "평가표 항목 상세 조회")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/getAllEvaluationDetailList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAllEvaluationDetailList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationDocVo> list = service.getAllEvaluationDetailList(searchVo);

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

    @Operation(summary = "[평가표 항목 예시 불러오기 조회]", description = "평가표 항목 예시 불러오기 조회")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/getEvaluationDataSetList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationDataSetList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<QualificationCertificationDocVo> list = service.getEvaluationDataSetList(searchVo);

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

    @Operation(summary = "[평가표 기준 등록]", description = "평가표 기준 데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/support/QualificationCertification/saveEvaluationList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationList(
            @RequestBody List<QualificationCertificationDocVo> voList) throws Exception {
        List<QualificationCertificationDocVo> list = service.saveEvaluationList(voList);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", list);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[등록]", description = "데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/support/QualificationCertification/saveDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveDetail(
        @RequestBody QualificationCertificationAssessmentVO reqVo) throws Exception {
        BaseVO rtnDto = service.create(reqVo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조회]", description = "데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
            List<QualificationCertificationAssessmentVO> list = service.searchList(searchVo);

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

    @Operation(summary = "[삭제]", description = "데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/support/QualificationCertification/delete")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> delete(
        @RequestBody List<QualificationCertificationAssessmentVO> reqVo) throws Exception {
            BaseVO list = service.delete(reqVo);

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

    @Operation(summary = "[상세 조회]", description = "상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/support/QualificationCertification/searchDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCommitteeDetail(
        @ModelAttribute @Valid SpSearchVO reqVo) throws Exception {
        QualificationCertificationAssessmentVO list = service.search(reqVo);

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

    @Operation(summary = "[산업안전보건위원회 출력(특정년도)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/support/QualificationCertification/getReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
        service.getReport(request, response, vo);
    }
}
