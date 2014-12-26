package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public enum TokenMisc implements TokenDefaultFields {

	VAR("#", -1),
	LEFT_BRACKET("[", 1),
	RIGHT_BRACKET("]", 1),
	ASSIGN("=", 13);
	
	private String alfa_;
	private TokenGroup group_ = TokenGroup.MISC;
	private int precedence_; // in order http://en.wikipedia.org/wiki/Order_of_operations
	
	private TokenMisc(String a, int p){
		alfa_ = a;
		precedence_ = p;
	}
	
	public double evalute(double x) throws InterpreterException{
		throw new InterpreterException("Token interpretation error");
	}	

	public double evalute(double x, double y) throws InterpreterException{
		throw new InterpreterException("Token interpretation error");
	}

	@Override
	public String getAlfa() {
		return alfa_;
	}

	@Override
	public TokenGroup getGroup() {
		return group_;
	}

	@Override
	public int getPrecedence() {
		return precedence_;
	}	

}
