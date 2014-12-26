package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public enum TokenCommand implements TokenDefaultFields {
	
	G("G"), // general command
	
	M("M"), // miscellaneous command
	
	N("N"), // line number
	
	O("O"), // program name
	
	S("S"), // spindle speed
	
	T("T"), // tool selection
	
	F("F"); // feed rate
	
	private String alfa_;
	private TokenGroup group_ = TokenGroup.COMMAND;
	private int precedence_ = -1;
	
	private TokenCommand(String a){
		alfa_ = a;
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
