package Interpreter.CannedCycle;


public class CannedCycleReturnMode {
	
	private double R_ = 0.0;
	private ReturnMode currentMode_ = ReturnMode.UNDEFINED;

	public double getR() {
		return this.R_;
	}

	public void setR(double r) {
		this.R_ = r;
	}

	public ReturnMode getMode() {
		return this.currentMode_;
	}

	public void setMode(ReturnMode returnMode_) {
		this.currentMode_ = returnMode_;
	}
}
