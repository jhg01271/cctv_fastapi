package kr.co.igns.framework.config.exception;

public class CEmailValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CEmailValidationException(String message, Throwable t) {
        super(message, t);
    }

    public CEmailValidationException(String message) {
        super(message);
    }

    public CEmailValidationException() {
        super();
    }
}
