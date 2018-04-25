package com.cooksys.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.handler.FlightJMS;
import com.cooksys.handler.LocationGenerator;
import com.cooksys.handler.PathFinder;
import com.cooksys.model.Flight;
import com.cooksys.model.Location;
import com.cooksys.wrapper.FlightWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class controls receives 
 * 
 * @author Jason Byrd
 */

@RestController
public class FlightController
{
	@Autowired
	LocationGenerator locationGenerator;
	
	@Autowired
	PathFinder pathFinder = new PathFinder();
	
	@Autowired
	FlightJMS flightJMS = FlightJMS.getInstance();
	
	/**
	 * Gets locations from the locationGenerator and sends them to the client.
	 * 
	 * @return List of all locations planes fly to and from
	 */
	@RequestMapping(value = "/location", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Location> getLocations()
	{	
		List<Location> locations = locationGenerator.getLocations();
		Collections.sort(locations);
		return locations;
	}
	
	/**
	 * Takes an origin, destination, and list of flights from client and 
	 * sends it to the PathFinder to calculate all the shortest paths
	 * between the two.
	 * 
	 * @param vals Json string values for origin, destination, and a list of Flights
	 * @return A list of lists of flights that are all the shortest path between two points
	 */
	@RequestMapping(value="/flightPath", method=RequestMethod.GET)
	public List<List<Flight>> login(@RequestParam Map<String, Object> vals)
	{	
		Gson gson = new Gson();
		
		// Transforms the origin and destination from Json string to object
		Location origin = gson.fromJson(vals.get("origin").toString(), Location.class);
		Location destination = gson.fromJson(vals.get("destination").toString(), Location.class);

		// Gets the Json string value for the list of flights
		String jsonString = vals.get("flights").toString();
		// Parses out the hashKey value that was added to it in the JavaScript
		jsonString = jsonString.replaceAll(",\"..hashKey\":\"object:[0-9]*\"", "");
		// Gets the class type for a "List<Flight>"
		Type listType = new TypeToken<ArrayList<Flight>>(){}.getType();
		// Transforms Json string value to object value
		List<Flight> flights = gson.fromJson(jsonString, listType);
		
		return pathFinder.getShortestPath(origin, destination, flights);
	}
	
	/**
	 * Get the list of Alerts sent by the JMS and returns it to client.
	 * 
	 * @return List of FlightWrappers that contain the status of various flights
	 */
	@RequestMapping(value = "/alerts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FlightWrapper> getAlerts()
	{	
		return flightJMS.getAlerts();
	}
}
