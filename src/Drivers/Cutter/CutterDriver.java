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
	
	private ArrayList<CanonCommand> commands_ = null; 

	public CutterDriver(){
	}
	
	
	@Override
	public void loadProgram(ArrayList<CanonCommand> sourceCommands) throws InterpreterException{
		
		commands_ = sourceCommands;
		addAccelDeaccel();
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
