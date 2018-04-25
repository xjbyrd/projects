package com.cooksys.model;

import java.io.Serializable;

public class Location implements Serializable, Comparable<Location>{
	private static final long serialVersionUID = 1L;
	
	private String state;
	private String city;
	
	public Location(){
		super();
	}
	public Location(String state, String city) {
		super();
		this.state = state;
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	@Override
	public int compareTo(Location location)
	{
		int test = this.getState().compareTo(location.getState());
		if (test < 0)
		{
			return -1;
		}
		else if(test > 0)
		{
			return 1;
		}
		else
		{
			test = this.getCity().compareTo(location.getCity());
			
			if (test < 0)
			{
				return -1;
			}
			else if (test >0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
			
		}
		
	}
	
	
}
