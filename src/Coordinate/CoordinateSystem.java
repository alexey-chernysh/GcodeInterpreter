package Coordinate;

public class CoordinateSystem {
	
	private DistanceMode distanceMode = DistanceMode.RELATIVE;
	private CoordinateMode coordinateMode = CoordinateMode.NORMAL;
	public CNCCoordinateRotation rotation = new CNCCoordinateRotation();
	private Plane currentPlane_ = Plane.PLANE_XY;
/*
	private Motion currentMotion_ = Motion.UNDEFINED;
	
	public Motion getCurrentMotion() {
		return this.currentMotion_;
	}

	public void setCurrentMotion(Motion currentMotion_) {
		this.currentMotion_ = currentMotion_;
	}
*/
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

	public enum DistanceMode{
		ABSOLUTE,
		RELATIVE,
		UNDEFINED;
	}

	public enum Plane {
		PLANE_XY,
		PLANE_XZ,
		PLANE_YZ,
		UNDEFINED;
	}

	public enum CoordinateMode {
		NORMAL,
		POLAR;
	}
/*	
	public enum Offset {
		G10,
		G52,
		G92,
		G92_1,
		G92_2,
		G92_3,
		UNDEFINED;
	}
	
	public enum Motion {
		G0,
		G1,
		G2,
		G3,
		G12,
		G13,
		G80,
		G81,
		G82,
		G83,
		G84,
		G85,
		G86,
		G87,
		G88,
		UNDEFINED;
	}
*/
}
