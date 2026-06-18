package kr.co.igns.framework.config.exception;

/**
 *
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CTokenValicationException.java
 * @ 기능명 		:
 * @ 작성자 		: 조동하
 * @ 최초생성일 	: 2024. 8. 7.
 */

public class CTokenValicationException extends RuntimeException{


    private static final long serialVersionUID = 1L;

    public CTokenValicationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CTokenValicationException(String msg) {
        super(msg);
    }

    public CTokenValicationException() {
        super();
    }
}
