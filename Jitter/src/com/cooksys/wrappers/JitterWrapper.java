package com.cooksys.wrappers;

import java.util.List;

import com.cooksys.models.Jitter;

public class JitterWrapper
{
	List<Jitter> jitters;

	public JitterWrapper()
	{
		
	}

	public JitterWrapper(List<Jitter> jitters)
	{
		this.jitters = jitters;
	}

	public List<Jitter> getJitters()
	{
		return jitters;
	}

	public void setJitters(List<Jitter> jitters)
	{
		this.jitters = jitters;
	}
}
