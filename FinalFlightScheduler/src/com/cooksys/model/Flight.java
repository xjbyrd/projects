package com.cooksys.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Flight implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final AtomicInteger NEXTID = new AtomicInteger(1);
	
	private Integer flightId;
	private Location origin;
	private Location destination;
	private Integer departure;
	private Integer eta;
	
	public static Integer getNextFlightID(){
		return NEXTID.incrementAndGet();
	}
	
	public Integer getFlightId() {
		return flightId;
	}
	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}
	public Location getOrigin() {
		return origin;
	}
	public void setOrigin(Location origin) {
		this.origin = origin;
	}
	public Location getDestination() {
		return destination;
	}
	public void setDestination(Location destination) {
		this.destination = destination;
	}
	public Integer getDeparture() {
		return departure;
	}
	public void setDeparture(Integer departure) {
		this.departure = departure;
	}
	public Integer getEta() {
		return eta;
	}
	public void setEta(Integer eta) {
		this.eta = eta;
	}
}
