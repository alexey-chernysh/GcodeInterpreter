package Interpreter.Expression.Tokens;

import Exceptions.GcodeRuntimeException;

public enum TokenEnum {
	
	ABS ("ABS",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.abs(x);
		};
	},
	
	ACOS("ACOS", EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.acos(x)*180.0/Math.PI;
		};
	},
	
	ASIN("ASIN", EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.asin(x)*180.0/Math.PI;
		};
	},
	
	ATAN("ATAN", EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x, double y){
			return Math.atan2(x, y)*180.0/Math.PI;
		};
	},
	
	COS("COS",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.cos(Math.PI*x/180.0);
		};
	},
	
	FIX("FIX",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.floor(x);
		};
	},
	
	FUP("FUP",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return (Math.floor(x)+1.0);
		};
	},
	
	ROUND("ROUND",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.round(x);
		};
	},
	
	SIN("SIN",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.sin(Math.PI*x/180.0);
		};
	},
	
	TAN("TAN",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.tan(Math.PI*x/180.0);
		};
	},
	
	SQRT("SQRT",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x) throws GcodeRuntimeException{
			if(x<0.0) throw new GcodeRuntimeException("Square root from negative number");
			return Math.sqrt(x);
		};
	},
	
	EXP("EXP",	EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x){
			return Math.exp(x);
		};
	},
	
	LN("LN", EnumTokenGroup.FUNCTION, -1){
		@Override
		public double evalute(double x) throws GcodeRuntimeException{
			if(x<0.0) throw new GcodeRuntimeException("Log from negative number");
			return Math.log(x);
		};
	},	
	
	XOR ("XOR",	EnumTokenGroup.ALGEBRA, 9){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)^(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	OR ("OR",	EnumTokenGroup.ALGEBRA, 10){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)||(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	AND ("AND",	EnumTokenGroup.ALGEBRA, 8){
		@Override
		public double evalute(double x, double y){
			if((x != 0.0)&&(y != 0.0)) return 1.0;
			else return 0.0;
		};
	},
	
	MOD ("MOD",	EnumTokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y){
			return x % y;
		};
	},
	
	POW("**", EnumTokenGroup.ALGEBRA, 1){
		@Override
		public double evalute(double x, double y){
			return Math.pow(x, y);
		};
	},
	
	PLUS("+", EnumTokenGroup.ALGEBRA, 4){
		@Override
		public double evalute(double x, double y){
			return (x+y);
		};
	},
	
	MINUS("-", EnumTokenGroup.ALGEBRA, 4){
		@Override
		public double evalute(double x, double y){
			return (x-y);
		};
	},
	
	MULTIPLY("*", EnumTokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y){
			return (x*y);
		};
	},
	
	DIVIDE("/", EnumTokenGroup.ALGEBRA, 3){
		@Override
		public double evalute(double x, double y) throws GcodeRuntimeException{
			if(y == 0.0) throw new GcodeRuntimeException("Divide by zero");
			return (x/y);
		};
	},
	
	G("G", EnumTokenGroup.WORD, -1), // general function
	
	M("M", EnumTokenGroup.WORD, -1), // miscellaneous function
	
	N("N", EnumTokenGroup.WORDINT, -1), // line number
	
	O("O", EnumTokenGroup.WORDINT, -1), // program name
	
	S("S", EnumTokenGroup.WORD, -1), // spindle speed
	
	T("T", EnumTokenGroup.WORD, -1), // tool selection
	
	F("F", EnumTokenGroup.WORD, -1), // feed rate
	
	A("A", EnumTokenGroup.WORD, -1), // A-axis of machine
	
	B("B", EnumTokenGroup.WORD, -1), // B-axis of machine
	
	C("C", EnumTokenGroup.WORD, -1), // C-axis of machine
	
	D("D", EnumTokenGroup.WORD, -1), // tool number in radius compensation 
	
	H("H", EnumTokenGroup.WORD, -1), // tool length offset index
	
	I("I", EnumTokenGroup.WORD, -1), // X-axis offset for arcs, X offset in G87 canned cycle
	
	J("J", EnumTokenGroup.WORD, -1), // Y-axis offset for arcs, Y offset in G87 canned cycle
	
	K("K", EnumTokenGroup.WORD, -1), // Z-axis offset for arcs, Z offset in G87 canned cycle
	
	L("L", EnumTokenGroup.WORD, -1), // number of repetitions in canned cycles
	
	P("P", EnumTokenGroup.WORD, -1), // dwell time in canned cycles,	dwell time with G4,	key used with G10
	
	Q("Q", EnumTokenGroup.WORD, -1), // feed increment in G83 canned cycle
	
	R("R", EnumTokenGroup.WORD, -1), // arc radius, canned cycle plane
	
	U("U", EnumTokenGroup.WORD, -1), // Synonymous with A
	
	V("V", EnumTokenGroup.WORD, -1), // Synonymous with B
	
	W("W", EnumTokenGroup.WORD, -1), // Synonymous with C
	
	X("X", EnumTokenGroup.WORD, -1), // X-axis of machine
	
	Y("Y", EnumTokenGroup.WORD, -1), // Y-axis of machine
	
	Z("Z", EnumTokenGroup.WORD, -1), // Z-axis of machine

//	VARBYINDEX("##", EnumTokenGroup.VARREF, -1),
	
	VAR("#", EnumTokenGroup.VARREF, -1),
	
	LEFT_BRACKET("[", EnumTokenGroup.BRACKET, 1),
	
	RIGHT_BRACKET("]", EnumTokenGroup.BRACKET, 1),
	
	ASSIGN("=", EnumTokenGroup.ASSIGNMENT, 13);
	
	public String alfa_;
	public EnumTokenGroup group_;
	public int precedence_; // in order http://en.wikipedia.org/wiki/Order_of_operations
	
	private TokenEnum(String a, EnumTokenGroup g, int p){
		alfa_ = a;
		group_ = g;
		precedence_ = p;
	}
	
	public double evalute(double x) throws GcodeRuntimeException{
		throw new GcodeRuntimeException("Token interpretation error");
	}	

	public double evalute(double x, double y) throws GcodeRuntimeException{
		throw new GcodeRuntimeException("Token interpretation error");
	}	

	public enum EnumTokenGroup{
		FUNCTION,
		ALGEBRA,
		WORD,
		WORDINT,
		VARREF,
		BRACKET,
		ASSIGNMENT;
	}

}
