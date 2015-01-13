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

package Interpreter.Motion.Attributes;

import Interpreter.Motion.Motion;


public class CoordinateSystem {
	
	private CoordinateMode coordinateMode = CoordinateMode.NORMAL;
	public CNCCoordinateRotation rotation = new CNCCoordinateRotation();

	private Motion currentMotion_ = Motion.UNDEFINED;
	
	public Motion getCurrentMotion() {
		return this.currentMotion_;
	}

	public void setCurrentMotion(Motion currentMotion_) {
		this.currentMotion_ = currentMotion_;
	}

	public class CNCCoordinateRotation {
		private double coord_x = 0.0;
		private double coord_y = 0.0;
		private double angle = 0.0;
		public void set(double x, double y, double a){
			this.coord_x = x;
			this.coord_y = y;
			this.angle   = a;
		}
		public void set_inc(double x, double y, double a){
			this.coord_x = x;
			this.coord_y = y;
			this.angle  += a;
		}
	}

	public CoordinateMode getCoordinateMode() { return coordinateMode; }
	public void setCoordinateMode(CoordinateMode curentMode_) { this.coordinateMode = curentMode_; }

	public enum CoordinateMode {
		NORMAL,
		POLAR;
	}

}
