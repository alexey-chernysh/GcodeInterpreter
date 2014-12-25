package Interpreter.State.ModalState;

import Interpreter.InterpreterException;
import Interpreter.State.InterpreterState;

public enum McommandSet {
	M0(0, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program stop
	M1(1, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Optional program stop
	M2(2, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program end
	M3(3, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Rotate spindle clockwise
	M4(4, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Rotate spindle counterclckwise
	M5(5, McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING){}, // Stop spindle rotation
	M6(6, McommandModalGroupSet.M_GROUP6_TOOL_CHANGE){}, // Tool change (by two macros)
	M7(7, McommandModalGroupSet.M_GROUP8_COOLANT){}, // Mist coolant on
	M8(8, McommandModalGroupSet.M_GROUP8_COOLANT){}, // Flood coolant on
	M9(9, McommandModalGroupSet.M_GROUP8_COOLANT){}, // All coolant off
	M30(30, McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL){}, // Program end and Rewind
	M47(47, McommandModalGroupSet.M_GROUP0_NON_MODAL){}, // Repeat program from first line
	M48(48, McommandModalGroupSet.M_GROUP9_OVERRIDES){}, // Enable speed and feed override
	M49(49, McommandModalGroupSet.M_GROUP9_OVERRIDES){}, // Disable speed and feed override
	M98(98, McommandModalGroupSet.M_GROUP0_NON_MODAL){}, // Call subroutine
	M99(99, McommandModalGroupSet.M_GROUP0_NON_MODAL){}, // Return from subroutine/repeat
	MDUMMY(99999,McommandModalGroupSet.M_GROUP0_NON_MODAL);
	
	public int number;
	public McommandModalGroupSet modalGroup;
	public void evalute() throws InterpreterException{
		InterpreterState.modalState.set(modalGroup, this);
	};
	
	
	private McommandSet(int n, McommandModalGroupSet g){
		this.number = n;
		this.modalGroup = g;
	};
	
}
