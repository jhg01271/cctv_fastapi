package kr.co.igns.framework.config.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.response
 * @ 파일명		: MapResult.java
 * @ 기능명 		: Map 형태
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@SuppressWarnings("hiding")
@Getter
@Setter
public class MapResult<String, Object> extends CommonResult {
    private Map<String, Object> map;
}