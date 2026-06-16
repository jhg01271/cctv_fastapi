package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.setting.model.LoginHistoryVO;
import kr.co.igns.system.setting.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : LoginHistory
 * ※ 기능명 : 사업장 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 26.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "LoginHistory")
@RestController
@RequiredArgsConstructor
public class LoginHistoryController {
    private final MessageService messageService;
    private final LoginHistoryService loginHistoryService;
    @Operation(summary = "[로그인 이력 조회]", description = "로그인 이력을 조회한다")
    @GetMapping(value = "/system/setting/loginHistory/getLoginHistory")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLoginHistory(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        int totalCount = loginHistoryService.searchCount(searchVo);
        List<LoginHistoryVO> list = loginHistoryService.getLoginHistory(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return  ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[마지막 로그인 정보 조회]", description = "마지막으로 로그인 했을 때의 정보를 조회한다 / 작성자 : 류원진, 작성일 : 2025.03.06")
    @GetMapping(value = "/system/setting/loginHistory/getLastLoginDetails")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLastLoginDetails(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        LoginHistoryVO vo = loginHistoryService.getLastLoginDetails(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", vo);
        if (vo == null) {
            return  ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
}
