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

package Interpreter.State.CannedCycle;

public class CannedCycleReturnMode {
	
	private double R_ = 0.0;
	private ReturnMode currentMode_ = ReturnMode.UNDEFINED;

	public double getR() {
		return this.R_;
	}

	public void setR(double r) {
		this.R_ = r;
	}

	public ReturnMode getMode() {
		return this.currentMode_;
	}

	public void setMode(ReturnMode returnMode_) {
		this.currentMode_ = returnMode_;
	}
}
