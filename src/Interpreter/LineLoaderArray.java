package Interpreter;

import java.util.ArrayList;

public class LineLoaderArray {

	private int programNum_;  
	private ArrayList<LineLoader> lineArray_ = new ArrayList<LineLoader>();

	public LineLoaderArray(int n){
		programNum_ = n;
	}
	
	public int getProgramNum() {
		return programNum_;
	}

	public void add(LineLoader newBlock){
		lineArray_.add(newBlock);
	}
	
	public void evalute() throws InterpreterException{
		int programSize = this.lineArray_.size();
		for(int i=0; i<programSize; i++){
			LineLoader currentFrame = this.lineArray_.get(i);
			currentFrame.evalute();
			System.out.println(currentFrame.tokenList.getSourceLine());
			System.out.println(currentFrame.toString());
		}
	}

}
