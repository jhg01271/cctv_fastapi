package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingResultVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.JobMgmtVO;
import kr.co.igns.business.support.model.JobLevelVO;
import kr.co.igns.business.support.model.TrainingInstructorVO;
import kr.co.igns.business.support.service.JobCompetencyAssessmentService;
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
 * ※ 파일명 : JobCompetencyAssessment
 * ※ 기능명 : 직무적격성 평가서
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "직무적격성 평가서")
@RestController
@RequiredArgsConstructor
public class JobCompetencyAssessmentController {
    private final MessageService messageService;
    private final JobCompetencyAssessmentService jobCompetencyAssessmentService;

    @Operation(summary = "[card 직무 분야 팝업 데이터 조회]", description = "card 직무 분야 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getCardJobManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCardJobManageList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentDetailVO> list = jobCompetencyAssessmentService.getCardJobManageList(searchVo);

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

    @Operation(summary = "[직무 관리 팝업 데이터 조회]", description = "직무 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getJobManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJobManageList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobMgmtVO> list = jobCompetencyAssessmentService.getJobManageList(searchVo);

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

    @Operation(summary = "[직무 관리 팝업 예시 데이터 조회]", description = "직무 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getDataSetJobManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetJobManageList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobMgmtVO> list = jobCompetencyAssessmentService.getDataSetJobManageList(searchVo);

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

    @Operation(summary = "[직무 관리 팝업 저장]", description = "선택된 직무 관리 팝업을 저장한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/saveJobManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveJobManageList(@RequestBody List<JobMgmtVO> voList) throws Exception {

        List<JobMgmtVO> rtnDto = jobCompetencyAssessmentService.saveJobManageList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무 관리 팝업 삭제]", description = "선택된 직무 관리 팝업 데이터를 사용안함 처리한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/deleteJobManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobManageList(@RequestBody List<JobMgmtVO> voList) throws Exception {

        List<JobMgmtVO> rtnDto = jobCompetencyAssessmentService.deleteJobManageList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무 수준 관리 팝업 데이터 조회]", description = "직무 수준 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getJobLevelManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJobLevelManageList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobLevelVO> list = jobCompetencyAssessmentService.getJobLevelManageList(searchVo);

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

    @Operation(summary = "[직무 수준 관리 팝업 예시 데이터 조회]", description = "직무 수준 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getDataSetJobLevelManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetJobLevelManageList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobLevelVO> list = jobCompetencyAssessmentService.getDataSetJobLevelManageList(searchVo);

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

    @Operation(summary = "[직무 수준 관리 팝업 저장]", description = "선택된 직무 수준 관리 팝업을 저장한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/saveJobLevelManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveJobLevelManageList(@RequestBody List<JobLevelVO> voList) throws Exception {

        List<JobLevelVO> rtnDto = jobCompetencyAssessmentService.saveJobLevelManageList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무 수준 관리 팝업 삭제]", description = "선택된 직무 수준 관리 팝업 데이터를 사용안함 처리한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/deleteJobLevelManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobLevelManageList(@RequestBody List<JobLevelVO> voList) throws Exception {

        List<JobLevelVO> rtnDto = jobCompetencyAssessmentService.deleteJobLevelManageList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무적격성 평가서 조회]", description = "직무적격성 평가서를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getJobCompAssessList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJobCompAssessList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentVO> list = jobCompetencyAssessmentService.getJobCompAssessList(searchVo);

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

    @Operation(summary = "[직무적격성 평가서 전년도 조회]", description = "직무적격성 평가서 전년도를 조회한다")
    @GetMapping(value = "/safewizpro/support/JobCompetencyAssessment/getPreJobCompAssessList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPreJobCompAssessList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<JobCompetencyAssessmentVO> list = jobCompetencyAssessmentService.getPreJobCompAssessList(searchVo);

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
    @Operation(summary = "[직무적격성 평가서 전년도 저장]", description = "직무적격성 평가서 전년도를 저장한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/savePreJobCompAssess")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePreJobCompAssess(@RequestBody List<JobCompetencyAssessmentVO> voList) throws Exception {

        BaseVO rtnDto = jobCompetencyAssessmentService.savePreJobCompAssess(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무적격성 평가서 저장]", description = "직무적격성 평가서를 저장한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/saveJobCompAssess")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveJobCompAssess(@RequestBody JobCompetencyAssessmentVO vo) throws Exception {

        BaseVO rtnDto = jobCompetencyAssessmentService.saveJobCompAssess(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무적격성 평가서 상세 저장]", description = "직무적격성 평가서 상세를 저장한다")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/saveJobCompAssessDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveJobCompAssessDetail(@RequestBody JobCompetencyAssessmentVO vo) throws Exception {

        BaseVO rtnDto = jobCompetencyAssessmentService.saveJobCompAssessDetail(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직무적격성 평가서 삭제]", description = "직무적격성 평가서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/JobCompetencyAssessment/deleteJobCompAssess")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobCompAssess(@RequestBody List<JobCompetencyAssessmentVO> list) throws Exception {
        BaseVO rtnDto = jobCompetencyAssessmentService.deleteJobCompAssess(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[직무적격성 평가서 상세 삭제]", description = "직무적격성 평가서 상세를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/JobCompetencyAssessment/deleteJobCompAssessDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobCompAssessDetail(@RequestBody List<JobCompetencyAssessmentDetailVO> list) throws Exception {
        BaseVO rtnDto = jobCompetencyAssessmentService.deleteJobCompAssessDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[직무 적격성 평가서 레포트 출력]", description = "직무 적격성 평가서 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/support/JobCompetencyAssessment/getJobCompAssessReport")
    public void getJobCompAssessReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            jobCompetencyAssessmentService.getJobCompAssessReport(request, response, spSearchVO);
    }
}
