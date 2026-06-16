package kr.co.igns.framework.api.log.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto;
import kr.co.igns.framework.api.log.dto.LogResponseDto;
import kr.co.igns.framework.api.log.service.LogService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;

/**
 * 
 * @ 패키지		: kr.co.igns.framework.comm.controller
 * @ 파일명		: LogController.java
 * @ 기능명 		: 로그관리 Controller
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 11. 10.
 * @ 프로그램 설명 	: 로그 관리 Controller
 * 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 */

@Tag(name="F000. Log")
@RestController
@RequiredArgsConstructor
@RequestMapping("/com")
public class LogController {
	
	private final ResponseService responseService;	// 리턴 서비스
	private final ExceptionAdvice exceptionAdvice; 	// 예외처리
	private final LogService logService;	 		// 로그 서비스
		
	/*
	 * 로그 조회
	 */
	@Operation(summary="[로그 조회]",description="로그를 조회한다.")
	@GetMapping(value = "/getLogListAll")
	public CommonResult getLogListAll() {
		List<LogResponseDto> resultList = logService.getLogListAll();
		if (resultList.size() < 1) {
			return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")), exceptionAdvice.getMessage("noResultFound.msg"));
		}
		return responseService.getListResult(resultList);
	}	

	/*
	 * 로그 차트 조회
	 */
	@Operation(summary="[로그 차트 조회]",description="로그를 차트 조회한다.")
	@GetMapping(value = "/getLogChart")
	public CommonResult getLogChart() {
		List<LogResponseChartDto> resultList = logService.getLogChart();
		if (resultList.size() < 1) {
			return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")), exceptionAdvice.getMessage("noResultFound.msg"));
		}
		return responseService.getListResult(resultList);
	}
}

