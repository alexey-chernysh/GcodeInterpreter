package Drivers;

import java.util.ArrayList;

import Drivers.CanonicalCommands.GCommand;
import Interpreter.InterpreterException;

public interface GeneralDriver {
	
	void loadProgram(ArrayList<GCommand> sourceCommands) throws InterpreterException; 
	
	void startProgram(); 
	
	void pauseProgram();
	
	void resumeProgram();
	
	void rewindProgram();
	
	void forewindProgram();

}
