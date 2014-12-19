package Interpreter.State.ModalState;

import Exceptions.GcodeRuntimeException;

public enum McommandSet {
	M0(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program stop
	M1(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Optional program stop
	M2(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program end
	M3(0, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Rotate spindle clockwise
	M4(0, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Rotate spindle counterclckwise
	M5(0, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Stop spindle rotation
	M6(0, McommandModalGroupSet.M_GROUP6_TOOL_CHANGE){}, // Tool change (by two macros)
	M7(0, McommandModalGroupSet.M_GROUP8_COOLANT){}, // Mist coolant on
	M8(0, McommandModalGroupSet.M_GROUP8_COOLANT){}, // Flood coolant on
	M9(0, McommandModalGroupSet.M_GROUP8_COOLANT){}, // All coolant off
	M30(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program end and Rewind
	M47(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Repeat program from first line
	M48(0, McommandModalGroupSet.M_GROUP9_OVERRIDES){}, // Enable speed and feed override
	M49(0, McommandModalGroupSet.M_GROUP9_OVERRIDES){}, // Disable speed and feed override
	M98(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Call subroutine
	M99(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}; // Return from subroutine/repeat
	
	public int number;
	public McommandModalGroupSet modalGroup;
	public void evalute() throws GcodeRuntimeException{
		throw new GcodeRuntimeException("M-command still not implemented");
	};
	
	
	private McommandSet(int n, McommandModalGroupSet g){
		this.number = n;
		this.modalGroup = g;
	};
	
}
