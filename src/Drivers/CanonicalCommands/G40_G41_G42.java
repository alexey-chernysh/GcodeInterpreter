package Drivers.CanonicalCommands;


public class G40_G41_G42 extends CanonCommand {

	private OffsetMode mode_;
	
	public G40_G41_G42(OffsetMode newMode){
		this.mode_ = newMode;
	}
	
	public OffsetMode getMode() {
		return mode_;
	}

}
