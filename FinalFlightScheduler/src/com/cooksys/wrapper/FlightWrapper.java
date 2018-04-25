package com.cooksys.wrapper;

import com.cooksys.model.Flight;

/**
 * A simple POJO that combines the flight class with a boolean
 * indicating arrived or delayed.
 * 
 * @author Jason Byrd
 */

public class FlightWrapper
{
	private Flight flight;
	private boolean delayed;
	
	public FlightWrapper() { }
	
	public FlightWrapper(Flight flight, boolean delayed) 
	{
		this.flight = flight;
		this.delayed = delayed;
	}
	
	public Flight getFlight() 
	{
		return flight;
	}
	
	public void setFlight(Flight flight) 
	{
		this.flight = flight;
	}
	
	public boolean isDelayed() 
	{
		return delayed;
	}
	
	public void setDelayed(boolean delayed) 
	{
		this.delayed = delayed;
	}
}
