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

import Interpreter.InterpreterException;
import Interpreter.Motion.Point;
import Interpreter.State.CutterRadiusCompensation;

public class G00_G01 extends CanonCommand {
	
	// straight line & arc common fields
	protected Point start_;
	protected Point end_;

	private MotionMode mode_;
	private VelocityPlan velocityPlan_;

	public G00_G01(Point s,
				   Point e,
				   VelocityPlan vp,
				   MotionMode m) throws InterpreterException{
		// all motions are absolute to current home point
		// init fields
		super(CanonCommand.type.MOTION);
		if(s != null) start_ = s;
		else throw new InterpreterException("Null start point in motion command");
		if(e != null) end_ = e;
		else throw new InterpreterException("Null end point in motion command");
		setVelocityPlan(vp);
		mode_ = m;
	}
	
	public void applyCutterRadiusCompensation(CutterRadiusCompensation offsetMode){
		if(offsetMode.getMode() != CutterRadiusCompensation.mode.OFF) {
			double radius = offsetMode.getRadius();
			double alfa = getStartTangentAngle();
			if(offsetMode.getMode() != CutterRadiusCompensation.mode.LEFT) alfa += Math.PI/2;
			else alfa -= Math.PI/2;
			double dx = radius * Math.sin(alfa);
			double dy = radius * Math.cos(alfa);
			start_ = new Point(start_.getX()+dx, start_.getY()+dy);
			end_ = new Point(end_.getX()+dx, end_.getY()+dy);
		}
	}

	public Point getStart() {
		return start_;
	}

	public void setStart(Point p) {
		this.start_ = p;
	}

	public Point getEnd() {
		return end_;
	}

	public void setEnd(Point p) {
		this.end_ = p;
	}

	public MotionMode getMode() {
		return mode_;
	}

	public double getDX(){
		return end_.getX() - start_.getX();
	}
		
	public double getDY(){
		return end_.getY() - start_.getY();
	}
	
	public double length(){
		double dx = this.getDX();
		double dy = this.getDY();
		return Math.sqrt(dx*dx + dy*dy);
	}

	public double getStartTangentAngle() {
		return Math.atan2(this.getDY(), this.getDX());
	}
		
	public double getEndTangentAngle() {
		return getStartTangentAngle();
	}
		
	public boolean isWorkingRun(){
		return (this.getMode() == MotionMode.WORK);
	}

	public boolean isFreeRun(){
		return (this.getMode() == MotionMode.FREE);
	}

	public VelocityPlan getVelocityPlan() {
		return velocityPlan_;
	}

	private void setVelocityPlan(VelocityPlan vp) {
		this.velocityPlan_ = new VelocityPlan(vp.getStartVel(),vp.getEndVel());
	}

	public void truncHead(double dl/* length change */) throws InterpreterException{
		double l = length();
		if(l < dl ) throw new InterpreterException("Line too short for current compensation");
		else {
			double alfa = getEndTangentAngle();
			double dx = dl * Math.sin(alfa);			
			double dy = dl * Math.cos(alfa);
			Point newStart = new Point(this.getStart().getX()+dx,
									   this.getStart().getY()+dy);
			this.setStart(newStart);
		}
	}

	public void truncTail(double dl/* length change */) throws InterpreterException{
		double l = length();
		if(l < dl ) throw new InterpreterException("Line too short for current compensation");
		else {
			double alfa = getEndTangentAngle();
			double dx = dl * Math.sin(alfa);			
			double dy = dl * Math.cos(alfa);
			Point newEnd = new Point(this.getEnd().getX()-dx,
									 this.getEnd().getY()-dy);
			this.setEnd(newEnd);
		}
	}

	public G00_G01 newSubLine(double lengthStart, double lengthEnd) throws InterpreterException {
		
		Point newStart = start_;
		Point newEnd = end_;
		double l = this.length();
		
		if(lengthStart > 0.0){ // change start point
			double x = start_.getX();
			double y = start_.getY();
			double a = getStartTangentAngle();
			x += lengthStart*Math.sin(a);
			y += lengthStart*Math.cos(a);
			newStart = new Point(x,y);
		}		
		
		if(lengthEnd < l){  // change end point
			double x = newEnd.getX();
			double y = newEnd.getY();
			double a = getEndTangentAngle();
			x += (l-lengthEnd)*Math.sin(a);
			y += (l-lengthEnd)*Math.cos(a);
			newEnd = new Point(x,y);
		}

		return new G00_G01(newStart, newEnd, this.velocityPlan_, this.mode_);
	}

}
