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

public class G00_G01 extends CanonCommand {
	
	private MotionMode mode_;
	protected Point start_;
	protected Point end_;
	private double velocity_;
	private OffsetMode offsetMode_; 
	
	public G00_G01(Point start,
				   Point end,
				   double velocity,
				   MotionMode mode,
				   OffsetMode offsetMode) throws InterpreterException{ // all motions are absolute to current home point
		if(start != null) this.start_ = start;
		else throw new InterpreterException("Null start point in motion command");
		if(end != null) this.end_ = end;
		else throw new InterpreterException("Null end point in motion command");
		if(start == end) throw new InterpreterException("Null distance in motion command");
		this.velocity_ =  velocity;
		this.mode_ = mode;
		this.offsetMode_ = new OffsetMode(offsetMode.getMode(), offsetMode.getRadius());
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

	public double getVelocity() {
		return velocity_;
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

	public OffsetMode getOffsetMode() {
		return offsetMode_;
	}

	public void setOffsetMode(OffsetMode offsetMode) {
		this.offsetMode_ = offsetMode;
	}

}
