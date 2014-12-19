package Interpreter.State.FeedRate;

import Exceptions.GcodeRuntimeException;
import Interpreter.Motion.MotionControlMode;
import Interpreter.State.InterpreterState;
import Interpreter.State.Overrides.OverrideSwitch;

public class FeedRate extends OverrideSwitch {

	private MotionControlMode motionMode = MotionControlMode.EXACT_STOP;
	private double max_ = 2000; // mm/sec
	private double default_ = 2000; 
	private double current_ = default_;
	
	public FeedRate(){
		//TODO save & restore
	}
	
	public double getDefault() {
		return default_;
	}

	public void setDefault(double defaultFeedRate) {
		if( defaultFeedRate <= this.max_ ){
			this.default_ = defaultFeedRate;
		} else {
			this.default_ = this.max_;
		}
	}

	public double getMax() {
		return max_;
	}

	public void setMax(double maxFeedRate) {
		this.max_ = maxFeedRate;
		if( this.default_ > this.max_ ) 
			this.default_ = this.max_;
	}

	public double get() {
		if(this.OverrideEnabled()&&this.isOverride()) return this.default_;
		else return current_;
	}

	public void set(double newCurrentFeedRate) throws GcodeRuntimeException {
		double fr = InterpreterState.gModalState.toMM(newCurrentFeedRate);
		if( fr <= this.max_ ){
			this.current_ = fr;
		} else {
			this.current_ = this.max_;
		}
	}
	
	public MotionControlMode getMotionMode() { return motionMode; }
	public void setMotionMode(MotionControlMode s) { this.motionMode = s;	}

}
