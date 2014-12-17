package GcodeInterpreter;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.ProgramParser;


public class GcodeInterpreter {
	
	static ProgramParser program;

	public static void main(String[] args) {
		try {
			program = new ProgramParser("D:/Gcode/KKZtest.cnc");
		} catch (LexerException | GcodeRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
