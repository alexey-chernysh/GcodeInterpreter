package CNCExpression;

import CNCExpression.CNCWord.CNCWordEnum;
import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.Lexer.Token;
import Interpreter.Lexer.TokenAlfa;
import Interpreter.Lexer.TokenComment;
import Interpreter.Lexer.TokenEnum;
import Interpreter.Lexer.TokenList;
import Interpreter.Lexer.TokenSequence;
import Interpreter.Lexer.TokenValue;
import Interpreter.Lexer.TokenEnum.EnumTokenGroup;

public class CNCCommandSequence extends TokenSequence {

	protected CNCWordList wordList_ = new CNCWordList();
	protected CNCCommandSet commandSet_ = new CNCCommandSet();
	protected CNCVarAssignmentSet varAssignmentSet_ = new CNCVarAssignmentSet();
	private String messageString_ = null;
	private int lineNum_ = 0;
	private int programNum_;

	public CNCCommandSequence(String frameString, int programNum) throws LexerException, GcodeRuntimeException {
		super(frameString);
		this.programNum_ = programNum;
		this.tokenList.printAllTokens();

		// split tokens in hardware's command sequence
		int index = this.tokenList.getNextIndex();
		while (index < this.tokenList.size()) {
			Token t = this.tokenList.get(index).setParsed();
			if(t instanceof TokenAlfa){
				CNCExpGeneral numExp = getItem(this.tokenList);
				TokenEnum currentWord = ((TokenAlfa)t).getType();
				switch( currentWord.group_ ){
					case WORD:
						CNCWord newWord = new CNCWord(currentWord, numExp);
						switch( currentWord ){
						case A:
							this.wordList_.addWord(CNCWordEnum.A, newWord);
							break;
						case B:
							this.wordList_.addWord(CNCWordEnum.B, newWord);
							break;
						case C:
							this.wordList_.addWord(CNCWordEnum.C, newWord);
							break;
						case D:
							this.wordList_.addWord(CNCWordEnum.D, newWord);
							break;
						case F:
							this.commandSet_.addCommand(newWord);
							break;
						case G:
							this.commandSet_.addCommand(newWord);
							break;
						case H:
							this.wordList_.addWord(CNCWordEnum.H, newWord);
							break;
						case I:
							this.wordList_.addWord(CNCWordEnum.I, newWord);
							break;
						case J:
							this.wordList_.addWord(CNCWordEnum.J, newWord);
							break;
						case K:
							this.wordList_.addWord(CNCWordEnum.K, newWord);
							break;
						case L:
							this.wordList_.addWord(CNCWordEnum.L, newWord);
							break;
						case M:
							this.commandSet_.addCommand(newWord);
							break;
						case P:
							this.wordList_.addWord(CNCWordEnum.P, newWord);
							break;
						case Q:
							this.wordList_.addWord(CNCWordEnum.Q, newWord);
							break;
						case R:
							this.wordList_.addWord(CNCWordEnum.R, newWord);
							break;
						case S:
							this.commandSet_.addCommand(newWord);
							break;
						case T:
							this.commandSet_.addCommand(newWord);
							break;
						case U:
							this.wordList_.addWord(CNCWordEnum.A, newWord);
							break;
						case V:
							this.wordList_.addWord(CNCWordEnum.B, newWord);
							break;
						case W:
							this.wordList_.addWord(CNCWordEnum.C, newWord);
							break;
						case X:
							this.wordList_.addWord(CNCWordEnum.X, newWord);
							break;
						case Y:
							this.wordList_.addWord(CNCWordEnum.Y, newWord);
							break;
						case Z:
							this.wordList_.addWord(CNCWordEnum.Z, newWord);
							break;
						default:
							throw new LexerException("Unsupported word", t.getStart());
						}
						break;
					case WORDINT:
						switch( currentWord ){
						case N:
							this.lineNum_ = numExp.integerEvalute();
							break;
						case O:
							this.programNum_ = numExp.integerEvalute();
							break;
						default:
						}
						break;
					case VARREF:
						switch( currentWord ){
						case VAR:
							t = this.tokenList.get(this.tokenList.getNextIndex()).setParsed();
							if(((TokenAlfa)t).getType() == TokenEnum.ASSIGN){
								CNCExpGeneral varValueExp = getItem(this.tokenList);
								CNCVarAssignment newVarAssign = new CNCVarAssignment(numExp, varValueExp);
								this.varAssignmentSet_.add(newVarAssign);
							} else throw new LexerException("Asigment symbol '=' needed", t.getStart());
							break;
						default:
						}
						break;
					default:
						throw new LexerException("Bad command", t.getStart());
				};
			}
			else {
				throw new LexerException("Bad command", t.getStart());
			}
			index = this.tokenList.getNextIndex();
		};

		// check block for comments
		for(int i=0; i<this.tokenList.size();i++){
			Token tkn = this.tokenList.get(i);
			if( tkn instanceof TokenComment)
				this.messageString_ = ((TokenComment)tkn).getMessage();
		}
	}
	
	private CNCExpGeneral getExpression(TokenList list) throws LexerException {
		// left bracket occurred, so parse token list for Expression until right bracket
		CNCExpGeneral item1 = getItem(list);
		TokenEnum Operator1 = ((TokenAlfa)list.get(list.getNextIndex()).setParsed()).getType();
		if(Operator1.group_ == EnumTokenGroup.ALGEBRA){
			CNCExpGeneral item2 = getItem(list);
			return getSubExpression(list, Operator1, item1, item2);
		} else {
			if(Operator1 == TokenEnum.RIGHT_BRACKET){
				return item1;
			} else throw new LexerException("Binary operator needed", 0);
		}
	}

	private CNCExpGeneral getSubExpression(TokenList list,
									   	   TokenEnum Operator1, 
									   	   CNCExpGeneral item1, 
									   	   CNCExpGeneral item2) throws LexerException {
		TokenEnum Operator2 = ((TokenAlfa)list.get(list.getNextIndex()).setParsed()).getType();
		if(Operator2.group_ == EnumTokenGroup.ALGEBRA){
			CNCExpGeneral item3 = getItem(list);
			if(Operator1.precedence_ <= Operator2.precedence_){
				return getSubExpression(list, Operator2, new CNCExpFunction(Operator1, item1, item2), item3);
			} else {
				return new CNCExpFunction(Operator1, item1, getSubExpression(list, Operator2, item2, item3));
			}
		} else {
			if(Operator2 == TokenEnum.RIGHT_BRACKET){
				return new CNCExpFunction(Operator1, item1, item2);
			} else throw new LexerException("Binary operator needed", 0);
		}
	}
	
	private CNCExpGeneral getItem(TokenList list) throws LexerException {
		// parse token list for expressions and return next unparsed token index
		CNCExpGeneral result = null;
		int index = list.getNextIndex();
		if(index < list.size()){
			Token t = list.get(index);
			t.setParsed();
			if(t instanceof TokenValue){
				// if const is last item in expression, then return it as result
				result = new CNCExpValue(((TokenValue)t).getValue());
				return result;
			} else {
				switch(((TokenAlfa)t).getType().group_){
					case FUNCTION:
						TokenEnum funType = ((TokenAlfa)t).getType();
						index = list.getNextIndex();
						if(index < list.size()){
							t = list.get(index);
							t.setParsed();
							CNCExpGeneral funArg1 = null;
							if((t instanceof TokenAlfa)&&(((TokenAlfa)t).getType() == TokenEnum.LEFT_BRACKET)){
								funArg1 = this.getExpression(list);
							} else throw new LexerException("Left bracket '[' requred", t.getStart());
							if(funType != TokenEnum.ATAN){
								result = new CNCExpFunction(funType, funArg1);
								return result;
							}else{
								index = list.getNextIndex();
								if(index < list.size()){
									t = list.get(index);
									t.setParsed();
									if((t instanceof TokenAlfa)&&(((TokenAlfa)t).getType() == TokenEnum.DIVIDE)){
										index = list.getNextIndex();
										if(index < list.size()){
											t = list.get(index);
											t.setParsed();
											if((t instanceof TokenAlfa)&&(((TokenAlfa)t).getType() == TokenEnum.LEFT_BRACKET)){
												CNCExpGeneral funArg2 = this.getExpression(list);
												result = new CNCExpFunction(funType, funArg1, funArg2);
												return result;
											} else throw new LexerException("Left bracket '[' requred", t.getStart());
										}else throw new LexerException("Unxpected end of frame string", t.getStart());
									} else throw new LexerException("Slash symnol '/' requred", t.getStart());
								}else throw new LexerException("Unxpected end of frame string", t.getStart());
							}
						} else throw new LexerException("Unxpected end of frame string", t.getStart());
					case ALGEBRA: // unary sign
						switch(((TokenAlfa)t).getType()){ // unary operations + -
							case MINUS: // sign -, substitute as expression (0 - x)
								CNCExpGeneral nextItem = this.getItem(list);
								CNCExpValue  zero = new CNCExpValue(0.0);
								result = (CNCExpGeneral) new CNCExpAlgebra(TokenEnum.MINUS, zero, nextItem);
								return result;
							case PLUS: // sign +, nothing to do
								result = this.getItem(list);
								return result;
							default:
								throw new LexerException("Unsupported unary operation", t.getStart());
						}
					case VARREF:
						switch(((TokenAlfa)t).getType()){ // reference to variable value
							case VAR: 
								CNCExpGeneral varNum = this.getItem(list);
								result = (CNCExpGeneral) new CNCExpVariable(varNum);
								return result;
							default:
								throw new LexerException("Unsupported var operation", t.getStart());
						}
					case BRACKET:
						switch(((TokenAlfa)t).getType()){ // in-bracket expression
							case LEFT_BRACKET: 
								result = this.getExpression(list);
								return result;
							default:
								throw new LexerException("Unsupported block structure", t.getStart());
						}
					default:
						this.tokenList.printAllTokens();
						throw new LexerException("Illegal expression syntax", t.getStart());
				}
			}
		} else throw new LexerException("Unxpected end of block string", 0);
	}

	@Override
	public String toString(){

		String result = this.commandSet_.toString() 
					  + this.wordList_.toString() 
					  + this.varAssignmentSet_.toString();
		if(messageString_ != null) result += "MESSAGE:" + messageString_;
		return result;
	}

	public String getMessageString() {
		return messageString_;
	}

	public int getProgramNum() {
		return programNum_;
	}

	public int getLineNum() {
		return lineNum_;
	}

}