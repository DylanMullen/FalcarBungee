package net.falcarmc.bungee.runnables;

import java.util.UUID;

import net.falcarmc.bungee.manager.PartyManager;
import net.falcarmc.bungee.util.Party;

public class PartyRunnable implements Runnable
{

	public PartyRunnable()
	{
	}

	@Override
	public void run()
	{
		for(Party p : PartyManager.getInstance().getParties())
		{
			for(UUID uuid : p.getInvited().keySet())
			{
				if(System.currentTimeMillis() - p.getInvited().get(uuid) >= (60*1000))
				{
					p.removeInvite(uuid);
				}
			}
		}
	}

}
