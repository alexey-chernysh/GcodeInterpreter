package Exceptions;

public class LexerException extends Exception {
	
	private	String message_ = null;
	private int position_ = -1;

	public LexerException(){
		
	}
	
	public
	LexerException(String msg, int p){
		message_ = msg;
		position_ = p;
	}

	public String getMessage(){ return message_;}
	
	public int getPosition(){ return position_; }
	
}
