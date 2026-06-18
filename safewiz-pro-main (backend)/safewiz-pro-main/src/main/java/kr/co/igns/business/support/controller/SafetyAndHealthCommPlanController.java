package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.SafetyAndHealthCommPlanVO;
import kr.co.igns.business.support.service.JobCompetencyAssessmentService;
import kr.co.igns.business.support.service.SafetyAndHealthCommPlanService;
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
 * ※ 파일명 : SafetyAndHealthCommPlan
 * ※ 기능명 : 안전보건 의사소통 계획
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 29.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전보건 의사소통 계획")
@RestController
@RequiredArgsConstructor
public class SafetyAndHealthCommPlanController {
    private final MessageService messageService;
    private final SafetyAndHealthCommPlanService safetyAndHealthCommPlanService;

    @Operation(summary = "[안전보건 의사소통 계획 조회]", description = "안전보건 의사소통 계획 를 조회한다")
    @GetMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getShCommPlanList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthCommPlanVO> list = safetyAndHealthCommPlanService.getShCommPlanList(searchVo);

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

    @Operation(summary = "[안전보건 의사소통 계획 저장]", description = "안전보건 의사소통 계획을 저장한다")
    @PostMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/saveShCommPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveShCommPlan(@RequestBody SafetyAndHealthCommPlanVO vo) throws Exception {

        BaseVO rtnDto = safetyAndHealthCommPlanService.saveShCommPlan(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 의사소통 계획 삭제]", description = "안전보건 의사소통 계획을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/deleteShCommPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteShCommPlan(@RequestBody List<SafetyAndHealthCommPlanVO> list) throws Exception {
        BaseVO rtnDto = safetyAndHealthCommPlanService.deleteShCommPlan(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 의사소통 계획 상세 삭제]", description = "안전보건 의사소통 계획 상세 데이터를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/deleteShCommPlanDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteShCommPlanDetail(@RequestBody SafetyAndHealthCommPlanVO vo) throws Exception {
        BaseVO rtnDto = safetyAndHealthCommPlanService.deleteShCommPlanDetail(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[안전보건 의사소통 계획 레포트 출력]", description = "안전보건 의사소통 계획 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanReport")
    public void getShCommPlanReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            safetyAndHealthCommPlanService.getShCommPlanReport(request, response, spSearchVO);
    }

    @Operation(summary = "[안전보건 의사소통 계획 상세 레포트 출력]", description = "안전보건 의사소통 계획 상세 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/support/safetyAndHealthCommPlan/getShCommPlanDetailReport")
    public void getShCommPlanDetailReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SafetyAndHealthCommPlanVO vo)
            throws Exception {
            safetyAndHealthCommPlanService.getShCommPlanDetailReport(request, response, vo);
    }

}
