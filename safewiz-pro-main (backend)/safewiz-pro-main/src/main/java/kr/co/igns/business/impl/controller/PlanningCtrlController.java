package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.PlanningCtrlVO;
import kr.co.igns.business.impl.service.PlanningCtrlService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : HseObjectives
 * ※ 기능명 : 운영 기획 및 관리
 * ※ 작성자 : 김연주
 * ※ 최초생성일 : 2024. 11. 01.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "PlanningAndControl", description = "운영 기획 및 관리")
@RestController
@RequiredArgsConstructor
public class PlanningCtrlController {
    private final MessageService messageService;
    private final PlanningCtrlService planningCtrlService;

    @Operation(summary = "[운영 기획 및 관리]", description = "운영 기획 및 관리 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/getPlanningCtrl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPlanningCtrl(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<PlanningCtrlVO> list = planningCtrlService.getPlanningCtrl(spSearchVO);

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
