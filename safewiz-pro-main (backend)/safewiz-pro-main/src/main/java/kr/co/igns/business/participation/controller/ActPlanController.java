package kr.co.igns.business.participation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.ActPlanDetailVO;
import kr.co.igns.business.participation.model.ActPlanPerformanceDetailVO;
import kr.co.igns.business.participation.model.ActPlanPerformanceVO;
import kr.co.igns.business.participation.model.ActPlanVO;
import kr.co.igns.business.participation.service.ActPlanService;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
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
 * ※ 패키지 : ;
 * ※ 파일명 : objective Action plan
 * ※ 기능명 : 전사 목표/추진 목표 및 분기별/월별 계획
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 14.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "전사 목표/추진 목표 및 분기별/월별 계획")
@RestController
@RequiredArgsConstructor
public class ActPlanController {
    private final MessageService messageService;
    private final ActPlanService actPlanService;

    //region objective Action plan master
    @Operation(summary = "[안전보건활동 목표/세부 추진 계획 문서 master 조회]", description = "안전보건활동 목표/세부 추진 계획 문서 master를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getActPlanMaster")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActPlanMaster(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> list = actPlanService.getActPlanMaster(searchVo);

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

    //endregion

    //region 전사 목표/추진 목표 및 분기별 계획
    @Operation(summary = "[전사 목표 및 중점 추진사항 조회]", description = "전사 목표 및 중점 추진사항을 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getActPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActPlan(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> list = actPlanService.getActPlan(searchVo);

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

    @Operation(summary = "[전사 목표 및 중점 추진사항 상세 조회]", description = "전사 목표 및 중점 추진사항 상세정보를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getActPlanDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActPlanDetail(@ModelAttribute SpSearchVO searchVo) throws Exception {
        ActPlanVO list = actPlanService.getPartnerDetail(searchVo);

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

    @Operation(summary = "[전사 목표 리스트 조회]", description = "전사 목표 리스트를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getObjective")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getObjective(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> list = actPlanService.getObjective(searchVo);

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

    @Operation(summary = "[추진 목표 리스트 조회]", description = "추진 목표 리스트를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getActionObjective")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionObjective(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanVO> list = actPlanService.getActionObjective(searchVo);

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

    @Operation(summary = "[추진 목표 등록 조직 리스트 조회]", description = "추진 목표 등록 조직 리스트를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getActionObjectiveOrgnList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionObjectiveOrgnList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanPerformanceVO> list = actPlanService.getActionObjectiveOrgnList(searchVo);

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

    @Operation(summary = "[안전보건목표 등록 조직 리스트 조회]", description = "안전보건목표 등록 조직 리스트를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlan/getSafetyHealthObjOrgnList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyHealthObjOrgnList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthObjectivesVO> list = actPlanService.getSafetyHealthObjOrgnList(searchVo);

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

//    @Operation(summary = "[전사 목표 및 중점 추진사항 등록]", description = "전사 목표 및 중점 추진사항을 등록한다.")
//    @PostMapping(value = "/safewizpro/actionPlan/insertActPlan")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertActPlan(@RequestBody @Valid ActPlanVO reqVo) throws Exception {
//        BaseVO rtnDto = actPlanService.insertActPlan(reqVo);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", rtnDto);
//        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
//    }
//
//    @Operation(summary = "[전사 목표 및 중점 추진사항 수정]", description = "전사 목표 및 중점 추진사항을 수정 한다.")
//    @PutMapping(value = "/safewizpro/actionPlan/{docSeq}")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateActPlan(@RequestBody @Valid ActPlanVO reqVO, @PathVariable String docSeq) throws Exception {
//        reqVO.setDocSeq(docSeq);
//        BaseVO rtnDto = actPlanService.updateActPlan(reqVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", rtnDto);
//        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
//    }

    @Operation(summary = "[전사 목표 및 중점 추진사항 저장]", description = "추진 계획(월별) 및 실적 저장한다.")
    @PostMapping(value = "/safewizpro/participation/actionPlan/saveActPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveActPlan(
            @RequestBody List<ActPlanVO> voList)
            throws Exception {
        BaseVO rtnDto = actPlanService.saveActPlan(voList);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[전사 목표 및 중점 추진사항 삭제]", description = "objective_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPlan/deleteActPlan")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteActPlan(@RequestBody ActPlanVO reqVo) throws Exception {
        BaseVO rtnDto = actPlanService.deleteActPlan(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[전사 목표 및 중점 추진사항 일괄 삭제]", description = "objective_id list에 해당되는 사용자를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPlan/deleteActPlans")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteActPlans(@RequestBody List<ActPlanVO> list) throws Exception {
        actPlanService.deleteActPlans(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
    //endregion

    //region 추진 계획 상세 - 월별
    @Operation(summary = "[추진 계획(월별) 및 실적 조회]", description = "추진 계획(월별) 및 실적을 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlanDetail/getActionMonthly")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionMonthly(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanDetailVO> list = actPlanService.getActionMonthly(searchVo);

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

    @Operation(summary = "[추진 계획(월별) 및 실적 상세 조회]", description = "전사 목표 및 중점 추진사항 상세정보를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlanDetail/{detailItemId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionMonthlyDetail(@ModelAttribute SpSearchVO searchVo, @PathVariable String detailItemId) throws Exception {
        searchVo.setSearchText(detailItemId);
        List<ActPlanDetailVO> list = actPlanService.getActionMonthlyDetail(searchVo);

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

    @Operation(summary = "[세부 항목 리스트 조회]", description = "세부 항목 리스트를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPlanDetail/getDetailItem")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDetailItem(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanDetailVO> list = actPlanService.getDetailItem(searchVo);

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
//
//    @Operation(summary = "[추진 계획(월별) 및 실적 등록]", description = "추진 계획(월별) 및 실적을 등록한다.")
//    @PostMapping(value = "/safewizpro/actionPlanDetail/insertActionMonthly")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertActionMonthly(@RequestBody @Valid ActPlanDetailVO reqVo) throws Exception {
//        BaseVO rtnDto = actPlanService.insertActionMonthly(reqVo);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", rtnDto);
//        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
//    }
//
//    @Operation(summary = "[추진 계획(월별) 및 실적 수정]", description = "추진 계획(월별) 및 실적을 수정 한다.")
//    @PutMapping(value = "/safewizpro/actionPlanDetail/{docDetailSeq}")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateActionMonthly(@RequestBody @Valid ActPlanDetailVO reqVO, @PathVariable String docDetailSeq) throws Exception {
//        reqVO.setDocDetailSeq(docDetailSeq);
//        BaseVO rtnDto = actPlanService.updateActionMonthly(reqVO);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", rtnDto);
//        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
//    }

    @Operation(summary = "[세부 항목 저장]", description = "세부항목을 저장한다.")
    @PostMapping(value = "/safewizpro/participation/actionPlanDetail/saveActionDetailItem")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveActionDetailItem(
            @RequestBody ActPlanDetailVO vo)
            throws Exception {
        BaseVO rtnDto = actPlanService.saveActionDetailItem(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[추진 계획(월별) 및 실적 저장]", description = "추진 계획(월별) 및 실적 저장한다.")
    @PostMapping(value = "/safewizpro/participation/actionPlanDetail/saveActionMonthly")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveActionMonthly(
            @RequestBody List<ActPlanDetailVO> voList)
            throws Exception {
        BaseVO rtnDto = actPlanService.saveActionMonthly(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[상세 항목 기준 추진 계획(월별) 및 실적 삭제]", description = "상세 항목 기준으로 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPlanDetail/deleteDetailItems")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteDetailItems(@RequestBody List<ActPlanDetailVO> list) throws Exception {
        actPlanService.deleteActionMonthlyByDetailItem(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[추진 계획(월별) 및 실적 일괄 삭제]", description = "action_id list에 해당되는 사용자를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPlanDetail/deleteActionMonthlys")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteActionMonthlys(@RequestBody List<ActPlanDetailVO> list) throws Exception {
        actPlanService.deleteActionMonthlys(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
    //endregion

    //region 추진 실적, 예산 (증빙자료)

    @Operation(summary = "[조직별 실적, 예산 조회]", description = "조직별 실적, 예산을 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPerformance/getActionPerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionPerformance(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ActPlanPerformanceVO> list = actPlanService.getActionPerformance(searchVo);

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

    @Operation(summary = "[조직별 실적, 예산 상세 조회]", description = "조직별 실적, 예산 상세정보를 조회한다")
    @GetMapping(value = "/safewizpro/participation/actionPerformance/getActionPerformanceDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getActionPerformanceDetail(@ModelAttribute SpSearchVO searchVo) throws Exception {
        ActPlanPerformanceVO list = actPlanService.getActionPerformanceDetail(searchVo);

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

    @Operation(summary = "[조직별 실적, 예산 저장]", description = "조직별 실적, 예산을 저장한다.")
    @PostMapping(value = "/safewizpro/participation/actionPerformance/saveActionPerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveActionPerformance(
            @RequestPart(value = "info")  ActPlanPerformanceVO voList,
            @RequestPart(value = "files", required = false) List<MultipartFile> files)
            throws Exception {
        BaseVO rtnDto = actPlanService.saveActionPerformance(voList,files);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[조직별 실적, 예산 삭제]", description = "조직별 실적, 예산 list을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPerformance/deleteActionPerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteActionPerformance(@RequestBody List<ActPlanPerformanceVO> list) throws Exception {
        actPlanService.deleteActionPerformance(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[조직별 실적 자료 삭제(use_yn='N')]", description = "조직별 실적 자료 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/actionPerformance/deleteActionPerformanceDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteActionPerformanceDetail(@RequestBody ActPlanPerformanceVO reqVo) throws Exception {
        actPlanService.deleteActionPerformanceDetail(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    //endregion
    //region 레포트
    @Operation(summary = "[전사 목표 및 중점 추진사항 레포트 출력]", description = "전사 목표 및 중점 추진사항 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/participation/actionPlan/getActionObjectiveReport")
    public void getActionObjectiveReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            actPlanService.getActionObjectiveReport(request, response, spSearchVO);
    }

    @Operation(summary = "[중점추진사항별 세부계획 ]", description = "중점추진사항별 세부계획 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/participation/actionPlan/getDetailedActionPlanReport")
    public void getDetailedActionPlanReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            actPlanService.getDetailedActionPlanReport(request, response, spSearchVO);
    }
    //endregion
    
    //region 레포트
    @Operation(summary = "[안전보건환경 예산 레포트 출력]", description = "안전보건환경 예산 레포트를 제작한다")
    @PostMapping(value = "/safewizpro/participation/actionPerformance/getHseBudgetReport")
    public void getHseBudgetReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            actPlanService.getHseBudgetReport(request, response, spSearchVO);
    }
    //endregion
}
