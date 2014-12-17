package GcodeInterpreter;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.ProgramLoader;


public class GcodeInterpreter {
	
	static ProgramLoader program;

	public static void main(String[] args) {
		try {
			program = new ProgramLoader("D:/Gcode/KKZtest.cnc");
		} catch (LexerException | GcodeRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
