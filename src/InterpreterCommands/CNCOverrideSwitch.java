package InterpreterCommands;

public class CNCOverrideSwitch {

	private boolean override_ = false; 
	private boolean overrideEnabled_ = false;

	public boolean isOverride() {
		return override_;
	}

	public void setOverride(boolean feedRateOverride) {
		this.override_ = feedRateOverride;
	}

	public boolean OverrideEnabled() {
		return overrideEnabled_;
	}

	public void enableOverride() {
		this.overrideEnabled_ = true;
	}

	public void disableOverride() {
		this.overrideEnabled_ = false;
	}

	public enum Overrides{
		ON, 
		OFF,
		UNDEFINED;
	}
	
}
