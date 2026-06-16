package kr.co.igns.framework.config.exception;

public class CDateTimeFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CDateTimeFormatException(String message, Throwable t) {
        super(message, t);
    }
	
	public CDateTimeFormatException(String message) {
        super(message);
    }
	
	public CDateTimeFormatException() {
        super();
    }
}
