package kr.co.igns.framework.config.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.response
 * @ 파일명		: CommonResult.java
 * @ 기능명 		: 모든 응답에 대한 형식 정의
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Getter
@Setter
public class CommonResult {
	
	@Schema(description="응답 성공 여부 : true/false")
    private boolean success;

	@Schema(description="응답 코드 번호 : > 0 정상, < 0 비정상")
    private int code;

	@Schema(description="응답 메세지")
    private String msg;
}
