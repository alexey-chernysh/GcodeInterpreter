package Interpreter;

import java.util.ArrayList;

public class ProgramModule {
	
	private int startLine_ = -1;
	private int endLine_ = -1;
	public final int num;
	private ArrayList<LineLoader> programBody_;
	
	public ProgramModule(int n, ArrayList<LineLoader> lineArray){
		this.num = n;
		programBody_ = lineArray;
	}
	
	public void setStart(int sl){
		this.startLine_ = sl;
	}

	public void setEnd(int el){
		this.endLine_ = el;
	}
	
	public void evalute() throws InterpreterException{
		if((this.startLine_ >= 0) && (this.endLine_ >= startLine_)){
			for(int i=this.startLine_; i<=this.endLine_; i++)
				programBody_.get(i).evalute();
		} else throw new InterpreterException("Call of not initialized module!");
	}

}
