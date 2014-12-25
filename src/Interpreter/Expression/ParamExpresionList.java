package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.CommandPair.CNCWordEnum;

public class ParamExpresionList {
	
	private static final int size_ = CNCWordEnum.Z.ordinal() + 1;
	private ExpressionGeneral[] expressionList = new ExpressionGeneral[size_];
	
	public
	ParamExpresionList(){
		for(int i=0; i<size_; i++) {
			expressionList[i] = null;
		}
	};

	public void addWord(CNCWordEnum w, ExpressionGeneral e) throws InterpreterException{ 
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
				result += CNCWordEnum.values()[i].toString() + " " + currentExp.toString();
			};
		}
		
		return result;
	}

	public boolean has(CNCWordEnum word){
		if(expressionList[word.ordinal()] != null) return true;
		else return false;
	}
	
	public boolean hasXYZ() {
		return (has(CNCWordEnum.X)||has(CNCWordEnum.Y)||has(CNCWordEnum.Z));
	}

	public double get(CNCWordEnum word) throws InterpreterException {
		int i = word.ordinal();
		if(expressionList[i] != null){
			return expressionList[i].evalute();
		}
		else return 0.0;
	}

}
