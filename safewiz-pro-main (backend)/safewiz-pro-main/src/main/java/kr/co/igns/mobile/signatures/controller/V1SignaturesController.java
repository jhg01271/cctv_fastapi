package kr.co.igns.mobile.signatures.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.signatures.model.ApprovalSignaturesVO;
import kr.co.igns.mobile.signatures.model.CommitteeSignaturesVO;
import kr.co.igns.mobile.signatures.model.SignaturesSearchVO;
import kr.co.igns.mobile.signatures.model.SignaturesVO;
import kr.co.igns.mobile.signatures.service.V1SignaturesService;
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
 * ※ 패키지 : kr.co.igns.mobile.signatures.controller
 * ※ 파일명 : Signatures
 * ※ 기능명 : 참석자 서명
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 11. 27.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "Signatures", description = "참석자 서명")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1SignaturesController {
    private final MessageService messageService;
    private final V1SignaturesService signaturesService;

    @Operation(summary = "[위원회 참석자 서명 목록 조회]", description = "위원회 참석자 서명을 조회하는 API")
    @GetMapping(value = "/committees/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCommitteesSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        CommitteeSignaturesVO data = signaturesService.getCommitteesSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[위원회 참석자 서명 등록]", description = "특정 위원회의 자신의 서명을 등록하는 API")
    @PutMapping(value = "/committees/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCommitteeSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.saveCommitteeSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[위원회 참석자 서명 취소]", description = "특정 위원회에 등록된 자신의 서명을 취소하는 API")
    @DeleteMapping(value = "/committees/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCommitteeSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.deleteCommitteeSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[교육 결과 참석자 서명 목록 조회]", description = "특정 교육의 참석자 서명 목록을 조회하는 API")
    @GetMapping(value = "/trainings/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingsSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        List<SignaturesVO> list = signaturesService.getTrainingsSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[교육 결과 참석자 서명 등록]", description = "특정 교육에 자신의 서명을 등록하는 API")
    @PutMapping(value = "/trainings/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTrainingsSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.saveTrainingsSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[교육 결과 참석자 서명 취소]", description = "특정 교육에 등록된 자신의 서명을 취소하는 API")
    @DeleteMapping(value = "/trainings/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingsSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.deleteTrainingsSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[결재자 서명 목록 조회]", description = "결재자 서명을 조회하는 API")
    @GetMapping(value = "/approval/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getApprovalSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        ApprovalSignaturesVO data = signaturesService.getApprovalSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", data);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[결재자 서명 저장]", description = "결재자 서명을 저장하는 API")
    @PutMapping(value = "/approval/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveApprovalSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.saveApprovalSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[결재자 서명 취소]", description = "결재자 서명을 취소하는 API")
    @DeleteMapping(value = "/approval/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteApprovalSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.deleteApprovalSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[TBM 참석자 서명 목록 조회]", description = "특정 TBM의 참석자 서명 목록을 조회하는 API")
    @GetMapping(value = "/tbm/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTbmSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        List<SignaturesVO> list = signaturesService.getTbmSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                messageService.getMessage("readSuccess.msg"), result);
    }

    @Operation(summary = "[TBM 참석자 서명 등록]", description = "특정 TBM에 자신의 서명을 등록하는 API")
    @PutMapping(value = "/tbm/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTbmSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.saveTbmSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[TBM 참석자 서명 취소]", description = "특정 TBM에 등록된 자신의 서명을 취소하는 API")
    @DeleteMapping(value = "/tbm/{id}/signatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTbmSignatures(
            @PathVariable("id") String id, @ModelAttribute @Valid SignaturesSearchVO signaturesSearchVO) throws Exception {
        signaturesService.deleteTbmSignatures(id, signaturesSearchVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", signaturesSearchVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
}


