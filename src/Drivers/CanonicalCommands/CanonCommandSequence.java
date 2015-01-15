/*
 * Copyright 2014-2015 Alexey Chernysh
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package Drivers.CanonicalCommands;

import java.util.ArrayList;

import Drivers.Cutter.CutterTorchOnOff;
import Interpreter.InterpreterException;
import Interpreter.Motion.Point;

public class CanonCommandSequence {
	
	private ArrayList<CanonCommand> seq_;
	
	public CanonCommandSequence(){
		seq_ = new ArrayList<CanonCommand>();
	}
	
	public void add(CanonCommand command) throws InterpreterException{
		if(command.getType() == CanonCommand.type.MOTION){
			if(command instanceof G00_G01){
				if(!((G00_G01) command).isWorkingRun()){
					addFreeMotion((G00_G01) command);
				} else {
					addCuttingStraightMotion((G00_G01) command);
				}
			} else {
				if(command instanceof G02_G03) {
					addCuttingArcMotion((G02_G03)command);
				} else {
					throw new InterpreterException("Unsupported command");
				}
			}
		} else {
			seq_.add(command);
		}
	}
	
	public int size(){
		return seq_.size();
	}
	
	public CanonCommand get(int i){
		return seq_.get(i);
	}

	private void addFreeMotion(G00_G01 command) throws InterpreterException {
		G00_G01 lastMotion = (G00_G01)findLastMotion();
		if(lastMotion != null){ 
			// last motion is straight or arc working run, link stright motion may be needed
			Point lastEnd = lastMotion.getCRCOend();
			Point newStart = command.getCRCOstart();
			if(newStart.getDistance(lastEnd) > 0.0){
				G00_G01 link = new G00_G01(lastEnd, 
										   newStart, 
										   command.getVelocityPlan(), 
										   command.getMode(), 
										   command.getOffsetMode());
				seq_.add(link);
			}
		}
		seq_.add(command);
	}

	private void addCuttingStraightMotion(G00_G01 command) throws InterpreterException {
		command.createCRCOpoints();
		G00_G01 lastMotion = (G00_G01)findLastMotion();
		if(lastMotion != null){ // its no first move
			if(lastMotion.isFreeRun()) {
				// free run line should be connected to start of new motion
				Point lastEnd = lastMotion.getCRCOend();
				Point newStart = command.getCRCOstart();
				if(newStart.getDistance(lastEnd) > 0.0){
					G00_G01 link = new G00_G01(lastEnd, newStart, command.getVelocityPlan(), command.getMode(), command.getOffsetMode());
					seq_.add(link);
				}
			} else {
				// cutting motion before this
				double alfaCurrent = command.getStartTangentAngle();
				double alfaPrev = lastMotion.getEndTangentAngle();
				final double d_alfa = alfaCurrent - alfaPrev;
				switch(command.getOffsetMode().getMode()){
				case LEFT:
					if(d_alfa > 0.0){
						// line turn left and left offset
						if(lastMotion instanceof G00_G01){  // Straight line before
							// calculate length shortening of new line
							double d_l = command.getOffsetMode().getRadius() * Math.sin(d_alfa/2.0);
							Point connectionPoint = lastMotion.getEnd().clone();
							connectionPoint.shift(-d_l*Math.sin(alfaPrev), -d_l*Math.cos(alfaPrev));
							// correct previous line
							if(lastMotion.length() <= d_l) 
								throw new InterpreterException("Previous line too short to current compensation");
							lastMotion.setEnd(connectionPoint);
							// correct current line
							if(command.length() <= d_l) 
								throw new InterpreterException("New line too short to current compensation");
							command.setStart(connectionPoint);
						} else {
							// arc line before 
							G02_G03 arc = (G02_G03)lastMotion;
							Point connectionPoint = getConnectionPoint(newCutterMotion, arc, ConnectionType.STARTEND);
							arc.setEnd(connectionPoint);
							newCutterMotion.setStart(connectionPoint);
						};
					} else {
						if(d_alfa < 0.0){
							// line turn right and left offset
							// linking arc with kerf offset radius needed
							G02_G03 newArc = new G02_G03(lastMotion.getEnd(),
									  							 	 newCutterMotion.getStart(),
									  							 	 command.getStart(),
									  							 	 ArcDirection.COUNTERCLOCKWISE,
									  							 	 command);
							seq_.add(newArc);
						};
					}
					break;
				case RIGHT:
					if(d_alfa > 0.0){
						// line turn left and right offset
						// linking arc with kerf offset radius needed
						G02_G03 newArc = new G02_G03(lastMotion.getEnd(),
								  								 newCutterMotion.getStart(),
								  								 command.getStart(),
								  								 ArcDirection.CLOCKWISE,
								  								 command);
						seq_.add(newArc);
					} else {
						if(d_alfa < 0.0){
							// line turn right and right offset
							if(lastMotion instanceof G00_G01){  // stright line before
								// calc length shortening of new line
								double d_l = command.getOffsetMode().getRadius() * Math.sin(d_alfa/2.0);
								Point connectionPoint = lastMotion.getEnd().clone();
								connectionPoint.shift(-d_l*Math.sin(alfaPrev), -d_l*Math.cos(alfaPrev));
								// correct previous line
								if(lastMotion.length() <= d_l) 
									throw new InterpreterException("Previous line too short to current compensation");
								lastMotion.setEnd(connectionPoint);
								// correct current line
								if(newCutterMotion.length() <= d_l) 
									throw new InterpreterException("New line too short to current compensation");
								newCutterMotion.setStart(connectionPoint);
							} else {
								// arc line before 
								G02_G03 arc = (G02_G03)lastMotion;
								Point connectionPoint = getConnectionPoint(newCutterMotion, arc, ConnectionType.STARTEND);
								arc.setEnd(connectionPoint);
								newCutterMotion.setStart(connectionPoint);
							};
						};
					};
					break;
				case OFF:
				default:
					break;
				}
			}
		}
		seq_.add(command);
	}

	private void addCuttingArcMotion(G02_G03 command) throws InterpreterException {
		Object lastMotion = findLastMotion();
		G02_G03 newArcMotion = new G02_G03(command);
		if(lastMotion != null){ // its no first move
			if((lastMotion instanceof G00_G01)&&(!((G00_G01)lastMotion).isWorkingRun())) {
				// free run line should be connected to start of new motion
				((G00_G01)lastMotion).setEnd(newArcMotion.getStart());
			} else {
				// cutting motion before this
				G00_G01 lm = (G00_G01)lastMotion;
				double alfaCurrent = newArcMotion.getStartTangentAngle();
				double alfaPrev = lastMotion.getEndTangentAngle();
				final double d_alfa = alfaCurrent - alfaPrev;
				switch(command.getOffsetMode().getMode()){
				case LEFT:
					if(d_alfa > 0.0){
						// line turn left and left offset
						if(lastMotion instanceof G00_G01){  // Straight line before
							Point connectionPoint = getConnectionPoint(lm, newArcMotion, ConnectionType.ENDSTART);
							lastMotion.setEnd(connectionPoint);
							newArcMotion.setStart(connectionPoint);
						} else {
							// arc line before 
							G02_G03 arc = (G02_G03)lastMotion;
							Point connectionPoint = getConnectionPoint(arc, newArcMotion);
							arc.setEnd(connectionPoint);
							newArcMotion.setStart(connectionPoint);
						};
					} else {
						if(d_alfa < 0.0){
							// line turn right and left offset
							// linking arc with kerf offset radius needed
							G02_G03 newArc = new G02_G03(lastMotion.getEnd(),
									  								 newArcMotion.getStart(),
									  								 command.getStart(),
									  								 ArcDirection.COUNTERCLOCKWISE,
									  								 command);
							seq_.add(newArc);
						};
					}
					break;
				case RIGHT:
					if(d_alfa > 0.0){
						// line turn left and right offset
						// linking arc with kerf offset radius needed
						G02_G03 newArc = new G02_G03(lastMotion.getEnd(),
								  								 newArcMotion.getStart(),
								  								 command.getStart(),
								  								 ArcDirection.CLOCKWISE,
								  								 command);
						seq_.add(newArc);
					} else {
						if(d_alfa < 0.0){
							// line turn right and right offset
							if(lastMotion instanceof G00_G01){  // stright line before
								Point connectionPoint = getConnectionPoint(lm, newArcMotion, ConnectionType.ENDSTART);
								lastMotion.setEnd(connectionPoint);
								newArcMotion.setStart(connectionPoint);
							} else { // arc line before 
								G02_G03 arc = (G02_G03)lastMotion;
								Point connectionPoint = getConnectionPoint(arc, newArcMotion);
								arc.setEnd(connectionPoint);
								newArcMotion.setStart(connectionPoint);
							};
						};
					};
					break;
				case OFF:
				default:
					break;
				}
			}
		}
		seq_.add(newArcMotion);
	}
	
	private Object findLastMotion() {
		int size = seq_.size();
		for(int i = (size-1); i>0; i--){
			Object command = seq_.get(i);
			if(command instanceof G00_G01) return command;
			if(command instanceof G02_G03) return command;
		}
		return null;
	}
	
	private enum ConnectionType {
		ENDSTART,
		STARTEND
	}
	
	private Point getConnectionPoint(G00_G01 Line, G02_G03 Arc, ConnectionType type){
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

	private Point getConnectionPoint(G02_G03 A1, G02_G03 A2){
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

}
