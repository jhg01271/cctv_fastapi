package kr.co.igns.business.participation.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.ConsultationAndParticipationVO;
import kr.co.igns.business.participation.service.ConsultationAndParticipationService;
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
 * ※ 패키지 : kr.co.igns.business.participation.controller
 * ※ 파일명 : ConsultationAndParticipation
 * ※ 기능명 : 협의 및 목표
 * ※ 작성자 : 김연주
 * ※ 최초생성일 : 2025. 01. 15.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ConsultationAndParticipation", description = "협의 및 참여")
@RestController
@RequiredArgsConstructor
public class ConsultationAndParticipationController {
    private final MessageService messageService;
    private final ConsultationAndParticipationService consultationAndParticipationService;

    @Operation(summary = "[협의 및 참여 조회]", description = "협의 및 참여 카드 메뉴를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/getConsultationAndParticipation")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getConsultationAndParticipation(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ConsultationAndParticipationVO> list = consultationAndParticipationService.getConsultationAndParticipation(spSearchVO);

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
