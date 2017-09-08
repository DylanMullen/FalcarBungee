package net.falcarmc.bungee.manager;

import java.util.ArrayList;
import java.util.UUID;

import net.falcarmc.bungee.util.Party;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyManager
{

	static PartyManager m = new PartyManager();

	private ArrayList<Party> parties = new ArrayList<>();

	public static PartyManager getInstance()
	{
		return m;
	}

	public boolean isNotInParty(User u)
	{
		return u.getParty() == null;
	}

	@SuppressWarnings("deprecation")
	public void createNewParty(ProxiedPlayer pp)
	{
		if (UserManager.getManager().getUser(pp.getUniqueId()).getParty() != null)
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "You are already in a party!");
			return;
		}

		parties.add(new Party(pp.getUniqueId()));
		pp.sendMessage(MessageManager.PARTY_LOGO + "You have created a party!");
	}

	@SuppressWarnings("deprecation")
	public void invitePlayer(ProxiedPlayer pp, String[] args)
	{
		if (args.length == 1)
		{
			pp.sendMessage(MessageManager.LOGO + "You must include a name!");
			return;
		}

		User u = UserManager.getManager().getUser(pp.getUniqueId());
		if (u.getParty() == null)
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "You are not in a party to invite someone to!");
			return;
		}
		u.getParty().invitePlayer(pp, args[1]);
	}

	@SuppressWarnings("deprecation")
	public void leaveParty(ProxiedPlayer pp)
	{
		User u = UserManager.getManager().getUser(pp.getUniqueId());
		if (isNotInParty(u))
		{
			pp.sendMessage(MessageManager.PARTY_LOGO + "You are not in a party!");
			return;
		}
		
		u.getParty().leaveParty(pp);

	}

	@SuppressWarnings("deprecation")
	public void joinParty(ProxiedPlayer pp, String[] args)
	{
		if(args.length == 1)
		{
			pp.sendMessage(MessageManager.LOGO + "You must include a leaders name!");
			return;
		}
		
		ProxiedPlayer t = BungeeCord.getInstance().getPlayer(args[1]);
		if(t == null)
		{
			pp.sendMessage(MessageManager.LOGO + "Player not found!");
			return;
		}
		
		getParty(t.getUniqueId()).joinParty(pp);
		
	}
	
	public Party getParty(UUID uuid)
	{
		for(Party party : parties)
		{
			if(party.getLeader().equals(uuid))
				return party;
		}
		return null;
	}

	public ArrayList<Party> getParties()
	{
		return parties;
	}
}
