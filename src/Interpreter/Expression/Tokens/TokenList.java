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

package Interpreter.Expression.Tokens;

import java.util.Iterator;
import java.util.LinkedList;

import Interpreter.InterpreterException;

public class TokenList extends LinkedList<Token> {
	
	private String sourceLine_;
	private String sourceLineUpperCase_;
	
	public
	TokenList(String frameLine) throws InterpreterException {
		super();
		
		this.sourceLine_ = frameLine;
		this.sourceLineUpperCase_ = this.sourceLine_.toUpperCase();
		
		if(this.sourceLine_.length() > 0) this.addFirst(new TokenUnlexedText(this.sourceLineUpperCase_, 0, this.sourceLine_.length()-1));
		else this.addFirst(new TokenUnlexedText(this.sourceLineUpperCase_, 0, 0));
	}
	
	protected 
	int addNewToken(Token newToken, int index){
		TokenUnlexedText newHead, newTail;
		Token replaced = this.get(index);
		this.remove(index);
		if(newToken.getStart()>replaced.getStart()){
			newHead = new TokenUnlexedText(this.sourceLineUpperCase_, replaced.getStart(), newToken.getStart()-1);
			this.add(index, newHead);
			index++;
		};
		this.add(index,newToken);
		if(newToken.getEnd()<replaced.getEnd()){
			newTail = new TokenUnlexedText(this.sourceLineUpperCase_, newToken.getEnd()+1, replaced.getEnd());
			this.add(index+1, newTail);
		};
		return index;
	}
	
	protected 
	int getNextToken(int index){
		for(int i=index; i<this.size(); i++){
			Token t = this.get(i);
			if(!(t instanceof TokenComment) ){
				if(!(t instanceof TokenSeparator) ){
					return i;
				}
			}
		}
		return -1;
	}
	
	public 
	int getNextIndex(){
		for(int i=0; i<this.size(); i++){
			Token tmp = this.get(i);
			if(tmp.isSignificant()&&(!tmp.isParsed()))
				return i;
		}
		return this.size();
	}
	
	protected 
	int getNextInt(int index) throws InterpreterException {
		int i = index+1;
		while(i < this.size()){
			Token t = this.get(i);
			if((t instanceof TokenComment) || (t instanceof TokenSeparator)){
				i++;
			} else
			if(t instanceof TokenValue){
				double tmp = ((TokenValue)t).getValue();
				int result = (int)tmp;
				if(Math.abs((double)tmp-result)==0){
					return result;
				} else {
					throw new InterpreterException("Double instead of integer. Integer requred", t.getStart());
				}
			} else {
				throw new InterpreterException("Integer value ommited", t.getStart());
			}
		}
		return -1;
	}
	
	public 
	String getSourceLine(){
		return this.sourceLine_;
	};
	
	public 
	String getSourceLineUpperCase(){
		return this.sourceLineUpperCase_;
	};
	
	public 
	void printAllTokens(){
		Iterator<Token> itr =  this.iterator(); 
		while(itr.hasNext())
			itr.next().printLine();
	}
}
