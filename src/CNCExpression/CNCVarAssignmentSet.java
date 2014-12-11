package CNCExpression;

import java.util.ArrayList;

public class CNCVarAssignmentSet extends ArrayList<CNCVarAssignment> {

	@Override
	public String toString(){
		String result = "";
		
		for(int i=0; i<this.size(); i++){
			CNCVarAssignment currentVar = this.get(i);
			result += " " + currentVar.toString();
		}
		
		return result;
	}

}
