package kr.co.igns.framework.config.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.config.response.CommonResult;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: ExceptionController.java
 * @ 기능명 		: 에러처리 컨트롤러
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Tag(name="#. Exception")
@RequiredArgsConstructor
@RestController
@Log4j2
public class ExceptionController {
	
	@Operation(summary="[에러처리]",description="진입 메서드가 없을때 에러 처리.")
    @GetMapping(value = "/exception/entrypoint")
    public CommonResult entrypointException() {
		log.info("============세션만료 에러처리============");
        throw new CAuthenticationEntryPointException();
    }

	@Operation(summary="[에러처리]",description="권한이 없을때 에러 처리.")
    @GetMapping(value = "/exception/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessDeniedException("");
    }
}
