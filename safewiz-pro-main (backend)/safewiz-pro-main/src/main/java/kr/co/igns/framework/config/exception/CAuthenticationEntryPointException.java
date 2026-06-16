package kr.co.igns.framework.config.exception;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CAuthenticationEntryPointException.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

public class CAuthenticationEntryPointException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public CAuthenticationEntryPointException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAuthenticationEntryPointException(String msg) {
        super(msg);
    }

    public CAuthenticationEntryPointException() {
        super();
    }
}
