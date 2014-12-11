package Exceptions;

public class HWCException extends Exception {
	
	private String message_ = null;
	private static final long serialVersionUID = 1L;
	
	public HWCException(String m){
		message_ = m;
	}

	public String getMessage_() {
		return message_;
	}

}
