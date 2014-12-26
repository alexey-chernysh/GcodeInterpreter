package Drivers.Cutter;

import java.util.ArrayList;

import Drivers.GeneralDriver;
import Drivers.CanonicalCommands.ArcDirection;
import Drivers.CanonicalCommands.G00_G01;
import Drivers.CanonicalCommands.G02_G03;
import Drivers.CanonicalCommands.G04;
import Drivers.CanonicalCommands.CanonCommand;
import Drivers.CanonicalCommands.M9cutter;
import Drivers.CanonicalCommands.M8cutter;
import Drivers.CanonicalCommands.MotionMode;
import HAL.MotionController.MCCommandArcMotion;
import HAL.MotionController.MCCommandStraightMotion;
import Interpreter.InterpreterException;
import Interpreter.Motion.Point;
import Settings.Settings;

public class CutterDriver implements GeneralDriver {
	
	private double kerf_offset_;
	
	private ArrayList<CanonCommand> sourceCommands_ = null; 
	private ArrayList<Object> commands_ = null;

	public CutterDriver(double kerf_offset){
		this.kerf_offset_ = kerf_offset;
	}

	public CutterDriver(){ // same default value for debug
		this.kerf_offset_ = 1.5;
	}
	
	
	@Override
	public void loadProgram(ArrayList<CanonCommand> sourceCommands) throws InterpreterException{
		
		sourceCommands_ = sourceCommands;
		addKerfWidthCompensation();
		addAccelDeaccel();
		
	}

	private void addKerfWidthCompensation() throws InterpreterException {
		
		commands_ = new  ArrayList<Object>();
		
		// initial loading with changing points according kerf offset
		int size = this.sourceCommands_.size();
		for(int i=0; i<size; i++){
			CanonCommand command = this.sourceCommands_.get(i);
			if(command instanceof G04){
				// TODO add pause implementation
			} else {
				if(command instanceof M9cutter){
					commands_.add(new CutterTorchOnOff(true));
				} else {
					if(command instanceof M8cutter){
						commands_.add(new CutterTorchOnOff(false));
					} else {
						G00_G01 motionCommand = (G00_G01)command;
						if(command instanceof G00_G01){
							if(!motionCommand.isWorkingRun()){
								addFreeMotion(motionCommand);
							} else {
								addCuttingStraightMotion(motionCommand);
							}
						} else {
							if(command instanceof G02_G03) {
								addCuttingArcMotion((G02_G03)command);
							} else {
								throw new InterpreterException("Unsupperted command");
							}
						}
					}
				}
			}
		}
	}

	private void addAccelDeaccel() throws InterpreterException {

		int size = this.commands_.size();
		
		double perforationVel = Settings.getPerforationSpeed();
		double perforationVelDur = Settings.getPerfSpeedDuration();
		double perfLength = perforationVel*perforationVelDur/60.0; // in mm
		
		double startVel = Settings.getStartSpeed();
		double cutVel = Settings.getWorkSpeed();
		double accel = Settings.getAcceleration();
		
		for(int i=0; i<size; i++){
			Object currentCommand = this.commands_.get(i);
			boolean itsCuttingLine = currentCommand instanceof MCCommandStraightMotion;
			boolean itsCuttingArc = currentCommand instanceof MCCommandArcMotion;
			if(itsCuttingLine || itsCuttingArc){ 
				// working with cutting lines and arc only
				Object prevCuttingCommand = PreviousCutting(i);
				Object nextCuttingCommand = NextCutting(i);
				double neededVelocity = ((MCCommandStraightMotion)currentCommand).getVelocity();
				if(prevCuttingCommand == null){ 
					// first cutting after perforation - inserting slow perforation phase 
					if(itsCuttingLine){
						MCCommandStraightMotion currentLine = (MCCommandStraightMotion)currentCommand;
						double currentLength = currentLine.length();
						if((perfLength < currentLength)&&(perfLength > 0.0)){
							MCCommandStraightMotion newLine1 = currentLine.newSubLine(0, perfLength);
							newLine1.setVelocityProfile(perforationVel,startVel, accel);
							MCCommandStraightMotion newLine2 = currentLine.newSubLine(perfLength, currentLength);
							newLine1.setVelocityProfile(startVel, neededVelocity, accel);
							this.commands_.remove(i);
							this.commands_.add(i, newLine2);
							this.commands_.add(i, newLine1);
							i++;
						} else {
							currentLine.setVelocityProfile(startVel, neededVelocity, accel);
						}
					} else {
						if(itsCuttingArc){
							MCCommandArcMotion currentArc = (MCCommandArcMotion)currentCommand;
							double currentLength = currentArc.length();
							if((perfLength < currentLength)&&(perfLength > 0.0)){
								MCCommandArcMotion newArc1 = currentArc.newSubArc(0, perfLength);
								newArc1.setVelocityProfile(perforationVel,startVel, accel);
								MCCommandArcMotion newArc2 = currentArc.newSubArc(perfLength, currentLength);
								newArc1.setVelocityProfile(startVel, neededVelocity, accel);
								this.commands_.remove(i);
								this.commands_.add(i, newArc2);
								this.commands_.add(i, newArc1);
								i++;
							} else {
								currentArc.setVelocityProfile(startVel, neededVelocity, accel);
							}
						}				
					}
				} else {
					// its no first cutting line - adjustment needed
					MCCommandStraightMotion beforeLine = (MCCommandStraightMotion)prevCuttingCommand;
					double angleBeforeStart = beforeLine.getEndTangentAngle();
					double velBeforeStart = beforeLine.getEndVel();
					MCCommandStraightMotion currentLine = (MCCommandStraightMotion)currentCommand;
					double angleStart = currentLine.getStartTangentAngle();
					if(Math.abs(angleStart - angleBeforeStart) < Settings.angleTol){
						// fine case of smooth line angle adjustment.
						// adjust velocity now
						if(velBeforeStart == neededVelocity){
							// velocity is equal
							currentLine.setVelocityProfile(neededVelocity, neededVelocity, accel);
						} else {
							currentLine.setVelocityProfile(velBeforeStart, neededVelocity, accel);
						}
					} else {
						currentLine.setVelocityProfile(startVel, neededVelocity, accel);
						((MCCommandStraightMotion)prevCuttingCommand).setEndVel(startVel);
					} 
				}
				if(nextCuttingCommand == null){
					((MCCommandStraightMotion)this.commands_.get(i)).setEndVel(startVel);
				}

			};
		}
	}

	private Object NextCutting(int i) {
		if((i+1) >= this.commands_.size()) return null;
		Object next = this.commands_.get(i+1);
		if(next instanceof MCCommandStraightMotion)
			if(((MCCommandStraightMotion)next).getMode() == MotionMode.WORK) return next;
		if(next instanceof MCCommandArcMotion) return next;
		return null;
	}

	private Object PreviousCutting(int i){  // command index
		if(i <= 0) return null;
		Object before = this.commands_.get(i-1);
		if(before instanceof MCCommandStraightMotion)
			if(((MCCommandStraightMotion)before).getMode() == MotionMode.WORK) return before;
		if(before instanceof MCCommandArcMotion) return before;
		return null;
	} 

	private void addFreeMotion(G00_G01 command) throws InterpreterException {
		Object lastMotion = findLastMotion();
		MCCommandStraightMotion newFreeMotion = new MCCommandStraightMotion(command, 0.0);
		if(lastMotion != null){ 
			// line should be corrected according kerf offset of previous line
			// last motion is straight or arc working run, correction needed
			newFreeMotion.setStart(((MCCommandStraightMotion)lastMotion).getEnd());
		}
		commands_.add(newFreeMotion);
	}

	private void addCuttingStraightMotion(G00_G01 command) throws InterpreterException {
		Object lastMotion = findLastMotion();
		MCCommandStraightMotion newCutterMotion = new MCCommandStraightMotion(command, kerf_offset_);
		if(lastMotion != null){ // its no first move
			if((lastMotion instanceof MCCommandStraightMotion)&&(!((MCCommandStraightMotion)lastMotion).isWorkingRun())) {
				// free run line should be connected to start of new motion
				((MCCommandStraightMotion)lastMotion).setEnd(newCutterMotion.getStart());
			} else {
				// cutting motion before this
				MCCommandStraightMotion lm = (MCCommandStraightMotion)lastMotion;
				double alfaCurrent = newCutterMotion.getStartTangentAngle();
				double alfaPrev = lm.getEndTangentAngle();
				final double d_alfa = alfaCurrent - alfaPrev;
				switch(command.getOffsetMode()){
				case LEFT:
					if(d_alfa > 0.0){
						// line turn left and left offset
						if(lastMotion instanceof MCCommandStraightMotion){  // Straight line before
							// calculate length shortening of new line
							double d_l = this.kerf_offset_ * Math.sin(d_alfa/2.0);
							Point connectionPoint = lm.getEnd().clone();
							connectionPoint.shift(-d_l*Math.sin(alfaPrev), -d_l*Math.cos(alfaPrev));
							// correct previous line
							if(lm.length() <= d_l) 
								throw new InterpreterException("Previous line too short to current compensation");
							lm.setEnd(connectionPoint);
							// correct current line
							if(newCutterMotion.length() <= d_l) 
								throw new InterpreterException("New line too short to current compensation");
							newCutterMotion.setStart(connectionPoint);
						} else {
							// arc line before 
							MCCommandArcMotion arc = (MCCommandArcMotion)lastMotion;
							Point connectionPoint = getConnectionPoint(newCutterMotion, arc, ConnectionType.STARTEND);
							arc.setEnd(connectionPoint);
							newCutterMotion.setStart(connectionPoint);
						};
					} else {
						if(d_alfa < 0.0){
							// line turn right and left offset
							// linking arc with kerf offset radius needed
							MCCommandArcMotion newArc = new MCCommandArcMotion(lm.getEnd(),
									  							 	 newCutterMotion.getStart(),
									  							 	 command.getStart(),
									  							 	 ArcDirection.COUNTERCLOCKWISE,
									  							 	 command);
							commands_.add(newArc);
						};
					}
					break;
				case RIGHT:
					if(d_alfa > 0.0){
						// line turn left and right offset
						// linking arc with kerf offset radius needed
						MCCommandArcMotion newArc = new MCCommandArcMotion(lm.getEnd(),
								  								 newCutterMotion.getStart(),
								  								 command.getStart(),
								  								 ArcDirection.CLOCKWISE,
								  								 command);
						commands_.add(newArc);
					} else {
						if(d_alfa < 0.0){
							// line turn right and right offset
							if(lastMotion instanceof MCCommandStraightMotion){  // stright line before
								// calc length shortening of new line
								double d_l = this.kerf_offset_ * Math.sin(d_alfa/2.0);
								Point connectionPoint = lm.getEnd().clone();
								connectionPoint.shift(-d_l*Math.sin(alfaPrev), -d_l*Math.cos(alfaPrev));
								// correct previous line
								if(lm.length() <= d_l) 
									throw new InterpreterException("Previous line too short to current compensation");
								lm.setEnd(connectionPoint);
								// correct current line
								if(newCutterMotion.length() <= d_l) 
									throw new InterpreterException("New line too short to current compensation");
								newCutterMotion.setStart(connectionPoint);
							} else {
								// arc line before 
								MCCommandArcMotion arc = (MCCommandArcMotion)lastMotion;
								Point connectionPoint = getConnectionPoint(newCutterMotion, arc, ConnectionType.STARTEND);
								arc.setEnd(connectionPoint);
								newCutterMotion.setStart(connectionPoint);
							};
						};
					};
					break;
				case NONE:
				default:
					break;
				}
			}
		}
		commands_.add(newCutterMotion);
	}

	private void addCuttingArcMotion(G02_G03 command) throws InterpreterException {
		Object lastMotion = findLastMotion();
		MCCommandArcMotion newArcMotion = new MCCommandArcMotion(command,
					   								   this.kerf_offset_);
		if(lastMotion != null){ // its no first move
			if((lastMotion instanceof MCCommandStraightMotion)&&(!((MCCommandStraightMotion)lastMotion).isWorkingRun())) {
				// free run line should be connected to start of new motion
				((MCCommandStraightMotion)lastMotion).setEnd(newArcMotion.getStart());
			} else {
				// cutting motion before this
				MCCommandStraightMotion lm = (MCCommandStraightMotion)lastMotion;
				double alfaCurrent = newArcMotion.getStartTangentAngle();
				double alfaPrev = lm.getEndTangentAngle();
				final double d_alfa = alfaCurrent - alfaPrev;
				switch(command.getOffsetMode()){
				case LEFT:
					if(d_alfa > 0.0){
						// line turn left and left offset
						if(lastMotion instanceof MCCommandStraightMotion){  // Straight line before
							Point connectionPoint = getConnectionPoint(lm, newArcMotion, ConnectionType.ENDSTART);
							lm.setEnd(connectionPoint);
							newArcMotion.setStart(connectionPoint);
						} else {
							// arc line before 
							MCCommandArcMotion arc = (MCCommandArcMotion)lastMotion;
							Point connectionPoint = getConnectionPoint(arc, newArcMotion);
							arc.setEnd(connectionPoint);
							newArcMotion.setStart(connectionPoint);
						};
					} else {
						if(d_alfa < 0.0){
							// line turn right and left offset
							// linking arc with kerf offset radius needed
							MCCommandArcMotion newArc = new MCCommandArcMotion(lm.getEnd(),
									  								 newArcMotion.getStart(),
									  								 command.getStart(),
									  								 ArcDirection.COUNTERCLOCKWISE,
									  								 command);
							commands_.add(newArc);
						};
					}
					break;
				case RIGHT:
					if(d_alfa > 0.0){
						// line turn left and right offset
						// linking arc with kerf offset radius needed
						MCCommandArcMotion newArc = new MCCommandArcMotion(lm.getEnd(),
								  								 newArcMotion.getStart(),
								  								 command.getStart(),
								  								 ArcDirection.CLOCKWISE,
								  								 command);
						commands_.add(newArc);
					} else {
						if(d_alfa < 0.0){
							// line turn right and right offset
							if(lastMotion instanceof MCCommandStraightMotion){  // stright line before
								Point connectionPoint = getConnectionPoint(lm, newArcMotion, ConnectionType.ENDSTART);
								lm.setEnd(connectionPoint);
								newArcMotion.setStart(connectionPoint);
							} else { // arc line before 
								MCCommandArcMotion arc = (MCCommandArcMotion)lastMotion;
								Point connectionPoint = getConnectionPoint(arc, newArcMotion);
								arc.setEnd(connectionPoint);
								newArcMotion.setStart(connectionPoint);
							};
						};
					};
					break;
				case NONE:
				default:
					break;
				}
			}
		}
		commands_.add(newArcMotion);
	}
	
	private Object findLastMotion() {
		int size = commands_.size();
		for(int i = (size-1); i>0; i--){
			Object command = commands_.get(i);
			if(command instanceof MCCommandStraightMotion) return command;
			if(command instanceof MCCommandArcMotion) return command;
		}
		return null;
	}
	
	private enum ConnectionType {
		ENDSTART,
		STARTEND
	}
	
	private Point getConnectionPoint(MCCommandStraightMotion Line, MCCommandArcMotion Arc, ConnectionType type){
		// find connection point of line & circle nearest to end of one & start of another
		double rx = 0.0;
		double ry = 0.0;

		double arccx = Arc.getCenter().getX();
		double arccy = Arc.getCenter().getY();
		double dx = Arc.getStart().getX() - arccx;
		double dy = Arc.getStart().getY() - arccy;
		double arcr = Math.sqrt(dx*dx + dy*dy);

		double lsx = Line.getStart().getX();
		double lsy = Line.getStart().getY();
		double lex = Line.getEnd().getX();
		double ley = Line.getEnd().getY();

		double ldx = lex - lsx;
		double ldy = ley - lsy;
		
		if(Math.abs(ldx)>0){  // line is not vertical
			if(Math.abs(ldy)>0){ // line is not horizontal
				// get canonical formula (y = a*x + b) for line
				double a1 = ldy/ldx;
				double b1 = lsy - a1 * lsx;
				double aD = 1.0 + a1*a1;
				double byc = b1 - arccy;
				double bD = 2.d*(byc*a1 - arccx);
				double cD = arccx*arccx + byc*byc - arcr*arcr;
				double Det = bD*bD - 4.0*aD*cD;
				if(Det<0) Det = 0d;
				double rx1 = (-bD + Math.sqrt(Det))/2/aD;
				double rx2 = (-bD - Math.sqrt(Det))/2/aD;
				switch(type){
				case ENDSTART:
					if( Math.abs(rx1-lex) < Math.abs(rx2-lex) ) rx = rx1;
					else rx = rx2;
					break;
				case STARTEND:
					if( Math.abs(rx1-lsx) < Math.abs(rx2-lsx) ) rx = rx1;
					else rx = rx2;
					break;
				default:
					break;
				}
				ry = a1*rx + b1;
			} else { 
				// line is horizontal 
				// connection is at point with y of line
				ry = ley;
				double t = ry - arccy;
				t = arcr*arcr - t*t;
				if(t>0){
					t = Math.sqrt(t);
					double rx1 = arccx + t;
					double rx2 = arccx - t;
					switch(type){
					case ENDSTART:
						if(Math.abs(rx1-lex) < Math.abs(rx2-lex)) rx = rx1;
						else rx = rx2;
						break;
					case STARTEND:
					default:
						if(Math.abs(rx1-lsx) < Math.abs(rx2-lsx)) rx = rx1;
						else rx = rx2;
						break;
					}
				} else { // tangent line
					 rx = arccx;
				}
			};
		} else {
			// line is vertical
			// connection is at point with x of line
			rx = lex;
			double t = rx - arccx;
			t = arcr*arcr - t*t;
			if(t>0){
				t = Math.sqrt(t);
				double ry1 = arccy + t;
				double ry2 = arccy - t;
				switch(type){
				case ENDSTART:
					if(Math.abs(ry1-ley) < Math.abs(ry2-ley)) ry = ry1;
					else ry = ry2;
					break;
				case STARTEND:
				default:
					if(Math.abs(ry1-lsy) < Math.abs(ry2-lsy)) ry = ry1;
					else ry = ry2;
					break;
				}
			} else { // tangent line
				 ry = arccy;
			}
		}
		return new Point(rx, ry);
	}

	private Point getConnectionPoint(MCCommandArcMotion A1, MCCommandArcMotion A2){
		Point result;
		
		double dxsa1 = A1.getStart().getX() - A1.getCenter().getX();
		double dysa1 = A1.getStart().getY() - A1.getCenter().getY();
		double dxea1 = A1.getEnd().getX() - A1.getCenter().getX();
		double dyea1 = A1.getEnd().getY() - A1.getCenter().getY();
		double r2a1 = dxsa1*dxsa1 + dysa1*dysa1;
		double ra1 = Math.sqrt(r2a1);

		double dxsa2 = A2.getStart().getX() - A2.getCenter().getX();
		double dysa2 = A2.getStart().getY() - A2.getCenter().getY();
		double r2a2 = dxsa2*dxsa2 + dysa2*dysa2;
		double ra2 = Math.sqrt(r2a2);
		
		double dxc = A2.getCenter().getX() - A1.getCenter().getX();
		double dyc = A2.getCenter().getY() - A1.getCenter().getY();
		double ac = Math.atan2(dyc, dxc);
		double r2c = dxc*dxc + dyc*dyc;
		double rc = Math.sqrt(r2c);
		
		double overlap = rc - ra1 - ra2;
		double meps = 0.001; //Drawing.DwgConst.masheps;
		if(Math.abs(overlap) < meps){ // centers offseted, one connection point
			double xcp = A2.getStart().getX();
			double ycp = A2.getStart().getY();
			result = new Point(xcp, ycp); 
		} else { 
			if(rc < meps){ // centers are equal
				double ae1 = Math.atan2(dyea1, dxea1);
				double as2 = Math.atan2(dysa2, dxsa2);
				double acp = (ae1 + as2)/2d;
				double xcp = A1.getCenter().getX() + ra1*Math.cos(acp);
				double ycp = A1.getCenter().getY() + ra1*Math.sin(acp);
				result = new Point(xcp, ycp); 
			} else {	// different centers, two connection point
				double d1 = (r2a1 - r2a2 + r2c)/(2d*rc);
				double t = r2a1 - d1*d1;
				if(t<0) t = 0;
				double h = Math.sqrt(t);
				double dah = Math.atan2(h, d1);
				// first point
				double xcp1 = A1.getCenter().getX() + ra1*Math.cos(ac-dah);
				double ycp1 = A1.getCenter().getY() + ra1*Math.sin(ac-dah);
				// second point
				double xcp2 = A1.getCenter().getX() + ra1*Math.cos(ac+dah);
				double ycp2 = A1.getCenter().getY() + ra1*Math.sin(ac+dah);
				double dx1 = A1.getEnd().getX() - xcp1;
				double dy1 = A1.getEnd().getY() - ycp1;
				double dx2 = A1.getEnd().getX() - xcp2;
				double dy2 = A1.getEnd().getY() - ycp2;
				if((dx1*dx1 + dy1*dy1)<(dx2*dx2 + dy2*dy2)){ // choose nearest point
					result = new Point(xcp1, ycp1); 
				}else{
					result = new Point(xcp2, ycp2); 
				}
			}
		}
		return result;
	}

	@Override
	public void startProgram() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseProgram() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeProgram() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rewindProgram() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forewindProgram() {
		// TODO Auto-generated method stub
		
	}

}
