package kr.co.igns.framework.config.exception;

public class CPrintConnectException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CPrintConnectException(String message, Throwable t) {
        super(message, t);
    }
	
	public CPrintConnectException(String message) {
        super(message);
    }
	
	public CPrintConnectException() {
        super();
    }
}
