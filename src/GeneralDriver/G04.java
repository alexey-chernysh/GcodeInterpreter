package GeneralDriver;


public class G04 extends GCommand {

	private long delay_; // milliseconds
	
	public G04(long d){
		this.delay_ = d;
	}

	public long getDelay() {
		return delay_;
	}

}
