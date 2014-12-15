package CNCExpression;

import Exceptions.GcodeRuntimeException;
import Interpreter.Lexer.TokenEnum;

public class CNCExpAlgebra extends CNCExpGeneral {
	
	private TokenEnum oper_;
	private CNCExpGeneral arg1 = null;
	private CNCExpGeneral arg2 = null;

	public CNCExpAlgebra( TokenEnum o,
								 CNCExpGeneral a1, 
								 CNCExpGeneral a2) {
		this.oper_ = o; 
		this.arg1 = a1;
		this.arg2 = a2;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
	}

	@Override
	public double evalute() throws GcodeRuntimeException {
		double x1 = this.arg1.evalute();
		double x2 = this.arg2.evalute();
		return oper_.evalute(x1, x2);
	}
	
}
