package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public interface TokenDefaultFields {

	public String getAlfa(); // string format token difinition

	public TokenGroup getGroup(); 

    int getPrecedence(); // precedence for expresion evolution

}
