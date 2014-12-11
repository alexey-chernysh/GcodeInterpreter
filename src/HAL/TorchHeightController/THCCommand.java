package HAL.TorchHeightController;

public class THCCommand {
	
	private THCState state_ = THCState.UNDEFINED;

	public THCState getState() {
		return state_;
	}

	public void setState(THCState state_) {
		this.state_ = state_;
	}

}
