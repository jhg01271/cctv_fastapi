package kr.co.igns.framework.config.exception;

public class CDataAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CDataAccessException(String message, Throwable t) {
        super(message, t);
    }
	
	public CDataAccessException(String message) {
        super(message);
    }
	
	public CDataAccessException() {
        super();
    }
}
