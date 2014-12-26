package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public enum TokenFunction implements TokenDefaultFields {
	
	ABS ("ABS"){
		@Override
		public double evalute(double x){
			return Math.abs(x);
		};
	},
	
	ACOS("ACOS"){
		@Override
		public double evalute(double x){
			return Math.acos(x)*180.0/Math.PI;
		};
	},
	
	ASIN("ASIN"){
		@Override
		public double evalute(double x){
			return Math.asin(x)*180.0/Math.PI;
		};
	},
	
	ATAN("ATAN"){
		@Override
		public double evalute(double x, double y){
			return Math.atan2(x, y)*180.0/Math.PI;
		};
	},
	
	COS("COS"){
		@Override
		public double evalute(double x){
			return Math.cos(Math.PI*x/180.0);
		};
	},
	
	FIX("FIX"){
		@Override
		public double evalute(double x){
			return Math.floor(x);
		};
	},
	
	FUP("FUP"){
		@Override
		public double evalute(double x){
			return (Math.floor(x)+1.0);
		};
	},
	
	ROUND("ROUND"){
		@Override
		public double evalute(double x){
			return Math.round(x);
		};
	},
	
	SIN("SIN"){
		@Override
		public double evalute(double x){
			return Math.sin(Math.PI*x/180.0);
		};
	},
	
	TAN("TAN"){
		@Override
		public double evalute(double x){
			return Math.tan(Math.PI*x/180.0);
		};
	},
	
	SQRT("SQRT"){
		@Override
		public double evalute(double x) throws InterpreterException{
			if(x<0.0) throw new InterpreterException("Square root from negative number");
			return Math.sqrt(x);
		};
	},
	
	EXP("EXP"){
		@Override
		public double evalute(double x){
			return Math.exp(x);
		};
	},
	
	LN("LN"){
		@Override
		public double evalute(double x) throws InterpreterException{
			if(x<0.0) throw new InterpreterException("Log from negative number");
			return Math.log(x);
		};
	};	
	
	private String alfa_;
	private TokenGroup group_ = TokenGroup.FUNCTION;
	private int precedence_ = -1; // in order http://en.wikipedia.org/wiki/Order_of_operations
	
	private TokenFunction(String a){
		alfa_ = a;
	}
	
	public double evalute(double x) throws InterpreterException{
		throw new InterpreterException("Token interpretation error");
	}	

	public double evalute(double x, double y) throws InterpreterException{
		throw new InterpreterException("Token interpretation error");
	}

	@Override
	public String getAlfa() {
		return alfa_;
	}

	@Override
	public TokenGroup getGroup() {
		return group_;
	}

	@Override
	public int getPrecedence() {
		return precedence_;
	}	

}
