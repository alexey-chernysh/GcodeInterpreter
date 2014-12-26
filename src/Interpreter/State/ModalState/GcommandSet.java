package Interpreter.State.ModalState;

import Drivers.CanonicalCommands.ArcDirection;
import Drivers.CanonicalCommands.G00_G01;
import Drivers.CanonicalCommands.G02_G03;
import Drivers.CanonicalCommands.G04;
import Drivers.CanonicalCommands.MotionMode;
import Interpreter.InterpreterException;
import Interpreter.ProgramLoader;
import Interpreter.Expression.ParamExpresionList;
import Interpreter.Expression.Tokens.TokenParameter;
import Interpreter.Motion.Point;
import Interpreter.State.InterpreterState;

public enum GcommandSet {
	G0(0.0, GcommandModalGroupSet.G_GROUP1_MOTION){ // Rapid positioning
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException { 
			InterpreterState.modalState.set(modalGroup, this);
			Point startPoint = InterpreterState.getLastPosition();
			Point endPoint = InterpreterState.modalState.getTargetPoint(startPoint, words);
			G00_G01 newG0 = new G00_G01(startPoint, 
										endPoint, 
										InterpreterState.feedRate.getRapidFeedRate(), 
										MotionMode.FREE, 
										null);
			ProgramLoader.hal_commands.add(newG0);
			InterpreterState.setLastPosition(endPoint);
		}
	}, 
	G1(1.0, GcommandModalGroupSet.G_GROUP1_MOTION){ // Linear interpolation
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException {
			InterpreterState.modalState.set(modalGroup, this);
			Point startPoint = InterpreterState.getLastPosition();
			Point endPoint = InterpreterState.modalState.getTargetPoint(startPoint, words);
			G00_G01 newG1 = new G00_G01(startPoint, 
										endPoint, 
										InterpreterState.feedRate.getWorkFeedRate(), 
										MotionMode.WORK, 
										InterpreterState.modalState.getCutterRadiusOffsetMode());
			ProgramLoader.hal_commands.add(newG1);
			InterpreterState.setLastPosition(endPoint);
		}
	}, 
	G2(2.0, GcommandModalGroupSet.G_GROUP1_MOTION){ // Clockwise circular/helical interpolation
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException {
			InterpreterState.modalState.set(modalGroup, this);
			Point startPoint = InterpreterState.getLastPosition();
			Point endPoint = InterpreterState.modalState.getTargetPoint(startPoint, words);
			// TODO R format also needed
			Point centerPoint = InterpreterState.modalState.getCenterPoint(startPoint, words);
			G02_G03 newG2 = new G02_G03(startPoint, 
										endPoint,
										centerPoint,
										ArcDirection.CLOCKWISE,
										InterpreterState.feedRate.getRapidFeedRate(), 
										MotionMode.WORK, 
										InterpreterState.modalState.getCutterRadiusOffsetMode());
			ProgramLoader.hal_commands.add(newG2);
			InterpreterState.setLastPosition(endPoint);
		}
	}, 
	G3(3.0, GcommandModalGroupSet.G_GROUP1_MOTION){ // Counterclockwise circular/Helical interpolation
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException {
			InterpreterState.modalState.set(modalGroup, this);
			Point startPoint = InterpreterState.getLastPosition();
			Point endPoint = InterpreterState.modalState.getTargetPoint(startPoint, words);
			// TODO R format also needed
			Point centerPoint = InterpreterState.modalState.getCenterPoint(startPoint, words);
			G02_G03 newG3 = new G02_G03(startPoint, 
										endPoint,
										centerPoint,
										ArcDirection.COUNTERCLOCKWISE,
										InterpreterState.feedRate.getRapidFeedRate(), 
										MotionMode.WORK, 
										InterpreterState.modalState.getCutterRadiusOffsetMode());
			ProgramLoader.hal_commands.add(newG3);
			InterpreterState.setLastPosition(endPoint);
		}
	}, 
	G4(4.0, GcommandModalGroupSet.G_GROUP0_G4_DWELL){ // Dwell
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException {
			InterpreterState.modalState.set(modalGroup, this);
			double p = words.get(TokenParameter.P);
			if(p >= 0.0){
				G04 newG4 = new G04(p);
				ProgramLoader.hal_commands.add(newG4);
			} else throw new InterpreterException("Illegal dwell time");
		}
	}, 
	G10(10.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Coordinate system origin setting
	G12(12.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Clockwise circular pocket
	G13(13.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Counterclockwise circular pocket
	G15(15.0, GcommandModalGroupSet.G_GROUP17_POLAR_COORDINATES), // Polar coordinate moves in G0 and G1
	G16(16.0, GcommandModalGroupSet.G_GROUP17_POLAR_COORDINATES), // Cancel polar coordinate moves
	G17(17.0, GcommandModalGroupSet.G_GROUP2_PLANE), // XY Plane select
	G18(18.0, GcommandModalGroupSet.G_GROUP2_PLANE), // XZ plane select
	G19(19.0, GcommandModalGroupSet.G_GROUP2_PLANE), // YZ plane select
	G20(20.0, GcommandModalGroupSet.G_GROUP6_UNITS), // Inch unit
	G21(21.0, GcommandModalGroupSet.G_GROUP6_UNITS), // Millimetre unit
	G28(28.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Return home
	G28_1(28.1, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Reference axes
	G30(30.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Return home
	G31(31.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Straight probe
	G40(40.0, GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION){ // Cancel cutter radius compensation
	}, 
	G41(41.0, GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION){ // Start cutter radius compensation left
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException{ 
			if(InterpreterState.modalState.getGModalState(GcommandModalGroupSet.G_GROUP2_PLANE) != G17)
				throw new InterpreterException("Kerf offset possible in  XY plane only");
			InterpreterState.modalState.set(modalGroup, this);
		}
	}, // Start cutter radius compensation left
	G42(42.0, GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION), // Start cutter radius compensation right
	G43(43.0, GcommandModalGroupSet.G_GROUP8_TOOL_LENGHT_OFFSET), // Apply tool length offset (plus)
	G49(49.0, GcommandModalGroupSet.G_GROUP8_TOOL_LENGHT_OFFSET), // Cancel tool length offset
	G50(50.0, GcommandModalGroupSet.G_GROUP18_SCALING), // Reset all scale factors to 1.0
	G51(51.0, GcommandModalGroupSet.G_GROUP18_SCALING), // Set axis data input scale factors
	G52(52.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Temporary coordinate system offsets
	G53(53.0, GcommandModalGroupSet.G_GROUP0_G53_MODIFIER), // Move in absolute machine coordinate system
	G54(54.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 1
	G55(55.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 2
	G56(56.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 3
	G57(57.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 4
	G58(58.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 5
	G59(59.0, GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION), // Use fixture offset 6 / use general fixture number
	G61(61.0, GcommandModalGroupSet.G_GROUP13_PATH_CONTROL_MODE), // Exact stop mode
	G64(64.0, GcommandModalGroupSet.G_GROUP13_PATH_CONTROL_MODE), // Constant Velocity mode
	G68(68.0, GcommandModalGroupSet.G_GROUP16_COORDINATE_ROTATION), // Rotate program coordinate system
	G69(69.0, GcommandModalGroupSet.G_GROUP16_COORDINATE_ROTATION), // Cancel program coordinate system rotation
	G70(70.0, GcommandModalGroupSet.G_GROUP6_UNITS),  // Inch unit
	G71(71.0, GcommandModalGroupSet.G_GROUP6_UNITS), // Millimetre unit
	G73(73.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - peck drilling
	G80(80.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Cancel motion mode (including canned cycles)
	G81(81.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - drilling
	G82(82.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - drilling with dwell
	G83(83.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - peck drilling
	G84(84.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - right hand rigid tapping
	G85(85.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - boring
	G86(86.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - boring
	G87(87.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - boring
	G88(88.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - boring
	G89(89.0, GcommandModalGroupSet.G_GROUP1_MOTION), // Canned cycle - boring
	G90(90.0, GcommandModalGroupSet.G_GROUP3_DISTANCE_MODE), // Absolute distance mode
	G90_1(90.1, GcommandModalGroupSet.G_GROUP4_ARC_DISTANCE_MODE), // Arc absolute distance mode
	G91(91.0, GcommandModalGroupSet.G_GROUP3_DISTANCE_MODE), // Incremental distance mode
	G91_1(91.1, GcommandModalGroupSet.G_GROUP4_ARC_DISTANCE_MODE), // Arc incremental distance mode
	G92(92.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Offset coordinates and set parameters
	G92_1(92.1, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // Cancel G92 etc.
	G92_2(92.2, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // 
	G92_3(92.3, GcommandModalGroupSet.G_GROUP0_NON_MODAL), // 
	G93(93.0, GcommandModalGroupSet.G_GROUP5_FEED_RATE_MODE){ // Inverse time feed mode
	}, 
	G94(94.0, GcommandModalGroupSet.G_GROUP5_FEED_RATE_MODE){ // Feed per minute mode
	}, 
	G95(95.0, GcommandModalGroupSet.G_GROUP5_FEED_RATE_MODE){ // Feed per rev mode
	}, 
	G98(98.0, GcommandModalGroupSet.G_GROUP10_CANNED_CYCLES_RETURN_MODE), // Initial level return after canned cycles
	G99(99.0, GcommandModalGroupSet.G_GROUP10_CANNED_CYCLES_RETURN_MODE), // R-point level return after canned cycles
	GDUMMY(-1.0, GcommandModalGroupSet.G_GROUP0_NON_MODAL){
		@Override
		public void evalute(ParamExpresionList words) throws InterpreterException{
		};
	}; // dummy command for initial assignment
	
	public int number;
	public GcommandModalGroupSet modalGroup;
	
	public void evalute(ParamExpresionList words) throws InterpreterException{
		InterpreterState.modalState.set(modalGroup, this);
	};
	
	
	private GcommandSet(double n, GcommandModalGroupSet g){
		this.number = (int)(10*n);
		this.modalGroup = g;
	};
	
}
