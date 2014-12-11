package CNCExpression;

import Exceptions.GcodeRuntimeException;

public class CNCExpGeneral { // general expression used in NGC274 code
	
	private boolean constant = false;

	public double evalute() throws GcodeRuntimeException {
		new GcodeRuntimeException("Empty expression evolution!");
		return 0.0;
	}
	
	public int integerEvalute() throws GcodeRuntimeException { 
		double resultDouble  = this.evalute();
		int    resultInteger = (int)resultDouble;
		if(resultDouble != ((double)resultInteger)) 
			throw new GcodeRuntimeException("Integer value required!");
		return resultInteger; 
	}

	public boolean isConstant() {
		return constant;
	}

	public void setConstant(boolean constant) {
		this.constant = constant;
	}

}
