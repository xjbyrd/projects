package com.cooksys.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.models.Jitter;
import com.cooksys.models.Jitterer;
import com.cooksys.services.JitterService;
import com.cooksys.wrappers.JitterPlusWrapper;
import com.cooksys.wrappers.JittererPlusWrapper;
import com.cooksys.wrappers.JittererScoped;

@RestController
public class JittererController
{
	@Autowired
	JittererScoped jts;

	@Autowired
	JitterService js;

	/**
	 * Pulls values for username and password from the webpage and saves them to the database.
	 * 
	 * @param vals A Map of the values comming from the webpage containing the username and password
	 * @return Integer value indicating if the registration was successful or not
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int Register(@RequestBody Map<String, String> vals)
	{
		if (!(vals.get("newusername").toString() == "" || vals.get("newpassword").toString() == ""))
		{
			int x = jts.register(vals.get("newusername").toString(), vals.get("newpassword").toString());
			return x;
		}
		else
		{
			return 1;
		}
	}

	/**
	 * Pulls username and password fields from the webpage and checks if there is a match in the database
	 * 
	 * @param vals Map of username and password from webpage
	 * @return returns the jitterer which is null if there was no match
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public Jitterer login(@RequestParam Map<String, String> vals)
	{
		Jitterer j = jts.login(vals.get("username").toString(), vals.get("password").toString());
		jts.SetJitterer(j);
		return j;
	}

	/** Pulls the top 15 Jitters from the database and returns them to webpage
	 * 
	 * @return Wrapper that contains a list of JitterPlus which contain the username from the Jitterer class
	 * 		   is returned to the webpage
	 */
	//=================================== GET TOP 15 JITTERS==================================================
	@RequestMapping(value = "/jitter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JitterPlusWrapper getTopJitters()
	{
		JitterPlusWrapper jpw = js.getGlobalJitters();

		return jpw;
	}

	//==================================== POST JITTER =========================================================
	@RequestMapping(value = "/jitter/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int postJitter(@RequestBody Map<String, String> vals)
	{	
		Jitter jt = new Jitter();
		jt.setJitter(vals.get("jitter").toString());

		jt.setJitterer(jts.getJitterer());
		if(jt.getJitter().length() <= 140)
		{
			js.postJitter(jt);
			return 0;
		}
		else
		{
			return 1;
		}

	}

	/**
	 * Pulls top 15 Jitters of everyone that has accepted users follow request and returns it to website
	 * 
	 * @return Wrapper with all the friends Jitters with their usernames included
	 */
	//============================== GET ALL FRIENDS ==========================================================
	@RequestMapping(value = "/jitter/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JitterPlusWrapper getFriendsJitters()
	{
		JitterPlusWrapper jpw = js.getFriendsJitters(jts.getJitterer());

		return jpw;
	}

	/**
	 * Gets id from webpage and searches database for a match and returns if there is a match
	 * 
	 * @param vals Map containing id sent from database
	 * @return returns Jitterer to the webpage if found
	 */
	//====================== Search BY ID ==========================
	@RequestMapping(value="/jitter/search", method=RequestMethod.GET)
	public Jitterer searchByID(@RequestParam Map<String, String> vals)
	{
		Jitterer j = js.searchByID(Integer.parseInt(vals.get("id").toString()));

		return j;
	}

	/**
	 * Gets the id of the friend you want to follow from webpage and uses the id of the persistant Jittrer
	 * in the controller to send a friend request 
	 * 
	 * @param vals ID of the friend you want to follow from the webpage
	 * @return success or failure based on if the webpage sent empty data
	 */
	//====================================== FRIENT REQUEST ===========================================================
	@RequestMapping(value = "/jitter/request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int friendRequest(@RequestBody Map<String, String> vals)
	{	
		if (vals.get("id").toString() != "")
		{
			js.friendRequest(jts.getJitterer(), Integer.parseInt(vals.get("id").toString()));

			return 0;
		}
		else
		{
			return 1;
		}

	}

	/**
	 * Pulls the people who have requested to follow user from database and returns them to webpage
	 * 
	 * @return String that has all of the Jitterers that have requested to follow user to database
	 */
	//================ GET FRIEND REQUESTS =============================
	@RequestMapping(value = "/jitter/friend", method = RequestMethod.GET)
	public String getFriendRequests()
	{
		StringBuilder sb = new StringBuilder("");
		JittererPlusWrapper jpw = new JittererPlusWrapper();
		jpw = js.getFriendRequests(jts.getJitterer());

		for (Jitterer j : jpw.getJl())
		{
			sb.append("<option id=\"" + j.getId() + "\">" + j.getUsername() + "</option>");
		}

		return sb.toString();
	}

	/**
	 * Gets the id of the friend user has accepted from webpage and changes value in database from requesting to following
	 * 
	 * @param vals Map with the value of the id from the webpage
	 */
	//============================== FRIENT ACCEPT ==================================================================
	@RequestMapping(value = "/jitter/accept", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void friendAccept(@RequestBody Map<String, String> vals)
	{	
		js.friendAccept(jts.getJitterer().getId(), Integer.parseInt(vals.get("id").toString()));
	}

	/**
	 * Gets the Jitters of user from the database and returns them to webpage
	 * 
	 * @return wrapper with username and jitter data to the webpage
	 */
	//========================== GET MY JITTERS ==========================================================================
	@RequestMapping(value = "/jitter/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JitterPlusWrapper getMYJitters()
	{
		JitterPlusWrapper jpw = js.getMyJitters(jts.getJitterer());

		return jpw;
	}

}
