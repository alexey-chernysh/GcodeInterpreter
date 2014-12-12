package Interpreter.Motion;

import Exceptions.GcodeRuntimeException;


public class ControlPoint {
	
	private double[] position_ = new double[Axis.n_of_axis];

	public ControlPoint(){
		for(int i=0; i<Axis.n_of_axis; i++)
			position_[i] = 0.0;
		
	}

	public double get(int i) {
		return this.position_[i];
	}
/*
	public double get(char c) throws GcodeRuntimeException {
		return this.position_[Axis.getNumByChar(c)];
	}
*/
	public ControlPoint set(int n, double x) {
		this.position_[n] = x;
		return this;
	}
/*
	public ControlPoint set(char c, double x) throws GcodeRuntimeException {
		this.position_[Axis.getNumByChar(c)] = x;
		return this;
	}
*/
}
