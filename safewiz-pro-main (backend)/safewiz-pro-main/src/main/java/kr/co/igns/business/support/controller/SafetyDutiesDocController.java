package kr.co.igns.business.support.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.support.model.SafetyDutiesDocVO;
import kr.co.igns.business.support.service.SafetyDutiesDocService;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.business.planning.service.MsdsService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "참여", description = "MSDS 관리")
@RestController
@RequiredArgsConstructor
public class SafetyDutiesDocController {
    private final MessageService messageService;
    private final SafetyDutiesDocService service;

    @Operation(summary = "[안전업무 구분 조회]", description = "안전업무 구분 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/getSafetyDutyType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyDutyType(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyDutiesDocVO> list = service.getSafetyDutyType(searchVo);

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

    @Operation(summary = "[조회]", description = "안전업무 지정서 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/SafetyDutiesDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyDutiesDoc(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
                List<SafetyDutiesDocVO> list = service.searchList(searchVo);

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

    @Operation(summary = "[조회]", description = "안전업무 지정서 데이터셋을 조회한다")
    @GetMapping(value = "/safewizpro/support/SafetyDutiesDocDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyDutiesDocDataset(
            @ModelAttribute @Valid SafetyDutiesDocVO vo) throws Exception {
                SafetyDutiesDocVO list = service.searchDataset(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[상세 조회]", description = "안전업무 지정서 상세 데이터를 조회한다.")
    @PostMapping(value = "/safewizpro/support/SafetyDutiesDocDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyDutiesDocDetail(
        @RequestBody @Valid SpSearchVO reqVo) throws Exception {
        SafetyDutiesDocVO list = service.search(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[등록]", description = "안전업무 지정서 데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/support/insertSafetyDutiesDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertSafetyDutiesDoc(@RequestBody @Valid SafetyDutiesDocVO reqVo) throws Exception {
        BaseVO rtnDto = service.create(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[삭제]", description = "안전업무 지정서 데이터를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/support/deleteSafetyDutiesDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyDutiesDoc(@RequestBody @Valid List<SafetyDutiesDocVO> reqVo) throws Exception {
        BaseVO rtnDto = service.delete(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[부서 상황 출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/support/SafetyDutiesDocReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
        service.getReport(request, response, vo);
    }
    
}
