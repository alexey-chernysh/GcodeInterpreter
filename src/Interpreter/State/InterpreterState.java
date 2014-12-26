package Interpreter.State;

import Interpreter.InterpreterException;
import Interpreter.Expression.Variables.VariablesSet;
import Interpreter.Motion.Point;
import Interpreter.Motion.Attributes.CoordinateSystem;
import Interpreter.Motion.Attributes.TimeFormat;
import Interpreter.State.CannedCycle.CannedCycleReturnMode;
import Interpreter.State.ModalState.ModalState;

public class InterpreterState {

	public static VariablesSet vars_ = new VariablesSet();

	public static boolean IsBlockDelete = true;

	private static Point homePosition = new Point(0.0,0.0);
	private static Point lastPosition = new Point(0.0,0.0);
	private static double currentFeedRate_ = 0.0; // max velocity mm in sec
	private static double cutterRadius = 0.0;

	public static ModalState modalState = new ModalState();
	
	public static ToolSet toolSet = new ToolSet();
	public static int G4142_D = 0; // tool number for cutting radius compensation

	public static Spindle spindle = new Spindle();
	public static FeedRate feedRate = new FeedRate();
	public static CoordinateSystem coordinateSystem = new CoordinateSystem();

	public static TimeFormat timeFormat;
	public static CannedCycleReturnMode cycleReturnMode;
	
	public InterpreterState() throws InterpreterException{
		modalState.initToDefaultState();
	};

	public static double getCurrentFeedRate() {
		return InterpreterState.currentFeedRate_;
	}
	
	public void setCurrentFeedRate(double currentFeedRate) {
		InterpreterState.currentFeedRate_ = currentFeedRate;
	}
	
	public static double getHomePointX() {
		return InterpreterState.homePosition.getX();
	}
	
	public static double getHomePointY() {
		return InterpreterState.homePosition.getY();
	}
	
	public static void setHomePoint(double X, double Y) {
		InterpreterState.homePosition.setX(InterpreterState.homePosition.getX() + X);
		InterpreterState.lastPosition.setX(InterpreterState.lastPosition.getX() - X);
		InterpreterState.homePosition.setY(InterpreterState.homePosition.getY() + Y);
		InterpreterState.lastPosition.setY(InterpreterState.lastPosition.getY() - Y);
	}

	public static Point getLastPosition() {
		return InterpreterState.lastPosition;
	}

	public static void setLastPosition(Point newPos) {
		InterpreterState.lastPosition = newPos;
	}

	public static void setCutterRadius(double offset) {
		cutterRadius = offset;
	}

}
