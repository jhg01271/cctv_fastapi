package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.AuditVO;
import kr.co.igns.system.setting.service.AuditService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "Audit")
@RestController
@RequiredArgsConstructor
public class AuditController {
    private final MessageService messageService;
    private final AuditService auditService;

    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/system/setting/audit/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.initialize();
        List<AuditVO> list = auditService.search(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

}
