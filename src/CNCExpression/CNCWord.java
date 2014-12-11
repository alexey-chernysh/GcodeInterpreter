package CNCExpression;

import Exceptions.GcodeRuntimeException;
import Token.TokenEnum;

public class CNCWord {
	
	private TokenEnum type_;
	private double value_ = -1;
	private CNCExpGeneral commandNumExpression_ = null;

	public CNCWord(TokenEnum t, CNCExpGeneral exp){
		this.type_ = t;
		this.commandNumExpression_ = exp;
	}
	
	public void evalute() throws GcodeRuntimeException {
		this.value_ = this.commandNumExpression_.evalute();
	}

	public double getValue(){
		return this.value_;
	}
	
	public TokenEnum getType(){
		return this.type_;
	}
	
	@Override
	public String toString(){
		String result = this.type_.alfa_ + this.value_ + " ";
		return result;
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
