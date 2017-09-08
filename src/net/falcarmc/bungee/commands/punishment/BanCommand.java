package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.falcarmc.bungee.util.TimeUtil;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

public class BanCommand extends FalcarCommand
{

	public BanCommand(String name, String permission, String usage, String... aliases)
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
		
		StringBuilder bb = new StringBuilder();
		String server = "all";
		long time = -1;
		
		if(TimeUtil.parseDateOffset(args[1]) != -1)
		{
			time = System.currentTimeMillis() + TimeUtil.parseDateOffset(args[1]);
			if(args.length == 1)
			{
				sendMessage("You must include a reason!");
				return;
			}
			
			if(BungeeCord.getInstance().getServerInfo(args[2]) != null)
			{
				server = args[2];
			}
			
			for(int i = 1; i < args.length; i++)
			{
				bb.append(args[i] + (args.length - i == 1 ? "" : " "));
			}
			PunishmentManager.getInstance().ban(getPlayer(), args[0], server, bb.toString(), time);
			return;
		}
		else if(BungeeCord.getInstance().getServerInfo(args[2]) != null)
		{
			server = args[2];
			PunishmentManager.getInstance().ban(getPlayer(), args[0], server, bb.toString(), time);
			return;
		}
		else
		{
			for(int i = 1; i < args.length; i++)
			{
				bb.append(args[i] + (args.length - i == 1 ? "" : " "));
			}
			PunishmentManager.getInstance().ban(getPlayer(), args[0], server, bb.toString(), time);
			return;
		}
	}

}
