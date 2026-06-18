package kr.co.igns.framework.config.exception;

public class CNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CNotFoundException(String message, Throwable t) {
        super(message, t);
    }
	
	public CNotFoundException(String message) {
        super(message);
    }
	
	public CNotFoundException() {
        super();
    }
}
