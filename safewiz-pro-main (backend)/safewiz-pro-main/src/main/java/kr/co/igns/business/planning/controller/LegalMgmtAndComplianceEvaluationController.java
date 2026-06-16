package kr.co.igns.business.planning.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.LegalMgmtAndComplianceEvaluationVO;
import kr.co.igns.business.planning.service.LegalMgmtAndComplianceEvaluationService;
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
 * ※ 패키지 : kr.co.igns.business.planning.controller
 * ※ 파일명 : LegalMgmtAndComplianceEvaluation
 * ※ 기능명 : 법규 관리 및 준수평가
 * ※ 작성자 : 김동건
 * ※ 최초생성일 : 2024. 10. 21.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "LegalMgmtAndComplianceEvaluation", description = "법규 관리 및 준수평가")
@RestController
@RequiredArgsConstructor
public class LegalMgmtAndComplianceEvaluationController {
    private final MessageService messageService;
    private final LegalMgmtAndComplianceEvaluationService legalMgmtAndComplianceEvaluationService;

    @Operation(summary = "[법규 관리 및 준수평가 조회]", description = "법규 관리 및 준수평가 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getLegalMgmtAndComplianceEvaluation")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalMgmtAndComplianceEvaluation(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<LegalMgmtAndComplianceEvaluationVO> list = legalMgmtAndComplianceEvaluationService.getLegalMgmtAndComplianceEvaluation(spSearchVO);

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
