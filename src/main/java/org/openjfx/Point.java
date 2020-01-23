package org.openjfx;

public class Point {
	private int id;
	private double x;
	private double y;

	Point(int i, double xx, double yy){
		id = i; x = xx; y = yy;
	}

	Point(){id = 0; x = 0; y = 0; }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
