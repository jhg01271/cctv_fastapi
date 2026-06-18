package kr.co.igns.system.user.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.system.user.model.VerificationVO;
import kr.co.igns.system.user.service.MemberService;
import kr.co.igns.system.user.service.UserService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;

@Tag(name = "member", description = "멤버 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MemberController {
    private final MessageService messageService;
    private final MemberService memberService;
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/igns/auth/user")
    public ResponseEntity<SingleResponseDto<BaseVO>> search(@Valid HttpServletRequest request) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        // count -> totalCount 변수 변경
        BaseVO res = userService.findOne(userId);
        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", res).getBody(), null, HttpStatus.valueOf(200));
    }

    @Operation(summary = "[인증 메일 발송]", description = "인증 메일을 발송한다.")
    @PostMapping(value = "/igns/auth/verification/{email}")
    public ResponseEntity<SingleResponseDto<String>> sendCode(@Valid HttpServletResponse response, @PathVariable @Valid String email) throws Exception {
        String verificationCode = memberService.sendCode(email);
        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", verificationCode).getBody(), null, HttpStatus.valueOf(200));
    }

    @Operation(summary = "[인증번호 검증]", description = "인증번호를 검증한다.")
    @PostMapping(value = "/igns/auth/verification")
    public ResponseEntity<SingleResponseDto<Boolean>> verifyCertification(@RequestBody VerificationVO vo) throws Exception {
        boolean res = memberService.verifyCertification(vo);
        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", res).getBody(), null, HttpStatus.valueOf(200));
    }


    @Operation(summary = "[회원가입]", description = "회원가입")
    @PostMapping(value = "/igns/auth/sign-up")
    public ResponseEntity<SingleResponseDto<BaseVO>> signUp(@Valid HttpServletRequest request, @Valid HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        BaseVO res = memberService.signUp(response, vo);

        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", res).getBody(), null, HttpStatus.valueOf(200));
    }

    @Operation(summary = "[비밀번호 초기화 페이지]", description = "비밀번호 초기화 페이지")
    @GetMapping(value = "/igns/auth/password")
    public ResponseEntity<SingleResponseDto<Boolean>> getFindUserPs(@RequestParam(value = "key") @Valid String token) throws Exception {
        Optional<String> uid = jwtService.extractCustomClaim(token, "uid");
        if (uid.isPresent()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), true);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), false);
        }
    }

    @Operation(summary = "[비밀번호 초기화 메일 발송]", description = "비밀번호 초기화 메일을 발송한다.")
    @PostMapping(value = "/igns/auth/password")
    public ResponseEntity<SingleResponseDto<String>> sendLink(@RequestParam(value = "email") @Valid String email) throws Exception {
        memberService.sendLink(email);
        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", "").getBody(), null, HttpStatus.valueOf(200));
    }

    @Operation(summary = "[비밀번호 초기화]", description = "비밀번호를 초기화한다.")
    @PutMapping(value = "/igns/auth/password")
    public ResponseEntity<SingleResponseDto<Boolean>> initPassword(@RequestParam(value = "key") @Valid String token, @RequestBody String password) throws Exception {
        password = password.replace("\"", "");
        boolean flag = memberService.initPassword(token, password);
        if (flag) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), flag);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), flag);
        }
    }

    @Operation(summary = "[아이디 찾기]", description = "사용자 아이디를 조회한다")
    @PostMapping(value = "/igns/auth/user/findId")
    public ResponseEntity<SingleResponseDto<BaseVO>> findId(@RequestBody UserVO vo) throws Exception {
        BaseVO res = userService.findId(vo);
        return new ResponseEntity<>(ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", res).getBody(), null, HttpStatus.valueOf(200));
    }

    @Operation(summary = "[비밀번호 찾기]", description = "이메일 인증 결과를 반환한다")
    @PostMapping(value = "/igns/auth/user/findPassword")
    public ResponseEntity<SingleResponseDto<Boolean>> findPassword(@RequestBody UserVO vo) throws Exception {
        boolean flag = userService.findPassword(vo);
        if (flag) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), flag);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), flag);
        }
    }
}
