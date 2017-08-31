package com.szmy.pigapp.pds;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Group extends Observable implements Observer{
	private String name;
	private boolean isChecked;
	private List<City> cityList = new ArrayList<City>();
	
	
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
	public List<City> getCityList() {
		return cityList;
	}
	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	public void changeChecked(){
		isChecked = !isChecked;
		setChanged();
		notifyObservers(isChecked);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		boolean flag = true;
		for (City city : cityList) {
			if (city.isChecked() == false) {
				flag = false;
			}
		}
		this.isChecked = flag;
	}
	
	
}
