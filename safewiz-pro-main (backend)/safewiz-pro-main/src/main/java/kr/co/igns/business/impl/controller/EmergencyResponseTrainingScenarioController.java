package kr.co.igns.business.impl.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyControlTaskAsgmtVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingScenarioVO;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.impl.service.EmergencyResponseService;
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
 * ※ 파일명 : EmergencyResponseTrainingScenario
 * ※ 기능명 : 비상대응 훈련 시나리오
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "EmergencyResponseTrainingScenario", description = "비상대응 훈련 시나리오")
@RestController
@RequiredArgsConstructor
public class EmergencyResponseTrainingScenarioController {
    private final MessageService messageService;
    private final EmergencyResponseTrainingScenarioService emergencyResponseTrainingScenarioService;

    @Operation(summary = "[비상 대응 훈련 조회]", description = "비상 대응 훈련 목록을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/scenario/getTrainingList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyResponseTrainingVO> list = emergencyResponseTrainingScenarioService.getTrainingList(spSearchVO);

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

    @Operation(summary = "[비상 대응 훈련 상세 조회]", description = "비상 대응 훈련 내용 및 시나리오 항목을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/scenario/getTrainingDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        EmergencyResponseTrainingVO list = emergencyResponseTrainingScenarioService.getTrainingDetail(spSearchVO);

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

    @Operation(summary = "[비상 대응 훈련 내용 및 시나리오 저장]", description = "비상 대응 훈련 내용 및 시나리오를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/scenario/saveTraining")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveTraining(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") EmergencyResponseTrainingVO vo)
            throws Exception {
        BaseVO rtnDto = emergencyResponseTrainingScenarioService.saveTraining(files,vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상 대응 훈련 내용 및 시나리오 삭제]", description = "비상 대응 훈련 내용 및 시나리오를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/scenario/deleteTraining")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTraining(@RequestBody List<EmergencyResponseTrainingVO> list) throws Exception {
        BaseVO rtnDto = emergencyResponseTrainingScenarioService.deleteTraining(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[비상 대응 훈련 내용 및 시나리오 출력]", description = "비상 대응 훈련 내용 및 시나리오 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/scenario/getTrainingReport")
    public void getTrainingReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            emergencyResponseTrainingScenarioService.getTrainingReport(request, response, spSearchVO);
    }
}
