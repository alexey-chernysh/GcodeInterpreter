package CNCExpression;

import Exceptions.GcodeRuntimeException;
import InterpreterCommands.InterpreterState;

public class CNCVarAssignment {

	private CNCExpGeneral varNumExp_;
	private CNCExpGeneral varValExp_;
	private int lastNum_ = 0;
	private double lastValue_ = 0.0;
	
	public CNCVarAssignment(CNCExpGeneral en, CNCExpGeneral ev) {
		this.varNumExp_ = en;
		this.varValExp_ = ev;
	}

	public void evalute() throws GcodeRuntimeException{
		this.lastNum_ = this.varNumExp_.integerEvalute();
		this.lastValue_ = this.varValExp_.evalute();
		InterpreterState.vars_.set(this.lastNum_, this.lastValue_);
	}

	@Override
	public String toString(){
		String result = "Var" + this.lastNum_ + " = " + this.lastValue_;
		return result;
	}

}