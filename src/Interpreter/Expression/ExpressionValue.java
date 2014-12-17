package Interpreter.Expression;


public class ExpressionValue extends ExpressionGeneral {
	
	private double value_;
	
	public ExpressionValue(double v){
		value_ = v;
		this.setConstant(true);
	}

	@Override
	public double evalute(){
		return value_;
	}
	
}
