package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.business.support.service.EducationTrainingService;
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
 * ※ 패키지 : kr.co.igns.business.support.controller
 * ※ 파일명 : EducationTraining
 * ※ 기능명 : 교육훈련
 * ※ 작성자 : 김연주
 * ※ 최초생성일 : 2024. 10. 22.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "HseObjectives", description = "Hse 목표")
@RestController
@RequiredArgsConstructor
public class EducationTrainingController {
    private final MessageService messageService;
    private final EducationTrainingService educationTrainingService;

    @Operation(summary = "[교육 훈련 조회]", description = "교육 훈련 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/support/getEduTraining")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEduTraining(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EducationTrainingVO> list = educationTrainingService.getEduTraining(spSearchVO);

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
