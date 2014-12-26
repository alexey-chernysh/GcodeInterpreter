package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.Tokens.TokenCommand;

public class CommandPair { // pair of command indrntifier alfa and associated expression
	
	private TokenCommand type_;
	private ExpressionGeneral commandExpression_ = null;

	public CommandPair(TokenCommand t, ExpressionGeneral exp){
		this.type_ = t;
		this.commandExpression_ = exp;
	}
	
	public double getCurrentValue() throws InterpreterException {
		return this.commandExpression_.evalute();
	}

	public ExpressionGeneral getValueExpression() {
		return this.commandExpression_;
	}
	
	public TokenCommand getType(){
		return this.type_;
	}
	
	@Override
	public String toString(){
		return this.type_.getAlfa() + "=" + this.commandExpression_.toString();
	}
	
	public enum CNCWordEnum {
		A,
		B,
		C,
		D,
		H,
		I,
		J,
		K,
		L,
		P,
		Q,
		R,
		X,
		Y,
		Z;
	}
}
