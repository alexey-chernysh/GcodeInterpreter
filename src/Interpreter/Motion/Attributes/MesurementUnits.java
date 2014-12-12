package Interpreter.Motion.Attributes;

public enum MesurementUnits {
	METRIC(1.0),   // mm
	IMPERIAL(25.2); // inch
	
	public double scale;

	private MesurementUnits(double s){
		scale = s;
	}
}
