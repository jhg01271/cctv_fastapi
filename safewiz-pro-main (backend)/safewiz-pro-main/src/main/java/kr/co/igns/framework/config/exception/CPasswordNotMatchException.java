package kr.co.igns.framework.config.exception;

public class CPasswordNotMatchException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CPasswordNotMatchException(String message, Throwable t) {
        super(message, t);
    }
	
	public CPasswordNotMatchException(String message) {
        super(message);
    }
	
	public CPasswordNotMatchException() {
        super();
    }
}
