package Interpreter;

import java.util.ArrayList;

public class LineLoaderArray {

	private int programNum_;  
	private ArrayList<LineLoader> frameList_ = new ArrayList<LineLoader>();

	public LineLoaderArray(int n){
		programNum_ = n;
	}
	
	public int getProgramNum() {
		return programNum_;
	}

	public void add(LineLoader newBlock){
		frameList_.add(newBlock);
	}
	
	public void evalute() throws InterpreterException{
		int programSize = this.frameList_.size();
		for(int i=0; i<programSize; i++){
			LineLoader currentFrame = this.frameList_.get(i);
			currentFrame.evalute();
			System.out.println(currentFrame.tokenList.getSourceLine());
			System.out.println(currentFrame.toString());
		}
	}

}
