package Drivers.CanonicalCommands;

import Exceptions.HWCException;
import Interpreter.Motion.Point;

public class G02_G03 extends G00_G01 {
	
	protected Point center_;
	private ArcDirection arcDirection_;
	public static final double arcTol = 0.000001;

	public G02_G03(Point startPoint,
				   Point endPoint, 
				   Point centerPoint, 
				   ArcDirection arcDirection,
				   Velocity velocity,
				   MotionMode mode,
				   OffsetMode offsetMode) throws HWCException {
		super(startPoint, endPoint, velocity, mode, offsetMode);

		this.center_ = centerPoint;
		this.arcDirection_ = arcDirection;

	}

	public double getStartRadialAngle() {
		double dx = this.getStart().getX() - this.getCenter().getX();
		double dy = this.getStart().getY() - this.getCenter().getY();
		return Math.atan2(dy, dx);
	}

	@Override
	public double getStartTangentAngle() {
		double alfa = getStartRadialAngle();
		return Radial2Tangent(alfa);
	}
		
	public double getEndRadialAngle() {
		double dx = this.getEnd().getX() - this.getCenter().getX();
		double dy = this.getEnd().getY() - this.getCenter().getY();
		return Math.atan2(dy, dx);
	}
	
	@Override
	public double getEndTangentAngle() {
		double alfa = getEndRadialAngle();
		return Radial2Tangent(alfa);
	}
	
	private double Radial2Tangent(double alfa){
		if(this.getArcDirection() == ArcDirection.CLOCKWISE){
			alfa += Math.PI/2.0;
			if(alfa > Math.PI) alfa -= 2.0 * Math.PI;
		} else {
			alfa -= Math.PI/2.0;
			if(alfa > Math.PI) alfa += 2.0 * Math.PI;
		};
		return alfa;
	}
		
	public ArcDirection getArcDirection() {
		return arcDirection_;
	}

	public Point getCenter() {
		return center_;
	}

	public double radius(){
		double dx = start_.getX() - center_.getX();
		double dy = start_.getY() - center_.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double angle(){
		double alfa1 = getStartRadialAngle();
		double alfa2 = getEndRadialAngle();
		return (alfa2 - alfa1);
	}
	
	@Override
	public double length(){
		return 2.0 * Math.PI * this.radius() / Math.abs(this.angle());
	}

}
