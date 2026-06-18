package kr.co.igns.system.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.common.code
 * ※ 파일명 : YesNo.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 5. 8.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum YesNo {

	Y("Yes"), N("No");

	private final String key;

}
