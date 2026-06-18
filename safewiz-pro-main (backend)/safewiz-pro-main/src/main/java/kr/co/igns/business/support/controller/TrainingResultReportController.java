package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.AnnualTrainingPlanVO;
import kr.co.igns.business.support.model.TrainingPlanImplVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.business.support.service.TrainingResultReportService;
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
 * ※ 파일명 : Safety & Health Training Result Report
 * ※ 기능명 : 안전보건 교육 실시 계획
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전보건 교육 결과 보고서")
@RestController
@RequiredArgsConstructor
public class TrainingResultReportController {
    private final MessageService messageService;
    private final TrainingResultReportService trainingResultReportService;

    @Operation(summary = "[안전보건 교육 결과 보고서조회]", description = "안전보건 교육 결과 보고서를 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingResultReport/getTrainingResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingResult(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<TrainingResultReportVO> list = trainingResultReportService.getTrainingResult(searchVo);

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

    @Operation(summary = "[안전보건 교육 결과 보고서 상세 조회]", description = "안전보건 교육 결과 보고서 상세 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingResultReport/getTrainingResultReportDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingResultReportDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        TrainingResultReportVO list = trainingResultReportService.getTrainingResultDetail(searchVo);

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

    @Operation(summary = "[안전보건 교육 결과 보고서 저장]", description = "안전보건 교육 결과 보고서를 저장한다.")
    @PostMapping(value = "/safewizpro/support/trainingResultReport/saveTrainingResultReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTrainingResultReport(
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") List<TrainingResultReportVO> voList)
            throws Exception {
        BaseVO rtnDto = trainingResultReportService.saveTrainingResultReport(files, voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 결과 보고서 삭제]", description = "안전보건 교육 결과 보고를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/trainingResultReport/deleteTrainingResultReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingResultReport(@RequestBody TrainingResultReportVO reqVo) throws Exception {
        BaseVO rtnDto = trainingResultReportService.deleteTrainingResultReport(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 결과 보고서 일괄 삭제]", description = "안전보건 교육 결과 보고서 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/trainingResultReport/deleteTrainingResultReports")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTrainingResultReports(@RequestBody List<TrainingResultReportVO> list) throws Exception {
        trainingResultReportService.deleteTrainingResultReports(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건 교육 결과 보고서 레포르]", description = "안전보건 교육 결과 보고서 레포트를 제작한다.")
    @PostMapping(value = "/safewizpro/support/trainingResultReport/getTrainingResultReport")
    public void getTrainingResultReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        trainingResultReportService.getTrainingResultReport(request, response, spSearchVO);
    }

    //전년도 불러오기
    @Operation(summary = "[전년도 및 현년도안전보건 교육 결과 조회]", description = "전년도 및 현년도의 안전보건 교육 결과를 조회한다")
    @GetMapping(value = "/safewizpro/support/trainingResultReport/getCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCurrentAndPreviousYear(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.setSearchText(searchVo.getWriteYear() + "," + SpUtils.addNumToString(searchVo.getWriteYear(),-1));
        searchVo.initialize();
        List<TrainingResultReportVO> list = trainingResultReportService.getCurrentAndPreviousYear(searchVo);

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
    @PostMapping(value = "/safewizpro/support/trainingResultReport/saveCurrentAndPreviousYear")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCurrentAndPreviousYear(
            @RequestBody List<TrainingResultReportVO> voList)
            throws Exception {
        BaseVO rtnDto = trainingResultReportService.saveCurrentAndPreviousYear(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

}
