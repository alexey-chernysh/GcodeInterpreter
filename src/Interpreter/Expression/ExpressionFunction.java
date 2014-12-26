package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.Tokens.TokenFunction;

public class ExpressionFunction extends ExpressionGeneral {

	private TokenFunction fun_ = null;
	private ExpressionGeneral arg1 = null;
	private ExpressionGeneral arg2 = null;

	public ExpressionFunction(TokenFunction f, ExpressionGeneral a) {
		this.fun_ = f;
		this.arg1 = a;
		this.setConstant(this.arg1.isConstant());
	}

	public ExpressionFunction(TokenFunction f, ExpressionGeneral a1, ExpressionGeneral a2) {
		this.fun_ = f;
		this.arg1 = a1;
		this.arg2 = a2;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
	}

	public ExpressionFunction(TokenFunction f) {
		this.fun_ = f;
	}

	@Override
	public double evalute() throws InterpreterException {
		double x = arg1.evalute();
		if(arg2 != null){
			double y = this.arg2.evalute();
			return this.fun_.evalute(x,y);
		} else return this.fun_.evalute(x);
	}
	
}
