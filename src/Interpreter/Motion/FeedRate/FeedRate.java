package Interpreter.Motion.FeedRate;

import Interpreter.Motion.MotionControlMode;
import Interpreter.Overrides.OverrideSwitch;

public class FeedRate extends OverrideSwitch {

	private FeedRateMode feedRateMode_ = FeedRateMode.FEED_PER_MINUTE_MODE;
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

	public void set(double newCurrentFeedRate) {
		if( newCurrentFeedRate <= this.max_ ){
			this.current_ = newCurrentFeedRate;
		} else {
			this.current_ = this.max_;
		}
	}
	
	public FeedRateMode getMode() {	return feedRateMode_; }
	public void setMode(FeedRateMode feedRateMode) { this.feedRateMode_ = feedRateMode;	}
	public MotionControlMode getMotionMode() { return motionMode; }
	public void setMotionMode(MotionControlMode s) { this.motionMode = s;	}

}
