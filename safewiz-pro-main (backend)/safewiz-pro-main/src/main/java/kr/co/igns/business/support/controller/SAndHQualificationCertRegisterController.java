package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.SAndHQualificationCertRegisterVO;
import kr.co.igns.business.support.service.SAndHQualificationCertRegisterService;
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
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;

@Tag(name = "안전보건 자격인증 관리대장")
@RestController
@RequiredArgsConstructor
public class SAndHQualificationCertRegisterController {
    private final MessageService messageService;
    private final SAndHQualificationCertRegisterService service;

    @Operation(summary = "[조회]", description = "안전보건 자격인증 관리대장 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/support/SAndHQualificationCertRegister")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSAndHQualificationCertRegister(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
                List<SAndHQualificationCertRegisterVO> list = service.searchList(searchVo);

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

    @Operation(summary = "[상세 조회]", description = "안전보건 자격인증 관리대장 상세 데이터를 조회한다.")
    @PostMapping(value = "/safewizpro/support/SAndHQualificationCertRegisterDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSAndHQualificationCertRegisterDetail(
        @RequestBody @Valid SAndHQualificationCertRegisterVO reqVo) throws Exception {
        SAndHQualificationCertRegisterVO list = service.search(reqVo);

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

    @Operation(summary = "[등록]", description = "안전보건 자격인증 관리대장 데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/support/insertSAndHQualificationCertRegister")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertSAndHQualificationCertRegister(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") SAndHQualificationCertRegisterVO reqVo) throws Exception {
        BaseVO rtnDto = service.create(files, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[삭제]", description = "안전보건 자격인증 관리대장 데이터를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/support/deleteSAndHQualificationCertRegister")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSAndHQualificationCertRegister(@RequestBody @Valid List<SAndHQualificationCertRegisterVO> reqVo) throws Exception {
        BaseVO rtnDto = service.delete(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/support/SAndHQualificationCertRegisterReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody List<SAndHQualificationCertRegisterVO> vo) throws Exception {
        service.getReport(request, response, vo);
    }
    
}
