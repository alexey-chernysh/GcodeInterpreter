package Interpreter.State.ModalState;

public enum McommandModalGroupSet {
	M_GROUP4,  // M0, M1, M2, M30 stopping
	M_GROUP6,  // M6 tool change
	M_GROUP7,  // M3, M4, M5 spindle turning
	M_GROUP8,  // M7, M8, M9 coolant (special case: M7 and M8 may be active at the same time)
	M_GROUP9;  // M48, M49 enable/disable feed and speed override controls
}
