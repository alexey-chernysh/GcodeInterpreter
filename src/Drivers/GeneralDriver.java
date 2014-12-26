package Drivers;

import java.util.ArrayList;

import Drivers.CanonicalCommands.CanonCommand;
import Interpreter.InterpreterException;

public interface GeneralDriver {
	
	void loadProgram(ArrayList<CanonCommand> sourceCommands) throws InterpreterException; 
	
	void startProgram(); 
	
	void pauseProgram();
	
	void resumeProgram();
	
	void rewindProgram();
	
	void forewindProgram();

}
