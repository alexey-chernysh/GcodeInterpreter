package GeneralDriver;

import java.util.ArrayList;

import Exceptions.HWCException;

public interface GeneralDriver {
	
	void loadProgram(ArrayList<GCommand> sourceCommands) throws HWCException; 
	
	void startProgram(); 
	
	void pauseProgram();
	
	void resumeProgram();
	
	void rewindProgram();
	
	void forewindProgram();

}
