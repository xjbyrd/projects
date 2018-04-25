package com.cooksys.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.model.Customer;

@Component
@Transactional
public class LoginService
{
	@Autowired
	SessionFactory sessionFactory;
	
	/**
	 * Checks login information.  If a match is found returns 1 otherwise returns 0.
	 * 
	 * @param username Username of client
	 * @param password Password of client
	 * @return 1 if match was found, otherwise returns 0
	 */
	@SuppressWarnings("unchecked")
	public int login(String username, String password)
	{
		List<Customer> actors = sessionFactory.getCurrentSession()		// Gets current session from sessionFactory
					.createCriteria(Customer.class)						// Creates criteria for the Customer class
					.add(Restrictions.eq("username", username))			// Adds restriction username = username
					.add(Restrictions.eq("password", password))			// Adds restriction password = password
					.list();
		
		if (actors.size() > 0)
			return 1;
		else
			return 0;
	}
	
	/**
	 * Checks registration information.  If a duplicate is found returns 0 otherwise returns 1.
	 * 
	 * @param username Username of client
	 * @param password Password of client
	 * @return 1 if registration was successful, otherwise returns 0
	 */
	public int register(String username, String password)
	{
		int isFound = this.login(username, password);
		
		if (isFound == 1)
			return 0;
		else
		{
			sessionFactory.getCurrentSession().save(new Customer(username, password));
			return 1;
		}
	}
}
