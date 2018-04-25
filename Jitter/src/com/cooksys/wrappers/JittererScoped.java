package com.cooksys.wrappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.cooksys.models.Jitterer;
import com.cooksys.services.JittererService;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class JittererScoped
{
	@Autowired
	JittererService js;
	
	Jitterer j;
	
	public int register(String username, String password)
	{
		return js.register(username, password);
	}
	
	public Jitterer login(String username, String password)
	{
		return js.login(username, password);
	}
	
	public void SetJitterer (Jitterer j)
	{
		this.j = j;
	}
	
	public Jitterer getJitterer()
	{
		return j;
	}
	
	
	
	
}
