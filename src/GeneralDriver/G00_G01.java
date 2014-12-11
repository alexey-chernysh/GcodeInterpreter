package GeneralDriver;

import Exceptions.HWCException;
import InterpreterCommands.Point;

public class G00_G01 extends GCommand {
	
	private MotionMode mode_;
	protected Point start_;
	protected Point end_;
	private Velocity velocity_;
	private OffsetMode offsetMode_; 
	
	public G00_G01(Point start,
				   Point end,
				   Velocity velocity,
				   MotionMode mode,
				   OffsetMode offsetMode) throws HWCException{ // all motions are absolute to current home point
		if(start != null) this.start_ = start;
		else throw new HWCException("Null start point in motion command");
		if(end != null) this.end_ = end;
		else throw new HWCException("Null end point in motion command");
		this.velocity_ =  velocity;
		this.mode_ = mode;
		this.offsetMode_ = offsetMode;
	}

	public Point getStart() {
		return start_;
	}

	public void setStart(Point p) {
		this.start_ = p;
	}

	public Point getEnd() {
		return end_;
	}

	public void setEnd(Point p) {
		this.end_ = p;
	}

	public MotionMode getMode() {
		return mode_;
	}

	public Velocity getVelocity() {
		return velocity_;
	}

	public double getDX(){
		return end_.getX() - start_.getX();
	}
		
	public double getDY(){
		return end_.getY() - start_.getY();
	}
	
	public double length(){
		double dx = this.getDX();
		double dy = this.getDY();
		return Math.sqrt(dx*dx + dy*dy);
	}

	public double getStartTangentAngle() {
		return Math.atan2(this.getDY(), this.getDX());
	}
		
	public double getEndTangentAngle() {
		return getStartTangentAngle();
	}
		
	public boolean isWorkingRun(){
		return (this.getMode() == MotionMode.WORK);
	}

	public OffsetMode getOffsetMode() {
		return offsetMode_;
	}

	public void setOffsetMode(OffsetMode offsetMode) {
		this.offsetMode_ = offsetMode;
	}

}
