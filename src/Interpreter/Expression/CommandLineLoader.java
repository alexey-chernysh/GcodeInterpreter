package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.CommandPair.CNCWordEnum;
import Interpreter.Expression.Tokens.Token;
import Interpreter.Expression.Tokens.TokenAlfa;
import Interpreter.Expression.Tokens.TokenComment;
import Interpreter.Expression.Tokens.TokenEnum;
import Interpreter.Expression.Tokens.TokenList;
import Interpreter.Expression.Tokens.TokenSequence;
import Interpreter.Expression.Tokens.TokenValue;
import Interpreter.Expression.Tokens.TokenEnum.EnumTokenGroup;
import Interpreter.Expression.Variables.ExpressionVarAssignment;
import Interpreter.Expression.Variables.ExpressionVariable;
import Interpreter.Expression.Variables.VarAssignmentList;

public class CommandLineLoader extends TokenSequence {

	protected ParamExpresionList wordList_ = new ParamExpresionList();
	protected CommandPairList commandSet_ = new CommandPairList();
	protected VarAssignmentList varAssignmentSet_ = new VarAssignmentList();
	private String messageString_ = null;
	private int lineNum_ = 0;
	private int programNum_;

	public CommandLineLoader(String frameString, int programNum) throws InterpreterException {
		super(frameString);
		this.programNum_ = programNum;
		this.tokenList.printAllTokens();

		// split tokens in hardware's command sequence
		int index = this.tokenList.getNextIndex();
		while (index < this.tokenList.size()) {
			Token t = this.tokenList.get(index).setParsed();
			if(t instanceof TokenAlfa){
				ExpressionGeneral numExp = getItem(this.tokenList);
				TokenEnum currentWord = ((TokenAlfa)t).getType();
				switch( currentWord.group_ ){
					case WORD:
						CommandPair newWord = new CommandPair(currentWord, numExp);
						switch( currentWord ){
						case A:
							this.wordList_.addWord(CNCWordEnum.A, numExp);
							break;
						case B:
							this.wordList_.addWord(CNCWordEnum.B, numExp);
							break;
						case C:
							this.wordList_.addWord(CNCWordEnum.C, numExp);
							break;
						case D:
							this.wordList_.addWord(CNCWordEnum.D, numExp);
							break;
						case F:
							this.commandSet_.addCommand(newWord);
							break;
						case G:
							this.commandSet_.addCommand(newWord);
							break;
						case H:
							this.wordList_.addWord(CNCWordEnum.H, numExp);
							break;
						case I:
							this.wordList_.addWord(CNCWordEnum.I, numExp);
							break;
						case J:
							this.wordList_.addWord(CNCWordEnum.J, numExp);
							break;
						case K:
							this.wordList_.addWord(CNCWordEnum.K, numExp);
							break;
						case L:
							this.wordList_.addWord(CNCWordEnum.L, numExp);
							break;
						case M:
							this.commandSet_.addCommand(newWord);
							break;
						case P:
							this.wordList_.addWord(CNCWordEnum.P, numExp);
							break;
						case Q:
							this.wordList_.addWord(CNCWordEnum.Q, numExp);
							break;
						case R:
							this.wordList_.addWord(CNCWordEnum.R, numExp);
							break;
						case S:
							this.commandSet_.addCommand(newWord);
							break;
						case T:
							this.commandSet_.addCommand(newWord);
							break;
						case U:
							this.wordList_.addWord(CNCWordEnum.A, numExp);
							break;
						case V:
							this.wordList_.addWord(CNCWordEnum.B, numExp);
							break;
						case W:
							this.wordList_.addWord(CNCWordEnum.C, numExp);
							break;
						case X:
							this.wordList_.addWord(CNCWordEnum.X, numExp);
							break;
						case Y:
							this.wordList_.addWord(CNCWordEnum.Y, numExp);
							break;
						case Z:
							this.wordList_.addWord(CNCWordEnum.Z, numExp);
							break;
						default:
							throw new InterpreterException("Unsupported word", t.getStart());
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
								ExpressionGeneral varValueExp = getItem(this.tokenList);
								ExpressionVarAssignment newVarAssign = new ExpressionVarAssignment(numExp, varValueExp);
								this.varAssignmentSet_.add(newVarAssign);
							} else throw new InterpreterException("Asigment symbol '=' needed", t.getStart());
							break;
						default:
						}
						break;
					default:
						throw new InterpreterException("Bad command", t.getStart());
				};
			}
			else {
				throw new InterpreterException("Bad command", t.getStart());
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
	
	private ExpressionGeneral getExpression(TokenList list) throws InterpreterException {
		// left bracket occurred, so parse token list for Expression until right bracket
		ExpressionGeneral item1 = getItem(list);
		TokenEnum Operator1 = ((TokenAlfa)list.get(list.getNextIndex()).setParsed()).getType();
		if(Operator1.group_ == EnumTokenGroup.ALGEBRA){
			ExpressionGeneral item2 = getItem(list);
			return getSubExpression(list, Operator1, item1, item2);
		} else {
			if(Operator1 == TokenEnum.RIGHT_BRACKET){
				return item1;
			} else throw new InterpreterException("Binary operator needed", 0);
		}
	}

	private ExpressionGeneral getSubExpression(TokenList list,
									   	   TokenEnum Operator1, 
									   	   ExpressionGeneral item1, 
									   	   ExpressionGeneral item2) throws InterpreterException {
		TokenEnum Operator2 = ((TokenAlfa)list.get(list.getNextIndex()).setParsed()).getType();
		if(Operator2.group_ == EnumTokenGroup.ALGEBRA){
			ExpressionGeneral item3 = getItem(list);
			if(Operator1.precedence_ <= Operator2.precedence_){
				return getSubExpression(list, Operator2, new ExpressionFunction(Operator1, item1, item2), item3);
			} else {
				return new ExpressionFunction(Operator1, item1, getSubExpression(list, Operator2, item2, item3));
			}
		} else {
			if(Operator2 == TokenEnum.RIGHT_BRACKET){
				return new ExpressionFunction(Operator1, item1, item2);
			} else throw new InterpreterException("Binary operator needed", 0);
		}
	}
	
	private ExpressionGeneral getItem(TokenList list) throws InterpreterException {
		// parse token list for expressions and return next unparsed token index
		ExpressionGeneral result = null;
		int index = list.getNextIndex();
		if(index < list.size()){
			Token t = list.get(index);
			t.setParsed();
			if(t instanceof TokenValue){
				// if const is last item in expression, then return it as result
				result = new ExpressionValue(((TokenValue)t).getValue());
				return result;
			} else {
				switch(((TokenAlfa)t).getType().group_){
					case FUNCTION:
						TokenEnum funType = ((TokenAlfa)t).getType();
						index = list.getNextIndex();
						if(index < list.size()){
							t = list.get(index);
							t.setParsed();
							ExpressionGeneral funArg1 = null;
							if((t instanceof TokenAlfa)&&(((TokenAlfa)t).getType() == TokenEnum.LEFT_BRACKET)){
								funArg1 = this.getExpression(list);
							} else throw new InterpreterException("Left bracket '[' requred", t.getStart());
							if(funType != TokenEnum.ATAN){
								result = new ExpressionFunction(funType, funArg1);
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
												ExpressionGeneral funArg2 = this.getExpression(list);
												result = new ExpressionFunction(funType, funArg1, funArg2);
												return result;
											} else throw new InterpreterException("Left bracket '[' requred", t.getStart());
										}else throw new InterpreterException("Unxpected end of frame string", t.getStart());
									} else throw new InterpreterException("Slash symnol '/' requred", t.getStart());
								}else throw new InterpreterException("Unxpected end of frame string", t.getStart());
							}
						} else throw new InterpreterException("Unxpected end of frame string", t.getStart());
					case ALGEBRA: // unary sign
						switch(((TokenAlfa)t).getType()){ // unary operations + -
							case MINUS: // sign -, substitute as expression (0 - x)
								ExpressionGeneral nextItem = this.getItem(list);
								ExpressionValue  zero = new ExpressionValue(0.0);
								result = (ExpressionGeneral) new ExpressionAlgebra(TokenEnum.MINUS, zero, nextItem);
								return result;
							case PLUS: // sign +, nothing to do
								result = this.getItem(list);
								return result;
							default:
								throw new InterpreterException("Unsupported unary operation", t.getStart());
						}
					case VARREF:
						switch(((TokenAlfa)t).getType()){ // reference to variable value
							case VAR: 
								ExpressionGeneral varNum = this.getItem(list);
								result = (ExpressionGeneral) new ExpressionVariable(varNum);
								return result;
							default:
								throw new InterpreterException("Unsupported var operation", t.getStart());
						}
					case BRACKET:
						switch(((TokenAlfa)t).getType()){ // in-bracket expression
							case LEFT_BRACKET: 
								result = this.getExpression(list);
								return result;
							default:
								throw new InterpreterException("Unsupported block structure", t.getStart());
						}
					default:
						this.tokenList.printAllTokens();
						throw new InterpreterException("Illegal expression syntax", t.getStart());
				}
			}
		} else throw new InterpreterException("Unxpected end of block string", 0);
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