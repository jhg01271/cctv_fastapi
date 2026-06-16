package kr.co.igns.system.setting.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.service.SystemCodeService;
import kr.co.igns.system.common.code.YesNo;
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

@Tag(name = "System Code")
@RestController
@RequiredArgsConstructor
public class SystemCodeController {
    private final MessageService messageService;
    private final SystemCodeService systemCodeService;
    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/base/system/systemCode/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        int totalCount = systemCodeService.searchCount(searchVo);
        List<SystemCodeVO> list = systemCodeService.search(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[트리 조회]", description = "목록으로 트리를 조회한다")
    @GetMapping(value = "/system/setting/systemCode/tree")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeSystemCode(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

    	searchVo.setUseYn(YesNo.Y.toString());// 사용자들은 사용만 보여 준다.
        ArrayNode list = systemCodeService.tree(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }


}
