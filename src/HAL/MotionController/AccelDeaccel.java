package HAL.MotionController;

import Interpreter.InterpreterException;

public interface AccelDeaccel {
	
	public void setVelocityProfile(double startVel, 
								   double endVel, 
								   double maxAccel) throws InterpreterException;
	public void setStartVel(double v);
	public double getStartVel();
	public void setEndVel(double v);
	public double getEndVel();

}
