package HAL.MotionController;

import Drivers.CanonicalCommands.ArcDirection;
import Drivers.CanonicalCommands.G00_G01;
import Drivers.CanonicalCommands.G02_G03;
import Drivers.CanonicalCommands.OffsetMode;
import Interpreter.InterpreterException;
import Interpreter.Motion.Point;

public class MCCommandArcMotion 
			extends G02_G03 
			implements AccelDeaccel {
	
	private G00_G01 prototype_;
	private double startVel_ = 0.0;
	private double endVel_ = 0.0;
	
	public MCCommandArcMotion(Point start, 
							  Point end,
							  Point center,
							  ArcDirection dir,
							  G00_G01 prototype) throws InterpreterException {
		super(start, 
			  end, 
			  center, 
			  dir, 
			  prototype.getVelocity(), 
			  prototype.getMode(),
			  prototype.getOffsetMode());
		this.prototype_ = prototype;
	}

	public MCCommandArcMotion(G02_G03 prototype,
						 	  double kerf_offset) throws InterpreterException {
		super(prototype.getStart().clone(),
			  prototype.getEnd().clone(), 
			  prototype.getCenter(), 
			  prototype.getArcDirection(), 
			  prototype.getVelocity(), 
			  prototype.getMode(),
			  prototype.getOffsetMode());
		this.prototype_ = prototype;

		OffsetMode offMode = prototype_.getOffsetMode();
		if(offMode != OffsetMode.NONE){
			double dx_start = this.getStart().getX() - this.getCenter().getX();
			double dy_start = this.getStart().getY() - this.getCenter().getY();
			double a_start = Math.atan2(dy_start, dx_start);
			double dx_end = this.getEnd().getX() - this.getCenter().getX();
			double dy_end = this.getEnd().getY() - this.getCenter().getY();
			double a_end = Math.atan2(dy_end, dx_end);
			double radius = Math.sqrt(dx_start*dx_start + dy_start*dy_start);
			if(((offMode == OffsetMode.LEFT)&&(this.getArcDirection() == ArcDirection.CLOCKWISE))
			 ||((offMode == OffsetMode.RIGHT)&&(this.getArcDirection() == ArcDirection.COUNTERCLOCKWISE))){ // external arc offset
				this.start_.shift(kerf_offset*Math.sin(a_start), kerf_offset*Math.cos(a_start));
				this.end_.shift(kerf_offset*Math.sin(a_end), kerf_offset*Math.cos(a_end));
			} else { // internal kerf offset
				this.start_.shift(-kerf_offset*Math.sin(a_start), -kerf_offset*Math.cos(a_start));
				this.end_.shift(-kerf_offset*Math.sin(a_end), -kerf_offset*Math.cos(a_end));
			}
		}
	}

	public G00_G01 getPrototype() {
		return prototype_;
	}

	public MCCommandArcMotion newSubArc(double lengthStart, double lengthEnd) throws InterpreterException {
		Point newStart = this.getStart();
		Point newEnd = this.getEnd();
		Point center = this.getCenter();
		double l = this.length();
		double r = this.radius();
		
		double x = center.getX();
		double y = center.getY();
		
		if(lengthStart > 0.0){ // change start point
			double a = lengthStart/2.0/Math.PI/r;
			if(this.getArcDirection() == ArcDirection.CLOCKWISE) a = this.getStartRadialAngle() - a;
			else a = this.getStartRadialAngle() + a;
			x += r*Math.sin(a);
			y += r*Math.cos(a);
			newStart = new Point(x,y);
		}		
		
		if(lengthEnd < l){  // change end point
			double a = lengthEnd/2.0/Math.PI/r;
			if(this.getArcDirection() == ArcDirection.CLOCKWISE) a = this.getEndRadialAngle() + a;
			else a = this.getEndRadialAngle() - a;
			x += r*Math.sin(a);
			y += r*Math.cos(a);
			newEnd = new Point(x,y);
		}

		return new MCCommandArcMotion(newStart, newEnd, center, this.getArcDirection(), this.getPrototype());
	}

	@Override
	public double getStartVel() {
		return startVel_;
	}

	@Override
	public void setStartVel(double startVel) {
		this.startVel_ = startVel;
	}

	@Override
	public double getEndVel() {
		return endVel_;
	}

	@Override
	public void setEndVel(double endVel) {
		this.endVel_ = endVel;
	}

	@Override
	public void setVelocityProfile(double startVel, 
								   double endVel,
								   double maxAccel) throws InterpreterException {
		// check required acceleration
		double l = this.length();
		double dT = (endVel + startVel)/2.0/l;
		double ac = (endVel - startVel)/dT;
		if(Math.abs(ac) <= maxAccel){ // acceleration in limits
			// check aditional velocity changes at arc
			double r = this.radius();
			// calc angle velocity
			double startW = startVel/r;
			double endW = endVel/r;
			double aW = (startW - endW)/dT;
			if(this.angle() >= Math.PI){ // large arc, covering Pi/2 sector - simple case
				
			} else {}
			this.setStartVel(startVel);
			this.setEndVel(endVel);
		} else throw new InterpreterException("Acceleration out of limits");
	}
	
	public double getMaxVelicity(double maxAccel){
		double r = this.radius();
		if(this.angle() >= Math.PI){ // large arc, covering Pi/2 sector - simple case
			return Math.sqrt(r*maxAccel); 
		} else {
			// TODO accurete solutions needed
			return Math.sqrt(r*maxAccel);
		}
	}

}
