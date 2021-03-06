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

package Interpreter.Expression;

import java.util.ArrayList;

public class CommandPairList extends ArrayList<CommandPair> {
	
	public void addCommand(CommandPair e){
		this.add(e);
	}

	@Override
	public String toString(){
		String result = "";
		for(int i=0; i<this.size(); i++){
			CommandPair currentCommand = this.get(i);
			result += " " + currentCommand.toString();
		}
		result += ";";
		return result;
	}

}
