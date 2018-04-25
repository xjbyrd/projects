package com.cooksys.services;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.models.Jitterer;

@Component
@Transactional
public class JittererService
{
	@Autowired
	SessionFactory sf;
	
	public int register(String username, String password)
	{
		Jitterer j = login(username, password);
		
		if (j == null)
		{
			sf.getCurrentSession().save(new Jitterer(username, password));
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Jitterer login(String username, String password)
	{
		List<Jitterer> lb = sf.getCurrentSession()
				 .createCriteria(Jitterer.class)
				 .add(Restrictions.eq("password", password))
				 .add(Restrictions.eq("username", username))
				 .list();
		
		if (lb.size() > 0)
			return lb.get(0);
		else
			return null;
	}
}
