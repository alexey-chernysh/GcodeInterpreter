package Interpreter.Expression.Tokens;

import Interpreter.InterpreterException;

public interface TokenDefaultFields {

	public String getAlfa(); // string format token difinition

	public TokenGroup getGroup(); 

    int getPrecedence(); // precedence for expresion evolution

	public double evalute(double x) throws InterpreterException; // evolution wit one arg

	public double evalute(double x, double y) throws InterpreterException; // evolution wit two arg

}
