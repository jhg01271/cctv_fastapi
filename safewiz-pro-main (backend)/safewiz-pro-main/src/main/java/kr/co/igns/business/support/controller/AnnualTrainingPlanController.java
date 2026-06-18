package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.AnnualTrainingPlanVO;
import kr.co.igns.business.support.model.TrainingInstructorVO;
import kr.co.igns.business.support.model.TrainingCoursesVO;
import kr.co.igns.business.support.model.TrainingCenterVO;
import kr.co.igns.business.support.model.TrainningInstructorVO;
import kr.co.igns.business.support.service.AnnualTrainingPlanService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
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
 * ※ 파일명 : annual_training_plan
 * ※ 기능명 : 안전보건 연간교육 계획
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전보건 연간교육 계획")
@RestController
@RequiredArgsConstructor
public class AnnualTrainingPlanController {
    private final MessageService messageService;
    private final AnnualTrainingPlanService annualTrainingPlanService;

    @Operation(summary = "[과목/과정 관리 팝업 데이터 조회]", description = "과목/과정 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getSubjectsMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSubjectsMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingCoursesVO> list = annualTrainingPlanService.getSubjectsMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 예시 데이터 조회]", description = "과목/과정 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getDataSetSubjectsMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetSubjectsMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingCoursesVO> list = annualTrainingPlanService.getDataSetSubjectsMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 데이터 저장 및 수정]", description = "과목/과정 관리 팝업 데이터 저장 및 수정한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/saveSubjectsMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSubjectsMngList(
            @RequestBody List<TrainingCoursesVO> voList)
            throws Exception {
        List<TrainingCoursesVO> rtnDto = annualTrainingPlanService.saveSubjectsMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[과목/과정 관리 팝업 데이터 삭제(사용안함)]", description = "과목/과정 관리 팝업 데이터 삭제(사용안함)한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/deleteSubjectsMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSubjectsMngList(
            @RequestBody List<TrainingCoursesVO> voList)
            throws Exception {
        List<TrainingCoursesVO> rtnDto = annualTrainingPlanService.deleteSubjectsMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[과목/과정 관리 팝업 데이터 조회]", description = "과목/과정 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getEduMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEduMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingCenterVO> list = annualTrainingPlanService.getEduMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 예시 데이터 조회]", description = "과목/과정 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getDataSetEduMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetEduMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingCenterVO> list = annualTrainingPlanService.getDataSetEduMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 데이터 저장 및 수정]", description = "과목/과정 관리 팝업 데이터 저장 및 수정한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/saveEduMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEduMngList(
            @RequestBody List<TrainingCenterVO> voList)
            throws Exception {
        List<TrainingCenterVO> rtnDto = annualTrainingPlanService.saveEduMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[과목/과정 관리 팝업 데이터 삭제(사용안함)]", description = "과목/과정 관리 팝업 데이터 삭제(사용안함)한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/deleteEduMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEduMngList(
            @RequestBody List<TrainingCenterVO> voList)
            throws Exception {
        List<TrainingCenterVO> rtnDto = annualTrainingPlanService.deleteEduMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[과목/과정 관리 팝업 데이터 조회]", description = "과목/과정 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getInstMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInstMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainningInstructorVO> list = annualTrainingPlanService.getInstMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 예시 데이터 조회]", description = "과목/과정 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getDataSetInstMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetInstMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainningInstructorVO> list = annualTrainingPlanService.getDataSetInstMngList(searchVo);

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

    @Operation(summary = "[과목/과정 관리 팝업 데이터 저장 및 수정]", description = "과목/과정 관리 팝업 데이터 저장 및 수정한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/saveInstMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveInstMngList(
            @RequestBody List<TrainningInstructorVO> voList)
            throws Exception {
        List<TrainningInstructorVO> rtnDto = annualTrainingPlanService.saveInstMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[과목/과정 관리 팝업 데이터 삭제(사용안함)]", description = "과목/과정 관리 팝업 데이터 삭제(사용안함)한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/deleteInstMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteInstMngList(
            @RequestBody List<TrainningInstructorVO> voList)
            throws Exception {
        List<TrainningInstructorVO> rtnDto = annualTrainingPlanService.deleteInstMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 연간교육 계획 조회]", description = "안전보건 연간교육 계획을 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getAnnualTrainingPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getAnnualTrainingPlan(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<AnnualTrainingPlanVO> list = annualTrainingPlanService.getAnnualTrainingPlan(searchVo);

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

    @Operation(summary = "[안전보건 연간교육 계획 저장]", description = "안전보건 연간교육 계획을 저장한다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/saveAnnualTrainingPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveAnnualTrainingPlan(
            @RequestBody List<AnnualTrainingPlanVO> voList)
            throws Exception {
        BaseVO rtnDto = annualTrainingPlanService.saveAnnualTrainingPlan(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 연간교육 계획 삭제]", description = "안전보건 연간교육 계획을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/annualTrainingPlan/deleteAnnualTrainingPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAnnualTrainingPlan(@RequestBody AnnualTrainingPlanVO reqVo) throws Exception {
        BaseVO rtnDto = annualTrainingPlanService.deleteAnnualTrainingPlan(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 연간교육 계획 일괄 삭제]", description = "안전보건 연간교육 계획 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/annualTrainingPlan/deleteAnnualTrainingPlans")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteAnnualTrainingPlans(@RequestBody List<AnnualTrainingPlanVO> list) throws Exception {
        annualTrainingPlanService.deleteAnnualTrainingPlans(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[교육 강사 조회]", description = "교육 강사를 조회한다")
    @GetMapping(value = "/safewizpro/support/getTrainingInstructor")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingInstructor(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingInstructorVO> list = annualTrainingPlanService.getTrainingInstructor(searchVo);

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

    @Operation(summary = "[교육 강사 저장]", description = "교육 강사을 저장한다.")
    @PostMapping(value = "/safewizpro/support/saveTrainingInstructor")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTrainingInstructor(
            @RequestBody List<TrainingInstructorVO> voList)
            throws Exception {
        BaseVO rtnDto = annualTrainingPlanService.saveTrainingInstructor(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[교육 강사 일괄 삭제]", description = "교육 강사 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/deleteTrainingInstructors")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingInstructors(@RequestBody List<TrainingInstructorVO> list) throws Exception {
        annualTrainingPlanService.deleteTrainingInstructors(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전보건 연간교육 계획서 레포트 ]", description = "안전보건 연간교육 계획서 레포트를 제작한다.")
    @PostMapping(value = "/safewizpro/support/getAnnualReport")
    public void getAnnualReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
    	annualTrainingPlanService.getAnnualReport(request, response, spSearchVO);
    }
    
    //전년도 불러오기
    @Operation(summary = "[전년도 및 현년도 안전보건 연간교육 조회]", description = "전년도 및 현년도의 안전보건 연간교육을 조회한다")
    @GetMapping(value = "/safewizpro/support/annualTrainingPlan/getCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCurrentAndPreviousYear(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.setSearchText(searchVo.getWriteYear() + "," + SpUtils.addNumToString(searchVo.getWriteYear(),-1));
        searchVo.initialize();
        List<AnnualTrainingPlanVO> list = annualTrainingPlanService.getCurrentAndPreviousYear(searchVo);

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

    @Operation(summary = "[전년도 불러오기 저장]", description = "전년도 불러오기의 저장 기능입니다.")
    @PostMapping(value = "/safewizpro/support/annualTrainingPlan/saveCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCurrentAndPreviousYear(
            @RequestBody List<AnnualTrainingPlanVO> voList)
            throws Exception {
        BaseVO rtnDto = annualTrainingPlanService.saveCurrentAndPreviousYear(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

}
