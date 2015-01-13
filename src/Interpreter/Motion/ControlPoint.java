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

package Interpreter.Motion;

import Interpreter.Motion.Attributes.Axis;


public class ControlPoint {
	
	private double[] position_ = new double[Axis.n_of_axis];

	public ControlPoint(){
		for(int i=0; i<Axis.n_of_axis; i++)
			position_[i] = 0.0;
		
	}

	public double get(int i) {
		return this.position_[i];
	}
/*
	public double get(char c) throws GcodeRuntimeException {
		return this.position_[Axis.getNumByChar(c)];
	}
*/
	public ControlPoint set(int n, double x) {
		this.position_[n] = x;
		return this;
	}
/*
	public ControlPoint set(char c, double x) throws GcodeRuntimeException {
		this.position_[Axis.getNumByChar(c)] = x;
		return this;
	}
*/
}
