package kr.co.igns.framework.config.exception;

/**
 *
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CInviteKeyException.java
 * @ 기능명 		:
 * @ 작성자 		: 조동하
 * @ 최초생성일 	: 2024. 8. 7.
 */

public class CInviteKeyException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CInviteKeyException(String msg, Throwable t) {
        super(msg, t);
    }

    public CInviteKeyException(String msg) {
        super(msg);
    }

    public CInviteKeyException() {
        super();
    }
}
