package kr.co.igns.mobile.toolBoxMeeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingSearchVO;
import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingVO;
import kr.co.igns.mobile.toolBoxMeeting.service.V1ToolBoxMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "ToolBoxMeeting", description = "ToolBoxMeeting")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1ToolBoxMeetingController {
    private final MessageService msgSvc;
    private final V1ToolBoxMeetingService svc;

    @Operation(summary = "[TBM 목록 조회]", description = "TBM 목록을 조회한다")
    @GetMapping(value = "/tbm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTbmSchedule(
            @ModelAttribute @Valid ToolBoxMeetingSearchVO vo) throws Exception {
        List<ToolBoxMeetingVO> list = svc.getTbmSchedule(vo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"),
                    msgSvc.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"),
                    msgSvc.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[TBM 생성]", description = "TBM을 생성한다")
    @PostMapping(value = "/tbm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> addTbm(@RequestParam String compId,
                                                                             @RequestBody @Valid ToolBoxMeetingVO vo) {
        try {
            vo.setCompId(compId);
            ToolBoxMeetingVO saveResult = svc.saveTbm(vo);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("result", saveResult);
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("createSuccess.code"),
                    msgSvc.getMessage("createSuccess.msg"), result);
        } catch (Exception e) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, false, "", e.getMessage(), null);

        }

    }

    @Operation(summary = "[TBM 수정]", description = "TBM을 수정한다")
    @PutMapping(value = "/tbm/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modifyTbm(@PathVariable("id") String id,
                                                                                @RequestBody @Valid ToolBoxMeetingVO vo) {
        try {
            ToolBoxMeetingVO saveResult = svc.saveTbm(vo);
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("result", saveResult);
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("createSuccess.code"),
                    msgSvc.getMessage("createSuccess.msg"), result);
        } catch (Exception e) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, false, "", e.getMessage(), null);

        }

    }

    @Operation(summary = "[TBM 상세조회]", description = "TBM 상세내용을 조회한다")
    @GetMapping(value = "/tbm/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTbm(@PathVariable("id") String id) {
        ToolBoxMeetingVO tbm = svc.getTbm(id);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", tbm);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
    }

    @Operation(summary = "[TBM 삭제]", description = "TBM을 삭제한다")
    @DeleteMapping(value = "/tbm/{id}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteTbm(@PathVariable("id") String id) throws Exception {
        svc.deleteTbm(id);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("deleteSuccess.code"), msgSvc.getMessage("deleteSuccess.msg"), result);
    }
}
