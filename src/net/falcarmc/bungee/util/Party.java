package net.falcarmc.bungee.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.falcarmc.bungee.manager.MessageManager;
import net.falcarmc.bungee.manager.PartyManager;
import net.falcarmc.bungee.manager.UserManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Party
{

	private UUID leader;
	private ArrayList<UUID> users = new ArrayList<>();
	private HashMap<UUID, Long> invited = new HashMap<>();
	
	public Party(UUID leader)
	{
		this.leader = leader;
		users.add(leader);
		UserManager.getManager().getUser(leader).setParty(this);
	}
	
	@SuppressWarnings("deprecation")
	public void invitePlayer(ProxiedPlayer sender, String name)
	{
		if(!isPartyLeader(sender.getUniqueId()))
		{
			sender.sendMessage(MessageManager.PARTY_LOGO + "You must be the party leader!");
		}
		
		ProxiedPlayer target = BungeeCord.getInstance().getPlayer(name);
		if(target == null)
		{
			sender.sendMessage(MessageManager.PARTY_LOGO + "Player not found!");
			return;
		}
		
		User us = UserManager.getManager().getUser(target.getUniqueId());
		
		if(users.contains(target.getUniqueId()))
		{
			sender.sendMessage(MessageManager.PARTY_LOGO + "Player already in the party!");
			return;
		}
		
		if(isInvited(target.getUniqueId()))
		{
			sender.sendMessage(MessageManager.PARTY_LOGO + "Player already invited!");
			return;
		}
		
		if(PartyManager.getInstance().isNotInParty(us))
		{
			sender.sendMessage(MessageManager.PARTY_LOGO + "Player is already in a party!");
			return;
		}
		
		invited.put(target.getUniqueId(), System.currentTimeMillis());
		target.sendMessage(MessageManager.PARTY_LOGO + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + " has invited you to their party. You have 60 seconds to join!");
		sender.sendMessage(MessageManager.PARTY_LOGO + "Invite sent!");
		return;
	}

	@SuppressWarnings("deprecation")
	public void joinParty(ProxiedPlayer pp)
	{
		if(!isInvited(pp.getUniqueId()))
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "You have not been invited to the party!");
			return;
		}
		
		if(!PartyManager.getInstance().isNotInParty(UserManager.getManager().getUser(pp.getUniqueId())))
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "You are already in a party!");
			return;
		}
		
		invited.remove(pp.getUniqueId());
		users.add(pp.getUniqueId());
		UserManager.getManager().getUser(pp.getUniqueId()).setParty(this);
		
		sendMessage(MessageManager.PARTY_LOGO + ChatColor.YELLOW + pp.getName() + ChatColor.RESET + " has joined the party!");
		return;
	}
	
	public void leaveParty(ProxiedPlayer pp)
	{
		User u = UserManager.getManager().getUser(pp.getUniqueId());
		boolean newLeader = false;

		if(isPartyLeader(pp.getUniqueId()))
		{
			if(users.size() == 1)
			{
				u.setParty(null);
				disbandParty();
			}
			newLeader = true;
		}
		
		if(newLeader)
		{
			u.setParty(null);
			users.remove(pp.getUniqueId());
			selectNewLeader();
			
			sendMessage(MessageManager.PARTY_LOGO + "The leader has left and leadership has been given to: " + BungeeCord.getInstance().getPlayer(leader).getName());
		}
		else
		{
			u.setParty(null);
			users.remove(pp.getUniqueId());
			sendMessage(MessageManager.PARTY_LOGO + ChatColor.YELLOW + pp.getName() + " has left the party!");
			return;
		}
	}
	
	public void selectNewLeader()
	{
		for(UUID uuid : users)
		{
			if(uuid.equals(leader))
				continue;
			if(BungeeCord.getInstance().getPlayer(uuid) != null)
			{
				leader = uuid;
				break;
			}
		}
	}
	
	
	private void disbandParty()
	{
		PartyManager.getInstance().getParties().remove(this);
		sendMessage("Party disbanded!");
		return;
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(String mes)
	{
		for(UUID uuid : users)
		{
			ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(uuid);
			if(pp.isConnected())
			{
				pp.sendMessage(mes);
			}
		}
	}
	
	public HashMap<UUID, Long> getInvited()
	{
		return invited;
	}
	
	public boolean isInvited(UUID uuid)
	{
		return invited.containsKey(uuid);
	}
	
	public boolean isPartyLeader(UUID uuid)
	{
		return uuid.equals(leader);
	}

	public UUID getLeader()
	{
		return leader;
	}

	@SuppressWarnings("deprecation")
	public void removeInvite(UUID uuid)
	{
		invited.remove(uuid);
		
		ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(uuid);
		ProxiedPlayer leader = BungeeCord.getInstance().getPlayer(getLeader());
		if(pp.isConnected())
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "Your invite to " + leader.getName() + "'s party has expired!");
			return;
		}
	}
}
