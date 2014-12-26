package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public enum TokenAlgebra implements TokenDefaultFields {
	
	XOR ("XOR",	9){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)^(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	OR ("OR", 10){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)||(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	AND ("AND",	8){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)&&(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	MOD ("MOD",	3){
		@Override
		public double evalute(double x, double y){
			return x % y;
		};
	},
	
	POW("**", 1){
		@Override
		public double evalute(double x, double y){
			return Math.pow(x, y);
		};
	},
	
	PLUS("+", 4){
		@Override
		public double evalute(double x, double y){
			return (x+y);
		};
	},
	
	MINUS("-", 4){
		@Override
		public double evalute(double x, double y){
			return (x-y);
		};
	},
	
	MULTIPLY("*", 3){
		@Override
		public double evalute(double x, double y){
			return (x*y);
		};
	},
	
	DIVIDE("/", 3){
		@Override
		public double evalute(double x, double y) throws InterpreterException{
			if(y == 0.0) throw new InterpreterException("Divide by zero");
			return (x/y);
		};
	};
	
	private String alfa_;
	private TokenGroup group_ = TokenGroup.ALGEBRA;
	private int precedence_; // in order http://en.wikipedia.org/wiki/Order_of_operations
	
	private TokenAlgebra(String a, int p){
		alfa_ = a;
		precedence_ = p;
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
