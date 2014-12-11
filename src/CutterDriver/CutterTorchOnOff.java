package CutterDriver;

public class CutterTorchOnOff {

	private boolean state;
	
	public CutterTorchOnOff(boolean s){
		state = s;
	}

	public boolean isOn() {
		return state;
	}

}
