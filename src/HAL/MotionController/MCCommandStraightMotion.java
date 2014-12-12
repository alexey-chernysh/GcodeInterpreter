package HAL.MotionController;

import Exceptions.HWCException;
import GeneralDriver.G00_G01;
import GeneralDriver.OffsetMode;
import Interpreter.Motion.Point;

public class MCCommandStraightMotion 
		extends G00_G01 
		implements AccelDeaccel {

	private final G00_G01 prototype_;
	private double startVel_ = 0.0;
	private double endVel_ = 0.0;
	
	public MCCommandStraightMotion(Point start, 
						 		   Point end, 
						 		   G00_G01 prototype) throws HWCException {
		super(start, 
			  end, 
			  prototype.getVelocity(), 
			  prototype.getMode(),
			  prototype.getOffsetMode());
		this.prototype_ = prototype;
	}

	public MCCommandStraightMotion(G00_G01 prototype, double kerf_offset) throws HWCException {
		super(prototype.getStart().clone(), 
			  prototype.getEnd().clone(), 
			  prototype.getVelocity(), 
			  prototype.getMode(),
			  prototype.getOffsetMode());
		this.prototype_ = prototype;
		OffsetMode offMode = prototype_.getOffsetMode();
		if(offMode != OffsetMode.NONE) {
			double alfa = prototype.getStartTangentAngle();
			if(offMode != OffsetMode.LEFT) alfa += Math.PI/2;
			else alfa -= Math.PI/2;
			double dx = kerf_offset*Math.sin(alfa); 
			double dy = kerf_offset*Math.cos(alfa);
			this.getStart().shift(dx, dy);
			this.getEnd().shift(dx, dy);
		}
	}

	public MCCommandStraightMotion newSubLine(double lengthStart, double lengthEnd) throws HWCException {
		
		Point newStart = this.getStart();
		Point newEnd = this.getEnd();
		double l = this.length();
		
		if(lengthStart > 0.0){ // change start point
			double x = newStart.getX();
			double y = newStart.getY();
			double a = this.getStartTangentAngle();
			x += lengthStart*Math.sin(a);
			y += lengthStart*Math.cos(a);
			newStart = new Point(x,y);
		}		
		
		if(lengthEnd < l){  // change end point
			double x = newEnd.getX();
			double y = newEnd.getY();
			double a = this.getEndTangentAngle();
			x += (l-lengthEnd)*Math.sin(a);
			y += (l-lengthEnd)*Math.cos(a);
			newEnd = new Point(x,y);
		}

		return new MCCommandStraightMotion(newStart, newEnd, this.getPrototype());
	}
	
	public G00_G01 getPrototype() {	return prototype_; }

	@Override
	public double getStartVel() { return startVel_;	}

	@Override
	public void setStartVel(double startVel) { this.startVel_ = startVel; }

	@Override
	public double getEndVel() {	return endVel_;	}

	@Override
	public void setEndVel(double endVel) { this.endVel_ = endVel; }

	@Override
	public void setVelocityProfile(double startVel, 
								   double endVel,
								   double maxAccel) throws HWCException {
		// check required acceleration
		double l = this.length();
		double dT = (endVel + startVel)/2.0/l;
		double ac = (endVel - startVel)/dT;
		if(Math.abs(ac) <= maxAccel){ // acceleration in limits
			this.setStartVel(startVel);
			this.setEndVel(endVel);
		} else throw new HWCException("Acceleration out of limits");
	}

}
