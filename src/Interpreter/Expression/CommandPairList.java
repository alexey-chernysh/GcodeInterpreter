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
		
		return result;
	}

}
