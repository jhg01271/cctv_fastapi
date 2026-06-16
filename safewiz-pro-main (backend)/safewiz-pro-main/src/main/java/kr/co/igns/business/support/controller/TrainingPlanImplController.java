package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.TrainingPlanImplVO;
import kr.co.igns.business.support.model.TrainingLocationVO;
import kr.co.igns.business.support.service.TrainingPlanImplService;
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
 * ※ 파일명 : Safety & Health Training Impl. Plan
 * ※ 기능명 : 안전보건 교육 실시 계획
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전보건 교육 실시 계획")
@RestController
@RequiredArgsConstructor
public class TrainingPlanImplController {
    private final MessageService messageService;
    private final TrainingPlanImplService trainingPlanImplService;

    @Operation(summary = "[교육장소 관리 팝업 데이터 조회]", description = "교육장소 관리 팝업 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingPlanImpl/getLocMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLocMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingLocationVO> list = trainingPlanImplService.getLocMngList(searchVo);

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

    @Operation(summary = "[교육장소 관리 팝업 예시 데이터 조회]", description = "교육장소 관리 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingPlanImpl/getDataSetLocMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDataSetLocMngList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingLocationVO> list = trainingPlanImplService.getDataSetLocMngList(searchVo);

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

    @Operation(summary = "[교육장소 관리 팝업 데이터 저장 및 수정]", description = "교육장소 관리 팝업 데이터 저장 및 수정한다.")
    @PostMapping(value = "/safewizpro/support/trainingPlanImpl/saveLocMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveLocMngList(
            @RequestBody List<TrainingLocationVO> voList)
            throws Exception {
        List<TrainingLocationVO> rtnDto = trainingPlanImplService.saveLocMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[교육장소 관리 팝업 데이터 삭제(사용안함)]", description = "교육장소 관리 팝업 데이터 삭제(사용안함)한다.")
    @PostMapping(value = "/safewizpro/support/trainingPlanImpl/deleteLocMngList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLocMngList(
            @RequestBody List<TrainingLocationVO> voList)
            throws Exception {
        List<TrainingLocationVO> rtnDto = trainingPlanImplService.deleteLocMngList(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 실시 계획 조회]", description = "안전보건 교육 실시 계획을 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingPlanImpl/getTrainingPlanImpl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingPlanImpl(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingPlanImplVO> list = trainingPlanImplService.getTrainingPlanImpl(searchVo);

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

    @Operation(summary = "[안전보건 교육 실시 계획 상세 조회]", description = "안전보건 교육 실시 계획 상세 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingPlanImpl/getTrainingPlanImplDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingPlanImplDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        TrainingPlanImplVO list = trainingPlanImplService.getTrainingPlanImplDetail(searchVo);

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

    @Operation(summary = "[안전보건 교육 실시 계획 저장]", description = "안전보건 교육 실시 계획을 저장한다.")
    @PostMapping(value = "/safewizpro/support/trainingPlanImpl/saveTrainingPlanImpl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTrainingPlanImpl(
            @RequestBody List<TrainingPlanImplVO> voList)
            throws Exception {
        BaseVO rtnDto = trainingPlanImplService.saveTrainingPlanImpl(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 실시 계획 삭제]", description = "안전보건 교육 실시 계획을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/trainingPlanImpl/deleteTrainingPlanImpl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingPlanImpl(@RequestBody TrainingPlanImplVO reqVo) throws Exception {
        BaseVO rtnDto = trainingPlanImplService.deleteTrainingPlanImpl(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 실시 계획 일괄 삭제]", description = "안전보건 교육 실시 계획 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/trainingPlanImpl/deleteTrainingPlanImpls")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingPlanImpls(@RequestBody List<TrainingPlanImplVO> list) throws Exception {
        trainingPlanImplService.deleteTrainingPlanImpls(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[안전보건 교육실시 계획계획서 레포트 ]", description = "안전보건 교육실시 계획계획서 레포트를 제작한다.")
    @PostMapping(value = "/safewizpro/support/trainingPlanImpl/getTrainingPlanImplReport")
    public void getTrainingPlanImplReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
    	trainingPlanImplService.getTrainingPlanImplReport(request, response, spSearchVO);
    }


    //전년도 불러오기
    @Operation(summary = "[전년도 및 현년도 안전보건 교육 실시 계획 조회]", description = "전년도 및 현년도의 안전보건 교육 실시 계획을 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingPlanImpl/getCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCurrentAndPreviousYear(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.setSearchText(searchVo.getWriteYear() + "," + SpUtils.addNumToString(searchVo.getWriteYear(),-1));
        searchVo.initialize();
        List<TrainingPlanImplVO> list = trainingPlanImplService.getCurrentAndPreviousYear(searchVo);

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
    @PostMapping(value = "/safewizpro/support/trainingPlanImpl/saveCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCurrentAndPreviousYear(
            @RequestBody List<TrainingPlanImplVO> voList)
            throws Exception {
        BaseVO rtnDto = trainingPlanImplService.saveCurrentAndPreviousYear(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

}
