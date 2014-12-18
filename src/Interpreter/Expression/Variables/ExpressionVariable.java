package Interpreter.Expression.Variables;

import Exceptions.GcodeRuntimeException;
import Interpreter.Expression.ExpressionGeneral;
import Interpreter.State.InterpreterState;

public class ExpressionVariable extends ExpressionGeneral  {

	private ExpressionGeneral varNumExp_;
	
	public ExpressionVariable(ExpressionGeneral e) {
		this.varNumExp_ = e;
	}

	@Override
	public double evalute() throws GcodeRuntimeException{
		int varNum = this.varNumExp_.integerEvalute();
		return InterpreterState.vars_.get(varNum);
	}
	
}