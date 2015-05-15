package edu.csulb.bacindicator.games.pint;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Brian
 */
public class PintElement {

	private Bitmap bitmap; 
	private int x;
	private int y;
	private boolean touched;
	private PintSpeed speed;

	public PintElement(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new PintSpeed();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public PintSpeed getSpeed() {
		return speed;
	}

	public void setSpeed(PintSpeed speed) {
		this.speed = speed;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2),
				y - (bitmap.getHeight() / 2), null);
	}

	public void update() {
		if (!touched) {
			x += (speed.getXv() * speed.getxDirection());
			y += (speed.getYv() * speed.getyDirection());
		}
	}

	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (x - bitmap.getWidth() / 2)
				&& (eventX <= (x + bitmap.getWidth() / 2))) {
			if (eventY >= (y - bitmap.getHeight() / 2)
					&& (y <= (y + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}

	}

	public boolean checkIfIn(int xP, int yP) {

		if (xP >= (x - bitmap.getWidth() / 2)
				&& (xP <= (x + bitmap.getWidth() / 2))) {
			return yP >= (y - bitmap.getHeight() / 2)
					&& (y <= (y + bitmap.getHeight() / 2));
		}
		return false;
	}
}