package InterpreterCommands;

import CNCExpression.CNCVariables;
import InterpreterCommands.MotionAttributes.DistanceMode;
import InterpreterCommands.MotionAttributes.MesurementUnits;
import InterpreterCommands.Tools.ToolSet;

public class InterpreterState {

	public static CNCVariables vars_ = new CNCVariables();

	private static double coorinateScale = 1.0;
	private static DistanceMode distanceMode;
	private static double kerfOffsetRadius = 0.0;
	public static boolean IsBlockDelete = true;

	private static Point referencePoint = new Point(0.0,0.0);
	private static Point controlPoint = new Point(0.0,0.0);
	private static double currentFeedRate_ = 0.0; // max velocity mm in sec

	public static ToolSet toolSet_;

	public static Object feedRate;

	public static MesurementUnits coordinateSystem;

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

	public static MesurementUnits getCoorinateSystem() {
		if(InterpreterState.coorinateScale == 1.0) return MesurementUnits.METRIC;
		else return MesurementUnits.IMPERIAL;
	}

	public static void setCoorinateSystem(MesurementUnits coordinateSystem) {
		if(coordinateSystem == MesurementUnits.METRIC) InterpreterState.coorinateScale = 1.0;
		else InterpreterState.coorinateScale = 25.4;
	}

	public static DistanceMode getDistanceMode() {
		return InterpreterState.distanceMode;
	}

	public static void setDistanceMode(DistanceMode newMode) {
		InterpreterState.distanceMode = newMode;
	}

	public static double getKerfOffsetRadius() {
		return InterpreterState.kerfOffsetRadius;
	}

	public static void setKerfOffsetRadius(double newRadius) {
		InterpreterState.kerfOffsetRadius = newRadius;
	}
}
