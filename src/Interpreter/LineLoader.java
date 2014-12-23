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
import Interpreter.State.ModalState.GcommandModalGroupSet;
import Interpreter.State.ModalState.GcommandSet;
import Interpreter.State.ModalState.McommandSet;

public class LineLoader extends CommandLineLoader {
	
	private String message_ = null;
	private ExpressionGeneral feedRate_     = null;
	private ExpressionGeneral tool_         = null;
	private ExpressionGeneral spindelSpeed_ = null;
	private McommandSet M3_M4_M5 = McommandSet.MDUMMY;
	private McommandSet M6       = McommandSet.MDUMMY;
	private McommandSet M7_M8_M9 = McommandSet.MDUMMY;
	private McommandSet M48_M49  = McommandSet.MDUMMY;
	private GcommandSet G4          = GcommandSet.GDUMMY;
	private GcommandSet G_NON_MODAL = GcommandSet.GDUMMY;
	private GcommandSet G15_G16     = GcommandSet.GDUMMY;
	private GcommandSet G17_G18_G19 = GcommandSet.GDUMMY;
	private GcommandSet G20_G21     = GcommandSet.GDUMMY;
	private GcommandSet G40_G41_G42 = GcommandSet.GDUMMY;
	private GcommandSet G43_G49     = GcommandSet.GDUMMY; 
	private GcommandSet G50_G51     = GcommandSet.GDUMMY; 
	private GcommandSet G53         = GcommandSet.GDUMMY; 
	private GcommandSet G54___G59   = GcommandSet.GDUMMY;
	private GcommandSet G61_G64     = GcommandSet.GDUMMY;
	private GcommandSet G68_G69     = GcommandSet.GDUMMY;
	private GcommandSet G90_G91     = GcommandSet.GDUMMY;
	private GcommandSet G90_1_G91_1 = GcommandSet.GDUMMY;
	private GcommandSet G93_G94_G95 = GcommandSet.GDUMMY;
	private GcommandSet G98_G99     = GcommandSet.GDUMMY;
	private GcommandSet G_MOTION    = GcommandSet.GDUMMY;
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
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G0;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G1:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G1;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G2:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G2;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G3:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G3;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G4:
					if(this.G4 == GcommandSet.GDUMMY) this.G4 = GcommandSet.G4;
					else throw new GcodeRuntimeException("Twice dwell command in same string");
					break;
				case G10:
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G10;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G12:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G12;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G13:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G13;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G15:
					if(this.G15_G16 == GcommandSet.GDUMMY) this.G15_G16 = GcommandSet.G15;
					else throw new GcodeRuntimeException("Twice polar coordinate command in same string");
					break;
				case G16:
					if(this.G15_G16 == GcommandSet.GDUMMY) this.G15_G16 = GcommandSet.G16;
					else throw new GcodeRuntimeException("Twice polar coordinate command in same string");
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
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G28;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G28_1:
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G28_1;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G30:
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G30;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G31:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G31;
					else throw new GcodeRuntimeException("Twice motion command in same string");
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
					if(this.G50_G51 == GcommandSet.GDUMMY) this.G50_G51 = GcommandSet.G50;
					else throw new GcodeRuntimeException("Twice scale change command in same string");
					break;
				case G51:
					if(this.G50_G51 == GcommandSet.GDUMMY) this.G50_G51 = GcommandSet.G51;
					else throw new GcodeRuntimeException("Twice scale change command in same string");
					break;
				case G52:
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G52;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G53:
					if(this.G53 == GcommandSet.GDUMMY) this.G53 = GcommandSet.G53;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G54:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G54;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G55:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G55;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G56:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G56;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G57:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G57;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G58:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G58;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
					break;
				case G59:
					if(this.G54___G59 == GcommandSet.GDUMMY) this.G54___G59 = GcommandSet.G59;
					else throw new GcodeRuntimeException("Twice fixture tool offset command in same string");
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
					if(this.G68_G69 == GcommandSet.GDUMMY) this.G68_G69 = GcommandSet.G68;
					else throw new GcodeRuntimeException("Twice coordinate rotation command in same string");
					break;
				case G69:
					if(this.G68_G69 == GcommandSet.GDUMMY) this.G68_G69 = GcommandSet.G69;
					else throw new GcodeRuntimeException("Twice coordinate rotation command in same string");
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
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G73;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G80:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G80;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G81:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G81;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G82:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G82;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G83:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G83;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G84:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G84;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G85:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G85;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G86:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G86;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G87:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G87;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G88:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G88;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G89:
					if(this.G_MOTION == GcommandSet.GDUMMY) this.G_MOTION = GcommandSet.G89;
					else throw new GcodeRuntimeException("Twice motion command in same string");
					break;
				case G90:
					if(this.G90_G91 == GcommandSet.GDUMMY) this.G90_G91 = GcommandSet.G90;
					else throw new GcodeRuntimeException("Twice distance mode command in same string");
					break;
				case G90_1:
					if(this.G90_1_G91_1 == GcommandSet.GDUMMY) this.G90_1_G91_1 = GcommandSet.G90_1;
					else throw new GcodeRuntimeException("Twice arc center distance mode command in same string");
					break;
				case G91:
					if(this.G90_G91 == GcommandSet.GDUMMY) this.G90_G91 = GcommandSet.G91;
					else throw new GcodeRuntimeException("Twice distance mode command in same string");
					break;
				case G91_1:
					if(this.G90_1_G91_1 == GcommandSet.GDUMMY) this.G90_1_G91_1 = GcommandSet.G91_1;
					else throw new GcodeRuntimeException("Twice arc center distance mode command in same string");
					break;
				case G92:
					if(this.G_NON_MODAL == GcommandSet.GDUMMY) this.G_NON_MODAL = GcommandSet.G92;
					else throw new GcodeRuntimeException("Twice homing command in same string");
					break;
				case G93:
					if(this.G93_G94_G95 == GcommandSet.GDUMMY) this.G93_G94_G95 = GcommandSet.G93;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G94:
					if(this.G93_G94_G95 == GcommandSet.GDUMMY) this.G93_G94_G95 = GcommandSet.G94;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G95:
					if(this.G93_G94_G95 == GcommandSet.GDUMMY) this.G93_G94_G95 = GcommandSet.G95;
					else throw new GcodeRuntimeException("Twice feed rate mode change command in same string");
					break;
				case G98:
					if(this.G98_G99 == GcommandSet.GDUMMY) this.G98_G99 = GcommandSet.G98;
					else throw new GcodeRuntimeException("Twice cycle return mode command in same string");
					break;
				case G99:
					if(this.G98_G99 == GcommandSet.GDUMMY) this.G98_G99 = GcommandSet.G99;
					else throw new GcodeRuntimeException("Twice cycle return mode command in same string");
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
					if(this.M3_M4_M5 == McommandSet.MDUMMY) this.M3_M4_M5 = McommandSet.M3;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M4:
					if(this.M3_M4_M5 == McommandSet.MDUMMY) this.M3_M4_M5 = McommandSet.M4;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M5:
					if(this.M3_M4_M5 == McommandSet.MDUMMY) this.M3_M4_M5 = McommandSet.M5;
					else throw new GcodeRuntimeException("Twice spindle rotation command in same string");
					break;
				case M6:
					if(this.M6 == McommandSet.MDUMMY) this.M6 = McommandSet.M5;
					else throw new GcodeRuntimeException("Twice change tool command in same string");
					break;
				case M7:
					if(this.M7_M8_M9 == McommandSet.MDUMMY) this.M7_M8_M9 = McommandSet.M7;
					else throw new GcodeRuntimeException("Twice coolant mode command in same string");
					break;
				case M8:
					if(this.M7_M8_M9 == McommandSet.MDUMMY) this.M7_M8_M9 = McommandSet.M8;
					else throw new GcodeRuntimeException("Twice coolant mode command in same string");
					break;
				case M9:
					if(this.M7_M8_M9 == McommandSet.MDUMMY) this.M7_M8_M9 = McommandSet.M9;
					else throw new GcodeRuntimeException("Twice coolant mode command in same string");
					break;
				case M30:
					break;
				case M47:
					break;
				case M48:
					if(this.M48_M49 == McommandSet.MDUMMY) this.M48_M49 = McommandSet.M48;
					else throw new GcodeRuntimeException("Twice override command in same string");
					break;
				case M49:
					if(this.M48_M49 == McommandSet.MDUMMY) this.M48_M49 = McommandSet.M49;
					else throw new GcodeRuntimeException("Twice override command in same string");
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
				throw new GcodeRuntimeException("Unsupported command");
			}
		}

	}
	
	public void evalute() throws GcodeRuntimeException{
		// evalution sequence strictly in order described by "Mach3 G and M code reference"
		// every evolution change interpreter's virtual CNC-machine state or generate HAL command
		// and add it in HAL execution sequence
		// 1 display message
		if(this.message_ != null) 
			System.out.println(this.message_);
		
		// 2 set feed rate mode 
		this.G93_G94_G95.evalute(this.wordList_);
		
		// 3 set feed rate (F)
		if(this.feedRate_ != null) 
			InterpreterState.feedRate.set(this.feedRate_.evalute());
		
		// 4 set spindel speed (S)
		if(this.spindelSpeed_ != null)
			InterpreterState.spindle.set(this.spindelSpeed_.evalute());
		
		// 5 select tool (T)
		if(this.tool_ != null)
			InterpreterState.toolSet.setCurrentTool((int)this.tool_.evalute());
		
		// 6 tool change macro M6
		this.M6.evalute();
		
		// 7 set spindel rotation
		this.M3_M4_M5.evalute();
		
		// 8 set coolant state
		this.M7_M8_M9.evalute();
		
		// 9 set overrides
		this.M48_M49.evalute();
		
		// 10 dwell
		this.G4.evalute(this.wordList_);
		
		// 11 set active plane
		this.G17_G18_G19.evalute(this.wordList_);
		
		// maybe it should be in another place of this sequence
		this.G15_G16.evalute(this.wordList_); 
		
		// 12 set length units
		this.G20_G21.evalute(this.wordList_);
		
		// 13 set cutter radius compensation
		this.G40_G41_G42.evalute(this.wordList_);
		
		// 14 set tool table offset
		this.G43_G49.evalute(this.wordList_);
		
		// 15 fixture table select
		this.G54___G59.evalute(this.wordList_);
		
		// 16 set path control mode
		this.G61_G64.evalute(this.wordList_);
		
		// 17 set distance mode
		this.G90_G91.evalute(this.wordList_);
		this.G53.evalute(this.wordList_);
		this.G68_G69.evalute(this.wordList_);
		
		// 18 set canned cycle return level mode
		this.G98_G99.evalute(this.wordList_);
		
		// 19 homing and coordinate system offset non modal commands
		this.G_NON_MODAL.evalute(this.wordList_);
		
		// 20 perform motion
		this.G_MOTION.evalute(this.wordList_);
		
		int size = this.varAssignmentSet_.size();
		for(int i=0; i<size; i++){
			ExpressionVarAssignment currentVar = this.varAssignmentSet_.get(i);
			currentVar.evalute();
		}
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
