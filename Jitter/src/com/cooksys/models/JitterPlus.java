package com.cooksys.models;

public class JitterPlus
{
	private int id;
	private String name;
	private String jitter;
	
	public JitterPlus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JitterPlus(String name, String jitter) {
		super();
		this.name = name;
		this.jitter = jitter;
	}
	public JitterPlus(int id, String name, String jitter) {
		super();
		this.id = id;
		this.name = name;
		this.jitter = jitter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJitter() {
		return jitter;
	}
	public void setJitter(String jitter) {
		this.jitter = jitter;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
