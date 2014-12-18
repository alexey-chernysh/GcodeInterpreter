package Interpreter.State.Tools;

public class ToolRadius {
	
	private Compensation state = Compensation.OFF;
	private  double radius = 0.0;
	
	public enum Compensation{
		OFF,
		LEFT,
		RIGHT,
		UNDEFINED;
	}

	public Compensation getState() {
		return state;
	}

	public void setState(Compensation state) {
		this.state = state;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean compensationIsOn() {
		if((this.state == Compensation.LEFT)||(this.state == Compensation.RIGHT)) return true;
		else return false;
	}

}
