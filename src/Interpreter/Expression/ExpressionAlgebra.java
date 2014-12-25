package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.Tokens.TokenEnum;

public class ExpressionAlgebra extends ExpressionGeneral {
	
	private TokenEnum oper_;
	private ExpressionGeneral arg1 = null;
	private ExpressionGeneral arg2 = null;

	public ExpressionAlgebra( TokenEnum o,
								 ExpressionGeneral a1, 
								 ExpressionGeneral a2) {
		this.oper_ = o; 
		this.arg1 = a1;
		this.arg2 = a2;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
	}

	@Override
	public double evalute() throws InterpreterException {
		double x1 = this.arg1.evalute();
		double x2 = this.arg2.evalute();
		return oper_.evalute(x1, x2);
	}
	
}
