package GcodeInterpreter;

import CNCProgram.CNCProgram;
import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;


public class GcodeInterpreter {
	
	static CNCProgram program;

	public static void main(String[] args) {
		try {
			program = new CNCProgram("D:/Gcode/KKZtest.cnc");
		} catch (LexerException | GcodeRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
