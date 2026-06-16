package kr.co.igns.business.comp.master.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.master.model.CompCodeVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.business.comp.master.service.CompCodeService;
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

@Tag(name = "Company Code")
@RestController
@RequiredArgsConstructor
public class CompCodeController {
    private final MessageService messageService;
    private final CompCodeService compCodeService;


    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/comp/master/compCode/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        int totalCount = compCodeService.searchCount(searchVo);
        List<CompCodeVO> list = compCodeService.search(searchVo);

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
    @GetMapping(value = "/comp/master/compCode/tree")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeCompCode(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        ArrayNode list = compCodeService.treeCompCode(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[하위 목록 조회]", description = "하위 목록을 조회한다")
    @GetMapping(value = "/comp/master/site/{compId}/compCode/{compCode}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSubCodeList(@PathVariable String compId, @PathVariable String compCode) throws Exception {
        List<CompCodeVO> list = compCodeService.getSubCodeList(compId, compCode);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

}
