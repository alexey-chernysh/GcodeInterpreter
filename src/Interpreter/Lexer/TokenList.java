package Interpreter.Lexer;

import java.util.Iterator;
import java.util.LinkedList;

import Exceptions.LexerException;

public class TokenList extends LinkedList<Token> {
	
	private String sourceLine_;
	private String sourceLineUpperCase_;
	
	public
	TokenList(String frameLine) throws LexerException {
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
	int getNextInt(int index) throws LexerException {
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
					throw new LexerException("Double instead of integer. Integer requred", t.getStart());
				}
			} else {
				throw new LexerException("Integer value ommited", t.getStart());
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
