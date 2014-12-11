package CNCExpression;

import java.util.ArrayList;

public class CNCCommandSet extends ArrayList<CNCWord> {
	
	public void addCommand(CNCWord e){
		this.add(e);
	}

	@Override
	public String toString(){
		String result = "";
		
		for(int i=0; i<this.size(); i++){
			CNCWord currentCommand = this.get(i);
			result += " " + currentCommand.toString();
		}
		
		return result;
	}

	public enum CNCCommandEnum {

		F(		1,		ModalGroupEnum.FEED_RATE), // set feed rate
		G0(		0,		ModalGroupEnum.MOTION), // rapid positioning
		G1(		1,		ModalGroupEnum.MOTION), // linear interpolation
		G2(		2,		ModalGroupEnum.MOTION), // circular/helical interpolation (clockwise), I, J, K center coordinates
		G3(		3,		ModalGroupEnum.MOTION), // circular/helical interpolation (counterclockwise), I, J, K center coordinates
		G4(		4,		ModalGroupEnum.NON_MODAL), // dwell
		G10(	10,		ModalGroupEnum.NON_MODAL), // coordinate system origin setting
		G12(	12,		ModalGroupEnum.NON_MODAL), // Clockwise circular pocket
		G13(	13,		ModalGroupEnum.NON_MODAL), // Counterclockwise circular pocket
		G15(	15,		ModalGroupEnum.NON_MODAL), // Cartesian Coordinate mode - default 
		G16(	16,		ModalGroupEnum.NON_MODAL), // Polar Coordinate moves in G0 and G1
		G17(	17,		ModalGroupEnum.PLANE_SELECTION), // XY-plane selection
		G18(	18,		ModalGroupEnum.PLANE_SELECTION), // XZ-plane selection
		G19(	19,		ModalGroupEnum.PLANE_SELECTION), // YZ-plane selection
		G20(	20,		ModalGroupEnum.UNITS),  // imperial (inch) system selection
		G21(	21,		ModalGroupEnum.UNITS),  // metric (millimeter) system selection
		G28(	28,		ModalGroupEnum.NON_MODAL), // return to home
		G28_1(	28.1,	ModalGroupEnum.NON_MODAL), // return to home
		G30(	30,		ModalGroupEnum.NON_MODAL),  // return to secondary home
		G31(	31,		ModalGroupEnum.MOTION),  // stright probe
		G40(	40,		ModalGroupEnum.CUTTER_RADIUS_COMPENSATION),  // cancel cutter radius compensation
		G41(	41,		ModalGroupEnum.CUTTER_RADIUS_COMPENSATION), // start cutter radius compensation left
		G42(	42,		ModalGroupEnum.CUTTER_RADIUS_COMPENSATION), // start cutter radius compensation right
		G43(	43,		ModalGroupEnum.TOOL_LENGTH_OFFSET), // tool length offset (plus)
		G49(	49,		ModalGroupEnum.TOOL_LENGTH_OFFSET), // cancel tool length offset
		G50(	50,		ModalGroupEnum.NON_MODAL), // Reset all scale factors to 1.0
		G51(	51,		ModalGroupEnum.NON_MODAL), // Set axis data input scale factors
		G53(	53,		ModalGroupEnum.NON_MODAL),  // motion in machine coordinate system
		G54(	54,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 1
		G55(	55,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 2
		G56(	56,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 3
		G57(	57,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 4
		G58(	58,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 5
		G59(	59,		ModalGroupEnum.COORDINAT_SYSTEM_SELECTION), // use preset work coordinate system 6
		G61(	61,		ModalGroupEnum.PATH_CONTROL_MODE), // set path control mode: exact stop
		G64(	64,		ModalGroupEnum.PATH_CONTROL_MODE), // set path control mode: constant velocity
		G68(	68,		ModalGroupEnum.NON_MODAL), // Rotate program coordinate system
		G69(	69,		ModalGroupEnum.NON_MODAL), // non-rotate program coordinate system
		G70(	70,		ModalGroupEnum.UNITS),  // imperial (inch) system selection
		G71(	71,		ModalGroupEnum.UNITS),  // metric (millimeter) system selection
		G73(	73,		ModalGroupEnum.MOTION), // Canned cycle - peck drilling
		G80(	80,		ModalGroupEnum.MOTION), // Cancel motion mode (including canned cycles)
		G81(	81,		ModalGroupEnum.MOTION), // canned cycle: drilling
		G82(	82,		ModalGroupEnum.MOTION), // canned cycle: drilling with dwell
		G83(	83,		ModalGroupEnum.MOTION), // canned cycle: peck drilling
		G84(	84,		ModalGroupEnum.MOTION), // canned cycle: right hand tapping
		G85(	85,		ModalGroupEnum.MOTION), // canned cycle: boring, no dwell, feed out
		G86(	86,		ModalGroupEnum.MOTION), // canned cycle: boring, spindle stop, rapid out
		G87(	87,		ModalGroupEnum.MOTION), // canned cycle: back boring
		G88(	88,		ModalGroupEnum.MOTION), // canned cycle: boring, spindle stop, manual out
		G89(	89,		ModalGroupEnum.MOTION), // canned cycle: boring, dwell, feed out
		G90(	90,		ModalGroupEnum.DISTANCE_MODE), // absolute distance mode
		G91(	91,		ModalGroupEnum.DISTANCE_MODE), // incremental distance mode
		G92(	92,		ModalGroupEnum.NON_MODAL),  // offset coordinate systems and set parameters
		G92_1(	92.1,	ModalGroupEnum.NON_MODAL),  // cancel offset coordinate systems and set parameters to zero
		G92_2(	92.2,	ModalGroupEnum.NON_MODAL),  // cancel offset coordinate systems but do not reset parameters
		G92_3(	92.3,	ModalGroupEnum.NON_MODAL),  // apply parameters to offset coordinate systems
		G93(	93,		ModalGroupEnum.FEED_RATE_MODE), // inverse time feed rate mode
		G94(	94,		ModalGroupEnum.FEED_RATE_MODE), // units per minute feed rate mode
		G95(	95,		ModalGroupEnum.FEED_RATE_MODE), // units per rev feed rate mode
		G98(	98,		ModalGroupEnum.CANNED_CYCLE_RETURN), // initial level return in canned cycles
		G99(	99,		ModalGroupEnum.CANNED_CYCLE_RETURN), // R-point level return in canned cycles
		M0(		0,		ModalGroupEnum.STOPPING), // Program stop
		M1(		1,		ModalGroupEnum.STOPPING), // Optional program stop
		M2(		2,		ModalGroupEnum.STOPPING), // Program end
		M3(		3,		ModalGroupEnum.SPINDLE_TURNING), // Rotate spindle clockwise
		M4(		4,		ModalGroupEnum.SPINDLE_TURNING), // Rotate spindle counterclckwise
		M5(		5,		ModalGroupEnum.SPINDLE_TURNING), // Stop spindle rotation
		M6(		6,		ModalGroupEnum.TOOL_CHANGE), // Tool change (by two macros)
		M7(		7,		ModalGroupEnum.COOLANT), // Mist coolant on
		M8(		8,		ModalGroupEnum.COOLANT), // Flood coolant on
		M9(		9,		ModalGroupEnum.COOLANT), // All coolant off
		M30(	30,		ModalGroupEnum.STOPPING), // Program end and Rewind
		M47(	47,		ModalGroupEnum.OVERRIDE_SWITCHES), // Repeat program from first line
		M48(	48,		ModalGroupEnum.OVERRIDE_SWITCHES), // Enable speed and feed override
		M49(	49,		ModalGroupEnum.OVERRIDE_SWITCHES), // Disable speed and feed override
		M98(	98,		ModalGroupEnum.NON_MODAL), // Call subroutine
		M99(	99,		ModalGroupEnum.NON_MODAL), // Return from subroutine/repeat
		S(		1,		ModalGroupEnum.SPINDLE_SPEED), // set spindle spee
		T(		1,		ModalGroupEnum.TOOL_SELECT); // select tool

		public double num;
		public ModalGroupEnum modalGroup;
		
		private CNCCommandEnum(double n, ModalGroupEnum g){
			num = n;
			modalGroup = g;
		};
		
		public enum ModalGroupEnum {

			NON_MODAL(0),
			MOTION(1),
			PLANE_SELECTION(2),
			DISTANCE_MODE(3),
			STOPPING(4),
			FEED_RATE(5),
			FEED_RATE_MODE(5),
			TOOL_CHANGE(6),
			UNITS(6),
			SPINDLE_TURNING(7),
			SPINDLE_SPEED(7),
			CUTTER_RADIUS_COMPENSATION(7),
			COOLANT(8),
			TOOL_SELECT(8),
			TOOL_LENGTH_OFFSET(8),
			OVERRIDE_SWITCHES(9),
			CANNED_CYCLE_RETURN(10),
			COORDINAT_SYSTEM_SELECTION(12),
			PATH_CONTROL_MODE(13),
			LAST_DUMMY(-1);
			
			public int groupNum;

			private ModalGroupEnum(int n){
				groupNum = n;
			}
		}
	}
}
