package kr.co.igns.business.participation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.HseObjectivesVO;
import kr.co.igns.business.participation.service.HseObjectivesService;
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
 * ※ 패키지 : kr.co.igns.business.participation.controller
 * ※ 파일명 : HseObjectives
 * ※ 기능명 : Hse 목표
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "HseObjectives", description = "Hse 목표")
@RestController
@RequiredArgsConstructor
public class HseObjectivesController {
    private final MessageService messageService;
    private final HseObjectivesService hseObjectivesService;

    @Operation(summary = "[Hse 목표 조회]", description = "Hse 목표 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/getHseObjectives")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseObjectives(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<HseObjectivesVO> list = hseObjectivesService.getHseObjectives(spSearchVO);

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

    @Operation(summary = "[Hse 조직도]", description = "Hse 조직도 카드 메뉴를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/getHseOrganizationChart")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHseOrganizationChart(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<HseObjectivesVO> list = hseObjectivesService.getHseOrganizationChart(spSearchVO);

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
