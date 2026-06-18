package kr.co.igns.business.evaluation.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.AuditExecutionPlanVO;
import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.business.evaluation.service.AuditExecutionPlanService;
import kr.co.igns.business.evaluation.service.InternalAuditService;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
 * ※ 패키지 : kr.co.igns.business.evaludation.controller
 * ※ 파일명 : AuditExecutionPlan
 * ※ 기능명 : 안전보건 내부심사 수행계획서
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "AuditExecutionPlan", description = "안전보건 내부심사 수행 계획서")
@RestController
@RequiredArgsConstructor
public class AuditExecutionPlanController {
    private final MessageService messageService;
    private final AuditExecutionPlanService auditExecutionPlanService;

    @Operation(summary = "[수행계획서 조회]", description = "수행계획서 조회 데이터 를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getAuditExecutionPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAuditExecutionPlan(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<AuditExecutionPlanVO> list = auditExecutionPlanService.getAuditExecutionPlan(spSearchVO);

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

    @Operation(summary = "[수행계획서 심사 팀원 목록 조회]", description = "수행계획서 심사가능 팀원 목록을 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getJudgeList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJudgeList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<HrVO> list = auditExecutionPlanService.getJudgeList(spSearchVO);

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

    @Operation(summary = "[수행 계획서 저장]", description = "수행 계획서를 저장한다")
    @PostMapping(value = "/safewizpro/evaluation/saveAuditExecutionPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveAuditExecutionPlan(@RequestBody AuditExecutionPlanVO vo) throws Exception {

        BaseVO rtnDto = auditExecutionPlanService.saveAuditExecutionPlan(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[수행 계획서 삭제]", description = "수행 계획서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/deleteAuditExecutionPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAuditExecutionPlan(@RequestBody List<AuditExecutionPlanVO> list) throws Exception {
        BaseVO rtnDto = auditExecutionPlanService.deleteAuditExecutionPlan(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[수행 계획서 출력]", description = "수행 계획서 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/evaluation/getAuditExecutionPlanReport")
    public void getAuditExecutionPlanReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            auditExecutionPlanService.getAuditExecutionPlanReport(request, response, spSearchVO);
    }

}
