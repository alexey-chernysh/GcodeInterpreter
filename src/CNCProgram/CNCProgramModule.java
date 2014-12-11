package CNCProgram;

import java.util.ArrayList;

import Exceptions.GcodeRuntimeException;

public class CNCProgramModule {

	private int programNum_ = 0;  // "main" program
	private ArrayList<CNCProgramFrame> frameList_ = new ArrayList<CNCProgramFrame>();

	public CNCProgramModule(int n){
		programNum_ = n;
	}
	
	public int getProgramNum() {
		return programNum_;
	}

	public void add(CNCProgramFrame newBlock){
		frameList_.add(newBlock);
	}
	
	public void execute() throws GcodeRuntimeException{
		int programSize = this.frameList_.size();
		for(int i=0; i<programSize; i++){
			CNCProgramFrame currentFrame = this.frameList_.get(i);
			currentFrame.evalute(this);
			System.out.println(currentFrame.tokenList.getSourceLine());
			System.out.println(currentFrame.toString());
		}
	}

}
