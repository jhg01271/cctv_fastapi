package kr.co.igns.business.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.notice.model.OutputVO;
import kr.co.igns.business.notice.service.OutputService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "SHOutput")
@RestController
@RequiredArgsConstructor
public class OutputController {
    private final ResponseService responseService;
    private final MessageService messageService;
    private final OutputService outputService;

    //private static final Logger log = LogManager.getLogger("com.project");

    @Operation(summary = "[안전보건정보 출력가능 목록 조회]", description = "안전보건정보 출력가능 목록을 조회한다")
    @GetMapping(value = "/safewizpro/hseService/getOutputList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOutputList(@ModelAttribute @Valid SpSearchVO searchVo) throws Throwable {
        List<OutputVO> list = outputService.getOutputList(searchVo);

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

    @Operation(summary = "[안전보건정보 출력]", description = "선택한 안전보건정보를 출력한다.")
    @PostMapping(value = "/safewizpro/hseService/getOutputReport")
    public void getOutputReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody List<OutputVO> voList)
            throws Exception {
        outputService.getOutputReport(request, response, voList);
    }
}
