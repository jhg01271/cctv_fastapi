package kr.co.igns.framework.config.exception;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CUserDefinedException.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

public class CUserDefinedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public CUserDefinedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserDefinedException(String msg) {
        super(msg);
    }

    public CUserDefinedException() {
        super();
    }
}