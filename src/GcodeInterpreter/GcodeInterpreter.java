package GcodeInterpreter;

import Interpreter.InterpreterException;
import Interpreter.ProgramLoader;


public class GcodeInterpreter {
	
	static ProgramLoader program;

	public static void main(String[] args) {
		try {
			program = new ProgramLoader("D:/Gcode/KKZtest.cnc");
		} catch (InterpreterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
