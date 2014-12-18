package Interpreter.State;

import Interpreter.Expression.Variables.VariablesSet;
import Interpreter.Motion.Point;
import Interpreter.Motion.Attributes.CoordinateSystem;
import Interpreter.Motion.Attributes.DistanceMode;
import Interpreter.Motion.Attributes.LengthUnits;
import Interpreter.Motion.Attributes.TimeFormat;
import Interpreter.State.CannedCycle.CannedCycleReturnMode;
import Interpreter.State.Coolant.Coolant;
import Interpreter.State.FeedRate.FeedRate;
import Interpreter.State.Spindle.Spindle;
import Interpreter.State.Tools.Tool;
import Interpreter.State.Tools.ToolHeight;
import Interpreter.State.Tools.ToolRadius;
import Interpreter.State.Tools.ToolSet;

public class InterpreterState {

	public static VariablesSet vars_ = new VariablesSet();

	public static boolean IsBlockDelete = true;

	private static Point referencePoint = new Point(0.0,0.0);
	private static Point controlPoint = new Point(0.0,0.0);
	private static double currentFeedRate_ = 0.0; // max velocity mm in sec

	public static ToolSet toolSet_ = new ToolSet();
	public static Tool tool = new Tool();
	public static ToolRadius cutterRadius = new ToolRadius();
	public static ToolHeight toolHeight = new ToolHeight();

	public static Spindle spindle = new Spindle();
	public static Coolant coolant = new Coolant();

	public static FeedRate feedRate = new FeedRate();
	public static DistanceMode distanceMode;
	public static LengthUnits lengthUnits;
	public static CoordinateSystem coordinateSystem = new CoordinateSystem();

	public static TimeFormat timeFormat;
	public static CannedCycleReturnMode cycleReturnMode;

	public static double getCurrentFeedRate() {
		return InterpreterState.currentFeedRate_;
	}
	
	public void setCurrentFeedRate(double currentFeedRate) {
		InterpreterState.currentFeedRate_ = currentFeedRate;
	}
	
	public static double getHomePointX() {
		return InterpreterState.referencePoint.getX();
	}
	
	public static double getHomePointY() {
		return InterpreterState.referencePoint.getY();
	}
	
	public static void setHomePoint(double X, double Y) {
		InterpreterState.referencePoint.setX(InterpreterState.referencePoint.getX() + X);
		InterpreterState.controlPoint.setX(InterpreterState.controlPoint.getX() - X);
		InterpreterState.referencePoint.setY(InterpreterState.referencePoint.getY() + Y);
		InterpreterState.controlPoint.setY(InterpreterState.controlPoint.getY() - Y);
	}

	public static DistanceMode getDistanceMode() {
		return InterpreterState.distanceMode;
	}

	public static void setDistanceMode(DistanceMode newMode) {
		InterpreterState.distanceMode = newMode;
	}

}
