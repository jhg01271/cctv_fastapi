package kr.co.igns.framework.config.exception;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CBoardNotFoundException.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 10. 23.
 */

public class CBoardNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public CBoardNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CBoardNotFoundException(String msg) {
        super(msg);
    }

    public CBoardNotFoundException() {
        super();
    }
}