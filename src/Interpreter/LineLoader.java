package Interpreter;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.Expression.CommandLineLoader;
import Interpreter.Expression.CommandPair;
import Interpreter.Expression.ExpressionGeneral;
import Interpreter.Expression.Variables.ExpressionVarAssignment;
import Interpreter.Motion.Motion;
import Interpreter.Motion.Offset;
import Interpreter.State.InterpreterState;
import Interpreter.State.CannedCycle.ReturnMode;
import Interpreter.State.Coolant.CoolantState;
import Interpreter.State.ModalState.GcommandModalGroupSet;
import Interpreter.State.ModalState.GcommandSet;
import Interpreter.State.ModalState.McommandSet;
import Interpreter.State.Overrides.Overrides;

public class LineLoader extends CommandLineLoader {
	
	private String message_ = null;
	private ExpressionGeneral feedRate_ = null;
	private ExpressionGeneral tool_ = null;
	private ExpressionGeneral spindelSpeed_ = null;
	private GcommandSet feedRateMode_ = GcommandSet.GDUMMY;
	private boolean M6_ = false;
	private McommandSet spindleRotation_ = McommandSet.MDUMMY;
	private CoolantState coolant_ = CoolantState.UNDEFINED;
	private Overrides overrides_ = Overrides.UNDEFINED;
	private boolean dwell_ = false;
	private GcommandSet G17_G18_G19 = GcommandSet.GDUMMY;
	private GcommandSet G20_G21 = GcommandSet.GDUMMY;
	private GcommandSet G40_G41_G42 = GcommandSet.GDUMMY;
	private GcommandSet G43_G49 = GcommandSet.GDUMMY; 
	private int fixtureToolOffset_ = -1;
	public static final int G59_SELECTED = 6;
	private GcommandSet G61_G64 = GcommandSet.GDUMMY;
	private GcommandSet G90_G91 = GcommandSet.GDUMMY;
	private ReturnMode returnMode_ = ReturnMode.UNDEFINED;
	private Offset offset_ = Offset.UNDEFINED;
	private Motion motion_ = InterpreterState.coordinateSystem.getCurrentMotion();
	public final LineValueList valueList_ = new LineValueList();

	public LineLoader(String s, int programNumber) throws LexerException, GcodeRuntimeException{
		super(s, programNumber);
		int size = this.commandSet_.size();
		int i;
		for(i=0; i<size; i++){
			CommandPair currentCommand = this.commandSet_.get(i);
			ExpressionGeneral commandValueExpressiion = currentCommand.getValueExpression();
			switch(currentCommand.getType()){
			case F:
				this.feedRate_ = commandValueExpressiion;
				break;
			case G:
				GcommandSet g_command = this.GcommandByNumber(currentCommand.getCurrentValue());
				switch(g_command){
				case G0:
					break;
				case G1:
					break;
				case G2:
					break;
				case G3:
					break;
				case G4:
					this.setDwell();
					break;
				case G10:
					break;
				case G12:
					break;
				case G13:
					break;
				case G15:
					break;
				case G16:
					break;
				case G17:
					if(this.G17_G18_G19 == GcommandSet.GDUMMY) this.G17_G18_G19 = GcommandSet.G17;
					else throw new GcodeRuntimeException("Twice plane selection command in same string");
					break;
				case G18:
					if(this.G17_G18_G19 == GcommandSet.GDUMMY) this.G17_G18_G19 = GcommandSet.G18;
					else throw new GcodeRuntimeException("Twice plane selection command in same string");
					break;
				case G19:
					if(this.G17_G18_G19 == GcommandSet.GDUMMY) this.G17_G18_G19 = GcommandSet.G19;
					else throw new GcodeRuntimeException("Twice plane selection command in same string");
					break;
				case G20:
					if(this.G20_G21 == GcommandSet.GDUMMY) this.G20_G21 = GcommandSet.G20;
					else throw new GcodeRuntimeException("Twice units change command in same string");
					break;
				case G21:
					if(this.G20_G21 == GcommandSet.GDUMMY) this.G20_G21 = GcommandSet.G21;
					else throw new GcodeRuntimeException("Twice units change command in same string");
					break;
				case G28:
					break;
				case G28_1:
					break;
				case G30:
					break;
				case G31:
					break;
				case G40:
					if(this.G40_G41_G42 == GcommandSet.GDUMMY) this.G40_G41_G42 = GcommandSet.G40;
					else throw new GcodeRuntimeException("Twice cutter radius compensation change command in same string");
					break;
				case G41:
					if(this.G40_G41_G42 == GcommandSet.GDUMMY) this.G40_G41_G42 = GcommandSet.G41;
					else throw new GcodeRuntimeException("Twice cutter radius compensation change command in same string");
					break;
				case G42:
					if(this.G40_G41_G42 == GcommandSet.GDUMMY) this.G40_G41_G42 = GcommandSet.G42;
					else throw new GcodeRuntimeException("Twice cutter radius compensation change command in same string");
					break;
				case G43:
					if(this.G43_G49 == GcommandSet.GDUMMY) this.G43_G49 = GcommandSet.G43;
					else throw new GcodeRuntimeException("Twice cutter height compensation change command in same string");
					break;
				case G49:
					if(this.G43_G49 == GcommandSet.GDUMMY) this.G43_G49 = GcommandSet.G49;
					else throw new GcodeRuntimeException("Twice cutter height compensation change command in same string");
					break;
				case G50:
					break;
				case G51:
					break;
				case G52:
					break;
				case G53:
					break;
				case G54:
					this.setFixtureToolOffset(1);
					break;
				case G55:
					this.setFixtureToolOffset(2);
					break;
				case G56:
					this.setFixtureToolOffset(3);
					break;
				case G57:
					this.setFixtureToolOffset(4);
					break;
				case G58:
					this.setFixtureToolOffset(5);
					break;
				case G59:
					this.setFixtureToolOffset(LineLoader.G59_SELECTED);
					break;
				case G61:
					if(this.G61_G64 == GcommandSet.GDUMMY) this.G61_G64 = GcommandSet.G61;
					else throw new GcodeRuntimeException("Twice path control mode command in same string");
					break;
				case G64:
					if(this.G61_G64 == GcommandSet.GDUMMY) this.G61_G64 = GcommandSet.G64;
					else throw new GcodeRuntimeException("Twice path control mode command in same string");
					break;
				case G68:
					break;
				case G69:
					break;
				case G70:
					if(this.G20_G21 == GcommandSet.GDUMMY) this.G20_G21 = GcommandSet.G70;
					else throw new GcodeRuntimeException("Twice units change command in same string");
					break;
				case G71:
					if(this.G20_G21 == GcommandSet.GDUMMY) this.G20_G21 = GcommandSet.G71;
					else throw new GcodeRuntimeException("Twice units change command in same string");
					break;
				case G73:
					break;
				case G80:
					break;
				case G81:
					break;
				case G82:
					break;
				case G83:
					break;
				case G84:
					break;
				case G85:
					break;
				case G86:
					break;
				case G87:
					break;
				case G88:
					break;
				case G89:
					break;
				case G90:
					if(this.G90_G91 == GcommandSet.GDUMMY) this.G90_G91 = GcommandSet.G90;
					else throw new GcodeRuntimeException("Twice distance mode command in same string");
					break;
				case G91:
					if(this.G90_G91 == GcommandSet.GDUMMY) this.G90_G91 = GcommandSet.G91;
					else throw new GcodeRuntimeException("Twice distance mode command in same string");
					break;
				case G92:
					break;
				case G93:
					if(this.feedRateMode_ == GcommandSet.GDUMMY) this.feedRateMode_ = GcommandSet.G93;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G94:
					if(this.feedRateMode_ == GcommandSet.GDUMMY) this.feedRateMode_ = GcommandSet.G94;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G95:
					if(this.feedRateMode_ == GcommandSet.GDUMMY) this.feedRateMode_ = GcommandSet.G95;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G98:
					this.setReturnMode(ReturnMode.RETURN_NO_LOWER_THEN_R);
					break;
				case G99:
					this.setReturnMode(ReturnMode.RETURN_TO_R);
					break;
				default:
					throw new GcodeRuntimeException("Unsupported G code num");
				}
				break;
			case M:
				McommandSet m_command = this.McommandByNumber(currentCommand.getCurrentValue());
				switch(m_command){
				case M0:
					break;
				case M1:
					break;
				case M2:
					break;
				case M3:
					if(this.spindleRotation_ == McommandSet.MDUMMY) this.spindleRotation_ = McommandSet.M3;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M4:
					if(this.spindleRotation_ == McommandSet.MDUMMY) this.spindleRotation_ = McommandSet.M4;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M5:
					if(this.spindleRotation_ == McommandSet.MDUMMY) this.spindleRotation_ = McommandSet.M5;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M6:
					break;
				case M7:
					this.M7();
					break;
				case M8:
					this.M8();
					break;
				case M9:
					this.M9();
					break;
				case M30:
					break;
				case M47:
					break;
				case M48:
					break;
				case M49:
					break;
				case M98:
					break;
				case M99:
					break;
				default:
					throw new GcodeRuntimeException("Unsupported M code num");
				};
				break;
			case S:
				this.spindelSpeed_ = commandValueExpressiion;
				break;
			case T:
				this.tool_ = commandValueExpressiion;
				break;
			default:
				throw new GcodeRuntimeException("Unsupported command num");
			}
		}
		size = this.wordList_.getLength();
		for(i=0; i<size; i++){
			CommandPair currentWord = this.wordList_.get(i);
		}
		size = this.varAssignmentSet_.size();
		for(i=0; i<size; i++){
			ExpressionVarAssignment currentVar = this.varAssignmentSet_.get(i);
			currentVar.evalute();
		}
	}
	
	public void evalute() throws GcodeRuntimeException{
		
		// display message
		if(this.message_ != null) 
			System.out.println(this.message_);
		
		// set feed rate mode
		this.feedRateMode_.evalute(this.wordList_);
		
		// set feed rate
		if(this.feedRate_ != null) 
			InterpreterState.feedRate.set(this.feedRate_.evalute());
		
		// set spindel speed
		if(this.spindelSpeed_ != null)
			InterpreterState.spindle.set(this.spindelSpeed_.evalute());
		
		// select tool
		if(this.tool_ != null)
			InterpreterState.toolSet.setCurrentTool((int)this.tool_.evalute());
		
		// tool change macro
		if(this.M6_){
			// TODO execute tool change macro
		}
		
		// set spindel rotation
		this.spindleRotation_.evalute();
		
		// set coolant state
		switch(this.coolant_){
		case M7:
			ProgramLoader.interpreterState.coolant.mistOn();
			break;
		case M8:
			ProgramLoader.interpreterState.coolant.floodOn();
			break;
		case M9:
			ProgramLoader.interpreterState.coolant.off();
			break;
		default:
		}
		
		// set overrides
		switch(this.overrides_){
		case ON:
			InterpreterState.feedRate.enableOverride();
			InterpreterState.spindle.enableOverride();
			break;
		case OFF:
			InterpreterState.feedRate.disableOverride();
			InterpreterState.spindle.disableOverride();
			break;
		default:
		}
		
		// dwell
		if(dwell_){
			double delay = this.valueList_.getP();
			if(delay>0.0){
				delay = ProgramLoader.interpreterState.timeFormat.scaleToMillis(delay);
				try {
					Thread.currentThread();
					Thread.sleep((long) delay);
				}
				catch(InterruptedException ie){
				//If this thread was intrrupted by another thread 
				}	
			}
			else throw new GcodeRuntimeException("P value needed!");
		}
		
		// set active plane
		this.G17_G18_G19.evalute(this.wordList_);
		
		// set length units
		this.G20_G21.evalute(this.wordList_);
		
		// set cutter radius compensation
		this.G40_G41_G42.evalute(this.wordList_);
		
		// set tool table offset
		this.G43_G49.evalute(wordList_);
		
		// fixture table select
		if(this.fixtureToolOffset_ > 0){
			if(InterpreterState.modalState.getGModalState(GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION) != GcommandSet.G40)
				throw new GcodeRuntimeException("Fixture offset unavailable while cutter radius compensation is on!");
			int toolNum = fixtureToolOffset_;
			int tmpP = (int) this.valueList_.getP();
			if((toolNum == LineLoader.G59_SELECTED)&&(tmpP>0)) toolNum = tmpP;
			double X = ProgramLoader.interpreterState.vars_.getOffsetX(toolNum);
			double Y = ProgramLoader.interpreterState.vars_.getOffsetY(toolNum);
			double Z = ProgramLoader.interpreterState.vars_.getOffsetZ(toolNum);
			double A = ProgramLoader.interpreterState.vars_.getOffsetA(toolNum);
			double B = ProgramLoader.interpreterState.vars_.getOffsetB(toolNum);
			double C = ProgramLoader.interpreterState.vars_.getOffsetC(toolNum);
			// TODO something with this shit	
		}
		
		// set path control mode
		this.G61_G64.evalute(wordList_);
		
		// set distance mode
		this.G90_G91.evalute(wordList_);
		
		// set canned cycle return level mode
		if(this.returnMode_ != ReturnMode.UNDEFINED){
			ProgramLoader.interpreterState.cycleReturnMode.setMode(returnMode_);
			double tmpR = this.valueList_.getR();
			if(tmpR > 0.0) InterpreterState.cycleReturnMode.setR(tmpR);
		}
		
		// set coordinate system offset
		switch(this.offset_) {
		case G10:
			int tmpL = this.valueList_.getL();
			switch (tmpL) {
			case 1:
				int tmpP = (int) this.valueList_.getP();
				ProgramLoader.interpreterState.vars_.setWorkingToolOffset(tmpP, 
															this.valueList_.getX(), 
															this.valueList_.getY(), 
															this.valueList_.getZ(), 
															this.valueList_.getA(), 
															this.valueList_.getB(), 
															this.valueList_.getC());
				break;
			case 2:
				break;
			default:
				throw new GcodeRuntimeException("Illegal L parameter in G10 command!");
			}
			break;
		case G52:
		case G92:
			ProgramLoader.interpreterState.vars_.setG92Offset(this.valueList_.getX(), 
												this.valueList_.getY(), 
												this.valueList_.getZ(), 
												this.valueList_.getA(), 
												this.valueList_.getB(), 
												this.valueList_.getC());
			break;
		case G92_1:
			ProgramLoader.interpreterState.vars_.setG92Offset(0.0, 
												0.0, 
												0.0, 
												0.0, 
												0.0, 
												0.0);
			break;
		case G92_2:
			// TODO don't understand what
			break;
		case G92_3:
			// TODO don't understand what
			break;
		case UNDEFINED:
		default:
		}
		
		// perform motion
		switch(this.motion_){
		case G0:
			break;
		case G1:
			break;
		case G2:
			break;
		case G3:
			break;
		case G12:
			break;
		case G13:
			break;
		case G80:
			break;
		case G81:
			break;
		case G82:
			break;
		case G83:
			break;
		case G84:
			break;
		case G85:
			break;
		case G86:
			break;
		case G87:
			break;
		case G88:
			break;
		case UNDEFINED:
		default:
		}
	}

	public void setMessage(String message) {
		this.message_ = message;
	}

	public void setM6(boolean m6_) {
		M6_ = m6_;
	}

	public void M7() {
		this.coolant_ = CoolantState.M7;
	}

	public void M8() {
		this.coolant_ = CoolantState.M8;
	}

	public void M9() {
		this.coolant_ = CoolantState.M9;
	}
	
	public void G48() {
		this.overrides_ = Overrides.ON;
	}
	
	public void G49() {
		this.overrides_ = Overrides.OFF;
	}
	
	public void setDwell() {
		this.dwell_ = true;
	}

	public void setFixtureToolOffset(int toolNum) {
		this.fixtureToolOffset_ = toolNum;
	}

	public void setReturnMode(ReturnMode returnMode) {
		this.returnMode_ = returnMode;
	}

	public void setOffset(Offset offset) {
		this.offset_ = offset;
	}

	public void setMotion(Motion m){
		this.motion_ = m;
	}

	public GcommandSet GcommandByNumber(double x){
		int tmp = (int)(10*x);
		for(int i=0; i<GcommandSet.GDUMMY.ordinal(); i++){
			if(GcommandSet.values()[i].number == tmp) return GcommandSet.values()[i];
		};
		return GcommandSet.GDUMMY;
	}

	private McommandSet McommandByNumber(double x) {
		int tmp = (int)(x);
		for(int i=0; i<McommandSet.MDUMMY.ordinal(); i++){
			if(McommandSet.values()[i].number == tmp) return McommandSet.values()[i];
		};
		return McommandSet.MDUMMY;
	}

}
