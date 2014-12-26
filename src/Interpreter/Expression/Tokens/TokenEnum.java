package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public enum TokenEnum implements TokenDefaultFields {
	
	ABS ("ABS",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.abs(x);
		};
	},
	
	ACOS("ACOS", TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.acos(x)*180.0/Math.PI;
		};
	},
	
	ASIN("ASIN", TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.asin(x)*180.0/Math.PI;
		};
	},
	
	ATAN("ATAN", TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x, double y){
			return Math.atan2(x, y)*180.0/Math.PI;
		};
	},
	
	COS("COS",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.cos(Math.PI*x/180.0);
		};
	},
	
	FIX("FIX",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.floor(x);
		};
	},
	
	FUP("FUP",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return (Math.floor(x)+1.0);
		};
	},
	
	ROUND("ROUND",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.round(x);
		};
	},
	
	SIN("SIN",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.sin(Math.PI*x/180.0);
		};
	},
	
	TAN("TAN",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.tan(Math.PI*x/180.0);
		};
	},
	
	SQRT("SQRT",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x) throws InterpreterException{
			if(x<0.0) throw new InterpreterException("Square root from negative number");
			return Math.sqrt(x);
		};
	},
	
	EXP("EXP",	TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.exp(x);
		};
	},
	
	LN("LN", TokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x) throws InterpreterException{
			if(x<0.0) throw new InterpreterException("Log from negative number");
			return Math.log(x);
		};
	},	
	
	XOR ("XOR",	TokenGroup.ALGEBRA, 9){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)^(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	OR ("OR",	TokenGroup.ALGEBRA, 10){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)||(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	AND ("AND",	TokenGroup.ALGEBRA, 8){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)&&(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	MOD ("MOD",	TokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y){
			return x % y;
		};
	},
	
	POW("**", TokenGroup.ALGEBRA, 1){
		@Override
		public double evalute(double x, double y){
			return Math.pow(x, y);
		};
	},
	
	PLUS("+", TokenGroup.ALGEBRA, 4){
		@Override
		public double evalute(double x, double y){
			return (x+y);
		};
	},
	
	MINUS("-", TokenGroup.ALGEBRA, 4){
		@Override
		public double evalute(double x, double y){
			return (x-y);
		};
	},
	
	MULTIPLY("*", TokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y){
			return (x*y);
		};
	},
	
	DIVIDE("/", TokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y) throws InterpreterException{
			if(y == 0.0) throw new InterpreterException("Divide by zero");
			return (x/y);
		};
	},
	
	G("G", TokenGroup.PARAMETER, -1), // general function
	
	M("M", TokenGroup.PARAMETER, -1), // miscellaneous function
	
	N("N", TokenGroup.COMMAND, -1), // line number
	
	O("O", TokenGroup.COMMAND, -1), // program name
	
	S("S", TokenGroup.PARAMETER, -1), // spindle speed
	
	T("T", TokenGroup.PARAMETER, -1), // tool selection
	
	F("F", TokenGroup.PARAMETER, -1), // feed rate
	
	A("A", TokenGroup.PARAMETER, -1), // A-axis of machine
	
	B("B", TokenGroup.PARAMETER, -1), // B-axis of machine
	
	C("C", TokenGroup.PARAMETER, -1), // C-axis of machine
	
	D("D", TokenGroup.PARAMETER, -1), // tool number in radius compensation 
	
	H("H", TokenGroup.PARAMETER, -1), // tool length offset index
	
	I("I", TokenGroup.PARAMETER, -1), // X-axis offset for arcs, X offset in G87 canned cycle
	
	J("J", TokenGroup.PARAMETER, -1), // Y-axis offset for arcs, Y offset in G87 canned cycle
	
	K("K", TokenGroup.PARAMETER, -1), // Z-axis offset for arcs, Z offset in G87 canned cycle
	
	L("L", TokenGroup.PARAMETER, -1), // number of repetitions in canned cycles
	
	P("P", TokenGroup.PARAMETER, -1), // dwell time in canned cycles,	dwell time with G4,	key used with G10
	
	Q("Q", TokenGroup.PARAMETER, -1), // feed increment in G83 canned cycle
	
	R("R", TokenGroup.PARAMETER, -1), // arc radius, canned cycle plane
	
	U("U", TokenGroup.PARAMETER, -1), // Synonymous with A
	
	V("V", TokenGroup.PARAMETER, -1), // Synonymous with B
	
	W("W", TokenGroup.PARAMETER, -1), // Synonymous with C
	
	X("X", TokenGroup.PARAMETER, -1), // X-axis of machine
	
	Y("Y", TokenGroup.PARAMETER, -1), // Y-axis of machine
	
	Z("Z", TokenGroup.PARAMETER, -1), // Z-axis of machine

	VAR("#", TokenGroup.VARREF, -1),
	
	LEFT_BRACKET("[", TokenGroup.BRACKET, 1),
	
	RIGHT_BRACKET("]", TokenGroup.BRACKET, 1),
	
	ASSIGN("=", TokenGroup.ASSIGNMENT, 13);
	
	private String alfa_;
	private TokenGroup group_;
	private int precedence_; // in order http://en.wikipedia.org/wiki/Order_of_operations
	
	private TokenEnum(String a, TokenGroup g, int p){
		alfa_ = a;
		group_ = g;
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
