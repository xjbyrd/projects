package com.cooksys.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A simple POJO to interact with the database.
 * 
 * @author Jason Byrd
 *
 */

@Entity
@Table(name = "customer", catalog = "flight")
public class Customer
{
	Integer customerId;
	private String username;
	private String password;
	
	public Customer() { }

	public Customer(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Customer(Integer customerId, String username, String password) {
		super();
		this.customerId = customerId;
		this.username = username;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "customer_id", unique = true, nullable = false)
	public Integer getCustomerId() 
	{
		return customerId;
	}

	public void setCustomerId(Integer customerId) 
	{
		this.customerId = customerId;
	}

	@Column(name = "username", nullable = false, length = 100)
	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 100)
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
}
