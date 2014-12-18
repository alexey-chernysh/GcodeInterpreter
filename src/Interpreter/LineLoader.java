package Interpreter;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.Expression.CommandLineLoader;
import Interpreter.Expression.CommandPair;
import Interpreter.Expression.ExpressionGeneral;
import Interpreter.Expression.Variables.ExpressionVarAssignment;
import Interpreter.Motion.Motion;
import Interpreter.Motion.MotionControlMode;
import Interpreter.Motion.Offset;
import Interpreter.Motion.Attributes.DistanceMode;
import Interpreter.Motion.Attributes.Plane;
import Interpreter.Motion.Attributes.LengthUnits.Units;
import Interpreter.State.InterpreterState;
import Interpreter.State.CannedCycle.ReturnMode;
import Interpreter.State.Coolant.CoolantState;
import Interpreter.State.FeedRate.FeedRateMode;
import Interpreter.State.Overrides.Overrides;
import Interpreter.State.Spindle.SpindleRotation;
import Interpreter.State.Tools.ToolHeight.ToolHeightOffset;
import Interpreter.State.Tools.ToolRadius.Compensation;

public class LineLoader extends CommandLineLoader {
	
	private String message_ = null;
	private FeedRateMode feedRateMode_ = FeedRateMode.UNDEFINED;
	private ExpressionGeneral feedRate_ = null;
	private int tool_ = -1;
	private boolean M6_ = false;
	private SpindleRotation spindleRotation_ = SpindleRotation.UNDEFINED;
	private ExpressionGeneral spindelSpeed_ = null;
	private CoolantState coolant_ = CoolantState.UNDEFINED;
	private Overrides overrides_ = Overrides.UNDEFINED;
	private boolean dwell_ = false;
	private Plane plane_ = Plane.UNDEFINED;
	private Units lengthUnits_ = Units.UNDEFINED;
	private Compensation compensation_ = Compensation.UNDEFINED;
	private ToolHeightOffset heightOffset_ = ToolHeightOffset.UNDEFINED; 
	private int fixtureToolOffset_ = -1;
	public static final int G59_SELECTED = 6;
	private MotionControlMode motionMode_ = MotionControlMode.UNDEFINED;
	private DistanceMode distanceMode_ = DistanceMode.UNDEFINED;
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
			double value = currentCommand.getCurrentValue();
			int commandNum = (int) (10*value);
			switch(currentCommand.getType()){
			case F:
				this.feedRate_ = commandValueExpressiion;
				break;
			case G:
				switch(commandNum){
				case 0:
					break;
				case 10:
					break;
				case 20:
					break;
				case 30:
					break;
				case 40:
					this.setDwell();
					break;
				case 100:
					break;
				case 120:
					break;
				case 130:
					break;
				case 150:
					break;
				case 160:
					break;
				case 170:
					this.setPlane(Plane.XY);
					break;
				case 180:
					this.setPlane(Plane.XZ);
					break;
				case 190:
					this.setPlane(Plane.YZ);
					break;
				case 200:
					this.setUnitsImperial();
					break;
				case 210:
					this.setUnitsMetric();
					break;
				case 280:
					break;
				case 281:
					break;
				case 300:
					break;
				case 310:
					break;
				case 400:
					this.setCompensation(Compensation.OFF);
					break;
				case 410:
					this.setCompensation(Compensation.LEFT);
					break;
				case 420:
					this.setCompensation(Compensation.RIGHT);
					break;
				case 430:
					this.setHeightOffset(ToolHeightOffset.ON);
					break;
				case 490:
					this.setHeightOffset(ToolHeightOffset.OFF);
					break;
				case 500:
					break;
				case 510:
					break;
				case 520:
					break;
				case 530:
					break;
				case 540:
					this.setFixtureToolOffset(1);
					break;
				case 550:
					this.setFixtureToolOffset(2);
					break;
				case 560:
					this.setFixtureToolOffset(3);
					break;
				case 570:
					this.setFixtureToolOffset(4);
					break;
				case 580:
					this.setFixtureToolOffset(5);
					break;
				case 590:
					this.setFixtureToolOffset(LineLoader.G59_SELECTED);
					break;
				case 610:
					this.setMotionMode(MotionControlMode.EXACT_STOP);
					break;
				case 640:
					this.setMotionMode(MotionControlMode.CONTINUOUS_SPEED);
					break;
				case 680:
					break;
				case 690:
					break;
				case 700:
					this.setUnitsImperial();
					break;
				case 710:
					this.setUnitsMetric();
					break;
				case 730:
					break;
				case 800:
					break;
				case 810:
					break;
				case 820:
					break;
				case 830:
					break;
				case 840:
					break;
				case 850:
					break;
				case 860:
					break;
				case 870:
					break;
				case 880:
					break;
				case 890:
					break;
				case 900:
					this.setDistanceMode(DistanceMode.ABSOLUTE);
					break;
				case 910:
					this.setDistanceMode(DistanceMode.INCREMENTAL);
					break;
				case 920:
					break;
				case 930:
					this.setFeedRateMode(FeedRateMode.INVERSE_TIME_FEED_MODE);
					break;
				case 940:
					this.setFeedRateMode(FeedRateMode.FEED_PER_MINUTE_MODE);
					break;
				case 950:
					this.setFeedRateMode(FeedRateMode.FED_PER_REV_MODE);
					break;
				case 980:
					this.setReturnMode(ReturnMode.RETURN_NO_LOWER_THEN_R);
					break;
				case 990:
					this.setReturnMode(ReturnMode.RETURN_TO_R);
					break;
				default:
					throw new GcodeRuntimeException("Unsupported G code num");
				}
				break;
			case M:
				switch(commandNum){
				case 0:
					break;
				case 10:
					break;
				case 20:
					break;
				case 30:
					this.setSpindleRotation(SpindleRotation.CLOCKWISE);
					break;
				case 40:
					this.setSpindleRotation(SpindleRotation.COUNTERCLOCKWISE);
					break;
				case 50:
					this.setSpindleRotation(SpindleRotation.OFF);
					break;
				case 60:
					break;
				case 70:
					this.M7();
					break;
				case 80:
					this.M8();
					break;
				case 90:
					this.M9();
					break;
				case 300:
					break;
				case 470:
					break;
				case 480:
					break;
				case 490:
					break;
				case 980:
					break;
				case 990:
					break;
				default:
					throw new GcodeRuntimeException("Unsupported M code num");
				};
				break;
			case S:
				this.spindelSpeed_ = commandValueExpressiion;
				break;
			case T:
				this.setTool((int)value);
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
		if(this.feedRateMode_ != FeedRateMode.UNDEFINED) 
			InterpreterState.feedRate.setMode(this.feedRateMode_);
		
		// set feed rate
		if(this.feedRate_ != null) 
			InterpreterState.feedRate.set(this.feedRate_.evalute());
		
		// set spindel speed
		if(this.spindelSpeed_ != null)
			InterpreterState.spindle.set(this.spindelSpeed_.evalute());
		
		// select tool
		if(this.tool_ >= 0)
			ProgramLoader.interpreterState.tool.setToolNum(this.tool_);
		
		// tool change macro
		if(this.M6_){
			// TODO execute tool change macro
		}
		
		// set spindel rotation
		if(this.spindleRotation_ != SpindleRotation.UNDEFINED) 
			ProgramLoader.interpreterState.spindle.setState(this.spindleRotation_);
		
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
			ProgramLoader.interpreterState.feedRate.enableOverride();
			ProgramLoader.interpreterState.spindle.enableOverride();
			break;
		case OFF:
			ProgramLoader.interpreterState.feedRate.disableOverride();
			ProgramLoader.interpreterState.spindle.disableOverride();
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
		if(this.plane_ != Plane.UNDEFINED) 
			ProgramLoader.interpreterState.coordinateSystem.setPlane(this.plane_);
		
		// set length units
		if(this.lengthUnits_ != Units.UNDEFINED) 
			ProgramLoader.interpreterState.lengthUnits.set(this.lengthUnits_);
		
		// set cutter radius compensation
		if(this.compensation_ != Compensation.UNDEFINED) { 
			if(ProgramLoader.interpreterState.coordinateSystem.getPlane() != Plane.XY)
				throw new GcodeRuntimeException("Cutter radius compensation available in XY plane only!");
			ProgramLoader.interpreterState.cutterRadius.setState(this.compensation_);
			double radius = this.valueList_.getP();
			if(radius > 0.0){
				radius = InterpreterState.lengthUnits.toMM(radius);
				ProgramLoader.interpreterState.cutterRadius.setRadius(radius);
			} else {
				int toolNum = this.valueList_.getD();
				if(toolNum >= 0){
					radius = ProgramLoader.interpreterState.toolSet_.getToolRadius(toolNum);
					ProgramLoader.interpreterState.cutterRadius.setRadius(radius);
				}
			}
			// TODO move tool at compensation offset from current point
		}
		
		// set tool table offset
		switch(this.heightOffset_){
		case ON:
			int toolNum = this.valueList_.getH();
			if(toolNum < 0){
				toolNum = ProgramLoader.interpreterState.toolSet_.getCurrentTool();
			};
			double height = ProgramLoader.interpreterState.toolSet_.getToolHeight(toolNum);
			ProgramLoader.interpreterState.toolHeight.setOffset(height);
			ProgramLoader.interpreterState.toolHeight.setState(ToolHeightOffset.ON);
			// TODO move tool at compensation offset from current point
			break;
		case OFF:
			ProgramLoader.interpreterState.toolHeight.setState(ToolHeightOffset.OFF);
			// TODO move tool at compensation offset from current point
			break;
		default:
		}
		
		// fixture table select
		if(this.fixtureToolOffset_ > 0){
			if(ProgramLoader.interpreterState.cutterRadius.compensationIsOn())
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
		if(this.motionMode_ != MotionControlMode.UNDEFINED)
			ProgramLoader.interpreterState.feedRate.setMotionMode(motionMode_);
		
		// set distance mode
		if(this.distanceMode_ != DistanceMode.UNDEFINED)
			ProgramLoader.interpreterState.coordinateSystem.set(distanceMode_);
		
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
/*	
	public void setFeedRate(ExpressionGeneral commandValueExpressiion) {
		this.feedRate_ = commandValueExpressiion;
	}

	public void setSpindelSpeed(double spindelSpeed) {
		this.spindelSpeed_ = spindelSpeed;
	}
*/
	public void setTool(int tool) {
		this.tool_ = tool;
	}

	public void setMessage(String message) {
		this.message_ = message;
	}

	public void setFeedRateMode(FeedRateMode feedRateMode) {
		this.feedRateMode_ = feedRateMode;
	}

	public void setM6(boolean m6_) {
		M6_ = m6_;
	}

	public void setSpindleRotation(SpindleRotation spindleRotation_) {
		this.spindleRotation_ = spindleRotation_;
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

	public void setPlane(Plane plane) {
		this.plane_ = plane;
	}

	public void setUnitsMetric() {
		this.lengthUnits_ = Units.METRIC;
	}

	public void setUnitsImperial() {
		this.lengthUnits_ = Units.IMPERIAL;
	}

	public void setCompensation(Compensation compensation) {
		this.compensation_ = compensation;
	}

	public void setHeightOffset(ToolHeightOffset heightOffset) {
		this.heightOffset_ = heightOffset;
	}

	public void setFixtureToolOffset(int toolNum) {
		this.fixtureToolOffset_ = toolNum;
	}

	public void setMotionMode(MotionControlMode motionMode) {
		this.motionMode_ = motionMode;
	}

	public void setDistanceMode(DistanceMode distanceMode) {
		this.distanceMode_ = distanceMode;
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
}
