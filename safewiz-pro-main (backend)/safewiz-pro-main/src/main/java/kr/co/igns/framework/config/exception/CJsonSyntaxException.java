package kr.co.igns.framework.config.exception;

public class CJsonSyntaxException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CJsonSyntaxException(String message, Throwable t) {
        super(message, t);
    }
	
	public CJsonSyntaxException(String message) {
        super(message);
    }
	
	public CJsonSyntaxException() {
        super();
    }
}
