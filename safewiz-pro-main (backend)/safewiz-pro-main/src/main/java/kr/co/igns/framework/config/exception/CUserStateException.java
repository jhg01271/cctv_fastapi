package kr.co.igns.framework.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.exception
 * @ 파일명		: CUserStateException.java
 * @ 기능명 		:
 * @ 작성자 		: 조동하
 * @ 최초생성일 	: 2024. 8. 7.
 */

public class CUserStateException extends AuthenticationException {
    public CUserStateException(String msg) {
        super(msg);
    }

    public CUserStateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
