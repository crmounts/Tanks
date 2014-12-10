
public class Vector {
	
	private double x;
	private double y;
	
	public Vector(double inX, double inY) {
		x = inX;
		y = inY;
	}
	
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	public double getDirection() {
		return Math.toDegrees(Math.atan(y/x));
	}
	
	// Return the unit vector
	public Vector unitVect() {
		double mag = getMagnitude();
		return new Vector(x/mag,y/mag);
	}
	
	public Vector scale(int c) {
		return new Vector(x * c, y *c);
	}
	
	// Getters
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	// Setters
	public void setX(double inX) {
		x = inX;
	}
	
	public void setY(double inY) {
		y = inY;
	}
	
	// Vector operations
	public static void addVect(Vector v1, Vector v2) {
		v1.x = v1.x + v2.x; 
		v1.y = v1.y + v2.y;
	}
	
	public static void scaleVector(Vector v1, double c) {
		v1.x = v1.x * c;
		v1.y = v1.y * c;
	}
	
	
	
	
	// Debugging method
	public String toString() {
		return x + " " + y;
	}
}