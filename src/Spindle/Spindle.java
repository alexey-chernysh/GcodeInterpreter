package Spindle;

import InterpreterCommands.CNCOverrideSwitch;

public class Spindle extends CNCOverrideSwitch {

	private double max_ = 2000; 
	private double default_ = 2000; 
	private double current_ = default_; 
	private SpindleDirection state =  SpindleDirection.OFF;
	
	public Spindle(){
		//TODO save & restore
	}
	
	public double getDefaulte() {
		return default_;
	}

	public void setDefault(double defaultSpindleRate) {
		if( defaultSpindleRate <= this.max_ ){
			this.default_ = defaultSpindleRate;
		} else {
			this.default_ = this.max_;
		}
	}

	public double getMax() {
		return max_;
	}

	public void setMax(double maxSpindleRate) {
		this.max_ = maxSpindleRate;
		if( this.default_ > this.max_ ) 
			this.default_ = this.max_;
	}

	public double get() {
		if(this.OverrideEnabled()&&this.isOverride()) return this.default_;
		else return current_;
	}

	public void set(double newCurrentSpindleRate) {
		if( newCurrentSpindleRate <= this.max_ ){
			this.current_ = newCurrentSpindleRate;
		} else {
			this.current_ = this.max_;
		}
	}

	public  SpindleDirection getState() {
		return this.state;
	}

	public void setState(SpindleDirection newState) {
		this.state = newState;
	}

}
