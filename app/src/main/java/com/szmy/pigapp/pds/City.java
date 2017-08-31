package com.szmy.pigapp.pds;

import java.util.Observable;
import java.util.Observer;

public class City extends Observable implements Observer {
	private String name;
	private boolean isChecked;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public void changeChecked(){
		isChecked = !isChecked;
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof Boolean) {
			this.isChecked = (Boolean) data;
		}
	}
	
	
}
