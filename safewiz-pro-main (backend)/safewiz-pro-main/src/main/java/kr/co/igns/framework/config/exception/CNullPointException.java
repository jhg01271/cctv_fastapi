package kr.co.igns.framework.config.exception;

public class CNullPointException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CNullPointException(String message, Throwable t) {
        super(message, t);
    }
	
	public CNullPointException(String message) {
        super(message);
    }
	
	public CNullPointException() {
        super();
    }
}
