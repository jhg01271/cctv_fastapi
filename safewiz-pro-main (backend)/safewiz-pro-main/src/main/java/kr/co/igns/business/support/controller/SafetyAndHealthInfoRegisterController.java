package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.SafetyAndHealthInfoRegisterVO;
import kr.co.igns.business.support.service.SafetyAndHealthInfoRegisterService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : safetyAndHealthInfoRegister
 * ※ 기능명 : 안전보건정보 관리대장
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 28.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "안전보건정보 관리대장")
@RestController
@RequiredArgsConstructor
public class SafetyAndHealthInfoRegisterController {
    private final MessageService messageService;
    private final SafetyAndHealthInfoRegisterService safetyAndHealthInfoRegisterService;

    @Operation(summary = "[안전보건정보 관리대장 조회]", description = "안전보건정보 관리대장을 조회한다.")
    @GetMapping(value = "/safewizpro/support/safetyAndHealthInfoRegister/getShInfoRegisterList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getShInfoRegisterList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthInfoRegisterVO> list = safetyAndHealthInfoRegisterService.getShInfoRegisterList(searchVo);

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

    @Operation(summary = "[My 안전보건정보 관리대장 조회]", description = "My 안전보건정보 관리대장을 조회한다.")
    @GetMapping(value = "/safewizpro/support/safetyAndHealthInfoRegister/getMyShInfoRegisterList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMyShInfoRegisterList(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthInfoRegisterVO> list = safetyAndHealthInfoRegisterService.getMyShInfoRegisterList(searchVo);

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

    @Operation(summary = "[안전보건정보 관리대장 저장]", description = "안전보건정보 관리대장을 저장한다")
    @PostMapping(value = "/safewizpro/support/safetyAndHealthInfoRegister/saveShInfoRegister")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveShInfoRegister(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") SafetyAndHealthInfoRegisterVO vo) throws Exception {

        BaseVO rtnDto = safetyAndHealthInfoRegisterService.saveShInfoRegister(files, vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건정보 관리대장 삭제]", description = "안전보건정보 관리대장을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/safetyAndHealthInfoRegister/deleteShInfoRegister")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteShInfoRegister(@RequestBody List<SafetyAndHealthInfoRegisterVO> list) throws Exception {
        BaseVO rtnDto = safetyAndHealthInfoRegisterService.deleteShInfoRegister(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건정보 관리대장 출력]", description = "안전보건정보 관리대장 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/support/safetyAndHealthInfoRegister/getShInfoRegisterReport")
    public void getShInfoRegisterReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            safetyAndHealthInfoRegisterService.getShInfoRegisterReport(request, response, spSearchVO);
    }

}
