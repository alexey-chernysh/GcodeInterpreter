package Tools;

public class ToolHeight {
	
	private double currentOffset = 0.0;
	private ToolHeightOffset state_ = ToolHeightOffset.OFF;

	public double getOffset() {
		return currentOffset;
	}

	public void setOffset(double newOffset) {
		this.currentOffset = newOffset;
	}
	
	public ToolHeightOffset getState() {
		return state_;
	}

	public void setState(ToolHeightOffset state) {
		this.state_ = state;
	}

	public enum ToolHeightOffset{
		ON,
		OFF,
		UNDEFINED;
	}

}
