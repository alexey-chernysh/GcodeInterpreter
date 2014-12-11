package Tools;

public class ToolSet {
	
	private int currentTool_ = 0;
	private int selectedTool_ = currentTool_;
	
	public void selectTool(int num){
		this.selectedTool_ = num;
	}

	public int getCurrentTool() {
		return currentTool_;
	}

	public void setCurrentTool(int num) {
		this.currentTool_ = num;
	}
	
	public void changeTool(){
		// TODO perform offset change
	}

	public double getMaxToolNum() {
		// TODO Auto-generated method stub
		return 255;
	}

	public double getToolRadius(int toolNum) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	public double getToolHeight(int toolNum) {
		// TODO Auto-generated method stub
		return 0.0;
	}
	
}
