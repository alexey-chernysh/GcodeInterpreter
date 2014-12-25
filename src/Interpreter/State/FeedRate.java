package Interpreter.State;

import Interpreter.InterpreterException;
import Interpreter.State.Overrides.OverrideSwitch;

public class FeedRate extends OverrideSwitch {

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

	public void set(double newCurrentFeedRate) throws InterpreterException {
		double fr = InterpreterState.modalState.toMM(newCurrentFeedRate);
		if( fr <= this.max_ ){
			this.current_ = fr;
		} else {
			this.current_ = this.max_;
		}
	}
	
}
