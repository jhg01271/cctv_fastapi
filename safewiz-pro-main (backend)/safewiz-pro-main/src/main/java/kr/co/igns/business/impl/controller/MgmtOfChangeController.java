package kr.co.igns.business.impl.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.MgmtOfChangeVO;
import kr.co.igns.business.impl.service.MgmtOfChangeService;
import kr.co.igns.business.support.model.QualificationCertificationAssessmentVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;


@Tag(name = "Impl")
@RequestMapping("/safewizpro/impl")
@RestController
@RequiredArgsConstructor
public class MgmtOfChangeController {
    
    private final MessageService messageService;
    private final MgmtOfChangeService service;
    @PostMapping(value = "/saveMgmtOfChange")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMgmtOfChange(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") MgmtOfChangeVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());
		BaseVO rtnDto = service.saveMgmtOfChange(files, vo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    
	}

    @DeleteMapping(value = "/deleteMgmtOfChange")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMgmtOfChange(
        @RequestBody @Valid List<SpSearchVO> voList) throws Throwable {
		BaseVO rtnDto = service.deleteMgmtOfChangeDetail(voList);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    
	}

    @PostMapping(value = "/getMgmtOfChangeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMgmtOfChangeDetail(
        @RequestBody @Valid SpSearchVO reqVo) throws Exception {
        MgmtOfChangeVO list = service.getMgmtOfChangeDetail(reqVo);

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

    @GetMapping(value = "/getMgmtOfChange")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMgmtOfChange(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
            List<MgmtOfChangeVO> list = service.getMgmtOfChange(searchVo);

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

    @Operation(summary = "변경관리 문서 출력", description = "출력물 다운로드")
	@PostMapping(value = "/getMgmtOfChangeReport")
	public void getMgmtOfChangeReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
        service.getMgmtOfChangeReport(request, response, vo);
    }
}
