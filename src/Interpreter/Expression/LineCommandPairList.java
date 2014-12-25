package Interpreter.Expression;

import Interpreter.InterpreterException;
import Interpreter.Expression.CommandPair.CNCWordEnum;

public class LineCommandPairList {

	private CommandPair[] commandPairList = new CommandPair[CNCWordEnum.Z.ordinal()+1];
	
	public
	LineCommandPairList(){
		for(int i=0; i<commandPairList.length; i++) 
			commandPairList[i] = null;
	};

	public void addWord(CNCWordEnum w, CommandPair e) throws InterpreterException{ 
		int n = w.ordinal();
		if(commandPairList[n] == null) commandPairList[n] = e;
		else throw new InterpreterException("Twice parameter");
	}

	public int getLength() {
		return commandPairList.length;
	}

	public CommandPair get(int i){
		return commandPairList[i];
	}

	@Override
	public String toString(){
		String result = "";
		
		for(int i=0; i<this.commandPairList.length; i++){
			CommandPair currentWord = this.commandPairList[i];
			if(currentWord != null) result += " " + currentWord.toString();
		}
		
		return result;
	}

}
