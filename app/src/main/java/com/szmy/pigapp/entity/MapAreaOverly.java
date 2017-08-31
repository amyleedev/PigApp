package com.szmy.pigapp.entity;

import java.io.Serializable;

public class MapAreaOverly implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2360970377013837143L;
	private int amount;
	private String area;
	private double x;
	private double y;
	private int zts;

	public int getZts() {
		return zts;
	}

	public void setZts(int zts) {
		this.zts = zts;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
