package Interpreter;

import java.util.ArrayList;

import Exceptions.GcodeRuntimeException;

public class RoutineParser {

	private int programNum_;  
	private ArrayList<LineParser> frameList_ = new ArrayList<LineParser>();

	public RoutineParser(int n){
		programNum_ = n;
	}
	
	public int getProgramNum() {
		return programNum_;
	}

	public void add(LineParser newBlock){
		frameList_.add(newBlock);
	}
	
	public void evalute() throws GcodeRuntimeException{
		int programSize = this.frameList_.size();
		for(int i=0; i<programSize; i++){
			LineParser currentFrame = this.frameList_.get(i);
			currentFrame.evalute(this);
			System.out.println(currentFrame.tokenList.getSourceLine());
			System.out.println(currentFrame.toString());
		}
	}

}
