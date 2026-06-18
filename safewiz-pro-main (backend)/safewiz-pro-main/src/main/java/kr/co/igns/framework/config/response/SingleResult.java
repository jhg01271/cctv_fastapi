package kr.co.igns.framework.config.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.response
 * @ 파일명		: SingleResult.java
 * @ 기능명 		: 일반 형태
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
    private T data;
}
