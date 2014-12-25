package Drivers.CanonicalCommands;

import Interpreter.InterpreterException;
import Settings.Settings;

public class Velocity {
	
	private VelocityType type_;
	private double value;
	
	public Velocity(double v) throws InterpreterException{
		type_ = VelocityType.OVERRIDES;
		if(v <= 0.0) value = v;
		else throw new InterpreterException("Illegal velocity value");
	}
	
	public Velocity(VelocityType t) throws InterpreterException{
		if(t != VelocityType.OVERRIDES) type_ = t;
		else throw new InterpreterException("Illegal velocity type");
	}
	
	public double getValue(){
		double result = 0.0;
		switch(type_){
		case FREE_RUN:
			result = Settings.getFreeSpeed();
			break;
		case WORK_RUN:
			result = Settings.getWorkSpeed();
			break;
		case OVERRIDES:
			result = value;
			break;
		default:
		}
		return result;
	}
	
	
	public enum VelocityType {
		FREE_RUN,
		WORK_RUN,
		OVERRIDES
	};

}
