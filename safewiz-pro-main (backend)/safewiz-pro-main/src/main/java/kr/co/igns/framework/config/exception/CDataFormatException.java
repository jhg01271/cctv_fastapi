package kr.co.igns.framework.config.exception;

public class CDataFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CDataFormatException(String message, Throwable t) {
        super(message, t);
    }
	
	public CDataFormatException(String message) {
        super(message);
    }
	
	public CDataFormatException() {
        super();
    }
}
