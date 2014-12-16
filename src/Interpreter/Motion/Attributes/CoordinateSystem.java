package Interpreter.Motion.Attributes;

import Interpreter.Motion.Motion;


public class CoordinateSystem {
	
	private DistanceMode distanceMode = DistanceMode.INCREMENTAL;
	private CoordinateMode coordinateMode = CoordinateMode.NORMAL;
	public CNCCoordinateRotation rotation = new CNCCoordinateRotation();
	private Plane currentPlane_ = Plane.XY;

	private Motion currentMotion_ = Motion.UNDEFINED;
	
	public Motion getCurrentMotion() {
		return this.currentMotion_;
	}

	public void setCurrentMotion(Motion currentMotion_) {
		this.currentMotion_ = currentMotion_;
	}

	public Plane getPlane() { 
		return currentPlane_;	
	}
	
	public void setPlane(Plane plane) {	
		this.currentPlane_ = plane;	
	}
	
	public class CNCCoordinateRotation {
		private double coord_x = 0.0;
		private double coord_y = 0.0;
		private double angle = 0.0;
		public void set(double x, double y, double a){
			this.coord_x = x;
			this.coord_y = y;
			this.angle   = a;
		}
		public void set_inc(double x, double y, double a){
			this.coord_x = x;
			this.coord_y = y;
			this.angle  += a;
		}
	}

	public CoordinateMode getCoordinateMode() { return coordinateMode; }
	public void setCoordinateMode(CoordinateMode curentMode_) { this.coordinateMode = curentMode_; }

	public DistanceMode get() {	return this.distanceMode; }
	public void set(DistanceMode mode_) { this.distanceMode = mode_; };

	public enum CoordinateMode {
		NORMAL,
		POLAR;
	}

}
