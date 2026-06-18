package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.EducationStatusVO;
import kr.co.igns.business.support.model.EducationTrainingVO;
import kr.co.igns.business.support.service.EducationStatusService;
import kr.co.igns.business.support.service.EducationTrainingService;
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

@Tag(name = "HseObjectives", description = "Hse 목표")
@RestController
@RequiredArgsConstructor
public class EducationStatusController {
    private final MessageService messageService;
    private final EducationStatusService educationStatusService;

    @Operation(summary = "[교육 현황 조회]", description = "교육 현황 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/support/getEducationStatus")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEducationStatus(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EducationStatusVO> list = educationStatusService.getEducationStatus(spSearchVO);

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
