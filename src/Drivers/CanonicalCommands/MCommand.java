package Drivers.CanonicalCommands;


public class MCommand extends GCommand {
	
	private int num_;
	
	public MCommand(int num){
		num_ = num;
	}

	public int getNum() {
		return num_;
	}

}
