package InterpreterCommands;

public class Point {
	
	private double x_;
	private double y_;
	
	public Point(double x, double y){
		this.x_ = x;
		this.y_ = y;
	}

	public double getX() {
		return x_;
	}

	public void setX(double x) {
		this.x_ = x;
	}

	public double getY() {
		return y_;
	}

	public void setY(double y) {
		this.y_ = y;
	}
	
	public void shift(double dX, double dY){
		this.x_ += dX;
		this.y_ += dY;
	}
	
	public Point clone(){
		return new Point(this.x_, this.y_);
	}

}
