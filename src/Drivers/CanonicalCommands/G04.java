package Drivers.CanonicalCommands;

public class G04 extends CanonCommand {

	private double delay_; // milliseconds
	
	public G04(double d){
		this.delay_ = d;
	}

	public double getDelay() {
		return delay_;
	}

}
