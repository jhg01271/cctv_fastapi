package kr.co.igns.system.user.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.user.model.PasswordVO;
import kr.co.igns.system.user.model.TokenVO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.service.TokenService;
import kr.co.igns.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.member.controller
 * ※ 파일명 : UserController.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 08. 13.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *

 */

@Tag(name = "user(사용자)", description = "사용자 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user")
public class UserController {
    private final MessageService messageService;
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    @Operation(summary = "[마이 페이지 조회]", description = "사용자의 마이페이지를 조회한다")
    @GetMapping(value = "/my/info")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@Valid HttpServletRequest request) throws Exception {
        UserVO rtnVo = userService.findOne();

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
    }


    @Operation(summary = "[사용자 정보 변경]", description = "사용자의 정보를 변경한다")
    @PutMapping(value = "/my/info")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@Valid HttpServletRequest request, @RequestBody UserVO vo) throws Exception {
        BaseVO rtnVo = userService.modify(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 탈퇴]", description = "사용자 탈퇴")
    @DeleteMapping(value = "/my/info")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> withdrawal(@Valid HttpServletRequest request, @RequestBody PasswordVO vo) throws Exception {
        boolean isCorrect = userService.isCorrectPassword(vo.getUserId(),vo.getPassword());
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", isCorrect);
        if (isCorrect) {
            userService.remove(vo.getUserId());
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("IncorrectPassword.code"), messageService.getMessage("notCorrectPassword.msg"), result);
        }
    }

    @Operation(summary = "[비밀번호 변경]", description = "사용자의 비밀번호를 변경한다")
    @PutMapping(value = "/my/password")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modifyPassword(@Valid HttpServletRequest request, @RequestBody PasswordVO vo) throws Exception {
        boolean isCorrect = userService.isCorrectPassword(vo.getUserId(),vo.getPassword());
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", isCorrect);
        if (isCorrect) {
            UserVO userVo = new UserVO();
            userVo.setUserId(vo.getUserId());
            userVo.setUserPs(vo.getNewPassword());
            userService.encryptUser(userVo);
            userService.modify(userVo);
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("IncorrectPassword.code"), messageService.getMessage("notCorrectPassword.msg"), result);
        }
    }

    @Operation(summary = "[토큰 삭제]", description = "토큰을 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/my/accesslog/{refreshToken}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> remove(@PathVariable String refreshToken) throws Exception {

        TokenVO rtnDto = tokenService.removeByRefreshToken(refreshToken);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[토큰 목록 조회]", description = "토큰 목록을 조회한다")
    @GetMapping(value = "/my/accesslog/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@Valid HttpServletRequest request, @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        String userId = request.getAttribute("userId").toString();
        UserVO userVo = userService.findOne(userId);
        searchVo.setUid(userVo.getUid());

        searchVo.initialize();

        List<TokenVO> list = tokenService.search(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();

        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[마이 페이지 조회]", description = "사용자의 마이페이지를 조회한다")
    @GetMapping(value = "/my/searchMyInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchMyInfo(@Valid HttpServletRequest request) throws Exception {
        UserVO rtnVo = userService.searchMyInfo();

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
    }
    
    @Operation(summary = "[마이 페이지 조회]", description = "사용자의 마이페이지를 조회한다")
    @PostMapping(value = "/my/saveMyInfo")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMyInfo(@RequestBody @Valid UserVO userVo) throws Exception {
        userService.saveMyInfo(userVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 FCM 토큰 추가]", description = "사용자의 FCM 토큰을 추가한다")
    @PostMapping(value = "/my/updateFcmToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateFcmToken(@Valid HttpServletRequest request, @RequestBody UserVO userVo) throws Exception {
        String FcmToken = userService.updateFcmToken(userVo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", FcmToken);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }

    /*
    * 토큰 관련  API
    * */

    /**** 사용자 refresh 토큰 ****/
    @Operation(summary = "[사용자 토큰 조회]", description = "사용자 토큰을 조회 한다.")
    @GetMapping(value = "/admin/getUserToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getUserToken(@ModelAttribute @Valid SpSearchVO vo) throws Exception {
        String roleCd = SecurityUtil.getCurrentRole();
        if (!"MASTER".equals(roleCd)) {
            return ResponseUtil.SingleResponse(HttpStatus.FORBIDDEN, false, messageService.getMessage("403"),
                    messageService.getMessage("토큰 조회 권한이 없습니다."), null);
        }
        List<TokenVO> rtnVo = tokenService.getUserToken(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 토큰 제거]", description = "사용자 토큰을 제거 한다.")
    @DeleteMapping(value = "/admin/removeUserToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> removeUserToken(@RequestBody List<TokenVO> voList) throws Exception {
        String roleCd = SecurityUtil.getCurrentRole();
        if (!"MASTER".equals(roleCd)) {
            return ResponseUtil.SingleResponse(HttpStatus.FORBIDDEN, false, messageService.getMessage("403"),
                    messageService.getMessage("토큰 제거 권한이 없습니다."), null);
        }
        tokenService.removeUserToken(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    /**** API 토큰 ****/
    @Operation(summary = "[API 토큰 조회]", description = "API 토큰을 조회 한다.")
    @GetMapping(value = "/admin/getApiToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getApiToken(@ModelAttribute @Valid SpSearchVO vo) throws Exception {
        String roleCd = SecurityUtil.getCurrentRole();
        if (!"MASTER".equals(roleCd)) {
            return ResponseUtil.SingleResponse(HttpStatus.FORBIDDEN, false, messageService.getMessage("403"),
                    messageService.getMessage("토큰 조회 권한이 없습니다."), null);
        }
        List<TokenVO> rtnVo = tokenService.getApiToken(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[API 토큰 생성]", description = "API 토큰을 생성 한다.")
    @PostMapping(value = "/admin/createApiToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createApiToken(@RequestBody List<TokenVO> voList) throws Exception {
        String roleCd = SecurityUtil.getCurrentRole();
        if (!"MASTER".equals(roleCd)) {
            return ResponseUtil.SingleResponse(HttpStatus.FORBIDDEN, false, messageService.getMessage("403"),
                    messageService.getMessage("토큰 생성 권한이 없습니다."), null);
        }
        TokenVO rtnVo = tokenService.createApiToken(voList, jwtService);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[API 토큰 제거]", description = "API 토큰을 제거 한다.")
    @DeleteMapping(value = "/admin/removeApiToken")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> removeApiToken(@RequestBody List<TokenVO> voList) throws Exception {
        String roleCd = SecurityUtil.getCurrentRole();
        if (!"MASTER".equals(roleCd)) {
            return ResponseUtil.SingleResponse(HttpStatus.FORBIDDEN, false, messageService.getMessage("403"),
                    messageService.getMessage("토큰 제거 권한이 없습니다."), null);
        }
        tokenService.removeApiToken(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
}
