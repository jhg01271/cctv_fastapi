package kr.co.igns.business.impl.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingResultVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.impl.service.EmergencyResponseService;
import kr.co.igns.business.impl.service.EmergencyResponseTrainingResultService;
import kr.co.igns.business.impl.service.EmergencyResponseTrainingScenarioService;
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
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : EmergencyResponseTrainingResult
 * ※ 기능명 : 비상대응 훈련 결과 보고서
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "EmergencyResponseTrainingResult", description = "비상대응 훈련 결과 보고서")
@RestController
@RequiredArgsConstructor
public class EmergencyResponseTrainingResultController {
    private final MessageService messageService;
    private final EmergencyResponseTrainingResultService emergencyResponseTrainingResultService;

    @Operation(summary = "[비상 대응 훈련 실시 보고서 조회]", description = "비상 대응 훈련 실시 보고서 목록을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/result/getResultList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getResultList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyResponseTrainingVO> list = emergencyResponseTrainingResultService.getResultList(spSearchVO);

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
    @Operation(summary = "[비상 대응 훈련 실시 보고서 조회]", description = "비상 대응 훈련 실시 보고서 목록을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/result/getResultMaster")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getResultMaster(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyResponseTrainingResultVO> list = emergencyResponseTrainingResultService.getResultMaster(spSearchVO);

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

    @Operation(summary = "[비상 대응 훈련 실시 보고서 상세 조회]", description = "비상 대응 훈련 실시 보고서 항목을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/result/getResultDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getResultDetail(
            @ModelAttribute @Valid EmergencyResponseTrainingVO vo) throws Exception {
        List<EmergencyResponseTrainingResultVO> list = emergencyResponseTrainingResultService.getResultDetail(vo);

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

    @Operation(summary = "[비상 대응 훈련 시나리오 목록 조회]", description = "비상 대응 훈련 시나리오 목록을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/result/getValidSenarioList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getValidSenarioList(
            @ModelAttribute @Valid EmergencyResponseTrainingVO vo) throws Exception {
        List<EmergencyResponseTrainingVO> list = emergencyResponseTrainingResultService.getValidSenarioList(vo);

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
    @Operation(summary = "[비상 대응 훈련 실시 보고서 저장]", description = "비상 대응 훈련 실시 보고서를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/result/saveResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveResult(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") EmergencyResponseTrainingResultVO vo)
            throws Exception {
        EmergencyResponseTrainingResultVO rtnDto = emergencyResponseTrainingResultService.saveResult(files, vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

    @Operation(summary = "[비상 대응 훈련 실시 보고서 삭제]", description = "비상 대응 훈련 실시 보고서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/result/deleteResult")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteResult(@RequestBody List<EmergencyResponseTrainingResultVO> list) throws Exception {
        BaseVO rtnDto = emergencyResponseTrainingResultService.deleteResult(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[비상 대응 훈련 실시 보고서 출력]", description = "비상 대응 훈련 실시 보고서 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/result/getResultReport")
    public void getResultReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            emergencyResponseTrainingResultService.getResultReport(request, response, spSearchVO);
    }
    @Operation(summary = "[비상 대응 훈련 실시 보고서 상세 출력]", description = "비상 대응 훈련 실시 보고서 상세 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/result/getResultDetailReport")
    public void getResultDetailReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            emergencyResponseTrainingResultService.getResultDetailReport(request, response, spSearchVO);
    }
}
