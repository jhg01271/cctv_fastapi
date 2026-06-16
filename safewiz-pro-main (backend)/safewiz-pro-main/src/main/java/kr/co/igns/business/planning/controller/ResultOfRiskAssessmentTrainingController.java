package kr.co.igns.business.planning.controller;


import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ResultOfRiskAssessmentTrainingVO;
import kr.co.igns.business.planning.service.ResultOfRiskAssessmentTrainingService;
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
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : ResultOfRiskAssessmentTraining
 * ※ 기능명 : 위험성평가 교육/참여 결과
 * ※ 작성자 : 장석천
 * ※ 최초생성일 : 2024. 10. 28.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ResultOfRiskAssessmentTraining", description = "위험성평가 교육/참여 결과")
@RestController
@RequiredArgsConstructor
public class ResultOfRiskAssessmentTrainingController {

    private final ResultOfRiskAssessmentTrainingService resultOfRiskAssessmentTrainingService;
    private final MessageService messageService;

    @Operation(summary = "[위험성평가 교육/참여 결과 목록조회]")
    @GetMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTraining")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getResultOfRiskAssessmentTraining(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        
        List<ResultOfRiskAssessmentTrainingVO> resultList = resultOfRiskAssessmentTrainingService.getResultOfRiskAssessmentTraining(searchVo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list", resultList);


        if(resultList == null || resultList.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        }else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 교육/참여 결과 추가]")
    @PostMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/insertResultOfRiskAssessmentTraining")
    public ResponseEntity<SingleResponseDto<BaseVO>> insertResultOfRiskAssessmentTraining(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") ResultOfRiskAssessmentTrainingVO reqVO) throws Exception {

        BaseVO rtnDto = resultOfRiskAssessmentTrainingService.insertResultOfRiskAssessmentTraining(files, reqVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), rtnDto);
    }

    @Operation(summary = "[위험성평가 교육/참여 결과 상세조회]")
    @GetMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTrainingDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getResultOfRiskAssessmentTrainingDetail(
            @ModelAttribute @Valid SpSearchVO vo) throws Exception {
        ResultOfRiskAssessmentTrainingVO result = new ResultOfRiskAssessmentTrainingVO();
        result = resultOfRiskAssessmentTrainingService.getResultOfRiskAssessmentTrainingDetail(vo);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", result);

        if (result == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), hashMap);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), hashMap);
        }
    }

    @Operation(summary = "[위험성평가 교육/참여 결과 수정]")
    @PostMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/updateResultOfRiskAssessmentTraining")
    public ResponseEntity<SingleResponseDto<BaseVO>> updateResultOfRiskAssessmentTraining(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") ResultOfRiskAssessmentTrainingVO reqVO) throws Exception {

        BaseVO rtnDto = resultOfRiskAssessmentTrainingService.updateResultOfRiskAssessmentTraining(files, reqVO);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), rtnDto);
    }

    @Operation(summary = "[위험성평가 교육/참여 결과 삭제")
    @DeleteMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/deleteResultOfRiskAssessmentTraining")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteResultOfRiskAssessmentTraining(
            @RequestBody @Valid List<ResultOfRiskAssessmentTrainingVO> voList) throws Exception {

        resultOfRiskAssessmentTrainingService.deleteResultOfRiskAssessmentTraining(voList);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("result", null);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), hashMap);
    }

    @Operation(summary = "[위험성평가 교육/참여 결과 출력]")
    @PostMapping(value = "/safewizpro/planning/resultOfRiskAssessmentTraining/getResultOfRiskAssessmentTrainingReport")
    public void getResultOfRiskAssessmentTrainingReport(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody @Valid SpSearchVO spSearchVO) throws Exception {
        resultOfRiskAssessmentTrainingService.getResultOfRiskAssessmentTrainingReport(request, response, spSearchVO);
    }




}
