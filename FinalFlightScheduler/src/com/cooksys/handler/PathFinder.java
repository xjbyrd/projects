package com.cooksys.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.Flight;
import com.cooksys.model.Location;

/**
 * Takes in an origin, a destination, and a list of flights and returns all
 * of the paths that are equal to the shortest paths time.
 * 
 * @author Jason Byrd
 */

@Component
@Scope("singleton")
public class PathFinder
{
	private List<List<Flight>> minPaths = new ArrayList<List<Flight>>();
	private List<Flight> flights = new ArrayList<Flight>();
	int minPathDays = 0;
	
	/**
	 * Takes in an origin, a destination, and a list of flights and returns all
	 * of the paths that are equal to the shortest paths time.
	 * 
	 * @param origin Starting point for flight
	 * @param destination Ending point for flight
	 * @param flights List of flights from one place to another
	 * @return List of Lists of shortest flight paths
	 */
	public List<List<Flight>> getShortestPath(Location origin, Location destination, List<Flight> flights)
	{	
		minPaths.clear();
		List<Flight> currentPath = new ArrayList<Flight>();
		this.flights = flights;
		for(Flight currentFlight : flights)
		{
			// Checks for an origin point in flights that matches origin
			if (currentFlight.getOrigin().getCity().equals(origin.getCity()) && 
				currentFlight.getOrigin().getState().equals(origin.getState()))
			{		
				findShortestPaths(currentFlight, destination, currentPath);
			}
		}
		
		return minPaths;
	}
	
	/**
	 * Recursively finds all of the flight paths that have the shortest time.
	 * 
	 * @param currentFlight The current flight that is being itterated through
	 * @param destination The final destination that is the end goal
	 * @param currentPath List of flights that have already been travered through
	 */
	private void findShortestPaths(Flight currentFlight, Location destination, List<Flight> currentPath)
	{
		currentPath.add(currentFlight);
		
		// Gets duration until destination for currentFlight
		int currentDay = currentFlight.getDeparture() + currentFlight.getEta();
				
		// Checks to see the the destination of the current flight matches the final destination
		if (currentFlight.getDestination().getCity().equals(destination.getCity()) && 
			currentFlight.getDestination().getState().equals(destination.getState()))
		{
			// Checks to see if there has been a minPath found yet
			
			if (!(minPaths.size() > 0))
			{
				List<Flight> flightCopy = new ArrayList<Flight>(currentPath);
				minPaths.add(flightCopy);
				minPathDays = currentDay;
			}
			else
			{
				// Min path has already been found
						
				if (currentDay < minPathDays)
				{
					// New better minPath has been found, clears previous paths and saves currentPath
					minPathDays = currentDay;
					minPaths.clear();
					List<Flight> flightCopy = new ArrayList<Flight>(currentPath);
					minPaths.add(flightCopy);
				}
				else if (currentDay == minPathDays)
				{
					// currentPath is same as minPath
					List<Flight> flightCopy = new ArrayList<Flight>(currentPath);
					minPaths.add(flightCopy);
				}
			}
		}
		else
		{
			for (Flight nextFlight : flights)
			{
				// Checks for an origin point in nextFlight that matches origin in currentFlight
				// and checks that the next flight starts after current flight arrives
				if (currentFlight.getDestination().getCity().equals(nextFlight.getOrigin().getCity()) && 
					currentFlight.getDestination().getState().equals(nextFlight.getOrigin().getState()) &&
					nextFlight.getDeparture() >= currentDay)
				{
					// Recursive call to check next flight
					findShortestPaths(nextFlight, destination, currentPath);
				}
			}
		}
		// Removes flight from currentPath once it has been through all connecting flights
		currentPath.remove(currentFlight);
	}
}
