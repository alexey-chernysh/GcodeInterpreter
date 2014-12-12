package CNCExpression;

import Exceptions.GcodeRuntimeException;
import Interpreter.InterpreterState;

public class CNCExpVariable extends CNCExpGeneral  {

	private CNCExpGeneral varNumExp_;
	
	public CNCExpVariable(CNCExpGeneral e) {
		this.varNumExp_ = e;
	}

	@Override
	public double evalute() throws GcodeRuntimeException{
		int varNum = this.varNumExp_.integerEvalute();
		return InterpreterState.vars_.get(varNum);
	}
	
}