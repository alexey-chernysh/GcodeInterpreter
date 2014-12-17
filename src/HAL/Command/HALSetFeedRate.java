package HAL.Command;

public class HALSetFeedRate extends HALCommand {

	private double value_;
	
	public HALSetFeedRate(double feedRate) {
		this.value_ = feedRate;
	}

}
