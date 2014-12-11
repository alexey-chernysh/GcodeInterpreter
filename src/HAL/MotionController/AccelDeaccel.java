package HAL.MotionController;

import Exceptions.HWCException;

public interface AccelDeaccel {
	
	public void setVelocityProfile(double startVel, 
								   double endVel, 
								   double maxAccel) throws HWCException;
	public void setStartVel(double v);
	public double getStartVel();
	public void setEndVel(double v);
	public double getEndVel();

}
