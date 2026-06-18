package kr.co.igns.framework.config.exception;

public class CAuthClaimNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CAuthClaimNotFoundException(String message, Throwable t) {
        super(message, t);
    }
	
	public CAuthClaimNotFoundException(String message) {
        super(message);
    }
	
	public CAuthClaimNotFoundException() {
        super();
    }
}
