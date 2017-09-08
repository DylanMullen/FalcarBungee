package net.falcarmc.bungee.manager;

import java.util.ArrayList;
import java.util.UUID;

import net.falcarmc.bungee.util.User;

public class UserManager
{

	static UserManager m = new UserManager();
	
	public static UserManager getManager()
	{
		return m;
	}
	
	private ArrayList<User> users = new ArrayList<>();	
	
	public User getUser(UUID uuid)
	{
		for(User u : users)
		{
			if(u.getUUID() == uuid)
			{
				return u;
			}
		}
		return null;
	}
	
	public User createUser(UUID u)
	{
		if(getUser(u) != null)
			return getUser(u);
	
		User us = new User(u);
		users.add(us);
		return us;
	}

	public void removeUser(User user)
	{
		users.remove(user);
	}

	public ArrayList<User> getUsers()
	{
		return users;
	}
	
}