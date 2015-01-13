/*
 * Copyright 2014-2015 Alexey Chernysh
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.Tokens.TokenParameter;
import Interpreter.Motion.Point;

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
		else throw new InterpreterException("Twice parameter " + w.toString() + ";");
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
				try {
					result += " " + TokenParameter.values()[i].toString() + currentExp.evalute();
				} catch (InterpreterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		if(expressionList[i] != null) return expressionList[i].evalute();
		else return 0.0;
	}

	public int getInt(TokenParameter word) throws InterpreterException {
		int i = word.ordinal();
		if(expressionList[i] != null) return expressionList[i].integerEvalute();
		else return 0;
	}
	
	public Point getPoint() throws InterpreterException{
		if(this.hasXYZ()) return new Point(this.get(TokenParameter.X), this.get(TokenParameter.Y));
		else return null;
	}

}
