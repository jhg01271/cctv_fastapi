package kr.co.igns.business.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.*;
import kr.co.igns.business.evaluation.service.MonitoringManageService;
import kr.co.igns.business.participation.model.HsePolicyVO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Monitoring Manage
 * ※ 기능명 : 모니터링 및 측정 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 18.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "모니터링 및 측정 관리")
@RestController
@RequiredArgsConstructor
public class MonitoringManageController {
    private final MessageService messageService;
    private final MonitoringManageService monitoringManageService;

    //모니터링 및 측정 관리 마스터
    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 조회]", description = "모니터링, 성과측정 및 평가 결과서를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationReportList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationReportList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationReportVO> list = monitoringManageService.getEvaluationReportList(searchVo);

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

    //모니터링, 성과측정 및 평가 결과서
    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 상세 조회]", description = "모니터링, 성과측정 및 평가 결과서 상세를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationReportDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationReportDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        EvaluationReportVO list = monitoringManageService.getEvaluationReportDetail(searchVo);

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

    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 저장]", description = "모니터링, 성과측정 및 평가 결과서를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveEvaluationReport")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationReport(
            @RequestBody EvaluationReportVO voList)
            throws Exception {
        BaseVO rtnDto = monitoringManageService.saveEvaluationReport(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 삭제]", description = "모니터링, 성과측정 및 평가 결과서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteEvaluationReportList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEvaluationReportList(@RequestBody List<EvaluationReportVO> list) throws Exception {
        monitoringManageService.deleteEvaluationReportList(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 상세 삭제]", description = "모니터링, 성과측정 및 평가 결과서 상세 데이터를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteEvaluationReportListDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEvaluationReportListDetail(@RequestBody EvaluationReportVO reqVo) throws Exception {
        monitoringManageService.deleteEvaluationReportListDetail(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 및 평가 결과서 보고서]", description = "모니터링, 성과측정 및 평가 결과서를 제작한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationReport")
    public void getEvaluationReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        monitoringManageService.getEvaluationReport(request, response, spSearchVO);
    }


    //평가항목 관리
    @Operation(summary = "[평가항목 조회]", description = "평가항목을 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getEvaluationChecklist(searchVo);

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

    //평가항목 예시 불러오기 데이터 관리
    @Operation(summary = "[평가항목 팝업 예시 데이터 조회]", description = "평가항목 팝업 예시 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDataSet")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationChecklistDataSet(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getEvaluationChecklistDataSet(searchVo);

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

    @Operation(summary = "[평가항목 조회(use_yn = y)]", description = "평가항목을 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationChecklistByUseYn")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationChecklistByUseYn(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getEvaluationChecklistByUseYn(searchVo);

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

    @Operation(summary = "[평가사항 조회]", description = "평가사항을 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationChecklistDetail(@ModelAttribute @Valid EvaluationChecklistVO vo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getEvaluationChecklistDetail(vo);

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

    @Operation(summary = "[평가사항 조회(팝업)]", description = "평가사항(팝업)을 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationChecklistDetailBySearchText")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEvaluationChecklistDetailBySearchText(@ModelAttribute @Valid SpSearchVO vo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getEvaluationChecklistDetailBySearchText(vo);

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

    @Operation(summary = "[평가항목 관리 저장]", description = "평가항목을 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveEvaluationChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationChecklist(
            @RequestBody EvaluationChecklistVO voList)
            throws Exception {
        BaseVO rtnDto = monitoringManageService.saveEvaluationChecklist(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[평가항목 관리 저장(팝업)]", description = "평가항목(팝업)을 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveEvaluationChecklistByPopup")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationChecklistByPopup(
            @RequestBody List<EvaluationChecklistVO> voList)
            throws Exception {
        BaseVO rtnDto = monitoringManageService.saveEvaluationChecklistByPopup(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[평가항목 삭제]", description = "평가항목을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteEvaluationChecklistDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEvaluationChecklistDetail(@RequestBody List<EvaluationChecklistVO> list) throws Exception {
        monitoringManageService.deleteEvaluationChecklistDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[평가항목 삭제(팝업)]", description = "평가항목(팝업)을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteEvaluationChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEvaluationChecklist(@RequestBody List<EvaluationChecklistVO> list) throws Exception {
        monitoringManageService.deleteEvaluationChecklist(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[평가항목 관리 - 예시불러오기]", description = "평가항목 예시를 불러온다.")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getBaseEvaluationChecklist")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getBaseEvaluationChecklist(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationChecklistVO> list = monitoringManageService.getBaseEvaluationChecklist(searchVo);

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

    //모니터링, 성과측정 - 준수평가표
    @Operation(summary = "[모니터링, 성과측정 준수평가표 조회]", description = "모니터링, 성과측정 준수평가표를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getLegalComplianceEvaluationList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>>getLegalComplianceEvaluationList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<LegalComplianceEvaluationVO> list = monitoringManageService.getLegalComplianceEvaluationList(searchVo);

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

    @Operation(summary = "[모니터링, 성과측정 준수평가표 상세 조회]", description = "모니터링, 성과측정 준수평가표 상세를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getLegalComplianceEvaluationDetailList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>>getLegalComplianceEvaluationDetailList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<LegalComplianceEvaluationDetailVO> list = monitoringManageService.getLegalComplianceEvaluationDetailList(searchVo);

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

    @Operation(summary = "[모니터링, 성과측정 준수평가표 저장]", description = "모니터링, 성과측정 준수평가표를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveLegalComplianceEvaluation")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveLegalComplianceEvaluation(@RequestBody LegalComplianceEvaluationVO vo)
            throws Exception {
        BaseVO rtnDto = monitoringManageService.saveLegalComplianceEvaluation(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 준수평가표 상세 저장]", description = "모니터링, 성과측정 준수평가표를 상세 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveLegalComplianceEvaluationDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveLegalComplianceEvaluationDetail(
            @RequestPart(value = "files", required = false)  List<MultipartFile> files,
            @RequestPart(value = "fileKeys", required = false) List<String> fileKeys,
            @RequestPart(value = "info") List<LegalComplianceEvaluationDetailVO> voList)
            throws Exception {

        // 파일 키에 따라 List<MultipartFile>을 Map으로 수동 매핑
        Map<String, List<MultipartFile>> filesMap = new HashMap<>();
        if(files != null) {
            for (int i = 0; i < files.size(); i++) {
                String key = fileKeys.get(i);
                filesMap.computeIfAbsent(key, k -> new ArrayList<>()).add(files.get(i));
            }
        }

        BaseVO rtnDto = monitoringManageService.saveLegalComplianceEvaluationDetail(filesMap, voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 준수평가표 삭제]", description = "모니터링, 성과측정 준수평가표를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteLegalComplianceEvaluation")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalComplianceEvaluation(@RequestBody List<LegalComplianceEvaluationVO> list) throws Exception {
        monitoringManageService.deleteLegalComplianceEvaluation(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[모니터링, 성과측정 준수평가표 상세 삭제]", description = "모니터링, 성과측정 준수평가표를 상세 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/evaluation/monitoringManage/deleteLegalComplianceEvaluationDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalComplianceEvaluationDetail(@RequestBody List<LegalComplianceEvaluationDetailVO> list) throws Exception {
        monitoringManageService.deleteLegalComplianceEvaluationDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[준수평가표 보고서]", description = "준수평가표를 제작한다.")
    @PostMapping(value = "/safewizpro/evaluation/evaluationReport/getEvaluationComplianceReport")
    public void getEvaluationComplianceReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        monitoringManageService.getEvaluationComplianceReport(request, response, spSearchVO);
    }

    //모니터링, 성과측정 - 성과평가표
    @Operation(summary = "[모니터링, 성과측정 성과평가표 조회]", description = "모니터링, 성과측정 성과평가표를 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getEvaluationReportPerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>>getEvaluationReportPerformance(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EvaluationReportDetailVO> list = monitoringManageService.getEvaluationReportPerformance(searchVo);

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

    @Operation(summary = "[모니터링, 성과측정 성과평가표 저장]", description = "모니터링, 성과측정 성과평가표를 저장한다.")
    @PostMapping(value = "/safewizpro/evaluation/monitoringManage/saveEvaluationReportPerformance")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEvaluationReportPerformance(
            @RequestBody List<EvaluationReportDetailVO> voList)
            throws Exception {
        BaseVO rtnDto = monitoringManageService.saveEvaluationReportPerformance(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }


    @Operation(summary = "[성과평가표 보고서]", description = "성과평가표를 제작한다.")
    @PostMapping(value = "/safewizpro/evaluation/evaluationReport/getEvaluationPerformanceReport")
    public void getEvaluationPerformanceReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        monitoringManageService.getEvaluationPerformanceReport(request, response, spSearchVO);
    }

    //정보 확인 버튼 이벤트
    @Operation(summary = "[정보 확인 버튼 클릭]", description = "특정 데이터의 키 값을 조회한다")
    @GetMapping(value = "/safewizpro/evaluation/monitoringManage/getMenuKeyInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMenuKeyInfo(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        SpSearchVO list = monitoringManageService.getMenuKeyInfo(searchVo);

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
}
