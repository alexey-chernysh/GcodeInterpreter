package Interpreter.State.ModalState;

import Drivers.CanonicalCommands.OffsetMode;
import Interpreter.InterpreterException;
import Interpreter.Expression.LineCommandPairList;
import Interpreter.Expression.CommandPair.CNCWordEnum;
import Interpreter.Motion.Point;
import Interpreter.State.InterpreterState;

public class ModalState {
	
	private GcommandSet[] g_state_;
	private McommandSet[] m_state_;
		
	public ModalState(){
		int i;
		g_state_ = new GcommandSet[GcommandModalGroupSet.G_GROUP0_NON_MODAL.ordinal()];
		for(i=0; i<GcommandModalGroupSet.G_GROUP0_NON_MODAL.ordinal(); i++)
			g_state_[i] = GcommandSet.GDUMMY;
		m_state_ = new McommandSet[McommandModalGroupSet.M_GROUP0_NON_MODAL.ordinal()];
		for(i=0; i<McommandModalGroupSet.M_GROUP0_NON_MODAL.ordinal(); i++)
			m_state_[i] = McommandSet.MDUMMY;
	};
		
	public void initToDefaultState() throws InterpreterException{
		
		set(GcommandModalGroupSet.G_GROUP1_MOTION, GcommandSet.G0);
		set(GcommandModalGroupSet.G_GROUP2_PLANE, GcommandSet.G17);
		set(GcommandModalGroupSet.G_GROUP3_DISTANCE_MODE, GcommandSet.G91);
		set(GcommandModalGroupSet.G_GROUP4_ARC_DISTANCE_MODE, GcommandSet.G91_1);
		set(GcommandModalGroupSet.G_GROUP5_FEED_RATE_MODE, GcommandSet.G94);
		set(GcommandModalGroupSet.G_GROUP6_UNITS, GcommandSet.G21);
		set(GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION, GcommandSet.G40);
		set(GcommandModalGroupSet.G_GROUP8_TOOL_LENGHT_OFFSET, GcommandSet.G49);
		set(GcommandModalGroupSet.G_GROUP10_CANNED_CYCLES_RETURN_MODE, GcommandSet.G98);
		set(GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION, GcommandSet.G54);
		set(GcommandModalGroupSet.G_GROUP13_PATH_CONTROL_MODE, GcommandSet.G61);
		set(GcommandModalGroupSet.G_GROUP16_COORDINATE_ROTATION, GcommandSet.G69);
		set(GcommandModalGroupSet.G_GROUP17_POLAR_COORDINATES, GcommandSet.G16);
		set(GcommandModalGroupSet.G_GROUP18_SCALING, GcommandSet.G50);
		
		set(McommandModalGroupSet.M_GROUP4_PROGRAM_CONTROL, McommandSet.M98);
		set(McommandModalGroupSet.M_GROUP6_TOOL_CHANGE, McommandSet.M6);
		set(McommandModalGroupSet.M_GROUP7_SPINDLE_TURNING, McommandSet.M5);
		set(McommandModalGroupSet.M_GROUP8_COOLANT, McommandSet.M9);
		set(McommandModalGroupSet.M_GROUP9_OVERRIDES, McommandSet.M48);
		
	};
	
	public void set(GcommandModalGroupSet group, GcommandSet command) throws InterpreterException{
		if(group != GcommandModalGroupSet.G_GROUP0_NON_MODAL){
			if(command.modalGroup == group){
				g_state_[group.ordinal()] = command;
			} else throw new InterpreterException("Changing modal state with command from another modal group");
		} else throw new InterpreterException("Assiment non modal command to modal state");
		
	}
	
	public GcommandSet getGModalState(GcommandModalGroupSet group){
		return g_state_[group.ordinal()];
	}
	
	public void set(McommandModalGroupSet group, McommandSet command) throws InterpreterException{
		if(group != McommandModalGroupSet.M_GROUP0_NON_MODAL){
			if(command.modalGroup == group){
				m_state_[group.ordinal()] = command;
			} else throw new InterpreterException("Changing modal state with command from another modal group");
		} else throw new InterpreterException("Assiment non modal command to modal state");
		
	}
	
	public McommandSet getMModalState(McommandModalGroupSet group){
		return m_state_[group.ordinal()];
	}
	
	public double toMM(double x){
		switch(this.getGModalState(GcommandModalGroupSet.G_GROUP6_UNITS)){
		case G20:
			return 25.4*x;
		case G21:
		default:
			return x;
		}
	}

	public boolean isPolar() {
		return (this.getGModalState(GcommandModalGroupSet.G_GROUP17_POLAR_COORDINATES) == GcommandSet.G15);
	};

	public boolean isAbsolute(){
		// TODO G53 command needed
		return (this.getGModalState(GcommandModalGroupSet.G_GROUP3_DISTANCE_MODE) == GcommandSet.G90);
	}

	public Point getPoint(Point refPoint, LineCommandPairList words) throws InterpreterException {
		double x, y;
		if(words.hasXYZ()){
			x = words.get(CNCWordEnum.X);
			y = words.get(CNCWordEnum.Y);
		} else throw new InterpreterException("Insuficient parameters in command line");
		Point resultPoint = refPoint.clone();
		if(InterpreterState.modalState.isPolar()){
			// TODO polar coordinate needed
		} else {
			x = InterpreterState.modalState.toMM(x);
			y = InterpreterState.modalState.toMM(y);
			if(!InterpreterState.modalState.isAbsolute()){
				x += refPoint.getX();
				y += refPoint.getY();
			};
			// TODO axis rotation needed
			resultPoint.setX(x);
			resultPoint.setY(y);
		};
		return resultPoint;
	}

	public OffsetMode getCutterRadiusOffsetMode() throws InterpreterException {
		GcommandSet mode = this.getGModalState(GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION);
		switch(mode){
		case G40:
			return OffsetMode.NONE;
		case G41:
			return OffsetMode.LEFT;
		case G42:
			return OffsetMode.RIGHT;
		default:
			throw new InterpreterException("Illegal value of cutter radius compensation modal group");
		}
	}
}
