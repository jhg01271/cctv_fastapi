package kr.co.igns.framework.config.security.jwt.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.igns.framework.config.response.ListResponseDto;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.jwt.dto.TokenResponseDto;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/igns/auth")
public class JwtController {

    private final JwtService jwtService;

    @GetMapping("/extractToken")
	public ResponseEntity<SingleResponseDto<TokenResponseDto>> extractToken(@Valid @RequestParam String accessToken){
		TokenResponseDto res = jwtService.extractClaimWithAccessToken(accessToken);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, "0000", "ok", res);
	}
}
