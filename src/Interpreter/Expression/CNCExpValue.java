package Interpreter.Expression;


public class CNCExpValue extends CNCExpGeneral {
	
	private double value_;
	
	public CNCExpValue(double v){
		value_ = v;
		this.setConstant(true);
	}

	@Override
	public double evalute(){
		return value_;
	}
	
}
