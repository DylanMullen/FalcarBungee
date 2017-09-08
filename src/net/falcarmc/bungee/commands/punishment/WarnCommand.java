package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.ConfigManager;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;

public class WarnCommand extends FalcarCommand
{

	public WarnCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length < 2)
		{
			sendUsage();
			return;
		}
		
		if(ConfigManager.getManager().getUserConfig(args[0]) == null)
		{
			sendMessage("Player not found!");
			return;
		}
		
		if(args[1].equalsIgnoreCase("remove"))
		{
			if(args.length == 2)
			{
				sendMessage("You must include an index!");
				return;
			}
			int index = 0;
			try
			{
				index = Integer.parseInt(args[2]);
			}
			catch(NumberFormatException e)
			{
				sendMessage("The index must be a whole number!");
				return;
			}
			
			PunishmentManager.getInstance().removeWarn(getPlayer(), args[0], index);
			
		}
		else
		{
			StringBuilder bb = new StringBuilder();
			
			for(int i = 1; i < args.length; i++)
			{
				bb.append(args[i] + (args.length - i == 1 ? "" : " "));
			}
			
			PunishmentManager.getInstance().warn(args[0], getPlayer().getName(), bb.toString(), System.currentTimeMillis(), true);
		}
	}

}
