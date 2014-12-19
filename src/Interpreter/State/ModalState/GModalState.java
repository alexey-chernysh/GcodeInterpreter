package Interpreter.State.ModalState;

import Exceptions.GcodeRuntimeException;

public class GModalState {
	
	private GcommandSet[] state_;
		
	public GModalState(){
		state_ = new GcommandSet[GcommandModalGroupSet.G_GROUP0_NON_MODAL.ordinal()];
		for(int i=0; i<GcommandModalGroupSet.G_GROUP0_NON_MODAL.ordinal(); i++)
			state_[i] = GcommandSet.GDUMMY;
	};
		
	public void initToDefaultState() throws GcodeRuntimeException{
		set(GcommandModalGroupSet.G_GROUP1_MOTION, GcommandSet.G0);
		set(GcommandModalGroupSet.G_GROUP2_PLANE, GcommandSet.G17);
		set(GcommandModalGroupSet.G_GROUP3_DISTANCE_MODE, GcommandSet.G91);
		set(GcommandModalGroupSet.G_GROUP4_ARC_DISTANCE_MODE, GcommandSet.G91_1);
		set(GcommandModalGroupSet.G_GROUP5_FEED_RATE_MODE, GcommandSet.G94);
		G21();
		set(GcommandModalGroupSet.G_GROUP7_CUTTER_RADIUS_COMPENSATION, GcommandSet.G40);
		set(GcommandModalGroupSet.G_GROUP8_TOOL_LENGHT_OFFSET, GcommandSet.G49);
		set(GcommandModalGroupSet.G_GROUP10_CANNED_CYCLES_RETURN_MODE, GcommandSet.G98);
		set(GcommandModalGroupSet.G_GROUP12_OFFSET_SELECTION, GcommandSet.G54_DUMMY);
		set(GcommandModalGroupSet.G_GROUP13_PATH_CONTROL_MODE, GcommandSet.G61);
		set(GcommandModalGroupSet.G_GROUP16_COORDINATE_ROTATION, GcommandSet.G69);
		set(GcommandModalGroupSet.G_GROUP17_POLAR_COORDINATES, GcommandSet.G16);
		set(GcommandModalGroupSet.G_GROUP18_SCALING, GcommandSet.G50);
	};
	
	public void set(GcommandModalGroupSet group, GcommandSet command) throws GcodeRuntimeException{
		if(group != GcommandModalGroupSet.G_GROUP0_NON_MODAL){
			if(command.modalGroup == group){
				state_[group.ordinal()] = command;
			} else throw new GcodeRuntimeException("Changing modal state with command from another modal group");
		} else throw new GcodeRuntimeException("Assiment non modal command to modal state");
		
	}
	
	public GcommandSet getGModalState(GcommandModalGroupSet group){
		return state_[group.ordinal()];
	}
	
	public void G20() throws GcodeRuntimeException{
		this.set(GcommandModalGroupSet.G_GROUP6_UNITS, GcommandSet.G20);
	}
	
	public void G21() throws GcodeRuntimeException{
		this.set(GcommandModalGroupSet.G_GROUP6_UNITS, GcommandSet.G21);
	}
	
	public double toMM(double x) throws GcodeRuntimeException{
		GcommandSet unitsState = this.getGModalState(GcommandModalGroupSet.G_GROUP6_UNITS);
		switch(unitsState){
		case G20:
			return 25.4*x;
		case G21:
			return x;
		default:
			throw new GcodeRuntimeException("Scaling to mesurement units request before units selextion");
		}
	};
	
}