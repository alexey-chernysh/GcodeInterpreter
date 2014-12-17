package Interpreter.Expression;

import Exceptions.GcodeRuntimeException;
import Interpreter.Lexer.TokenEnum;

public class ExpressionFunction extends ExpressionGeneral {

	private TokenEnum fun_ = null;
	private ExpressionGeneral arg1 = null;
	private ExpressionGeneral arg2 = null;

	public ExpressionFunction(TokenEnum f, ExpressionGeneral a) {
		this.fun_ = f;
		this.arg1 = a;
		this.setConstant(this.arg1.isConstant());
	}

	public ExpressionFunction(TokenEnum f, ExpressionGeneral a1, ExpressionGeneral a2) {
		this.fun_ = f;
		this.arg1 = a1;
		this.arg2 = a2;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
	}

	public ExpressionFunction(TokenEnum f) {
		this.fun_ = f;
	}
/*
	public CNCExpFunction setArg1(CNCExpGeneral a){
		this.arg1 = a;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
		return this;
	}
	
	public CNCExpFunction setArg2(CNCExpGeneral a){
		this.arg2 = a;
		if(this.arg1.isConstant() && this.arg2.isConstant()) this.setConstant(true);
		return this;
	}
*/	
	@Override
	public double evalute() throws GcodeRuntimeException {
		double x = arg1.evalute();
		if(arg2 != null){
			double y = this.arg2.evalute();
			return this.fun_.evalute(x,y);
		} else return this.fun_.evalute(x);
	}
	
}
