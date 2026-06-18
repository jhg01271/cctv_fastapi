package kr.co.igns.framework.config.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.response
 * @ 파일명		: ListResult.java
 * @ 기능명 		: list 형식
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Getter
@Setter
public class ListResult<T> extends CommonResult {
    private List<T> list;
}