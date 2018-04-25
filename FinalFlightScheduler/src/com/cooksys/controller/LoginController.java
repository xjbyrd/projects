package com.cooksys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.service.LoginService;

/**
 * Registers new users and logs in existing users to the server.
 * 
 * @author Jason Byrd
 */

@RestController
public class LoginController
{
	@Autowired
	LoginService loginService;
	
	/**
	 * Receives username and password from client and checks it against the database
	 * 
	 * @param vals username and password as Map values
	 * @return 1 if they are found and 0 if the are not
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public int login(@RequestParam Map<String, String> vals)
	{	
		if(vals.isEmpty() || !vals.containsKey("username") || !vals.containsKey("password"))
		{
			return 0;
		}
		else
		{
			return loginService.login(vals.get("username").toString(), vals.get("password").toString());
		}
	}
	
	/**
	 * Receives username and password from client and saves it to database if not a duplicate
	 * and logs user in.
	 * 
	 * @param vals username and password as Map values
	 * @return 1 if successful and 0 if not
	 */
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public int register(@RequestParam Map<String, String> vals)
	{	
		if(vals.isEmpty() || !vals.containsKey("newUsername") || !vals.containsKey("newPassword"))
		{
			return 0;
		}
		else
		{
			return loginService.register(vals.get("newUsername").toString(), vals.get("newPassword").toString());
		}
	}
}
