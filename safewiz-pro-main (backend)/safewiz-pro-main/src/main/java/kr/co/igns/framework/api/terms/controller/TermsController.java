//package kr.co.igns.framework.api.terms.controller;
//
//import java.util.HashMap;
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.ArraySchema;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.framework.api.terms.dto.TermsResponseDto;
//import kr.co.igns.framework.api.terms.service.TermsService;
//import kr.co.igns.framework.config.message.MessageService;
//import kr.co.igns.framework.config.response.ListResponseDto;
//import kr.co.igns.framework.config.response.ResponseUtil;
//import kr.co.igns.framework.config.response.SingleResponseDto;
//import lombok.RequiredArgsConstructor;
//
//@Tag(name = "Terms", description = "개인정보처리방침, 약관정보, 정책 등")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/igns/common")
//
//public class TermsController {
//
//	private final TermsService termsService;
//
//	private final MessageService messageService;
//	
//	@Operation(summary = "약관 정보 조회", description = "약관 정보를 조회한다.")
//	@GetMapping(value = "/terms")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "약관 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TermsResponseDto.class)))) })
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTerms(
//		@Parameter(description = "약관 타입(B- 이용약관 , P - 개인정보처리방침 )", example = "B") @RequestParam String type,
//		@Parameter(description = "서비스 타입(W- 웹, A - 앱)", example = "") @RequestParam String serviceType) 
//	{
//		List<TermsResponseDto> res = termsService.getTerms(type, serviceType);
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("list", res);
//
//		
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//	}
//}
