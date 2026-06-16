package kr.co.igns.framework.config.exception;

public class CIndexBoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CIndexBoundException(String message, Throwable t) {
        super(message, t);
    }
	
	public CIndexBoundException(String message) {
        super(message);
    }
	
	public CIndexBoundException() {
        super();
    }
}
