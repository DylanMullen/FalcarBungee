package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;

public class KickCommand extends FalcarCommand
{
	
	public KickCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length < 2)
		{
			sendUsage();
			return;
		}
		
		StringBuilder bb = new StringBuilder();
		
		for(int i = 1; i < args.length; i++)
		{
			bb.append(args[i] + (args.length - i == 1 ? "" : " "));
		}
		
		PunishmentManager.getInstance().kick(getPlayer(), args[0], bb.toString());
		
	}

}
