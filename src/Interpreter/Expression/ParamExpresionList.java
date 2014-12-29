package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.Tokens.TokenParameter;

public class ParamExpresionList {
	
	private static final int size_ = TokenParameter.Z.ordinal() + 1;
	private ExpressionGeneral[] expressionList = new ExpressionGeneral[size_];
	
	public
	ParamExpresionList(){
		for(int i=0; i<size_; i++) {
			expressionList[i] = null;
		}
	};

	public void addWord(TokenParameter w, ExpressionGeneral e) throws InterpreterException{ 
		int n = w.ordinal();
		if(expressionList[n] == null) expressionList[n] = e;
		else throw new InterpreterException("Twice parameter");
	}

	public int getLength() {
		return size_;
	}

	public ExpressionGeneral get(int i){
		return expressionList[i];
	}

	@Override
	public String toString(){
		String result = "";
		
		for(int i=0; i<ParamExpresionList.size_; i++){
			ExpressionGeneral currentExp = this.expressionList[i];
			if(currentExp != null){
				result += TokenParameter.values()[i].toString() + " " + currentExp.toString();
			};
		}
		
		return result;
	}

	public boolean has(TokenParameter word){
		if(expressionList[word.ordinal()] != null) return true;
		else return false;
	}
	
	public boolean hasXYZ() {
		return (has(TokenParameter.X)||has(TokenParameter.Y)||has(TokenParameter.Z));
	}

	public double get(TokenParameter word) throws InterpreterException {
		int i = word.ordinal();
		if(expressionList[i] != null){
			return expressionList[i].evalute();
		}
		else return 0.0;
	}

	public int getInt(TokenParameter word) throws InterpreterException {
		int i = word.ordinal();
		if(expressionList[i] != null){
			return expressionList[i].integerEvalute();
		}
		else return 0;
	}

}
