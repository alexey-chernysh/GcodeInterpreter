package Exceptions;

public class GcodeRuntimeException extends Exception {
	private
	String message_ = null;

	public
	GcodeRuntimeException(){}
	
	public
	GcodeRuntimeException(String msg){
		message_ = msg;
	}

	public
	String getMessage(){return message_;}
}
