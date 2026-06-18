package kr.co.igns.mobile.user.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.user.model.UnsignedSignaturesVO;
import kr.co.igns.mobile.user.model.UserSearchVO;
import kr.co.igns.mobile.user.service.V1UserService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.user.controller
 * ※ 파일명 : V1UserController
 * ※ 기능명 : 사용자 controller
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 12. 13.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "User", description = "사용자")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1UserController {
    private final MessageService messageService;
    private final V1UserService v1UserService;
    
    @Operation(summary = "[미서명 항목 목록 조회]", description = "사용자가 서명하지 않은 모든 항목 목록을 조회하기 위한 API ")
    @GetMapping(value = "/unsignedSignatures")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getUnsignedSignaturesList(
            @ModelAttribute @Valid UserSearchVO userSearchVO) throws Exception {
    	List<UnsignedSignaturesVO> list = v1UserService.getUnsignedSignaturesList(userSearchVO);

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
