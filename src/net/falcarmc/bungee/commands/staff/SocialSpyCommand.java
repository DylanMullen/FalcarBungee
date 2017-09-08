package net.falcarmc.bungee.commands.staff;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.md_5.bungee.api.CommandSender;

public class SocialSpyCommand extends FalcarCommand
{

	public SocialSpyCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(getUser().isSpying())
		{
			getUser().toggleSpy(false);
		}
		else
		{
			getUser().toggleSpy(true);
		}
		
		sendMessage("You have turned " + (getUser().isSpying() ? "on" : "off") + " social spy!");
		return;
	}


}
