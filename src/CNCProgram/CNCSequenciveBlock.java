package CNCProgram;

import CNCHardware.CNCCannedCycleReturnMode.ReturnMode;
import CNCHardware.CNCCoolant.CoolantCommand;
import CNCHardware.CNCHardwareState;
import CNCHardware.CNCOverrideSwitch.Overrides;
import Coordinate.CoordinateSystem.DistanceMode;
import Coordinate.CoordinateSystem.Motion;
import Coordinate.CoordinateSystem.Offset;
import Coordinate.CoordinateSystem.Plane;
import Coordinate.LengthUnits.Units;
import Exceptions.GcodeRuntimeException;
import Feed.FeedRateMode;
import Feed.MotionControlMode;
import Spindle.Spindle.SpindleRotation;
import Tools.ToolRadius.Compensation;
import Tools.ToolHeight.ToolHeightOffset;

public class CNCSequenciveBlock {
	
	private String message_ = null;
	private FeedRateMode feedRateMode_ = FeedRateMode.UNDEFINED;
	private double feedRate_ = 0.0;
	private double spindelSpeed_ = 0.0;
	private int tool_ = -1;
	private boolean M6_ = false;
	private SpindleRotation spindleRotation_ = SpindleRotation.UNDEFINED;
	private CoolantCommand coolant_ = CoolantCommand.UNDEFINED;
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
	private Motion motion_ = CNCHardwareState.coordinateSystem.getCurrentMotion();
	public final CNCBlockValueList valueList_ = new CNCBlockValueList();

	public CNCSequenciveBlock(){
	}
	
	public void compile() throws GcodeRuntimeException{
		
		// display message
		if(this.message_ != null) 
			System.out.println(this.message_);
		
		// set feed rate mode
		if(this.feedRateMode_ != FeedRateMode.UNDEFINED) 
			CNCHardwareState.feedRate.setMode(this.feedRateMode_);
		
		// set feed rate
		if(this.feedRate_ > 0.0){
			double fr = this.feedRate_ * CNCHardwareState.lengthUnits.getScale();
			CNCHardwareState.feedRate.set(fr);
		}
		
		// set spindel speed
		if(this.spindelSpeed_ > 0.0)
			CNCHardwareState.spindle.set(this.spindelSpeed_);
		
		// select tool
		if(this.tool_ >= 0)
			CNCHardwareState.tool.setToolNum(this.tool_);
		
		// tool change macro
		if(this.M6_){
			// TODO execute tool change macro
		}
		
		// set spindel rotation
		if(this.spindleRotation_ != SpindleRotation.UNDEFINED) 
			CNCHardwareState.spindle.setState(this.spindleRotation_);
		
		// set coolant state
		switch(this.coolant_){
		case M7:
			CNCHardwareState.coolant.mistOn();
			break;
		case M8:
			CNCHardwareState.coolant.floodOn();
			break;
		case M9:
			CNCHardwareState.coolant.off();
			break;
		default:
		}
		
		// set overrides
		switch(this.overrides_){
		case ON:
			CNCHardwareState.feedRate.enableOverride();
			CNCHardwareState.spindle.enableOverride();
			break;
		case OFF:
			CNCHardwareState.feedRate.disableOverride();
			CNCHardwareState.spindle.disableOverride();
			break;
		default:
		}
		
		// dwell
		if(dwell_){
			double delay = this.valueList_.getP();
			if(delay>0.0){
				delay = CNCHardwareState.timeFormat.scaleToMillis(delay);
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
			CNCHardwareState.coordinateSystem.setPlane(this.plane_);
		
		// set length units
		if(this.lengthUnits_ != Units.UNDEFINED) 
			CNCHardwareState.lengthUnits.set(this.lengthUnits_);
		
		// set cutter radius compensation
		if(this.compensation_ != Compensation.UNDEFINED) { 
			if(CNCHardwareState.coordinateSystem.getPlane() != Plane.PLANE_XY)
				throw new GcodeRuntimeException("Cutter radius compensation available in XY plane only!");
			CNCHardwareState.cutterRadius.setState(this.compensation_);
			double radius = this.valueList_.getP();
			if(radius>0.0){
				radius *= CNCHardwareState.lengthUnits.getScale();
				CNCHardwareState.cutterRadius.setRadius(radius);
			} else {
				int toolNum = this.valueList_.getD();
				if(toolNum >= 0){
					radius = CNCHardwareState.toolSet_.getToolRadius(toolNum);
					CNCHardwareState.cutterRadius.setRadius(radius);
				}
			}
			// TODO move tool at compensation offset from current point
		}
		
		// set tool table offset
		switch(this.heightOffset_){
		case ON:
			int toolNum = this.valueList_.getH();
			if(toolNum < 0){
				toolNum = CNCHardwareState.toolSet_.getCurrentTool();
			};
			double height = CNCHardwareState.toolSet_.getToolHeight(toolNum);
			CNCHardwareState.toolHeight.setOffset(height);
			CNCHardwareState.toolHeight.setState(ToolHeightOffset.ON);
			// TODO move tool at compensation offset from current point
			break;
		case OFF:
			CNCHardwareState.toolHeight.setState(ToolHeightOffset.OFF);
			// TODO move tool at compensation offset from current point
			break;
		default:
		}
		
		// fixture table select
		if(this.fixtureToolOffset_ > 0){
			if(CNCHardwareState.cutterRadius.compensationIsOn())
				throw new GcodeRuntimeException("Fixture offset unavailable while cutter radius compensation is on!");
			int toolNum = fixtureToolOffset_;
			int tmpP = (int) this.valueList_.getP();
			if((toolNum == CNCSequenciveBlock.G59_SELECTED)&&(tmpP>0)) toolNum = tmpP;
			double X = CNCHardwareState.vars_.getOffsetX(toolNum);
			double Y = CNCHardwareState.vars_.getOffsetY(toolNum);
			double Z = CNCHardwareState.vars_.getOffsetZ(toolNum);
			double A = CNCHardwareState.vars_.getOffsetA(toolNum);
			double B = CNCHardwareState.vars_.getOffsetB(toolNum);
			double C = CNCHardwareState.vars_.getOffsetC(toolNum);
			// TODO something with this shit	
		}
		
		// set path control mode
		if(this.motionMode_ != MotionControlMode.UNDEFINED)
			CNCHardwareState.feedRate.setMotionMode(motionMode_);
		
		// set distance mode
		if(this.distanceMode_ != DistanceMode.UNDEFINED)
			CNCHardwareState.coordinateSystem.set(distanceMode_);
		
		// set canned cycle return level mode
		if(this.returnMode_ != ReturnMode.UNDEFINED){
			CNCHardwareState.cycleReturnMode.setMode(returnMode_);
			double tmpR = this.valueList_.getR();
			if(tmpR > 0.0) CNCHardwareState.cycleReturnMode.setR(tmpR);
		}
		
		// set coordinate system offset
		switch(this.offset_) {
		case G10:
			int tmpL = this.valueList_.getL();
			switch (tmpL) {
			case 1:
				int tmpP = (int) this.valueList_.getP();
				CNCHardwareState.vars_.setWorkingToolOffset(tmpP, 
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
			CNCHardwareState.vars_.setG92Offset(this.valueList_.getX(), 
												this.valueList_.getY(), 
												this.valueList_.getZ(), 
												this.valueList_.getA(), 
												this.valueList_.getB(), 
												this.valueList_.getC());
			break;
		case G92_1:
			CNCHardwareState.vars_.setG92Offset(0.0, 
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
	
	public void setFeedRate(double feedRate) {
		this.feedRate_ = feedRate;
	}

	public void setSpindelSpeed(double spindelSpeed) {
		this.spindelSpeed_ = spindelSpeed;
	}

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
		this.coolant_ = CoolantCommand.M7;
	}

	public void M8() {
		this.coolant_ = CoolantCommand.M8;
	}

	public void M9() {
		this.coolant_ = CoolantCommand.M9;
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