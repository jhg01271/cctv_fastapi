package kr.co.igns.business.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.evaluation.model.InternalAuditVO;
import kr.co.igns.business.evaluation.service.InternalAuditService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.evaludation.controller
 * ※ 파일명 : InternalAudit
 * ※ 기능명 : 내부 심사
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 21.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "InternalAudit", description = "내부 심사")
@RestController
@RequiredArgsConstructor
public class InternalAuditController {
    private final MessageService messageService;
    private final InternalAuditService internalAuditService;

    @Operation(summary = "[내부 심사]", description = "내부심사 메뉴를 조회한다.")
    @GetMapping(value = "/safewizpro/evaluation/getInternalAudit")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getInternalAudit(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<InternalAuditVO> list = internalAuditService.getInternalAudit(spSearchVO);

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
