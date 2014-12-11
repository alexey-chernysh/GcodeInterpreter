package CNCExpression;

import CNCExpression.CNCWord.CNCWordEnum;
import Exceptions.GcodeRuntimeException;

public class CNCWordList {

	private CNCWord[] wordList = new CNCWord[CNCWordEnum.Z.ordinal()+1];
	
	public
	CNCWordList(){
		for(int i=0; i<wordList.length; i++) 
			wordList[i] = null;
	};

	public void addWord(CNCWordEnum w, CNCWord e) throws GcodeRuntimeException{ 
		int n = w.ordinal();
		if(wordList[n] == null) wordList[n] = e;
		else throw new GcodeRuntimeException("Twice parameter");
	}

	public int getLength() {
		return wordList.length;
	}

	public CNCWord get(int i){
		return wordList[i];
	}

	@Override
	public String toString(){
		String result = "";
		
		for(int i=0; i<this.wordList.length; i++){
			CNCWord currentWord = this.wordList[i];
			if(currentWord != null) result += " " + currentWord.toString();
		}
		
		return result;
	}

}
