package Interpreter.Motion.Attributes;

public class LengthUnits {
	
	private Units current_ = Units.METRIC;

	public double toMM(double v) {
		return (this.current_.scale * v);
	};
	
	public void set(Units s) { 
		this.current_ = s; 
	}
/*	
	public double getScale(){ 
		return this.current_.scale; 
	}
*/
	@Override
	public String toString(){ 
		return this.current_.name; 
	}

	public enum Units {
		METRIC(1.0, "METRIC"),
		IMPERIAL(25.4, "INCHES"),
		UNDEFINED(0.0, "UNDEFINED");
		
		private double scale;
		private String name;
		
		private Units(double s, String n){
			scale = s;
			name  = n;
		}
	}

}
