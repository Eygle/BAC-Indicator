package edu.csulb.bacindicator.games.pint;

/**
 * Created by Brian on 30/04/2015.
 */
public class PintSpeed {
	
	public static final int DIRECTION_RIGHT	= 2;
	public static final int DIRECTION_LEFT	= -2;
	public static final int DIRECTION_UP	= -2;
	public static final int DIRECTION_DOWN	= 2;
	
	private double xv = 1.5;
	private double yv = 1.5;
	
	private int xDirection = DIRECTION_RIGHT;
	private int yDirection = DIRECTION_DOWN;
	
	public PintSpeed() {
		this.xv = 1.5;
		this.yv = 1.5;
	}

	public PintSpeed(float xv, float yv) {
		this.xv = xv;
		this.yv = yv;
	}

	public double getXv() {
		return xv;
	}
	public void setXv(float xv) {
		this.xv = xv;
	}
	public double getYv() {
		return yv;
	}
	public void setYv(float yv) {
		this.yv = yv;
	}

	public int getxDirection() {
		return xDirection;
	}
	public void setxDirection(int xDirection) {
		this.xDirection = xDirection;
	}
	public int getyDirection() {
		return yDirection;
	}
	public void setyDirection(int yDirection) {
		this.yDirection = yDirection;
	}

	public void toggleXDirection() {
		xDirection = xDirection * -1;
	}

	public void toggleYDirection() {
		yDirection = yDirection * -1;
	}

}
