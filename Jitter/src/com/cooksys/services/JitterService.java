package com.cooksys.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.models.Friend;
import com.cooksys.models.Jitter;
import com.cooksys.models.JitterPlus;
import com.cooksys.models.Jitterer;
import com.cooksys.wrappers.JitterPlusWrapper;
import com.cooksys.wrappers.JitterWrapper;
import com.cooksys.wrappers.JittererPlusWrapper;

@Component
@Transactional
public class JitterService
{
	@Autowired
	SessionFactory sf;
	
	@SuppressWarnings("unchecked")
	public JitterPlusWrapper getGlobalJitters()
	{
		JitterWrapper jw = new JitterWrapper();
		List<JitterPlus> jpl = new ArrayList<JitterPlus>();
		jw.setJitters(sf.getCurrentSession().createCriteria(Jitter.class)
                							.addOrder(Order.desc("id"))
                							.setMaxResults(15).list());
		
		for (Jitter j: jw.getJitters())
		{
			Jitterer jt = getJittererByJitterId(j.getId());
			JitterPlus jp = new JitterPlus();
			jp.setName(jt.getUsername());
			jp.setJitter(j.getJitter());
			jpl.add(jp);
		}
		
		JitterPlusWrapper jpw = new JitterPlusWrapper();
		jpw.setJpl(jpl);
		
		return jpw;
	}
	
	public Jitterer getJittererByJitterId(int id)
	{
		return (Jitterer) sf.getCurrentSession()
                .createSQLQuery("select jitterer.id, jitterer.username, jitterer.password "
          		         + "from jitterer "
          		         + "join jitter on jitterer.id=jitter.jitterer_id "
          		         + "where jitter.id=" + id)
          .addEntity(Jitterer.class).list().get(0);
	}
	
	public Jitterer getJittererByFriendId(int id)
	{
		return (Jitterer) sf.getCurrentSession()
                .createSQLQuery("select jitterer.id, jitterer.username, jitterer.password "
          		         + "from jitterer "
          		         + "join friend on jitterer.id=friend.jitterer "
          		         + "where friend.jitterer=" + id)
          .addEntity(Jitterer.class).list().get(0);
	}
	
	@SuppressWarnings("unchecked")
	public JitterPlusWrapper getFriendsJitters(Jitterer jit)
	{
		List<JitterPlus> fullList = new ArrayList<JitterPlus>();
		List<JitterPlus> finalList = new ArrayList<JitterPlus>();
		List<Jitterer> jttl = new ArrayList<Jitterer>();
		List<Friend> fl = new ArrayList<Friend>();
		List<Integer> il = new ArrayList<Integer>();
		
		fl = sf.getCurrentSession().createCriteria(Friend.class)
				.add(Restrictions.eq("jittererByJitterer.id", jit.getId()))
				.add(Restrictions.eq("follow", (short)2)).list();
		
		for (Friend fr : fl)
		{
			jttl.add( (Jitterer) sf.getCurrentSession().createCriteria(Jitterer.class)
					               .add(Restrictions.eq("id", fr.getJittererByFriend().getId())).list().get(0)); 
		}
		
		for (Jitterer j : jttl)
		{
			List<Jitter> jl = new ArrayList<Jitter>();
			
			jl = sf.getCurrentSession().createCriteria(Jitter.class)
					          .add(Restrictions.eq("jitterer.id", j.getId())).list();
			
			for (Jitter jt : jl)
			{
				JitterPlus jp = new JitterPlus();
				jp.setJitter(jt.getJitter());
				jp.setName(j.getUsername());
				jp.setId(jt.getId());
				il.add(jt.getId());
				fullList.add(jp);
			}
		}
		JitterPlusWrapper jpw = new JitterPlusWrapper();
		Collections.sort(il);
		Collections.reverse(il);
		
		for (int i = 0; i < 15; i++)
		{
			for (JitterPlus jp: fullList)
			{
				if (il.get(i) == jp.getId())
				{
					finalList.add(jp);
				}
			}
		}
		
		
		jpw.setJpl(finalList);
		
		return jpw;
	}
	
	public Jitterer searchByID(int id)
	{
		return (Jitterer) sf.getCurrentSession().createCriteria(Jitterer.class).add(Restrictions.eq("id", id)).list().get(0);
	}
	
	public void friendRequest(Jitterer j, int id)
	{
		Friend f = new Friend();
		
		f.setJittererByJitterer(j);
		f.setJittererByFriend(searchByID(id));
		f.setFollow((short)0);
		
		sf.getCurrentSession().save(f);
	}
	
	@SuppressWarnings("unchecked")
	public JittererPlusWrapper getFriendRequests(Jitterer jit)
	{
		List<Friend> lf = new ArrayList<Friend>();
		List<Jitterer> lj = new ArrayList<Jitterer>();
	
		lf = sf.getCurrentSession().createCriteria(Friend.class)
				                   .add(Restrictions.eq("jittererByFriend.id", jit.getId()))
				                   .add(Restrictions.eq("follow", (short)0)).list();
		
		for (Friend f: lf)
		{
			f.getJittererByJitterer().getUsername();
			
			lj.add(f.getJittererByJitterer());
		}
		JittererPlusWrapper jw = new JittererPlusWrapper();
		jw.setJl(lj);
		
		return jw;
	}
	
	public void friendAccept(int id2, int id)
	{
		Friend f = new Friend(); 
		
		f = (Friend) sf.getCurrentSession().createCriteria(Friend.class)
				                  .add(Restrictions.eq("jittererByJitterer.id", id))
				                  .add(Restrictions.eq("jittererByFriend.id", id2))
				                  .add(Restrictions.eq("follow", (short)0)).list().get(0);
		
		f.setFollow((short)2);
		sf.getCurrentSession().saveOrUpdate(f);
		
	}
	
	@SuppressWarnings("unchecked")
	public JitterPlusWrapper getMyJitters(Jitterer jit)
	{
		JitterWrapper jw = new JitterWrapper();
		List<JitterPlus> jpl = new ArrayList<JitterPlus>();
		jw.setJitters(sf.getCurrentSession().createCriteria(Jitter.class)
											.add(Restrictions.eq("jitterer.id", jit.getId()))
                							.addOrder(Order.desc("id"))
                							.setMaxResults(15).list());
		
		for (Jitter j: jw.getJitters())
		{
			Jitterer jt = getJittererByJitterId(j.getId());
			JitterPlus jp = new JitterPlus();
			jp.setName(jt.getUsername());
			jp.setJitter(j.getJitter());
			jpl.add(jp);
		}
		
		JitterPlusWrapper jpw = new JitterPlusWrapper();
		jpw.setJpl(jpl);
		
		return jpw;
	}
	
	public void postJitter(Jitter jt)
	{
		sf.getCurrentSession().save(jt);
	}
}
