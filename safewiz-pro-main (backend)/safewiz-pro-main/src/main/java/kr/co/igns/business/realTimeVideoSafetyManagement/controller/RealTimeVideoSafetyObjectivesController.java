package kr.co.igns.business.realTimeVideoSafetyManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.co.igns.business.realTimeVideoSafetyManagement.model.RealTimeVideoSafetyObjectivesVO;
import kr.co.igns.business.realTimeVideoSafetyManagement.service.RealTimeVideoSafetyObjectivesService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.realTimeVideoSafetyManagement.controller
 * ※ 파일명 : realTimeVideoSafetyObjectives
 * ※ 기능명 : 실시간 영상 안전관리
 * ※ 작성자 : 주우경
 * ※ 최초생성일 : 2026. 01. 26.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RealTimeVideoSafetyObjectives", description = "실시간 영상 안전관리")
@RestController
@RequiredArgsConstructor
public class RealTimeVideoSafetyObjectivesController {
    private final MessageService messageService;
    private final RealTimeVideoSafetyObjectivesService realTimeVideoSafetyObjectivesService;

    @Operation(summary = "[실시간 영상 안전관리 메뉴 조회]", description = "실시간 영상 안전관리 메뉴를 조회한다.")
    @GetMapping(value = "/safewizpro/realTimeVideoSafetyManagement/getRealTimeVideoSafetyObjectives")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRealTimeVideoSafetyObjectives(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<RealTimeVideoSafetyObjectivesVO> list = realTimeVideoSafetyObjectivesService.getRealTimeVideoSafetyObjectives(spSearchVO);

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

}
