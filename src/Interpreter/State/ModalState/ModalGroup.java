package Interpreter.State.ModalState;

public enum ModalGroup {
	G_GROUP0,  // G4, G10, G28, G30, G53, G92, G92.1, G92.2, G92.3 non modal group
	G_GROUP1,  // G00, G01, G02, G03, G38.2, G80, G81, G82, G84, G85, G86, G87, G88, G89 motion
	G_GROUP2,  // G17, G18, G19 plane selection
	G_GROUP3,  // G90, G91 distance mode
	G_GROUP5,  // G93, G94 feed rate mode
	G_GROUP6,  // G20, G21 units
	G_GROUP7,  // G40, G41, G42 cutter radius compensation
	G_GROUP8,  // G43, G49 tool length offset
	G_GROUP10, // G98, G99 return mode in canned cycles
	G_GROUP12, // G54, G55, G56, G57, G58, G59, G59.xxx coordinate system selection
	G_GROUP13, // G61, G61.1, G64 path control mode
	M_GROUP4,  // M0, M1, M2, M30 stopping
	M_GROUP6,  // M6 tool change
	M_GROUP7,  // M3, M4, M5 spindle turning
	M_GROUP8,  // M7, M8, M9 coolant (special case: M7 and M8 may be active at the same time)
	M_GROUP9;  // M48, M49 enable/disable feed and speed override controls
}
